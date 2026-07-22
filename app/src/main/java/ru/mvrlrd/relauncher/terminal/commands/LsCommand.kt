package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult
import ru.mvrlrd.relauncher.vfs.PathResolver

class LsCommand : Command {
    override val name = "ls"
    override val help = "list directory contents"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult {
        val target = if (args.isEmpty()) {
            ctx.session.cwd
        } else {
            val path = args.first()
            PathResolver.resolve(ctx.session.cwd, path)
                ?: return CommandResult.error("ls: no such directory: $path")
        }
        val names = target.children.keys.sorted()
        return CommandResult.ok(names)
    }
}
