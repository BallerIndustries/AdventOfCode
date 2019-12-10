package Year2019

interface Instruction {
    companion object {
        fun getIndexValue(paramNumber: Int, paramModes: List<IntCodeVirtualMachine.ParameterMode>, valueOrIndex: Long, state: State): Int {
            val paramMode = paramModes[paramNumber]

            return when (paramMode) {
                IntCodeVirtualMachine.ParameterMode.RELATIVE -> (state.relativeBaseOffset + valueOrIndex).toInt()
                else -> valueOrIndex.toInt()
            }
        }

        fun getParamOrValue(paramNumber: Int, paramModes: List<IntCodeVirtualMachine.ParameterMode>, valueOrIndex: Long, state: State): Long {
            val paramMode = paramModes[paramNumber]

            return when (paramMode) {
                IntCodeVirtualMachine.ParameterMode.IMMEDIATE -> valueOrIndex
                IntCodeVirtualMachine.ParameterMode.POSITION -> state.list[valueOrIndex.toInt()]
                IntCodeVirtualMachine.ParameterMode.RELATIVE -> state.list[valueOrIndex.toInt() + state.relativeBaseOffset.toInt()]
            }
        }
    }

    fun execute(state: State): State

    val size: Int
    val paramModes: List<IntCodeVirtualMachine.ParameterMode>
}

data class JumpIfTrueInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val paramA: Long, val paramB: Long): Instruction {
    override val size = 3

    override fun execute(state: State): State {
        val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
        val b = Instruction.getParamOrValue(1, paramModes, paramB, state)

        if (a != 0L) {
            return state.jump(b)
        }
        else {
            return state
        }
    }
}

data class JumpIfFalseInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val paramA: Long, val paramB: Long): Instruction {
    override val size = 3

    override fun execute(state: State): State {
        val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
        val b = Instruction.getParamOrValue(1, paramModes, paramB, state)

        if (a == 0L) {
            return state.jump(b)
        }
        else {
            return state
        }
    }
}

data class LessThanInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val paramA: Long, val paramB: Long, val paramC: Long): Instruction {
    override val size = 4

    override fun execute(state: State): State {
        val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
        val b = Instruction.getParamOrValue(1, paramModes, paramB, state)
        val index = Instruction.getIndexValue(2, paramModes, paramC, state)

        if (a < b) {
            return state.writeToIndex(index, 1)
        }
        else {
            return state.writeToIndex(index, 0)
        }
    }
}

data class AdjustRelativeBase(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val paramA: Long): Instruction {
    override val size = 2

    override fun execute(state: State): State {
        val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
        return state.adjustRelativeBase(a)
    }
}

data class EqualsInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val paramA: Long, val paramB: Long, val paramC: Long): Instruction {
    override val size = 4

    override fun execute(state: State): State {
        val a = Instruction.getParamOrValue(0, paramModes, paramA, state)
        val b = Instruction.getParamOrValue(1, paramModes, paramB, state)
        val index = Instruction.getIndexValue(2, paramModes, paramC, state)

        if (a == b) {
            return state.writeToIndex(index, 1)
        }
        else {
            return state.writeToIndex(index, 0)
        }
    }
}

data class ReadInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val paramA: Long): Instruction {
    override val size = 2

    override fun execute(state: State): State {
        val (userInput, newState) = state.popOffInput()
        val index = Instruction.getIndexValue(0, paramModes, paramA, state)
        return newState.writeToIndex(index, userInput)
    }
}

data class WriteInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val paramA: Long): Instruction {
    override val size: Int = 2

    override fun execute(state: State): State {
        val horse = Instruction.getParamOrValue(0, paramModes, paramA, state)
        println(horse)
        return state.setLastPrintedValue(horse)
    }
}


data class AddInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val indexA: Long, val indexB: Long, val destinationIndex: Long): Instruction {
    override val size = 4

    override fun execute(state: State): State {
        val a = Instruction.getParamOrValue(0, paramModes, indexA, state)
        val b = Instruction.getParamOrValue(1, paramModes, indexB, state)
        val c = Instruction.getIndexValue(2, paramModes, destinationIndex, state)

        val result = a + b
        return state.writeToIndex(c, result)
    }
}

data class MultiplyInstruction(override val paramModes: List<IntCodeVirtualMachine.ParameterMode>, val indexA: Long, val indexB: Long, val destinationIndex: Long): Instruction {
    override val size = 4

    override fun execute(state: State): State {
        val a = Instruction.getParamOrValue(0, paramModes, indexA, state)
        val b = Instruction.getParamOrValue(1, paramModes, indexB, state)
        val c = Instruction.getIndexValue(2, paramModes, destinationIndex, state)

        val result = a * b
        return state.writeToIndex(c, result)
    }
}

class HaltInstruction : Instruction {
    override val size = 1
    override val paramModes = listOf<IntCodeVirtualMachine.ParameterMode>()

    override fun execute(state: State): State {
        return state.halt()
    }
}

data class State(val list: List<Long>, val programCounter: Long = 0, val isHalted: Boolean = false, val lastPrintedValue: Long? = null, val justJumped: Boolean = false, val userInput: List<Long>, val relativeBaseOffset: Long = 0) {
    fun writeToIndex(index: Int, value: Long): State {
        val newList = this.list.mapIndexed { i, it -> if (i == index) value else it }
        return this.copy(list = newList)
    }

    fun halt(): State = this.copy(isHalted =  true)
    fun incrementProgramCounter(amount: Int): State = this.copy(programCounter = this.programCounter + amount)
    fun setLastPrintedValue(value: Long) = this.copy(lastPrintedValue = value)
    fun jump(valueB: Long) = this.copy(programCounter = valueB, justJumped = true)
    fun clearJustJumped() = this.copy(justJumped = false)
    fun addUserInput(input: Long) = this.copy(userInput = this.userInput + input)

    fun popOffInput(): Pair<Long, State> {
        return userInput.first() to this.copy(userInput = this.userInput.subList(1, this.userInput.size))
    }

    fun adjustRelativeBase(relativeBaseDelta: Long): State {
        return this.copy(relativeBaseOffset = this.relativeBaseOffset + relativeBaseDelta)
    }
}

class IntCodeVirtualMachine {

    fun runProgram(initialState: State): State {
        var state = initialState

        while (!state.isHalted) {
            val instruction = parseInstruction(state)

            if (instruction is ReadInstruction && state.userInput.isEmpty()) {
                return state
            }

            state = instruction.execute(state)

            if (state.justJumped) {
                state = state.clearJustJumped()
            }
            else {
                state = state.incrementProgramCounter(instruction.size)
            }
        }

        return state
    }

    private fun parseInstruction(state: State): Instruction {
        val paramModeAndOpCode = state.list[state.programCounter.toInt()].toString()
        val opcode = paramModeAndOpCode.takeLast(2).toInt()
        val paramModeText = getParamModeText(paramModeAndOpCode)
        val numParams = getParamCountFromOpCode(opcode)
        val parameterModes = ParameterMode.determineParameterMode(paramModeText, numParams)

        val indexA = getParamOrNull(state, 1)
        val indexB = getParamOrNull(state, 2)
        val indexC = getParamOrNull(state, 3)

        return when (opcode) {
            1 -> AddInstruction(parameterModes, indexA!!, indexB!!, indexC!!)
            2 -> MultiplyInstruction(parameterModes, indexA!!, indexB!!, indexC!!)
            3 -> ReadInstruction(parameterModes, indexA!!)
            4 -> WriteInstruction(parameterModes, indexA!!)
            5 -> JumpIfTrueInstruction(parameterModes, indexA!!, indexB!!)
            6 -> JumpIfFalseInstruction(parameterModes, indexA!!, indexB!!)
            7 -> LessThanInstruction(parameterModes, indexA!!, indexB!!, indexC!!)
            8 -> EqualsInstruction(parameterModes, indexA!!, indexB!!, indexC!!)
            9 -> AdjustRelativeBase(parameterModes, indexA!!)
            99 -> HaltInstruction()
            else -> throw RuntimeException("Unexpected opcode = $opcode")
        }
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
            9 -> 1
            99 -> 0
            else -> throw RuntimeException("Unexpected opcode = $opcode")
        }
    }

    private fun getParamModeText(paramModeAndOpCode: String): String {
        return if (paramModeAndOpCode.length < 2) "" else paramModeAndOpCode.substring(0, paramModeAndOpCode.length - 2)
    }

    private fun getParamOrNull(state: State, offset: Int) = state.list.getOrNull(state.programCounter.toInt() + offset)

    enum class ParameterMode {
        POSITION, IMMEDIATE, RELATIVE;

        companion object {
            fun determineParameterMode(paramModeText: String, paramCount: Int): List<ParameterMode> {
                val paramModes = paramModeText.reversed().mapIndexed { index, c ->
                    when (c) {
                        '0' -> POSITION
                        '1' -> IMMEDIATE
                        '2' -> RELATIVE
                        else -> throw RuntimeException()
                    }
                }

                val paramsMissing = paramCount - paramModes.size
                return paramModes + (0 until paramsMissing).map { POSITION }
            }
        }
    }
}