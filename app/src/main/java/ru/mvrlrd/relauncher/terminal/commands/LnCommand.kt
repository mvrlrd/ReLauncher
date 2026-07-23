package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.domain.model.GroupApp
import ru.mvrlrd.relauncher.domain.usecase.FindAppUseCase
import ru.mvrlrd.relauncher.domain.usecase.LinkAppToGroupUseCase
import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult
import ru.mvrlrd.relauncher.vfs.PathResolver

class LnCommand(
    private val findApp: FindAppUseCase,
    private val linkApp: LinkAppToGroupUseCase,
) : Command {
    override val name = "ln"
    override val help = "link an app into a /groups folder"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult {
        val appName = args.getOrNull(0)
            ?: return CommandResult.error("ln: missing app name")

        val groupArg = args.getOrNull(1)
        val target = if (groupArg == null) {
            ctx.session.cwd
        } else {
            PathResolver.resolve(ctx.session.cwd, groupArg)
                ?: return CommandResult.error("ln: no such directory: $groupArg")
        }

        if (target.path != "/groups" && !target.path.startsWith("/groups/")) {
            return CommandResult.error("ln: target must be under /groups")
        }

        val app = findApp(appName)
            ?: return CommandResult.error("ln: app not found: $appName")

        if (target.child(app.slug) != null) {
            return CommandResult.error("ln: '${app.slug}' already in ${target.path}")
        }

        target.addChild(app.slug)
        linkApp(GroupApp(target.path, app.packageName))
        return CommandResult.ok()
    }
}
