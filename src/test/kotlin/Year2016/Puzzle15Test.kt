package Year2016

import Year2016.Puzzle15.Disc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {
    val puzzle = Puzzle15()
    val puzzleText = this::class.java.getResource("/2016/puzzle15.txt").readText().replace("\r", "")

    val exampleText =
        "Disc #1 has 5 positions; at time=0, it is at position 4.\n" +
        "Disc #2 has 2 positions; at time=0, it is at position 1."

    @Test
    fun `can solve part a`() {
        val result: Int = puzzle.solveOne(puzzleText)
        assertEquals(result, 400589)
    }

    @Test
    fun `can solve part b`() {
        val result: Int = puzzle.solveTwo(puzzleText)
        assertEquals(result, 3045959)
    }

    @Test
    fun `can parse example input text`() {
        val discPositions = puzzle.parseText(exampleText)
        assertEquals(listOf(Disc(1, 5, 4), Disc(2, 2, 1)), discPositions)
    }

    @Test
    fun `when t = 0 for disc with five positions starting at position 4`() {
        val disc = Disc(1, 5, 4)
        assertEquals(4, disc.positionAtTime(0))
    }

    @Test
    fun `when t = 1 for disc with five positions starting at position 4`() {
        val disc = Disc(1, 5, 4)
        assertEquals(0, disc.positionAtTime(1))
    }

    @Test
    fun `when t = 2 for disc with five positions starting at position 4`() {
        val disc = Disc(1, 5, 4)
        assertEquals(1, disc.positionAtTime(2))
    }

    @Test
    fun `when t = 3 for disc with five positions starting at position 4`() {
        val disc = Disc(1, 5, 4)
        assertEquals(2, disc.positionAtTime(3))
    }

    @Test
    fun `when t = 4 for disc with five positions starting at position 4`() {
        val disc = Disc(1, 5, 4)
        assertEquals(3, disc.positionAtTime(4))
    }

    @Test
    fun `when t = 5 for disc with five positions starting at position 4`() {
        val disc = Disc(1, 5, 4)
        assertEquals(4, disc.positionAtTime(5))
    }

    @Test
    fun `when t = 6 for disc with five positions starting at position 4`() {
        val disc = Disc(1, 5, 4)
        assertEquals(0, disc.positionAtTime(6))
    }
}

class Puzzle15 {
    data class Disc(val discNumber: Int, val positions: Int, val initialPosition: Int) {
        fun positionAtTime(t: Int): Int {
            return (initialPosition + t) % positions
        }
    }

    fun parseText(inputText: String): List<Disc> {
        return inputText.split("\n").map { line ->
            val tmp = line.split(" ")
            val discNumber = tmp[1].replace("#", "").toInt()
            val positions = tmp[3].toInt()
            val initialPosition = tmp[11].replace(".", "").toInt()

            Disc(discNumber, positions, initialPosition)
        }
    }

    fun solveOne(puzzleText: String): Int {
        val discs = parseText(puzzleText)
        return solvePlease(discs)
    }

    private fun solvePlease(discs: List<Disc>): Int {
        for (time in 0 until Int.MAX_VALUE) {
            val discsToTimes = discs.map { disc -> disc to time + disc.discNumber }

            if (discsToTimes.all { (disc, time) -> disc.positionAtTime(time) == 0 }) {
                return time
            }
        }

        throw RuntimeException("Fuck a duck")
    }

    fun solveTwo(puzzleText: String): Int {
        val discs = parseText(puzzleText)
        val bottomDisc = Disc(discs.last().discNumber + 1, 11, 0)
        return solvePlease(discs + bottomDisc)
    }
}
