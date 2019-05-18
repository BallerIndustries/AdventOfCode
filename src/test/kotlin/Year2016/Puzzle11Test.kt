package Year2016

import Year2016.Puzzle11.*
import Year2016.Puzzle11.State
import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle11Test {
    val puzzle = Puzzle11()
    val puzzleText = Puzzle11Test::class.java.getResource("/2016/puzzle11.txt").readText().replace("\r","")
    val exampleText = """
        The first floor contains a hydrogen-compatible microchip, and a lithium-compatible microchip.
        The second floor contains a hydrogen generator.
        The third floor contains a lithium generator.
        The fourth floor contains nothing relevant.
    """.trimIndent()


    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(11, result)
    }

    @Test
    fun `puzzle part a`() {
        // 195 too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals(12122, result)
    }

    @Test
    fun `puzzle part b`() {
        val result: String = puzzle.solveTwo(puzzleText)
        assertEquals("", result)
    }

    @Test
    fun `can parse puzzle input`() {
        val actualFloors = puzzle.parseText(puzzleText)
        val expectedFloors = mapOf(
                1 to Floor(number = 1, things = setOf(Microchip("thulium"), Generator("thulium"), Generator("plutonium"), Generator("strontium"))),
                2 to Floor(number = 2, things = setOf(Microchip("plutonium"), Microchip("strontium"))),
                3 to Floor(number = 3, things = setOf(Generator("promethium"), Microchip("promethium"), Generator("ruthenium"), Microchip("ruthenium"))),
                4 to Floor(number = 4, things = setOf())
        )

        assertEquals(expectedFloors, actualFloors)
    }

    @Test
    fun `can parse example input`() {
        val actualFloors = puzzle.parseText(exampleText)
        val expectedFloors = mapOf(
            1 to Floor(number = 1, things = setOf(Microchip(name = "hydrogen"), Microchip("lithium"))),
            2 to Floor(number = 2, things = setOf(Generator(name = "hydrogen"))),
            3 to Floor(number = 3, things = setOf(Generator(name = "lithium"))),
            4 to Floor(number = 4, things = setOf())
        )

        assertEquals(expectedFloors, actualFloors)
    }

    @Test
    fun `should be able to transition to another floor from this state`() {
        val floors = mapOf(
                1 to Floor(number = 1, things = setOf(Microchip(name = "hydrogen"), Microchip("lithium"))),
                2 to Floor(number = 2, things = setOf(Generator(name = "hydrogen"))),
                3 to Floor(number = 3, things = setOf(Generator(name = "lithium"))),
                4 to Floor(number = 4, things = setOf())
        )

        val state = Puzzle11.State(elevator = Puzzle11.Elevator(floorNumber = 1), floors = floors)

        val expectedFloors = mapOf(
            1 to Floor(number = 1, things = setOf(Microchip("lithium"))),
            2 to Floor(number = 2, things = setOf(Microchip(name = "hydrogen"), Generator(name = "hydrogen"))),
            3 to Floor(number = 3, things = setOf(Generator(name = "lithium"))),
            4 to Floor(number = 4, things = setOf())
        )

        val expectedNextStates = listOf(State(elevator = Elevator(2), floors = expectedFloors))
        val actualNextStates = state.nextStates(setOf())
        assertEquals(expectedNextStates, actualNextStates)
    }

    @Test
    fun `floor with one microchip on it should be a valid floor`() {
        val floor = Floor(1, setOf(Microchip("lithium")))
        assertEquals(true, floor.isValid())
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

        fun isValid(): Boolean {
            if (number < 1 || number > 4) return false

            //if a chip is ever left in the same area as another RTG, and it's not connected to its own RTG, the chip will be fried.
            val chipsOnThisFloor = things.mapNotNull { it as? Microchip }
            val generatorsOnThisFloor = things.mapNotNull { it as? Generator }

            if (generatorsOnThisFloor.isEmpty()) {
                return true
            }

            return chipsOnThisFloor.all { microchip ->
                generatorsOnThisFloor.find { it.name == microchip.name } != null
            }
        }
    }

    data class Elevator(val floorNumber: Int) {
        fun moveUp() = this.copy(this.floorNumber + 1)

        fun moveDown() = this.copy(this.floorNumber - 1)

        fun isValid() = floorNumber in 1..4
    }

    data class State(val elevator: Elevator, val floors: Map<Int, Floor>) {

        fun isGoal(): Boolean {
            val topFloorNotEmpty = floors[4]!!.things.isNotEmpty()
            val otherFloorsAreEmpty = (1 .. 3).all { floors[it]!!.things.isEmpty() }
            return topFloorNotEmpty && otherFloorsAreEmpty
        }

        private fun isValid(): Boolean {
            return elevator.isValid() && floors.values.all { floor -> floor.isValid() }
        }

        fun nextStates(visitedStates: Set<State>): List<State> {
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

            return nextStates.filter { it.isValid() && !visitedStates.contains(it) }
        }
    }

    fun solveOne(puzzleText: String): Int {
        val initialFloors = parseText(puzzleText)
        var min = Int.MAX_VALUE

        (0 .. 100000).forEach {
            val number = randomRandomRANDOM(initialFloors, min)
            if (number < min) min = number
            if (it % 100 == 0) println(min)
        }

        return min
    }

    private fun randomRandomRANDOM(initialFloors: Map<Int, Floor>, min: Int): Int {
        val initialElevator = Elevator(1)
        var currentState = State(initialElevator, initialFloors)
        val visitedStates = mutableSetOf(currentState)
        var moves = 0

        while (!currentState.isGoal()) {
            if (moves > min) {
                return Int.MAX_VALUE
            }

            val nextStates = currentState.nextStates(visitedStates)

            if (nextStates.isEmpty()) {
                return Int.MAX_VALUE
            }

            currentState = nextStates.random()
            moves++
        }

        return moves
    }

    fun parseText(puzzleText: String): Map<Int, Floor> {
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

