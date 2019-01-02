package Year2018

import Year2018.Puzzle22RedoTest.Tool.*
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle22RedoTest {
    val puzzleText = this::class.java.getResource("/2018/puzzle22.txt").readText().replace("\r", "")
    val puzzle = Puzzle22()

    val exampleText = """
            depth: 510
            target: 10,10
        """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(114, result)
    }

    @Test
    fun `example part b`() {
        val result = puzzle.solveTwo(exampleText)
        assertEquals(45, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(5622, result)
    }

    @Test
    fun `puzzle part b`() {
        // 1411 too high
        // 1339 too high
        // 1376 too high
        // 1265 too high
        // 1105 not right
        // maybe? 1089
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1089, result)
    }

    data class PlayerState(val currentTool: Tool, val position: Point) {
        fun up() = this.copy(position = this.position.up())
        fun down() = this.copy(position = this.position.down())
        fun right() = this.copy(position = this.position.right())
        fun left() = this.copy(position = this.position.left())
    }

    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)
    }

    enum class Tool { NONE, TORCH, CLIMBING_GEAR }

    enum class Tile(val value: Int, val validTools: List<Tool>) {
        ROCKY(0, listOf(CLIMBING_GEAR, TORCH)),
        WET(1, listOf(CLIMBING_GEAR, NONE)),
        NARROW(2, listOf(NONE, TORCH))
    }

    class Puzzle22 {
        private val geologicIndexCache = mutableMapOf<Point, Int>()

        private fun geologicIndex(point: Point, target: Point, depth: Int): Int {
            val (x, y) = point

            return when {
                x == 0 && y == 0 -> 0
                point == target -> 0
                y == 0 -> x * 16807
                x == 0 -> y * 48271
                else -> {
                    val left = point.copy(x = point.x - 1)
                    val north = point.copy(y = point.y - 1)
                    erosionLevel(left, target, depth) * erosionLevel(north, target, depth)
                }
            }
        }

        private fun erosionLevel(point: Point, target: Point, depth: Int): Int {
            if (!geologicIndexCache.containsKey(point)) {
                val erosionLevel =  (geologicIndex(point, target, depth) + depth) % 20183
                geologicIndexCache[point] = erosionLevel
            }

            return geologicIndexCache[point]!!
        }

        private fun type(point: Point, target: Point, depth: Int): Tile {
            val jur = erosionLevel(point, target, depth) % 3
            return Tile.values().find { it.value == jur }!!
        }

        fun solveOne(puzzleText: String): Int {
            val (depth, target) = parseDepthAndTarget(puzzleText)

            return (0 .. target.y).sumBy { y ->
                (0 .. target.x).sumBy { x ->
                    val point = Point(x, y)
                    type(point, target, depth).value
                }
            }
        }

        private fun parseDepthAndTarget(puzzleText: String): Pair<Int, Point> {
            val (firstLine, secondLine) = puzzleText.split("\n")

            val depth = firstLine.replace("depth: ", "").toInt()
            val target = secondLine.replace("target: ", "").split(",").map { it.toInt() }.let { Point(it[0], it[1]) }
            return Pair(depth, target)
        }

        fun solveTwo(puzzleText: String): Int {
            val (depth, target) = parseDepthAndTarget(puzzleText)
            val grid = createGrid(target, depth)

            val shortestPath = calculateQuickestPath(grid, PlayerState(TORCH, target))
            return scoreThePath(shortestPath)
        }

        private fun scoreThePath(path: List<PlayerState>): Int {
            return (1 until path.size).sumBy { index ->
                val previous = path[index - 1]
                val current = path[index]
                val distance = manhattanDistance(previous, current)

                when (distance) {
                    0 -> 7
                    1 -> 1
                    else -> throw RuntimeException("distance between neighbours should only be 1 or 7")
                }
            }
        }

        private fun createGrid(target: Point, depth: Int): Map<Point, Tile> {
            return (0..target.y + 66).flatMap { y ->
                (0..target.x + 66).map { x ->
                    val point = Point(x, y)
                    val type = type(point, target, depth)

                    point to type
                }
            }.toMap()
        }

        private fun calculateQuickestPath(grid: Map<Point, Tile>, goal: PlayerState): List<PlayerState> {
            val start = PlayerState(TORCH, Point(0, 0))

            // The set of nodes already evaluated
            val closedSet = mutableSetOf<PlayerState>()

            // The set of currently discovered nodes that are not evaluated yet.
            // Initially, only the start node is known.
            val openSet = mutableSetOf(start)

            // For each node, which node it can most efficiently be reached from.
            // If a node can be reached from many nodes, cameFrom will eventually contain the
            // most efficient previous step.
            val cameFrom = mutableMapOf<PlayerState, PlayerState>()

            // For each node, the cost of getting from the start node to that node.
            val gScore = mutableMapOf<PlayerState, Int>()
            gScore[start] = 0

            val fScore = mutableMapOf<PlayerState, Int>()
            fScore[start] = heuristicCostEstimate(start, goal)

            fun getGScore(state: PlayerState): Int {
                return gScore[state] ?: (Int.MAX_VALUE / 2)
            }

            while (openSet.isNotEmpty()) {
                // The node in the open set having the lowest fScore value
                val current = openSet.minBy{ fScore[it]!! }!! //fScore.minBy { it.value }!!.key

                if (current == goal) {
                    return reconstructPath(cameFrom, current)
                }

                openSet.remove(current)
                closedSet.add(current)

                // get the neighbours of the current node
                val nextStatesNotValidated = nextStates(grid, current)
                val validNextStates = nextStatesNotValidated.filter { nextPlayerState ->

                    val nextStateTile = grid[nextPlayerState.position]
                    val equippedToolAllowedForTile = nextStateTile?.validTools?.contains(nextPlayerState.currentTool) ?: false

                    equippedToolAllowedForTile
                }

                for (neighbour in validNextStates) {
                    // Ignore the neighbor which is already evaluated.
                    if (neighbour in closedSet) {
                        continue
                    }

                    val tentativeGScore = getGScore(current) + distanceBetween(current, neighbour)

                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour)
                    }
                    else if (tentativeGScore >= getGScore(neighbour)) {
                        continue
                    }

                    // This path is the best until now. Record it!
                    cameFrom[neighbour] = current
                    gScore[neighbour] = tentativeGScore
                    fScore[neighbour] = gScore[neighbour]!! + heuristicCostEstimate(neighbour, goal)
                }
            }

            throw RuntimeException("Could not find a path")
        }

        private fun distanceBetween(current: PlayerState, neighbour: PlayerState): Int {
            val distance = heuristicCostEstimate(current, neighbour)
            if (distance != 7 && distance != 1) throw RuntimeException("Move should take 1 or 7 minutes! distance = $distance")

            return distance
        }

        private fun reconstructPath(cameFrom: MutableMap<PlayerState, PlayerState>, sCurrent: PlayerState): List<PlayerState> {
            var current = sCurrent
            val totalPath = mutableListOf(current)

            while (cameFrom.containsKey(current)) {
                current = cameFrom[current]!!
                totalPath.add(current)
            }

            return totalPath.reversed()
        }

        private fun heuristicCostEstimate(start: PlayerState, goal: PlayerState): Int {
            val toolChangeTime = if (start.currentTool == goal.currentTool) 0 else 7
            return manhattanDistance(start, goal) + toolChangeTime
        }

        private fun manhattanDistance(start: PlayerState, goal: PlayerState) =
                Math.abs(start.position.x - goal.position.x) + Math.abs(start.position.y - goal.position.y)

        private fun nextStates(grid: Map<Point, Tile>, state: PlayerState): List<PlayerState> {
            // State where you equip
            val tile: Tile = grid[state.position]!!

            val equippableTools = tile.validTools.filter { it != state.currentTool }
            if (equippableTools.size != 1) throw RuntimeException("Did not expect this to happen")

            // State where you change tools
            val toolChangeState = state.copy(currentTool = equippableTools.first())

            // Return a list of states, filter for ones that are on the grid
            return listOf(toolChangeState, state.up(), state.right(), state.down(), state.left())
                    .filter { nextState -> grid.containsKey(nextState.position) }
        }
    }
}