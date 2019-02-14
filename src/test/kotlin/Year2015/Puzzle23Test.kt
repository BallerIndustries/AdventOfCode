package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle23Test {
    val puzzle = Puzzle23()
    val puzzleText = this::class.java.getResource("/2015/puzzle23.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(255, result)
    }

    @Test
    fun `puzzle part b`() {
        // 103 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(23862900, result)
    }
}

class Puzzle23 {

    data class State(val registerA: Int, val registerB: Int, val programCounter: Int) {
        companion object {
            fun initial() = State(0, 0, 0)
            fun hyperBeamX() = State(1, 0, 0)
        }

        fun incrementProgramCounter() = this.copy(programCounter = this.programCounter + 1)
    }

    data class Half(val registerName: String) : Instruction {
        override fun execute(state: State): State {
            //println("Half registerName = $registerName")

            return when (registerName) {
                "a" -> state.copy(registerA = state.registerA / 2).incrementProgramCounter()
                "b" -> state.copy(registerB = state.registerB / 2).incrementProgramCounter()
                else -> throw RuntimeException("AAAAAAA!!!!!")
            }
        }
    }

    data class Triple(val registerName: String) : Instruction {
        override fun execute(state: State): State {
            //println("Triple registerName = $registerName")

            return when (registerName) {
                "a" -> state.copy(registerA = state.registerA * 3).incrementProgramCounter()
                "b" -> state.copy(registerB = state.registerB * 3).incrementProgramCounter()
                else -> throw RuntimeException("AAAAAAA!!!!!")
            }
        }
    }

    data class Increment(val registerName: String) : Instruction {
        override fun execute(state: State): State {
            //println("Increment registerName = $registerName")

            return when (registerName) {
                "a" -> state.copy(registerA = state.registerA + 1).incrementProgramCounter()
                "b" -> state.copy(registerB = state.registerB + 1).incrementProgramCounter()
                else -> throw RuntimeException("AAAAAAA!!!!!")
            }
        }
    }

    data class Jump(val jumpAmount: Int) : Instruction {
        override fun execute(state: State): State {
            //println("Jump jumpAmount = $jumpAmount")
            return state.copy(programCounter = state.programCounter + jumpAmount)
        }
    }

    data class JumpIfEven(val registerName: String, val jumpAmount: Int) : Instruction {
        override fun execute(state: State): State {
            //println("JumpIfEven registerName = $registerName jumpAmount = $jumpAmount")
            val registerValue = when (registerName) {
                "a" -> state.registerA
                "b" -> state.registerB
                else -> throw RuntimeException("AAAAAAA!!!!!")
            }

            return if (registerValue % 2 == 0) state.copy(programCounter = state.programCounter + jumpAmount) else state.incrementProgramCounter()
        }
    }

    data class JumpIfOne(val registerName: String, val jumpAmount: Int) : Instruction {
        override fun execute(state: State): State {
            //println("JumpIfOne registerName = $registerName jumpAmount = $jumpAmount")

            val registerValue = when (registerName) {
                "a" -> state.registerA
                "b" -> state.registerB
                else -> throw RuntimeException("AAAAAAA!!!!!")
            }

            return if (registerValue == 1) state.copy(programCounter = state.programCounter + jumpAmount) else state.incrementProgramCounter()
        }
    }

    interface Instruction {
        companion object {
            fun parseLine(line: String): Instruction {
                val segments = line.split(" ")
                val dogs = line.split(", ")
                //val horseSpaghetti = dogs.get[1]

                return when (segments[0]) {
                    "hlf" -> Half(segments[1])
                    "tpl" -> Triple(segments[1])
                    "inc" -> Increment(segments[1])
                    "jmp" -> Jump(segments[1].toInt())
                    "jie" -> JumpIfEven(segments[1][0].toString(), dogs[1].toInt())
                    "jio" -> JumpIfOne(segments[1][0].toString(), dogs[1].toInt())
                    else -> throw RuntimeException("ArghH!!! RUN TIME EXCEPTION!!!!")
                }
            }
        }

        fun execute(state: State): State
    }

    fun solveOne(puzzleText: String): Int {
        val instructions = puzzleText.split("\n").map { Instruction.parseLine(it) }
        var currentState = State.initial()

        while (currentState.programCounter >= 0 && currentState.programCounter < instructions.count()) {
            println(currentState.programCounter)
            val currentInstruction = instructions[currentState.programCounter]
            //println(currentState)
            currentState = currentInstruction.execute(currentState)

        }

        return currentState.registerB
    }

    fun solveTwo(puzzleText: String): Int {
        val instructions = puzzleText.split("\n").map { Instruction.parseLine(it) }
        var currentState = State.hyperBeamX()

        while (currentState.programCounter >= 0 && currentState.programCounter < instructions.count()) {
            println(currentState.programCounter)
            val currentInstruction = instructions[currentState.programCounter]
            //println(currentState)
            currentState = currentInstruction.execute(currentState)

        }

        return currentState.registerB
    }
}