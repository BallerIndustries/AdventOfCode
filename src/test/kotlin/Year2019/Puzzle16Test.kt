package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle16.txt").readText().replace("\r", "")
    val puzzle = Puzzle16()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("29795507", result)
    }

    @Test
    fun `example a`() {
        val text = """80871224585914546619083218645595"""
        val result = puzzle.solveOne(text)
        assertEquals("24176176", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(89568529, result)
    }

    @Test
    fun `example b`() {
        val text = "03036732577212944063491565474664"
        val result = puzzle.solveTwo(text)
        assertEquals(84462026, result)
    }

    @Test
    fun `patternMultiplicand when repeat amount = 1`() {
        val basePattern = listOf(0, 1, 0, -1)
        val repeatAmount = 1
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 0))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 1))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 2))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 3))

        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 4))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 5))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 6))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 7))

        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 8))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 9))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 10))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 11))
    }

    @Test
    fun `patternMultiplicand when repeat amount = 2`() {
        val basePattern = listOf(0, 1, 0, -1)
        // 0 1 1 0 0 -1 -1 0
        val repeatAmount = 2
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 0))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 1))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 2))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 3))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 4))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 5))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 6))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 7))

        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 8))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 9))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 10))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 11))
    }

    @Test
    fun `patternMultiplicand when repeat amount = 3`() {
        val basePattern = listOf(0, 1, 0, -1)
        // 0 0 1 1 1 0 0 0 -1 -1 -1 0

        val repeatAmount = 3
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 0))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 1))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 2))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 3))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 4))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 5))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 6))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 7))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 8))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 9))
        assertEquals(-1, puzzle.getMultiplicand(basePattern, repeatAmount, 10))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 11))

        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 12))
        assertEquals(0, puzzle.getMultiplicand(basePattern, repeatAmount, 13))
        assertEquals(1, puzzle.getMultiplicand(basePattern, repeatAmount, 14))
    }
}

class Puzzle16 {

    fun getMultiplicand(basePattern: List<Int>, repeatAmount: Int, index: Int): Int {
        if (repeatAmount == 1) {
            val octopus = index + 1
            return basePattern[octopus % basePattern.size]
        }
        else {
            val octopus = (index + 1) / repeatAmount
            return basePattern[octopus % basePattern.size]
        }
    }

    fun solveOne(puzzleText: String): String {
        var list = puzzleText.map { it.toString().toInt() }
        val basePattern = listOf(0, 1, 0, -1)

        (0 until 100).forEach {
            list = goThroughAPhase(list, basePattern)
            println(it)
        }

        return list.subList(0, 8).joinToString("")
    }

    private fun goThroughAPhase(list: List<Int>, basePattern: List<Int>): List<Int> {
        val phaseTwo = list.mapIndexed { index, _ ->
            val repeatAmount = index + 1
            //val pattern = multiplyAndOffset(basePattern, repeatAmount)
            var sum = 0

            list.forEachIndexed { index, number ->
                val patternMultiplicand = getMultiplicand(basePattern, repeatAmount, index)


                //println("$number * $patternMultiplicand")
                sum += number * patternMultiplicand
            }

            Math.abs(sum) % 10
        }
        return phaseTwo
    }

    fun solveTwo(puzzleText: String): Int {
        val skipAmount = puzzleText.take(7).toInt()

        if (skipAmount < (puzzleText.length / 2)) {
            throw RuntimeException("Didn't think that would happen!")
        }

        val jur = puzzleText.map { it.toInt() }
        var nums = mutableListOf<Int>()
        (0 until 10_000).forEach { nums.addAll(jur) }

        nums = nums.subList(skipAmount, nums.size)

        val length = nums.size

        (0 until 100).forEach { step ->
            var i = length - 2

            while (i > -1) {
                nums[i] += nums[i+1]
                nums[i] %= 10
                i--
            }
        }

        return nums.take(8).joinToString("").toInt()
    }
}

