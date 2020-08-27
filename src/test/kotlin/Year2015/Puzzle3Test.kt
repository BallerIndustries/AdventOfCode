package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle3.txt").readText().replace("\r", "")
    val puzzle = Puzzle3()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2592, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(2360, result)
    }
}

data class Point(val x: Int, val y: Int)

class Puzzle3 {
    fun solveOne(puzzleText: String): Int {
        return runSimulation(puzzleText).count()
    }

    private fun runSimulation(puzzleText: String): Map<Point, Int> {
        var x = 0
        var y = 0
        val initial = mapOf(Point(x, y) to 1)

        return puzzleText.fold(initial) { map, char ->
            when (char) {
                '^' -> y--
                '>' -> x++
                'v' -> y++
                '<' -> x--
                else -> throw RuntimeException("Unexpected character")
            }

            val key = Point(x, y)
            val presentCount = (map[key] ?: 0) + 1
            map + (key to presentCount)
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val santaOrders = puzzleText.filterIndexed { index, _ -> index % 2 == 0 }
        val roboSantaOrders = puzzleText.filterIndexed { index, _ -> index % 2 == 1 }
        val santaState = runSimulation(santaOrders)
        val roboSantaState = runSimulation(roboSantaOrders)

        return (santaState + roboSantaState).count()
    }
}