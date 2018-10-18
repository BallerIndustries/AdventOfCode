package Year2016

typealias State = Map<String, Set<Int>>
interface Command
data class InitialCommand(val botName: String, val value: Int) : Command
data class DistributeCommand(val botName: String, val highRecipientName: String, val lowRecipientName: String) : Command

class Puzzle10 {
    fun parseToCommands(commandText: String): List<Command> {
        return commandText.split("\n").map { command ->
            val temp = command.split(' ')

            if (command.startsWith("value")) {
                val value = temp[1].toInt()
                val botNumber = "${temp[4]} ${temp[5]}"
                InitialCommand(botNumber, value)
            }
            else if (command.startsWith("bot")) {
                val botNumber = "${temp[0]} ${temp[1]}"
                val lowBotNumber = "${temp[5]} ${temp[6]}"
                val highBotNumber = "${temp[10]} ${temp[11]}"
                DistributeCommand(botNumber, lowBotNumber, highBotNumber)
            }
            else {
                throw RuntimeException("Unable to parse command = $command")
            }
        }
    }

    fun initialState(commandText: String): State {
        val commands = parseToCommands(commandText)
        val botsWithChips = commands
                .mapNotNull { it as? InitialCommand }
                .groupBy { it.botName }
                .map { it.key to it.value.map { it.value }.toSet() }
                .toMap()

        val botsWithoutChips = commands
                .mapNotNull { (it as? DistributeCommand) }
                .flatMap { listOf(it.botName, it.highRecipientName, it.lowRecipientName) }
                .filterNot { botsWithChips.containsKey(it) }
                .associate { it to emptySet<Int>()}

        return botsWithChips + botsWithoutChips
    }

    fun onePass(commandText: String, currentState: State): State {
        val nextState = currentState.toMutableMap()

        parseToCommands(commandText)
            .mapNotNull { it as? DistributeCommand }
            .forEach { distributeCommand ->
                val botState = currentState[distributeCommand.botName] ?: throw RuntimeException("Unable to find state for botName = ${distributeCommand.botName}")
                if (botState.size > 2) throw RuntimeException("Woah bro! This botName = ${distributeCommand.botName} is holding more than two chips. chipsHeld = $botState")

                // If the bot is holding two chips, we can do the distribution
                if (botState.size == 2) {
                    nextState[distributeCommand.botName] = emptySet()
                    nextState[distributeCommand.highRecipientName] = (nextState[distributeCommand.highRecipientName] ?: emptySet()) + botState.min()!!
                    nextState[distributeCommand.lowRecipientName] = (nextState[distributeCommand.lowRecipientName] ?: emptySet()) + botState.max()!!
                }
            }

        return nextState
    }

    fun moveStuffAroundPlease(commandText: String): State {
        var previousState = initialState(commandText)
        var currentState = onePass(commandText, previousState)

        while (previousState != currentState) {
            previousState = currentState
            currentState = onePass(commandText, currentState)
        }

        return currentState
    }

    fun findBotResponsibleForComparing(commandText: String, microchips: Set<Int>): String? {
        var previousState = initialState(commandText)
        previousState.entries.find { entry -> entry.value == microchips }?.key?.let { return it }

        var currentState = onePass(commandText, previousState)
        currentState.entries.find { entry -> entry.value == microchips }?.key?.let { return it }

        while (previousState != currentState) {
            previousState = currentState
            currentState = onePass(commandText, currentState)
            currentState.entries.find { entry -> entry.value == microchips }?.key?.let { return it }
        }

        return null
    }
}