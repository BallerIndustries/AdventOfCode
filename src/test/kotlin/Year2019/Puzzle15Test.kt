package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle15Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle15.txt").readText().replace("\r", "")
    val puzzle = Puzzle15()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(212, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(358, result)
    }
}

class Puzzle15 {
    data class Point(val x: Int, val y: Int) {
        fun handleMove(direction: Direction): Point {
            return when (direction) {
                Direction.UP -> this.up()
                Direction.DOWN -> this.down()
                Direction.LEFT -> this.left()
                Direction.RIGHT -> this.right()
            }
        }

        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)
        fun neighbours() = listOf(up(), down(), left(), right())
    }

    enum class Direction(val value: Long) { UP(1), DOWN(2), LEFT(3), RIGHT(4) }

    enum class RepairDroidRespond(val value: Long) {
        HIT_A_WALL(0), MOVED_INTO_EMPTY_TILE(1), MOVED_INTO_OXYGEN_SYSTEM(2);

        companion object {
            fun parse(value: Long): RepairDroidRespond {
                return values().find { it.value == value } ?: throw RuntimeException()
            }
        }
    }

    data class RobotState(val position: Point, val direction: Direction, val programState: State, val stepCount: Int) {
        override fun toString(): String {
            return "RobotState(position=$position, direction=$direction, stepCount=$stepCount)"
        }
    }

    fun solveOne(puzzleText: String): Int {
        val (_, steps) = resolveGrid(puzzleText)
        return steps
    }

    private fun resolveGrid(puzzleText: String): Pair<Map<Point, Char>, Int> {
        val virtualMachine = IntCodeVirtualMachine()
        val program = puzzleText.split(",").map { it.toLong() } + puzzleText.map { 0L }
        var currentState = State(program, userInput = listOf())
        var currentPoint = Point(0, 0)
        val frontier = Direction.values().map { RobotState(currentPoint, it, currentState, 1) }.toMutableList()
        val grid = mutableMapOf(currentPoint to '.')
        val history = mutableSetOf<Pair<Point, Direction>>()
        var minStepsToOxygen = Int.MAX_VALUE

        while (frontier.isNotEmpty()) {
            val robotState = frontier.removeAt(0)
            currentState = robotState.programState.addUserInput(robotState.direction.value)
            currentState = virtualMachine.runProgram(currentState)

            val tmp = currentState.popOffOutput()
            val output = tmp.first
            currentState = tmp.second
            val response = RepairDroidRespond.parse(output.first())

            when (response) {
                RepairDroidRespond.HIT_A_WALL -> {
                    markPointAsWall(grid, robotState.position, robotState.direction)
                    currentPoint = robotState.position
                    //println("Was at ${robotState.position} tried to move ${robotState.direction} HIT_A_WALL now at $currentPoint")
                }
                RepairDroidRespond.MOVED_INTO_EMPTY_TILE -> {
                    markPointAsOpen(grid, robotState.position, robotState.direction)
                    currentPoint = robotState.position.handleMove(robotState.direction)
                    //println("Was at ${robotState.position} tried to move ${robotState.direction} MOVED_INTO_EMPTY_TILE now at $currentPoint. Steps taken = ${robotState.stepCount}")
                }
                RepairDroidRespond.MOVED_INTO_OXYGEN_SYSTEM -> {
                    markPointAsOxygenSystem(grid, robotState.position, robotState.direction)
                    currentPoint = robotState.position.handleMove(robotState.direction)
                    //println("Was at ${robotState.position} tried to move ${robotState.direction} MOVED_INTO_OXYGEN_SYSTEM now at $currentPoint. Steps taken = ${robotState.stepCount}")
                    minStepsToOxygen = robotState.stepCount
                }
            }

            history.add(robotState.position to robotState.direction)

            val nextMoves = createNextMoves(currentPoint, currentState, robotState.stepCount).filter { nextMove ->
                !history.contains(nextMove.position to nextMove.direction) && frontier.none { it.position == nextMove.position && it.direction == nextMove.direction }
            }

            frontier.addAll(nextMoves)
        }

        return grid to minStepsToOxygen
    }

    private fun createNextMoves(currentPoint: Point, state: State, lastStepCount: Int): List<RobotState> {
        return Direction.values().map {
            RobotState(currentPoint, it, state, lastStepCount + 1)
        }
    }

    private fun renderGrid(grid: Map<Point, Char>, currentPoint: Point) {
        val maxX = grid.keys.maxBy { it.x }!!.x
        val maxY = grid.keys.maxBy { it.y }!!.y
        val minX = grid.keys.minBy { it.x }!!.x
        val minY = grid.keys.minBy { it.y }!!.y

        val buffer = (minY .. maxY).map { y ->
            (minX .. maxX).map { x ->

                if (currentPoint.x == x && currentPoint.y == y) {
                    'R'
                }
                else {
                    grid[Point(x, y)] ?: '?'
                }

            }.joinToString("")
        }.joinToString("\n")

        println(buffer)
    }

    private fun markPointAsOxygenSystem(grid: MutableMap<Point, Char>, position: Point, nextMove: Direction) {
        grid[position.handleMove(nextMove)] = 'O'
    }

    private fun markPointAsOpen(grid: MutableMap<Point, Char>, position: Point, nextMove: Direction) {
        grid[position.handleMove(nextMove)] = '.'
    }

    private fun markPointAsWall(grid: MutableMap<Point, Char>, position: Point, nextMove: Direction) {
        grid[position.handleMove(nextMove)] = '#'
    }

    fun solveTwo(puzzleText: String): Int {
        val grid = resolveGrid(puzzleText).first.toMutableMap()
        var minutesTaken = 0

        while (grid.values.any { it == '.' }) {
            val zonesToFillUp = grid.entries
                .filter { it.value == 'O' }
                .flatMap { it.key.neighbours() }
                .toSet()
                .filter { grid[it] == '.' }

            zonesToFillUp.forEach {
                grid[it] = 'O'
            }

            minutesTaken++
        }

        return minutesTaken
    }
}

