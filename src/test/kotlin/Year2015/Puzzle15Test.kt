package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle15Test {
    val puzzle = Puzzle15()
    val puzzleText = this::class.java.getResource("/2015/puzzle15.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2660, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1256, result)
    }
}

class Puzzle15 {
    data class Ingredient(val name: String, val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int)

    fun solveOne(puzzleText: String): Int {
        val ingredients = puzzleText.split("\n").map { line ->

            val tmp = line.split(" ")
            val name = tmp[0].replace(":", "")
            val capacity = tmp[2].replace(",", "").toInt()
            val durability = tmp[4].replace(",", "").toInt()
            val flavor = tmp[6].replace(",", "").toInt()
            val texture = tmp[8].replace(",", "").toInt()
            val calories = tmp[10].replace(",", "").toInt()

            Ingredient(name, capacity, durability, flavor, texture, calories)
        }


        println(ingredients)
        return 100
    }

    fun solveTwo(puzzleText: String): Int {
        return 400
    }
}