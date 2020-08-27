package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle2Test {
    val puzzle = Puzzle2()
    val puzzleText = this::class.java.getResource("/2018/puzzle2.txt").readText()
            .replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val ohrse = puzzle.solve(puzzleText)
        assertEquals(7163, ohrse)
    }

    @Test
    fun `puzzle part b`() {
        val dog = puzzle.solveTwo(puzzleText)
        assertEquals("ighfbyijnoumxjlxevacpwqtr", dog)
    }
}

class Puzzle2 {
    fun solve(puzzleText: String): Int {
        var twoCount = 0
        var threeCount = 0

        puzzleText.split("\n").forEach { line ->

            var hasTwo = line.groupBy { it }
                    .filter { it.value.size == 2 }
                    .isNotEmpty()

            var hasThree = line.groupBy { it }
                    .filter { it.value.size == 3 }
                    .isNotEmpty()


            if (hasTwo) twoCount++
            if (hasThree) threeCount++
        }

        return twoCount * threeCount
    }

    fun solveTwo(puzzleText: String): String {
        val allIds = puzzleText.split("\n")

        allIds.forEachIndexed { index, id ->
            (index + 1 until allIds.count()).forEach { jindex ->
                val secondId = allIds[jindex]

                if (isOffByOne(id, secondId)) {
                    return getCommonLetters(id, secondId)
                }
            }
        }

        throw RuntimeException("Unable to find string with one charater off")
    }

    private fun getCommonLetters(id: String, secondId: String): String {
        var buffer = ""
        id.forEachIndexed { index, _ -> if (id[index] == secondId[index]) buffer += id[index] }

        return buffer
    }

    private fun isOffByOne(id: String, secondId: String): Boolean {
        var offCount = 0

        id.forEachIndexed { index, _ ->
            if (id[index] != secondId[index]) offCount++
            if (offCount > 1) return false
        }

        return true
    }
}
