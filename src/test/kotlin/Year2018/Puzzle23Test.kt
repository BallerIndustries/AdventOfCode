package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.io.File

class Puzzle23Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle23.txt").readText().replace("\r", "")
    val puzzle = Puzzle23()

    @Test
    fun `puzzle part a`() {

//        val set = setOf<Int>()
//
//        set.delta


        val result = puzzle.solveOne(puzzleText)
        assertEquals(420, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    data class NanoBot(val position: Point, val radius: Int)

    data class Point(val x: Int, val y: Int, val z: Int)

    class Puzzle23 {
        fun solveOne(puzzleText: String): Int {
            val nanoBots = parseNanoBots(puzzleText)
            val largestRadiusBot = nanoBots.maxBy { it.radius }!!

            return nanoBots
                .map { thisBot -> manhattanDistance(largestRadiusBot.position, thisBot.position) }
                .count { it <= largestRadiusBot.radius }
        }

        private fun parseNanoBots(puzzleText: String): List<NanoBot> {
            return puzzleText.split("\n").map { line ->
                val tmp = line.split(", ")
                val radius = tmp[1].replace("r=", "").toInt()

                val position = tmp[0].replace("pos=", "")
                    .removeSurrounding("<", ">")
                    .split(",")
                    .map { it.toInt() }
                    .let { Point(it[0], it[1], it[2]) }

                NanoBot(position, radius)
            }
        }

        private fun manhattanDistance(a: Point, b: Point): Int =
            Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z)

        fun solveTwo(puzzleText: String): Int {


            return 1337
        }
    }
}