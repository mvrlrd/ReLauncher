package ru.mvrlrd.relauncher.terminal.commands

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.mvrlrd.relauncher.domain.model.AppInfo
import ru.mvrlrd.relauncher.domain.model.GroupApp
import ru.mvrlrd.relauncher.domain.model.GroupDir
import ru.mvrlrd.relauncher.domain.repository.AppRepository
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import ru.mvrlrd.relauncher.domain.usecase.FindAppUseCase
import ru.mvrlrd.relauncher.domain.usecase.LinkAppToGroupUseCase
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandRegistry
import ru.mvrlrd.relauncher.terminal.Session
import ru.mvrlrd.relauncher.vfs.PathResolver

class LnCommandTest {

    private class FakeAppRepository : AppRepository {
        val apps = listOf(
            AppInfo("org.telegram.messenger", "Telegram", "telegram"),
            AppInfo("com.whatsapp", "WhatsApp", "whatsapp"),
        )
        override fun getInstalledApps(): List<AppInfo> = apps
        override fun findByName(name: String): AppInfo? =
            apps.firstOrNull { it.slug == name || it.packageName == name }
        override fun launch(app: AppInfo): Boolean = true
    }

    private class FakeGroupsRepository : GroupsRepository {
        val stored = mutableListOf<GroupApp>()
        override fun getAll(): List<GroupDir> = emptyList()
        override fun add(dir: GroupDir) {}
        override fun getApps(): List<GroupApp> = stored
        override fun addApp(app: GroupApp) {
            stored.add(app)
        }
    }

    private lateinit var groupsRepo: FakeGroupsRepository
    private lateinit var command: LnCommand
    private lateinit var session: Session

    private val ctx: CommandContext
        get() = object : CommandContext {
            override val registry = CommandRegistry()
            override val session = this@LnCommandTest.session
        }

    private fun setUp() {
        groupsRepo = FakeGroupsRepository()
        command = LnCommand(
            FindAppUseCase(FakeAppRepository()),
            LinkAppToGroupUseCase(groupsRepo),
        )
        session = Session()
        session.tree.root.child("groups")!!.addChild("social")
    }

    @Test
    fun linksAppIntoCwdGroup() {
        setUp()
        session.cwd = PathResolver.resolve(session.tree.root, "/groups/social")!!

        val result = command.execute(ctx, listOf("telegram"), emptyList())

        assertTrue(!result.isError)
        assertNotNull(PathResolver.resolve(session.tree.root, "/groups/social/telegram"))
        assertEquals(1, groupsRepo.stored.size)
        assertEquals("/groups/social", groupsRepo.stored[0].groupPath)
        assertEquals("org.telegram.messenger", groupsRepo.stored[0].packageName)
    }

    @Test
    fun failsWhenAlreadyLinked() {
        setUp()
        session.cwd = PathResolver.resolve(session.tree.root, "/groups/social")!!
        command.execute(ctx, listOf("telegram"), emptyList())

        val result = command.execute(ctx, listOf("telegram"), emptyList())

        assertTrue(result.isError)
    }

    @Test
    fun failsWhenAppNotFound() {
        setUp()
        session.cwd = PathResolver.resolve(session.tree.root, "/groups/social")!!

        val result = command.execute(ctx, listOf("nonexistent"), emptyList())

        assertTrue(result.isError)
    }

    @Test
    fun failsOutsideGroups() {
        setUp()
        session.cwd = PathResolver.resolve(session.tree.root, "/apps")!!

        val result = command.execute(ctx, listOf("telegram"), emptyList())

        assertTrue(result.isError)
    }

    @Test
    fun linksWithExplicitTargetArgument() {
        setUp()

        val result = command.execute(ctx, listOf("telegram", "/groups/social"), emptyList())

        assertTrue(!result.isError)
        assertNotNull(PathResolver.resolve(session.tree.root, "/groups/social/telegram"))
        assertEquals(1, groupsRepo.stored.size)
    }
}
