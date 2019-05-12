package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle21Test {
    val puzzle = Puzzle21()
    val puzzleText = this::class.java.getResource("/2016/puzzle21.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(22887907, result)
    }

    @Test
    fun `can solve part b`() {
        // 108 is too low
        val result= puzzle.solveTwo(puzzleText)
        assertEquals(109, result)
    }
}

class Puzzle21 {
    class SwapPosition(val from: Int, val to: Int) : Operation {
        override fun execute(text: String): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class SwapLetter(val from: Char, val to: Char) : Operation {
        override fun execute(text: String): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class RotateLeft(val steps: Int) : Operation {
        override fun execute(text: String): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class RotateRight(val steps: Int) : Operation {
        override fun execute(text: String): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class RotateBasedOnPosition(val letter: Char) : Operation {
        override fun execute(text: String): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class ReversePositions(val from: Int, val to: Int) : Operation {
        override fun execute(text: String): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class MovePosition() : Operation {
        override fun execute(text: String): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    interface Operation {
        companion object {
            fun parse(line: String): Operation {
                val tmp = line.split(" ")

                return when {
                    line.startsWith("swap position") -> SwapPosition(tmp[2].toInt(), tmp[5].toInt())
                    else -> throw RuntimeException("Asdasdasd")
                }
            }
        }

        fun execute(text: String): String
    }

    fun solveOne(puzzleText: String): Long {
        return 200L
    }

    fun solveTwo(puzzleText: String): Long {
        return 100L
    }
}
