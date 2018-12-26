package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.io.File

class Puzzle22Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle22.txt").readText().replace("\r", "")
    val puzzle = Puzzle22()

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

        enum class RegionType { ROCKY, WET, NARROW }

        data class Point(val x: Int, val y: Int) {
            companion object {
                fun from(text: String): Point {
                    return text.split(",").map { it.toInt() }.let { Point(it[0], it[1]) }
                }
            }

            fun calculateGeologicIndex(depth: Int): Int {
                return when {
                    x == 0 && y == 0 -> 0
                    x == 10 && y == 10 -> 0
                    y == 0 -> x * 16807
                    x == 0 -> y * 48271
                    else -> {
                        val a = this.copy(x = x - 1)
                        val b = this.copy(y = y - 1)
                        a.erosionLevel(depth) * b.erosionLevel(depth)
                    }
                }
            }

            private fun erosionLevel(depth: Int): Int {
                return this.calculateGeologicIndex(depth) + depth
            }
        }

        fun solveOne(puzzleText: String): Int {
            val (depth, target) = puzzleText.replace("depth: ", "")
                .replace("target: ", "")
                .split("\n")
                .let { Pair(it[0].toInt(), Point.from(it[1])) }

            println("depth = $depth target = $target")

            return 38943
        }



        fun solveTwo(puzzleText: String): Int {
            return 1337
        }
    }
}