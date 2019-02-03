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
        val actual: Map<String, Long> = puzzle.stateAfter(exampleText)
        val expected = mapOf(
            "d" to 72L,
            "e" to 507L,
            "f" to 492L,
            "g" to 114L,
            "h" to 65412L,
            "i" to 65079L,
            "x" to 123L,
            "y" to 456L
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(579999, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(17837115, result)
    }
}

class Puzzle7 {
    fun solveOne(text: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun stateAfter(exampleText: String): Map<String, Long> {
        val instructions = exampleText.split("\n")
            .map { Instruction.parse(it) }
            .associate { it to false }
            .toMutableMap()

        var state = mapOf<String, Long>()

        // While any of the instructions have not been processed
        while (instructions.values.any { !it }) {

            // Get unprocessed instructions
            val unprocessedInstructions = instructions.entries.filter { !it.value }

            unprocessedInstructions.forEach { (instruction ) ->

                val (wasSuccesful, newState) = instruction.execute(state)

                if (wasSuccesful) {
                    state = newState
                    instructions[instruction] = true
                }
            }
        }

        return state
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

        fun execute(state: Map<String, Long>): ExecutionResult

        fun tryGetValue(state: Map<String, Long>, valueOrRegisterName: String): Long?  {
            // It's a constant
            valueOrRegisterName.toLongOrNull()?.let { return it }

            // It's a registerName
            return state[valueOrRegisterName]
        }
    }

    data class ExecutionResult(val succeeded: Boolean, val state: Map<String, Long>) {
        companion object {
            val Failed = ExecutionResult(false, mapOf())
            fun Succeeded(state: Map<String, Long>) = ExecutionResult(true, state)
        }
    }

    data class Set(val operandA: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, Long>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)

            if (operandAValue == null) {
                return ExecutionResult.Failed
            }

            val newState = state + (registerName to operandAValue)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class And(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, Long>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val andResult = operandAValue and operandBValue
            val newState = state + (registerName to andResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class Or(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, Long>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val orResult = operandAValue or operandBValue
            val newState = state + (registerName to orResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class LeftShift(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, Long>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val leftShiftResult = operandAValue shl operandBValue.toInt()
            val newState = state + (registerName to leftShiftResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class RightShift(val operandA: String, val operandB: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, Long>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)
            val operandBValue = tryGetValue(state, operandB)

            if (operandAValue == null || operandBValue == null) {
                return ExecutionResult.Failed
            }

            val leftShiftResult = operandAValue shr operandBValue.toInt()
            val newState = state + (registerName to leftShiftResult)
            return ExecutionResult.Succeeded(newState)
        }
    }

    data class Not(val operandA: String, val registerName: String): Instruction {
        override fun execute(state: Map<String, Long>): ExecutionResult {
            val operandAValue = tryGetValue(state, operandA)

            if (operandAValue == null) {
                return ExecutionResult.Failed
            }

            val notResult = operandAValue.inv()
            val newState = state + (registerName to notResult)
            return ExecutionResult.Succeeded(newState)
        }
    }
}