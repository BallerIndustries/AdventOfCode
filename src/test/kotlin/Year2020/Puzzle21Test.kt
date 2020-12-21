package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle21Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle21.txt").readText().replace("\r", "")
    val puzzle = Puzzle21()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2078, result)
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

        val result = puzzle.findAllergicIngredients(puzzleText)
        assertEquals(setOf("kfcds", "nhms", "sbzzf", "trh"), result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n" +
                "trh fvjkl sbzzf mxmxvkd (contains dairy)\n" +
                "sqjhc fvjkl (contains soy)\n" +
                "sqjhc mxmxvkd sbzzf (contains fish)"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("mxmxvkd,sqjhc,fvjkl", result)
    }
}

class Puzzle21 {
    data class Food(val foodId: Int, val ingredients: List<String>, val allergens: List<String>)

    fun solveOne(puzzleText: String): Int {
        val allergenIngredients = findAllergicIngredients(puzzleText)

        val foods = parseFoods(puzzleText)

        return foods.flatMap { it.ingredients }.count { ingredient ->
            ingredient !in allergenIngredients
        }



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

    fun solveTwo(puzzleText: String): String {
        val aaa: Map<String, Set<String>> = findAllergicIngredients2(puzzleText)


        val bbb: String = aaa.entries.map { it.key to it.value.first() }
            .sortedBy { it.first }
            .map { it.second }
            .joinToString(",")


        return bbb;
    }

    fun findAllergicIngredients(puzzleText: String): Set<String> {
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

        shakeTheTree(allergenToPossibleIngredient)

        println(allergenToPossibleIngredient)

        // Think I need to do multiple passes
        val resolvedAllergens = allergenToPossibleIngredient.entries.filter { it.value.size == 1 }.map { it.key }

        for (food in foods) {
            val unresolvedAllergens = food.allergens - resolvedAllergens

            if (unresolvedAllergens.size == 1) {
                val allergen = unresolvedAllergens.first()
                val foodsWithThisAllergen = foods.filter { allergen in it.allergens && it.foodId != food.foodId }

                for (ingredient in food.ingredients) {
                    if (foodsWithThisAllergen.all { ingredient in it.ingredients }) {
                        allergenToPossibleIngredient[allergen]!!.add(ingredient)
                    }
                }
            }
        }

        println("Hi Sandeep")

        shakeTheTree(allergenToPossibleIngredient)

        println("Hi Angus")

        return allergenToPossibleIngredient.flatMap { it.value }.toSet()
    }


    fun findAllergicIngredients2(puzzleText: String): Map<String, MutableSet<String>> {
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

        shakeTheTree(allergenToPossibleIngredient)

        println(allergenToPossibleIngredient)

        // Think I need to do multiple passes
        val resolvedAllergens = allergenToPossibleIngredient.entries.filter { it.value.size == 1 }.map { it.key }

        for (food in foods) {
            val unresolvedAllergens = food.allergens - resolvedAllergens

            if (unresolvedAllergens.size == 1) {
                val allergen = unresolvedAllergens.first()
                val foodsWithThisAllergen = foods.filter { allergen in it.allergens && it.foodId != food.foodId }

                for (ingredient in food.ingredients) {
                    if (foodsWithThisAllergen.all { ingredient in it.ingredients }) {
                        allergenToPossibleIngredient[allergen]!!.add(ingredient)
                    }
                }
            }
        }

        println("Hi Sandeep")

        shakeTheTree(allergenToPossibleIngredient)

        println("Hi Angus")

        return allergenToPossibleIngredient
    }

    private fun shakeTheTree(allergenToPossibleIngredient: Map<String, MutableSet<String>>) {
        var removalHappened = true

        while (removalHappened) {
            removalHappened = false

            allergenToPossibleIngredient.forEach { (allergen, possibleMatches) ->
                if (possibleMatches.size == 1) {
                    val resolvedIngredient = possibleMatches.first()

                    // Remove it from all the other entries
                    allergenToPossibleIngredient.forEach { (key, value) ->

                        // Don't want to remove it from this entry
                        if (key != allergen) {


                            if (value.remove(resolvedIngredient)) {
                                removalHappened = true
                            }
                        }
                    }
                }
            }
        }
    }
}

