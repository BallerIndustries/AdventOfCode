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

    @Test
    fun `state after a copy value command`() {
        val initialState = Puzzle12.State()
        val currentState = Puzzle12.Copy("12", "a").execute(initialState)
        assertEquals(currentState, Puzzle12.State(1, mapOf("a" to 12)))
    }

    @Test
    fun `state after a copy register command`() {
        val initialState = Puzzle12.State()
        val state1 = Puzzle12.Copy("12", "a").execute(initialState)
        val state2 = Puzzle12.Copy("a", "b").execute(state1)
        assertEquals(state2, Puzzle12.State(2, mapOf("a" to 12, "b" to 12)))
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

    interface Command {
        fun execute(state: State): State
    }

    data class Copy(val valueOrRegister: String, val destinationRegister: String) : Command {
        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val mutableRegisters = state.registers + mapOf(destinationRegister to state.getValueOrValueFromRegister(valueOrRegister))
            return State(newProgramCounter, mutableRegisters)
        }
    }

    data class IncrementRegister(val sourceRegister: String) : Command {
        override fun execute(state: State): State {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    data class DecrementRegister(val sourceRegister: String) : Command {
        override fun execute(state: State): State {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    data class Jump(val conditionalValueOrRegister: String, val deltaValueOrRegister: String) : Command {
        override fun execute(state: State): State {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    data class State(val programCounter: Int = 0, val registers: Map<String, Int> = mapOf()) {
        fun getValueOrValueFromRegister(valueOrRegister: String): Int {
            return if (valueOrRegister.toIntOrNull() != null) valueOrRegister.toInt() else registers[valueOrRegister]!!
        }
    }
}
