package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle7Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle7.txt").readText().replace("\r", "")
    val puzzle = Puzzle7()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(287, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(48160, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "shiny gold bags contain 2 dark red bags.\n" +
                "dark red bags contain 2 dark orange bags.\n" +
                "dark orange bags contain 2 dark yellow bags.\n" +
                "dark yellow bags contain 2 dark green bags.\n" +
                "dark green bags contain 2 dark blue bags.\n" +
                "dark blue bags contain 2 dark violet bags.\n" +
                "dark violet bags contain no other bags."
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(126, result)
    }
}

class Puzzle7 {
    data class BagTypeAndQuantity(val bagType: String, val quantity: Int)

    fun solveOne(puzzleText: String): Int {
        val rules: Map<String, List<BagTypeAndQuantity>> = puzzleText.split("\n").associate { line ->
            val tmp = line.split(" ")
            val bagType = "${tmp[0]} ${tmp[1]}"

            val horse = line.split(" contain ")[1].split(", ")
                .map { rule -> bagTypeAndQuantity(rule) }

            bagType to horse
        }

        val initialBagType = "shiny gold"
        val frontier = mutableListOf<String>(initialBagType)
        val visited = mutableSetOf<String>()

        while (frontier.isNotEmpty()) {
            val currentBagType = frontier.removeAt(0)
            visited.add(currentBagType)

            val matches = rules.filter { rule ->
                rule.value.any { it.bagType == currentBagType }
            }.map {
                it.key
            }.filter { it !in visited }.toSet()

            frontier.addAll(matches)
            println()
        }

        return visited.size - 1
    }

    private fun bagTypeAndQuantity(rule: String): BagTypeAndQuantity {
        val tmp = rule.split(" ")
        val quantity = if (tmp[0] == "no") 0 else tmp[0].toInt()
        //val quantity = if (tmp[0] == "no") 0 else tmp[0].toInt()
        val bagType = tmp[1] + " " + tmp[2]
        return BagTypeAndQuantity(bagType, quantity)
    }

    fun solveTwo(puzzleText: String): Int {
        val rules: Map<String, List<BagTypeAndQuantity>> = puzzleText.split("\n").associate { line ->
            val tmp = line.split(" ")
            val bagType = "${tmp[0]} ${tmp[1]}"

            val horse = line.split(" contain ")[1].split(", ")
                .map { rule -> bagTypeAndQuantity(rule) }

            if (horse.size == 1 && horse[0].quantity == 0) {
                bagType to listOf()
            }
            else {
                bagType to horse
            }
        }

        val initialBagType = "shiny gold"
        return bagsNeeded(rules, initialBagType) - 1
    }

    fun bagsNeeded(rules: Map<String, List<BagTypeAndQuantity>>, bagType: String): Int {
        val thisBagsRules: List<BagTypeAndQuantity> = rules[bagType]!!

        if (thisBagsRules.size == 0) {
            println("bagType = $bagType count = 1")
            return 1
        }

        val count = thisBagsRules.sumBy { rule -> rule.quantity * bagsNeeded(rules, rule.bagType) }
        println("bagType = $bagType count = $count")
        return count + 1
    }
}

