package ru.mvrlrd.relauncher.ui.terminal

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.mvrlrd.relauncher.terminal.CommandRegistry
import ru.mvrlrd.relauncher.terminal.Executor
import ru.mvrlrd.relauncher.terminal.commands.HelpCommand

class TerminalViewModel : ViewModel() {

    private val _state = MutableStateFlow(TerminalState())
    val state: StateFlow<TerminalState> = _state.asStateFlow()

    private val executor: Executor = Executor(
        CommandRegistry().apply {
            register(HelpCommand())
        },
    )

    fun onInputChange(value: String) {
        _state.update { it.copy(input = value) }
    }

    fun onSubmit() {
        val current = _state.value
        val command = current.input
        if (command.isBlank()) return

        val result = executor.execute(command)
        val outputType = if (result.isError) LineType.ERROR else LineType.OUTPUT
        val newLines = current.lines +
            TerminalLine(current.prompt + command, LineType.PROMPT) +
            result.output.map { TerminalLine(it, outputType) }

        _state.update {
            it.copy(
                lines = newLines,
                input = "",
                history = it.history + command,
                historyIndex = -1,
            )
        }
    }

    fun historyPrev() {
        val current = _state.value
        if (current.history.isEmpty()) return
        val newIndex = if (current.historyIndex == -1) {
            current.history.lastIndex
        } else {
            (current.historyIndex - 1).coerceAtLeast(0)
        }
        _state.update {
            it.copy(input = current.history[newIndex], historyIndex = newIndex)
        }
    }

    fun historyNext() {
        val current = _state.value
        if (current.historyIndex == -1) return
        val newIndex = current.historyIndex + 1
        if (newIndex > current.history.lastIndex) {
            _state.update { it.copy(input = "", historyIndex = -1) }
        } else {
            _state.update { it.copy(input = current.history[newIndex], historyIndex = newIndex) }
        }
    }
}
