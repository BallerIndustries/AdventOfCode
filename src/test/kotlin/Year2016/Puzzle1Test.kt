package Year2016

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class Puzzle1Test {

    val puzzleOne = Puzzle1()

    @Test
    fun `R2, L3`() {
        val input = "R2, L3"
        val numberOfBlocks = puzzleOne.countBlocks(input)
        assertEquals(numberOfBlocks, 5)
    }

    @Test
    fun `R2, R2, R2`() {
        val input = "R2, R2, R2"
        val numberOfBlocks = puzzleOne.countBlocks(input)
        assertEquals(numberOfBlocks, 2)
    }

    @Test
    fun `R5, L5, R5, R3`() {
        val input = "R5, L5, R5, R3"
        val numberOfBlocks = puzzleOne.countBlocks(input)
        assertEquals(numberOfBlocks, 12)
    }

    @Test
    fun `visited twice R8, R4, R4, R8`() {
        val input = "R8, R4, R4, R8"
        val numberOfBlocks = puzzleOne.findFirstPlaceVisitedTwice(input)
        assertEquals(numberOfBlocks, 4)
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle1.txt").readText()
        val numberOfBlocks = puzzleOne.countBlocks(input)
        assertEquals(numberOfBlocks, 262)
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle1.txt").readText()
        val numberOfBlocks = puzzleOne.findFirstPlaceVisitedTwice(input)
        assertEquals(numberOfBlocks, 131)
    }

    @Test
    fun `fromHereToThere x low to high`() {
        val positions = puzzleOne.positionsFromHereToThere(Point(0, 0), Point(3, 0))
        assertEquals(positions, listOf(Point(1, 0), Point(2, 0), Point(3, 0)))
    }

    @Test
    fun `fromHereToThere x high to high`() {
        val positions = puzzleOne.positionsFromHereToThere(Point(3, 0), Point(0, 0))
        assertEquals(positions, listOf(Point(2, 0), Point(1, 0), Point(0, 0)))
    }
}

