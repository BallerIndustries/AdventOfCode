package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle6Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle6.txt").readText().replace("\r", "")
    val puzzle = Puzzle6()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(0, result)
    }

    @Test
    fun `example`() {
        val dog = "1, 1\n" +
                "1, 6\n" +
                "8, 3\n" +
                "3, 4\n" +
                "5, 5\n" +
                "8, 9"

        val result = puzzle.solveOne(dog)
        assertEquals(result, 17)
    }

    @Test
    fun `nearest point`() {
        val dog = "1, 1\n" +
                "1, 6\n" +
                "8, 3\n" +
                "3, 4\n" +
                "5, 5\n" +
                "8, 9"
        val pointMap = puzzle.createPointMap(dog)

        assertEquals("0", puzzle.findNearestPoint(pointMap, 1, 1))
        assertEquals("1", puzzle.findNearestPoint(pointMap, 1, 6))
        assertEquals(null, puzzle.findNearestPoint(pointMap, 5, 1))
    }



    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(0, result)
    }
}

data class Point(val id: String, val x: Int, val y: Int)

class Puzzle6 {
    fun solveOne(puzzleText: String): Int {
        val pointMap = createPointMap(puzzleText)

        val top = pointMap.values.minBy { it.y }
        val bottom = pointMap.values.maxBy { it.y }
        val left = pointMap.values.minBy { it.x }
        val right = pointMap.values.maxBy { it.x }

        val topMax = top!!.y
        val bottomMax = bottom!!.y
        val rightMax = right!!.x
        val leftMax = left!!.x
        var buffer = ""


        println("gridArea = ${Math.abs(topMax - bottomMax) * Math.abs(leftMax - rightMax)}")

        println("topMax = $topMax bottomMax = $bottomMax rightMax = $rightMax leftMax = $leftMax")
        val nearestPointMap = mutableMapOf<Point, Point>()


        val jur: List<Char> = ('A' .. 'Z').map { it }

        (topMax .. bottomMax ).forEach { y ->
            (leftMax .. rightMax ).forEach { x ->



                val nearestPoint = findNearestPoint(pointMap, x, y)
                if (nearestPoint != null) nearestPointMap.put(Point("bob", x, y), nearestPoint)

//                if (nearestPoint == null) buffer += "."
//                else buffer += jur.get(nearestPoint.id.toInt())


            }

//            buffer += "\n"
        }

//        println(buffer)

        // Remove points that are on the edge
        val infinitePoints = nearestPointMap.filter {
            it.key.x == leftMax || it.key.x == rightMax || it.key.y == topMax || it.key.y == bottomMax
        }.map { it.value }.toSet()

        val nonInfiniteShite = nearestPointMap.filterNot { infinitePoints.contains(it.value) }.values

        val dog = nonInfiniteShite.groupBy { it }.maxBy { it.value.size }

        println(dog!!.value.count())
        return dog.value.count()
    }

    fun createPointMap(puzzleText: String): Map<Pair<Int, Int>, Point> {
        val pointMap = puzzleText.split("\n").mapIndexed { index, line ->
            val tmp = line.split(", ").map { it.toInt() }
            Point(index.toString(), tmp[0], tmp[1])
        }.associate { Pair(it.x, it.y) to it }
        return pointMap
    }

    fun findNearestPoint(pointMap: Map<Pair<Int, Int>, Point>, x: Int, y: Int): Point? {
        val distances: List<Pair<Point, Int>> = pointMap.values.map { point ->
            point to manhattanDistance(x, y, point.x, point.y)
        }

        val minDistance: Pair<Point, Int>? = distances.minBy { it.second }

        if (distances.filter { it.second == minDistance!!.second }.size > 1) return null

        return minDistance!!.first
    }

    fun manhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2)
    }

    fun solveTwo(puzzleText: String): Int {
        val pointMap = createPointMap(puzzleText)

        val top = pointMap.values.minBy { it.y }
        val bottom = pointMap.values.maxBy { it.y }
        val left = pointMap.values.minBy { it.x }
        val right = pointMap.values.maxBy { it.x }

        val topMax = top!!.y
        val bottomMax = bottom!!.y
        val rightMax = right!!.x
        val leftMax = left!!.x

        val nearestPointMap = mutableMapOf<Point, Point>()
        var counter = 0

        (topMax .. bottomMax ).forEach { y ->
            (leftMax .. rightMax ).forEach { x ->

//                val nearestPoint = findNearestPoint(pointMap, x, y)
//                if (nearestPoint != null) nearestPointMap.put(Point("bob", x, y), nearestPoint)

                val sum = calcManDistToOtherPoints(x, y, pointMap)

                if (sum < 10000) {
                    counter++
                }
            }
        }

        return counter;


    }

    private fun calcManDistToOtherPoints(x: Int, y: Int, pointMap: Map<Pair<Int, Int>, Point>): Int {

        return pointMap.values.sumBy {
            manhattanDistance(x, y, it.x, it.y)
        }


    }
}
