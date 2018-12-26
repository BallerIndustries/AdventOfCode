package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle25Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle25.txt").readText().replace("\r", "")
    val puzzle = Puzzle25()

    @Test
    fun `puzzle part a`() {
        // 15885 too low
        val result = puzzle.solveOne(puzzleText)
        assertEquals(15919, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    data class Point(val x: Int, val y: Int, val z: Int, val t: Int, var neighbours: MutableList<Point> = mutableListOf())



    class Puzzle25 {
        fun manhattanDistance(a: Point, b: Point): Int {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z) + Math.abs(a.t - b.t)
        }

        fun solveOne(puzzleText: String): Int {
            val points = puzzleText.split("\n").map { line -> line.split(",").map { it.toInt() }.let { Point(it[0], it[1], it[2], it[3]) } }
            val constellations = mutableListOf(mutableListOf<Point>())

            for (index in 0 until points.size) {

                val currentPoint = points[index]
                val pointsAhead: List<Point> = points.subList(index + 1, points.size)
                        .filter { manhattanDistance(currentPoint, it) <= 3 }

                // Add all the neighbours
                currentPoint.neighbours.addAll(pointsAhead)
            }








            return 239847
        }

        fun solveTwo(puzzleText: String): Int {
            return 290834
        }
    }
}