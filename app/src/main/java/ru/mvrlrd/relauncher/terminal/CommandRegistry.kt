package ru.mvrlrd.relauncher.terminal

class CommandRegistry {

    private val commands = LinkedHashMap<String, Command>()

    fun register(command: Command) {
        commands[command.name] = command
    }

    fun resolve(name: String): Command? = commands[name]

    fun all(): List<Command> = commands.values.toList()
}
