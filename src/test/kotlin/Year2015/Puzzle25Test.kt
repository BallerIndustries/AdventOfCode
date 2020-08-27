package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle25Test {
    val puzzle = Puzzle25()
    val puzzleText = this::class.java.getResource("/2015/puzzle25.txt").readText().replace("\r", "")
    val exampleText = "".trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(8997277, result)
    }

    @Test
    fun `first ten points should be`() {
        val actual = puzzle.getPoints { pointList -> pointList.size == 10 }
        val expected = listOf(
            Puzzle25.Point(1,1),
            Puzzle25.Point(1,2),
            Puzzle25.Point(2,1),
            Puzzle25.Point(1,3),
            Puzzle25.Point(2,2),
            Puzzle25.Point(3,1),
            Puzzle25.Point(1,4),
            Puzzle25.Point(2,3),
            Puzzle25.Point(3,2),
            Puzzle25.Point(4,1)
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `point 2 2 should be 21629792`() {
        val result = puzzle.solveOne("2 2")
        assertEquals(result, 21629792)
    }

    @Test
    fun `point 6 6 should be 27995004`() {
        val result = puzzle.solveOne("6 6")
        assertEquals(result, 27995004)
    }
}

class Puzzle25 {
    data class Point(val x: Int, val y: Int)

    fun getPoints(terminationCondition: (List<Point>) -> Boolean): List<Point> {
        var current = Point(1, 1)

        var maxHeight = 1
        val returnList = mutableListOf(current)

        while (true) {
            if (terminationCondition(returnList)) {
                return returnList
            }

            if (current.y == 1) {
                maxHeight++
                current = Point(1, maxHeight)
            }
            else {
                current = Point(current.x + 1, current.y - 1)
            }

            returnList.add(current)
        }
    }

    fun solveOne(puzzleText: String): Long {
        val (row, column) = puzzleText.split(" ")
            .map { it.replace(".", "").replace(",", "") }
            .mapNotNull { it.toIntOrNull() }

        val pointList = getPoints { pointList -> pointList.last() == Point(column, row) }

        var currentCode = 20151125L
        val theMap = mutableMapOf(Point(1, 1) to currentCode)


        pointList.subList(1, pointList.size).forEach { point ->
            currentCode = (currentCode * 252533L) % 33554393L
            theMap[point] = currentCode
        }

        return theMap[pointList.last()]!!
    }
}