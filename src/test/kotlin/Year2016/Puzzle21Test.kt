package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle21Test {
    val puzzle = Puzzle21()
    val puzzleText = this::class.java.getResource("/2016/puzzle21.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("gbhcefad", result)
    }

    @Test
    fun `can solve part b`() {
        // 108 is too low
        val result= puzzle.solveTwo(puzzleText)
        assertEquals(109, result)
    }

    @Test
    fun `rotate abcde 1 position to the left`() {
        val result = Puzzle21.RotateLeft(1).execute("abcde")
        assertEquals("bcdea", result)
    }

    @Test
    fun `rotate abcde 1 position to the right`() {
        val result = Puzzle21.RotateRight(1).execute("abcde")
        assertEquals("eabcd", result)
    }

    @Test
    fun `rotate abdec based on the position of letter b`() {
        val result = Puzzle21.RotateBasedOnPosition('b').execute("abdec")
        assertEquals(result, "ecabd")
    }

    @Test
    fun `reverse edcba for indexes 0 to 4`() {
        val result = Puzzle21.ReversePositions(0, 4).execute("edcba")
        assertEquals("abcde", result)
    }

    @Test
    fun `move char to a later index`() {
        val result = Puzzle21.MovePosition(1, 4).execute("bcdea")
        assertEquals("bdeac", result)
    }

    @Test
    fun `move char to a lesser index`() {
        val result = Puzzle21.MovePosition(4, 1).execute("bcdea")
        assertEquals("bacde", result)
    }

    @Test
    fun `why do you explode?`() {
        val result = Puzzle21.RotateBasedOnPosition('d').execute("bchgfead")
        assertEquals("dbchgfea", result)
    }
}

class Puzzle21 {
    data class SwapPosition(val indexA: Int, val indexB: Int) : Operation {
        override fun execute(text: String): String {
            val buffer = StringBuilder(text)
            buffer.setCharAt(indexA, text[indexB])
            buffer.setCharAt(indexB, text[indexA])
            return buffer.toString()
        }
    }

    data class SwapLetter(val charA: Char, val charB: Char) : Operation {
        override fun execute(text: String): String {
            val indexA = text.indexOf(charA)
            val indexB = text.indexOf(charB)

            val buffer = StringBuilder(text)
            buffer.setCharAt(indexA, text[indexB])
            buffer.setCharAt(indexB, text[indexA])
            return buffer.toString()
        }
    }

    data class RotateLeft(val steps: Int) : Operation {
        override fun execute(text: String): String {
            val buffer = StringBuilder()
            buffer.append(text.substring(steps, text.length))
            buffer.append(text.substring(0, steps))
            return buffer.toString()
        }
    }

    data class RotateRight(val steps: Int) : Operation {
        override fun execute(text: String): String {
            val buffer = StringBuilder()
            val midPoint = text.length - (steps % text.length)
            buffer.append(text.substring(midPoint, text.length))
            buffer.append(text.substring(0, midPoint))
            return buffer.toString()
        }
    }

    data class RotateBasedOnPosition(val letter: Char) : Operation {
        override fun execute(text: String): String {
            val indexOfLetter = text.indexOf(letter)
            val rotateCount = if (indexOfLetter >= 4) indexOfLetter + 1 + 1 else indexOfLetter + 1
            return RotateRight(rotateCount).execute(text)
        }
    }

    data class ReversePositions(val from: Int, val to: Int) : Operation {
        override fun execute(text: String): String {
            val buffer = StringBuilder()
            buffer.append(text.substring(0, from))
            buffer.append(text.substring(from, to + 1).reversed())
            buffer.append(text.substring(to + 1, text.length))
            return buffer.toString()
        }
    }

    data class MovePosition(val fromIndex: Int, val toIndex: Int) : Operation {
        override fun execute(text: String): String {
            val buffer = StringBuilder(text)
            buffer.deleteCharAt(fromIndex)
            buffer.insert(toIndex, text[fromIndex])
            return buffer.toString()
        }
    }

    interface Operation {
        companion object {
            fun parse(line: String): Operation {
                val tmp = line.split(" ")

                return when {
                    line.startsWith("swap position") -> SwapPosition(tmp[2].toInt(), tmp[5].toInt())
                    line.startsWith("swap letter") -> SwapLetter(tmp[2][0], tmp[5][0])
                    line.startsWith("rotate left") -> RotateLeft(tmp[2].toInt())
                    line.startsWith("rotate right") -> RotateRight(tmp[2].toInt())
                    line.startsWith("rotate based on position") -> RotateBasedOnPosition(tmp[6][0])
                    line.startsWith("reverse positions") -> ReversePositions(tmp[2].toInt(), tmp[4].toInt())
                    line.startsWith("move position") -> MovePosition(tmp[2].toInt(), tmp[5].toInt())
                    else -> throw RuntimeException("Asdasdasd")
                }
            }
        }

        fun execute(text: String): String
    }

    fun solveOne(puzzleText: String): String {
        val operations = puzzleText.split("\n").map(Operation.Companion::parse)
        var text = "abcdefgh"

        operations.forEach { operation ->
            try {
                text = operation.execute(text)
            }
            catch (e: Exception) {
                println("Blew up on operation = $operation")
                throw e
            }
        }

        return text
    }

    fun solveTwo(puzzleText: String): Long {
        return 100L
    }
}
