package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class Puzzle23Test {
    val puzzle = Puzzle23()
    val puzzleText = this::class.java.getResource("/2017/puzzle23.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(6724, result)
    }

    @Test
    @Ignore("Toooo slow")
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(0, result)
    }
}

class Puzzle23 {

    fun solveOne(puzzleText: String): Long {
        val codes = puzzleText.split("\n").map { line -> Instruction.parsePartOne(line) }
        var state = State()

        try {
            while (true) {
                state = runProgram(codes, state)
            }
        }
        catch (e: IndexOutOfBoundsException) {
        }

        return state.mulCount
    }

    fun solveTwo(puzzleText: String): Long {
        val codes = puzzleText.split("\n").map { line -> Instruction.parsePartOne(line) }
        var state = State(registers = mapOf('a' to 1L))

        try {
            while (true) {
                state = runProgram(codes, state)
            }
        }
        catch (e: IndexOutOfBoundsException) {
        }

        return state.mulCount
    }

    data class State(
        val programCounter: Long = 0L,
        val offset: Long? = null,
        val registers: Map<Char, Long> = mapOf(),
        val mulCount: Long = 0L
    )

    private fun runProgram(codes: List<Instruction>, programStateConst: State): State {
        var programState = programStateConst
        val instruction = codes[programState.programCounter.toInt()]
        programState = instruction.execute(programState)

        val programCounter = if (programState.offset != null) programState.programCounter + programState.offset!! else programState.programCounter + 1
        programState = programState.copy(offset = null, programCounter = programCounter)

        return programState
    }

    data class Set(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            // Set register x to the value of y
            val yValue = getValueOrRegister(state, y)
            println("setting $x = $yValue")
            val newRegisters = state.registers + (x to yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Sub(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val yValue = getValueOrRegister(state, y)
            val xValue = getValueOrRegister(state, x.toString())
            println("setting $x = $xValue - $yValue")

            val newRegisters = state.registers + (x to xValue - yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Mul(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val yValue = getValueOrRegister(state, y)
            val xValue = getValueOrRegister(state, x.toString())
            println("setting $x = $xValue * $yValue")
            val newRegisters = state.registers + (x to xValue * yValue)
            return state.copy(registers = newRegisters, mulCount = state.mulCount + 1)
        }
    }



    data class Jnz(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val xValue = getValueOrRegister(state, x.toString())
            val yValue = getValueOrRegister(state, y)

            if (xValue != 0L) {
                println("jumping offset = $yValue")
                return state.copy(offset = yValue)
            }
            else {
                println("skipping jump $x = xValue")
                return state
            }
        }
    }

    interface Instruction {
        companion object {
            fun parsePartOne(text: String): Instruction {
                val tmp = text.split(" ")

                return when (tmp[0]) {
                    "set" -> Set(tmp[1][0], tmp[2])
                    "sub" -> Sub(tmp[1][0], tmp[2])
                    "mul" -> Mul(tmp[1][0], tmp[2])
                    "jnz" -> Jnz(tmp[1][0], tmp[2])
                    else -> throw RuntimeException("fuck!")
                }
            }
        }

        fun execute(state: State): State

        fun getValueOrRegister(state: State, valueOrRegister: String): Long {
            return if (valueOrRegister.toLongOrNull() != null) {
                valueOrRegister.toLong()
            } else {
                state.registers[valueOrRegister[0]] ?: 0
            }
        }
    }
}