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
        val result = puzzle.solveOneFillzors(exampleText)
        assertEquals(57, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(9000, result)
    }

    enum class Collision { WALL, GAP }

    class Puzzle17 {
        private val movingWaterPoints = mutableSetOf<Point>()

        fun solveOneFillzors(puzzleText: String): Int {
            val state = createInitialState(puzzleText)
            val stateWithWater = state.toMutableMap()

            // TODO: Lets actually deal with these please
            val otherStreamsToDealWith = mutableListOf<Point>()
            var currentPoint = Point(500, 0)

            val maxY = state.keys.maxBy { it.y }!!.y

            // While we are still within the map
            while (true) {
                println("currentPoint = $currentPoint")
                println(maxY)

                currentPoint = shootDownUntilYouHitGround(state, currentPoint) ?: break
                var (leftPoint, leftCollision) = chargeLeftUntilWallOrGap(state, currentPoint)
                var (rightPoint, rightCollision) = chargeRightUntilWallOrGap(state, currentPoint)

                // We are in a bucket
                if (leftCollision == Collision.WALL && rightCollision == Collision.WALL) {
                    currentPoint = handleBucket(currentPoint, leftPoint, rightPoint, stateWithWater)

                    println(renderState(stateWithWater))

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
                    otherStreamsToDealWith.add(rightPoint)
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

            movingWaterPoints.forEach { point -> if (!stateWithWater.containsKey(point)) stateWithWater[point] = '|' }
            println(renderState(stateWithWater))

            return stateWithWater.values.count { it == '~' || it == '|' }
        }

        private fun handleBucket(startingPoint: Point, firstLeftPoint: Point, firstRightPoint: Point, stateWithWater: MutableMap<Point, Char>): Point {
            setHorizontalLine(stateWithWater, firstLeftPoint, firstRightPoint, '~')
            println(renderState(stateWithWater))
            var currentPoint = startingPoint.up()

            var (leftPoint, leftCollision) = chargeLeftUntilWallOrGap(stateWithWater, currentPoint)
            var (rightPoint, rightCollision) = chargeRightUntilWallOrGap(stateWithWater, currentPoint)

            while (leftCollision == Collision.WALL && rightCollision == Collision.WALL) {
                // Mark this as still water
                setHorizontalLine(stateWithWater, leftPoint, rightPoint, '~')
                println(renderState(stateWithWater))

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

//        fun solveOneSpeedy(puzzleText: String): Int {
//            val state = createInitialState(puzzleText)
//            val waterSource = Point(500, 0)
//            val waterMolecules = mutableSetOf<Point>()
//
//            while (true) {
//                // Create a water molecule
//                var molecule = waterSource.down()
//                var direction = WaterDirection.DOWN
//
//                // Move the jerk down until it hits a something
//                while (true) {
//                    if (waterMolecules.count() == 15) {
//                        println("Hooray")
//                    }
//
//                    if (direction == WaterDirection.DOWN) {
//                        molecule = goDownUntilColliding(molecule, state, waterMolecules)
//                    }
//
//                    // Okay we are done falling, should we go left or right?
//                    if (isFree(molecule.left(), state, waterMolecules)) {
//                        direction = WaterDirection.LEFT
//                    }
//                    else if (isFree(molecule.right(), state, waterMolecules)) {
//                        direction = WaterDirection.RIGHT
//                    }
//                    else {
//                        break
//                    }
//
//                    val tmp: Pair<Point, WaterDirection> = moveHorizontally(direction, molecule, state, waterMolecules)
//
//                    molecule = tmp.first
//                    direction = tmp.second
//
//                    if (direction == WaterDirection.STUCK) {
//                        break
//                    }
//                }
//
//                waterMolecules.add(molecule)
//                //println(waterMolecules.size)
//
//                println(movingWaterPoints.size)
//
//                if (waterMolecules.count() % 2000 == 0) {
//                    println(renderState(state, waterMolecules))
//                }
//            }
//        }

//        private fun moveHorizontally(direction: WaterDirection, molecule: Point, state: Map<Point, Char>, waterMolecules: Set<Point>): Pair<Point, WaterDirection> {
//            var mutableMolecule = molecule
//            movingWaterPoints.add(mutableMolecule)
//
//            if (direction == WaterDirection.LEFT) {
//
//                while (isFree(mutableMolecule.left(), state, waterMolecules)) {
//
//                    // Wait a minute! We should be fallllinnnnggg!!
//                    if (isFree(mutableMolecule.down(), state, waterMolecules)) {
//                        return mutableMolecule to WaterDirection.DOWN
//                    }
//
//                    mutableMolecule = mutableMolecule.left()
//                    movingWaterPoints.add(mutableMolecule)
//                }
//
//                return mutableMolecule to WaterDirection.STUCK
//            }
//            else if (direction == WaterDirection.RIGHT) {
//
//
//                while (isFree(mutableMolecule.right(), state, waterMolecules)) {
//
//                    // Wait a minute! We should be fallllinnnnggg!!
//                    if (isFree(mutableMolecule.down(), state, waterMolecules)) {
//                        return mutableMolecule to WaterDirection.DOWN
//                    }
//
//                    mutableMolecule = mutableMolecule.right()
//                    movingWaterPoints.add(mutableMolecule)
//                }
//
//                return mutableMolecule to WaterDirection.STUCK
//            }
//            else {
//                throw RuntimeException("Oh no")
//            }
//        }
//
//        enum class WaterDirection { STUCK, DOWN, LEFT, RIGHT }
//
//        private fun goDownUntilColliding(molecule: Point, state: Map<Point, Char>, waterMolecules: Set<Point>): Point {
//            var molecule1 = molecule
//            movingWaterPoints.add(molecule1)
//
//            while (isFree(molecule1.down(), state, waterMolecules)) {
//                molecule1 = molecule1.down()
//                movingWaterPoints.add(molecule1)
//            }
//
//            return molecule1
//        }

//        fun goLeftOrRight(molecule: Point, state: Map<Point, Char>, waterMolecules: List<Point>): Point {
//            var mutableMolecule = molecule
//            val firstLeftIsFree = isFree(mutableMolecule.left(), state, waterMolecules)
//
//            if (firstLeftIsFree) {
//                while (isFree(mutableMolecule.left(), state, waterMolecules)) {
//                    mutableMolecule = mutableMolecule.left()
//                }
//            }
//            else {
//                while (isFree(mutableMolecule.right(), state, waterMolecules)) {
//                    mutableMolecule = mutableMolecule.right()
//                }
//            }
//
//            return mutableMolecule
//        }

//        fun solveOne(puzzleText: String): Int {
//            val state = createInitialState(puzzleText)
//            val waterSource = Point(500, 0)
//            var waterSquares = mutableListOf<Point>()
//
//            val outputFile = File("/Users/anguruso/Desktop/jur.txt")
//
//            if (outputFile.exists()) {
//                outputFile.delete()
//            }
//
//            while (true) {
//                // Move all the other waters
//                val tmp = moveTheWaterAlong(state, waterSquares)
//                val noWaterMoleculesMoved = tmp.first
//
//                if (!noWaterMoleculesMoved) {
//                    break
//                }
//
//                waterSquares = tmp.second.toMutableList()
//
//                // Generate a square at one point below the water source
//                waterSquares.add(waterSource.down())
//
//                //outputFile.appendText(waterSquares.count().toString())
//                println(waterSquares.size)
//
//                if (waterSquares.count() % 300 == 0) {
//                    outputFile.appendText(renderState(state, waterSquares))
//                    outputFile.appendText("\n\n\n")
//                }
//            }
//
//            return 1337
//        }

//        // returns a boolean representing whether all water has moved, and a list of the new water state.
//        private fun moveTheWaterAlong(state: Map<Point, Char>, waterSquares: Set<Point>): Pair<Boolean, List<Point>> {
//            if (waterSquares.isEmpty()) {
//                return true to emptyList()
//            }
//
//            val newWaterSquares = waterSquares.map { it }.toMutableList()
//            var moveCount = 0
//
//            for (index in 0 until newWaterSquares.size) {
//                val water = newWaterSquares[index]
//
//                // Cannot move left/right if any water below is on the maxY
//                //val streamBelowIsOnMaxY: Boolean = streamBelowIsOnMaxY(water, state, newWaterSquares)
//                val streamBelowIsOnMaxY = false
//
//
//                // Is the point below free?
//                if (isFree(water.down(), state, newWaterSquares)) {
//                    newWaterSquares[index] = water.down()
//                    moveCount++
//                }
//                else if (isFree(water.left(), state, newWaterSquares) && !isOnMaxY(water, state) && !streamBelowIsOnMaxY) {
//                    newWaterSquares[index] = water.left()
//                    moveCount++
//                }
//                else if (isFree(water.right(), state, newWaterSquares) && !isOnMaxY(water, state) && !streamBelowIsOnMaxY) {
//                    newWaterSquares[index] = water.right()
//                    moveCount++
////                }
////            }
//
//            if (moveCount == 0) {
//                return false to emptyList()
//            }
//            else {
//                return true to newWaterSquares
//            }
//        }

//        private fun streamBelowIsOnMaxY(water: Point, state: Map<Point, Char>, newWaterSquares: MutableList<Point>): Boolean {
//            val maxY = state.keys.maxBy { it.y }!!.y
//            var below = water.down()
//
//            while (newWaterSquares.contains(below)) {
//                if (below.y == maxY) {
//                    return true
//                }
//
//                below = below.down()
//            }
//
//            return false
//        }
//
//        private fun isOnMaxY(point: Point, state: Map<Point, Char>): Boolean {
//            val maxY = state.keys.maxBy { it.y }!!.y
//            return point.y == maxY
//        }
//
//        private fun isWithinMaxY(point: Point, state: Map<Point, Char>): Boolean {
//            val maxY = state.keys.maxBy { it.y }!!.y
//            return point.y <= maxY
//        }

//        private fun isFree(point: Point, state: Map<Point, Char>, waterSquares: Set<Point>): Boolean {
//            val maxY = state.keys.maxBy { it.y }!!.y
//            val pointHasNoClay = state[point] != '#'
//            val pointHasNoWater = !waterSquares.contains(point)
//            val pointIsBelowMaxY = point.y <= maxY
//
//            return pointHasNoClay && pointHasNoWater && pointIsBelowMaxY
//        }


        fun solveTwo(puzzleText: String): String {
            return ""
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