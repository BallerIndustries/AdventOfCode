package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {
    val puzzle = Puzzle19()
    val puzzleText = this::class.java.getResource("/2015/puzzle19.txt").readText().replace("\r", "")
    val exampleText = """
        H => HO
        H => OH
        O => HH

        HOH
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(4, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        // 129 too low
        // 627 too high
        assertEquals(509, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(195, result)
    }

    @Test
    fun `replace 0-1 with AAA`() {
        val result = puzzle.replaceRange("HOH", IntRange(0, 0), "HO")
        assertEquals("HOOH", result)
    }
}

class Puzzle19 {
    data class Transition(val from: String, val to: String)

    fun parseInput(puzzleText: String): Pair<List<Transition>, String> {
        val (transitionText , startingMolecule) = puzzleText.split("\n\n")

        val transitions = transitionText.split("\n").map { line ->
            val (from, to) = line.split(" => ")
            Transition(from, to)
        }

        return transitions to startingMolecule
    }

    fun solveOne(puzzleText: String): Int {
        val (transitions , startingMolecule) = parseInput(puzzleText)

        val octopus = transitions.flatMap { transition ->
            val regex = Regex(transition.from)
            regex.findAll(startingMolecule).map { jur: MatchResult ->

                val dog = replaceRange(startingMolecule, jur.range, transition.to)
                dog
            }.toList()
        }.toSet()

        return octopus.size
    }

    fun replaceRange(startingMolecule: String, range: IntRange, to: String): String {
        val before = startingMolecule.substring(0, range.first)
        val after = startingMolecule.substring(range.last + 1)

        return before + to + after
    }

    fun solveTwo(puzzleText: String): Int {
        val (transitions, targetMolecule) = parseInput(puzzleText)
        val spaghetti = (0.. 1000).map { randomlyDecreateMolecule(targetMolecule, transitions) }
        return spaghetti.filter { it.first == "e" }.map { it.second }.min()!!
    }

    private fun randomlyDecreateMolecule(targetMolecule: String, transitions: List<Transition>): Pair<String, Int> {
        var currentMolecule = targetMolecule
        var steps = 0

        val mirroredTransitions = transitions.map { Transition(it.to, it.from) }

        while (currentMolecule != "e") {
            val possibleTransitions = mirroredTransitions.filter { transition -> currentMolecule.contains(transition.from) }

            if (possibleTransitions.isEmpty()) {
                return "" to Int.MAX_VALUE
            }

            val randomTransition = possibleTransitions.random()
            currentMolecule = tryReplaceOneElement(currentMolecule, randomTransition)
//            println(currentMolecule)
            steps++
        }

        return currentMolecule to steps
    }

    private fun randomlyCreateMolecule(targetMolecule: String, transitions: List<Transition>): Pair<String, Int> {
        var currentMolecule = "e"
        var steps = 0

        while (currentMolecule.length < targetMolecule.length) {
            val randomTransition = transitions.random()

            currentMolecule = tryReplaceOneElement(currentMolecule, randomTransition)
            steps++
        }

        return currentMolecule to steps
    }

    private fun tryReplaceOneElement(currentMolecule: String, randomTransition: Transition): String {
        if (!currentMolecule.contains(randomTransition.from)) {
            return currentMolecule
        }

        val regex = Regex(randomTransition.from)
        val randomMatch = regex.findAll(currentMolecule).toList().random()
        return replaceRange(currentMolecule, randomMatch.range, randomTransition.to)
    }
}