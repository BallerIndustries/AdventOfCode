package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException
import java.util.*

class Puzzle25Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle25.txt").readText().replace("\r", "")
    val puzzle = Puzzle25()

    @Test
    fun `puzzle part a`() {
        // 15885 too low
        val result = puzzle.solveOne(puzzleText)

        // 150 too low
        // 151 too low
        assertEquals(386, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    @Test
    fun `example 1`() {
        val text = """
0,0,0,0
3,0,0,0
0,3,0,0
0,0,3,0
0,0,0,3
0,0,0,6
9,0,0,0
12,0,0,0
        """.trimIndent()

        val dog = puzzle.solveOne(text)
        assertEquals(2, dog)
    }

    @Test
    fun `example 2`() {
        val text = """
-1,2,2,0
0,0,2,-2
0,0,0,-2
-1,2,0,0
-2,-2,-2,2
3,0,2,-1
-1,3,2,2
-1,0,-1,0
0,2,1,-2
3,0,0,0
        """.trimIndent()

        val dog = puzzle.solveOne(text)
        assertEquals(4, dog)
    }

    @Test
    fun `example 3`() {
        val text = """
1,-1,0,1
2,0,-1,0
3,2,-1,0
0,0,3,1
0,0,-1,-1
2,3,-2,0
-2,2,0,0
2,-2,0,-1
1,-1,0,-1
3,2,0,2
        """.trimIndent()

        val dog = puzzle.solveOne(text)
        assertEquals(3, dog)
    }

    @Test
    fun `example 4`() {
        val text = """
1,-1,-1,-2
-2,-2,0,1
0,2,1,3
-2,3,-2,1
0,2,3,-2
-1,-1,1,-2
0,-2,-1,0
-2,2,3,-1
1,2,2,0
-1,-2,0,-2
        """.trimIndent()

        val dog = puzzle.solveOne(text)
        assertEquals(8, dog)
    }

    data class Point(val x: Int, val y: Int, val z: Int, val t: Int, var neighbours: MutableList<Point> = mutableListOf())



    class Puzzle25 {
        fun manhattanDistance(a: Point, b: Point): Int {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z) + Math.abs(a.t - b.t)
        }

        fun solveOne(puzzleText: String): Int {
            val points = puzzleText.split("\n").map { line -> line.split(",").map { it.toInt() }.let { Point(it[0], it[1], it[2], it[3]) } }
            val horse = points.map { point -> getConstellation(point, points) }.distinct()
            return horse.size
        }

        fun getConstellation(point: Point, allPoints: List<Point>): Set<Point> {
            val otherPoints = allPoints.filter { it != point }

            val toProcess = LinkedList<Point>()
            toProcess.add(point)
            val constellation = mutableSetOf(point)

            while (toProcess.isNotEmpty()) {
                val currentPoint = toProcess.removeFirst()
                val neighborsToCurrentPoint = otherPoints.filter { !constellation.contains(it) && manhattanDistance(it, currentPoint) <= 3 }

                constellation.addAll(neighborsToCurrentPoint)
                toProcess.addAll(neighborsToCurrentPoint)
            }

            return constellation
        }


        fun solveTwo(puzzleText: String): Int {
            return 290834
        }
    }
}