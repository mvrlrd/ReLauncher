package ru.mvrlrd.relauncher.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.mvrlrd.relauncher.data.apps.PackageManagerAppRepository
import ru.mvrlrd.relauncher.data.db.AppDatabase
import ru.mvrlrd.relauncher.data.db.GroupAppDao
import ru.mvrlrd.relauncher.data.db.GroupDirDao
import ru.mvrlrd.relauncher.data.groups.RoomGroupsRepository
import ru.mvrlrd.relauncher.domain.repository.AppRepository
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import ru.mvrlrd.relauncher.domain.usecase.CreateGroupDirUseCase
import ru.mvrlrd.relauncher.domain.usecase.FindAppUseCase
import ru.mvrlrd.relauncher.domain.usecase.GetGroupAppsUseCase
import ru.mvrlrd.relauncher.domain.usecase.GetGroupDirsUseCase
import ru.mvrlrd.relauncher.domain.usecase.GetInstalledAppsUseCase
import ru.mvrlrd.relauncher.domain.usecase.LaunchAppUseCase
import ru.mvrlrd.relauncher.domain.usecase.LinkAppToGroupUseCase
import ru.mvrlrd.relauncher.terminal.CommandRegistry
import ru.mvrlrd.relauncher.terminal.Executor
import ru.mvrlrd.relauncher.terminal.Session
import ru.mvrlrd.relauncher.terminal.commands.CdCommand
import ru.mvrlrd.relauncher.terminal.commands.ClearCommand
import ru.mvrlrd.relauncher.terminal.commands.HelpCommand
import ru.mvrlrd.relauncher.terminal.commands.LnCommand
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
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()

        @Provides
        @Singleton
        fun provideGroupDirDao(db: AppDatabase): GroupDirDao = db.groupDirDao()

        @Provides
        @Singleton
        fun provideGroupAppDao(db: AppDatabase): GroupAppDao = db.groupAppDao()

        @Provides
        @Singleton
        fun provideCommandRegistry(
            launchApp: LaunchAppUseCase,
            createGroupDir: CreateGroupDirUseCase,
            findApp: FindAppUseCase,
            linkApp: LinkAppToGroupUseCase,
        ): CommandRegistry =
            CommandRegistry().apply {
                register(HelpCommand())
                register(PwdCommand())
                register(LsCommand())
                register(CdCommand())
                register(ClearCommand())
                register(RunCommand(launchApp))
                register(MkdirCommand(createGroupDir))
                register(LnCommand(findApp, linkApp))
            }

        @Provides
        @Singleton
        fun provideSession(
            getInstalledApps: GetInstalledAppsUseCase,
            getGroupDirs: GetGroupDirsUseCase,
            getGroupApps: GetGroupAppsUseCase,
        ): Session {
            val session = Session()
            val apps = getInstalledApps()
            val appsNode = session.tree.root.child("apps")
            if (appsNode != null) {
                for (app in apps) {
                    appsNode.addChild(app.slug)
                }
            }
            val dirs = getGroupDirs().sortedBy { it.path.count { c -> c == '/' } }
            for (dir in dirs) {
                PathResolver.resolve(session.tree.root, dir.parentPath)?.addChild(dir.name)
            }
            val byPkg = apps.associateBy { it.packageName }
            for (ga in getGroupApps()) {
                val node = PathResolver.resolve(session.tree.root, ga.groupPath) ?: continue
                val appInfo = byPkg[ga.packageName] ?: continue
                if (node.child(appInfo.slug) == null) {
                    node.addChild(appInfo.slug)
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
