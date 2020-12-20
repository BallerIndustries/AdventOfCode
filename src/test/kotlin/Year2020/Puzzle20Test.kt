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
        fun delta(xDelta: Int, yDelta: Int) = this.copy(x = this.x + xDelta, y = this.y + yDelta)

        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun left() = this.copy(x = this.x - 1)
        fun right() = this.copy(x = this.x + 1)

        fun rotate(angle: Int): Point {
            val radians = Math.toRadians(angle.toDouble())
            val s = Math.sin(radians)
            val c = Math.cos(radians)

            var thisX = this.x.toDouble()
            var thisY = this.y.toDouble()

            // translate point back to origin:
            thisX -= 4.5
            thisY -= 4.5

            // rotate point
            // rotate point
            val xNew = Math.round(((thisX * c) - (thisY * s)) + 4.5)
            val yNew = Math.round(((thisX * s) + (thisY * c)) + 4.5)

            return Point(xNew.toInt(), yNew.toInt())
        }


        fun neighbors(): List<Point> {
            val range = (-1 .. 1)

            return range.flatMap { x ->
                range.map { y ->
                    Point(this.x + x, this.y + y)
                }
            } - this
        }
    }

    data class Tile(val tileId: Int, val grid: Map<Point, Char>) {
        fun allConfigurations(): List<Tile> {
            return listOf(
                this,
                this.rotate90(),
                this.rotate180(),
                this.rotate270(),

                this.horizontallyFlipped(),
                this.horizontallyFlipped().rotate90(),
                this.horizontallyFlipped().rotate180(),
                this.horizontallyFlipped().rotate270(),
            )
        }

        fun horizontallyFlipped(): Tile {
            val flippedGrid = grid.entries.associate { (point, char) ->
                val newX = 9 - point.x
                point.copy(x = newX) to char
            }

            return this.copy(grid = flippedGrid).validateGrid()
        }

        fun rotate90(): Tile {
            val rotatedGrid = grid.entries.associate { (point, char) ->
                point.rotate(90) to char
            }

            return this.copy(grid = rotatedGrid).validateGrid()
        }

        fun rotate180(): Tile {
            val rotatedGrid = grid.entries.associate { (point, char) ->
                point.rotate(180) to char
            }

            return this.copy(grid = rotatedGrid).validateGrid()
        }

        fun rotate270(): Tile {
            val rotatedGrid = grid.entries.associate { (point, char) ->
                point.rotate(270) to char
            }

            return this.copy(grid = rotatedGrid).validateGrid()
        }

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

        fun withoutBorder(): Tile {
            val gridWithoutBorder = grid.entries.filter { (key, value) ->
                key.x != 0 && key.x != 9 && key.y != 0 && key.y != 9
            }.associate { it.key to it.value }

            return this.copy(grid = gridWithoutBorder)
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
        val joinedTiles = joinTiles(puzzleText).entries.associate { (k, v) -> k to v.withoutBorder() }

        val minX = joinedTiles.keys.minBy { it.x }!!.x
        val maxX = joinedTiles.keys.maxBy { it.x }!!.x
        val minY = joinedTiles.keys.minBy { it.y }!!.y
        val maxY = joinedTiles.keys.maxBy { it.y }!!.y
        val finalMap = mutableMapOf<Point, Char>()

        (minY .. maxY).forEach { tileY ->
            (minX .. maxX).forEach { tileX ->

                val point = Point(tileX, tileY)
                val tile = joinedTiles[point]!!

                (1 .. 8).forEach { x ->
                    (1 .. 8).forEach { y ->

                        val finalX = ((tileX - minX) * 8) + (x - 1)
                        val finalY = ((tileY - minY) * 8) + (y - 1)
                        val char = tile.grid[Point(x, y)]!!

                        finalMap[Point(finalX, finalY)] = char
                    }
                }
            }
        }

        if (finalMap.size != joinedTiles.size * 64) {
            throw RuntimeException()
        }

        return countSeaMonsters(finalMap)
    }

    private fun countSeaMonsters(finalMap: Map<Point, Char>): Int {
        val minX = finalMap.keys.minBy { it.x }!!.x
        val maxX = finalMap.keys.maxBy { it.x }!!.x
        val minY = finalMap.keys.minBy { it.y }!!.y
        val maxY = finalMap.keys.maxBy { it.y }!!.y

        val seaMonster =
            "                  # \n" +
            "#    ##    ##    ###\n" +
            " #  #  #  #  #  #   "

        fun hasSeaMonster(finalMap: Map<Point, Char>, point: Point): Boolean {
            return listOf(
                finalMap[point],
                finalMap[point.delta(1, 1)],
                finalMap[point.delta(4, 1)],
                finalMap[point.delta(5, 0)],
                finalMap[point.delta(6, 0)],

                finalMap[point.delta(7, 1)],
                finalMap[point.delta(10, 1)],
                finalMap[point.delta(11, 0)],
                finalMap[point.delta(12, 0)],
                finalMap[point.delta(13, 1)],

                finalMap[point.delta(16, 1)],
                finalMap[point.delta(17, 0)],
                finalMap[point.delta(18, 0)],
                finalMap[point.delta(18, -1)],
                finalMap[point.delta(19, 0)],
            ).all { it == '#' }
        }

        var count = 0

        (minY .. maxY).forEach { y ->
            (minX .. maxX).forEach { x ->
                if (hasSeaMonster(finalMap, Point(x, y))) {
                    count++
                }
            }
        }

        return count
    }

    private fun joinTiles(puzzleText: String): Map<Point, Tile> {
        val tiles = parseTiles(puzzleText).toMutableList()
        val initialSize = tiles.size
        val joinedTiles = mutableMapOf(Point(0, 0) to tiles[0])
        val frontier = mutableListOf(tiles[0])
        tiles.removeAt(0)

        while (joinedTiles.size < initialSize) {
            //println("frontier = ${frontier.map { it.tileId }}")
            //println("joinedTiles = ${joinedTiles.values.map { it.tileId }}")
            if (joinedTiles.size + tiles.size != initialSize) {
                throw RuntimeException()
            }

            val currentTile = frontier.removeAt(0)
            val currentTilePoint = joinedTiles.entries.find { it.value.tileId == currentTile.tileId }!!.key

            // TOP PATTERN
            val topTile = getTileMatchingThisTop(tiles, currentTile)

            if (topTile != null) {
                tiles.removeIf { it.tileId == topTile.tileId }
                frontier.add(topTile)
                joinedTiles[currentTilePoint.up()] = topTile
                println("Added tile ${topTile.tileId} to frontier and joinedTiles")
            }

            // BOTTOM PATTERN
            val bottomTile = getTileMatchingThisBottom(tiles, currentTile)

            if (bottomTile != null) {
                tiles.removeIf { it.tileId == bottomTile.tileId }
                frontier.add(bottomTile)
                joinedTiles[currentTilePoint.down()] = bottomTile
                println("Added tile ${bottomTile.tileId} to frontier and joinedTiles")
            }

            // LEFT PATTERN
            val leftTile = getTileMatchingThisLeft(tiles, currentTile)

            if (leftTile != null) {
                tiles.removeIf { it.tileId == leftTile.tileId }
                frontier.add(leftTile)
                joinedTiles[currentTilePoint.left()] = leftTile
                println("Added tile ${leftTile.tileId} to frontier and joinedTiles")
            }

            // RIGHT PATTERN
            val rightTile = getTileMatchingThisRight(tiles, currentTile)

            if (rightTile != null) {
                tiles.removeIf { it.tileId == rightTile.tileId }
                frontier.add(rightTile)
                joinedTiles[currentTilePoint.right()] = rightTile
                println("Added tile ${rightTile.tileId} to frontier and joinedTiles")
            }
        }
        return joinedTiles
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

    fun getTileMatchingThisTop(tiles: List<Tile>, originalTile: Tile): Tile? {
        val topPattern = originalTile.topPattern()

        for (tile in tiles) {
            if (tile.tileId == originalTile.tileId) {
                continue
            }

            for (configuration in tile.allConfigurations()) {
                if (topPattern == configuration.bottomPattern()) {
                    return configuration
                }
            }
        }

        return null
    }

    fun getTileMatchingThisBottom(tiles: List<Tile>, originalTile: Tile): Tile? {
        val bottomPattern = originalTile.bottomPattern()

        for (tile in tiles) {
            if (tile.tileId == originalTile.tileId) {
                continue
            }

            for (configuration in tile.allConfigurations()) {
                if (bottomPattern == configuration.topPattern()) {
                    return configuration
                }
            }
        }

        return null
    }

    fun getTileMatchingThisLeft(tiles: List<Tile>, originalTile: Tile): Tile? {
        val leftPattern = originalTile.leftPattern()

        for (tile in tiles) {
            if (tile.tileId == originalTile.tileId) {
                continue
            }

            for (configuration in tile.allConfigurations()) {
                if (leftPattern == configuration.rightPattern()) {
                    return configuration
                }
            }
        }

        return null
    }

    fun getTileMatchingThisRight(tiles: List<Tile>, originalTile: Tile): Tile? {
        val rightPattern = originalTile.rightPattern()

        for (tile in tiles) {
            if (tile.tileId == originalTile.tileId) {
                continue
            }

            for (configuration in tile.allConfigurations()) {
                if (rightPattern == configuration.leftPattern()) {
                    return configuration
                }
            }
        }

        return null
    }




//    fun getCorrespondingTile(tiles: List<Tile>, originalTile: Tile, pattern: String): Tile? {
//        val patternNumber = patternToInt(pattern)
//
//        for (tile in tiles) {
//            if (tile.tileId == originalTile.tileId) {
//                continue
//            }
//
//            if (patternNumber in tile.allEdgesAsBinary()) {
//                return tile
//            }
//        }
//
//        return null
//    }

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