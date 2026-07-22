package ru.mvrlrd.relauncher.terminal.parser

sealed interface AstNode

/** Simple command: name + positional arguments + flags. */
data class CommandNode(
    val name: String,
    val args: List<String>,
    val flags: List<String>,
) : AstNode

/**
 * Reserved for the future: command pipeline (`cmd1 | cmd2`) and chains (`&&`).
 * Not produced by the parser in the MVP, but reserved in the AST.
 */
data class PipelineNode(
    val commands: List<CommandNode>,
) : AstNode
