package Year2015

import generateGrid
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {
    val puzzle = Puzzle6()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle6.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(569999, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(17836115, result)
    }
}

class Puzzle6 {
    fun solveOne(puzzleText: String): Int {
        val lights = generateGrid(1000, 1000, false)

        puzzleText.split("\n").forEach { line ->
            val (from, to) = parsePointsFromLine(line)

            if (line.startsWith("turn on")) {
                goAcrossPoints(lights, from, to) { true }
            }
            else if (line.startsWith("turn off")) {
                goAcrossPoints(lights, from, to) { false }
            }
            else if (line.startsWith("toggle")) {
                goAcrossPoints(lights, from, to) { !it }
            }
        }

        return lights.flatten().count { it }
    }

    fun solveTwo(puzzleText: String): Int {
        val lights = generateGrid(1000, 1000, 0)

        puzzleText.split("\n").forEach { line ->
            val (from, to) = parsePointsFromLine(line)

            if (line.startsWith("turn on")) {
                goAcrossPoints(lights, from, to) { it + 1 }
            }
            else if (line.startsWith("turn off")) {
                goAcrossPoints(lights, from, to) { Math.max(0, it - 1) }
            }
            else if (line.startsWith("toggle")) {
                goAcrossPoints(lights, from, to) { it + 2 }
            }
        }

        return lights.flatten().sum()
    }

    private fun<E> goAcrossPoints(lights: MutableList<MutableList<E>>, from: Point, to: Point, op: (E) -> E) {
        (from.x .. to.x).forEach { x ->
            (from.y .. to.y).forEach { y ->
                lights[x][y] = op(lights[x][y])
            }
        }
    }

    private fun parsePointsFromLine(line: String): List<Point> {
        return line.split(" ")
            .mapNotNull { if (it.contains(',')) it else null }
            .map { it.split(",") }
            .map { Point(it[0].toInt(), it[1].toInt()) }
    }
}