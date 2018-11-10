package Year2016

import Year2016.Puzzle15.Disc
import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle15Test {

    val puzzle = Puzzle15()
    val inputText =
        "Disc #1 has 5 positions; at time=0, it is at position 4.\n" +
        "Disc #2 has 2 positions; at time=0, it is at position 1."

    @Test
    fun `can parse example input text`() {
        val discPositions = puzzle.parseText(inputText)
        assertEquals(listOf(Disc(1, 5, 4, 4), Disc(2, 2, 1, 1)), discPositions)
    }

    @Test
    fun `first disc at position zero at t=6`() {
        throw RuntimeException()
//        val discPositions = puzzle.parseText(inputText)

//        val discPositions = puzzle.discPositionsAsOf(inputText, 5).map { it.currentPosition }
//        assertEquals(listOf(0, ), discPositions)

    }
}

class Puzzle15 {
    data class Disc(val discNumber: Int, val positions: Int, val initialPosition: Int, val currentPosition: Int) {
        fun asOf(t: Int): Disc {
            return Disc(discNumber, positions, initialPosition, initialPosition + t % positions)
        }
    }

    fun parseText(inputText: String): List<Disc> {
        return inputText.split("\n").map { line ->
            val tmp = line.split(" ")

            val discNumber = tmp[1].replace("#", "").toInt()
            val positions = tmp[3].toInt()
            val initialPosition = tmp[11].replace(".", "").toInt()

            Disc(discNumber, positions, initialPosition, initialPosition)
        }
    }

    fun discPositionsAsOf(inputText: String, t: Int): List<Disc> {
        val initialDiscPositions = parseText(inputText)

        return initialDiscPositions.map { it.asOf(t) }
    }
}
