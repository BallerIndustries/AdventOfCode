package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle11Test {
    val puzzle = Puzzle11()
    val puzzleText = this::class.java.getResource("/2015/puzzle11.txt").readText().replace("\r", "")
    val exampleText = "1"

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(252594, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3579328, result)
    }
}

class Puzzle11 {
    fun hasThreeCharStraight(text: String): Boolean {
        (2 until text.length).forEach { index ->
            val c1 = text[index-2]
            val c2 = text[index-1]
            val c3 = text[index]

            if ((c2 - c1 == 1) && (c3 - c2 == 1)) {
                return true
            }
        }

        return false
    }

    fun hasNoIOOrL(text: String) = !text.contains('i') && !text.contains('o') && !text.contains('l')

    fun hasTwoNonOverlappingPairs(text: String): Boolean {
        val pairs = mutableSetOf<String>()

        (1 until text.length).forEach { index ->
            val c1 = text[index-1]
            val c2 = text[index]

            if (c1 == c2) pairs.add("$c1$c2")
            if (pairs.size == 2) return true
        }

        return false
    }

    fun passwordToInt(password: String): Int {
        var multiplier = 1
        var buffer = 0

        (password.lastIndex .. 0).forEach { index ->
            val char = password[index]
            val cheese = (char.toInt() - 97) * multiplier
            buffer += cheese
            multiplier *= 26
        }

        return buffer
    }

//    fun intToPassword(number: Int): String {
//        var multiplier = 1
//    }

    fun solveOne(puzzleText: String): Int {
        return 1281928
    }

    fun solveTwo(puzzleText: String): Int {
        return 1281928
    }
}