package ru.mvrlrd.relauncher.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.mvrlrd.relauncher.data.apps.PackageManagerAppRepository
import ru.mvrlrd.relauncher.domain.repository.AppRepository
import ru.mvrlrd.relauncher.domain.usecase.GetInstalledAppsUseCase
import ru.mvrlrd.relauncher.domain.usecase.LaunchAppUseCase
import ru.mvrlrd.relauncher.terminal.CommandRegistry
import ru.mvrlrd.relauncher.terminal.Executor
import ru.mvrlrd.relauncher.terminal.Session
import ru.mvrlrd.relauncher.terminal.commands.CdCommand
import ru.mvrlrd.relauncher.terminal.commands.ClearCommand
import ru.mvrlrd.relauncher.terminal.commands.HelpCommand
import ru.mvrlrd.relauncher.terminal.commands.LsCommand
import ru.mvrlrd.relauncher.terminal.commands.PwdCommand
import ru.mvrlrd.relauncher.terminal.commands.RunCommand
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(impl: PackageManagerAppRepository): AppRepository

    companion object {

        @Provides
        @Singleton
        fun provideCommandRegistry(launchApp: LaunchAppUseCase): CommandRegistry =
            CommandRegistry().apply {
                register(HelpCommand())
                register(PwdCommand())
                register(LsCommand())
                register(CdCommand())
                register(ClearCommand())
                register(RunCommand(launchApp))
            }

        @Provides
        @Singleton
        fun provideSession(getInstalledApps: GetInstalledAppsUseCase): Session {
            val session = Session()
            val appsNode = session.tree.root.child("apps")
            if (appsNode != null) {
                for (app in getInstalledApps()) {
                    appsNode.addChild(app.slug)
                }
            }
            return session
        }

        @Provides
        @Singleton
        fun provideExecutor(registry: CommandRegistry, session: Session): Executor =
            Executor(registry, session)
    }
}
