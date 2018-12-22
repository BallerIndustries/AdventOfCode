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
    //@Ignore("This case is failling, and I do not care to fix it")
    fun `summary number 1`() {
        val initial = """
            #######
            #G..#E#
            #E#E.E#
            #G.##.#
            #...#E#
            #...E.#
            #######
            """.trimIndent()

        val expectedState = """
            #######
            #...#E#
            #E#...#
            #.E##.#
            #E..#E#
            #.....#
            #######
        """.trimIndent()

        val (score, state) = puzzle.solveOneWithState(initial)
        assertEquals(expectedState, state)
        assertEquals(36334, score)
    }

    @Test
    fun `summary number 2`() {
        val initial = """
            #######
            #E..EG#
            #.#G.E#
            #E.##E#
            #G..#.#
            #..E#.#
            #######
            """.trimIndent()

        val expectedState = """
            #######
            #.E.E.#
            #.#E..#
            #E.##.#
            #.E.#.#
            #...#.#
            #######
        """.trimIndent()

        val (score, state) = puzzle.solveOneWithState(initial)
        assertEquals(expectedState, state)
        assertEquals(39514, score)
    }

    @Test
    fun `puzzle part a`() {
        // 208160 is too low
        val result = puzzle.solveOne(puzzleText)
        assertEquals(208960, result)
    }

    @Test
    @Ignore("Runs too slowly")
    fun `puzzle part b`() {
        // Not 23
        // Not Attack = 23 48864
        // Not attack = 23 47337 (roundCount - 1)
        // Not Attack = 25 48515
        // Not Attack = 25 46950 (roundCount - 1)
        // Not Attack = 67 38913
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(49863, result)
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

        val actual = puzzle.stateAfter(exampleText, 1)
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

        val oneToTwo = puzzle.stateAfter(exampleText, 2)
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

        val actual = puzzle.stateAfter(exampleText, 3)
        assertEquals(expected, actual)
    }

    val fightExampleText = """
        #######
        #.G...#
        #...EG#
        #.#.#G#
        #..G#E#
        #.....#
        #######
    """.trimIndent()

    @Test
    fun `fight example a, after one step`() {
        val actual = puzzle.stateAfter(fightExampleText, 1)
        val expected = """
            #######
            #..G..#
            #...EG#
            #.#G#G#
            #...#E#
            #.....#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `fight example a, after 2 steps`() {
        val actual = puzzle.stateAfter(fightExampleText, 2)
        val expected = """
            #######
            #...G.#
            #..GEG#
            #.#.#G#
            #...#E#
            #.....#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `fight example a, after 23 steps`() {
        val actual = puzzle.stateAfter(fightExampleText, 23)
        val expected = """
            #######
            #...G.#
            #..G.G#
            #.#.#G#
            #...#E#
            #.....#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `fight exmple a, state 23 to 24, as one step`() {
        val state23 = """
            #######
            #...G.#
            #..G.G#
            #.#.#G#
            #...#E#
            #.....#
            #######
        """.trimIndent()

        val actual = puzzle.stateAfter(state23, 1)

        val expected = """
            #######
            #..G..#
            #...G.#
            #.#G#G#
            #...#E#
            #.....#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `fight example a, after 24 steps`() {
        val actual = puzzle.stateAfter(fightExampleText, 24)
        val expected = """
            #######
            #..G..#
            #...G.#
            #.#G#G#
            #...#E#
            #.....#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `fight example a, after 25 steps`() {
        val actual = puzzle.stateAfter(fightExampleText, 25)
        val expected = """
            #######
            #.G...#
            #..G..#
            #.#.#G#
            #..G#E#
            #.....#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `fight example a, after 28 steps`() {
        val actual = puzzle.stateAfter(fightExampleText, 28)
        val expected = """
            #######
            #G....#
            #.G...#
            #.#.#G#
            #...#E#
            #....G#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `fight example a, after 47 steps`() {
        val actual = puzzle.stateAfter(fightExampleText, 47)
        val expected = """
            #######
            #G....#
            #.G...#
            #.#.#G#
            #...#.#
            #....G#
            #######
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun `puzzle example a`() {
        val actual = puzzle.solveOne(fightExampleText)
        assertEquals(27730, actual)
    }

    @Test
    fun `example a, goblin 1 should be able to reach elf`() {
        val grid = puzzle.parseGrid(exampleText)
        val ( canBeReached, stepCount ) = puzzle.isReachable(grid, Puzzle15.Point(1, 1), Puzzle15.Point(3, 4))
        assertTrue(canBeReached)
        assertEquals(5, stepCount.size)
    }

    @Test
    fun `example a, goblin 1 should be able to reach point right next to him`() {
        val grid = puzzle.parseGrid(exampleText)
        val ( canBeReached, stepCount ) = puzzle.isReachable(grid, Puzzle15.Point(1, 1), Puzzle15.Point(2, 1))
        assertTrue(canBeReached)
        assertEquals(1, stepCount.size)
    }

    @Test
    fun `example a, goblin 1 should be able to reach his pwn point`() {
        val grid = puzzle.parseGrid(exampleText)
        val ( canBeReached, stepCount ) = puzzle.isReachable(grid, Puzzle15.Point(1, 1), Puzzle15.Point(1, 1))
        assertTrue(canBeReached)
        assertEquals(0, stepCount.size)
    }

    class Puzzle15 {

        val defaultElfGenerator = { point: Point -> Elf(point) }

        fun solveOneWithState(puzzleText: String, elfGenerator: (Point) -> Elf = defaultElfGenerator): Triple<Int, String, Int> {
            var grid = parseGrid(puzzleText)
            val soldiers = getSoldiers(grid, elfGenerator)
            var roundCount = 0

            while (true) {
                grid = runCycle(grid, soldiers)

                if (oneSideIsDead(soldiers)) break



                roundCount++

                //println("Round $roundCount done! elfTotalHp = $elfTotalHp goblinTotalHp = $goblinTotalHp")
            }

            val score = soldiers.filter { !it.isDead() }.sumBy { it.hp } * (roundCount)
            val state = outputGrid(grid)
            val numElfDeaths = soldiers.filter { it.isDead() && it.type == Type.ELF }.count()

            return Triple(score, state, numElfDeaths)
        }

        private fun oneSideIsDead(soldiers: List<Soldier>): Boolean {
            val elvesAlive = soldiers.filter { it.type == Type.ELF }.count { !it.isDead() }
            val goblinsAlive = soldiers.filter { it.type == Type.GOBLIN }.count { !it.isDead() }

            if (elvesAlive == 0 || goblinsAlive == 0) {
                return true
            }
            return false
        }

        fun solveOne(puzzleText: String): Int {
            return solveOneWithState(puzzleText).first
        }

        fun stateAfter(puzzleText: String, numCycles: Int = 10): String {
            var grid = parseGrid(puzzleText)
            val soldiers = getSoldiers(grid) { point -> Elf(point) }

            (0 until numCycles).forEach {
                grid = runCycle(grid, soldiers)
            }

            return outputGrid(grid)
        }

        private fun runCycle(gridState: Map<Point, Char>, soldiers: List<Soldier>): Map<Point, Char> {
            var mutableGridState = gridState
            val sortedSoldiers = soldiers.filter { !it.isDead() }.sortedWith(unitCompare)

            for (index in 0 until sortedSoldiers.size) {

                if (oneSideIsDead(soldiers)) {
                    return mutableGridState
                }


                val currentUnit = sortedSoldiers[index]

                if (currentUnit.isDead()) {
                    continue
                }


                mutableGridState = tryMoveSoldier(currentUnit, mutableGridState, sortedSoldiers)
                //println("Unit #$index/${sortedSoldiers.lastIndex} finished move")

                mutableGridState = tryAttackEnemy(currentUnit, mutableGridState, sortedSoldiers)
                //println("Unit #$index/${sortedSoldiers.lastIndex} finished attack")
            }

            return mutableGridState
        }

        private fun tryAttackEnemy(currentUnit: Soldier, grid: Map<Point, Char>, soldiers: List<Soldier>): Map<Point, Char> {
            if (currentUnit.isDead()) {
                return grid
//                println("Hey I am dead and I am trying to attack! Wtf mate")
            }

            val enemyUnits = soldiers.filter { it.type != currentUnit.type && !it.isDead() }

            val enemiesNextToMe = currentUnit
                .position.getAdjacentTiles()
                .mapNotNull { point -> enemyUnits.find { it.position == point } }

            if (enemiesNextToMe.isEmpty()) {
                return grid
            }

            // Attack
            val enemyToAttack = enemiesNextToMe.sortedWith(hpAndPointCompare).first()
            enemyToAttack.receiveDamage(currentUnit.attack)

            return updateGridState(grid, soldiers)
        }

        private fun tryMoveSoldier(currentUnit: Soldier, grid: Map<Point, Char>, soldiers: List<Soldier>): Map<Point, Char> {
            if (currentUnit.isDead()) {
                println("I am dead and trying to move wtf mate!")
            }


            val enemyUnits = soldiers.filter { it.type != currentUnit.type && !it.isDead() }
            val pointsAdjacentToEnemies = enemyUnits.flatMap { enemy -> enemy.position.getAdjacentTiles() }.toSet()

            // Already standing next to an enemy. No need to move.
            if (pointsAdjacentToEnemies.contains(currentUnit.position)) {
                return grid
            }

            // Find N, E, S, W reachable points

            val reachablePointsToPath = pointsAdjacentToEnemies
                .map { point -> point to isReachable(grid, currentUnit.position, point) }
                .filter { it.second.first }
                .map { it.first to it.second.second }

            // No paths, we cannot move. Job done.
            if (reachablePointsToPath.isEmpty()) {
                return grid
            }

            val minPathLength = reachablePointsToPath.minBy { it.second.size }!!.second.size

            val chosenPoint = reachablePointsToPath
                .filter { it.second.size == minPathLength }
                .map { it.first }
                .sortedWith(pointCompare)
                .first()

            // Okay we know which point we should go for. Now we need to decide which step to take. NORTH, WEST, EAST or SOUTH
            val dog = currentUnit.position.getFreeAdjacentTiles(grid)
                .map { point ->
                    val (isReachable, path) = isReachable(grid, point, chosenPoint)
                    val mutable = path.toMutableList()
                    mutable.add(0, point)
                    isReachable to mutable
                }
                .filter { it.first }
                .map { it.second }

            val minDogLength = dog.minBy { it.size }!!.size
            val stepToTake = dog.filter { it.size == minDogLength }.first().first()
            currentUnit.moveTo(stepToTake)

            return updateGridState(grid, soldiers)
        }

        private fun efficientGetPathsToPoints(reachablePoints: List<Point>, grid: Map<Point, Char>, currentUnit: Soldier): List<List<Point>> {
            val pointsSortedByManDistance = reachablePoints
                .sortedBy { manhattanDistance(currentUnit.position, it) }

            if (pointsSortedByManDistance.isEmpty()) return emptyList()

            // Output min man distance
            //println("Minimum distance to point is ${manhattanDistance(currentUnit.position, pointsSortedByManDistance.first())}")

            val shortestPathToFirstPoint = getShortestPaths(grid, currentUnit.position, pointsSortedByManDistance.first())
            var minPathLength = shortestPathToFirstPoint!!.first().size

            val allPaths = pointsSortedByManDistance.mapNotNull { point ->

                val distToPoint = manhattanDistance(currentUnit.position, point)

                if (distToPoint > minPathLength) {
                    null
                }
                else {
                    val jurPaths = getShortestPaths(grid, currentUnit.position, point, minPathLength)

                    if (jurPaths == null) {
                        jurPaths
                    }
                    else {
                        if (jurPaths!!.firstOrNull()?.size ?: Int.MAX_VALUE < minPathLength) {
                            minPathLength = jurPaths.first().size
                        }
                        jurPaths
                    }
                }

            }.flatten()

            return allPaths.filter { it.size == minPathLength }
        }

        fun manhattanDistance(a: Point, b: Point): Int {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y)
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

            return dog
        }


        fun getShortestPaths(grid: Map<Point, Char>, a: Point, b: Point, maxLength: Int = Int.MAX_VALUE): List<List<Point>>? {
            var allPaths = listOf(listOf(a))


            while (true) {
                val tailPoints = allPaths.map { it.last() }

                //if (allPaths.any { it.size > maxLength} ) return emptyList()

                if (tailPoints.any { it == b }) {
                    break
                }

                if (allPaths.any { it.size > maxLength }) return null

                allPaths = allPaths.flatMap { list ->
                    val lastItem = list.last()
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
            fun north() = this.copy(y = this.y - 1)
            fun south() = this.copy(y = this.y + 1)
            fun west() = this.copy(x = this.x - 1)
            fun east() = this.copy(x = this.x + 1)

            fun getFreeAdjacentTiles(grid: Map<Point, Char>): List<Point> {
                return getAdjacentTiles()
                    .map { it to grid[it] }
                    .filter { it.second != null && it.second == '.' }
                    .map { it.first }
            }

            fun getAdjacentTiles(): List<Point> {
                return listOf(north(), west(), east(), south())
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

        fun isReachable(grid: Map<Point, Char>, start: Point, end: Point): Pair<Boolean, List<Point>> {
            val visited = mutableSetOf(start)
            val toProcess = LinkedList<Pair<Point, MutableList<Point>>>()
            toProcess.add(start to mutableListOf())

            while (toProcess.isNotEmpty()) {
                val (currentPoint, path) = toProcess.pop()

                if (currentPoint == end) {
                    return true to path
                }

                visited.add(currentPoint)

                val adjacentTiles = currentPoint
                    .getFreeAdjacentTiles(grid)
                    .filter { !visited.contains(it) }
                    .map { it to (path + it).toMutableList() }

                // Add adjacent tiles we have not visited
                toProcess.addAll(adjacentTiles)
                visited.addAll(adjacentTiles.map { it.first })
//                stepCount++
            }

            return false to listOf()
        }

        private fun getSoldiers(grid: Map<Point, Char>, elfGenerator: (Point) -> Elf): List<Soldier> {
            val units = grid.mapNotNull { (point, char) ->
                when (char) {
                    'G' -> Goblin(point)
                    'E' -> elfGenerator(point)
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

        fun solveTwo(puzzleText: String): Int {
            var currentAttack = 68
            var lastSuccessfulScore = 0

            while (true) {
                val elfGenerator = { point: Point -> Elf(point, attack = currentAttack)}
                val (score, _, numDeadElves) = solveOneWithState(puzzleText, elfGenerator)

                if (numDeadElves > 0) {
                    println("An elf died when currentAttack = $currentAttack")
                    return lastSuccessfulScore
                }
                else {
                    lastSuccessfulScore = score
                }

                println("Flawless elf victory when currentAttack = $currentAttack score = $score. Going to try currentAttack = ${currentAttack - 1}")
                currentAttack--
            }
        }
    }
}
