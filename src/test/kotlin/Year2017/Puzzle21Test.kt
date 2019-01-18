package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle21Test {
    val puzzle = Puzzle21()
    val puzzleText = this::class.java.getResource("/2017/puzzle21.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(170, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(571, result)
    }

    @Test
    fun `rotate 2x2 three times`() {
        val pattern = puzzle.parsePattern("#./..")
        assertEquals("#./..", pattern.toString())
        assertEquals(".#/..", pattern.rotateRight().toString())
        assertEquals("../.#", pattern.rotateRight().rotateRight().toString())
        assertEquals("../#.", pattern.rotateRight().rotateRight().rotateRight().toString())
    }

    @Test
    fun `rotate 3x3 three times`() {
        val pattern = puzzle.parsePattern("#../.../...")
        assertEquals("#../.../...", pattern.toString())
        assertEquals("..#/.../...", pattern.rotateRight().toString())
        assertEquals(".../.../..#", pattern.rotateRight().rotateRight().toString())
        assertEquals(".../.../#..", pattern.rotateRight().rotateRight().rotateRight().toString())
    }

    @Test
    fun `mirror 3x3 horizontally`() {
        val pattern = puzzle.parsePattern("#../#../#..")
        assertEquals("#../#../#..", pattern.toString())
        assertEquals("..#/..#/..#", pattern.mirrorHorizontal().toString())
    }

    @Test
    fun `mirror 3x3 vertically`() {
        val pattern = puzzle.parsePattern("###/.../...")
        assertEquals("###/.../...", pattern.toString())
        assertEquals(".../.../###", pattern.mirrorVertical().toString())
    }
}

class Puzzle21 {
    data class Point(val x: Int, val y: Int)

    data class Pattern(val data: Map<Point, Char>) {
        fun width() = data.keys.maxBy { it.x }!!.x + 1

        val twoByTwoRotate = mapOf(
            Point(0, 0) to Point(1, 0),
            Point(1, 0) to Point(1, 1),
            Point(1, 1) to Point(0, 1),
            Point(0, 1) to Point(0, 0)
        )

        val threeByThreeRotate = mapOf(
            Point(0, 0) to Point(2, 0),
            Point(1, 0) to Point(2, 1),
            Point(2, 0) to Point(2, 2),

            Point(0, 1) to Point(1, 0),
            Point(1, 1) to Point(1, 1),
            Point(2, 1) to Point(1, 2),

            Point(0, 2) to Point(0, 0),
            Point(1, 2) to Point(0, 1),
            Point(2, 2) to Point(0, 2)
        )

        fun rotateRight(): Pattern {
            val width = width()
            val rotateMatrix = if (width == 2) twoByTwoRotate else if (width == 3) threeByThreeRotate else throw RuntimeException()

            val dog = data.map { (point, char) ->
                rotateMatrix[point]!! to char
            }.toMap()

            return Pattern(dog)
        }

        fun mirrorHorizontal(): Pattern {
            val minX = this.data.keys.map { it.x }.min()!!
            val maxX = this.data.keys.map { it.x }.max()!!

            val mirroredData = this.data.map { (point, char) ->
                when {
                    point.x == minX -> point.copy(x = maxX) to char
                    point.x == maxX -> point.copy(x = minX) to char
                    else -> point to char
                }
            }.toMap()

            return Pattern(mirroredData)
        }

        fun mirrorVertical(): Pattern {
            val minY = this.data.keys.map { it.y }.min()!!
            val maxY = this.data.keys.map { it.y }.max()!!

            val mirroredData = this.data.map { (point, char) ->
                when {
                    point.y == minY -> point.copy(y = maxY) to char
                    point.y == maxY -> point.copy(y = minY) to char
                    else -> point to char
                }
            }.toMap()

            return Pattern(mirroredData)
        }

        fun giveMeThePengestCombos(): List<Pattern> {

            return listOf(
                this,

                this.rotateRight(),
                this.mirrorVertical(),
                this.mirrorHorizontal(),

                this.rotateRight().rotateRight(),
                this.rotateRight().mirrorVertical(),
                this.rotateRight().mirrorHorizontal(),

                this.rotateRight().rotateRight().rotateRight(),
                this.rotateRight().rotateRight().mirrorVertical(),
                this.rotateRight().rotateRight().mirrorHorizontal(),

                this.rotateRight().rotateRight().rotateRight().rotateRight(),
                this.rotateRight().rotateRight().rotateRight().mirrorVertical(),
                this.rotateRight().rotateRight().rotateRight().mirrorHorizontal()
            )
        }

        override fun toString(): String {
            val width = width()

            val dog = (0 until width).map { y ->
                (0 until width).map { x ->

                    val point = Point(x, y)
                    this.data[point]!!
                }.joinToString("")
            }.joinToString("/")

            return dog
        }

        fun split(count: Int): List<Pattern> {
            if (count != 2 && count != 3) throw RuntimeException("WOaaahhh!!! NO!!!!")

            var place = Point(0, 0)
            val width = width()

            while (true) {

                if (place.x > width) {
                    val subPattern = getPattern(place, count)
                }




            }




            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        private fun getPattern(place: Point, count: Int): Pattern {
            (place.x until place.x + count).map { x ->
                (place.y until place.y + count)


            }
        }
    }

    fun solveOne(puzzleText: String): Int {
        // Parse the puzzleText
        val enhancements = puzzleText.split("\n").flatMap { line ->
            val (from, to) = line.split(" => ").map { parsePattern(it) }
            from.giveMeThePengestCombos().map { it to to }
        }.toMap()


        var currentPattern = ".#./..#/###"

        (0 until 5).forEach {

            val windows: List<Pattern> = splitUpPattern(currentPattern)



        }









        return 0
    }

    private fun splitUpPattern(patternText: String): List<Pattern> {
        val pattern = parsePattern(patternText)

        if (pattern.width() % 2 == 0) {
            // Multiple of two
            return pattern.split(2)
        }
        else {
            // Multiple of three
            return pattern.split(3)

        }
    }

    fun parsePattern(text: String): Pattern {
        var x = 0
        var y = 0
        val map = mutableMapOf<Point, Char>()

        text.forEach { char ->
            if (char == '.' || char == '#') {
                map[Point(x, y)] = char
                x++
            }
            else if (char == '/') {
                x = 0
                y++
            }
            else {
                throw RuntimeException("Nooooo!!! WHY DID YOU DO THIS TO ME?!?!?!")
            }
        }

        return Pattern(map)
    }

    fun solveTwo(puzzleText: String): Int {
        return 0
    }
}