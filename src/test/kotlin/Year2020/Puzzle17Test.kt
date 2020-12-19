package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle17.txt").readText().replace("\r", "")
    val puzzle = Puzzle17()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(426, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1892, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = ".#.\n" +
                "..#\n" +
                "###"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(112, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = ".#.\n" +
                "..#\n" +
                "###"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(848, result)
    }

    @Test
    fun `there should be 26 neighbours for a 3D point`() {
        val neighbours = Puzzle17.Point3D(0, 0, 0).neighbours()
        assertEquals(26, neighbours.size)
    }

    @Test
    fun `there should be 80 neighbours for a 4D point`() {
        val neighbours = Puzzle17.Point4D(0, 0, 0, 0).neighbours()
        assertEquals(80, neighbours.size)
        assertEquals(80, neighbours.toSet().size)
    }
}

class Puzzle17 {
    fun solveOne(puzzleText: String): Int {
        var grid = parseGrid3D(puzzleText)

        (0 until 6).forEach {
            grid = iterate3D(grid)
        }

        return grid.values.count { it == '#' }
    }

    fun solveTwo(puzzleText: String): Int {
        var grid = parseGrid4D(puzzleText)

        (0 until 6).forEach {
            grid = iterate4D(grid)
        }

        return grid.values.count { it == '#' }
    }

    private fun iterate4D(grid: Map<Point4D, Char>): Map<Point4D, Char> {
        val newPoints = mutableSetOf<Point4D>()

        grid.keys.forEach { point ->
            newPoints.addAll(point.neighbours())
        }

        return newPoints.associate { point ->
            val pointIsActive = grid[point] == '#'
            val activeNeighbourCount = point.neighbours().map { grid[it] }.count { it == '#' }

            if (pointIsActive && activeNeighbourCount in (2..3)) {
                point to '#'
            }
            else if (!pointIsActive && activeNeighbourCount == 3) {
                point to '#'
            }
            else {
                point to '.'
            }
        }
    }

    private fun iterate3D(grid: Map<Point3D, Char>): Map<Point3D, Char> {
        val newPoints = mutableSetOf<Point3D>()

        grid.keys.forEach { point ->
            newPoints.addAll(point.neighbours())
        }

        return newPoints.associate { point ->
            val pointIsActive = grid[point] == '#'
            val activeNeighbourCount = point.neighbours().map { grid[it] }.count { it == '#' }


            if (pointIsActive && activeNeighbourCount in (2..3)) {
                point to '#'
            }
            else if (!pointIsActive && activeNeighbourCount == 3) {
                point to '#'
            }
            else {
                point to '.'
            }
        }
    }

    private fun parseGrid3D(puzzleText: String): Map<Point3D, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines.first().count()

        val grid = (0 until width).flatMap { x ->
            (0 until height).map { y ->

                val p = Point3D(x, y, 0)
                val c = lines[y][x]

                p to c
            }
        }.toMap()
        return grid
    }

    private fun parseGrid4D(puzzleText: String): Map<Point4D, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines.first().count()

        val grid = (0 until width).flatMap { x ->
            (0 until height).map { y ->

                val p = Point4D(x, y, 0, 0)
                val c = lines[y][x]

                p to c
            }
        }.toMap()
        return grid
    }

    data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int) {
        fun neighbours(): List<Point4D> {
            val neighbours = mutableListOf<Point4D>()

            (-1 .. 1).forEach { i ->
                (-1 .. 1).forEach { j ->
                    (-1 .. 1).forEach { k ->
                        (-1 .. 1).forEach { l ->
                            neighbours.add(this.copy(
                                x = this.x - i,
                                y = this.y - j,
                                z = this.z - k,
                                w = this.w - l)
                            )
                        }
                    }
                }
            }

            val returnValue = neighbours - this
            return returnValue
        }
    }

    data class Point3D(val x: Int, val y: Int, val z: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun left() = this.copy(x = this.x - 1)
        fun right() = this.copy(x = this.x + 1)

        fun upLeft() = up().left()
        fun upRight() = up().right()
        fun downLeft() = down().left()
        fun downRight() = down().right()

        fun neighbours(): List<Point3D> {
            val twoDNeighbours = listOf(up(), down(), left(), right(), upLeft(), upRight(), downLeft(), downRight())
            val thisAndTwoDNeighbours = listOf(this) + twoDNeighbours
            return twoDNeighbours + thisAndTwoDNeighbours.map { it.copy(z = it.z - 1) } + thisAndTwoDNeighbours.map { it.copy(z = it.z + 1) }
        }
    }
}

