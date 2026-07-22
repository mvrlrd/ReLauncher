package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult
import ru.mvrlrd.relauncher.vfs.PathResolver

class CdCommand : Command {
    override val name = "cd"
    override val help = "change current directory"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult {
        if (args.isEmpty()) {
            ctx.session.cwd = ctx.session.tree.root
            return CommandResult.ok()
        }
        val path = args.first()
        val target = PathResolver.resolve(ctx.session.cwd, path)
            ?: return CommandResult.error("cd: no such directory: $path")
        ctx.session.cwd = target
        return CommandResult.ok()
    }
}
