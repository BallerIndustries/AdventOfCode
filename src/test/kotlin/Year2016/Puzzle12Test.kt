package Year2016

import org.junit.Assert.*
import org.junit.Test



class Puzzle12Test {

    val puzzle = Puzzle12()

    @Test
    fun `can parse copy value command`() {
        val commands = puzzle.parseCommandText("cpy 41 a")
        assertEquals(commands, listOf(Puzzle12.Copy("41", "a")))
    }

    @Test
    fun `can parse copy register command`() {
        val commands = puzzle.parseCommandText("cpy b a")
        assertEquals(commands, listOf(Puzzle12.Copy("b", "a")))
    }

    @Test
    fun `can parse increment command`() {
        val commands = puzzle.parseCommandText("inc a")
        assertEquals(commands, listOf(Puzzle12.IncrementRegister("a")))
    }

    @Test
    fun `can parse decrement command`() {
        val commands = puzzle.parseCommandText("dec a")
        assertEquals(commands, listOf(Puzzle12.DecrementRegister("a")))
    }

    @Test
    fun `can parse jump command`() {
        val commands = puzzle.parseCommandText("jnz a 2")
        assertEquals(commands, listOf(Puzzle12.Jump("a", "2")))
    }
}

class Puzzle12 {
    fun parseCommandText(commandList: String): List<Command> {
        return commandList.split("\n").map { commandText ->
            val tmp = commandText.split(" ")

            when (tmp[0]) {
                "cpy" -> Copy(tmp[1], tmp[2])
                "inc" -> IncrementRegister(tmp[1])
                "dec" -> DecrementRegister(tmp[1])
                "jnz" -> Jump(tmp[1], tmp[2])
                else -> throw RuntimeException("Sorry don't support that command. command = ${tmp[0]}")
            }
        }
    }

    interface Command

    data class Copy(val valueOrRegister: String, val destinationRegister: String) : Command

    data class IncrementRegister(val sourceRegister: String) : Command

    data class DecrementRegister(val sourceRegister: String) : Command

    data class Jump(val conditionalValueOrRegister: String, val deltaValueOrRegister: String) : Command
}
