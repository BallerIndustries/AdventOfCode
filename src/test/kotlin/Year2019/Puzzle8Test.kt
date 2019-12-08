package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle8Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle8.txt").readText().replace("\r", "")
    val puzzle = Puzzle8()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `example a`() {
        val dog = "003456789012"
        val result = puzzle.solveOne(dog, 3, 2)
        assertEquals("a", result)
    }
}

class Puzzle8 {
    fun solveOne(puzzleText: String, width: Int = 25, height: Int = 6): Int {
        val layerSize = width * height
        val numLayers = puzzleText.length / layerSize

        val layerWithLeastZeros = puzzleText.chunked(layerSize).minBy { layer -> layer.count { char -> char == '0' } }!!
        return layerWithLeastZeros.count { it == '1' }!! * layerWithLeastZeros.count { it == '2'}!!
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

