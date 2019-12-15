package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle15Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle15.txt").readText().replace("\r", "")
    val puzzle = Puzzle15()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
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
                return RepairDroidRespond.values().find { it.value == value } ?: throw RuntimeException()
            }
        }
    }

    data class RobotState(val position: Point,  val nextMove: Direction, val programState: State)

    fun solveOne(puzzleText: String): String {
        val virtualMachine = IntCodeVirtualMachine()
        val program = puzzleText.split(",").map { it.toLong() } + puzzleText.map { 0L }
        var currentState = State(program, userInput = listOf())
        var currentPoint = Point(0, 0)
        val frontier = Direction.values().map { RobotState(currentPoint, it, currentState) }.toMutableList()
        val grid = mutableMapOf(currentPoint to '.')

        while (frontier.isNotEmpty()) {
            val robotState = frontier.removeAt(0)
            currentState = robotState.programState.addUserInput(robotState.nextMove.value)
            currentState = virtualMachine.runProgram(currentState)

            val response = RepairDroidRespond.parse(currentState.outputList.first())

            when (response) {
                RepairDroidRespond.HIT_A_WALL -> {
                    markPointAsWall(grid, robotState.position, robotState.nextMove)
                    currentPoint = robotState.position
                }
                RepairDroidRespond.MOVED_INTO_EMPTY_TILE -> {
                    markPointAsOpen(grid, robotState.position, robotState.nextMove)
                    currentPoint = robotState.position.handleMove(robotState.nextMove)
                }
                RepairDroidRespond.MOVED_INTO_OXYGEN_SYSTEM -> {
                    markPointAsOxygenSystem(grid, robotState.position, robotState.nextMove)
                    currentPoint = robotState.position.handleMove(robotState.nextMove)
                }
            }

            println()
            renderGrid(grid)
        }













        throw NotImplementedError()
    }

    private fun renderGrid(grid: Map<Point, Char>) {
        val maxX = grid.keys.maxBy { it.x }!!.x
        val maxY = grid.keys.maxBy { it.y }!!.y
        val minX = grid.keys.minBy { it.x }!!.x
        val minY = grid.keys.minBy { it.y }!!.y



        val buffer = (minY .. maxY).map { y ->
            (minX .. maxX).map { x ->
                grid[Point(x, y)] ?: ' '
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

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

