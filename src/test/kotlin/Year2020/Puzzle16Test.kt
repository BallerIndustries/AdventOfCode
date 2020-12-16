package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle16.txt").readText().replace("\r", "")
    val puzzle = Puzzle16()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(964875, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "class: 1-3 or 5-7\n" +
                "row: 6-11 or 33-44\n" +
                "seat: 13-40 or 45-50\n" +
                "\n" +
                "your ticket:\n" +
                "7,1,14\n" +
                "\n" +
                "nearby tickets:\n" +
                "7,3,47\n" +
                "40,4,50\n" +
                "55,2,20\n" +
                "38,6,12"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(71, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = ""
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(241861950, result)
    }
}

class Puzzle16 {
    data class Rule(val name: String, val range1: IntRange, val range2: IntRange) {

        fun validValue(value: Int): Boolean {
            return value in range1 || value in range2
        }

        fun invalidValue(value: Int) = !validValue(value)
    }

    fun solveOne(puzzleText: String): Int {
        val (rulesText, yourTicket, nearbyTicketsText) = puzzleText.split("\n\n")

        val rules = parseRules(rulesText)
        val split = nearbyTicketsText.split("\n")
        val nearbyTickets = split.subList(1, split.size).map { it.split(",").map { it.toInt() } }

        return nearbyTickets.sumBy { nearbyTicket ->
            nearbyTicket.sumBy { value ->
                if (rules.all { rule -> rule.invalidValue(value) }) {
                    value
                }
                else {
                    0
                }
            }
        }



//        return 1
    }

    fun parseRules(rulesText: String): List<Rule> {
        val rules = rulesText.split("\n").map { text ->
            val name = text.split(":")[0]
            val (range1, range2) = text.split(": ")[1].split(" or ").map {
                val (start, end) = it.split("-").map { it.toInt() }
                IntRange(start, end)
            }

            Rule(name, range1, range2)
        }

        return rules
    }

    fun solveTwo(puzzleText: String): Int {
        return 1
    }
}

