package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle2.txt").readText().replace("\r", "")
    val puzzle = Puzzle2()

    @Test
    fun `example part a`() {
        val puzzleText = "1-3 a: abcde\n" +
                "1-3 b: cdefg\n" +
                "2-9 c: ccccccccc"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(569, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(346, result)
    }
}

class Puzzle2 {
    fun solveOne(puzzleText: String): Int {
        return puzzleText.split("\n").count { text ->
            val (range, letterComponent, password) = text.split(" ")
            val (min, max) = range.split("-").map { it.toInt() }
            val letter = letterComponent[0]

            password.count { it == letter } in min..max
        }
    }

    fun solveTwo(puzzleText: String): Int {
        return puzzleText.split("\n").count { text ->
            val (range, letterComponent, password) = text.split(" ")
            val (indexA, indexB) = range.split("-").map { it.toInt() - 1 }
            val letter = letterComponent[0]

            val posAlEtter = password[indexA]
            val posBLetter = password[indexB]
            (posAlEtter == letter) xor (posBLetter == letter)
        }
    }
}

