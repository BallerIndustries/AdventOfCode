package Year2017

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {
    val puzzle = Puzzle13()
    val puzzleText = this::class.java.getResource("/2017/puzzle13.txt").readText().replace("\r", "")

    val exampleText = """
        0: 3
        1: 2
        4: 4
        6: 4
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(24, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1928, result)
    }

    @Test
    fun `example part b`() {
        val result = puzzle.solveTwo(exampleText)
        assertEquals(10, result)
    }

    @Test
    fun `puzzle part b`() {
        // 146664 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3830344, result)
    }

    @Test
    fun `1 range layer test`() {
        val layer = Puzzle13.Layer(0, 1)

        assertEquals(0, layer.yPosAt(0))
        assertEquals(0, layer.yPosAt(1))
        assertEquals(0, layer.yPosAt(2))
        assertEquals(0, layer.yPosAt(3))
        assertEquals(0, layer.yPosAt(4))
        assertEquals(0, layer.yPosAt(35235))
    }

    @Test
    fun `2 range layer test`() {
        val layer = Puzzle13.Layer(0, 2)

        assertEquals(0, layer.yPosAt(0))
        assertEquals(1, layer.yPosAt(1))
        assertEquals(0, layer.yPosAt(2))
        assertEquals(1, layer.yPosAt(3))
        assertEquals(0, layer.yPosAt(4))
        assertEquals(1, layer.yPosAt(5))
    }

    @Test
    fun `3 range layer test`() {
        val layer = Puzzle13.Layer(0, 3)

        assertEquals(0, layer.yPosAt(0))
        assertEquals(1, layer.yPosAt(1))
        assertEquals(2, layer.yPosAt(2))
        assertEquals(1, layer.yPosAt(3))
        assertEquals(0, layer.yPosAt(4))
        assertEquals(1, layer.yPosAt(5))
        assertEquals(2, layer.yPosAt(6))
        assertEquals(1, layer.yPosAt(7))
        assertEquals(0, layer.yPosAt(8))
        assertEquals(1, layer.yPosAt(9))
    }

    @Test
    fun `4 range layer test`() {
        val layer = Puzzle13.Layer(0, 4)

        assertEquals(0, layer.yPosAt(0))
        assertEquals(1, layer.yPosAt(1))
        assertEquals(2, layer.yPosAt(2))
        assertEquals(3, layer.yPosAt(3))
        assertEquals(2, layer.yPosAt(4))
        assertEquals(1, layer.yPosAt(5))
        assertEquals(0, layer.yPosAt(6))
        assertEquals(1, layer.yPosAt(7))
        assertEquals(2, layer.yPosAt(8))
        assertEquals(3, layer.yPosAt(9))
    }
}

class Puzzle13 {

    data class Layer(val depth: Int, val range: Int) {

        // Range = 2
        // t = 0 y = 0
        // t = 1 y = 1
        // t = 2 y = 0

        // Range = 3
        // t = 0 y = 0
        // t = 1 y = 1
        // t = 2 y = 2
        // t = 3 y = 1
        // t = 4 y = 0

        // Range = 4
        // t = 0 y = 0
        // t = 1 y = 1
        // t = 2 y = 2
        // t = 3 y = 3
        // t = 4 y = 2
        // t = 5 y = 1
        // t = 6 y = 0




        fun yPosAt(time: Int): Int {
            if (range == 1) return 0

            val wrapAroundPoint = (range * 2) - 2
            val normalisedTime = time % wrapAroundPoint

            if (normalisedTime < range) {
                return normalisedTime
            }
            else {
                val delta = normalisedTime - range + 2
                return range - delta
            }
        }
    }

    fun solveOne(puzzleText: String): Int {
        val layers = createLayers(puzzleText)
        return simulateRun(layers, 0)
    }

    private fun createLayers(puzzleText: String): List<Layer> {
        val layers = puzzleText.split("\n").map {
            val tmp = it.split(": ").map { it.toInt() }
            Layer(tmp[0], tmp[1])
        }
        return layers
    }

    private fun simulateRun(layers: List<Layer>, waitFor: Int): Int {
        return layers.filter { layer -> layer.yPosAt(layer.depth + waitFor) == 0 }
            .sumBy { it.depth * it.range }
    }

    private fun didYouGetCaught(layers: List<Layer>, waitFor: Int): Boolean {
        return layers.any { layer -> layer.yPosAt(layer.depth + waitFor) == 0 }
    }

    fun solveTwo(puzzleText: String): Int {
        val layers = createLayers(puzzleText)

        for (waitFor in 0 .. Int.MAX_VALUE) {
            if (!didYouGetCaught(layers, waitFor)) {
                return waitFor
            }
        }

        throw RuntimeException("You can't ever do this!")
    }
}