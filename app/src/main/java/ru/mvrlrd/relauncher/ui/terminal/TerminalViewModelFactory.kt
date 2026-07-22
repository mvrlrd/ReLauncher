package ru.mvrlrd.relauncher.ui.terminal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mvrlrd.relauncher.terminal.Executor

class TerminalViewModelFactory(
    private val executor: Executor,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TerminalViewModel(executor) as T
}
