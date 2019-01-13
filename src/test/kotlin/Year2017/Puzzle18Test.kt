package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle18Test {
    val puzzle = Puzzle18()
    val puzzleText = this::class.java.getResource("/2017/puzzle18.txt").readText().replace("\r", "")
    val exampleText = """
        set a 1
        add a 2
        mul a a
        mod a 5
        snd a
        set a 0
        rcv a
        jgz a -1
        set a 1
        jgz a -2
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(4, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(9423, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(41797835, result)
    }
}

class Puzzle18 {
    companion object {
        var lastSoundPlayed = -1L
        var offset = 0L
        var recoverInstruction = 0L
    }

    fun solveOne(puzzleText: String): Long {
        val codes = puzzleText.split("\n").map { line -> Instruction.parse(line) }
        var state = mapOf<Char, Long>()
        var programCounter = 0L

        while (true) {
            val instruction = codes[programCounter.toInt()]

            println(instruction)
            println(state)
            println("programCounter = $programCounter")
            state = instruction.execute(state)
            println()
            programCounter = if (offset != 0L) programCounter + offset else programCounter + 1
            offset = 0

            if (recoverInstruction != 0L) {
                return recoverInstruction
            }
        }
    }

    fun solveTwo(puzzleText: String): Int {
        return 348934983
    }


    data class Snd(val x: String) : Instruction {
        override fun execute(state: Map<Char, Long>): Map<Char, Long> {
            lastSoundPlayed = getValueOrRegister(state, x)
            println("playing sound with frequency = $x")
            return state
        }
    }

    data class Set(val x: Char, val y: String) : Instruction {
        override fun execute(state: Map<Char, Long>): Map<Char, Long> {
            // Set register x to the value of y
            val yValue = getValueOrRegister(state, y)
            println("setting $x = $yValue")
            return state + (x to yValue)
        }
    }

    data class Add(val x: Char, val y: String) : Instruction {
        override fun execute(state: Map<Char, Long>): Map<Char, Long> {
            val yValue = getValueOrRegister(state, y)
            val xValue = state[x] ?: 0
            println("setting $x = $xValue + $yValue")
            return state + (x to xValue + yValue)
        }
    }

    data class Mul(val x: Char, val y: String) : Instruction {
        override fun execute(state: Map<Char, Long>): Map<Char, Long> {
            val yValue = getValueOrRegister(state, y)
            val xValue = state[x] ?: 0
            println("setting $x = $xValue * $yValue")
            return state + (x to xValue * yValue)
        }
    }

    data class Mod(val x: Char, val y: String) : Instruction {
        override fun execute(state: Map<Char, Long>): Map<Char, Long> {
            val yValue = getValueOrRegister(state, y)
            val xValue = state[x] ?: 0
            println("setting $x = $xValue % $yValue")
            return state + (x to xValue % yValue)
        }
    }

    data class Rcv(val x: Char) : Instruction {
        override fun execute(state: Map<Char, Long>): Map<Char, Long> {
            val xValue = getValueOrRegister(state, x.toString())

            if (xValue != 0L) {
                recoverInstruction = lastSoundPlayed
            }
            else {
                println("failed to recover, $x = $xValue")
            }

            return state
        }
    }

    data class Jgz(val x: Char, val y: String) : Instruction {
        override fun execute(state: Map<Char, Long>): Map<Char, Long> {
            val xValue = getValueOrRegister(state, x.toString())
            val yValue = getValueOrRegister(state, y)

            if (xValue > 0) {
                println("jumping offset = $yValue")
                offset = yValue
            }
            else {
                println("skipping jump $x = 0")
            }

            return state
        }
    }

    interface Instruction {
        companion object {
            fun parse(text: String): Instruction {
                val tmp = text.split(" ")

                return when (tmp[0]) {
                    "snd" -> Snd(tmp[1])
                    "set" -> Set(tmp[1][0], tmp[2])
                    "add" -> Add(tmp[1][0], tmp[2])
                    "mul" -> Mul(tmp[1][0], tmp[2])
                    "mod" -> Mod(tmp[1][0], tmp[2])
                    "rcv" -> Rcv(tmp[1][0])
                    "jgz" -> Jgz(tmp[1][0], tmp[2])
                    else -> throw RuntimeException("fuck!")
                }
            }
        }

        fun execute(state: Map<Char, Long>): Map<Char, Long>

        fun getValueOrRegister(state: Map<Char, Long>, valueOrRegister: String): Long {
            if (valueOrRegister.toLongOrNull() != null) {
                return valueOrRegister.toLong()
            }
            else {
                return state[valueOrRegister[0]] ?: 0
            }
        }
    }
}