package Year2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {
    val puzzle = Puzzle18()
    val puzzleText = this::class.java.getResource("/2016/puzzle18.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1951, result)
    }

    @Test
    fun `can solve part b`() {
        val result= puzzle.solveTwo(puzzleText)
        assertEquals(20002936, result)
    }
}

class Puzzle18 {
    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun left() = this.copy(x = this.x - 1)
        fun right() = this.copy(x = this.x + 1)
    }

    fun isTrap(buffer: Array<Array<Boolean>>, point: Point, maxX: Int): Boolean {
        if (point.x < 0 || point.x >= maxX) {
            return false
        }

        return buffer[point.y][point.x]
    }

    fun solveOne(puzzleText: String, rowCount: Int = 40): Int {
        val buffer = Array(rowCount) { Array( puzzleText.length) {false} }

        puzzleText.forEachIndexed { index, char ->
            buffer[0][index] = char == '^'
        }

        (1 until rowCount).forEach { y ->
            (0 until puzzleText.length).forEach { x ->

                val thisPoint = Point(x, y)
                val upPoint = thisPoint.up()

                val left = isTrap(buffer, upPoint.left(), puzzleText.length)
                val center = isTrap(buffer, upPoint, puzzleText.length)
                val right = isTrap(buffer, upPoint.right(), puzzleText.length)

                val thisJerkIsATrap = when {
                    left && center && !right -> true
                    right && center && !left -> true
                    left && !right && !center -> true
                    right && !center && !left -> true
                    else -> false
                }

                buffer[y][x] = thisJerkIsATrap
            }

            if (y % 10000 == 0) println(y)
        }

        return buffer.sumBy { it.count { !it  } }
    }

    fun solveTwo(puzzleText: String): Int {
        return solveOne(puzzleText, 400000)
    }
}
