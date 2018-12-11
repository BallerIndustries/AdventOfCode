package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class Puzzle11Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle11.txt")
            .readText().replace("\r", "")
    val puzzle = Puzzle11()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("235,85", result)
    }

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne("18")
        assertEquals("33,45", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("233,40,13", result)
    }

    @Test
    fun `example 1 part b`() {
        val result = puzzle.solveTwo("18")
        assertEquals("90,269,16", result)
    }

    @Test
    fun `example 2 part b`() {
        val result = puzzle.solveTwo("42")
        assertEquals("232,251,12", result)
    }

    data class Point(val x: Int, val y: Int)

    class Puzzle11 {
        fun solveOne(puzzleText: String): String {
            val gridSerialNumber = puzzleText.toInt()
            val powerLevels = createPowerLevels(gridSerialNumber)

            // Now find the max 3x3 square
            val max = (0 until 300).map { y ->
                (0 until 300).map { x ->
                    val sum = sumSquare(powerLevels, x, y, 3)
                    Pair(Point(y + 1, x + 1), sum)
                }
            }
            .flatten()
            .maxBy { it.second }

            return "${max?.first?.x},${max?.first?.y}"
        }

        fun sumSquare(powerLevels: List<List<Int>>, initX: Int, initY: Int, squareSize: Int): Long {
            if (initX + squareSize > powerLevels.lastIndex) return Long.MIN_VALUE
            if (initY + squareSize > powerLevels.lastIndex) return Long.MIN_VALUE

            return (initX until initX + squareSize).map { x ->
                (initY until initY + squareSize).map { y ->
                    powerLevels[x][y].toLong()
                }
            }.flatten().sum()
        }

        fun solveTwo(puzzleText: String): String {
            val gridSerialNumber = puzzleText.toInt()
            val powerLevels = createPowerLevels(gridSerialNumber)

            // Now find the max 3x3 square
            val max =
            (0 until 300).map { y ->
                println(y)

                (0 until 300).map { x ->


                    val (squareSize, maxSum) = getMaxSubSquareSum(powerLevels, x, y)
                    val point = Point(y + 1, x + 1)
                    Triple(point, maxSum, squareSize)
                }
            }
            .flatten()
            .maxBy { it.second }

            return "${max?.first?.x},${max?.first?.y},${max?.third}"
        }

        private fun getMaxSubSquareSum(powerLevels: List<List<Int>>, x: Int, y: Int): Pair<Int, Long> {
            var sum = powerLevels[x][y].toLong()
            val squareOneToSum = 1 to sum

            val sizeToSum = (2 .. 300).map { squareSize ->
                val squareDelta = squareSize - 1

                // We want to calculate the L shape around the previous square
                val bottomLeft = Point(x, y + squareDelta)
                val bottomRight = Point(x + squareDelta, y + squareDelta)
                val topRight = Point(x + squareDelta, y)

                val sumOfL = doTheLSum(powerLevels, bottomLeft, bottomRight, topRight)
                sum += sumOfL

                squareSize to sum
            }.toMutableList()

            // Add the size 1 square
            sizeToSum.add(squareOneToSum)

            return sizeToSum.maxBy { it.second }!!
        }

        private fun doTheLSum(powerLevels: List<List<Int>>, bottomLeft: Point, bottomRight: Point, topRight: Point): Int {
                if (bottomRight.x > powerLevels.lastIndex || bottomRight.y > powerLevels.lastIndex) {
                    return Int.MIN_VALUE
                }

                return (bottomLeft.x .. bottomRight.x).sumBy { x -> powerLevels[x][bottomLeft.y] } +
                        (topRight.y .. bottomRight.y).sumBy { y -> powerLevels[topRight.x][y] }
        }

        private fun createPowerLevels(gridSerialNumber: Int): List<List<Int>> {
            return (1..300).map { y ->
                (1..300).map { x ->
                    val rackId = x + 10
                    var powerLevel = rackId * y
                    powerLevel += gridSerialNumber
                    powerLevel *= rackId

                    val stringPowerLevel = Math.abs(powerLevel).toString()
                    powerLevel = if (stringPowerLevel.length >= 2)
                        stringPowerLevel[stringPowerLevel.lastIndex - 2].toString().toInt()
                    else
                        0

                    powerLevel - 5
                }
            }
        }
    }
}