package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle14.txt").readText().replace("\r", "")
    val puzzle = Puzzle14()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOneNextGen(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `example a`() {
        val text = """
            10 ORE => 10 A
            1 ORE => 1 B
            7 A, 1 B => 1 C
            7 A, 1 C => 1 D
            7 A, 1 D => 1 E
            7 A, 1 E => 1 FUEL
        """.trimIndent()

        val result = puzzle.solveOneNextGen(text)
        assertEquals(31, result)

    }
}

class Puzzle14 {

    data class NameAndAmount(val name: String, val amount: Int, val oreAmount: Int? = null) {

        companion object {
            fun parse(text: String): NameAndAmount {
                val split = text.split(" ")
                val name = split[1]
                val amount = split[0].toInt()
                return if (name == "ORE") NameAndAmount(name, amount, oreAmount = amount) else NameAndAmount(name, amount)
            }
        }

        override fun toString(): String {
            return if (oreAmount == null) "$amount $name" else "$oreAmount ORE"
        }
    }


    data class Recipe(val recipeId: Int, val ingredients: List<NameAndAmount>, val mineral: NameAndAmount) {

        fun getIngredient(name: String): NameAndAmount {
            return ingredients.find { it.name == name } ?: throw RuntimeException()
        }

        fun containsIngredient(name: String): Boolean {
            return this.ingredients.any { it.name == name }
        }

        fun allOreAmountsAreKnown(): Boolean {
            return this.ingredients.all { it.oreAmount != null }
        }

        fun oreRequired(): Int? {
            if (!allOreAmountsAreKnown()) {
                return null
            }

            return ingredients.sumBy { it.oreAmount!! }
        }

        fun setOreAmount(name: String, amount: Int): Recipe {
            val ingredientsWithOreAmount = this.ingredients.map {
                if (it.name == name) {
                    it.copy(oreAmount = amount)
                }
                else {
                    it
                }
            }

            return this.copy(ingredients = ingredientsWithOreAmount)
        }

        override fun toString(): String {
            return "${ingredients.joinToString(", ")} => $mineral"
        }

        fun mergeInTheOre(updatedRecipe: Recipe): Recipe {
            val oreIngredients = updatedRecipe.ingredients.filter { it.oreAmount != null }
            var current = this

            oreIngredients.forEach {
                current = current.setOreAmount(it.name, it.oreAmount!!)
            }

            return current
        }
    }

    fun solveOneNextNextGen(puzzleText: String): Int? {
        throw java.lang.RuntimeException()
    }

    fun solveOneNextGen(puzzleText: String): Int? {
        val recipes = parseRecipes(puzzleText).toMutableList()
        val surplus = mutableMapOf<String, Int>()

        // while we have unknown ore amounts
        while (!recipes.all { it.allOreAmountsAreKnown() }) {
            val understoodRecipes = recipes.filter { it.allOreAmountsAreKnown() }

            val changedRecipes = understoodRecipes.flatMap { recipeToCreateMineral ->
                val mineral = recipeToCreateMineral.mineral.name
                val recipesThatUseMineralAsIngredient = recipes.filter { it.containsIngredient(mineral) }

                recipesThatUseMineralAsIngredient.map { recipeThatUsesMineral ->
                    val ingredientInRecipe = recipeThatUsesMineral.getIngredient(recipeToCreateMineral.mineral.name)
                    val ingredientAmount = ingredientInRecipe.amount
                    val multiple = Math.ceil(ingredientAmount.toDouble() / recipeToCreateMineral.oreRequired()!!.toDouble()).toInt()
                    val amount = recipeToCreateMineral.oreRequired()!! * multiple
                    val surplus = amount - ingredientAmount

                    recipeThatUsesMineral.setOreAmount(ingredientInRecipe.name, amount)
                }
            }

            mergeUpdatesIntoRecipeList(changedRecipes, recipes)
        }

        val fuelRecipe = recipes.find { it.mineral.name == "FUEL" } ?: throw RuntimeException()


        fuelRecipe.ingredients.forEach { ingredient: NameAndAmount ->






        }


        return fuelRecipe.oreRequired()
    }

    private fun mergeUpdatesIntoRecipeList(dogs: List<Recipe>, recipes: MutableList<Recipe>) {
        dogs.forEach { updatedRecipe ->
            val index = recipes.indexOfFirst { it.recipeId == updatedRecipe.recipeId }
            if (index == -1) throw RuntimeException()
            recipes[index] = recipes[index].mergeInTheOre(updatedRecipe)
        }
    }

    private fun parseRecipes(puzzleText: String): List<Recipe> {
        val recipes = puzzleText.split("\n").mapIndexed { index, line ->
            val (fromText, toText) = line.split(" => ")
            val from = fromText.split(", ").map(NameAndAmount.Companion::parse)
            val to = NameAndAmount.parse(toText)
            Recipe(index, from, to)
        }
        return recipes
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

