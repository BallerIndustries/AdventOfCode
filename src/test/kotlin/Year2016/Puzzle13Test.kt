package Year2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {

    val puzzle = Puzzle13()

    @Test
    fun `coord 0,0 should be an open space`() {
        val state = puzzle.getTile(0, 0, 10)
        assertEquals(state, Puzzle13.Tile.OPEN_SPACE)
    }

    @Test
    fun `coord 1,0 should be a wall`() {
        val state = puzzle.getTile(1, 0, 10)
        assertEquals(state, Puzzle13.Tile.WALL)
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

        val board = puzzle.generateFloorPlan(10, 7, 10)
        assertEquals(expectedBoard, board.toString())
    }

    @Test
    fun `should be able to find a path through 10x7 grid`() {
        val floorPlan = puzzle.generateFloorPlan(10, 7, 10)
        val path = floorPlan.findPath(1, 1, 7, 4)
        val expected = listOf(
            Pair(1, 1),
            Pair(1, 2),
            Pair(2, 2),
            Pair(3, 2),
            Pair(3, 3),
            Pair(3, 4),
            Pair(4, 4),
            Pair(4, 5),
            Pair(5, 5),
            Pair(6, 5),
            Pair(7, 5),
            Pair(7, 4)
        )

        assertEquals(expected, path)
    }

    @Test
    fun `should take eleven steps to go from 1,1, to 7,4`() {
        val floorPlan = puzzle.generateFloorPlan(10, 7, 10)
        val steps = floorPlan.countSteps(1, 1, 7, 4)
        assertEquals(11, steps)
    }

    @Test
    fun `show me a big board`() {
        println(puzzle.generateFloorPlan(40, 40, 1364).toString())
    }

    @Test
    fun `puzzle part a`() {
        val floorPlan = puzzle.generateFloorPlan(50, 50, 1364)
        val steps = floorPlan.countSteps(1, 1, 31, 39)
        assertEquals(86, steps)
    }

    @Test
    fun `puzzle part b`() {
        val floorPlan = puzzle.generateFloorPlan(1000, 1000, 1364)
        val uniqueLocations = floorPlan.countUniqueLocationsInFiftySteps(1, 1)
        assertEquals(127, uniqueLocations)
    }
}

