package Year2015

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle12Test {
    val puzzle = Puzzle12()
    val puzzleText = this::class.java.getResource("/2015/puzzle12.txt").readText().replace("\r", "")
    val exampleText = "1"

    @Test
    fun `puzzle part a`() {
        //214938 is too high
        //203051 is too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals(191164, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(87842, result)
    }
}

class Puzzle12 {
    fun solveOne(puzzleText: String): Int {
        val jsonObject = ObjectMapper().readTree(puzzleText)
        val toProcess = mutableListOf<JsonNode>(jsonObject)
        var sum = 0

        while (toProcess.isNotEmpty()) {
            val currentNode = toProcess.removeAt(0)

            if (currentNode.isObject || currentNode.isArray) {
                toProcess.addAll(currentNode.elements().asSequence())
            }
            else if (currentNode.isNumber) {
                sum += currentNode.asInt()
            }
        }

        return sum
    }

    fun solveTwo(puzzleText: String): Int {
        val jsonObject = ObjectMapper().readTree(puzzleText)
        val toProcess = mutableListOf<JsonNode>(jsonObject)
        var sum = 0

        while (toProcess.isNotEmpty()) {
            val currentNode = toProcess.removeAt(0)

            if (currentNode.isObject) {
                val objectNode = currentNode as ObjectNode
                val containsRed = objectNode.elements().asSequence().any { jsonNode -> jsonNode.isTextual && jsonNode.asText() == "red" }

                if (!containsRed) {
                    toProcess.addAll(objectNode.elements().asSequence())
                }
            }
            else if (currentNode.isArray) {
                toProcess.addAll(currentNode.elements().asSequence())
            }
            else if (currentNode.isNumber) {
                sum += currentNode.asInt()
            }
        }

        return sum
    }
}