package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle22Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle22.txt").readText().replace("\r", "")
    val puzzle = Puzzle22()

    val exampleText = """
            depth: 510
            target: 10,10
        """.trimIndent()

    @Test
    fun `example 1`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(114, result)
    }

    @Test
    fun `map for example 1`() {
        val dog = """
            M=.|=.|.|=.|=|=.
            .|=|=|||..|.=...
            .==|....||=..|==
            =.|....|.==.|==.
            =|..==...=.|==..
            =||.=.=||=|=..|=
            |.=.===|||..=..|
            |..==||=.|==|===
            .=..===..=|.|||.
            .======|||=|=.|=
            .===|=|===T===||
            =|||...|==..|=.|
            =.=|=.=..=.||==|
            ||=|=...|==.=|==
            |=.=||===.|||===
            ||.|==.|.|.||=||
        """.trimIndent()

        val spagh = puzzle.outputMap(exampleText)
        assertEquals(dog, spagh)
    }

    @Test
    fun `puzzle part a`() {
        // 5562 too low
        // 6413 too high
        // Not 5570
        val result = puzzle.solveOne(puzzleText)
        assertEquals(420, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    class Puzzle22 {



        enum class RegionType(val riskLevel: Int, val char: Char) { ROCKY(0, '.'), WET(1, '='), NARROW(2, '|') }

        data class Point(val x: Int, val y: Int) {
            companion object {
                val ZERO = Point(0, 0)

                val pointToErosionLevel = mutableMapOf<Point, Int>()
                val pointToGeologicIndex = mutableMapOf<Point, Int>()

                fun from(text: String): Point {
                    return text.split(",").map { it.toInt() }.let { Point(it[0], it[1]) }
                }
            }

            private fun calculateGeologicIndex(target: Point, depth: Int): Int {
                if (!pointToGeologicIndex.containsKey(this)) {
                    val geologicIndex = when {
                        this == ZERO -> 0
                        this == target -> 0
                        y == 0 -> x * 16807
                        x == 0 -> y * 48271
                        else -> {
                            val a = this.copy(x = x - 1)
                            val b = this.copy(y = y - 1)
                            a.calculateErosionLevel(target, depth) * b.calculateErosionLevel(target, depth)
                        }
                    }

                    pointToGeologicIndex[this] = geologicIndex
                }

                return pointToGeologicIndex[this]!!
            }

            private fun calculateErosionLevel(target: Point, depth: Int): Int {
                if (!pointToErosionLevel.containsKey(this)) {
                    pointToErosionLevel[this] = (this.calculateGeologicIndex(target, depth) + depth) % 20183
                }

                return pointToErosionLevel[this]!!
            }

            fun calculateRegionType(target: Point, depth: Int): RegionType {
                val horse = calculateErosionLevel(target, depth) % 3

                return when (horse) {
                    0 -> RegionType.ROCKY
                    1 -> RegionType.WET
                    2 -> RegionType.NARROW
                    else -> throw RuntimeException("horse should be between 0 and 2 weird! horse = $horse")
                }
            }
        }

        fun solveOne(puzzleText: String): Int {
            val (depth, target) = puzzleText.replace("depth: ", "")
                .replace("target: ", "")
                .split("\n")
                .let { Pair(it[0].toInt(), Point.from(it[1])) }

            println("depth = $depth target = $target")

            //val (width + 1, height) = target
            val width = target.x + 1
            val height = target.y + 1


            val regionTypes = generateMap(target, depth, width, height)

            return regionTypes.values.sumBy { it.riskLevel }
        }

        fun outputMap(puzzleText: String): String {
            val (depth, target) = puzzleText.replace("depth: ", "")
                .replace("target: ", "")
                .split("\n")
                .let { Pair(it[0].toInt(), Point.from(it[1])) }


            val width = target.x + 6
            val height = target.y + 6


            val regionTypes = generateMap(target, depth, width, height)

            return renderMap(regionTypes, target)
        }

        private fun renderMap(regionTypes: Map<Point, RegionType>, target: Point): String {
            val width = regionTypes.keys.maxBy { it.x }!!.x
            val height = regionTypes.keys.maxBy { it.y }!!.y

            return (0 .. height).map { y ->
                (0 .. width).map { x ->

                    val point = Point(x, y)

                    if (point == Point.ZERO) {
                        'M'
                    }
                    else if (point == target) {
                        'T'
                    }
                    else {
                    regionTypes[point]!!.char
                    }
                }.joinToString("")
            }.joinToString("\n")
        }

        private fun generateMap(target: Point, depth: Int, width: Int, height: Int): Map<Point, RegionType> {
            var current = 0
            val total = (width) * (height)

            val regionTypes = (0 until height).flatMap { y ->
                (0 until width).map { x ->

                    current++
                    println("$current / $total")

                    val point = Point(x, y)

                    point to point.calculateRegionType(target, depth)
                }
            }.toMap()
            return regionTypes
        }


        fun solveTwo(puzzleText: String): Int {
            return 1337
        }
    }
}