package Year2015

import generateGrid
import junit.framework.Assert.*
import org.junit.Test

class Puzzle7Test {
    val puzzle = Puzzle7()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle7.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(579999, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(17837115, result)
    }
}

class Puzzle7 {
    fun solveOne(puzzleText: String): Int {
        val state = mutableMapOf<String, Int>()

        puzzleText.split("\n").forEach { line ->
            if (line.contains("AND")) {

            }
            else if (line.contains("OR")) {

            }
            else if (line.contains("NOT")) {

            }
            else if (line.contains("LSHIFT")) {

            }
            else if (line.contains("RSHIFT")) {

            }
            else if (line.contains("->")) {

            }
            else {
                throw RuntimeException()
            }



        }


        return 0
    }

    fun solveTwo(puzzleText: String): Int {
       return 0
    }
}