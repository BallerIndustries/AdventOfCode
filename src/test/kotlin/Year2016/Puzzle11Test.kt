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

    data class Floor(val number: Int, val things: Set<Thing>) {
        fun withTheseThings(things: List<Thing>) = this.copy(things = this.things + things)

        fun withoutTheseThings(things: List<Thing>) = this.copy(things = this.things - things)
    }

    data class Elevator(val floorNumber: Int) {
        fun moveUp() = this.copy(this.floorNumber + 1)
        fun moveDown() = this.copy(this.floorNumber - 1)

        fun isValid() = floorNumber in 1..4
    }

    data class State(val elevator: Elevator, val floors: Map<Int, Floor>) {

        fun isValid(): Boolean {
            return elevator.isValid() && floors.values.all { floor -> floor.isValid() }
        }

        fun nextStates(): List<State> {

            // Elevator can go up or down
            val elevatorStates = listOf(elevator.moveUp(), elevator.moveDown())
            val currentFloor = floors[elevator.floorNumber]!!

            // List of one item lists
            val singlesToCarry = currentFloor.things.toList()
            val pairsToCarry = mutableListOf<Pair<Thing, Thing>>()

            for (index in 0 until singlesToCarry.size) {
                for (jindex in index + 1 until singlesToCarry.size) {
                    pairsToCarry.add(singlesToCarry[index] to singlesToCarry[jindex])
                }
            }

            // Variable name of the year
            val listOfListOfThingsToCarryToAnotherFloor = singlesToCarry.map { listOf(it) } + pairsToCarry.map { listOf(it.first, it.second) }
            println("listOfListOfThingsToCarryToAnotherFloor = $listOfListOfThingsToCarryToAnotherFloor")

            val elevatorAndItsContents = elevatorStates.flatMap { elevator ->
                listOfListOfThingsToCarryToAnotherFloor.map { contents ->
                    elevator to contents
                }
            }

            val nextStates = elevatorAndItsContents.map { (nextElevator, contents) ->

                val currentFloor = floors[elevator.floorNumber] ?: Floor(elevator.floorNumber, setOf())
                val nextFloor = floors[nextElevator.floorNumber] ?: Floor(nextElevator.floorNumber, setOf())
                val currentFloorChanged = currentFloor.withoutTheseThings(contents)
                val nextFloorChanged = nextFloor.withTheseThings(contents)

                val nextFloors = floors + mapOf(currentFloorChanged.number to currentFloorChanged, nextFloorChanged.number to nextFloorChanged)
                State(nextElevator, nextFloors)
            }

            // Elevator can have 1 item or 2 items
            throw NotImplementedError("asdasda")

        }
    }

    fun solveOne(puzzleText: String): String {
        val initialFloors = parseText(puzzleText)
        val initialElevator = Elevator(1)
        val initialState = State(initialElevator, initialFloors)

        initialState.nextStates()

        // Elevator can move up or down
        // Elevator must take 1 or 2 items when going up or down
        // if a chip is ever left in the same area as another RTG, and it's not connected to its own RTG, the chip will be fried.


        return "798798798789"
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

