package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle12Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle12.txt").readText().replace("\r", "")
    val puzzle = Puzzle12()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1007, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(41212, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "F10\n" +
                "N3\n" +
                "F7\n" +
                "R90\n" +
                "F11"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(25, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "F10\n" +
                "N3\n" +
                "F7\n" +
                "R90\n" +
                "F11"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(286, result)
    }

    @Test
    fun aaa() {
        val point = Puzzle12.Point(12, -5)
        assertEquals(point.rotate(90), point.rotate(-270))
    }
}

class Puzzle12 {
    enum class Direction {
        NORTH, WEST, EAST, SOUTH;

        private fun left(): Direction {
            return when (this) {
                NORTH -> WEST
                WEST -> SOUTH
                SOUTH -> EAST
                EAST -> NORTH


            }
        }

        private fun right(): Direction {
            return when (this) {
                NORTH -> EAST
                EAST -> SOUTH
                SOUTH -> WEST
                WEST -> NORTH
            }
        }

        fun left(degrees: Int): Direction {
            var current = degrees
            var facing = this

            while (current > 0) {
                facing = facing.left()
                current -= 90
            }

            return facing
        }

        fun right(degrees: Int): Direction {
            var current = degrees
            var facing = this

            while (current > 0) {
                facing = facing.right()
                current -= 90
            }

            return facing
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun move(units: Int, direction: Direction): Point {
            return when (direction) {
                Direction.NORTH -> this.copy(y = this.y - units)
                Direction.SOUTH -> this.copy(y = this.y + units)
                Direction.WEST -> this.copy(x = this.x - units)
                Direction.EAST -> this.copy(x = this.x + units)
            }
        }

        fun move(x: Int, y: Int): Point {
            return this.copy(x = this.x + x, y = this.y + y)
        }

        fun multiply(value: Int): Point {
            return this.copy(x = this.x * value, y = this.y * value)
        }

        fun rotate(angle: Int): Point {
            val radians = Math.toRadians(angle.toDouble())
            val s = Math.sin(radians)
            val c = Math.cos(radians)

            // rotate point
            // rotate point
            val xnew = Math.round((this.x * c) - (this.y * s))
            val ynew = Math.round((this.x * s) + (this.y * c))

            return Point(xnew.toInt(), ynew.toInt())
        }
    }

    fun solveOne(puzzleText: String): Int {
        var pos = Point(0, 0)
        var facing = Direction.EAST

        puzzleText.split("\n").map { line ->
            val command = line[0]
            val units = line.substring(1).toInt()

            when (command) {
                'N' -> pos = pos.move(units, Direction.NORTH)
                'S' -> pos = pos.move(units, Direction.SOUTH)
                'E' -> pos = pos.move(units, Direction.EAST)
                'W' -> pos = pos.move(units, Direction.WEST)
                'L' -> facing = facing.left(units)
                'R' -> facing = facing.right(units)
                'F' -> {
                    pos = pos.move(units, facing)
                }
                else -> throw RuntimeException()
            }

        }

        return Math.abs(pos.x) + Math.abs(pos.y)
    }

    fun solveTwo(puzzleText: String): Int {
        var pos = Point(0, 0)
        var waypoint = Point(10, -1)

        puzzleText.split("\n").map { line ->
            val command = line[0]
            val units = line.substring(1).toInt()

            when (command) {
                'N' -> waypoint = waypoint.move(units, Direction.NORTH)
                'S' -> waypoint = waypoint.move(units, Direction.SOUTH)
                'E' -> waypoint = waypoint.move(units, Direction.EAST)
                'W' -> waypoint = waypoint.move(units, Direction.WEST)
                'L' -> waypoint = waypoint.rotate(360 - units)
                'R' -> waypoint = waypoint.rotate(units)
                'F' -> {
                    val delta = waypoint.multiply(units)
                    pos = pos.move(delta.x, delta.y)
                }
                else -> throw RuntimeException()
            }

            println("line     = $line")
            println("ship     = $pos")
            println("waypoint = $waypoint")
            println()
        }

        return Math.abs(pos.x) + Math.abs(pos.y)
    }
}

