package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult

class PwdCommand : Command {
    override val name = "pwd"
    override val help = "print current directory"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult {
        return CommandResult.ok(ctx.session.cwd.path)
    }
}
