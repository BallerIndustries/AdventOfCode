package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle21Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle21.txt").readText().replace("\r", "")
    val puzzle = Puzzle21()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(964875, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n" +
                "trh fvjkl sbzzf mxmxvkd (contains dairy)\n" +
                "sqjhc fvjkl (contains soy)\n" +
                "sqjhc mxmxvkd sbzzf (contains fish)"

        val result = puzzle.findNonAllergicIngredients(puzzleText)
        assertEquals(setOf("kfcds", "nhms", "sbzzf", "trh"), result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = ""
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(241861950, result)
    }
}

class Puzzle21 {
    data class Food(val foodId: Int, val ingredients: List<String>, val allergens: List<String>)

    fun solveOne(puzzleText: String): Int {
        val aaa = findNonAllergicIngredients(puzzleText)

        return 1
    }

    private fun parseFoods(puzzleText: String): List<Food> {
        val foods = puzzleText.split("\n").mapIndexed { index, line ->
            val allergens = parseAllergens(line)
            val ingredients = line.split(" (")[0].split(" ")
            Food(index, ingredients, allergens)
        }
        return foods
    }

    private fun parseAllergens(line: String): List<String> {
        val tmp = line.split("(")

        val allergens = if (tmp.size == 2) {
            val cleanedText = tmp[1].replace("contains ", "").replace(")", "")
            cleanedText.split(", ")
        } else {
            listOf()
        }
        return allergens
    }

    fun solveTwo(puzzleText: String): Int {
        return 1
    }

    fun findNonAllergicIngredients(puzzleText: String): Set<String> {
        val foods = parseFoods(puzzleText)
        val allergenToPossibleIngredient = foods.flatMap { it.allergens }
            .associateWith { mutableSetOf<String>() }

        for (food in foods) {
            if (food.allergens.size == 1) {
                val allergen = food.allergens.first()
                val foodsWithThisAllergen = foods.filter { allergen in it.allergens && it.foodId != food.foodId }

                for (ingredient in food.ingredients) {
                    if (foodsWithThisAllergen.all { ingredient in it.ingredients }) {
                        allergenToPossibleIngredient[allergen]!!.add(ingredient)
                    }
                }
            }
        }

        while (allergenToPossibleIngredient.values.any { it.size > 1 }) {
            allergenToPossibleIngredient.forEach { (allergen, possibleMatches) ->
                if (possibleMatches.size == 1) {
                    val resolvedIngredient = possibleMatches.first()

                    // Remove it from all the other entries
                    allergenToPossibleIngredient.forEach { (key, value) ->

                        // Don't want to remove it from this entry
                        if (key != allergen) {
                            value.remove(resolvedIngredient)
                        }
                    }
                }
            }
        }

        return setOf()
    }
}

