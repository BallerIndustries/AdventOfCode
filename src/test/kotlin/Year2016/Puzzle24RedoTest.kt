package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle24RedoTest {
    val puzzle = Puzzle24Redo()
    val puzzleText = this::class.java.getResource("/2016/puzzle24.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        //42728 too high
        //1220 too high
        //902?
        val result = puzzle.solveOne(puzzleText)
        assertEquals(12748, result)
    }

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(14, result)
    }

    @Test
    fun `can solve part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(479009308, result)
    }

    val exampleText = """
            ###########
            #0.1.....2#
            #.#######.#
            #4.......3#
            ###########
        """.trimIndent()

    @Test
    fun `can build weighted graph for example text`() {
        val graph = puzzle.buildWeightedGraph(exampleText).toMap()
        val expected = mapOf(
            '4' to setOf(Puzzle24Redo.Path('0', 2), Puzzle24Redo.Path('3', 8)),
            '3' to setOf(Puzzle24Redo.Path('2', 2), Puzzle24Redo.Path('4', 8)),
            '1' to setOf(Puzzle24Redo.Path('0', 2), Puzzle24Redo.Path('2', 6)),
            '2' to setOf(Puzzle24Redo.Path('1', 6), Puzzle24Redo.Path('3', 2)),
            '0' to setOf(Puzzle24Redo.Path('1', 2), Puzzle24Redo.Path('4', 2))
        )

        assertEquals(expected, graph)
    }

    @Test
    fun `can build weighted graph for real puzzle`() {
        val dog = puzzle.buildWeightedGraph(puzzleText)
        println(dog)
    }

    @Test
    fun `can get min distance from 0 to 1 for example`() {
        val distance: Int = puzzle.minDistance(exampleText, '0', '1')
        assertEquals(2, distance)
    }

    @Test
    fun `can get min distance from 0 to 4 for example`() {
        val distance: Int = puzzle.minDistance(exampleText, '0', '4')
        assertEquals(2, distance)
    }

    @Test
    fun `can get min distance from 4 to 3 for example`() {
        val distance: Int = puzzle.minDistance(exampleText, '4', '3')
        assertEquals(8, distance)
    }

    @Test
    fun `distance from 0 to 2 should be unreachable`() {
        val distance: Int = puzzle.minDistance(exampleText, '0', '2')
        assertEquals(-1, distance)
    }
}

class Puzzle24Redo {
    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)
        fun neighbours() = listOf(up(), down(), left(), right())
    }

    data class Path(val nodeName: Char, val weight: Int)

    fun solveOne(puzzleText: String): Int {
        val graph = buildWeightedGraph(puzzleText)
        println(graph['4'])

        val initialNodesToVisit = graph.keys.filter { it != '0' }.toSet()
        val continues = mutableListOf(Triple('0', initialNodesToVisit, 0))
        val solutions = mutableSetOf<Int>()

        while (true) {
            var currentNode = '0'
            var nodesToVisit = initialNodesToVisit
            var distanceTravelled = 0

            while (nodesToVisit.isNotEmpty()) {
                // Visit the current node
                nodesToVisit = nodesToVisit.filter { it != currentNode }.toSet()

                if (nodesToVisit.isEmpty()) {
                    if (distanceTravelled < solutions.min() ?: Int.MAX_VALUE) {
                        solutions.add(distanceTravelled)
                        val distHome = graph[currentNode]!!.find { it.nodeName == '0' }!!.weight
                        val distanceAndHome = distanceTravelled + distHome
                        println("distanceAndHome = $distanceAndHome distanceTravelled = $distanceTravelled currentNode = $currentNode")
                    }

                    break
                }

                if (distanceTravelled > solutions.min() ?: Int.MAX_VALUE) {
                    break
                }

                // Figure out what paths we can take
                val randomPathsWeCanTake = graph[currentNode]!!.shuffled()

                // Take the first path
                val firstPath = randomPathsWeCanTake.first()
                currentNode = firstPath.nodeName
                distanceTravelled += firstPath.weight

                // Add the remaining paths as continuations
                val newContinuations = randomPathsWeCanTake.subList(1, randomPathsWeCanTake.size).map { futurePath ->
                    Triple(futurePath.nodeName, nodesToVisit, distanceTravelled + futurePath.weight)
                }

                continues.addAll(newContinuations)
            }
        }

        return 100
    }

    fun buildWeightedGraph(puzzleText: String): Map<Char, Set<Path>> {
        val (placesToVisit, _, startPosition) = parseGrid(puzzleText)
        val graph = mutableMapOf<Char, Set<Path>>()
        val dorks = (placesToVisit + ('0' to startPosition)).entries.map { it.key to it.value }

        for (index in 0 until dorks.size) {
            for (jindex in index + 1 until dorks.size) {

                val from = dorks[index].first
                val to = dorks[jindex].first
                val minDistance = minDistance(puzzleText, from, to)

                if (minDistance == -1) {
                    continue
                }

                graph[from] = (graph[from] ?: setOf()) + Path(to, minDistance)
                graph[to] = (graph[to] ?: setOf()) + Path(from, minDistance)
            }
        }
        return graph
    }

    fun solveTwo(puzzleText: String): Int {
        val graph = buildWeightedGraph(puzzleText)
//        println(graph['4'])

        val initialNodesToVisit = graph.keys.filter { it != '0' }.toSet()
        val continues = mutableListOf(Triple('0', initialNodesToVisit, 0))
        val solutions = mutableSetOf<Int>()

        while (true) {
            var currentNode = '0'
            var nodesToVisit = initialNodesToVisit
            var distanceTravelled = 0

            while (nodesToVisit.isNotEmpty()) {
                // Visit the current node
                nodesToVisit = nodesToVisit.filter { it != currentNode }.toSet()

                if (nodesToVisit.isEmpty()) {
                    val distHome = graph[currentNode]!!.find { it.nodeName == '0' }!!.weight
                    val distanceAndHome = distanceTravelled + distHome

                    if (distanceAndHome < solutions.min() ?: Int.MAX_VALUE) {
                        solutions.add(distanceAndHome)
                        println("distanceAndHome = $distanceAndHome")
                    }

                    break
                }

                if (distanceTravelled > solutions.min() ?: Int.MAX_VALUE) {
                    break
                }

                // Figure out what paths we can take
                val randomPathsWeCanTake = graph[currentNode]!!.shuffled()

                // Take the first path
                val firstPath = randomPathsWeCanTake.first()
                currentNode = firstPath.nodeName
                distanceTravelled += firstPath.weight

                // Add the remaining paths as continuations
                val newContinuations = randomPathsWeCanTake.subList(1, randomPathsWeCanTake.size).map { futurePath ->
                    Triple(futurePath.nodeName, nodesToVisit, distanceTravelled + futurePath.weight)
                }

                continues.addAll(newContinuations)
            }
        }

        return 100
    }

    private fun manhattanDistance(a: Point, b: Point) = Math.abs(a.x - b.x) + Math.abs(a.y - b.y)

    private fun parseGrid(puzzleText: String): Triple<Map<Char, Point>, Map<Point, Char>, Point> {
        val gridWithDigits = parseGridWithDigits(puzzleText)
        val placesToVisit = gridWithDigits.entries
                .filter { it.value.isDigit() }
                .associate { (key, value) -> value to key }

        val grid = gridWithDigits.entries.associate { (key, value) ->
            if (value.isDigit()) {
                key to '.'
            } else {
                key to value
            }
        }

        val startPosition = placesToVisit['0']!!
        return Triple(placesToVisit.filter { it.value != startPosition }, grid, startPosition)
    }

    private fun parseGridWithDigits(puzzleText: String): Map<Point, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines[0].count()

        return (0 until width).flatMap { x -> (0 until height).map { y -> Point(x, y) to lines[y][x] } }.toMap()
    }

    fun minDistance(puzzleText: String, from: Char, to: Char): Int {
        val gridWithDigits = parseGridWithDigits(puzzleText)
        val start = gridWithDigits.entries.find { it.value == from }!!.key
        val finish = gridWithDigits.entries.find { it.value == to }!!.key

        val visited = mutableSetOf(start)
        var frontier = mutableSetOf(start)
        var steps = 0

        while (!frontier.contains(finish)) {
            frontier = frontier
                .flatMap { point -> point.neighbours() }
                .filter { point ->
                    val char = gridWithDigits[point]!!
                    (char == '.' || char == to) && !visited.contains(point)
                }
                .toMutableSet()

            if (frontier.isEmpty()) {
                return -1
            }

            visited.addAll(frontier)
            steps++
        }

        return steps
    }
}
