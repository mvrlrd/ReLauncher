package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.domain.model.GroupDir
import ru.mvrlrd.relauncher.domain.usecase.CreateGroupDirUseCase
import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult
import ru.mvrlrd.relauncher.vfs.PathResolver

class MkdirCommand(
    private val createGroupDir: CreateGroupDirUseCase,
) : Command {
    override val name = "mkdir"
    override val help = "create a directory under /groups"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult {
        val arg = args.firstOrNull()
            ?: return CommandResult.error("mkdir: missing directory name")

        val slashIndex = arg.lastIndexOf('/')
        val name = arg.substring(slashIndex + 1)
        val parentPath = if (slashIndex < 0) "." else arg.substring(0, slashIndex).ifEmpty { "/" }

        if (name.isEmpty()) {
            return CommandResult.error("mkdir: cannot create '$name': already exists")
        }

        val parent = PathResolver.resolve(ctx.session.cwd, parentPath)
            ?: return CommandResult.error("mkdir: no such directory: $parentPath")

        if (parent.path != "/groups" && !parent.path.startsWith("/groups/")) {
            return CommandResult.error("mkdir: can only create directories under /groups")
        }

        if (parent.child(name) != null) {
            return CommandResult.error("mkdir: cannot create '$name': already exists")
        }

        val node = parent.addChild(name)
        createGroupDir(GroupDir(node.path, name, parent.path))
        return CommandResult.ok()
    }
}
