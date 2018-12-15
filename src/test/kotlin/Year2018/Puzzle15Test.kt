package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class Puzzle15Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle15.txt").readText().replace("\r", "")
    val puzzle = Puzzle15()

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

    @Test
    fun `can find shortest path for two points right next to each other`() {
        val grid = puzzle.parseGrid(puzzleText)
        val shortestPath = puzzle.getAllPaths(grid, Puzzle15.Point(3, 4), Puzzle15.Point(4, 4), setOf(), listOf())
        assertEquals(listOf(Puzzle15.Point(3, 4), Puzzle15.Point(4, 4)), shortestPath)
    }

    @Test
    fun `can find shortest path for two points right two units away from each other`() {
        val grid = puzzle.parseGrid(puzzleText)
        val shortestPath = puzzle.getAllPaths(grid, Puzzle15.Point(3, 4), Puzzle15.Point(5, 4), setOf(), listOf())
        assertEquals(listOf(Puzzle15.Point(3, 4), Puzzle15.Point(4, 4)), shortestPath)
    }

    class Puzzle15 {

        fun getAllPaths(grid: Map<Point, Char>, a: Point, b: Point): List<List<Point>> {
            val startPath = listOf(a)
            val adjacentTiles = a.getFreeAdjacentTiles(grid)

            val allPaths = adjacentTiles.map { adjacentTile -> octopusOut(grid, adjacentTile, b, startPath) }
            return allPaths
        }

        fun octopusOut(grid: Map<Point, Char>, a: Point, b: Point, path: List<Point>): List<Point> {
            if (a == b) return path


        }


        enum class Type { ELF, GOBLIN }

        data class Point(val x: Int, val y: Int) {
            fun getFreeAdjacentTiles(grid: Map<Point, Char>): List<Point> {
                val northPoint = this.copy(y = this.y - 1)
                val southPoint = this.copy(y = this.y + 1)
                val westPoint = this.copy(x = this.x - 1)
                val eastPoint = this.copy(x = this.x + 1)

                return listOf(northPoint, southPoint, westPoint, eastPoint)
                    .map { it to grid[it] }
                    .filter { it.second != null && it.second == '.' }
                    .map { it.first }
            }
        }

        data class Elf(override val position: Point, override val type: Type = Type.ELF, override val hp: Int = 200, override val attack: Int = 3) : Soldier

        data class Goblin(override val position: Point, override val type: Type = Type.GOBLIN, override val hp: Int = 200, override val attack: Int = 3) : Soldier

        interface Soldier {
            val type: Type
            val position: Point
            val hp: Int
            val attack: Int

            fun getFreeAdjacentTiles(grid: Map<Point, Char>): List<Point> {
                return position.getFreeAdjacentTiles(grid)
            }
        }

        val unitCompare = Comparator<Soldier> { a, b -> pointCompare.compare(a.position, b.position) }

        val pointCompare = Comparator<Point> { a, b ->
            when {
                a.y > b.y -> 1
                a.y < b.y -> -1
                a.x > b.x -> 1
                a.x < b.x -> -1
                else -> 0
            }
        }

        fun solveOne(puzzleText: String): String {
            val grid = parseGrid(puzzleText)

            // Go through the grid, find a list of goblins
            val units = getUnits(grid)

            for (index in 0 until units.size) {
                val currentUnit = units[index]

                // 1. identifying all possible targets (enemy units)
                val enemyUnits = units.filter { it.type != currentUnit.type }

                // 2. identifies all of the open squares (.) that are in range of each target;
                // these are the squares which are adjacent (immediately up, down, left, or right) to any target and
                // which aren't already occupied by a wall or another unit.
                val pointAdjacentToEnemy = enemyUnits.flatMap { enemy -> enemy.getFreeAdjacentTiles(grid) }.toSet()

                // Already next to an enemy, do not need to move
                if (pointAdjacentToEnemy.contains(currentUnit.position)) {

                }
                else {

                    val reachablePoints = pointAdjacentToEnemy
                        .filter { point -> isReachable(grid, currentUnit.position, point) }
                        .map { point -> point to manhattanDistance(currentUnit.position, point) }

                    val minDistance: Int = reachablePoints.minBy { it.second }!!.second

                    val chosenPoint = reachablePoints
                        .filter { it.second == minDistance }
                        .map { it.first }
                        .sortedWith(pointCompare)
                        .first()
                }

                // 3. attack the enemy unit if you are already in range of it.

            }

            return ""
        }

        private fun isReachable(grid: Map<Point, Char>, start: Point, end: Point): Boolean {
            if (end == start) return true

            // Start at start
            val toProcess = LinkedList<Point>()
            val visited = mutableSetOf<Point>()

            while (toProcess.isNotEmpty()) {
                val currentPoint = toProcess.pop()

                if (currentPoint == end) {
                    return true
                }

                visited.add(currentPoint)

                val adjacentTiles: List<Point> = currentPoint.getFreeAdjacentTiles(grid)
                    .filter { !visited.contains(it) }

                // Add adjacent tiles we have not visited
                toProcess.addAll(adjacentTiles)
            }

            return false
        }

        private fun getUnits(grid: Map<Point, Char>): List<Soldier> {
            val units = grid.mapNotNull { (point, char) ->
                when (char) {
                    'G' -> Goblin(point)
                    'E' -> Elf(point)
                    else -> null
                }
            }.sortedWith(unitCompare)
            return units
        }



        fun manhattanDistance(a: Point, b: Point): Int {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
        }

        fun parseGrid(puzzleText: String): Map<Point, Char> {
            val lines = puzzleText.split("\n")
            val width = lines[0].length
            val height = lines.size

            return (0 until width).flatMap { x ->
                (0 until height).map { y ->

                    val point = Point(x, y)
                    val char = lines[y][x]

                    point to char
                }
            }.toMap()
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }
    }
}