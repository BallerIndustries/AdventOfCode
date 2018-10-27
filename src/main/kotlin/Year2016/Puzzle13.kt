package Year2016

import java.lang.RuntimeException

class Puzzle13 {
    fun getTile(x: Int, y: Int, favouriteNumber: Int): Tile {
        val numberOfOnes = ((x*x + 3*x + 2*x*y + y + y*y) + favouriteNumber)
                .toString(2)
                .count { it == '1' }

        return if (numberOfOnes % 2 == 0) Tile.OPEN_SPACE else Tile.WALL
    }

    fun generateFloorPlan(width: Int, height: Int, favouriteNumber: Int): FloorPlan {
        val state: List<List<Tile>> = (0 until height).map { y ->
            (0 until width).map { x ->
               getTile(x, y, favouriteNumber)
            }
        }

        return FloorPlan(state)
    }

    class FloorPlan(private val state: List<List<Tile>>) {
        override fun toString(): String {
            return state.map { row -> row.map { it.toString() }.joinToString("") }.joinToString("\n")
        }

        fun countSteps(x1: Int, y1: Int, x2: Int, y2: Int): Int {
            val path = findPath(x1, y1, x2, y2)
            return if (path == null) 0 else path.size - 1
        }

        fun progressAlong(history: List<Pair<Int, Int>>, endX: Int, endY: Int): List<List<Pair<Int, Int>>> {
            val (x, y) = history.last()

            // Already where we want to be
            if (x == endX && y == endY) {
                return emptyList()
            }
            else {
                val possibleNextMoves = getNextPossibleMoves(x, y, history)
                return possibleNextMoves.map { possibleMove -> history + possibleMove }
            }
        }

        fun findPath(x1: Int, y1: Int, x2: Int, y2: Int): List<Pair<Int, Int>>? {
            if (!isOpenAndInBounds(x1, y1) || !isOpenAndInBounds(x2, y2)) throw RuntimeException("There ain't no path. from/to coords are not open")

            val T0Paths = listOf(listOf(Pair(x1, y1)))
            val T1Paths = T0Paths.flatMap { progressAlong(it, x2, y2) }

            val allPaths = mutableListOf(T0Paths, T1Paths)

            while (allPaths[allPaths.size - 2] != allPaths.last()) {
                allPaths.add(allPaths.last().flatMap { progressAlong(it, x2, y2) })
            }

            return allPaths.flatten()
                    .filter { it.last() == Pair(x2, y2) }
                    .sortedBy { it.size }
                    .first()
        }

        private fun getNextPossibleMoves(x: Int, y: Int, history: List<Pair<Int, Int>>): Set<Pair<Int, Int>> {
            fun canMoveToPoint(x: Int, y: Int, history: List<Pair<Int, Int>>): Pair<Int, Int>? {
                val point = Pair(x, y)
                return if (isOpenAndInBounds(x, y) && !history.contains(point)) point else null
            }

            return listOfNotNull(
                canMoveToPoint(x - 1, y, history),
                canMoveToPoint(x + 1, y, history),
                canMoveToPoint(x, y - 1, history),
                canMoveToPoint(x, y + 1, history)
            ).toSet()
        }

        private fun isOpenAndInBounds(x: Int, y: Int) = isInBounds(x, y) && isOpen(x, y)

        private fun isInBounds(x: Int, y: Int): Boolean {
            return x >= 0 && y >= 0 && y < state.size && x < state[0].size
        }

        private fun isOpen(x: Int, y: Int): Boolean {
            return state[y][x] == Tile.OPEN_SPACE
        }

        fun countUniqueLocationsInFiftySteps(x: Int, y: Int): Int {
            if (!isOpenAndInBounds(x, y)) throw RuntimeException("There ain't no path. from/to coords are not open")

            val T0Paths = listOf(listOf(Pair(x, y)))
            val allPaths = mutableListOf(T0Paths)

            // Do 49 more steps
            (1 .. 50).forEach { _ ->
                allPaths.add(allPaths.last().flatMap { progressAlong(it, Int.MAX_VALUE, Int.MAX_VALUE) })
            }

            val uniqueLocations: Set<Pair<Int, Int>> = allPaths.flatten().flatten().toSet()
            return uniqueLocations.size
        }
    }

    enum class Tile {
        OPEN_SPACE, WALL;

        override fun toString(): String {
            return if (this == OPEN_SPACE) "." else "#"
        }
    }
}