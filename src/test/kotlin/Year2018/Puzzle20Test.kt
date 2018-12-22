package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

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

    data class PointAndDirection(val point: Point, val direction: Direction)

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

            val normalisedGraph = graph.entries.associate { (point, paths) ->
                point.normalise(minX, minY) to paths.map { it.normalise(minX, minY) }.toSet()
            }

            val displayWidth  = (width * 2) - 1
            val displayHeight = (height * 2) - 1

            fun hasUpDownPath(point: Point): Boolean {
                val pointBelow = point.south()
                val pointAbove = point.north()
                val pathsForPointBelow = normalisedGraph[pointBelow]

                return pathsForPointBelow != null && pathsForPointBelow.contains(pointAbove)
            }

            fun hasLeftRightPath(point: Point): Boolean {
                val pointLeft = point.west()
                val pointRight = point.east()
                val pathsForPointLeft = normalisedGraph[pointLeft]

                return pathsForPointLeft != null && pathsForPointLeft.contains(pointRight)
            }

            val gridAsText = (-1 until displayHeight + 1).map { y ->
                (-1 until displayWidth + 1).map { x ->
                    val point = Point(x, y)

                    when {
                        normalisedGraph.containsKey(point) -> '.'
                        hasUpDownPath(point) -> '-'
                        hasLeftRightPath(point) -> '|'
                        else -> '#'
                    }
                }.joinToString("")
            }.joinToString("\n")

            return gridAsText
        }

        private fun parseRegexIntoGraph(text: String): MutableMap<Point, MutableSet<Point>> {
            var currentPoint = Point(0, 0)
            val graph = mutableMapOf(currentPoint to mutableSetOf<Point>())

            text.replace("^", "").replace("$", "").forEach { character ->
                val nextPoint = when (character) {
                    'N' -> currentPoint.twoNorth()
                    'E' -> currentPoint.twoEast()
                    'S' -> currentPoint.twoSouth()
                    'W' -> currentPoint.twoWest()
                    else -> throw RuntimeException("Woah")
                }

                // Add in a node for the next point, if one does not already exist
                if (!graph.containsKey(nextPoint)) {
                    graph[nextPoint] = mutableSetOf()
                }

                // Add a path from current point to next point
                graph[currentPoint]!!.add(nextPoint)
                graph[nextPoint]!!.add(currentPoint)

                currentPoint = nextPoint
            }
            return graph
        }
    }


}