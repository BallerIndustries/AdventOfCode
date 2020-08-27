package Year2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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

