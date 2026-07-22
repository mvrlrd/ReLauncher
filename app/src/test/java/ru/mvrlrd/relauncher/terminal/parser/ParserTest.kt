package ru.mvrlrd.relauncher.terminal.parser

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import ru.mvrlrd.relauncher.terminal.lexer.Lexer
import org.junit.Test

class ParserTest {

    private val lexer = Lexer()
    private val parser = Parser()

    private fun parse(input: String) = parser.parse(lexer.tokenize(input))

    @Test
    fun emptyInputParsesToNull() {
        assertNull(parse(""))
        assertNull(parse("   "))
    }

    @Test
    fun simpleCommandWithArgs() {
        val node = parse("cd apps")
        assertEquals(CommandNode("cd", listOf("apps"), emptyList()), node)
    }

    @Test
    fun separatesArgsFromFlags() {
        val node = parse("ls apps -l --all")
        assertEquals("ls", node!!.name)
        assertEquals(listOf("apps"), node.args)
        assertEquals(listOf("-l", "--all"), node.flags)
    }

    @Test
    fun quotedStringBecomesArg() {
        val node = parse("mkdir 'my dir'")
        assertEquals(listOf("my dir"), node!!.args)
    }

    @Test
    fun leadingFlagThrows() {
        assertThrows(ParserException::class.java) { parse("-l apps") }
    }
}
