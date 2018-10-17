package Year2016

data class Point(val x: Int, val y: Int)

class Puzzle1 {
    fun countBlocks(input: String): Int {
        var direction = 0
        var currentX = 0
        var currentY = 0
        val moves = input.split(", ")

        moves.forEach { move ->
            direction = determineDirection(move, direction)
            val steps = move.substring(1).toInt()

            when (direction) {
                0 -> currentY += steps
                1 -> currentX += steps
                2 -> currentY -= steps
                3 -> currentX -= steps
                else -> throw RuntimeException("Direction is supposed to be NORTH/EAST/SOUTH/WEST. Expected it to be between 0-3")
            }
        }

        return Math.abs(currentX) + Math.abs(currentY)
    }

    fun findFirstPlaceVisitedTwice(input: String): Int {
        var direction = 0
        var currentPosition = Point(0, 0)
        val moves = input.split(", ")
        val history = mutableSetOf(Point(0, 0))

        moves.forEach { move ->
            direction = determineDirection(move, direction)
            val steps = move.substring(1).toInt()
            val nextPosition = getNextPosition(currentPosition, direction, steps)

            // Walk along, see if you've been to a point before.
            positionsFromHereToThere(currentPosition, nextPosition).forEach { position ->
                // Been here before bro!
                if (history.contains(position)) {
                    return Math.abs(position.x) + Math.abs(position.y)
                }

                history.add(position)
            }

            currentPosition = nextPosition
        }

        throw RuntimeException("Looks like we didn't visit any one place twice")
    }

    fun positionsFromHereToThere(currentPosition: Point, nextPosition: Point): List<Point> {
        val positions = if (currentPosition.x == nextPosition.x) {
            if (currentPosition.y < nextPosition.y) (currentPosition.y .. nextPosition.y).map { Point(currentPosition.x, it) }
            else (currentPosition.y downTo nextPosition.y).map { Point(currentPosition.x, it) }
        }
        else if (currentPosition.y == nextPosition.y) {
            if (currentPosition.x < nextPosition.x) (currentPosition.x .. nextPosition.x).map { Point(it, currentPosition.y) }
            else (currentPosition.x downTo nextPosition.x).map { Point(it, currentPosition.y) }
        }
        else {
            throw RuntimeException("Hey! I was expecting it to be a horizontal or vertical line!")
        }

        return positions.subList(1, positions.size)
    }

    private fun getNextPosition(position: Point, direction: Int, steps: Int): Point {
        return when (direction) {
            0 -> Point(position.x, position.y + steps)
            1 -> Point(position.x + steps, position.y)
            2 -> Point(position.x, position.y - steps)
            3 -> Point(position.x - steps, position.y)
            else -> throw RuntimeException("Direction is supposed to be NORTH/EAST/SOUTH/WEST. Expected it to be between 0-3")
        }
    }

    private fun determineDirection(move: String, direction: Int): Int {
        var newDirection = direction
        if (move[0] == 'R') {
            newDirection++
        } else if (move[0] == 'L') {
            newDirection--
        }

        if (newDirection < 0) newDirection = 3
        if (newDirection > 3) newDirection = 0
        return newDirection
    }


}