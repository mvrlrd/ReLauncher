package ru.mvrlrd.relauncher.terminal.parser

import ru.mvrlrd.relauncher.terminal.lexer.Token
import ru.mvrlrd.relauncher.terminal.lexer.TokenType

class ParserException(message: String) : Exception(message)

/**
 * Tokens -> AST. In the MVP only a simple [CommandNode] is produced.
 * Pipes/chains will be added here without changing the interface.
 */
class Parser {

    fun parse(tokens: List<Token>): CommandNode? {
        if (tokens.isEmpty()) return null

        val first = tokens.first()
        if (first.type == TokenType.FLAG) {
            throw ParserException("expected command name, got flag '${first.value}'")
        }
        val name = first.value

        val args = mutableListOf<String>()
        val flags = mutableListOf<String>()
        for (token in tokens.drop(1)) {
            when (token.type) {
                TokenType.FLAG -> flags.add(token.value)
                TokenType.WORD, TokenType.STRING -> args.add(token.value)
            }
        }
        return CommandNode(name = name, args = args, flags = flags)
    }
}
