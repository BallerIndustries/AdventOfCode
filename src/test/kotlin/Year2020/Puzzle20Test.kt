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
//        assertEquals(2, puzzle.countPatternOccurrences(tiles, ""))
//        assertEquals(2, puzzle.countPatternOccurrences(tiles, ""))
//        assertEquals(2, puzzle.countPatternOccurrences(tiles, ""))
//        assertEquals(2, puzzle.countPatternOccurrences(tiles, ""))
//        assertEquals(2, puzzle.countPatternOccurrences(tiles, ""))
//        assertEquals(2, puzzle.countPatternOccurrences(tiles, ""))
//        assertEquals(2, puzzle.countPatternOccurrences(tiles, ""))
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

//        fun edgesAsBinary(): Set<Int> {
//            return listOf(
//                topPattern(),
//                bottomPattern(),
//                leftPattern(),
//                rightPattern(),
//            ).map { it.replace("#", "0").replace(".", "1").toInt(2) }.toSet()
//        }

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

                this.verticallyFlipped(),
                this.verticallyFlipped().rotate90(),
                this.verticallyFlipped().rotate180(),
                this.verticallyFlipped().rotate270(),

                this.verticallyFlipped().horizontallyFlipped(),
                this.verticallyFlipped().horizontallyFlipped().rotate90(),
                this.verticallyFlipped().horizontallyFlipped().rotate180(),
                this.verticallyFlipped().horizontallyFlipped().rotate270(),
            )
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

        fun horizontallyFlipped(): Tile {
            val flippedGrid = grid.entries.associate { (point, char) ->
                val newX = 9 - point.x
                point.copy(x = newX) to char
            }

            return this.copy(grid = flippedGrid).validateGrid()
        }

        fun verticallyFlipped(): Tile {
            val flippedGrid = grid.entries.associate { (point, char) ->
                val newY = 9 - point.y
                point.copy(y = newY) to char
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
        println(groups)
        // Should be
        //   4 tiles with 2 matches
        //  92 tiles with 3 matches
        // 191 tiles with 4 matches

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
        return 1
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