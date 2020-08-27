package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {
    val puzzle = Puzzle16()
    val puzzleText = this::class.java.getResource("/2015/puzzle16.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(103, result)
    }

    @Test
    fun `puzzle part b`() {
        // 103 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(405, result)
    }
}

class Puzzle16 {

    data class Aunt(val name: String, val attributes: Map<String, Int>) {
        fun matchPercentage(giftSueAttributes: Map<String, Int>): Double {
            var matches = 0

            this.attributes.entries.forEach { (item, count) ->
                if (giftSueAttributes[item] == count) {
                    matches++
                }
            }

            return matches / this.attributes.count().toDouble()
        }

        fun matchPercentageTwo(giftSueAttributes: Map<String, Int>): Double {
            var matches = 0

            this.attributes.entries.forEach { (item, count) ->
                val detectedCount = giftSueAttributes[item] ?: throw RuntimeException("Angerrrr!!")

                val atLeastProperties = setOf("cats", "trees")
                val atMostProperties = setOf("pomeranians", "goldfish")
                val specialCaseProperties = atLeastProperties + atMostProperties

                if (atLeastProperties.contains(item) && count > detectedCount) {
                    matches++
                }
                else if (atMostProperties.contains(item) && count < detectedCount) {
                    matches++
                }
                else if (!specialCaseProperties.contains(item) && detectedCount == count) {
                    matches++
                }
            }

            return matches / this.attributes.count().toDouble()
        }
    }

    val giftSueAttributeText = """
        children: 3
        cats: 7
        samoyeds: 2
        pomeranians: 3
        akitas: 0
        vizslas: 0
        goldfish: 5
        trees: 3
        cars: 2
        perfumes: 1""".trimIndent()

    val giftSueAttributes = giftSueAttributeText.split("\n").associate { attribute ->
        val tmp = attribute.split(": ")
        tmp[0] to tmp[1].toInt()
    }

    fun solveOne(puzzleText: String): Int {
        val aunts = parseAunts(puzzleText)
        val matchingAunt = aunts.maxBy { it.matchPercentage(giftSueAttributes) }!!
        return matchingAunt.name.split(" ")[1].toInt()
    }

    private fun parseAunts(puzzleText: String): List<Aunt> {
        val aunts = puzzleText.split("\n").map { line ->

            val tmp = line.split(" ")
            val name = "Sue " + tmp[1].replace(":", "")
            val attributesText = tmp.subList(2, tmp.size).joinToString(" ")

            val attributes = attributesText.split(", ").associate { attribute ->
                val dog = attribute.split(": ")
                dog[0] to dog[1].toInt()
            }

            Aunt(name, attributes)
        }
        return aunts
    }

    fun solveTwo(puzzleText: String): Int {
        val aunts = parseAunts(puzzleText)
        val maxScore = aunts.map { it.matchPercentageTwo(giftSueAttributes) }.max()!!
        val maxScoringAunts = aunts.filter { it.matchPercentageTwo(giftSueAttributes) == maxScore }

        return maxScoringAunts.first().name.split(" ")[1].toInt()
    }
}