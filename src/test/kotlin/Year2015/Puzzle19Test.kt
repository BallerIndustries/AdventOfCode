package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle19Test {
    val puzzle = Puzzle19()
    val puzzleText = this::class.java.getResource("/2015/puzzle19.txt").readText().replace("\r", "")
    val exampleText = """
        H => HO
        H => OH
        O => HH

        HOH
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(4, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        // 129 too low
        // 627 too high
        assertEquals(509, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(781, result)
    }

    @Test
    fun `replace 0-1 with AAA`() {
        val result = puzzle.replaceRange("HOH", IntRange(0, 0), "HO")
        assertEquals("HOOH", result)
    }
}

class Puzzle19 {
    data class Transition(val from: String, val to: String)

    fun solveOne(puzzleText: String): Int {
        val (transitionText , startingMolecule) = puzzleText.split("\n\n")

        val transitions = transitionText.split("\n").map { line ->
            val (from, to) = line.split(" => ")
            Transition(from, to)
        }

        val octopus = transitions.flatMap { transition ->
            val regex = Regex(transition.from)
            regex.findAll(startingMolecule).map { jur: MatchResult ->

                val dog = replaceRange(startingMolecule, jur.range, transition.to)
                dog
            }.toList()
        }.toSet()

        return octopus.size
    }

    fun replaceRange(startingMolecule: String, range: IntRange, to: String): String {
        val before = startingMolecule.substring(0, range.first)
        val after = startingMolecule.substring(range.last + 1)

        return before + to + after
    }

    fun solveTwo(puzzleText: String): Int {
        return 394839
    }
}