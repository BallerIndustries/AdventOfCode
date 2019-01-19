package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.IndexOutOfBoundsException

class Puzzle22Test {
    val puzzle = Puzzle22()
    val puzzleText = this::class.java.getResource("/2017/puzzle22.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(6722, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(7620, result)
    }
}

class Puzzle22 {
    data class Point(val x: Int, val y: Int)

    fun solveOne(puzzleText: String): Int {
        val grid = parseGrid(puzzleText).toMutableMap()
        println(grid)

        val width = grid.keys.maxBy { it.x }!!.x
        val height = grid.keys.maxBy { it.y }!!.y
        
        var currentPoint = Point(width / 2, height / 2)
        var direction = Direction.UP
        
        (0 until 10000).forEach {
            
            // toggle whether char is or is not infected
            
            if (grid[currentPoint] == '#') {
                direction =direction.right()
                
            }
            else {
                direction = direction.left()
                
            }
            
            currentPoint = currentPoint.move(direction)
            
        }


        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun parseGrid(puzzleText: String): Map<Point, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines.first().count()

        val grid = (0 until width).flatMap { x ->
            (0 until height).map { y ->

                val p = Point(x, y)
                val c = lines[y][x]

                p to c
            }
        }.toMap()
        return grid
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
