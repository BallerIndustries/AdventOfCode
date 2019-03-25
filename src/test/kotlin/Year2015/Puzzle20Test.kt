package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException
import java.util.*
import kotlin.random.Random

class Puzzle20Test {
    val puzzle = Puzzle20()
    val puzzleText = this::class.java.getResource("/2015/puzzle20.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(4, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(509, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(205, result)
    }

}

class Puzzle20 {
    fun getMultiples(number: Int): List<Int> {
        return (1..number).filter { aCandidateForTheOfficeOfMultiple ->
            number % aCandidateForTheOfficeOfMultiple == 0
        }
    }

    fun solveOne(puzzleText: String): Int {
        val magicNumber = puzzleText.toInt()

//        val numberToMultiples = generateMemo(magicNumber)


        (500_000 until Int.MAX_VALUE).forEach { houseNumber ->

            val numPresentsReceived = getMultiples(houseNumber).map { it * 10 }.sum()

            if (houseNumber % 1000 == 0) {
                println("houseNumber = $houseNumber numPresentsReceived = $numPresentsReceived")
            }

            if (numPresentsReceived > magicNumber) {
                return houseNumber
            }
        }

        throw RuntimeException("Summin strange be happnin")
    }

    private fun generateMemo(magicNumber: Int): Map<Int, MutableSet<Int>> {
        val jur = mutableMapOf<Int, MutableSet<Int>>()

        (1 .. magicNumber).forEach { multiple ->

            if (multiple % 10 == 0) {
                println(multiple)
            }

            var currentNumber = multiple

            while (currentNumber < magicNumber) {
                val setOfMultiples = jur[currentNumber] ?: mutableSetOf()
                setOfMultiples.add(currentNumber)
                jur[currentNumber] = setOfMultiples
                currentNumber += multiple
            }
        }

        return jur
    }

    fun solveTwo(puzzleText: String): Int {
        return 222
    }
}