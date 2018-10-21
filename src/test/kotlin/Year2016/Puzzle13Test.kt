package Year2016

import org.junit.Assert
import org.junit.Test

class Puzzle13Test {

    val puzzle = Puzzle13()

    @Test
    fun `coord 0,0 should be an open space`() {
        val state = puzzle.getTile(0, 0, 10)
        Assert.assertEquals(state, Puzzle13.Tile.OPEN_SPACE)
    }

    @Test
    fun `coord 1,0 should be a wall`() {
        val state = puzzle.getTile(1, 0, 10)
        Assert.assertEquals(state, Puzzle13.Tile.WALL)
    }

    @Test
    fun `10x7 (fn=10) grid should look like this`() {
        val expectedBoard = """.#.####.##
..#..#...#
#....##...
###.#.###.
.##..#..#.
..##....#.
#...##.###"""

        val board = puzzle.generateBoard(10, 7, 10)
        Assert.assertEquals(expectedBoard, board.toString())
    }

    @Test
    fun `should be able to find a path through 10x7 grid`() {
        val board = puzzle.generateBoard(10, 7, 10)
        val path = board.findPath(1, 1, 7, 4)
        Assert.assertEquals(path, listOf(Pair(1, 1)))
    }

    @Test
    fun `show me a big board`() {
        println(puzzle.generateBoard(40, 40, 1364).toString())
    }
}

class Puzzle13 {
    fun getTile(x: Int, y: Int, favouriteNumber: Int): Tile {
        val numberOfOnes = ((x*x + 3*x + 2*x*y + y + y*y) + favouriteNumber)
                .toString(2)
                .count { it == '1' }

        return if (numberOfOnes % 2 == 0) Tile.OPEN_SPACE else Tile.WALL
    }

    fun generateBoard(width: Int, height: Int, favouriteNumber: Int): Board {
        val state: List<List<Tile>> = (0 until height).map { y ->
            (0 until width).map { x ->
               getTile(x, y, favouriteNumber)
            }
        }

        return Board(state)
    }

    class Board(val state: List<List<Tile>>) {
        override fun toString(): String {
            val dog: String = state.map { row -> row.map { it.toString() }.joinToString("") }.joinToString("\n")
            return dog
        }

        fun findPath(x1: Int, y1: Int, x2: Int, y2: Int): List<Pair<Int, Int>> {
            return listOf()
        }
    }

    enum class Tile {
        OPEN_SPACE, WALL;

        override fun toString(): String {
            return if (this == OPEN_SPACE) "." else "#"
        }
    }
}
