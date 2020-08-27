package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class Puzzle4Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle4.txt").readText().replace("\r", "")
    val puzzle = Puzzle4()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(95199, result.first)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(7887, result.second)
    }
}

data class Horse(val date: LocalDateTime, val text: String)

//data class MinuteFlag()

class Puzzle4 {
    fun solveOne(puzzleText: String): Pair<Int, Int> {
        val horses = puzzleText.split("\n").map { line ->
            val tmp = line.split(" ")
            val dateString = tmp[0].replace("[", "") + " " + tmp[1].replace("]", "")
            val date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

            Horse(date, line.split("] ")[1])
        }.sortedBy { it.date }

        // Create a dictionary from guard id to numberOfMinsAsleep

        val guardToTotal = mutableMapOf<String, Long>()
        val guardToMinute = mutableMapOf<String, MutableList<Int>>()
        var currentGuard = ""
        var fellAsleepAt = LocalDateTime.MIN
        val minutesToGuard: MutableMap<Int, MutableList<String>> = (0..59)
                .associate { it to mutableListOf<String>() }
                .toMutableMap()



        horses.forEach { horse ->
            if (horse.text.endsWith("begins shift")) {
                currentGuard = horse.text.split(" ")[1]
            }
            else if (horse.text.endsWith("falls asleep")) {
                fellAsleepAt = horse.date
            }
            else if (horse.text.endsWith("wakes up")) {
                val minutes = fellAsleepAt.until(horse.date, ChronoUnit.MINUTES)
                val totalMinutes = (guardToTotal[currentGuard] ?: 0) + minutes
                guardToTotal[currentGuard] = totalMinutes

                // Add in the minutes this guard is asleep
                val minuteFrom = fellAsleepAt.minute
                val minuteTo = horse.date.minute

                if (minuteFrom > minuteTo) throw java.lang.RuntimeException("minuteFrom is bigger than minuteTo")
                if (fellAsleepAt.hour != horse.date.hour) throw RuntimeException("at different hours")

                val minuteList: MutableList<Int> = guardToMinute[currentGuard] ?: mutableListOf()
                minuteList += (minuteFrom until minuteTo).map { it }

                guardToMinute[currentGuard] = minuteList

                // Add in minutesToGuards
                (minuteFrom until minuteTo).forEach { minute ->
                    minutesToGuard[minute] = (minutesToGuard[minute]!! + currentGuard).toMutableList()
                }
            }
            else {
                println("horse.text = ${horse.text}")
                throw RuntimeException("asjfoidjsfodsi ")
            }
        }

        // Get guardId that is asleep the most, group by the minutes
        val maxSleepingGuardId = guardToTotal.entries.maxBy { it.value }!!.key

        // NOw get the minutes he was asleep
        val dog = guardToMinute[maxSleepingGuardId]!!
                .groupBy { it }
                .maxBy { it.value.count() }


        val answerOne = maxSleepingGuardId.substring(1).toInt() * dog!!.key

        var maxMinute = -1
        var maxGuardId: String = ""
        var octopus = -1

        // Go through each minute, find out which guard has the most
        minutesToGuard.entries.forEach { entry ->

            val dog = entry.value.groupBy { it }
                    .maxBy { it.value.count() }

            println(dog)

            if (dog != null && dog.value.size > maxMinute) {
                maxMinute = dog.value.size
                maxGuardId = dog.key
                octopus = entry.key
            }
        }





        val answerTwo = octopus * maxGuardId.replace("#", "").toInt()

        return Pair(answerOne, answerTwo)
    }

    fun solveTwo(puzzleText: String): Int {
        return 2
    }
}
