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
        val puzzleText =
                "....#\n" +
                "#..#.\n" +
                "#..##\n" +
                "..#..\n" +
                "#...."
        val result = puzzle.solveTwo(puzzleText, 10)
        assertEquals(99, result)
    }

    @Test
    fun `angus example part b`() {
        val puzzleText = """
            .....
            .....
            ...#.
            .....
            .....
""".trimIndent()
        val result = puzzle.solveTwo(puzzleText, 1)
        assertEquals(8, result)
    }


    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(20751345, result)
    }

    @Test
    fun `puzzle part b`() {
        // 2019 too high
        val result = puzzle.solveTwo(puzzleText, 200)
        assertEquals(1983, result)
    }

    @Test
    fun `3-3-0 neighbors, vanilla position`() {
        val expected = setOf(
            Puzzle24.Point3D(x=3, y=2, z=0),
            Puzzle24.Point3D(x=3, y=4, z=0),
            Puzzle24.Point3D(x=2, y=3, z=0),
            Puzzle24.Point3D(x=4, y=3, z=0)
        )
        assertEquals(expected, Puzzle24.Point3D(3, 3, 0).neighbors())
    }

    @Test
    fun `0-3-0 neighbors, left edge`() {
        val expected = setOf(
            Puzzle24.Point3D(x=0, y=2, z=0),
            Puzzle24.Point3D(x=0, y=4, z=0),
            Puzzle24.Point3D(x=1, y=3, z=0),
            Puzzle24.Point3D(x=1, y=2, z=-1),
        )
        assertEquals(expected, Puzzle24.Point3D(x=0, y=3, z=0).neighbors())
    }

    @Test
    fun `4-0-0 neighbors, top right corner`() {
        val expected = setOf(
            Puzzle24.Point3D(x=3, y=0, z=0),
            Puzzle24.Point3D(x=4, y=1, z=0),
            Puzzle24.Point3D(x=2, y=1, z=-1),
            Puzzle24.Point3D(x=3, y=2, z=-1),
        )
        assertEquals(expected, Puzzle24.Point3D(x=4, y=0, z=0).neighbors())
    }

    @Test
    fun `0-4-0 neighbors, bottom left corner`() {
        val expected = setOf(
            Puzzle24.Point3D(x=1, y=2, z=-1),
            Puzzle24.Point3D(x=2, y=3, z=-1),

            Puzzle24.Point3D(x=0, y=3, z=0),
            Puzzle24.Point3D(x=1, y=4, z=0),
        )
        assertEquals(expected, Puzzle24.Point3D(x=0, y=4, z=0).neighbors())
    }


    @Test
    fun `1-2-0 neighbors, left of center`() {
        val expected = setOf(
            Puzzle24.Point3D(x=0, y=2, z=0),
            Puzzle24.Point3D(x=1, y=1, z=0),
            Puzzle24.Point3D(x=1, y=3, z=0),

            Puzzle24.Point3D(x=0, y=0, z=1),
            Puzzle24.Point3D(x=0, y=1, z=1),
            Puzzle24.Point3D(x=0, y=2, z=1),
            Puzzle24.Point3D(x=0, y=3, z=1),
            Puzzle24.Point3D(x=0, y=4, z=1),
        )
        assertEquals(expected, Puzzle24.Point3D(x=1, y=2, z=0).neighbors())
    }

    @Test
    fun `3-2-0 neighbors, right of center`() {
        val expected = setOf(
            Puzzle24.Point3D(x=4, y=2, z=0),
            Puzzle24.Point3D(x=3, y=1, z=0),
            Puzzle24.Point3D(x=3, y=3, z=0),

            Puzzle24.Point3D(x=4, y=0, z=1),
            Puzzle24.Point3D(x=4, y=1, z=1),
            Puzzle24.Point3D(x=4, y=2, z=1),
            Puzzle24.Point3D(x=4, y=3, z=1),
            Puzzle24.Point3D(x=4, y=4, z=1),
        )
        assertEquals(expected, Puzzle24.Point3D(x=3, y=2, z=0).neighbors())
    }

    @Test
    fun `2-1-0 neighbors, north of center`() {
        val expected = setOf(
            Puzzle24.Point3D(x=1, y=1, z=0),
            Puzzle24.Point3D(x=3, y=1, z=0),
            Puzzle24.Point3D(x=2, y=0, z=0),

            Puzzle24.Point3D(x=0, y=0, z=1),
            Puzzle24.Point3D(x=1, y=0, z=1),
            Puzzle24.Point3D(x=2, y=0, z=1),
            Puzzle24.Point3D(x=3, y=0, z=1),
            Puzzle24.Point3D(x=4, y=0, z=1),
        )
        assertEquals(expected, Puzzle24.Point3D(x=2, y=1, z=0).neighbors())
    }

    @Test
    fun `2-3-0 neighbors, south of center`() {
        val expected = setOf(
            Puzzle24.Point3D(x=2, y=4, z=0),
            Puzzle24.Point3D(x=1, y=3, z=0),
            Puzzle24.Point3D(x=3, y=3, z=0),

            Puzzle24.Point3D(x=0, y=4, z=1),
            Puzzle24.Point3D(x=1, y=4, z=1),
            Puzzle24.Point3D(x=2, y=4, z=1),
            Puzzle24.Point3D(x=3, y=4, z=1),
            Puzzle24.Point3D(x=4, y=4, z=1),
        )
        assertEquals(expected, Puzzle24.Point3D(x=2, y=3, z=0).neighbors())
    }
}

class Puzzle24 {
    fun solveOne(puzzleText: String): Long {
        var grid = parseGrid2D(puzzleText)
        val grids = mutableSetOf<Map<Point2D, Char>>()

        while (grid !in grids) {
            grids.add(grid)
            grid = iterate2D(grid)
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

    private fun iterate3D(grid: Map<Point3D, Char>): Map<Point3D, Char> {
        val relevantPoints = grid.entries.filter { it.value == '#' }.flatMap { it.key.neighbors() }.toSet()

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

    private fun iterate2D(grid: Map<Point2D, Char>): Map<Point2D, Char> {
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

    fun solveTwo(puzzleText: String, minutes: Int): Int {
        return (0 until minutes)
            .fold(parseGrid3D(puzzleText)) { acc, _ -> iterate3D(acc) }
            .values.count { it == '#'}
    }

    private fun debugGrid(grid: Map<Point3D, Char>) {
        val minZ = grid.keys.minBy { it.z }!!.z
        val maxZ = grid.keys.maxBy { it.z }!!.z

        (minZ .. maxZ).forEach { zLevel ->
            val slice = grid.keys.filter { it.z == zLevel }
            val minX = slice.minBy { it.x }!!.x
            val maxX = slice.maxBy { it.x }!!.x
            val minY = slice.minBy { it.y }!!.y
            val maxY = slice.maxBy { it.y }!!.y

            val jur: String = (minY .. maxY).map { y ->
                (minX .. maxX).map { x ->
                    grid[Point3D(x, y, zLevel)] ?: '.'
                }.joinToString("")
            }.joinToString("\n")

            if (jur.any { it == '#' }) {
                println("Depth $zLevel")
                println(jur)
                println()
            }
        }
    }

    data class Point3D(val x: Int, val y: Int, val z: Int) {
        fun neighbors(): Set<Point3D> {
            return up() + down() + left() + right()
        }

        private fun up(): Set<Point3D> {
            val new = this.copy(y = y - 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = it, y = 4, z = z + 1) }.toSet()
            }

            if (new.y == -1) {
                return setOf(Point3D(x = 2, y = 1, z = z - 1))
            }

            return setOf(new)
        }

        private fun down(): Set<Point3D> {
            val new = this.copy(y = y + 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = it, y = 0, z = z + 1) }.toSet()
            }

            if (new.y == 5) {
                return setOf(Point3D(x = 2, y = 3, z = z - 1))
            }

            return setOf(new)
        }

        private fun left(): Set<Point3D> {
            val new = this.copy(x = x - 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = 4, y = it, z = z + 1) }.toSet()
            }

            if (new.x == -1) {
                return setOf(Point3D(x=1, y=2, z=z-1))
            }

            return setOf(new)
        }

        private fun right(): Set<Point3D> {
            val new = this.copy(x = x + 1)
            val range: IntRange = (0 .. 4)

            // Center point
            if (new.x == 2 && new.y == 2) {
                return range.map { Point3D(x = 0, y = it, z = z + 1) }.toSet()
            }

            if (new.x == 5) {
                return setOf(Point3D(x = 3, y = 2, z = z - 1))
            }

            return setOf(new)
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
        }.toMap().toMutableMap()

        grid.remove(Point3D(2, 2, 0))
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

