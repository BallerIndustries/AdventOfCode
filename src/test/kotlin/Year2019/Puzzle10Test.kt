package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle10.txt").readText().replace("\r", "")
    val puzzle = Puzzle10()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(309, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(416, result)
    }

    @Test
    fun `part b example a`() {
        val text = """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent()

        val result = puzzle.solveTwo(text)
        assertEquals(802, result)
    }
}

class Puzzle10 {
    data class Point(val x: Int, val y: Int) {
        fun rotationTo(other: Point): Double {
            return Math.atan2(this.y.toDouble() - other.y.toDouble(), this.x.toDouble() - other.x.toDouble()) * (180.0 / Math.PI)
        }

        fun manhattanDistanceTo(other: Point): Int {
           return Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
        }
    }

    fun solveOne(puzzleText: String): Int? {
        val asteroids = getAsteroids(puzzleText)
        val distinctRotationsToAsteroid = getRotationCountPerAsteroid(asteroids)
        return distinctRotationsToAsteroid.map { it.first }.max()
    }

    private fun getRotationCountPerAsteroid(asteroids: List<Point>): List<Pair<Int, Point>> {
        val distinctRotationsToAsteroid = asteroids.map { thisAsteroid ->
            val allOtherAsteroids = asteroids.filter { it != thisAsteroid }

            // Get angle of rotation for this asteroid to all the others
            val rotations = allOtherAsteroids.map { thisAsteroid.rotationTo(it) }.distinct()

            rotations.size to thisAsteroid
        }
        return distinctRotationsToAsteroid
    }

    private fun getAsteroids(puzzleText: String): List<Point> {
        val asteroids = puzzleText.split("\n").mapIndexed { y, line ->
            line.mapIndexed { x, char ->
                Point(x, y) to char
            }
        }.flatten().filter {
            it.second == '#'
        }.map { it.first }
        return asteroids
    }

    fun solveTwo(puzzleText: String): Int {
        val asteroids = getAsteroids(puzzleText)
        val distinctRotationsToAsteroid = getRotationCountPerAsteroid(asteroids)
        val positionOfLaser = distinctRotationsToAsteroid.maxBy { it.first }!!.second

        val pointRotationAndDistance = asteroids.filter {
            it != positionOfLaser
        }.map {
            Triple(it, positionOfLaser.rotationTo(it), positionOfLaser.manhattanDistanceTo(it))
        }.sortedWith(compareBy({ it.second }, { it.third })).toMutableList()

        var zappedAsteroids = 0
        var lastZappedAsteroid: Triple<Point, Double, Int>
        var index = pointRotationAndDistance.indexOfFirst { it.second >= 90.0 }

        do {
            lastZappedAsteroid = pointRotationAndDistance[index]
            pointRotationAndDistance.removeAt(index)
            zappedAsteroids++

            index = pointRotationAndDistance.indexOfFirst { it.second > lastZappedAsteroid.second }

            if (index == -1) {
                index = 0
            }
        } while (zappedAsteroids < 200)

        return lastZappedAsteroid.first.x * 100 + lastZappedAsteroid.first.y
    }
}

