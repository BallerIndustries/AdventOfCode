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
        assertEquals("lmcqt,kcddk,npxrdnd,cfb,ldkt,fqpt,jtfmtpd,tsch", result)
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
        val puzzleText = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n" +
                "trh fvjkl sbzzf mxmxvkd (contains dairy)\n" +
                "sqjhc fvjkl (contains soy)\n" +
                "sqjhc mxmxvkd sbzzf (contains fish)"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("mxmxvkd,sqjhc,fvjkl", result)
    }
}

class Puzzle21 {
    data class Food(val foodId: Int, val ingredients: Set<String>, val allergens: Set<String>)

    fun solveOne(puzzleText: String): Int {
        val allergenIngredients = findAllergicIngredients(puzzleText).flatMap { it.value }.toSet()

        return parseFoods(puzzleText).flatMap { it.ingredients }
            .count { ingredient -> ingredient !in allergenIngredients }
    }

    private fun parseFoods(puzzleText: String): List<Food> {
        return puzzleText.split("\n").mapIndexed { index, line ->
            val allergens = parseAllergens(line)
            val ingredients = line.split(" (")[0].split(" ").toSet()
            Food(index, ingredients, allergens)
        }
    }

    private fun parseAllergens(line: String): Set<String> {
        val tmp = line.split("(")

        return if (tmp.size == 2) {
            val cleanedText = tmp[1].replace("contains ", "").replace(")", "")
            cleanedText.split(", ").toSet()
        } else {
            setOf()
        }
    }

    fun solveTwo(puzzleText: String): String {
        return findAllergicIngredients(puzzleText).entries.map { it.key to it.value.first() }
            .sortedBy { it.first }
            .joinToString(",") { it.second }
    }

    fun findNonAllergicIngredients(puzzleText: String): Set<String> {
        val allergicIngredients = findAllergicIngredients(puzzleText).flatMap { it.value }
        val foods = parseFoods(puzzleText)
        return foods.flatMap { it.ingredients }.toSet() - allergicIngredients
    }

    fun findAllergicIngredients(puzzleText: String): Map<String, Set<String>> {
        val foods = parseFoods(puzzleText)
        val allergenToPossibleIngredient = foods.flatMap { it.allergens }
            .associateWith { mutableSetOf<String>() }

        var resolvedAllergens = setOf<String>()

        while (resolvedAllergens.size < allergenToPossibleIngredient.size) {
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

            shakeTheTree(allergenToPossibleIngredient)
            resolvedAllergens = allergenToPossibleIngredient.entries.filter { it.value.size == 1 }.map { it.key }.toSet()
        }

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

