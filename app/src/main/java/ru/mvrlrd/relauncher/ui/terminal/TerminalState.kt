package ru.mvrlrd.relauncher.ui.terminal

enum class LineType { PROMPT, OUTPUT, ERROR }

data class TerminalLine(
    val text: String,
    val type: LineType,
)

data class TerminalState(
    val lines: List<TerminalLine> = emptyList(),
    val input: String = "",
    val prompt: String = "$ ",
    val history: List<String> = emptyList(),
    val historyIndex: Int = -1,
)
