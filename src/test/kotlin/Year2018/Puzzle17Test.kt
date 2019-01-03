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
                val allWaterMoved = tmp.first

                if (!allWaterMoved) {
                    break
                }

                waterSquares = tmp.second

                // Generate a square at one point below the water source
                waterSquares += waterSource.down()
                //println(waterSquares)


            }

            println(renderState(state, waterSquares))

            return 1337
        }

        // returns a boolean representing whether all water has moved, and a list of the new water state.
        private fun moveTheWaterAlong(state: Map<Point, Char>, waterSquares: List<Point>): Pair<Boolean, List<Point>> {
            val newWaterSquares = waterSquares.map { it }.toMutableList()

            for (index in 0 until newWaterSquares.size) {
                val water = newWaterSquares[index]

                // Is the point below free?
                if (water.down().isFree(state)) {
                    newWaterSquares[index] = water.down()
                }
                else {
                    return false to listOf()
                }
            }

            return true to newWaterSquares
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