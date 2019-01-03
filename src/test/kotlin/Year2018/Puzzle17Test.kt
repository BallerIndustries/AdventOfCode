package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle17Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle17.txt").readText().replace("\r", "")
    val puzzle = Puzzle17()

    val exampleText = """
            x=495, y=2..7
            y=7, x=495..501
            x=501, y=3..7
            x=498, y=2..4
            x=506, y=1..2
            x=498, y=10..13
            x=504, y=10..13
            y=13, x=498..504
        """.trimIndent()

    @Test
    fun `render puzzle part a`() {
        val result = puzzle.renderInitial(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle example a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }

    class Puzzle17 {
        fun solveOne(puzzleText: String): Int {
            val state = createInitialState(puzzleText)
            val waterSource = Point(500, 0)
            var waterSquares = listOf<Point>()

            while (true) {
                // Move all the other waters
                val tmp = moveTheWaterAlong(state, waterSquares)
                val noWaterMoleculesMoved = tmp.first

                if (!noWaterMoleculesMoved) {
                    break
                }

                waterSquares = tmp.second

                // Generate a square at one point below the water source
                waterSquares += waterSource.down()
                println(renderState(state, waterSquares))
                println()
                println()
            }

            //println(renderState(state, waterSquares))
            return 1337
        }

        // returns a boolean representing whether all water has moved, and a list of the new water state.
        private fun moveTheWaterAlong(state: Map<Point, Char>, waterSquares: List<Point>): Pair<Boolean, List<Point>> {
            if (waterSquares.isEmpty()) {
                return true to emptyList()
            }

            val newWaterSquares = waterSquares.map { it }.toMutableList()
            var moveCount = 0

            for (index in 0 until newWaterSquares.size) {
                val water = newWaterSquares[index]
                //val belowIsFloorOrWater = isFloorOrWater(water.down(), state, newWaterSquares)

                // Cannot move if any water below is on the maxY


                // Is the point below free?
                if (isFree(water.down(), state, newWaterSquares)) {
                    newWaterSquares[index] = water.down()
                    moveCount++
                }
                else if (isFree(water.left(), state, newWaterSquares) && !isOnMaxY(water, state)) {
                    newWaterSquares[index] = water.left()
                    moveCount++
                }
                else if (isFree(water.right(), state, newWaterSquares) && !isOnMaxY(water, state)) {
                    newWaterSquares[index] = water.right()
                    moveCount++
                }
            }

            if (moveCount == 0) {
                return false to emptyList()
            }
            else {
                return true to newWaterSquares
            }
        }

        private fun isOnMaxY(point: Point, state: Map<Point, Char>): Boolean {
            val maxY = state.keys.maxBy { it.y }!!.y
            return point.y == maxY
        }

        private fun isWithinMaxY(point: Point, state: Map<Point, Char>): Boolean {
            val maxY = state.keys.maxBy { it.y }!!.y
            return point.y <= maxY
        }

//        private fun isFloorOrWater(point: Point, state: Map<Point, Char>, waterSquares: List<Point>): Boolean {
//            val isFloor = state[point] == '#'
//            val isWater = waterSquares.contains(point)
//
//            return isFloor || isWater
//        }

        private fun isFree(point: Point, state: Map<Point, Char>, waterSquares: List<Point>): Boolean {
            val maxY = state.keys.maxBy { it.y }!!.y
            val pointHasNoClay = point.isFree(state)
            val pointHasNoWater = !waterSquares.contains(point)
            val pointIsBelowMaxY = point.y <= maxY

            return pointHasNoClay && pointHasNoWater && pointIsBelowMaxY
        }


        fun solveTwo(puzzleText: String): String {
            return ""
        }

        fun renderInitial(puzzleText: String): String {
            val state = createInitialState(puzzleText)
            return renderState(state)
        }

        private fun renderState(state: Map<Point, Char>, waterSquares: List<Point> = listOf()): String {
            val allPoints = state.keys
            val waterSet = waterSquares.toSet()

            val minX = allPoints.minBy { it.x }!!.x - 1
            val maxX = allPoints.maxBy { it.x }!!.x + 1
            val minY = allPoints.minBy { it.y }!!.y
            val maxY = allPoints.maxBy { it.y }!!.y

            val spaghetti = (minY..maxY).map { y ->
                (minX..maxX).map { x ->

                    val point = Point(x, y)

                    if (state.containsKey(point)) {
                        state[point]
                    }
                    else if (waterSet.contains(point)) {
                        '|'
                    }
                    else {
                        '.'
                    }
                }.joinToString("")
            }.joinToString("\n")

            return spaghetti
        }

        private fun createInitialState(puzzleText: String): Map<Point, Char> {
            val commands = parseCommands(puzzleText)
            val horseTime = mutableMapOf<Point, Char>()

            commands.forEach { command ->
                val points = command.getPoints()

                points.forEach { point ->
                    horseTime[point] = '#'
                }
            }

            // Add in the spigot or whatever the fuck it is called
            horseTime[Point(500, 0)] = '+'
            return horseTime
        }

        data class Point(val x: Int, val y: Int) {
            fun down() = this.copy(y = y + 1)
            fun left() = this.copy(x = x - 1)
            fun right() = this.copy(x = x + 1)

            fun isFree(state: Map<Point, Char>): Boolean {
                return state[this] != '#'
            }
        }

        private fun parseCommands(puzzleText: String):  List<LineCommand> {
            return puzzleText.split("\n").map { line ->

                val (first, second) = line.split(", ")

                if (first.startsWith("x")) {
                    val x = first.replace("x=", "").toInt()
                    val (yFrom, yTo) = second.replace("y=", "").split("..").map { it.toInt() }

                    VerticalLineCommand(x, yFrom, yTo)
                } else if (first.startsWith("y")) {
                    val y = first.replace("y=", "").toInt()
                    val (xFrom, xTo) = second.replace("x=", "").split("..").map { it.toInt() }

                    HorizontalLineCommand(y, xFrom, xTo)
                } else {
                    throw RuntimeException("Woah there!!")
                }
            }
        }


        data class HorizontalLineCommand(val y: Int, val xFrom: Int, val xTo: Int) : LineCommand {
            override fun getPoints(): List<Point> {
                return (xFrom .. xTo).map { x -> Point(x, y) }
            }
        }

        data class VerticalLineCommand(val x: Int, val yFrom: Int, val yTo: Int) : LineCommand {
            override fun getPoints(): List<Point> {
                return (yFrom .. yTo).map { y -> Point(x, y) }
            }
        }

        interface LineCommand {
            fun getPoints(): List<Point>
        }
    }
}