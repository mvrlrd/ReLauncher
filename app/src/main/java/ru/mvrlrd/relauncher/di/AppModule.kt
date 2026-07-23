package ru.mvrlrd.relauncher.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.mvrlrd.relauncher.data.apps.PackageManagerAppRepository
import ru.mvrlrd.relauncher.data.db.AppDatabase
import ru.mvrlrd.relauncher.data.db.GroupDirDao
import ru.mvrlrd.relauncher.data.groups.RoomGroupsRepository
import ru.mvrlrd.relauncher.domain.repository.AppRepository
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import ru.mvrlrd.relauncher.domain.usecase.CreateGroupDirUseCase
import ru.mvrlrd.relauncher.domain.usecase.GetGroupDirsUseCase
import ru.mvrlrd.relauncher.domain.usecase.GetInstalledAppsUseCase
import ru.mvrlrd.relauncher.domain.usecase.LaunchAppUseCase
import ru.mvrlrd.relauncher.terminal.CommandRegistry
import ru.mvrlrd.relauncher.terminal.Executor
import ru.mvrlrd.relauncher.terminal.Session
import ru.mvrlrd.relauncher.terminal.commands.CdCommand
import ru.mvrlrd.relauncher.terminal.commands.HelpCommand
import ru.mvrlrd.relauncher.terminal.commands.LsCommand
import ru.mvrlrd.relauncher.terminal.commands.MkdirCommand
import ru.mvrlrd.relauncher.terminal.commands.PwdCommand
import ru.mvrlrd.relauncher.terminal.commands.RunCommand
import ru.mvrlrd.relauncher.vfs.PathResolver
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(impl: PackageManagerAppRepository): AppRepository

    @Binds
    @Singleton
    abstract fun bindGroupsRepository(impl: RoomGroupsRepository): GroupsRepository

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "relauncher.db")
                .allowMainThreadQueries()
                .build()

        @Provides
        @Singleton
        fun provideGroupDirDao(db: AppDatabase): GroupDirDao = db.groupDirDao()

        @Provides
        @Singleton
        fun provideCommandRegistry(
            launchApp: LaunchAppUseCase,
            createGroupDir: CreateGroupDirUseCase,
        ): CommandRegistry =
            CommandRegistry().apply {
                register(HelpCommand())
                register(PwdCommand())
                register(LsCommand())
                register(CdCommand())
                register(RunCommand(launchApp))
                register(MkdirCommand(createGroupDir))
            }

        @Provides
        @Singleton
        fun provideSession(
            getInstalledApps: GetInstalledAppsUseCase,
            getGroupDirs: GetGroupDirsUseCase,
        ): Session {
            val session = Session()
            val appsNode = session.tree.root.child("apps")
            if (appsNode != null) {
                for (app in getInstalledApps()) {
                    appsNode.addChild(app.slug)
                }
            }
            val dirs = getGroupDirs().sortedBy { it.path.count { c -> c == '/' } }
            for (dir in dirs) {
                PathResolver.resolve(session.tree.root, dir.parentPath)?.addChild(dir.name)
            }
            return session
        }

        @Provides
        @Singleton
        fun provideExecutor(registry: CommandRegistry, session: Session): Executor =
            Executor(registry, session)
    }
}
