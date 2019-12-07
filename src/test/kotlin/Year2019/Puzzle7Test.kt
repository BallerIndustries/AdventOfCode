package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle7Test {
    val puzzleText = Puzzle7Test::class.java.getResource("/2019/puzzle7.txt").readText().replace("\r", "")
    val puzzle = Puzzle7()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(262086, result)
    }

    @Test
    fun `puzzle part b`() {
        // NOT 262086
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("7704130", result)
    }

    @Test
    fun `example a`() {
        val result = puzzle.solveTwo("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5")
        assertEquals(139629729, result)
    }
}

class Puzzle7 {


    fun solveOne(puzzleText: String): Int? {
//        val permutations = getPermutationsForRange(0, 4)
//        return permutations.map { sendItThroughThePipe(puzzleText, it) }.map { it.first }.max()

        throw java.lang.RuntimeException()
    }

    fun solveTwo(puzzleText: String): Int? {
        val permutations = getPermutationsForRange(5, 9)

        val allOutputs = mutableListOf<Int>()


        permutations.forEach {phases ->
            val input = puzzleText.split(",").map { it.toLong() }
            val amplifiers = phases.map { phase -> State(input, userInput = listOf(phase)) }.toMutableList()
            var lastOutput = 0
            var index = 0

            while (!amplifiers.last().isHalted) {
                val state = amplifiers[index]
                val currentAmplifier: State = runAmplifier(state.copy(userInput = state.userInput + lastOutput))

                if (currentAmplifier.lastPrintedValue != null) {
                    lastOutput = currentAmplifier.lastPrintedValue!!.toInt()
                }

                amplifiers[index] = currentAmplifier
                index = if (index == amplifiers.lastIndex) 0 else index + 1
            }

            allOutputs.add(lastOutput)
        }

        return allOutputs.max()






//        val amplificationResults = permutations.map { amplifiedPhaseSettings: List<Int> ->
//            val temp = sendItThroughThePipe(puzzleText, amplifiedPhaseSettings, signal)
//            signal = temp.first
//            amplifyTheseGuys(signal, temp.second, amplifiedPhaseSettings)
//        }
//
//        return amplificationResults.max()
    }

    private fun runAmplifier(state: State): State {
        //val list = puzzleText.split(",").map { it.toLong() }
        //var state = State(list, 0, false, userInput = listOf(phaseSetting, inputSignal))
        return runAmplifierProgram(state).second
    }

//    private val amplificationCounts = mutableSetOf<Int>()

//    private fun amplifyTheseGuys(outputSignal: Int, programStates: List<State>, amplifiedPhaseSettings: List<Int>): Int {
//        var amplifiedSignal = outputSignal
//        var amplifiedProgramStates = programStates
//        var count = 0
//        val allSignals = mutableListOf<Int>()
//
//
//        while (!amplifiedProgramStates[4].isHalted) {
//            count++
//            val tmp = sendItThroughThesePipes(amplifiedProgramStates, amplifiedPhaseSettings, amplifiedSignal)
//            amplifiedSignal = tmp.first
//            amplifiedProgramStates = tmp.second
//            allSignals += amplifiedSignal
//        }
//
//        amplificationCounts.add(count)
//        return allSignals.max()!!
//    }
//
//    private fun sendItThroughThesePipes(programs: List<State>, phaseSettings: List<Int>, initialInputSignal: Int): Pair<Int, List<State>> {
//        var inputSignal = initialInputSignal
//        val amplifierProgramStates = mutableListOf<State>()
//
//        phaseSettings.forEachIndexed { index, phaseSetting ->
//            val program = programs[index]
//            val tmp = runAmplifierProgram(program, phaseSetting, inputSignal)
//            inputSignal = (tmp.second.lastPrintedValue?.toInt() ?: -1)
//            amplifierProgramStates.add(tmp.second)
//        }
//
//        return inputSignal to amplifierProgramStates
//    }
//
//
//    private fun sendItThroughThePipe(puzzleText: String, phaseSettings: List<Int>, initialInputSignal: Int = 0): Pair<Int, List<State>> {
//        var inputSignal = initialInputSignal
//        val amplifierProgramStates = mutableListOf<State>()
//
//        phaseSettings.forEachIndexed { index, phaseSetting ->
//            val tmp = runAmplifierProgram(puzzleText, phaseSetting, inputSignal)
//            inputSignal = tmp.first
//            amplifierProgramStates.add(tmp.second)
//        }
//
//        return inputSignal to amplifierProgramStates
//    }

    enum class TerminationCode { BLOCKED_ON_IO, HALT }

    private fun runAmplifierProgram(initialState: State, phaseSetting: Int? = null, inputSignal: Int? = null): Pair<TerminationCode, State> {
        //var state = initialState.copy(userInput = listOfNotNull(phaseSetting, inputSignal))
        var state = initialState

        while (!state.isHalted) {
            val instruction = parseInstruction(state)
            //println(instruction)

            if (instruction is ReadInstruction && state.userInput.isEmpty()) {
                return TerminationCode.BLOCKED_ON_IO to state
            }


            state = instruction.execute(state)

            if (state.justJumped) {
                state = state.clearJustJumped()
            }
            else {
                state = state.incrementProgramCounter(instruction.size)
            }
        }

        return TerminationCode.HALT to state
    }

    private fun runAmplifierProgram(puzzleText: String, phaseSetting: Int, inputSignal: Int): Pair<TerminationCode, State> {
        val list = puzzleText.split(",").map { it.toLong() }
        var state = State(list, 0, false, userInput = listOf(phaseSetting, inputSignal))
        return runAmplifierProgram(state, phaseSetting, inputSignal)
    }

    private fun getPermutationsForRange(from: Int, to: Int): List<List<Int>> {
        val permutations = mutableListOf<List<Int>>()

        (from..to).forEach { A ->
            (from..to).forEach { B ->
                (from..to).forEach { C ->
                    (from..to).forEach { D ->
                        (from..to).forEach { E ->
                            if (setOf(A, B, C, D, E).size == 5) {
                                permutations.add(listOf(A, B, C, D, E))
                            }
                        }
                    }
                }
            }
        }

        return permutations
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



    data class State(val list: List<Long>, val programCounter: Int = 0, val isHalted: Boolean = false, val lastPrintedValue: String? = null, val justJumped: Boolean = false, val userInput: List<Int>) {
        fun writeToIndex(index: Int, value: Long): State {
            val newList = this.list.mapIndexed { i, it -> if (i == index) value else it }
            return this.copy(list = newList)
        }

        fun halt(): State = this.copy(isHalted =  true)
        fun incrementProgramCounter(amount: Int): State = this.copy(programCounter = this.programCounter + amount)
        fun setLastPrintedValue(value: String) = this.copy(lastPrintedValue = value)
        fun jump(valueB: Int) = this.copy(programCounter = valueB, justJumped = true)
        fun clearJustJumped() = this.copy(justJumped = false)

        fun popOffInput(): Pair<Int, State> {
            return userInput.first() to this.copy(userInput = this.userInput.subList(1, this.userInput.size))
        }
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
            val (userInput, newState) = state.popOffInput()
            return newState.writeToIndex(paramA, userInput.toLong())
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

}

