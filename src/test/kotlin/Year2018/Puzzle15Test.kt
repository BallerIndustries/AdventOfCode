package Year2018

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.lang.RuntimeException
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
        val shortestPath = puzzle.getShortestPaths(grid, Puzzle15.Point(3, 4), Puzzle15.Point(4, 4))
        assertEquals(listOf(listOf(Puzzle15.Point(3, 4), Puzzle15.Point(4, 4))), shortestPath)
    }

    @Test
    fun `can find shortest path for two points right two units away from each other`() {
        val grid = puzzle.parseGrid(puzzleText)
        val shortestPath = puzzle.getShortestPaths(grid, Puzzle15.Point(3, 4), Puzzle15.Point(5, 4))
        assertEquals(listOf(listOf(Puzzle15.Point(3, 4), Puzzle15.Point(4, 4), Puzzle15.Point(5, 4))), shortestPath)
    }

    @Test
    fun `can find two equally short paths`() {
        val text = """
            ...
            ...
            ...
        """.trimIndent()

        val grid = puzzle.parseGrid(text)
        val shortestPath = puzzle.getShortestPaths(grid, Puzzle15.Point(0, 0), Puzzle15.Point(1, 1))

        assertEquals(listOf(
            listOf(Puzzle15.Point(0, 0), Puzzle15.Point(0, 1), Puzzle15.Point(1, 1)),
            listOf(Puzzle15.Point(0, 0), Puzzle15.Point(1, 0), Puzzle15.Point(1, 1))
            ), shortestPath)
    }

    @Test
    fun `example a`() {
        val text = """
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

        val dog = puzzle.solveOne(text)

    }

    @Test
    fun `example a, goblin 1 should be able to reach elf`() {
        val text = """
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

        val grid = puzzle.parseGrid(text)
        val canBeReached = puzzle.isReachable(grid, Puzzle15.Point(1, 1), Puzzle15.Point(3, 4))
        assertTrue(canBeReached)
    }

    class Puzzle15 {

        fun solveOne(puzzleText: String): String {
            var gridState = parseGrid(puzzleText)
            val soldiers = getSoldiers(gridState)
            var cycle = 0

            while (true) {
                for (index in 0 until soldiers.size) {
                    cycle++
                    println(cycle)

                    val currentUnit = soldiers[index]

                    // 1. identifying all possible targets (enemy units)
                    val enemyUnits = soldiers.filter { it.type != currentUnit.type }

                    // 2. identifies all of the open squares (.) that are in range of each target;
                    // these are the squares which are adjacent (immediately up, down, left, or right) to any target and
                    // which aren't already occupied by a wall or another unit.
                    val pointAdjacentToEnemy = enemyUnits.flatMap { enemy -> enemy.getFreeAdjacentTiles(gridState) }.toSet()

                    // move elf, get on your way, get on your way elf get on your way
                    if (!pointAdjacentToEnemy.contains(currentUnit.position)) {

                        val reachablePoints = pointAdjacentToEnemy
                            .filter { point -> isReachable(gridState, currentUnit.position, point) }

                        val chosenPoint = reachablePoints
                            .map { getShortestPaths(gridState, currentUnit.position, it) }
                            .flatMap{ it.first() }
                            .sortedWith(pointCompare)
                            .first()

                        currentUnit.moveTo(chosenPoint)
                        gridState = updateGridState(gridState, soldiers)
                    }


                    // TODO: Attack part comes back later, lets just get moving right for now
//                    // Get a list of enemies next to this soldier
//                    val enemiesNextToMe = currentUnit
//                        .position.getAdjacentTiles()
//                        .mapNotNull { point ->
//                            enemyUnits.find { it.position == point }
//                        }
//
//                    // ATTACK IF NEXT TO AN ENEMY
//                    if (enemiesNextToMe.isNotEmpty()) {
//
//                        val enemyToAttack = enemiesNextToMe
//                            .sortedWith(hpAndPointCompare)
//                            .first()
//
//                        enemyToAttack.receiveDamage(3)
//                        gridState = updateGridState(gridState, soldiers)
//                    }
                }
            }
        }


        fun getShortestPaths(grid: Map<Point, Char>, a: Point, b: Point): List<List<Point>> {
            var dog = listOf(listOf(a))

            while (true) {
                val tailPoints = dog.map { it.last() }

                if (tailPoints.any { it == b }) {
                    break
                }

                dog = dog.flatMap { list ->
                    val lastItem = list.last()!!
                    val nextTiles = lastItem.getFreeAdjacentTiles(grid).filter { !list.contains(it) }

                    nextTiles.map { nextTile -> list + nextTile }
                }
            }

            return dog.filter { it.last() == b }
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

        data class Elf(override var position: Point, override val type: Type = Type.ELF, override var hp: Int = 200, override val attack: Int = 3) : Soldier

        data class Goblin(override var position: Point, override val type: Type = Type.GOBLIN, override var hp: Int = 200, override val attack: Int = 3) : Soldier

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
                }
                else if (char == 'G' || char == 'E') {
                     position to '.'
                }
                else if (char == '#' || char == '.') {
                    position to char
                }
                else {
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
                //println(currentPoint)

                if (currentPoint == end) {
                    return true
                }

                visited.add(currentPoint)

                val adjacentTiles: List<Point> = currentPoint.getFreeAdjacentTiles(grid)
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
