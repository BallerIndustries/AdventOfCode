package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

typealias State = Map<String, Set<Int>>
interface Command
data class InitialCommand(val botName: String, val value: Int) : Command
data class DistributeCommand(val botName: String, val highRecipientName: String, val lowRecipientName: String) : Command

class Puzzle10Test {
    val puzzle = Puzzle10()

    @Test
    fun `can convert commandText into InitialCommand`() {
        val commandText = "value 5 goes to bot 2"
        val commands = puzzle.parseToCommands(commandText)
        assertEquals(commands, listOf(InitialCommand("bot 2", 5)))
    }

    @Test
    fun `can convert commandText into DistributeCommand`() {
        val commandText = "bot 2 gives low to bot 1 and high to bot 0"
        val commands = puzzle.parseToCommands(commandText)
        assertEquals(commands, listOf(DistributeCommand("bot 2", "bot 1", "bot 0")))
    }

    @Test
    fun `can figure out the initial bot state`() {
        val result = puzzle.initialState(commandText)

        assertEquals(result, mapOf(
            "bot 0" to emptySet(),
            "bot 1" to setOf(3),
            "bot 2" to setOf(2, 5),
            "output 0" to emptySet(),
            "output 1" to emptySet(),
            "output 2" to emptySet())
        )
    }

    @Test
    fun `can figure out state after one pass`() {
        val result1 = puzzle.initialState(commandText)
        val result2 = puzzle.onePass(commandText, result1)

        assertEquals(result2, mapOf(
            "bot 0" to setOf(5),
            "bot 1" to setOf(2, 3),
            "bot 2" to setOf(),
            "output 0" to emptySet(),
            "output 1" to emptySet(),
            "output 2" to emptySet())
        )
    }

    @Test
    fun `can figure out state after two passes`() {
        val result1 = puzzle.initialState(commandText)
        val result2 = puzzle.onePass(commandText, result1)
        val result3 = puzzle.onePass(commandText, result2)

        assertEquals(result3, mapOf(
            "bot 0" to setOf(3, 5),
            "bot 1" to setOf(),
            "bot 2" to setOf(),
            "output 0" to emptySet(),
            "output 1" to setOf(2),
            "output 2" to emptySet())
        )
    }

    val commandText =
            "value 5 goes to bot 2\n" +
            "bot 2 gives low to bot 1 and high to bot 0\n" +
            "value 3 goes to bot 1\n" +
            "bot 1 gives low to output 1 and high to bot 0\n" +
            "bot 0 gives low to output 2 and high to output 0\n" +
            "value 2 goes to bot 2"


    @Test
    fun `can figure out state after three passes`() {
        val result1 = puzzle.initialState(commandText)
        val result2 = puzzle.onePass(commandText, result1)
        val result3 = puzzle.onePass(commandText, result2)
        val result4 = puzzle.onePass(commandText, result3)

        assertEquals(result4, mapOf(
                "bot 0" to setOf(),
                "bot 1" to setOf(),
                "bot 2" to setOf(),
                "output 0" to setOf(5),
                "output 1" to setOf(2),
                "output 2" to setOf(3))
        )
    }

    @Test
    fun `bot move your stuff around please`() {
        val result = puzzle.moveStuffAroundPlease(commandText)

        assertEquals(result, mapOf(
                "bot 0" to setOf(),
                "bot 1" to setOf(),
                "bot 2" to setOf(),
                "output 0" to setOf(5),
                "output 1" to setOf(2),
                "output 2" to setOf(3))
        )
    }

    @Test
    fun `bot 2 should be responsible for comparing microchips 5 and 2`() {
        val botName = puzzle.findBotResponsibleForComparing(commandText, setOf(2, 5))
        assertEquals(botName, "bot 2")
    }

    @Test
    fun `puzzle part a`() {
        val commandText = Puzzle10Test::class.java.getResource("/2016/puzzle10.txt").readText().replace("\r\n","\n")
        val botName = puzzle.findBotResponsibleForComparing(commandText, setOf(61, 17))
        assertEquals(botName, "bot 147")
    }

    @Test
    fun `puzzle part b`() {
        val commandText = Puzzle10Test::class.java.getResource("/2016/puzzle10.txt").readText().replace("\r\n","\n")
        val result = puzzle.moveStuffAroundPlease(commandText)
        println(result)
        val horse = result["output 0"]!!.first() * result["output 1"]!!.first() * result["output 2"]!!.first()
        assertEquals(horse, 55637)

    }
}

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
