package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle1Test {
    val puzzleText = Puzzle1Test::class.java.getResource("/2019/puzzle1.txt").readText().replace("\r", "")
    val puzzle = Puzzle1()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(3404722, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(5104215, result)
    }
}

class Puzzle1 {
    fun solveOne(puzzleText: String): Int {
        return puzzleText.split("\n").map { it.toInt() }.sumBy {someNumber ->

            (someNumber / 3) - 2

        }
    }

    fun solveTwo(puzzleText: String): Int {
        return puzzleText.split("\n").map { it.toInt() }.sumBy {someNumber ->
            var sum = 0
            var lastThing = someNumber

            while (lastThing > 0) {
                lastThing = (lastThing / 3) - 2
                println("lastThing = ${lastThing}")


                if (lastThing > 0) {
                    sum += lastThing
                }
            }


            sum
        }
    }
}

