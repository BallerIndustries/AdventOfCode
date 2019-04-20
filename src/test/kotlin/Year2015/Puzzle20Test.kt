package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle20Test {
    val puzzle = Puzzle20()
    val puzzleText = this::class.java.getResource("/2015/puzzle20.txt").readText().replace("\r", "")
    val exampleText = """150"""

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(8, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(831600, result)
    }

    @Test
    fun `puzzle part b`() {
        //3603600 too high
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(205, result)
    }
}

class Puzzle20 {
    // TODO: Memoize
    fun getMultiples(number: Int): List<Int> {
        return (1..number).filter { aCandidateForTheOfficeOfMultiple ->
            number % aCandidateForTheOfficeOfMultiple == 0
        }
    }

    fun solveOne(puzzleText: String): Int {
        val magicNumber = puzzleText.toInt()

        (800000 until Int.MAX_VALUE).forEach { houseNumber ->
            val numPresentsReceived = getMultiples(houseNumber).map { it * 10 }.sum()

            if (houseNumber % 1000 == 0) {
                println("houseNumber = $houseNumber numPresentsReceived = $numPresentsReceived")
            }

            if (numPresentsReceived >= magicNumber) {
                return houseNumber
            }
        }

        throw RuntimeException("Summin strange be happnin")
    }

    //TODO: Do this better you butthead
    fun solveTwo(puzzleText: String): Int {
        val magicNumber = puzzleText.toInt()
        val housesToGifts = mutableMapOf<Int, Int>()
        var minHouse = Int.MAX_VALUE

        // TODO: Should move up by the house number
        (1 until Int.MAX_VALUE).forEach { elfNumber ->

            val firstFiftyHouses = (1 .. 50).map { it * elfNumber }
            val prezziesToDeliver = 11 * elfNumber

            firstFiftyHouses.forEach { houseNumber ->
                val giftsAtThisHouse = (housesToGifts[houseNumber] ?: 0) + prezziesToDeliver
                housesToGifts[houseNumber] = giftsAtThisHouse

                if (giftsAtThisHouse > magicNumber && houseNumber < minHouse) {
                    minHouse = houseNumber
                    println(houseNumber)
                }
            }
        }

        throw RuntimeException("asoidjas")
    }
}