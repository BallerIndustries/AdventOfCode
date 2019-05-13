package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class Puzzle24Test {
    val puzzle = Puzzle24()
    val puzzleText = this::class.java.getResource("/2016/puzzle24.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        //42728 too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals(12748, result)
    }

    @Test
    fun `example part a`() {
        val exampleText = """
            ###########
            #0.1.....2#
            #.#######.#
            #4.......3#
            ###########
        """.trimIndent()

        val result = puzzle.solveOne(exampleText)
        assertEquals(14, result)
    }

    @Test
    fun `can solve part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(479009308, result)
    }
}

class Puzzle24 {
    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)
        fun neighbours() = listOf(up(), down(), left(), right())
    }

    val random = Random(122)

    data class Continuation(val currentPosition: Point, val remainingPlacesToVisit: Set<Point>, val stepsTaken: Int, val pathTaken: List<Point>)

    fun solveOne(puzzleText: String): Int {
        val (placesToVisit, grid) = parseGrid(puzzleText)
        var currentPosition = placesToVisit['0']!!
        var remainingPlacesToVisit = placesToVisit.filter { it.key != '0' }.map { it.value }.toSet()
        var stepsTaken = 0
        var pathTaken = mutableListOf<Point>(currentPosition)

        val continuations = mutableListOf(Continuation(currentPosition, remainingPlacesToVisit, stepsTaken, pathTaken))
        val continuationStates = mutableSetOf(Pair(currentPosition, remainingPlacesToVisit))
        val solutions = mutableListOf(Int.MAX_VALUE)
        val pathSolutions = mutableListOf<List<Point>>()

        while (continuations.isNotEmpty()) {
            val randomIndex = random.nextInt(continuations.size)
            val tmp = continuations.removeAt(randomIndex)
            currentPosition = tmp.currentPosition
            remainingPlacesToVisit = tmp.remainingPlacesToVisit
            stepsTaken = tmp.stepsTaken
            pathTaken = tmp.pathTaken.map { it }.toMutableList()

            while (remainingPlacesToVisit.isNotEmpty()) {
//                println(currentPosition)

                // Did we visit one of the points?
                if (remainingPlacesToVisit.contains(currentPosition)) {
                    remainingPlacesToVisit = remainingPlacesToVisit.filter { it != currentPosition }.toSet()
                }

                if (remainingPlacesToVisit.isEmpty()) {
                    println("Wow found a path! Only took stepsTaken = $stepsTaken")
                    solutions.add(stepsTaken)
                    pathSolutions.add(pathTaken)
                    break
                }

                if (stepsTaken > solutions.min()!!) {
                    break
                }

                val nextSteps = currentPosition.neighbours()
                    .filter { neighbour -> grid[neighbour] != '#' }

                if (nextSteps.isEmpty()) {
                    println("No point back tracking. Pruning this branch")
                    break
                }

                if (nextSteps.size == 1) {
                    currentPosition = nextSteps.first()
                    pathTaken.add(currentPosition)
                    stepsTaken++
                    continue
                }

                val randomNextSteps = nextSteps.shuffled(random)
                val stepsWeDidNotChoose = randomNextSteps.subList(1, randomNextSteps.size)

                stepsWeDidNotChoose.forEach { nextStep ->
                    continuationStates.add(nextStep to remainingPlacesToVisit)
                    continuations.add(Continuation(nextStep, remainingPlacesToVisit, stepsTaken + 1, pathTaken + nextStep))
                }

                currentPosition = randomNextSteps.first()
                pathTaken.add(currentPosition)
                stepsTaken++
            }
        }

        return solutions.min()!!
    }

    fun manhattanDistance(a: Point, b: Point) = Math.abs(a.x - b.x) + Math.abs(a.y - b.y)

    fun solveTwo(puzzleText: String): Int {
        return 2323
    }

    private fun parseGrid(puzzleText: String): Pair<Map<Char, Point>, Map<Point, Char>> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines[0].count()

        val gridWithDigits = (0 until width).flatMap { x -> (0 until height).map { y -> Point(x, y) to lines[y][x] } }.toMap()
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

        return Pair(placesToVisit, grid)
    }
}
