package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult

class HelpCommand : Command {
    override val name = "help"
    override val help = "list available commands"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult {
        val lines = ctx.registry.all()
            .sortedBy { it.name }
            .map { "${it.name.padEnd(10)} ${it.help}" }
        return CommandResult.ok(lines)
    }
}
