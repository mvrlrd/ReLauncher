package ru.mvrlrd.relauncher.terminal.lexer

enum class TokenType { WORD, FLAG, STRING }

data class Token(
    val type: TokenType,
    val value: String,
)
