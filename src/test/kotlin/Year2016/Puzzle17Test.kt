package Year2016

import org.apache.commons.codec.digest.DigestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {
    val puzzle = Puzzle17()
    val puzzleText = this::class.java.getResource("/2016/puzzle17.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("DRLRDDURDR", result)
    }

    @Test
    fun `can solve part b`() {
        val result= puzzle.solveTwo(puzzleText)
        assertEquals(500, result)
    }

    @Test
    fun `error there are no paths`() {
        try {
            puzzle.solveOne("hijkl")
            throw AssertionError("This fucker was supposed to throw!")
        }
        catch (rte: RuntimeException) {
            assertEquals(rte.message, "I failed you sensei. There are no paths to the vault :(")
        }
    }

    @Test
    fun `another example`() {
        val result = puzzle.solveOne("ihgpwlah")
        assertEquals("DDRRRD", result)
    }
}

class Puzzle17 {
    val utils = DigestUtils("MD5")

    data class Point(val x: Int, val y: Int) {
        fun eatDirection(direction: Direction): Point {
            return when (direction) {
                Direction.UP -> this.copy(y = this.y - 1)
                Direction.DOWN -> this.copy(y = this.y + 1)
                Direction.LEFT -> this.copy(x = this.x - 1)
                Direction.RIGHT -> this.copy(x = this.x + 1)
            }
        }
    }

    enum class Direction(val value: Char) { UP('U'), DOWN('D'), LEFT('L'), RIGHT('R') }

    fun solveOne(puzzleText: String): String {
        val successfulPaths = getAllPaths(puzzleText)
        if (successfulPaths.isEmpty()) throw RuntimeException("I failed you sensei. There are no paths to the vault :(")
        return successfulPaths.minBy { it.length }!!.replace(puzzleText, "")
    }

    fun solveTwo(puzzleText: String): Int {
        val successfulPaths = getAllPaths(puzzleText)
        return successfulPaths.maxBy { it.length }!!.length - puzzleText.length
    }

    private fun getAllPaths(puzzleText: String): MutableList<String> {
        var currentPosition = Point(1, 1)
        val target = Point(4, 4)
        var currentPath = puzzleText
        val pathsToExplore = mutableListOf(currentPosition to currentPath)
        val successfulPaths = mutableListOf<String>()

        while (pathsToExplore.isNotEmpty()) {
            val dog = pathsToExplore.removeAt(0)
            currentPosition = dog.first
            currentPath = dog.second

            while (true) {
                if (currentPosition == target) {
                    successfulPaths.add(currentPath)
                    break
                }

                val openDoors: List<Direction> = calculateOpenDoors(currentPath, currentPosition)

                if (openDoors.isEmpty()) {
                    break
                }

                if (openDoors.size > 1) {
                    openDoors.subList(1, openDoors.size).forEach { direction ->
                        pathsToExplore.add(currentPosition.eatDirection(direction) to currentPath + direction.value)
                    }
                }

                currentPosition = currentPosition.eatDirection(openDoors[0])
                currentPath += openDoors[0].value
            }
        }
        return successfulPaths
    }

    private fun calculateOpenDoors(currentPath: String, position: Point): List<Direction> {
        val dog = utils.digestAsHex(currentPath).substring(0, 4)
        val openDoors = mutableListOf<Direction>()

        if (dog[0] in 'b'..'f' && position.y > 1) openDoors.add(Direction.UP)
        if (dog[1] in 'b'..'f' && position.y < 4) openDoors.add(Direction.DOWN)
        if (dog[2] in 'b'..'f' && position.x > 1) openDoors.add(Direction.LEFT)
        if (dog[3] in 'b'..'f' && position.x < 4) openDoors.add(Direction.RIGHT)
        return openDoors
    }
}
