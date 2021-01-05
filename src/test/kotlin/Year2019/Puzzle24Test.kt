package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle24.txt").readText().replace("\r", "")
    val puzzle = Puzzle24()

    @Test
    fun `example part a`() {
        val puzzleText = ".....\n" +
                ".....\n" +
                ".....\n" +
                "#....\n" +
                ".#..."
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2129920L, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = ".....\n" +
                ".....\n" +
                ".....\n" +
                "#....\n" +
                ".#..."
        val result = puzzle.solveTwo(puzzleText, 10)
        assertEquals(99L, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(20751345, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText, 200)
        assertEquals("a", result)
    }
}

class Puzzle24 {
    fun solveOne(puzzleText: String): Long {
        var grid = parseGrid2D(puzzleText)
        val grids = mutableSetOf<Map<Point2D, Char>>()

        while (grid !in grids) {
            grids.add(grid)
            grid = iterate(grid)
        }

        val width = grid.keys.maxBy { it.x }!!.x + 1

        return grid.entries.fold(0L) { acc, (point, char) ->
            if (char == '#') {
                val power = (point.y * width) + point.x
                val value = Math.pow(2.0, power.toDouble()).toLong()
                acc + value
            }
            else {
                acc
            }
        }
    }

    private fun iterate(grid: Map<Point2D, Char>): Map<Point2D, Char> {
        return grid.entries.associate { (point, char) ->
            val adjacentBugCount = point.neighbors().count { grid[it] == '#' }

            if (char == '#' && adjacentBugCount != 1) {
                point to '.'
            }
            else if (char == '.' && (adjacentBugCount == 1 || adjacentBugCount == 2)) {
                point to '#'
            }
            else {
                point to char
            }
        }
    }

    fun solveTwo(puzzleText: String, minutes: Int): Long {
        var grid = parseGrid3D(puzzleText)

        (0 until minutes).forEach {
           grid = iterate3D(grid)
        }

        return grid.values.count { it == '#' }.toLong()
    }

    private fun iterate3D(grid: Map<Point3D, Char>): Map<Point3D, Char> {
        val relevantPoints = grid.entries.filter { it.value == '#' }
            .map { it.key }.flatMap { it.neighbors() }.toSet()

        return relevantPoints.associate { point ->
            val adjacentBugCount = point.neighbors().count { grid[it] == '#' }
            val char = grid[point] ?: '.'

            if (char == '#' && adjacentBugCount != 1) {
                point to '.'
            }
            else if (char == '.' && (adjacentBugCount == 1 || adjacentBugCount == 2)) {
                point to '#'
            }
            else {
                point to char
            }
        }
    }

    data class Point3D(val x: Int, val y: Int, val z: Int) {
        fun neighbors(): List<Point3D> {
            return up() + down() + left() + right()
        }

        private fun up(): List<Point3D> {
            val new = this.copy(y = y - 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = it, y = 4, z = z - 1) }
            }

            return zabadoo(new, range)
        }

        private fun down(): List<Point3D> {
            val new = this.copy(y = y + 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = it, y = 0, z = z - 1) }
            }

            return zabadoo(new, range)
        }

        private fun zabadoo(
            new: Point3D,
            range: IntRange
        ): List<Point3D> {
            return when {
                // Left edge
                new.x == -1 -> range.map { Point3D(x = 4, y = it, z = z + 1) }
                // Right edge
                new.x == 5 -> range.map { Point3D(x = 0, y = it, z = z + 1) }
                // Top edge
                new.y == -1 -> range.map { Point3D(x = it, y = 4, z = z + 1) }
                // Bottom edge
                new.y == 5 -> range.map { Point3D(x = it, y = 0, z = z + 1) }
                else -> listOf(new)
            }
        }

        private fun left(): List<Point3D> {
            val new = this.copy(x = x - 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = 4, y = it, z = z - 1) }
            }

            return zabadoo(new, range)
        }

        private fun right(): List<Point3D> {
            val new = this.copy(x = x + 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = 0, y = it, z = z - 1) }
            }

            return zabadoo(new, range)
        }
    }

    data class Point2D(val x: Int, val y: Int) {
        fun neighbors(): List<Point2D> {
            return listOf(
                this.copy(x = x - 1),
                this.copy(x = x + 1),
                this.copy(y = y - 1),
                this.copy(y = y + 1),
            )
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

    private fun parseGrid2D(puzzleText: String): Map<Point2D, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines.first().count()

        val grid = (0 until width).flatMap { x ->
            (0 until height).map { y ->

                val p = Point2D(x, y)
                val c = lines[y][x]

                p to c
            }
        }.toMap()
        return grid
    }
}

