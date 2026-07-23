package ru.mvrlrd.relauncher.terminal.commands

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.mvrlrd.relauncher.domain.model.GroupDir
import ru.mvrlrd.relauncher.domain.repository.GroupsRepository
import ru.mvrlrd.relauncher.domain.usecase.CreateGroupDirUseCase
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandRegistry
import ru.mvrlrd.relauncher.terminal.Session
import ru.mvrlrd.relauncher.vfs.PathResolver

class MkdirCommandTest {

    private class FakeGroupsRepository : GroupsRepository {
        val stored = mutableListOf<GroupDir>()
        override fun getAll(): List<GroupDir> = stored
        override fun add(dir: GroupDir) {
            stored.add(dir)
        }
    }

    private lateinit var repo: FakeGroupsRepository
    private lateinit var command: MkdirCommand
    private lateinit var session: Session

    private val ctx: CommandContext
        get() = object : CommandContext {
            override val registry = CommandRegistry()
            override val session = this@MkdirCommandTest.session
        }

    private fun setUp() {
        repo = FakeGroupsRepository()
        command = MkdirCommand(CreateGroupDirUseCase(repo))
        session = Session()
    }

    @Test
    fun createsDirectoryUnderGroups() {
        setUp()
        session.cwd = PathResolver.resolve(session.tree.root, "/groups")!!

        val result = command.execute(ctx, listOf("social"), emptyList())

        assertTrue(!result.isError)
        assertNotNull(session.cwd.child("social"))
        assertEquals(1, repo.stored.size)
        assertEquals("/groups/social", repo.stored[0].path)
    }

    @Test
    fun failsWhenAlreadyExists() {
        setUp()
        session.cwd = PathResolver.resolve(session.tree.root, "/groups")!!
        command.execute(ctx, listOf("social"), emptyList())

        val result = command.execute(ctx, listOf("social"), emptyList())

        assertTrue(result.isError)
    }

    @Test
    fun failsOutsideGroups() {
        setUp()
        session.cwd = PathResolver.resolve(session.tree.root, "/apps")!!

        val result = command.execute(ctx, listOf("x"), emptyList())

        assertTrue(result.isError)
    }

    @Test
    fun failsWhenParentMissing() {
        setUp()

        val result = command.execute(ctx, listOf("/groups/nope/x"), emptyList())

        assertTrue(result.isError)
    }
}
