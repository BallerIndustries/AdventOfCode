package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

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

    data class Elevator(val floorNumber: Int) {
        fun moveUp() = this.copy(this.floorNumber + 1)
        fun moveDown() = this.copy(this.floorNumber - 1)
    }

    data class State(val elevator: Elevator, val floors: Map<Int, Floor>) {
        fun nextStates(): List<State> {

            // Elevator can go up or down
            // Elevator can have 1 item or 2 items
            throw NotImplementedError("asdasda")

        }
    }

    fun solveOne(puzzleText: String): String {
        val initialFloors = parseText(puzzleText)
        val initialElevator = Elevator(1)
        val initialState = State(initialElevator, initialFloors)

        // Elevator can move up or down
        // Elevator must take 1 or 2 items when going up or down
        // if a chip is ever left in the same area as another RTG, and it's not connected to its own RTG, the chip will be fried.


        return ""
    }

    private fun parseText(puzzleText: String): Map<Int, Floor> {
        return puzzleText.split("\n").mapIndexed { index, line ->
            val things = line.split(" ")
                .let { it.subList(5, it.size) }
                .joinToString(" ")
                .split(", ")
                .mapNotNull(Thing.Companion::parse)
                .toSet()

            val floor = Floor(index + 1, things)
            floor.number to floor
        }.toMap()
    }

    fun solveTwo(puzzleText: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

