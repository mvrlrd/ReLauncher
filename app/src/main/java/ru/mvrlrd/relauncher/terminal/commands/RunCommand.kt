package ru.mvrlrd.relauncher.terminal.commands

import ru.mvrlrd.relauncher.domain.usecase.LaunchAppUseCase
import ru.mvrlrd.relauncher.terminal.Command
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandResult

class RunCommand(
    private val launchApp: LaunchAppUseCase,
) : Command {
    override val name = "run"
    override val help = "launch app"

    override fun execute(ctx: CommandContext, args: List<String>, flags: List<String>): CommandResult {
        val name = args.firstOrNull()
            ?: return CommandResult.error("run: missing app name")

        return when (val result = launchApp(name)) {
            is LaunchAppUseCase.Result.Launched -> CommandResult.ok("launching ${result.label}...")
            is LaunchAppUseCase.Result.NotFound -> CommandResult.error("run: app not found: ${result.name}")
            is LaunchAppUseCase.Result.NoLaunchIntent ->
                CommandResult.error("run: cannot launch ${result.label}")
        }
    }
}
