package ru.mvrlrd.relauncher.terminal.lexer

class LexerException(message: String) : Exception(message)

/**
 * Splits a string into tokens: words, flags (-x / --long) and quoted strings ('...' / "...").
 * Whitespace outside quotes acts as a separator. Quotes are stripped and their content becomes a STRING token.
 */
class Lexer {

    fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        val n = input.length

        while (i < n) {
            val c = input[i]
            when {
                c.isWhitespace() -> i++
                c == '\'' || c == '"' -> {
                    val quote = c
                    val sb = StringBuilder()
                    i++
                    while (i < n && input[i] != quote) {
                        sb.append(input[i]); i++
                    }
                    if (i >= n) throw LexerException("unterminated quote")
                    i++
                    tokens.add(Token(TokenType.STRING, sb.toString()))
                }
                else -> {
                    val sb = StringBuilder()
                    while (i < n && !input[i].isWhitespace() && input[i] != '\'' && input[i] != '"') {
                        sb.append(input[i]); i++
                    }
                    val word = sb.toString()
                    val type = if (word.startsWith("-") && word.length > 1) TokenType.FLAG else TokenType.WORD
                    tokens.add(Token(type, word))
                }
            }
        }
        return tokens
    }
}
