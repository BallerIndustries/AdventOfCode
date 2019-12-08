package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle8Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle8.txt").readText().replace("\r", "")
    val puzzle = Puzzle8()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2480, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        val expected = """
#### #   ####  #    #  # 
   # #   ##  # #    #  # 
  #   # # ###  #    #### 
 #     #  #  # #    #  # 
#      #  #  # #    #  # 
####   #  ###  #### #  # """.trimIndent()

        assertEquals(expected, result)
    }

    @Test
    fun `example a`() {
        val dog = "003456789012"
        val result = puzzle.solveOne(dog, 3, 2)
        assertEquals(1, result)
    }
}

class Puzzle8 {
    enum class Pixel(val value: Char, val output: Char) { BLACK('0', ' '), WHITE('1', '#'), TRANSPARENT('2', ' ') }

    data class Point(val x: Int, val y: Int)

    fun solveOne(puzzleText: String, width: Int = 25, height: Int = 6): Int {
        val layerSize = width * height
        val layerWithLeastZeros = puzzleText.chunked(layerSize).minBy { layer -> layer.count { char -> char == '0' } }!!
        return layerWithLeastZeros.count { it == '1' } * layerWithLeastZeros.count { it == '2'}
    }

    fun solveTwo(puzzleText: String, width: Int = 25, height: Int = 6): String {
        val layerSize = width * height

        val layers = puzzleText.chunked(layerSize).map { layer: String ->
            layer.mapIndexed { index, char ->
                val x = index % width
                val y = index / width
                val point = Point(x, y)
                val pixel = Pixel.values().find { it.value == char }!!
                point to pixel
            }.toMap()
        }

        val picture = (0 until height).map { y ->
            (0 until width).map { x ->
                val point = Point(x, y)
                getFirstNonTransparentPixel(layers, point)
            }.joinToString("")
        }.joinToString("\n")

        println(picture)
        return picture
    }

    private fun getFirstNonTransparentPixel(layers: List<Map<Point, Pixel>>, point: Point): Char {
        layers.forEach { layer ->
            if (layer[point] != Pixel.TRANSPARENT) {
                return layer[point]!!.output
            }
        }

        return layers.last()[point]!!.output
    }
}