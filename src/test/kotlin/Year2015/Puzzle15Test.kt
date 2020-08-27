package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {
    val puzzle = Puzzle15()
    val puzzleText = this::class.java.getResource("/2015/puzzle15.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(18965440, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(15862900, result)
    }
}

class Puzzle15 {
    data class Ingredient(val name: String, val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int)

    data class Cookie(val frostingCount: Int, val candyCount: Int, val butterscotchCount: Int, val sugarCount: Int) {

        fun score(ingredients: List<Ingredient>): Int {
            val frostingIngredient = ingredients.find { it.name == "Frosting" }!!
            val candyIngredient = ingredients.find { it.name == "Candy" }!!
            val butterscotchIngredient = ingredients.find { it.name == "Butterscotch" }!!
            val sugarIngredient = ingredients.find { it.name == "Sugar" }!!

            val capacityScore = (frostingIngredient.capacity * frostingCount) + (candyIngredient.capacity * candyCount) + (butterscotchIngredient.capacity * butterscotchCount) + (sugarIngredient.capacity * sugarCount)

            val durabilityScore = (frostingIngredient.durability * frostingCount) + (candyIngredient.durability * candyCount) + (butterscotchIngredient.durability * butterscotchCount) + (sugarIngredient.durability * sugarCount)

            val flavorScore = (frostingIngredient.flavor * frostingCount) + (candyIngredient.flavor * candyCount) + (butterscotchIngredient.flavor * butterscotchCount) + (sugarIngredient.flavor * sugarCount)

            val textureScore = (frostingIngredient.texture * frostingCount) + (candyIngredient.texture * candyCount) + (butterscotchIngredient.texture * butterscotchCount) + (sugarIngredient.texture * sugarCount)

            val scores = listOf(capacityScore, durabilityScore, flavorScore, textureScore)

            if (scores.any { it < 0 }) {
                return 0
            }

            return scores.reduce { acc, score ->  acc * score }
        }

        fun calories(ingredients: List<Ingredient>): Int {
            val frostingIngredient = ingredients.find { it.name == "Frosting" }!!
            val candyIngredient = ingredients.find { it.name == "Candy" }!!
            val butterscotchIngredient = ingredients.find { it.name == "Butterscotch" }!!
            val sugarIngredient = ingredients.find { it.name == "Sugar" }!!

            val calories = (frostingIngredient.calories * frostingCount) + (candyIngredient.calories * candyCount) + (butterscotchIngredient.calories * butterscotchCount) + (sugarIngredient.calories * sugarCount)
            return calories
        }
    }

    fun solveOne(puzzleText: String): Int {
        val ingredients = parseInput(puzzleText)
        val cookies = generateAllCookies()
        return cookies.map { it.score(ingredients) }.max()!!
    }

    private fun generateAllCookies(): MutableList<Cookie> {
        val cookies = mutableListOf<Cookie>()

        (0..100).forEach { frosting ->
            (0..100 - frosting).forEach { candy ->
                (0..100 - frosting - candy).forEach { butterscotch ->

                    val sugar = Math.max(100 - frosting - candy - butterscotch, 0)
                    cookies.add(Cookie(frosting, candy, butterscotch, sugar))
                }
            }
        }
        return cookies
    }

    private fun parseInput(puzzleText: String): List<Ingredient> {
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
        return ingredients
    }

    fun solveTwo(puzzleText: String): Int {
        val ingredients = parseInput(puzzleText)
        val cookies = generateAllCookies()

        return cookies.filter { it.calories(ingredients) == 500 }.map { it.score(ingredients) }.max()!!
    }
}