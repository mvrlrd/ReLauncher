package ru.mvrlrd.relauncher.terminal.commands

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.mvrlrd.relauncher.terminal.CommandContext
import ru.mvrlrd.relauncher.terminal.CommandRegistry
import ru.mvrlrd.relauncher.terminal.Session

class ClearCommandTest {

    private val ctx = object : CommandContext {
        override val registry = CommandRegistry()
        override val session = Session()
    }

    @Test
    fun returnsClearScreenResult() {
        val result = ClearCommand().execute(ctx, emptyList(), emptyList())
        assertTrue(result.clearScreen)
        assertEquals(emptyList<String>(), result.output)
    }
}
