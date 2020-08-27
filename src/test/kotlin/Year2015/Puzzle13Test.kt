package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {
    val puzzle = Puzzle13()
    val puzzleText = this::class.java.getResource("/2015/puzzle13.txt").readText().replace("\r", "")
    val exampleText = "1"

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(733, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(725, result)
    }
}

class Puzzle13 {

    data class Record(val guestName: String, val neighborName: String, val happinessPoints: Int)

    data class TableInProgress(val seatedGuests: List<String>, val unseatedGuests: List<String>) {
        fun everyoneIsSeated() = unseatedGuests.isEmpty()

        fun addGuest(guestName: String): TableInProgress {
            if (!unseatedGuests.contains(guestName)) throw RuntimeException("FUCK A DUCK YOU FUCKERE!@!@#!@")

            val seatedGuests = this.seatedGuests + guestName
            val unseatedGuests = this.unseatedGuests.filter { it != guestName }
            return TableInProgress(seatedGuests, unseatedGuests)
        }

        fun happinessScore(records: List<Record>): Int {
            return seatedGuests.mapIndexed { index, guestName ->
                val leftGuest = if (index == 0) seatedGuests.last() else seatedGuests[index - 1]
                val rightGuest = if (index == seatedGuests.lastIndex) seatedGuests.first() else seatedGuests[index + 1]

                val leftScore = records.find { it.guestName == guestName && it.neighborName == leftGuest }!!.happinessPoints
                val rightScore = records.find { it.guestName == guestName && it.neighborName == rightGuest }!!.happinessPoints

                leftScore + rightScore
            }.sum()
        }
    }

    fun solveOne(puzzleText: String): Int {
        val records = parseRecords(puzzleText)
        return getScoreForHappiestTableArrangement(records)
    }

    private fun getScoreForHappiestTableArrangement(records: List<Record>): Int {
        val guestNames = records.map { it.guestName }.distinct()

        var tableSettings = guestNames.map { guestName ->
            TableInProgress(listOf(guestName), guestNames.filter { it != guestName })
        }

        while (!tableSettings.any { it.everyoneIsSeated() }) {
            tableSettings = tableSettings.flatMap { tableInProgress ->

                tableInProgress.unseatedGuests.map { guestToAdd ->
                    tableInProgress.addGuest(guestToAdd)
                }
            }
        }

        return tableSettings.map { it.happinessScore(records) }.max()!!
    }

    private fun parseRecords(puzzleText: String): List<Record> {
        return puzzleText.split("\n").map { line ->
            val tmp = line.split(" ")
            val guestName = tmp[0]
            val neighborName = tmp.last().replace(".", "")
            val happinessPoints = if (tmp[2] == "gain") tmp[3].toInt() else tmp[3].toInt() * -1

            Record(guestName, neighborName, happinessPoints)
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val records = parseRecords(puzzleText)
        val guestNames = records.map { it.guestName }.distinct()
        val recordsWithYou = records + guestNames.flatMap { listOf(Record(it, "Angus", 0), Record("Angus", it, 0)) }

        return getScoreForHappiestTableArrangement(recordsWithYou)
    }
}