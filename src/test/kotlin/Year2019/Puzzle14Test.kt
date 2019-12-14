package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

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
}

class Puzzle14 {

    data class NameAndAmount(val name: String, val amount: Int, val oreAmount: Int? = null) {
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

        fun onlyHasOneIngredientNamed(name: String): Boolean {
            return this.ingredients.size == 1 && this.ingredients[0].name == name
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
    }

    fun solveOneNextGen(puzzleText: String): String {
        val recipes = parseRecipes(puzzleText).toMutableList()
        val oreOnlyRecipes = recipes.filter { it.onlyHasOneIngredientNamed("ORE") }

        // Set the
        val dogs = oreOnlyRecipes.flatMap { recipeToCreateMineral ->
            val mineral = recipeToCreateMineral.mineral.name
            val recipesThatUseMineralAsIngredient = recipes.filter { it.containsIngredient(mineral) }

            recipesThatUseMineralAsIngredient.map { recipeThatUsesMineral ->
                val ingredientInRecipe = recipeThatUsesMineral.getIngredient(recipeToCreateMineral.mineral.name)
                val multiple = Math.ceil(ingredientInRecipe.amount.toDouble() / recipeToCreateMineral.mineral.amount.toDouble()).toInt()
                recipeThatUsesMineral.setOreAmount(ingredientInRecipe.name, ingredientInRecipe.amount * multiple)
            }
        }

        // Merge the updated recipes back into the recipe list.
        dogs.forEach { updatedRecipe ->
            val index = recipes.indexOfFirst { it.recipeId == updatedRecipe.recipeId }
            if (index == -1) throw RuntimeException()
            recipes[index] = updatedRecipe
        }



        val frontier = dogs.flatMap {
            it.ingredients.filter {
                it.oreAmount == null
            }
        }








        throw RuntimeException()
    }

    fun solveOne(puzzleText: String): String {
        val recipes = parseRecipes(puzzleText)

        // Figure out the ORE cost for every mineral starting with zaza do do
        // Need to figure out the minimum path for minerals that have multiple recipes
        val fuelRecipe = recipes.find { it.mineral.name == "FUEL" }!!

        val mineralToOreAmount = recipes
            .filter { it.ingredients.size == 1 && it.ingredients.first().name == "ORE" }
            .associate { it.mineral to it.ingredients.first().amount }
            .toMutableMap()

        val mineralsToDetermineOreCostFor = mutableListOf<String>()

        while (true) {
            val resolvedStuff = mineralToOreAmount.entries.flatMap { (mineral, amount) ->
                val soloNexties = recipes.filter { it.ingredients.size == 1 && it.ingredients.first().name == mineral.name }
                val groupedNexties = recipes.filter { it.ingredients.size > 1 && it.ingredients.any { it.name == mineral.name } }

                soloNexties.map { recipe: Recipe ->
                    val multipliedAmount = recipe.ingredients[0].amount * amount
                    recipe.mineral to multipliedAmount
                }
            }.toMap()

            if (resolvedStuff.isEmpty()) {
                break
            }

            mineralToOreAmount.putAll(resolvedStuff)
        }



        println("oaisjd")


//        (0 until 1_000_000).forEach {
//            walkRandomPath(recipes, "ORE", "FUEL")
//        }


        throw NotImplementedError()
    }

    private fun parseRecipes(puzzleText: String): List<Recipe> {
        val recipes = puzzleText.split("\n").mapIndexed { index, line ->
            val (fromText, toText) = line.split(" => ")
            val from = fromText.split(", ").map { it.split(" ").let { NameAndAmount(it[1], it[0].toInt()) } }
            val to = toText.split(" ").let { NameAndAmount(it[1], it[0].toInt()) }
            Recipe(index, from, to)
        }
        return recipes
    }

    private fun getPath(recipes: List<Recipe>, from: String, to: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val paths = mutableSetOf<List<String>>()

    private fun walkRandomPath(recipes: List<Recipe>, from: String, to: String) {
        var currentMineral = from
        val path = mutableListOf<String>()

        while (currentMineral != to) {
            path.add(currentMineral)
            val possiblePaths = recipes.filter { it.ingredients.any { it.name == currentMineral } }

            if (possiblePaths.isEmpty()) {
                return
            }

            // Take a random path
            currentMineral = possiblePaths.random().mineral.name
        }

        path.add(currentMineral)
        paths.add(path)
//        println("Fucking did it!")
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

