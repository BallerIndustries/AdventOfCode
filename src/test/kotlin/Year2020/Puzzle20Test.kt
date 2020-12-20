package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle20Test {
    val exampleText =
        "Tile 2311:\n" +
        "..##.#..#.\n" +
        "##..#.....\n" +
        "#...##..#.\n" +
        "####.#...#\n" +
        "##.##.###.\n" +
        "##...#.###\n" +
        ".#.#.#..##\n" +
        "..#....#..\n" +
        "###...#.#.\n" +
        "..###..###\n" +
        "\n" +
        "Tile 1951:\n" +
        "#.##...##.\n" +
        "#.####...#\n" +
        ".....#..##\n" +
        "#...######\n" +
        ".##.#....#\n" +
        ".###.#####\n" +
        "###.##.##.\n" +
        ".###....#.\n" +
        "..#.#..#.#\n" +
        "#...##.#..\n" +
        "\n" +
        "Tile 1171:\n" +
        "####...##.\n" +
        "#..##.#..#\n" +
        "##.#..#.#.\n" +
        ".###.####.\n" +
        "..###.####\n" +
        ".##....##.\n" +
        ".#...####.\n" +
        "#.##.####.\n" +
        "####..#...\n" +
        ".....##...\n" +
        "\n" +
        "Tile 1427:\n" +
        "###.##.#..\n" +
        ".#..#.##..\n" +
        ".#.##.#..#\n" +
        "#.#.#.##.#\n" +
        "....#...##\n" +
        "...##..##.\n" +
        "...#.#####\n" +
        ".#.####.#.\n" +
        "..#..###.#\n" +
        "..##.#..#.\n" +
        "\n" +
        "Tile 1489:\n" +
        "##.#.#....\n" +
        "..##...#..\n" +
        ".##..##...\n" +
        "..#...#...\n" +
        "#####...#.\n" +
        "#..#.#.#.#\n" +
        "...#.#.#..\n" +
        "##.#...##.\n" +
        "..##.##.##\n" +
        "###.##.#..\n" +
        "\n" +
        "Tile 2473:\n" +
        "#....####.\n" +
        "#..#.##...\n" +
        "#.##..#...\n" +
        "######.#.#\n" +
        ".#...#.#.#\n" +
        ".#########\n" +
        ".###.#..#.\n" +
        "########.#\n" +
        "##...##.#.\n" +
        "..###.#.#.\n" +
        "\n" +
        "Tile 2971:\n" +
        "..#.#....#\n" +
        "#...###...\n" +
        "#.#.###...\n" +
        "##.##..#..\n" +
        ".#####..##\n" +
        ".#..####.#\n" +
        "#..#.#..#.\n" +
        "..####.###\n" +
        "..#.#.###.\n" +
        "...#.#.#.#\n" +
        "\n" +
        "Tile 2729:\n" +
        "...#.#.#.#\n" +
        "####.#....\n" +
        "..#.#.....\n" +
        "....#..#.#\n" +
        ".##..##.#.\n" +
        ".#.####...\n" +
        "####.#.#..\n" +
        "##.####...\n" +
        "##..#.##..\n" +
        "#.##...##.\n" +
        "\n" +
        "Tile 3079:\n" +
        "#.#.#####.\n" +
        ".#..######\n" +
        "..#.......\n" +
        "######....\n" +
        "####.#..#.\n" +
        ".#...#.##.\n" +
        "#.#####.##\n" +
        "..#.###...\n" +
        "..#.......\n" +
        "..#.###..."

    val puzzleText = this::class.java.getResource("/2020/puzzle20.txt").readText().replace("\r", "")
    val puzzle = Puzzle20()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(8425574315321, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }

    @Test
    fun `there should be TWO matches for this pattern`() {
        val tiles = puzzle.parseTiles(exampleText)
        assertEquals(2, puzzle.countPatternOccurrences(tiles, "..##.#..#."))
        assertEquals(1, puzzle.countPatternOccurrences(tiles, "..###..###"))
        assertEquals(1, puzzle.countPatternOccurrences(tiles, "#...##.#.."))
        assertEquals(2, puzzle.countPatternOccurrences(tiles, "..#.###..."))
    }

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(20899048083289, result)
    }

    @Test
    fun `example part b`() {
        val result = puzzle.solveTwo(exampleText)
        assertEquals(273, result)
    }
}

class Puzzle20 {

    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun left() = this.copy(x = this.x - 1)
        fun right() = this.copy(x = this.x + 1)
    }

    data class Tile(val tileId: Int, val grid: Map<Point, Char>) {
        fun allEdgesAsBinary(): Set<Int> {
            return listOf(
                topPattern(),
                bottomPattern(),
                leftPattern(),
                rightPattern(),
                topPattern().reversed(),
                bottomPattern().reversed(),
                leftPattern().reversed(),
                rightPattern().reversed()
            ).map { patternToInt(it) }.toSet()
        }

        fun validateGrid(): Tile {
            val invalidPoints = grid.keys.filter { it.x !in 0 until 10 || it.y !in 0 until 10 }

            if (grid.count() != 100) {
                throw RuntimeException("Grid size = ${grid.count()} is not 100 for tileId = $tileId")
            }

            if (invalidPoints.size > 0) {
                throw RuntimeException("Grid has points values outside of 0-9")
            }

            return this
        }

        fun topPattern(): String {
            return (0 until 10).map { x ->
                val point = Point(x, 0)
                grid[point]!!
            }.joinToString("")
        }

        fun bottomPattern(): String {
            return (0 until 10).map { x ->
                val point = Point(x, 9)
                grid[point]!!
            }.joinToString("")
        }

        fun leftPattern(): String {
            return (0 until 10).map { y ->
                val point = Point(0, y)
                grid[point] ?:
                    throw RuntimeException()
            }.joinToString("")
        }

        fun rightPattern(): String {
            return (0 until 10).map { y ->
                val point = Point(9, y)
                grid[point] ?:
                    throw RuntimeException()
            }.joinToString("")
        }
    }

    fun solveOne(puzzleText: String): Long {
        val tiles = parseTiles(puzzleText)
        val groups = tiles.groupBy { countMatchingEdges(tiles, it) }
        return groups[2]!!.fold(1L) { acc, tile -> acc * tile.tileId }
    }

    private fun countMatchingEdges(tiles: List<Tile>, thisTile: Tile): Int {
        val otherTiles = tiles.filter { it.tileId != thisTile.tileId }

        return countPatternOccurrences(otherTiles, thisTile.topPattern()) +
                countPatternOccurrences(otherTiles, thisTile.bottomPattern()) +
                countPatternOccurrences(otherTiles, thisTile.leftPattern()) +
                countPatternOccurrences(otherTiles, thisTile.rightPattern())
    }

    fun parseTiles(puzzleText: String): List<Tile> {
        return puzzleText.split("\n\n").mapIndexed { index, jur ->
            val tmp = jur.split("\n")
            val tileId = tmp[0].replace("Tile ", "").replace(":", "").toInt()
            val grid = parseGrid(tmp.subList(1, tmp.size).joinToString("\n"))

            Tile(tileId, grid).validateGrid()
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val tiles = parseTiles(puzzleText)
        val joinedTiles = mutableMapOf(Point(0, 0) to tiles[0])
        val frontier = mutableListOf(tiles[0])

        while (joinedTiles.size < tiles.size) {
            //println("frontier = ${frontier.map { it.tileId }}")
            //println("joinedTiles = ${joinedTiles.values.map { it.tileId }}")
            val currentTile = frontier.removeAt(0)
            val currentTilePoint = joinedTiles.entries.find { it.value.tileId == currentTile.tileId }!!.key

            // TOP PATTERN
            val topPattern = currentTile.topPattern()
            val topTile = getCorrespondingTile(tiles, currentTile, topPattern)

            if (topTile != null) {
                frontier.add(topTile)
                joinedTiles[currentTilePoint.up()] = topTile
                println("Added tile ${topTile.tileId} to frontier and joinedTiles")
            }

            // BOTTOM PATTERN
            val bottomPattern = currentTile.bottomPattern()
            val bottomTile = getCorrespondingTile(tiles, currentTile, bottomPattern)

            if (bottomTile != null) {
                frontier.add(bottomTile)
                joinedTiles[currentTilePoint.down()] = bottomTile
                println("Added tile ${bottomTile.tileId} to frontier and joinedTiles")
            }

            // LEFT PATTERN
            val leftPattern = currentTile.leftPattern()
            val leftTile = getCorrespondingTile(tiles, currentTile, leftPattern)

            if (leftTile != null) {
                frontier.add(leftTile)
                joinedTiles[currentTilePoint.left()] = leftTile
                println("Added tile ${leftTile.tileId} to frontier and joinedTiles")
            }

            // RIGHT PATTERN
            val rightPattern = currentTile.rightPattern()
            val rightTile = getCorrespondingTile(tiles, currentTile, rightPattern)

            if (rightTile != null) {
                frontier.add(rightTile)
                joinedTiles[currentTilePoint.right()] = rightTile
                println("Added tile ${rightTile.tileId} to frontier and joinedTiles")
            }
        }

        return 1
    }

    private fun parseGrid(puzzleText: String): Map<Point, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines.first().count()

        return (0 until width).flatMap { x ->
            (0 until height).map { y ->

                val p = Point(x, y)
                val c = lines[y][x]

                p to c
            }
        }.toMap()
    }

    fun getCorrespondingTile(tiles: List<Tile>, originalTile: Tile, pattern: String): Tile? {
        val patternNumber = patternToInt(pattern)

        for (tile in tiles) {
            if (tile.tileId == originalTile.tileId) {
                continue
            }

            if (patternNumber in tile.allEdgesAsBinary()) {
                return tile
            }
        }

        return null
    }

    fun countPatternOccurrences(tiles: List<Tile>, pattern: String): Int {
        val patternNumber = patternToInt(pattern)
        var count = 0

        for (tile in tiles) {
            if (patternNumber in tile.allEdgesAsBinary()) {
                count++
            }
        }

        return count
    }
}

fun patternToInt(pattern: String) = pattern.replace("#", "0").replace(".", "1").toInt(2)