package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle8Test {

    val puzzle = Puzzle8()

    @Test
    fun `initial board should be a 50x6 pixels`() {
        assertEquals(puzzle.toString(),"..................................................\n..................................................\n..................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `should be able to set a 3x2 rect command`() {
        puzzle.sendCommand("rect 3x2")
        assertEquals(puzzle.toString(),"###...............................................\n###...............................................\n..................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `set rect and then rotate column`() {
        puzzle.sendCommand("rect 3x2")
        puzzle.sendCommand("rotate column x=1 by 1")
        assertEquals(puzzle.toString(),"#.#...............................................\n###...............................................\n.#................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `set rect, rotate column and then rotate row`() {
        puzzle.sendCommand("rect 3x2")
        puzzle.sendCommand("rotate column x=1 by 1")
        puzzle.sendCommand("rotate row y=0 by 4")
        assertEquals(puzzle.toString(),"....#.#...........................................\n###...............................................\n.#................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `should be able to set a 1x1 rect command`() {
        puzzle.sendCommand("rect 1x1")
        assertEquals(puzzle.toString(),"#.................................................\n..................................................\n..................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `make 1x1 rect then rotate row y=0 by 4`() {
        puzzle.sendCommand("rect 1x1")
        puzzle.sendCommand("rotate row y=0 by 4")
        assertEquals(puzzle.toString(),"....#.............................................\n..................................................\n..................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `make 1x1 rect then rotate row y=0 by 50`() {
        puzzle.sendCommand("rect 1x1")
        puzzle.sendCommand("rotate row y=0 by 50")
        assertEquals(puzzle.toString(),"#.................................................\n..................................................\n..................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `make 1x1 rect then rotate row y=0 by 51`() {
        puzzle.sendCommand("rect 1x1")
        puzzle.sendCommand("rotate row y=0 by 51")
        assertEquals(puzzle.toString(),".#................................................\n..................................................\n..................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `make a 1x1 rect and then rotate column x=0 by 1`() {
        puzzle.sendCommand("rect 1x1")
        puzzle.sendCommand("rotate column x=0 by 1")
        assertEquals(puzzle.toString(),"..................................................\n#.................................................\n..................................................\n..................................................\n..................................................\n..................................................")
    }

    @Test
    fun `puzzle part a`() {
        val commands = Puzzle1Test::class.java.getResource("/2016/puzzle8.txt").readText().split(System.lineSeparator())
        commands.forEach { puzzle.sendCommand(it) }
        assertEquals(puzzle.countLitPixels(), 119)
    }

    @Test
    fun `puzzle part b`() {
        val commands = Puzzle1Test::class.java.getResource("/2016/puzzle8.txt").readText().split(System.lineSeparator())
        commands.forEach { puzzle.sendCommand(it) }
        assertEquals(puzzle.toString(),
                "####.####.#..#.####..###.####..##...##..###...##..\n" +
                "...#.#....#..#.#....#....#....#..#.#..#.#..#.#..#.\n" +
                "..#..###..####.###..#....###..#..#.#....#..#.#..#.\n" +
                ".#...#....#..#.#.....##..#....#..#.#.##.###..#..#.\n" +
                "#....#....#..#.#.......#.#....#..#.#..#.#....#..#.\n" +
                "####.#....#..#.#....###..#.....##...###.#.....##..")
    }
}

