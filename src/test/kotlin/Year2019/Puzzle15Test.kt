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

    data class RobotState(val position: Point, val direction: Direction, val programState: State, val stepCount: Int) {
        override fun toString(): String {
            return "RobotState(position=$position, direction=$direction, stepCount=$stepCount)"
        }
    }

    fun solveOne(puzzleText: String): String {
        val virtualMachine = IntCodeVirtualMachine()
        val program = puzzleText.split(",").map { it.toLong() } + puzzleText.map { 0L }
        var currentState = State(program, userInput = listOf())
        var currentPoint = Point(0, 0)
        val frontier = Direction.values().map { RobotState(currentPoint, it, currentState, 1) }.toMutableList()
        val grid = mutableMapOf(currentPoint to '.')

        val history = mutableSetOf<Pair<Point, Direction>>()

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
                    //println("Was at ${robotState.position} tried to move ${robotState.direction} MOVED_INTO_EMPTY_TILE now at $currentPoint")
                }
                RepairDroidRespond.MOVED_INTO_OXYGEN_SYSTEM -> {
                    markPointAsOxygenSystem(grid, robotState.position, robotState.direction)
                    currentPoint = robotState.position.handleMove(robotState.direction)
                    println("Was at ${robotState.position} tried to move ${robotState.direction} MOVED_INTO_OXYGEN_SYSTEM now at $currentPoint. Steps taken = ${robotState.stepCount}")
                }
            }

            history.add(robotState.position to robotState.direction)

            val nextMoves = createNextMoves(currentPoint, currentState, robotState.stepCount).filter { nextMove ->
                !history.contains(nextMove.position to nextMove.direction) && frontier.none { it.position == nextMove.position && it.direction == nextMove.direction }
            }

            frontier.addAll(nextMoves)

//            if (counter++ % 1000 == 0) {
//                renderGrid(grid, currentPoint)
//                println()
//                println(counter)
//            }
        }















        throw NotImplementedError()
    }

    private fun createNextMoves(currentPoint: Point, state: State, lastStepCount: Int): List<RobotState> {
        return Direction.values().map {
            RobotState(currentPoint, it, state, lastStepCount + 1)
        }
    }

//    private fun createRobotStateForCurrentPosition(grid: Map<Point, Char>, currentPoint: Point, programState: State, frontier: List<RobotState>): List<RobotState> {
//        val allDirections = Direction.values()
//        val directionToPoint = allDirections.map { it to currentPoint.handleMove(it) }
//        val unvisitedPointsToDirections = directionToPoint.filter { (_, position) ->
//            !grid.contains(position) /*&& frontier.none { it.position == position }*/
//        }
//
//        return unvisitedPointsToDirections.map { (direction, point) ->
//            RobotState(point, direction, programState)
//        }
//    }

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

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

