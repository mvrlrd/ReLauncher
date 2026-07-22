package ru.mvrlrd.relauncher.terminal.lexer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class LexerTest {

    private val lexer = Lexer()

    @Test
    fun emptyInputProducesNoTokens() {
        assertTrue(lexer.tokenize("").isEmpty())
        assertTrue(lexer.tokenize("   ").isEmpty())
    }

    @Test
    fun splitsWordsAndArgs() {
        val tokens = lexer.tokenize("cd apps")
        assertEquals(
            listOf(
                Token(TokenType.WORD, "cd"),
                Token(TokenType.WORD, "apps"),
            ),
            tokens,
        )
    }

    @Test
    fun recognizesShortAndLongFlags() {
        val tokens = lexer.tokenize("ls -l --all")
        assertEquals(TokenType.WORD, tokens[0].type)
        assertEquals(TokenType.FLAG, tokens[1].type)
        assertEquals("-l", tokens[1].value)
        assertEquals(TokenType.FLAG, tokens[2].type)
        assertEquals("--all", tokens[2].value)
    }

    @Test
    fun singleAndDoubleQuotesBecomeStringTokens() {
        val tokens = lexer.tokenize("mkdir 'my dir' \"other dir\"")
        assertEquals(Token(TokenType.STRING, "my dir"), tokens[1])
        assertEquals(Token(TokenType.STRING, "other dir"), tokens[2])
    }

    @Test
    fun loneDashIsWord() {
        val tokens = lexer.tokenize("cd -")
        assertEquals(Token(TokenType.WORD, "-"), tokens[1])
    }

    @Test
    fun unterminatedQuoteThrows() {
        assertThrows(LexerException::class.java) { lexer.tokenize("echo 'oops") }
    }
}
