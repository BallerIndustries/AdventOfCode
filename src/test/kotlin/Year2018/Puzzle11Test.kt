package Year2018

import junit.framework.Assert.assertEquals
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
    fun example() {
        val result = puzzle.solveOne("18")
        assertEquals("33,45", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("233,40,13", result)
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
                (0 until 300).map { x ->
                    println("($x,$y)")

                    (1 .. 300).map { squareSize ->
                        val sum = sumSquare(powerLevels, x, y, squareSize)
                        Triple(Point(y + 1, x + 1), sum, squareSize)
                    }
                }
            }
            .flatten()
            .flatten()
            .maxBy { it.second }

//            .maxBy { }

            return "${max?.first?.x},${max?.first?.y},${max?.third}"
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