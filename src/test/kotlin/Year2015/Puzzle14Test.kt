package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle14Test {
    val puzzle = Puzzle14()
    val puzzleText = this::class.java.getResource("/2015/puzzle14.txt").readText().replace("\r", "")
    val exampleText = """
        Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText, 1000)
        assertEquals(1120, result)
    }

    @Test
    fun `example part b`() {
        val result = puzzle.solveTwo(exampleText, 1000)
        assertEquals(689, result)
    }

    @Test
    fun `puzzle part a`() {
        // 30400 is too high
        // 30375 is too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2660, result)
    }

    @Test
    fun `puzzle part b`() {
        //647 is too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(725, result)
    }
}

class Puzzle14 {
    data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int) {

        var distanceTravelled = 0
        var isFlying = true
        var remainingFlyTime = flyTime
        var remainingRestTime = restTime
        var points = 0

        fun update() {
            // Cover some ground if you are flying
            if (isFlying) {
                distanceTravelled += speed
            }

            // Run down the rest/fly timers
            if (isFlying) {
                remainingFlyTime--
            }
            else {
                remainingRestTime--
            }

            // Do we need to switch between flying/resting
            if (isFlying && remainingFlyTime == 0) {
                isFlying = false
                remainingRestTime = restTime
            }
            else if (!isFlying && remainingRestTime == 0) {
                isFlying = true
                remainingFlyTime = flyTime
            }
        }

        fun awardPoint() {
            points++
        }
    }

    fun solveOne(puzzleText: String, seconds: Int = 2503): Int {
        val reindeers = parseInput(puzzleText)

        (1 .. seconds).forEach { time ->
            reindeers.forEach { reindeer -> reindeer.update()}
        }

        return reindeers.map { it.distanceTravelled }.max()!!
    }

    fun solveTwo(puzzleText: String, seconds: Int = 2503): Int {
        val reindeers = parseInput(puzzleText)

        (1 .. seconds).forEach { time ->
            reindeers.forEach { reindeer -> reindeer.update()}

            val maxDistance = reindeers.maxBy { it.distanceTravelled }!!.distanceTravelled
            reindeers.filter { it.distanceTravelled == maxDistance }.forEach { it.awardPoint() }
        }

        return reindeers.map { it.points }.max()!!
    }

    private fun parseInput(puzzleText: String): List<Reindeer> {
        val reindeers = puzzleText.split("\n").map { line ->

            val tmp = line.split(" ")
            Reindeer(tmp[0], tmp[3].toInt(), tmp[6].toInt(), tmp[13].toInt())
        }
        return reindeers
    }
}