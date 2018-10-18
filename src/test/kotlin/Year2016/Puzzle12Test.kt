package Year2016

import Year2016.Puzzle12.State
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
        assertEquals(commands, listOf(Puzzle12.JumpIfNotZero("a", "2")))
    }

    @Test
    fun `state after a copy value command`() {
        val initialState = State()
        val currentState = Puzzle12.Copy("12", "a").execute(initialState)
        assertEquals(currentState, State(1, mapOf("a" to 12, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after a copy register command`() {
        val initialState = State()
        val state1 = Puzzle12.Copy("12", "a").execute(initialState)
        val state2 = Puzzle12.Copy("a", "b").execute(state1)
        assertEquals(state2, State(2, mapOf("a" to 12, "b" to 12, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after an increment register command`() {
        val initialState = State()
        val state1 = Puzzle12.IncrementRegister("a").execute(initialState)
        assertEquals(state1, State(1, mapOf("a" to 1, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after an decrement register command`() {
        val initialState = State()
        val state1 = Puzzle12.DecrementRegister("a").execute(initialState)
        assertEquals(state1, State(1, mapOf("a" to -1, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after jump command, when register is zero`() {
        val initialState = State()
        val state1 = Puzzle12.JumpIfNotZero("a", "7").execute(initialState)
        assertEquals(state1, State(1, mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after jump command, when register is not zero`() {
        val initialState = State()
        val stateOne = Puzzle12.IncrementRegister("a").execute(initialState)
        val stateTwo = Puzzle12.JumpIfNotZero("a", "7").execute(stateOne)
        assertEquals(stateTwo, State(8, mapOf("a" to 1, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `example input`() {
        val input = "cpy 41 a\n" +
                "inc a\n" +
                "inc a\n" +
                "dec a\n" +
                "jnz a 2\n" +
                "dec a"

        val finalState = puzzle.runCommands(input)
        assertEquals(finalState, State(6, mapOf("a" to 42, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle12::class.java.getResource("/2016/puzzle12.txt").readText().replace("\r", "")
        val finalState = puzzle.runCommands(input)
        assertEquals(finalState, State(23, mapOf("a" to 318077, "b" to 196418, "c" to 0, "d" to 0)))
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle12::class.java.getResource("/2016/puzzle12.txt").readText().replace("\r", "")
        val initialState = State(0, mapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0))
        val finalState = puzzle.runCommands(input, initialState)
        assertEquals(finalState, State(23, mapOf("a" to 9227731, "b" to 5702887, "c" to 0, "d" to 0)))
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
                "jnz" -> JumpIfNotZero(tmp[1], tmp[2])
                else -> throw RuntimeException("Sorry don't support that command. command = ${tmp[0]}")
            }
        }
    }

    fun runCommands(commandText: String, initialState: State = State()): State {
        var state = initialState
        val commands = parseCommandText(commandText)

        while (state.programCounter < commands.size) {
            val command = commands[state.programCounter]
            state = command.execute(state)
        }

        return state
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
            val newProgramCounter = state.programCounter + 1
            val newRegisters = state.registers + mapOf(sourceRegister to state.getValueOrValueFromRegister(sourceRegister) + 1)
            return State(newProgramCounter, newRegisters)
        }
    }

    data class DecrementRegister(val sourceRegister: String) : Command {
        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val newRegisters = state.registers + mapOf(sourceRegister to state.getValueOrValueFromRegister(sourceRegister) - 1)
            return State(newProgramCounter, newRegisters)
        }
    }

    data class JumpIfNotZero(val conditionalValueOrRegister: String, val deltaValueOrRegister: String) : Command {
        override fun execute(state: State): State {
            if (state.getValueOrValueFromRegister(conditionalValueOrRegister) == 0) {
                return State(state.programCounter + 1, state.registers)
            }
            else {
                val deltaValue = state.getValueOrValueFromRegister(deltaValueOrRegister)
                return State(state.programCounter + deltaValue, state.registers)
            }
        }
    }

    data class State(val programCounter: Int = 0, val registers: Map<String, Int> = initialRegisters) {
        companion object {
            val initialRegisters = mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
        }

        fun getValueOrValueFromRegister(valueOrRegister: String): Int {
            return if (valueOrRegister.toIntOrNull() != null) valueOrRegister.toInt() else registers[valueOrRegister]!!
        }
    }
}
