package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class Puzzle5Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle5.txt").readText().replace("\r", "")
    val puzzle = Puzzle5()

    @Test
    fun `getRow tests`() {
        assertEquals(44, puzzle.getRow("FBFBBFFRLR"))
        assertEquals(70, puzzle.getRow("BFFFBBFRRR"))
        assertEquals(14, puzzle.getRow("FFFBBBFRRR"))
        assertEquals(102, puzzle.getRow("BBFFBBFRLL"))
    }

    @Test
    fun `getColumn tests`() {
        assertEquals(5, puzzle.getColumn("FBFBBFFRLR"))
        assertEquals(7, puzzle.getColumn("BFFFBBFRRR"))
        assertEquals(7, puzzle.getColumn("FFFBBBFRRR"))
        assertEquals(4, puzzle.getColumn("BBFFBBFRLL"))
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(987, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(603, result)
    }
}

class Puzzle5 {
    fun solveOne(puzzleText: String): Int {
        return puzzleText.split("\n").map { decodeBoardingPass(it).third }.max()!!
    }

    fun solveTwo(puzzleText: String): Int {
        val results = puzzleText.split("\n").map { decodeBoardingPass(it) }

        val maxRow = results.maxBy { it.first }!!.first
        val minRow = results.minBy { it.first }!!.first

        val rowWithAnEmptySeat: List<Triple<Int, Int, Int>> = results.filter { it.first != maxRow && it.first != minRow }
                .groupBy { it.first }
                .filter { it.value.size == 7 }
                .values.first()

        val zazaColumNumbers: Set<Int> = rowWithAnEmptySeat.map { it.second }.toSet()
        val allColumnNumbers: Set<Int> = (0..7).toSet()

        val myColumnNumber = (allColumnNumbers - zazaColumNumbers).first()
        val myRowNumber = rowWithAnEmptySeat.first().first

        return (myRowNumber * 8) + myColumnNumber
    }

    fun decodeBoardingPass(puzzleText: String): Triple<Int, Int, Int> {
        val row = getRow(puzzleText)
        val column = getColumn(puzzleText)
        val seatId = (row * 8) + column

        return Triple(row, column, seatId)
    }

    fun getRow(puzzleText: String): Int {
        var max = 127
        var min = 0
        var length = 128

        (0..6).forEach {
            val char = puzzleText[it]
            length /= 2

            if (char == 'F') {
                max = min + length - 1
            } else if (char == 'B') {
                min = min + length
            }

//            println("($min, $max)")
        }

        if (min != max) {
            throw RuntimeException()
        }

        return min
    }

    fun getColumn(puzzleText: String): Int {
        var max = 7
        var min = 0
        var length = max - min + 1

        (7..9).forEach {
            val char = puzzleText[it]
            length /= 2

            if (char == 'L') {
                max = min + length - 1
            } else if (char == 'R') {
                min = min + length
            }

//            println("($min, $max)")
        }

        if (min != max) {
            throw RuntimeException()
        }

        return min
    }
}

