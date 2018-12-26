package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle22Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle22.txt").readText().replace("\r", "")
    val puzzle = Puzzle22()

    @Test
    fun `example 1`() {
        val text = """
            depth: 510
            target: 10,10
        """.trimIndent()

        val result = puzzle.solveOne(text)
        assertEquals(114, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(420, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    class Puzzle22 {

        enum class RegionType(val riskLevel: Int) { ROCKY(0), WET(1), NARROW(2) }

        data class Point(val x: Int, val y: Int) {
            companion object {
                val ZERO = Point(0, 0)

                fun from(text: String): Point {
                    return text.split(",").map { it.toInt() }.let { Point(it[0], it[1]) }
                }
            }

            fun calculateGeologicIndex(target: Point, depth: Int): Int {
                return when {
                    this == ZERO -> 0
                    this == target -> 0
                    y == 0 -> x * 16807
                    x == 0 -> y * 48271
                    else -> {
                        val a = this.copy(x = x - 1)
                        val b = this.copy(y = y - 1)
                        a.calculateErosionLevel(target, depth) * b.calculateErosionLevel(target, depth)
                    }
                }
            }

            private fun calculateErosionLevel(target: Point, depth: Int): Int {
                return (this.calculateGeologicIndex(target, depth) + depth) % 20183
            }

            fun calculateRegionType(target: Point, depth: Int): RegionType {
                val horse = calculateErosionLevel(target, depth) % 3

                return when (horse) {
                    0 -> RegionType.ROCKY
                    1 -> RegionType.WET
                    2 -> RegionType.NARROW
                    else -> throw RuntimeException("horse should be between 0 and 2 weird! horse = $horse")
                }
            }
        }

        fun solveOne(puzzleText: String): Int {
            val (depth, target) = puzzleText.replace("depth: ", "")
                .replace("target: ", "")
                .split("\n")
                .let { Pair(it[0].toInt(), Point.from(it[1])) }

            println("depth = $depth target = $target")

            val regionTypes = (0 .. target.y).flatMap { y ->
                (0 .. target.x).map { x ->
                    Point(x, y).calculateRegionType(target, depth)
                }
            }

            return regionTypes.sumBy { it.riskLevel }
        }



        fun solveTwo(puzzleText: String): Int {
            return 1337
        }
    }
}