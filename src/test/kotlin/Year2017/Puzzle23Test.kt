package Year2017

import junit.framework.Assert.assertEquals
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
    fun `puzzle part b`() {
        val result = puzzle.solveTwo()
        assertEquals(903, result)
    }
}

class Puzzle23 {
    fun solveOne(puzzleText: String): Long {
        val codes = puzzleText.split("\n").map { line -> Instruction.parsePartOne(line) }
        var state = State()

        try { while (true) state = runProgram(codes, state) }
        catch (e: IndexOutOfBoundsException) { }

        return state.mulCount
    }

    fun solveTwo(): Long {
        var a = 1L
        var b = 0L
        var c = 0L
        var d = 0L
        var e = 0L
        var f = 0L
        var g = 0L
        var h = 0L

        b = 84
        c = b

        if (a != 0L) {
            b = b * 100 + 100000
            c = b + 17000
        }

        do {
            f = 1
            d = 2
            e = 2

            while (d * d <= b) {
                if (b % d == 0L) {
                    f = 0
                    break
                }

                d++
            }

            if (f == 0L) {
                h++
            }

            g = b - c
            b += 17
        } while (g != 0L)

        return h
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
            val yValue = getValueOrRegister(state, y)
            val newRegisters = state.registers + (x to yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Sub(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val yValue = getValueOrRegister(state, y)
            val xValue = getValueOrRegister(state, x.toString())

            val newRegisters = state.registers + (x to xValue - yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Mul(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val yValue = getValueOrRegister(state, y)
            val xValue = getValueOrRegister(state, x.toString())
            val newRegisters = state.registers + (x to xValue * yValue)
            return state.copy(registers = newRegisters, mulCount = state.mulCount + 1)
        }
    }

    data class Jnz(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val xValue = getValueOrRegister(state, x.toString())
            val yValue = getValueOrRegister(state, y)

            if (xValue != 0L) {
                return state.copy(offset = yValue)
            }
            else {
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