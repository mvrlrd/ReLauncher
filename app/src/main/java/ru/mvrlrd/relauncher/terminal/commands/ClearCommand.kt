package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult

class ClearCommand : Command {
    override val name = "clear"
    override val help = "clear the screen"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult =
        CommandResult.clear()
}
