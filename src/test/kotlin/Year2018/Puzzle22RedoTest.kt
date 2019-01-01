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
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    data class PlayerState(val currentTool: Tool, val position: Point)

    data class PlayerStateWithTime(val currentTool: Tool, val position: Point, val timeTaken: Int) {
        fun up() = this.copy(position = this.position.up(), timeTaken = this.timeTaken + 1)
        fun down() = this.copy(position = this.position.down(), timeTaken = this.timeTaken + 1)
        fun right() = this.copy(position = this.position.right(), timeTaken = this.timeTaken + 1)
        fun left() = this.copy(position = this.position.left(), timeTaken = this.timeTaken + 1)

        fun toPlayerState() = PlayerState(currentTool, position)
    }

    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)
    }

    enum class Tool { NONE, TORCH, CLIMBING_GEAR }

    enum class Type(val value: Int, val validTools: List<Tool>) {
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

        private fun type(point: Point, target: Point, depth: Int): Type {
            val jur = erosionLevel(point, target, depth) % 3
            return Type.values().find { it.value == jur }!!
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

            val grid = (0 .. target.y + 10).flatMap { y ->
                (0 .. target.x + 10).map{ x ->
                    val point = Point(x, y)
                    val type = type(point, target, depth)

                    point to type
                }
            }.toMap()

            val minutes = calculateQuickestPath(grid, target)
            return minutes
        }

        private fun nextStates(grid: Map<Point, Type>, state: PlayerStateWithTime): List<PlayerStateWithTime> {
            // State where you equip
            val tile: Type = grid[state.position]!!

            val equippableTools = tile.validTools.filter { it != state.currentTool }
            if (equippableTools.size != 1) throw RuntimeException("Did not expect this to happen")

            // State where you change tools
            val toolChangeState = state.copy(currentTool = equippableTools.first(), timeTaken = state.timeTaken + 7)

            // States where you move up, right, down and left
            return listOf(toolChangeState, state.up(), state.right(), state.down(), state.left())
        }

        private fun calculateQuickestPath(grid: Map<Point, Type>, target: Point): Int {
            val visited = mutableSetOf<PlayerState>()
            val initialState = PlayerStateWithTime(TORCH, Point(0, 0), 0)
            val toProcess = mutableListOf(initialState)

            val answers = mutableListOf<Int>()

            while (toProcess.isNotEmpty()) {
                val currentState: PlayerStateWithTime = toProcess.first()
                toProcess.removeAt(0)

                if (currentState.position == target && currentState.currentTool == Tool.TORCH) {
                    println(currentState.timeTaken)
                    answers.add(currentState.timeTaken)
                }

                visited.add(currentState.toPlayerState())

                val nextStatesNotValidated = nextStates(grid, currentState)
                val validNextStates = nextStatesNotValidated.filter { nextPlayerState ->

                    val notVisited = !visited.contains(nextPlayerState.toPlayerState())
                    val nextStateTile = grid[nextPlayerState.position]
                    val equippedToolAllowedForTile = nextStateTile?.validTools?.contains(nextPlayerState.currentTool) ?: false

                    notVisited && equippedToolAllowedForTile
                }

                toProcess.addAll(validNextStates)
            }

            if (answers.isEmpty()) throw RuntimeException("Unable to find a path. What a crying shame")

            return answers.min()!!

        }
    }
}