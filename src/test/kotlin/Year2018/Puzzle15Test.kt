package Year2018

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import java.lang.RuntimeException
import java.util.*

class Puzzle15Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle15.txt").readText().replace("\r", "")
    val puzzle = Puzzle15()

    val exampleText = """
            #########
            #G..G..G#
            #.......#
            #.......#
            #G..E..G#
            #.......#
            #.......#
            #G..G..G#
            #########
        """.trimIndent()

    @Test
    @Ignore
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    @Ignore
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }


    @Test
    fun `example a, after one step`() {
        val expected = """
            #########
            #.G...G.#
            #...G...#
            #...E..G#
            #.G.....#
            #.......#
            #G..G..G#
            #.......#
            #########
        """.trimIndent()

        val actual = puzzle.solveOne(exampleText, 1)
        assertEquals(expected, actual)
    }

    @Test
    fun `example a, after two steps`() {
        val expected = """
            #########
            #..G.G..#
            #...G...#
            #.G.E.G.#
            #.......#
            #G..G..G#
            #.......#
            #.......#
            #########
        """.trimIndent()

        val oneToTwo = puzzle.solveOne(exampleText, 2)
        assertEquals(expected, oneToTwo)
    }

    @Test
    fun `example a, after three steps`() {
        val expected = """
            #########
            #.......#
            #..GGG..#
            #..GEG..#
            #G..G...#
            #......G#
            #.......#
            #.......#
            #########
        """.trimIndent()

        val actual = puzzle.solveOne(exampleText, 3)
        assertEquals(expected, actual)
    }

    @Test
    fun `example a, goblin 1 should be able to reach elf`() {
        val grid = puzzle.parseGrid(exampleText)
        val canBeReached = puzzle.isReachable(grid, Puzzle15.Point(1, 1), Puzzle15.Point(3, 4))
        assertTrue(canBeReached)
    }

    class Puzzle15 {
        fun solveOne(puzzleText: String, numCycles: Int = 10): String {
            var gridState = parseGrid(puzzleText)

            (0 until numCycles).forEach {
                gridState = runCycle(gridState)
            }

            return outputGrid(gridState)
        }

        private fun runCycle(gridState: Map<Point, Char>): Map<Point, Char> {
            val soldiers = getSoldiers(gridState)
            var mutableGridState = gridState

            for (index in 0 until soldiers.size) {
                val currentUnit = soldiers[index]

                mutableGridState = tryMoveSoldier(currentUnit, mutableGridState, soldiers)
                mutableGridState = tryAttackEnemy(currentUnit, mutableGridState, soldiers)
            }

            return mutableGridState
        }

        private fun tryAttackEnemy(currentUnit: Soldier, grid: Map<Point, Char>, soldiers: List<Soldier>): Map<Point, Char> {
            val enemyUnits = soldiers.filter { it.type != currentUnit.type }

            val enemiesNextToMe = currentUnit
                .position.getAdjacentTiles()
                .mapNotNull { point -> enemyUnits.find { it.position == point } }

            if (enemiesNextToMe.isEmpty()) {
                return grid
            }

            // Attack
            val enemyToAttack = enemiesNextToMe.sortedWith(hpAndPointCompare).first()
            enemyToAttack.receiveDamage(3)

            return updateGridState(grid, soldiers)
        }

        private fun tryMoveSoldier(currentUnit: Soldier, grid: Map<Point, Char>, soldiers: List<Soldier>): Map<Point, Char> {
            val enemyUnits = soldiers.filter { it.type != currentUnit.type }
            val pointsAdjacentToEnemies = enemyUnits.flatMap { enemy -> enemy.position.getAdjacentTiles() }.toSet()

            // Already standing next to an enemy. No need to move.
            if (pointsAdjacentToEnemies.contains(currentUnit.position)) {
                return grid
            }

            val reachablePoints = pointsAdjacentToEnemies
                .filter { point -> isReachable(grid, currentUnit.position, point) }

            val pathsToPoints = reachablePoints
                .flatMap { getShortestPaths(grid, currentUnit.position, it) }

            // Ah shit, there are no paths to this point. No moving
            if (pathsToPoints.isEmpty()) {
                return grid
            }

            val minPathLength = pathsToPoints.minBy { it.size }!!.size

            val possibleNextSteps = pathsToPoints
                .filter { it.size == minPathLength }
                .map { it.first() }
                .distinct()
                .sortedWith(pointCompare)

            // Move the soldier to the chosen point
            val chosenPoint = possibleNextSteps.first()
            currentUnit.moveTo(chosenPoint)

            return updateGridState(grid, soldiers)
        }

        private fun outputGrid(grid: Map<Puzzle15.Point, Char>): String {
            val width = grid.keys.maxBy { it.x }!!.x
            val height = grid.keys.maxBy { it.y }!!.y

            val dog = (0..height).map { y ->
                (0..width).map { x ->
                    val point = Point(x, y)
                    grid[point]!!
                }.joinToString("")
            }.joinToString("\n")

//            println(dog)
            return dog
        }


        fun getShortestPaths(grid: Map<Point, Char>, a: Point, b: Point): List<List<Point>> {
            var allPaths = listOf(listOf(a))

            while (true) {
                val tailPoints = allPaths.map { it.last() }

                if (tailPoints.any { it == b }) {
                    break
                }

                allPaths = allPaths.flatMap { list ->
                    val lastItem = list.last()!!
                    val nextTiles = lastItem.getFreeAdjacentTiles(grid).filter { !list.contains(it) }
                    nextTiles.map { nextTile -> list + nextTile }
                }
            }

            val pathsTheEndAtB = allPaths
                .filter { it.last() == b }
                .map { it.subList(1, it.size) }

            val minPathLength = pathsTheEndAtB.minBy { it.size }!!.size
            return pathsTheEndAtB.filter { it.size == minPathLength }
        }

        enum class Type(val char: Char) { ELF('E'), GOBLIN('G') }

        data class Point(val x: Int, val y: Int) {
            fun getFreeAdjacentTiles(grid: Map<Point, Char>): List<Point> {
                return getAdjacentTiles()
                    .map { it to grid[it] }
                    .filter { it.second != null && it.second == '.' }
                    .map { it.first }
            }

            fun getAdjacentTiles(): List<Point> {
                val northPoint = this.copy(y = this.y - 1)
                val southPoint = this.copy(y = this.y + 1)
                val westPoint = this.copy(x = this.x - 1)
                val eastPoint = this.copy(x = this.x + 1)

                return listOf(northPoint, southPoint, westPoint, eastPoint)
            }
        }

        class Elf(override var position: Point, override val type: Type = Type.ELF, override var hp: Int = 200, override val attack: Int = 3) : Soldier

        class Goblin(override var position: Point, override val type: Type = Type.GOBLIN, override var hp: Int = 200, override val attack: Int = 3) : Soldier

        interface Soldier {
            val type: Type
            var position: Point
            var hp: Int
            val attack: Int

            fun getFreeAdjacentTiles(grid: Map<Point, Char>): List<Point> {
                return position.getFreeAdjacentTiles(grid)
            }

            fun moveTo(chosenPoint: Point) {
                this.position = chosenPoint
            }

            fun toChar(): Char {
                return this.type.char
            }

            fun receiveDamage(damage: Int) {
                hp -= damage
            }

            fun isDead() = hp <= 0
        }

        val unitCompare = Comparator<Soldier> { a, b -> pointCompare.compare(a.position, b.position) }

        // TODO: Defer to the comparator on the integers?
        val pointCompare = Comparator<Point> { a, b ->
            when {
                a.y > b.y -> 1
                a.y < b.y -> -1
                a.x > b.x -> 1
                a.x < b.x -> -1
                else -> 0
            }
        }

        val hpAndPointCompare = Comparator<Soldier> { a, b ->
            when {
                a.hp != b.hp -> a.hp.compareTo(b.hp)
                else -> unitCompare.compare(a, b)
            }
        }

        private fun updateGridState(gridState: Map<Point, Char>, soldiers: List<Soldier>): Map<Point, Char> {
            val dog: Map<Point, Char> = gridState.entries.associate { (position, char) ->
                // Look for a soldier at this position
                val soldierAtThisPoint = soldiers.find { it.position == position }

                if (soldierAtThisPoint != null && !soldierAtThisPoint.isDead()) {
                    position to soldierAtThisPoint.toChar()
                } else if (char == 'G' || char == 'E') {
                    position to '.'
                } else if (char == '#' || char == '.') {
                    position to char
                } else {
                    throw RuntimeException("Woah! Didn't expect that! char = $char")
                }
            }

            return dog
        }

        fun isReachable(grid: Map<Point, Char>, start: Point, end: Point): Boolean {
            val visited = mutableSetOf(start)
            val toProcess = LinkedList<Point>()
            toProcess.add(start)

            while (toProcess.isNotEmpty()) {
                val currentPoint = toProcess.pop()

                if (currentPoint == end) {
                    return true
                }

                visited.add(currentPoint)

                val adjacentTiles: List<Point> = currentPoint
                    .getFreeAdjacentTiles(grid)
                    .filter { !visited.contains(it) }

                // Add adjacent tiles we have not visited
                toProcess.addAll(adjacentTiles)
                visited.addAll(adjacentTiles)
            }

            return false
        }

        private fun getSoldiers(grid: Map<Point, Char>): List<Soldier> {
            val units = grid.mapNotNull { (point, char) ->
                when (char) {
                    'G' -> Goblin(point)
                    'E' -> Elf(point)
                    else -> null
                }
            }.sortedWith(unitCompare)
            return units
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
