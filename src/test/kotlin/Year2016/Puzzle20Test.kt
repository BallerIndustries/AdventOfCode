package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle20Test {
    val puzzle = Puzzle20()
    val puzzleText = this::class.java.getResource("/2016/puzzle20.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(22887907, result)
    }

    @Test
    fun `can solve part b`() {
        // 108 is too low
        val result= puzzle.solveTwo(puzzleText)
        assertEquals(1410630, result)
    }
}

class Puzzle20 {
    fun solveOne(puzzleText: String): Long {
        val ranges = parseRanges(puzzleText)
        var valueToTest = 0L

        while (true) {
            // Find a range that covers the value
            val coveringRange = ranges.find { range -> range.contains(valueToTest) } ?: return valueToTest
            valueToTest = coveringRange.endInclusive + 1
        }

        throw RuntimeException("Fuck a duck")
    }

    fun solveTwo(puzzleText: String): Long {
        var ranges = parseRanges(puzzleText).toMutableList()
        var index = 0

        do {
            removeOverlaps(index, ranges)
        } while (hasOverlaps(ranges))

        if (hasOverlaps(ranges)) {
            throw RuntimeException("Mate!!!")
        }

        val theMagicLong = 4294967295L
        val killedIpAddressed = (ranges.sumByDouble { it.endInclusive.toDouble() - it.start.toDouble() + 1.0  }).toLong()
        return theMagicLong - killedIpAddressed
    }

    private fun removeOverlaps(index: Int, ranges: MutableList<LongRange>) {
        var index1 = index
        while (index1 < ranges.size) {
            var currentRange = ranges[index1]
            val rangesAheadOfThisIndex = ranges.subList(index1 + 1, ranges.size)
            val tmp = extendOutThisBork(rangesAheadOfThisIndex, currentRange)

            ranges[index1] = tmp.first
            ranges.removeAll(tmp.second)
            index1++
        }
    }

    private fun hasOverlaps(ranges: MutableList<LongRange>): Boolean {
        for (index in 0 until ranges.size) {
            val thisRange = ranges[index]

            for (jindex in index + 1 until ranges.size) {
                val thatRange = ranges[jindex]

                if (thisRange.contains(thatRange.start) || thisRange.contains(thatRange.endInclusive) || thatRange.contains(thisRange.start) || thatRange.contains(thisRange.endInclusive)) {
                    println("thisRange = $thisRange thatRange = $thatRange")
                    return true
                }
            }
        }

        return false
    }

    private fun extendOutThisBork(rangesAheadOfThisIndex: MutableList<LongRange>, currentRange: LongRange): Pair<LongRange, MutableList<LongRange>> {
        var currentRange1 = currentRange
        var rangesThatCollideUs = rangesAheadOfThisIndex.filter { range ->
            range.contains(currentRange1.endInclusive) || range.contains(currentRange1.start) || currentRange1.contains(range.endInclusive) || currentRange1.contains(range.start)
        }

        val rangesToDrop = mutableListOf<LongRange>()

        while (rangesThatCollideUs.isNotEmpty()) {
            // Modify the current range
            val collAndMe = rangesThatCollideUs.toList() + listOf(currentRange1)
            val maxEnd = collAndMe.maxBy { range -> range.endInclusive }!!.endInclusive
            val minStart = collAndMe.minBy { range -> range.start }!!.start

            currentRange1 = LongRange(minStart, maxEnd)

            // Clear out the collided ranges
            rangesToDrop.addAll(rangesThatCollideUs)

            // Find collisions we haven't already found
            val dog = rangesAheadOfThisIndex
                .filter { range -> !rangesToDrop.contains(range) }
                .filter { range ->
                    range.contains(currentRange1.endInclusive) || range.contains(currentRange1.start) || currentRange1.contains(range.endInclusive) || currentRange1.contains(range.start)
                }

            rangesThatCollideUs = dog
        }

        return currentRange1 to rangesToDrop
    }

    private fun parseRanges(puzzleText: String): List<LongRange> {
        val ranges = puzzleText
            .split("\n")
            .map { it.split("-").let { LongRange(it[0].toLong(), it[1].toLong()) } }
            .sortedBy { it.start }
        return ranges
    }
}
