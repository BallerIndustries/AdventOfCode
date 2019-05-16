package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle11Test {
    val puzzle = Puzzle11()
    val puzzleText = Puzzle11Test::class.java.getResource("/2016/puzzle11.txt").readText().replace("\r","")

    @Test
    fun `puzzle part a`() {
        val result: String = puzzle.solveOne(puzzleText)
        assertEquals("", result)
    }

    @Test
    fun `puzzle part b`() {
        val result: String = puzzle.solveTwo(puzzleText)
        assertEquals("", result)
    }
}

class Puzzle11 {
    interface Thing {
        companion object {
            fun parse(line: String): Thing? {
                val cleanedLine = line.replace("a ", "").replace("and", "").replace(".", "").trim()

                return when {
                    cleanedLine.contains("-compatible microchip") -> {
                        Microchip(cleanedLine.replace("-compatible microchip", ""))
                    }
                    cleanedLine.contains("generator") -> {
                        Generator(cleanedLine.replace(" generator", ""))
                    }
                    else -> null
                }
            }
        }

        val name: String
    }

    data class Generator(override val name: String): Thing

    data class Microchip(override val name: String): Thing

    data class Floor(val number: Int, val things: Set<Thing>)

    fun solveOne(puzzleText: String): String {
        val floors = parseText(puzzleText)

        return ""
    }

    private fun parseText(puzzleText: String): List<Floor> {
        return puzzleText.split("\n").mapIndexed { index, line ->
            val things = line.split(" ")
                .let { it.subList(5, it.size) }
                .joinToString(" ")
                .split(", ")
                .mapNotNull(Thing.Companion::parse)
                .toSet()

            val floor = Floor(index + 1, things)
            floor
        }
    }

    fun solveTwo(puzzleText: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

