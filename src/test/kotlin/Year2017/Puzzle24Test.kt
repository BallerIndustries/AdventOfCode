package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle24Test {
    val puzzle = Puzzle24()
    val puzzleText = this::class.java.getResource("/2017/puzzle24.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1511, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1471, result)
    }
}

class Puzzle24 {
    data class Component(val id: Int, val leftPinCount: Int, val rightPinCount: Int, val wasFlipped: Boolean = false) {
        fun flip() = this.copy(leftPinCount = rightPinCount, rightPinCount =  leftPinCount, wasFlipped = true)
        override fun toString() = "$leftPinCount/$rightPinCount"
    }

    data class Bridge(val usedComponents: List<Component>, val remainingComponents: List<Component>) {
        override fun toString() = usedComponents.joinToString("--")
        fun strength() = usedComponents.sumBy { it.leftPinCount + it.rightPinCount }

        fun addComponent(componentToAdd: Component): Bridge {
            if (!remainingComponents.any { it.id == componentToAdd.id }) throw RuntimeException("Hey!")

            val usedComponents = this.usedComponents + componentToAdd
            val remainingComponents = this.remainingComponents.filter { it.id != componentToAdd.id }
            return Bridge(usedComponents, remainingComponents)
        }
    }

    fun solveOne(puzzleText: String): Int {
        val components = parseComponents(puzzleText)
        var previousBridges = getInitialBridges(components)
        var currentBridges = iterateBridges(previousBridges)
        val maxStrength = mutableListOf<Int>()

        while (currentBridges != previousBridges) {
            previousBridges = currentBridges
            currentBridges = iterateBridges(previousBridges)
            maxStrength.add(currentBridges.maxBy { it.strength() }?.strength() ?: 0)
        }

        return maxStrength.max()!!
    }

    private fun iterateBridges(previousBridges: Set<Bridge>): Set<Bridge> {
        return previousBridges.flatMap { bridge ->
            val (usedComponents, remainingComponents) = bridge
            val pinNeededForThisFuckingBridge = usedComponents.last().rightPinCount
            val componentsThatMatch = remainingComponents.filter { it.leftPinCount == pinNeededForThisFuckingBridge }
            val flippedComponentsThatMatch = remainingComponents.map { it.flip() }.filter { it.leftPinCount == pinNeededForThisFuckingBridge }
            val matches = componentsThatMatch + flippedComponentsThatMatch

            val octopus= matches.map { matchingComponent -> bridge.addComponent(matchingComponent) }

            if (octopus.isEmpty()) listOf(bridge) else octopus
        }.toSet()
    }

    private fun parseComponents(puzzleText: String): List<Component> {
        val components = puzzleText.split("\n").mapIndexed { index, line ->
            val (left, right) = line.split("/").map { it.toInt() }
            Component(index, left, right)
        }
        return components
    }

    private fun getInitialBridges(components: List<Component>): Set<Bridge> {
        val initialPinCount = 0
        val componentsThatMatch = components.filter { it.leftPinCount == initialPinCount }
        val flippedComponentsThatMatch = components.map { it.flip() }.filter { it.leftPinCount == initialPinCount }

        val matchos = componentsThatMatch + flippedComponentsThatMatch

        val initialBridges = matchos.map { matchingComponent ->
            val otherJerks = components.filter { it.id != matchingComponent.id }
            Bridge(listOf(matchingComponent), otherJerks)
        }
        return initialBridges.toSet()
    }

    fun solveTwo(puzzleText: String): Int {
        val components = parseComponents(puzzleText)
        var previousBridges = getInitialBridges(components)
        var currentBridges = iterateBridges(previousBridges)
        val maxStrength = mutableListOf<Pair<Int, Int>>()

        while (currentBridges != previousBridges) {
            previousBridges = currentBridges
            currentBridges = iterateBridges(previousBridges)

            val longestLength = currentBridges.maxBy { it.usedComponents.size }?.usedComponents?.size ?: 0
            val longestBridges = currentBridges.filter { it.usedComponents.size == longestLength }
            val strongestOfTheLongestBridges = longestBridges.map { it.strength() }.max() ?: 0

            maxStrength.add(longestLength to strongestOfTheLongestBridges)
        }

        return maxStrength.maxBy { it.first }!!.second
    }
}