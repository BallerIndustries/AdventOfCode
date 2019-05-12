package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle23Test {
    val puzzle = Puzzle23()
    val puzzleText = this::class.java.getResource("/2016/puzzle23.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(981, result)
    }

    @Test
    fun `can solve part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(123123, result)
    }
}

class Puzzle23 {
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

    fun solveOne(puzzleText: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun solveTwo(puzzleText: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
