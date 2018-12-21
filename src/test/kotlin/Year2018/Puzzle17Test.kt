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
    fun `render example commands to a canvas`() {
        val expected = """
            ......+.......
            ............#.
            .#..#.......#.
            .#..#..#......
            .#..#..#......
            .#.....#......
            .#.....#......
            .#######......
            ..............
            ..............
            ....#.....#...
            ....#.....#...
            ....#.....#...
            ....#######...
        """.trimIndent()

        val actual = puzzle.stateAfterUnitsOfWater(exampleText, 5)
        assertEquals(expected, actual)
    }

    @Test
    fun `state after one units of water`() {
        val expected = """
            ......+.......
            ......|.....#.
            .#..#.|.....#.
            .#..#.|#......
            .#..#.|#......
            .#....|#......
            .#~||||#......
            .#######......
            ..............
            ..............
            ....#.....#...
            ....#.....#...
            ....#.....#...
            ....#######...
        """.trimIndent()

        val state = puzzle.stateAfterUnitsOfWater(exampleText, 1)
        assertEquals(expected, state)
    }

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
        fun solveOne(puzzleText: String): String {
            return ""
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }

        fun renderInitial(puzzleText: String): String {
            val state = createInitialState(puzzleText)
            return renderState(state)
        }

        private fun renderState(state: Map<Point, Char>): String {
            val allPoints = state.keys

            val minX = allPoints.minBy { it.x }!!.x - 1
            val maxX = allPoints.maxBy { it.x }!!.x + 1
            val minY = allPoints.minBy { it.y }!!.y
            val maxY = allPoints.maxBy { it.y }!!.y

            val spaghetti = (minY..maxY).map { y ->
                (minX..maxX).map { x ->

                    val point = Point(x, y)

                    if (state.containsKey(point)) {
                        state[point]
                    } else {
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
                return state[this] != '.'
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

        fun stateAfterUnitsOfWater(puzzleText: String, units: Int): String {
            val state = createInitialState(puzzleText)
            val waterSource = Point(500, 0)

            // Spawn some water
            var waterPoint = Point(500, 0)


            while (true) {
                val downPoint = waterPoint.down()
                val leftPoint = waterPoint.left()
                val rightPoint = waterPoint.right()


                // Try go down
                if (state[downPoint] == '.') {
                    waterPoint = downPoint
                }
                // Can't go down any more look for a left/right point.
                else if (state[leftPoint] == '.') {
                    waterPoint = findLeftOrRightPoint(state, waterPoint)
                }

            }





        }

        private fun findLeftOrRightPoint(state: Map<Point, Char>, waterPoint: Point): Point {
            val directLeftTileIsBlocked = !waterPoint.left().isFree(state)
            val directRightTileIsBlocked = !waterPoint.right().isFree(state)
            var currentPoint = waterPoint

            // Scan left for a wall or cliff
            if (!directLeftTileIsBlocked) {
                var leftTile = currentPoint.left()
                var downLeftTile = leftTile.down()

                while (leftTile.isFree(state) && downLeftTile.isFree(state)) {
                    currentPoint = currentPoint.left()
                    leftTile = currentPoint.left()
                    downLeftTile = leftTile.down()
                }

                return currentPoint
            }
            else if (!directRightTileIsBlocked) {
                var rightTile = currentPoint.right()
                var downRightTile = rightTile.down()

                while (rightTile.isFree(state) && downRightTile.isFree(state)) {
                    currentPoint = currentPoint.right()
                    rightTile = currentPoint.right()
                    downRightTile = rightTile.down()
                }

                return currentPoint
            }
            else {
                throw RuntimeException("No where to go")
            }
        }

        fun moveWaterUntilDeadEnd(state: Map<Point, Char>, water: Point) {



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