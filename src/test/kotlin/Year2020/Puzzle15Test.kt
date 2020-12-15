package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle15.txt").readText().replace("\r", "")
    val puzzle = Puzzle15()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(203, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(9007186, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "0,3,6"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(436, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "0,3,6"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(175594, result)
    }

    @Test
    fun `iterate a lot`() {
        var number = 0
        (0 until 30_000_000).forEach {
            number++
        }
    }
}

class Puzzle15 {
    class LastTwo(var index0: Int, var index1: Int?) {
        fun add(index: Int): LastTwo {
            this.index1 = this.index0
            this.index0 = index
            return this
        }

        fun size(): Int {
            return when (index1) {
                null -> 1
                else -> 2
            }
        }
    }

    private fun solve(endTurn: Int, puzzleText: String): Int {
        val numbers = puzzleText.split(",").map { it.toInt() }
        val numbersToLastTwo = numbers.mapIndexed { index, number ->
            number to LastTwo(index+1, null)
        }.toMap().toMutableMap()

        var turn = numbers.size + 1
        var currentNumber = numbers.last()

        while (turn <= endTurn) {
            val lastTwo = numbersToLastTwo[currentNumber]!!

            currentNumber = when(lastTwo.size()) {
                1 -> 0
                2 -> lastTwo.index0!! - lastTwo.index1!!
                else -> throw RuntimeException()
            }

            val jurness = numbersToLastTwo[currentNumber]
            numbersToLastTwo[currentNumber] = jurness?.add(turn) ?: LastTwo(index0 = turn, index1 = null)
            turn++
        }

        return currentNumber
    }

    fun solveOne(puzzleText: String): Int {
        return solve(2020, puzzleText)
    }

    fun solveTwo(puzzleText: String): Int {
        return solve(30_000_000, puzzleText)
    }
}

