package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle3Test {
    val puzzle = Puzzle3()
    val puzzleText = this::class.java.getResource("/2018/puzzle3.txt").readText().replace("\r", "")
    val jurText = this::class.java.getResource("/2018/puzzle3-jur.txt").readText().replace("\r", "")
    val exampleText = "#1 @ 1,3: 4x4\n" +
            "#2 @ 3,1: 4x4\n" +
            "#3 @ 5,5: 2x2"

    @Test
    fun example() {
        val horse = puzzle.solve(exampleText)
        assertEquals(horse, 4)
    }

    @Test
    fun jur() {
        val jur = puzzle.solve(jurText)
        assertEquals(jur, 100)
    }

    @Test
    fun `puzzle part a`() {
        val horse = puzzle.solve(puzzleText)
        assertEquals(horse, 113966)
    }

    @Test
    fun `puzzle part b`() {
        val dog = puzzle.solveTwo(puzzleText)
        assertEquals(dog, 235)
    }
}

class Puzzle3 {
    fun solve(puzzleText: String): Int {
        val buffer = mutableMapOf<Pair<Int, Int>, Int>()
        var ops = 0
        var areas = 0

        puzzleText.split("\n").forEach { line ->
            val tmp = line.split(" ")
            val (x, y) = tmp[2].replace(":", "").split(",").map { it.toInt() }
            val (width, height) = tmp[3].split("x").map { it.toInt() }

            areas += (width * height)

            (x until x + width).forEach { curX ->
                (y until y + height).forEach { curY ->
                    val key = Pair(curX, curY)

                    if (!buffer.containsKey(key)) {
                        buffer[key] = 0
                    }

                    buffer[key] = buffer[key]!! + 1
                    ops++
                }
            }
        }

        return buffer.values.filter { it > 1 }.count()
    }

    fun solveTwo(puzzleText: String): Int {
        val buffer = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

        puzzleText.split("\n").forEach { line ->
            val tmp = line.split(" ")
            val (x, y) = tmp[2].replace(":", "").split(",").map { it.toInt() }
            val (width, height) = tmp[3].split("x").map { it.toInt() }
            val claimId = tmp[0].substring(1).toInt()

            (x until x + width).forEach { curX ->
                (y until y + height).forEach { curY ->
                    val key = Pair(curX, curY)

                    if (!buffer.containsKey(key)) {
                        buffer[key] = mutableListOf()
                    }

                    buffer[key] = (buffer[key]!! + mutableListOf(claimId)).toMutableList()
                }
            }
        }

        val claimIds = (1..1347).associate { it to true }.toMutableMap()

        puzzleText.split("\n").forEach { line ->
            val tmp = line.split(" ")
            val (x, y) = tmp[2].replace(":", "").split(",").map { it.toInt() }
            val (width, height) = tmp[3].split("x").map { it.toInt() }
            val claimId = tmp[0].substring(1).toInt()

            (x until x + width).forEach { curX ->
                (y until y + height).forEach { curY ->


                    val key = Pair(curX, curY)
                    val dog = buffer[key]!!

                    if (dog.size > 1) {
                        claimIds[claimId] = false
                    }
                }
            }
        }

        val dog = claimIds.filter { it.value == true }.keys.first()
        return dog
    }
}
