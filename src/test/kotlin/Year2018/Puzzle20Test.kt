package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException
import java.util.*

class Puzzle20Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle20.txt").readText().replace("\r", "")
    val puzzle = Puzzle19()

    @Test
    fun `map for ^WNE$`() {
        val text = """^WNE$"""
        val expected = """
            #####
            #.|.#
            #-###
            #.|X#
            #####
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `map for ^ENWWW$`() {
        val text = """^ENWWW$"""
        val expected = """
            #########
            #.|.|.|.#
            #######-#
            #####X|.#
            #########
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `map for (NEEE|SSE(EE|N))`() {
        val text = """^ENWWW(NEEE|SSE(EE|N))$"""
        val expected = """
            #########
            #.|.|.|.#
            #-#######
            #.|.|.|.#
            #-#####-#
            #.#.#X|.#
            #-#-#####
            #.|.|.|.#
            #########
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1152, result)
    }

    @Test
    fun `puzzle part b`() {
       val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    enum class Direction { NORTH, EAST, SOUTH, WEST }

    data class NodeData(val paths: MutableSet<Point> = mutableSetOf(), val isStart: Boolean = false)

    data class Point(val x: Int, val y: Int) {
        fun twoNorth() = this.copy(y = this.y - 2)
        fun twoSouth() = this.copy(y = this.y + 2)
        fun twoWest() = this.copy(x = this.x - 2)
        fun twoEast() = this.copy(x = this.x + 2)

        fun north() = this.copy(y = this.y - 1)
        fun south() = this.copy(y = this.y + 1)
        fun west() = this.copy(x = this.x - 1)
        fun east() = this.copy(x = this.x + 1)

        fun normalise(x: Int, y: Int) = this.copy(x = this.x - x, y = this.y - y)
    }

    class Puzzle19 {
        fun solveOne(puzzleText: String): Int {
            return 1337
        }

        fun solveTwo(puzzleText: String): Int {
            return 1337
        }

        fun generatePlan(text: String): String {
            val graph = parseRegexIntoGraph(text)

            val minX = graph.keys.minBy { it.x }!!.x
            val maxX = graph.keys.maxBy { it.x }!!.x
            val minY = graph.keys.minBy { it.y }!!.y
            val maxY = graph.keys.maxBy { it.y }!!.y

            val width = maxX - minX
            val height = maxY - minY

            // Now want to normalise all the points

            val normalisedGraph = graph.entries.associate { (point, nodedata) ->
                point.normalise(minX, minY) to nodedata.copy(paths = nodedata.paths.map { it.normalise(minX, minY) }.toMutableSet())
            }

            val displayWidth  = width
            val displayHeight = height

            fun hasUpDownPath(point: Point): Boolean {
                val pointBelow = point.south()
                val pointAbove = point.north()
                val pathsForPointBelow = normalisedGraph[pointBelow]

                return pathsForPointBelow != null && pathsForPointBelow.paths.contains(pointAbove)
            }

            fun hasLeftRightPath(point: Point): Boolean {
                val pointLeft = point.west()
                val pointRight = point.east()
                val pathsForPointLeft = normalisedGraph[pointLeft]

                return pathsForPointLeft != null && pathsForPointLeft.paths.contains(pointRight)
            }

            return (-1 .. displayHeight + 1).map { y ->
                (-1 .. displayWidth + 1).map { x ->
                    val point = Point(x, y)
                    val nodeData = normalisedGraph[point]

                    when {
                        nodeData != null && nodeData.isStart -> 'X'
                        nodeData != null -> '.'
                        hasUpDownPath(point) -> '-'
                        hasLeftRightPath(point) -> '|'
                        else -> '#'
                    }
                }.joinToString("")
            }.joinToString("\n")
        }

        private fun parseRegexIntoGraph(text: String): MutableMap<Point, NodeData> {
            var currentPoint = Point(0, 0)
            val graph = mutableMapOf(currentPoint to NodeData(paths = mutableSetOf(), isStart = true))
            val stack = Stack<Point>()

            text.replace("^", "").replace("$", "").forEach { character ->
                val nextPoint: Point = when (character) {
                    'N' -> currentPoint.twoNorth()
                    'E' -> currentPoint.twoEast()
                    'S' -> currentPoint.twoSouth()
                    'W' -> currentPoint.twoWest()
                    '(' -> {
                        stack.push(currentPoint)
                        currentPoint
                    }
                    '|' -> stack.peek()
                    ')' -> stack.pop()
                    else -> throw RuntimeException("Woah unexpected character! character = $character")
                }

                // Add in a node for the next point, if one does not already exist
                if (!graph.containsKey(nextPoint)) {
                    graph[nextPoint] = NodeData()
                }

                // Add a path from current point to next point
                graph[currentPoint]!!.paths.add(nextPoint)
                graph[nextPoint]!!.paths.add(currentPoint)

                currentPoint = nextPoint
            }

            return graph
        }
    }
}