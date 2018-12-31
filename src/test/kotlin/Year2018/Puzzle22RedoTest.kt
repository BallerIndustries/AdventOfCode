package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle22RedoTest {
    val puzzleText = this::class.java.getResource("/2018/puzzle22.txt").readText().replace("\r", "")
    val puzzle = Puzzle22()

    val exampleText = """
            depth: 510
            target: 10,10
        """.trimIndent()

    @Test
    fun `example 1`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(114, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(5622, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    data class Point(val x: Int, val y: Int)

    enum class Type(val value: Int) { ROCKY(0), WET(1), NARROW(2) }

    class Puzzle22 {

        private val geologicIndexCache = mutableMapOf<Point, Int>()

        fun geologicIndex(point: Point, target: Point, depth: Int): Int {
            val (x, y ) = point

            return when {
                x == 0 && y == 0 -> 0
                point == target -> 0
                y == 0 -> x * 16807
                x == 0 -> y * 48271
                else -> {
                    val left = Point(x - 1, y)
                    val north = Point(x, y - 1)
                    erosionLevel(left, target, depth) * erosionLevel(north, target, depth)
                }
            }
        }

        private fun erosionLevel(point: Point, target: Point, depth: Int): Int {
            if (!geologicIndexCache.containsKey(point)) {
                val erosionLevel =  (geologicIndex(point, target, depth) + depth) % 20183
                geologicIndexCache[point] = erosionLevel
            }

            return geologicIndexCache[point]!!
        }

        private fun type(point: Point, target: Point, depth: Int): Type {
            val jur = erosionLevel(point, target, depth) % 3
            return Type.values().find { it.value == jur }!!
        }

        fun solveOne(puzzleText: String): Int {
            val (firstLine, secondLine) = puzzleText.split("\n")

            val depth = firstLine.replace("depth: ", "").toInt()
            val target = secondLine.replace("target: ", "").split(",").map { it.toInt() }.let { Point(it[0], it[1]) }

            return (0 .. target.y).sumBy { y ->
                (0 .. target.x).sumBy { x ->
                    val point = Point(x, y)
                    type(point, target, depth).value
                }
            }
        }

        fun solveTwo(puzzleText: String): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}