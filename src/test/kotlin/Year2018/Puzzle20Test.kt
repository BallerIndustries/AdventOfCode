package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Ignore
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
    fun `map for ENWWW(NEEE|SSE(EE|N))`() {
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
    fun `map for ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN`() {
        val text = """^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$"""
        val expected = """
            ###########
            #.|.#.|.#.#
            #-###-#-#-#
            #.|.|.#.#.#
            #-#####-#-#
            #.#.#X|.#.#
            #-#-#####-#
            #.#.|.|.|.#
            #-###-###-#
            #.|.|.#.|.#
            ###########
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `map for NN(EE|WW)`() {
        val text = """^NN(EE|WW)$"""
        val expected = """
            ###########
            #.|.|.|.|.#
            #####-#####
            #####.#####
            #####-#####
            #####X#####
            ###########
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `map for NN(EE|WW)SS`() {
        val text = """^NN(EE|WW)SS$"""
        val expected = """
            ###########
            #.|.|.|.|.#
            #-###-###-#
            #.###.###.#
            #-###-###-#
            #.###X###.#
            ###########
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }


    @Test
    fun `map for EEE(NNN|)EEE`() {
        val text = """^EEE(NNN|)EEE$"""
        val expected = """
            ###############
            #######.|.|.|.#
            #######-#######
            #######.#######
            #######-#######
            #######.#######
            #######-#######
            #X|.|.|.|.|.|.#
            ###############
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `map for EEE(NNN|)EEENN`() {
        val text = """^EEE(NNN|)EEENN$"""
        val expected = """
            ###############
            #############.#
            #############-#
            #############.#
            #############-#
            #######.|.|.|.#
            #######-#######
            #######.#####.#
            #######-#####-#
            #######.#####.#
            #######-#####-#
            #X|.|.|.|.|.|.#
            ###############
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `map for WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))`() {
        val text = """^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$"""
        val expected = """
            ###############
            #.|.|.|.#.|.|.#
            #-###-###-#-#-#
            #.|.#.|.|.#.#.#
            #-#########-#-#
            #.#.|.|.|.|.#.#
            #-#-#########-#
            #.#.#.|X#.|.#.#
            ###-#-###-#-#-#
            #.|.#.#.|.#.|.#
            #-###-#####-###
            #.|.#.|.|.#.#.#
            #-#-#####-#-#-#
            #.#.|.|.|.#.|.#
            ###############
        """.trimIndent()

        val plan: String = puzzle.generatePlan(text)
        assertEquals(expected, plan)
    }

    @Test
    fun `puzzle plan can be generated`() {
        val result = puzzle.generatePlan(puzzleText)
        //assertEquals("", result)
    }

    @Test
    fun `furthest room for ^WNE$3`() {
        val text = """^WNE$"""
        val distance = puzzle.solveOne(text)
        assertEquals(3, distance)
    }

    @Test
    fun `furthest room for ^ENWWW(NEEE|SSE(EE|N))$`() {
        val text = """^ENWWW(NEEE|SSE(EE|N))$"""
        val distance = puzzle.solveOne(text)
        assertEquals(10, distance)
    }

    @Test
    fun `fursthest room for WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))`() {
        val result = puzzle.solveOne("""^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$""")
        assertEquals(31, result)
    }

    @Test
    fun `furthest room for ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))`() {
        val result = puzzle.solveOne("""^ESSWWN(E|NNENN(EESS(WNSE|)SSS|WWWSSSSE(SW|NNNE)))$""")
        assertEquals(23, result)
    }

    @Test
    fun `furthest room for EE(NNN|)EEE`() {
        val text = """^EEE(NNN|)EEE$"""
        val furthestRoom = puzzle.solveOne(text)
        assertEquals(9, furthestRoom)
    }

    @Test
    fun `furthest room for EEE(NNN|)EEENN`() {
        val text = """^EEE(NNN|)EEENN$"""
        val furthestRoom = puzzle.solveOne(text)
        assertEquals(11, furthestRoom)
    }

    @Test
    @Ignore
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        // 235 is too low
        // 3814 is too low
        // 3814 is too low
        assertEquals(900000, result)
    }

    @Test
    @Ignore
    fun `puzzle part b`() {
       val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

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
        private fun parseRegexIntoGraph(text: String): MutableMap<Point, NodeData> {
            var currentPoints = mutableSetOf(Point(0, 0))
            val graph = mutableMapOf(currentPoints.first() to NodeData(paths = mutableSetOf(), isStart = true))
            val stack = Stack<MutableSet<Point>>()
            val tailStack = Stack<MutableSet<Point>>()

            val cleanedText = text.replace("^", "").replace("$", "")

            for (index in 0 until cleanedText.length) {
                val character = cleanedText[index]

                if (character == '(') {
                    tailStack.add(mutableSetOf())
                    stack.add(currentPoints)
                    continue
                }
                else if (character == '|') {
                    tailStack.peek().addAll(currentPoints)
                    currentPoints = stack.peek()
                    continue
                }
                else if (character == ')') {
                    tailStack.peek().addAll(currentPoints)
                    //println("tailStack.peek().size = ${tailStack.peek().size}")
                    currentPoints = tailStack.pop()
                    continue
                }

                val nextPoints: List<Point> = when (character) {
                    'N' -> currentPoints.map { it.twoNorth() }
                    'E' -> currentPoints.map { it.twoEast() }
                    'S' -> currentPoints.map { it.twoSouth() }
                    'W' -> currentPoints.map { it.twoWest() }
                    else -> throw RuntimeException("Woah unexpected character! character = $character")
                }

                // Add in a node for the next point, if one does not already exist
                nextPoints.forEach { nextPoint ->
                    if (!graph.containsKey(nextPoint)) {
                        graph[nextPoint] = NodeData()
                    }
                }

                currentPoints.forEachIndexed { jIndex, currentPoint ->
                    // Add a path from current point to next point
                    val nextPoint = nextPoints[jIndex]
                    addPath(graph, currentPoint, nextPoint, character, index)
                }

                currentPoints = nextPoints.toMutableSet()

                // Print out the graph
                println(cleanedText.substring(0, index + 1))
                println(stringifyPlan(graph))
            }

            return graph
        }

        private fun addPath(graph: MutableMap<Point, NodeData>, currentPoint: Point, nextPoint: Point, character: Char, index: Int) {
            val distance = manhattanDistance(currentPoint, nextPoint)

            if (distance > 2) {
                throw RuntimeException("Woah woah! distance between the points is greater than 2! distance = $distance character = $character index = $index")
            }

            graph[currentPoint]!!.paths.add(nextPoint)
            graph[nextPoint]!!.paths.add(currentPoint)
        }


        fun solveOne(puzzleText: String): Int {
            val graph = parseRegexIntoGraph(puzzleText)
            val (startPoint ) = graph.entries.find { it.value.isStart }!!

            val otherPoints = graph.entries
                .filter { !it.value.isStart }
                .map { it.key }

            val distancesToOtherPoints: List<List<Point>> = otherPoints.map { otherPoint ->
                measureDistance(graph, startPoint, otherPoint)
            }

            println(stringifyPlan(graph))
            return distancesToOtherPoints.maxBy { it.size }!!.size
        }

        fun manhattanDistance(p1: Point, p2: Point): Int {
            return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y)
        }

        private fun measureDistance(graph: Map<Point, NodeData>, startPoint: Point, endPoint: Point): MutableList<Point> {
            val visited = mutableSetOf(startPoint)
            val toProcess = LinkedList<Pair<Point, MutableList<Point>>>()
            toProcess.add(startPoint to mutableListOf())

            while (toProcess.isNotEmpty()) {
                val (currentPoint, path) = toProcess.pop()

                if (currentPoint == endPoint) {
                    return path
                }

                visited.add(currentPoint)

                val nodeData = graph[currentPoint]!!

                val adjacentTiles = nodeData.paths
                    .filter { !visited.contains(it) }
                    .map { it to (path + it).toMutableList() }

                // Add adjacent tiles we have not visited
                toProcess.addAll(adjacentTiles)
                visited.addAll(adjacentTiles.map { it.first })
            }

            throw RuntimeException("Couldn't find a path to that room. Oh no!")
        }

        fun solveTwo(puzzleText: String): Int {
            return 1337
        }

        fun generatePlan(text: String): String {
            val graph = parseRegexIntoGraph(text)
            return stringifyPlan(graph)
        }

        private fun stringifyPlan(graph: MutableMap<Point, NodeData>): String {
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

            return (-1..height + 1).map { y ->
                (-1..width + 1).map { x ->
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
    }
}