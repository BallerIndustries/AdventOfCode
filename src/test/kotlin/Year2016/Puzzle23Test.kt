package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle23Test {
    val puzzle = Puzzle23()
    val puzzleText = this::class.java.getResource("/2016/puzzle23.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(12748, result)
    }

    @Test
    fun `example part a`() {
        val dog = """
            cpy 2 a
            tgl a
            tgl a
            tgl a
            cpy 1 a
            dec a
            dec a
        """.trimIndent()

        val result = puzzle.solveOne(dog)
        assertEquals(3, result)
    }

    @Test
    fun `can solve part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(479009308, result)
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
                "tgl" -> Toggle(tmp[1])
                "mul" -> Multiply(tmp[1], tmp[2], tmp[3])
                else -> throw RuntimeException("Sorry don't support that command. command = ${tmp[0]}")
            }
        }
    }

    fun solveOne(puzzleText: String): Int {
        var state = State(commands = parseCommandText(puzzleText))

        while (state.programCounter < parseCommandText(puzzleText).size && state.programCounter >= 0) {
            val command = state.commands[state.programCounter]
            state = command.execute(state)
        }

        return state.registers["a"]!!
    }

    fun solveTwo(puzzleText: String): Int {
        val original = "cpy b c\n" +
                "inc a\n" +
                "dec c\n" +
                "jnz c -2\n" +
                "dec d\n" +
                "jnz d -5"

        val replacement = "mul b d a\n" +
                "cpy 0 c\n" +
                "cpy 0 c\n" +
                "cpy 0 c\n" +
                "cpy 0 c\n" +
                "cpy 0 d"

        var state = State(commands = parseCommandText(puzzleText.replace(original, replacement)), registers = State.partBState)

        while (state.programCounter < parseCommandText(puzzleText).size && state.programCounter >= 0) {
            val command = state.commands[state.programCounter]
            state = command.execute(state)
        }

        return state.registers["a"]!!
    }

    interface Command {
        fun execute(state: State): State
        fun toggle(): Command
    }

    data class Toggle(val deltaValueOrRegister: String): Command {
        override fun toggle(): Command {
            return IncrementRegister(deltaValueOrRegister)
        }

        override fun execute(state: State): State {
            val delta = state.getValueOrValueFromRegister(deltaValueOrRegister)

            val index = state.programCounter + delta

            if (index < 0 || index > state.commands.lastIndex) {
                return state.copy(programCounter = state.programCounter + 1)
            }

            val commands = state.commands.map { it }.toMutableList()
            commands[index] = commands[index].toggle()
            return state.copy(programCounter = state.programCounter + 1, commands = commands)
        }
    }

    data class Copy(val valueOrRegister: String, val destinationRegister: String) : Command {
        override fun toggle(): Command {
            return JumpIfNotZero(valueOrRegister, destinationRegister)
        }

        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val mutableRegisters = state.registers + mapOf(destinationRegister to state.getValueOrValueFromRegister(valueOrRegister))
            return state.copy(programCounter = newProgramCounter, registers = mutableRegisters)
        }
    }

    data class IncrementRegister(val sourceRegister: String) : Command {
        override fun toggle(): Command {
            return DecrementRegister(sourceRegister)
        }

        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val newRegisters = state.registers + mapOf(sourceRegister to state.getValueOrValueFromRegister(sourceRegister) + 1)
            return state.copy(programCounter = newProgramCounter, registers = newRegisters)
        }
    }

    data class DecrementRegister(val sourceRegister: String) : Command {
        override fun toggle(): Command {
            return IncrementRegister(sourceRegister)
        }

        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val newRegisters = state.registers + mapOf(sourceRegister to state.getValueOrValueFromRegister(sourceRegister) - 1)
            return state.copy(programCounter = newProgramCounter, registers = newRegisters)
        }
    }

    data class JumpIfNotZero(val conditionalValueOrRegister: String, val deltaValueOrRegister: String) : Command {
        override fun toggle(): Command {
            return Copy(conditionalValueOrRegister, deltaValueOrRegister)
        }

        override fun execute(state: State): State {
            if (state.getValueOrValueFromRegister(conditionalValueOrRegister) == 0) {
                return state.copy(programCounter = state.programCounter + 1, registers = state.registers)
            }
            else {
                val deltaValue = state.getValueOrValueFromRegister(deltaValueOrRegister)
                return state.copy(programCounter = state.programCounter + deltaValue, registers = state.registers)
            }
        }
    }

    data class Multiply(val operandA: String, val operandB: String, val destinationRegister: String): Command {
        override fun execute(state: State): State {
            val valueA = state.getValueOrValueFromRegister(operandA)
            val valueB = state.getValueOrValueFromRegister(operandB)
            return state.copy(programCounter = state.programCounter + 1, registers = state.registers + (destinationRegister to valueA * valueB))
        }

        override fun toggle(): Command {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    data class State(val programCounter: Int = 0, val registers: Map<String, Int> = initialRegisters, val commands: List<Command>) {
        companion object {
            val initialRegisters = mapOf("a" to 7, "b" to 0, "c" to 0, "d" to 0)
            val partBState = mapOf("a" to 12, "b" to 0, "c" to 0, "d" to 0)
        }

        fun getValueOrValueFromRegister(valueOrRegister: String): Int {
            return if (valueOrRegister.toIntOrNull() != null) valueOrRegister.toInt() else registers[valueOrRegister]!!
        }

        override fun toString(): String {
            return "State(programCounter=$programCounter, registers=$registers commands=$commands)"
        }
    }
}
