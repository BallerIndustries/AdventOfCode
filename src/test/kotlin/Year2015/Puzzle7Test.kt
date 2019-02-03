package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle7Test {
    val puzzle = Puzzle7()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle7.txt").readText().replace("\r", "")

    val exampleText = """
        123 -> x
        456 -> y
        x AND y -> d
        x OR y -> e
        x LSHIFT 2 -> f
        y RSHIFT 2 -> g
        NOT x -> h
        NOT y -> i
    """.trimIndent()

    @Test
    fun `example part a`() {
        val actual = puzzle.stateAfter(exampleText)
        val expected = mapOf<String, UShort>(
            "d" to 72u,
            "e" to 507u,
            "f" to 492u,
            "g" to 114u,
            "h" to 65412u,
            "i" to 65079u,
            "x" to 123u,
            "y" to 456u
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(956, result.toInt())
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)

        // 33706 too low
        assertEquals(17837115, result)
    }
}

class Puzzle7 {
    fun solveOne(text: String): UShort {
        val state = stateAfter(text)
        return state["a"]!!
    }

    fun solveTwo(puzzleText: String): UShort {
        val wireA = solveOne(puzzleText)

        val instructions = parseInstructions(puzzleText)
        val state = mapOf<String, UShort>("b" to wireA)

        // While any of the instructions have not been processed
        val secondRunState = runInstructions(instructions, state)
        return secondRunState["a"]!!
    }

    fun stateAfter(exampleText: String): Map<String, UShort> {
        val instructions = parseInstructions(exampleText)
        val state = mapOf<String, UShort>()

        // While any of the instructions have not been processed
        return runInstructions(instructions, state)
    }

    private fun parseInstructions(exampleText: String): MutableMap<Instruction, Boolean> {
        val instructions = exampleText.split("\n")
                .map { Instruction.parse(it) }
                .associate { it to false }
                .toMutableMap()
        return instructions
    }

    private fun runInstructions(instructions: MutableMap<Instruction, Boolean>, state: Map<String, UShort>): Map<String, UShort> {
        var state1 = state

        while (instructions.values.any { !it }) {
            // Get unprocessed instructions
            val unprocessedInstructions = instructions.entries.filter { !it.value }

            unprocessedInstructions.forEach { (instruction) ->
                val (wasSuccesful, newState) = instruction.execute(state1)

                if (wasSuccesful) {
                    println("successfully executed instruction = $instruction")
                    state1 = newState
                    instructions[instruction] = true
                }
            }
        }

        return state1
    }

    interface Instruction {
        companion object {
            fun parse(text: String): Instruction {
                val dog = text.split(" ")

                return when {
                    dog[1] == "AND" -> And(dog[0], dog[2], dog[4])
                    dog[1] == "OR" -> Or(dog[0], dog[2], dog[4])
                    dog[1] == "LSHIFT" -> LeftShift(dog[0], dog[2], dog[4])
                    dog[1] == "RSHIFT" -> RightShift(dog[0], dog[2], dog[4])
                    dog[0] == "NOT" -> Not(dog[1], dog[3])
                    dog.count() == 3 && dog[1] == "->" -> Set(dog[0], dog[2])
                    else -> throw RuntimeException("fuck a doodle doo")
                }
            }
        }

        fun execute(state: Map<String, UShort>): ExecutionResult

        fun tryGetValue(state: Map<String, UShort>, valueOrRegisterName: String): UShort?  {
            // It's a constant
            valueOrRegisterName.toUShortOrNull()?.let { return it }

            // It's a registerName
            return state[valueOrRegisterName]
        }
    }

    data class ExecutionResult(val succeeded: Boolean, val state: Map<String, UShort>) {
        companion object {
            val Failed = ExecutionResult(false, mapOf())
            fun Succeeded(state: Map<String, UShort>) = ExecutionResult(true, state)
        }
    }

    data class Set(val operandA: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, UShort>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)

            if (operandAValue == null) {
                return ExecutionResult.Failed
            }

            val newState = state + (registerName to operandAValue)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class And(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, UShort>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val andResult = (operandAValue.toInt() and operandBValue.toInt()).toUShort()
            val newState = state + (registerName to andResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class Or(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, UShort>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val orResult = (operandAValue.toInt() or operandBValue.toInt()).toUShort()
            val newState = state + (registerName to orResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class LeftShift(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, UShort>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val leftShiftResult = (operandAValue.toInt() shl operandBValue.toInt()).toUShort()
            val newState = state + (registerName to leftShiftResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class RightShift(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, UShort>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val leftShiftResult = (operandAValue.toInt() shr operandBValue.toInt()).toUShort()
            val newState = state + (registerName to leftShiftResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class Not(val operandA: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, UShort>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)

            if (operandAValue == null) {
                return ExecutionResult.Failed
            }

            val notResult = operandAValue.toInt().inv().toUShort()
            val newState = state + (registerName to notResult)
            return ExecutionResult.Succeeded(newState)
        }
    }
}