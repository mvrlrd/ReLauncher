package ru.mvrlrd.relauncher.terminal

/** Command execution result: success/error + output lines. */
data class CommandResult(
    val output: List<String> = emptyList(),
    val isError: Boolean = false,
) {
    companion object {
        fun ok(vararg lines: String) = CommandResult(lines.toList(), isError = false)
        fun ok(lines: List<String>) = CommandResult(lines, isError = false)
        fun error(vararg lines: String) = CommandResult(lines.toList(), isError = true)
    }
}

/** Execution context. Extended in task 3 (cwd) and beyond. */
interface CommandContext {
    val registry: CommandRegistry
}

interface Command {
    val name: String
    val help: String
    fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult
}
