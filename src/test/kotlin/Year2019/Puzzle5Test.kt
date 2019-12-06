package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle5Test {
    val puzzleText = Puzzle5Test::class.java.getResource("/2019/puzzle5.txt").readText().replace("\r", "")
    val puzzle = Puzzle5()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("12896948", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("7704130", result)
    }
}

class Puzzle5 {
    data class State(val list: List<Long>, val programCounter: Int = 0, val isHalted: Boolean = false, val lastPrintedValue: String? = null, val justJumped: Boolean = false, val userInput: Int) {
        fun writeToIndex(index: Int, value: Long): State {
            val newList = this.list.mapIndexed { i, it -> if (i == index) value else it }
            return this.copy(list = newList)
        }

        fun halt(): State = this.copy(isHalted =  true)
        fun incrementProgramCounter(amount: Int): State = this.copy(programCounter = this.programCounter + amount)
        fun setLastPrintedValue(value: String) = this.copy(lastPrintedValue = value)
        fun jump(valueB: Int) = this.copy(programCounter = valueB, justJumped = true)
        fun clearJustJumped() = this.copy(justJumped = false)
    }

    enum class ParameterMode {
        POSITION, IMMEDIATE;

        companion object {
            fun determineParameterMode(paramModeText: String, paramCount: Int): List<ParameterMode> {
                val paramModes = paramModeText.reversed().mapIndexed { index, c ->
                    if (c == '0') POSITION
                    else if (c == '1') IMMEDIATE
                    else throw RuntimeException()
                }.toMutableList()

                val paramsMissing = paramCount - paramModes.size
                (0 until paramsMissing).forEach { paramModes.add(POSITION) }

                return paramModes
            }
        }
    }

    interface Instruction {
        companion object {
            fun getParamOrValue(paramNumber: Int, paramModes: List<ParameterMode>, valueOrIndex: Int, state: State): Int {
                val paramMode = paramModes[paramNumber]

                return if (paramMode == ParameterMode.IMMEDIATE) valueOrIndex
                else if (paramMode == ParameterMode.POSITION) state.list[valueOrIndex].toInt()
                else throw RuntimeException()
            }
        }

        fun execute(state: State): State

        val size: Int
        val paramModes: List<ParameterMode>
    }

    data class JumpIfTrueInstruction(override val paramModes: List<ParameterMode>, val paramA: Int, val paramB: Int): Instruction {
        override val size = 3

        override fun execute(state: State): State {
            val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
            val b = Instruction.getParamOrValue(1, paramModes, paramB, state)

            if (a != 0) {
                return state.jump(b)
            }
            else {
                return state
            }
        }
    }

    data class JumpIfFalseInstruction(override val paramModes: List<ParameterMode>, val paramA: Int, val paramB: Int): Instruction {
        override val size = 3

        override fun execute(state: State): State {
            val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
            val b = Instruction.getParamOrValue(1, paramModes, paramB, state)

            if (a == 0) {
                return state.jump(b)
            }
            else {
                return state
            }
        }
    }

    data class LessThanInstruction(override val paramModes: List<ParameterMode>, val paramA: Int, val paramB: Int, val paramC: Int): Instruction {
        override val size = 4

        override fun execute(state: State): State {
            val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
            val b = Instruction.getParamOrValue(1, paramModes, paramB, state)

            if (a < b) {
                return state.writeToIndex(paramC, 1)
            }
            else {
                return state.writeToIndex(paramC, 0)
            }
        }
    }

    data class EqualsInstruction(override val paramModes: List<ParameterMode>, val paramA: Int, val paramB: Int, val paramC: Int): Instruction {
        override val size = 4

        override fun execute(state: State): State {
            val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
            val b = Instruction.getParamOrValue(1, paramModes, paramB, state)

            if (a == b) {
                return state.writeToIndex(paramC, 1)
            }
            else {
                return state.writeToIndex(paramC, 0)
            }
        }
    }

    data class ReadInstruction(override val paramModes: List<ParameterMode>, val paramA: Int): Instruction {
        override val size = 2

        override fun execute(state: State): State {
            return state.writeToIndex(paramA, state.userInput.toLong())
        }
    }

    data class WriteInstruction(override val paramModes: List<ParameterMode>, val paramA: Int): Instruction {
        override val size: Int = 2

        override fun execute(state: State): State {
            val horse = Instruction.getParamOrValue(0, paramModes, paramA, state)
            return state.setLastPrintedValue(horse.toString())
        }
    }


    data class AddInstruction(val indexA: Int, val indexB: Int, val destinationIndex: Int, override val paramModes: List<ParameterMode>): Instruction {
        override val size = 4

        override fun execute(state: State): State {
            val a = Instruction.getParamOrValue(0, paramModes, indexA, state)
            val b = Instruction.getParamOrValue(1, paramModes, indexB, state)
            val result = a + b
            return state.writeToIndex(destinationIndex, result.toLong())
        }
    }

    data class MultiplyInstruction(val indexA: Int, val indexB: Int, val destinationIndex: Int, override val paramModes: List<ParameterMode>): Instruction {
        override val size = 4

        override fun execute(state: State): State {
            val a = Instruction.getParamOrValue(0, paramModes, indexA, state)
            val b = Instruction.getParamOrValue(1, paramModes, indexB, state)

            val result = a * b
            return state.writeToIndex(destinationIndex, result.toLong())
        }
    }

    class HaltInstruction(): Instruction {
        override val size = 1
        override val paramModes = listOf<ParameterMode>()

        override fun execute(state: State): State {
            return state.halt()
        }
    }

    fun solveOne(puzzleText: String): String? {
        val list = puzzleText.split(",").map { it.toLong() }
        var state = State(list, 0, false, userInput = 1)

        while (!state.isHalted) {
            val instruction = parseInstruction(state)
            state = instruction.execute(state).incrementProgramCounter(instruction.size)
        }

        return state.lastPrintedValue
    }

    private fun parseInstruction(state: State): Instruction {
        val paramModeAndOpCode = state.list[state.programCounter].toString()
        val opcode = paramModeAndOpCode.takeLast(2).toInt()
        val paramModeText = getParamModeText(paramModeAndOpCode)
        val numParams = getParamCountFromOpCode(opcode)
        val parameterModes = ParameterMode.determineParameterMode(paramModeText, numParams)

        val instruction: Instruction = when (opcode) {
            1 -> {
                val indexA = getParam(state, 1)
                val indexB = getParam(state, 2)
                val destinationIndex = getParam(state, 3)
                AddInstruction(indexA, indexB, destinationIndex, parameterModes)
            }
            2 -> {
                val indexA = getParam(state, 1)
                val indexB = getParam(state, 2)
                val destinationIndex = getParam(state, 3)
                MultiplyInstruction(indexA, indexB, destinationIndex, parameterModes)
            }
            3 -> {
                val indexA = getParam(state, 1)
                ReadInstruction(parameterModes, indexA)

            }
            4 -> {
                val indexA = getParam(state, 1)
                WriteInstruction(parameterModes, indexA)
            }
            5 -> {
                val indexA = getParam(state, 1)
                val indexB = getParam(state, 2)
                JumpIfTrueInstruction(parameterModes, indexA, indexB)
            }
            6 -> {
                val indexA = getParam(state, 1)
                val indexB = getParam(state, 2)
                JumpIfFalseInstruction(parameterModes, indexA, indexB)
            }
            7 -> {
                val indexA = getParam(state, 1)
                val indexB = getParam(state, 2)
                val indexC = getParam(state, 3)
                LessThanInstruction(parameterModes, indexA, indexB, indexC)
            }
            8 -> {
                val indexA = getParam(state, 1)
                val indexB = getParam(state, 2)
                val indexC = getParam(state, 3)
                EqualsInstruction(parameterModes, indexA, indexB, indexC)
            }
            99 -> HaltInstruction()
            else -> throw RuntimeException("Unexpected opcode = $opcode")
        }
        return instruction
    }

    private fun getParamCountFromOpCode(opcode: Int): Int {
        return when (opcode) {
            1 -> 3
            2 -> 3
            3 -> 1
            4 -> 1
            5 -> 2
            6 -> 2
            7 -> 3
            8 -> 3
            99 -> 0
            else -> throw RuntimeException("oaisjdaoisjd")
        }
    }

    private fun getParamModeText(paramModeAndOpCode: String): String {
        return if (paramModeAndOpCode.length < 2) "" else paramModeAndOpCode.substring(0, paramModeAndOpCode.length - 2)
    }

    private fun getParam(state: State, offset: Int) = state.list[state.programCounter + offset].toInt()

    fun solveTwo(puzzleText: String): String? {
        val list = puzzleText.split(",").map { it.toLong() }
        var state = State(list, 0, false, userInput = 5)

        while (!state.isHalted) {
            val instruction = parseInstruction(state)
            state = instruction.execute(state)

            if (state.justJumped) {
                state = state.clearJustJumped()
            }
            else {
                state = state.incrementProgramCounter(instruction.size)
            }
        }

        return state.lastPrintedValue
    }
}

