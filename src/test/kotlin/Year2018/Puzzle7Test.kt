package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle7Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle7.txt").readText().replace("\r", "")
    val puzzle = Puzzle7()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("GKRVWBESYAMZDPTIUCFXQJLHNO", result)
    }

    @Test
    fun `puzzle part b`() {
        // Not 912
        // Not 913
        // Not 919
        // Not 930
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(903, result)
    }
}

class Puzzle7 {
    data class Row(val name: String, val dependsOn: String)

    fun solveOne(puzzleText: String): String {
        val rows = puzzleText.split("\n").map { line ->
            val tmp = line.split(" ")
            Row(tmp[7], tmp[1])
        }

        val stepToDependencies = mutableMapOf<String, MutableSet<String>>()

        rows.forEach { row ->
            val dependencies: MutableSet<String> = stepToDependencies[row.name] ?: mutableSetOf()
            dependencies.add(row.dependsOn)
            stepToDependencies[row.name] = dependencies
        }

        // Find steps that has no dependencies
        val firstSteps = rows.filter { !stepToDependencies.containsKey(it.dependsOn) }
                .map { it.dependsOn }
                .distinct()

        firstSteps.forEach { stepName -> stepToDependencies[stepName] = mutableSetOf() }
        return findOrder(stepToDependencies)
    }

    fun findOrder(stepToDependencies: MutableMap<String, MutableSet<String>>): String {
        val stepsCompleted = mutableListOf<String>()
        val stepToDo = stepToDependencies
                .filter { it.value.isEmpty() }
                .map { it.key }
                .sorted().first()

        // Now remove the completed step from out dependencies map
        stepsCompleted.add(stepToDo)
        removeStep(stepToDependencies, stepToDo)

        while (stepToDependencies.isNotEmpty()) {
            // Figure out what steps can be done.
            val stepNamesWeCanDo = stepToDependencies
                .filter { it.value.isEmpty() }
                .map { it.key }

            val stepWeWillDo = stepNamesWeCanDo.sorted().first()

            // Add step to our stepsCompletedLIst
            stepsCompleted.add(stepWeWillDo)

            // Remove step from our state
            removeStep(stepToDependencies, stepWeWillDo)
        }

        return stepsCompleted.joinToString("")
    }

    private fun removeStep(stepToDependencies: MutableMap<String, MutableSet<String>>, stepName: String) {
        if (stepToDependencies.containsKey(stepName)) stepToDependencies.remove(stepName)

        stepToDependencies.forEach { entry ->
            if (entry.value.contains(stepName)) {
                entry.value.remove(stepName)
            }
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val rows = puzzleText.split("\n").map { line ->
            val tmp = line.split(" ")
            Row(tmp[7], tmp[1])
        }

        val stepToDependencies = mutableMapOf<String, MutableSet<String>>()

        rows.forEach { row ->
            val dependencies: MutableSet<String> = stepToDependencies[row.name] ?: mutableSetOf()
            dependencies.add(row.dependsOn)
            stepToDependencies[row.name] = dependencies
        }

        // Find steps that has no dependencies
        val firstSteps = rows.filter { !stepToDependencies.containsKey(it.dependsOn) }
                .map { it.dependsOn }
                .distinct()

        firstSteps.forEach { stepName -> stepToDependencies[stepName] = mutableSetOf() }
        return findOrderMulti(5, stepToDependencies)
    }

    data class JurState(val second: Int, val workers: MutableList<String?>,
                        val stuffDone: List<String>, val shitThatFucksOff: MutableList<String>)

    fun findOrderMulti(numWorkers: Int, stepToDependencies: MutableMap<String, MutableSet<String>>): Int {
        val workerState = (0 until 1000).map {
            JurState(it, (1 .. numWorkers).map { null }.toMutableList(), emptyList(), mutableListOf())
        }.toMutableList()

        // For each second figure out what we can do
        (0 until 1000).forEach { second ->
            val jurState: JurState = workerState[second]!!

            // Clear out shit that is supposed to fuck off
            jurState.shitThatFucksOff.forEach { stepNameToFuckOff ->
                removeStep(stepToDependencies, stepNameToFuckOff)
            }

            // Figure out which steps we should do
            val allStepsThatCanBeDone = stepToDependencies
                .filter { it.value.isEmpty() }
                .map { it.key }
                .sorted()

            val numberOfFreeWorkersThisSecond = jurState.workers.count { it == null }

            val stepsToDo = allStepsThatCanBeDone.subList(0,
                    Math.min(numberOfFreeWorkersThisSecond, allStepsThatCanBeDone.size))


            stepsToDo.forEach { stepName ->
                // Find the index of a free worker
                val indexOfFreeWorker = jurState.workers.indexOfFirst { it == null }
                val stepDuration = (stepName[0].toInt() - 64) + 60

                // Mark this workers 'calendar' as busy
                markCalendarAsBusy(
                        workerState, indexOfFreeWorker, jurState.second,
                        jurState.second + stepDuration, stepName)
            }

            // Remove stepsToDO from thestepWhatever
            stepsToDo.forEach { stepToDependencies.remove(it) }
        }

        return workerState.count { it.workers.any { it != null } }
    }

    fun markCalendarAsBusy(workerState: MutableList<JurState>, indexOfFreeWorker: Int, fromSecond: Int,
                           toSecond: Int, stepName: String) {
        (fromSecond until toSecond).forEach { second ->
            workerState[second].workers[indexOfFreeWorker] = stepName
        }

        // Add Mark in when this shit will fuck off
        workerState[toSecond].shitThatFucksOff.add(stepName)
    }
}
