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
        println(result)
    }

    @Test
    fun `puzzle example a`() {
        val (result, _) = puzzle.solveOneFillzors(exampleText)
        assertEquals(57, result)
    }

    @Test
    fun `puzzle part a`() {
        // 36789 too high
        val (result, _) = puzzle.solveOneFillzors(puzzleText)
        assertEquals(36787, result)
    }

    @Test
    fun `puzzle part b`() {
        val (_, result) = puzzle.solveOneFillzors(puzzleText)
        assertEquals(29662, result)
    }

    enum class Collision { WALL, GAP }

    class Puzzle17 {
        private val movingWaterPoints = mutableSetOf<Point>()

        fun solveOneFillzors(puzzleText: String): Pair<Int, Int> {
            val state = createInitialState(puzzleText)
            val stateWithWater = state.toMutableMap()

            val streamsYo = mutableListOf(Point(500, 0))
            var index = 0

            while (index < streamsYo.size) {
                var currentPoint = streamsYo[index]

                // While we are still within the map
                while (true) {
                    currentPoint = shootDownUntilYouHitGround(state, currentPoint) ?: break
                    var (leftPoint, leftCollision) = chargeLeftUntilWallOrGap(state, currentPoint)
                    var (rightPoint, rightCollision) = chargeRightUntilWallOrGap(state, currentPoint)

                    // We are in a bucket
                    if (leftCollision == Collision.WALL && rightCollision == Collision.WALL) {
                        currentPoint = handleBucket(currentPoint, leftPoint, rightPoint, stateWithWater)

                        // Recalc the jerks
                        val (_leftPoint, _leftCollision) = chargeLeftUntilWallOrGap(stateWithWater, currentPoint)
                        val (_rightPoint, _rightCollision) = chargeRightUntilWallOrGap(stateWithWater, currentPoint)

                        leftPoint = _leftPoint
                        leftCollision = _leftCollision
                        rightPoint = _rightPoint
                        rightCollision = _rightCollision
                    }

                    // We should be able to charge left/right and drop off
                    if (leftCollision == Collision.GAP && rightCollision == Collision.GAP) {
                        setHorizontalLine(stateWithWater, leftPoint, rightPoint, '|')

                        if (!streamsYo.contains(rightPoint)) {
                            streamsYo.add(rightPoint)
                        }

                        currentPoint = leftPoint
                    }
                    else if (leftCollision == Collision.GAP && rightCollision == Collision.WALL) {
                        setHorizontalLine(stateWithWater, leftPoint, rightPoint, '|')
                        currentPoint = leftPoint
                    }
                    else if (leftCollision == Collision.WALL && rightCollision == Collision.GAP) {
                        setHorizontalLine(stateWithWater, leftPoint, rightPoint, '|')
                        currentPoint = rightPoint
                    }
                }

                index++
            }

            val minY = state.entries.filter  { it.value != '+' }.minBy { it.key.y }!!.key.y
            val maxY = state.keys.maxBy { it.y }!!.y

            movingWaterPoints.forEach { point -> if (!stateWithWater.containsKey(point)) stateWithWater[point] = '|' }

            val waterCount = stateWithWater.entries.filter { it.key.y >= minY && it.key.y <= maxY }.count { it.value == '~' || it.value == '|' }
            val retainedWaterCount = stateWithWater.entries.filter { it.key.y >= minY && it.key.y <= maxY }.count { it.value == '~' }

            return waterCount to retainedWaterCount
        }

        private fun handleBucket(startingPoint: Point, firstLeftPoint: Point, firstRightPoint: Point, stateWithWater: MutableMap<Point, Char>): Point {
            setHorizontalLine(stateWithWater, firstLeftPoint, firstRightPoint, '~')
            //println(renderState(stateWithWater))
            var currentPoint = startingPoint.up()

            var (leftPoint, leftCollision) = chargeLeftUntilWallOrGap(stateWithWater, currentPoint)
            var (rightPoint, rightCollision) = chargeRightUntilWallOrGap(stateWithWater, currentPoint)

            while (leftCollision == Collision.WALL && rightCollision == Collision.WALL) {
                // Mark this as still water
                setHorizontalLine(stateWithWater, leftPoint, rightPoint, '~')
                //println(renderState(stateWithWater))

                // Move up
                currentPoint = currentPoint.up()

                // Recalc the jerks
                val (_leftPoint, _leftCollision) = chargeLeftUntilWallOrGap(stateWithWater, currentPoint)
                val (_rightPoint, _rightCollision) = chargeRightUntilWallOrGap(stateWithWater, currentPoint)

                leftPoint = _leftPoint
                leftCollision = _leftCollision
                rightPoint = _rightPoint
                rightCollision = _rightCollision
            }

            return currentPoint
        }

        private fun chargeLeftUntilWallOrGap(state: Map<Point, Char>, pointJustAboveGround: Point): Pair<Point, Collision> {
            var currentPoint = pointJustAboveGround

            while (state[currentPoint.left()] != '#' && (state[currentPoint.down()] == '#' || state[currentPoint.down()] == '~' )) {
                currentPoint = currentPoint.left()
            }

            val collision = when {
                state[currentPoint.down()] == null -> Collision.GAP
                state[currentPoint.left()] == '#' -> Collision.WALL
                else -> throw RuntimeException("Hmm unexpected.. pointJustAboveGround = $pointJustAboveGround")
            }

            return currentPoint to collision
        }

        fun setHorizontalLine(stateWithWater: MutableMap<Point, Char>, a: Point, b: Point, c: Char) {
            val y = a.y
            (a.x .. b.x).forEach { x -> stateWithWater[Point(x, y)] = c }
        }

        private fun chargeRightUntilWallOrGap(state: Map<Point, Char>, pointJustAboveGround: Point): Pair<Point, Collision> {
            var currentPoint = pointJustAboveGround

            while (state[currentPoint.right()] != '#' && (state[currentPoint.down()] == '#' || state[currentPoint.down()] == '~' )) {
                currentPoint = currentPoint.right()
            }

            val collision = when {
                state[currentPoint.down()] == null -> Collision.GAP
                state[currentPoint.right()] == '#' -> Collision.WALL
                else -> throw RuntimeException("Hmm unexpected..")
            }

            return currentPoint to collision
        }

        private fun shootDownUntilYouHitGround(state: Map<Point, Char>, point: Point): Point? {
            val maxY = state.keys.maxBy { it.y }!!.y

            var currentPosition = point
            movingWaterPoints.add(currentPosition)

            while (state[currentPosition.down()] != '#' && currentPosition.y < maxY) {
                currentPosition = currentPosition.down()
                movingWaterPoints.add(currentPosition)
            }

            return if (currentPosition.y >= maxY) null else currentPosition
        }

        fun renderInitial(puzzleText: String): String {
            val state = createInitialState(puzzleText)
            return renderState(state)
        }

        private fun renderState(state: Map<Point, Char>, waterSquares: Set<Point> = setOf()): String {
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
            fun up() = this.copy(y = y - 1)
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