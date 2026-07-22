package ru.mvrlrd.relauncher.terminal

import ru.mvrlrd.relauncher.terminal.lexer.Lexer
import ru.mvrlrd.relauncher.terminal.lexer.LexerException
import ru.mvrlrd.relauncher.terminal.parser.Parser
import ru.mvrlrd.relauncher.terminal.parser.ParserException

class Executor(
    override val registry: CommandRegistry,
    override val session: Session = Session(),
    private val lexer: Lexer = Lexer(),
    private val parser: Parser = Parser(),
) : CommandContext {

    fun execute(line: String): CommandResult {
        val node = try {
            parser.parse(lexer.tokenize(line))
        } catch (e: LexerException) {
            return CommandResult.error("syntax error: ${e.message}")
        } catch (e: ParserException) {
            return CommandResult.error("syntax error: ${e.message}")
        } ?: return CommandResult.ok()

        val command = registry.resolve(node.name)
            ?: return CommandResult.error("${node.name}: command not found")

        return command.execute(this, node.args, node.flags)
    }
}
