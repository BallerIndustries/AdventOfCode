package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle24.txt").readText().replace("\r", "")
    val puzzle = Puzzle24()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(549, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(4147, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "sesenwnenenewseeswwswswwnenewsewsw\n" +
                "neeenesenwnwwswnenewnwwsewnenwseswesw\n" +
                "seswneswswsenwwnwse\n" +
                "nwnwneseeswswnenewneswwnewseswneseene\n" +
                "swweswneswnenwsewnwneneseenw\n" +
                "eesenwseswswnenwswnwnwsewwnwsene\n" +
                "sewnenenenesenwsewnenwwwse\n" +
                "wenwwweseeeweswwwnwwe\n" +
                "wsweesenenewnwwnwsenewsenwwsesesenwne\n" +
                "neeswseenwwswnwswswnw\n" +
                "nenwswwsewswnenenewsenwsenwnesesenew\n" +
                "enewnwewneswsewnwswenweswnenwsenwsw\n" +
                "sweneswneswneneenwnewenewwneswswnese\n" +
                "swwesenesewenwneswnwwneseswwne\n" +
                "enesenwswwswneneswsenwnewswseenwsese\n" +
                "wnwnesenesenenwwnenwsewesewsesesew\n" +
                "nenewswnwewswnenesenwnesewesw\n" +
                "eneswnwswnwsenenwnwnwwseeswneewsenese\n" +
                "neswnwewnwnwseenwseesewsenwsweewe\n" +
                "wseweeenwnesenwwwswnew"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(10, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "sesenwnenenewseeswwswswwnenewsewsw\n" +
                "neeenesenwnwwswnenewnwwsewnenwseswesw\n" +
                "seswneswswsenwwnwse\n" +
                "nwnwneseeswswnenewneswwnewseswneseene\n" +
                "swweswneswnenwsewnwneneseenw\n" +
                "eesenwseswswnenwswnwnwsewwnwsene\n" +
                "sewnenenenesenwsewnenwwwse\n" +
                "wenwwweseeeweswwwnwwe\n" +
                "wsweesenenewnwwnwsenewsenwwsesesenwne\n" +
                "neeswseenwwswnwswswnw\n" +
                "nenwswwsewswnenenewsenwsenwnesesenew\n" +
                "enewnwewneswsewnwswenweswnenwsenwsw\n" +
                "sweneswneswneneenwnewenewwneswswnese\n" +
                "swwesenesewenwneswnwwneseswwne\n" +
                "enesenwswwswneneswsenwnewswseenwsese\n" +
                "wnwnesenesenenwwnenwsewesewsesesew\n" +
                "nenewswnwewswnenesenwnesewesw\n" +
                "eneswnwswnwsenenwnwnwwseeswneewsenese\n" +
                "neswnwewnwnwseenwseesewsenwsweewe\n" +
                "wseweeenwnesenwwwswnew"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(2208, result)
    }
}

class Puzzle24 {
    data class Point(val x: Int, val y: Int) {
        fun east() = this.copy(x = this.x + 2)
        fun west() = this.copy(x = this.x - 2)
        fun south() = this.copy(y = this.y + 1)
        fun north() = this.copy(y = this.y - 1)

        fun southEast() = south().copy(x = this.x + 1)
        fun southWest() = south().copy(x = this.x - 1)
        fun northEast() = north().copy(x = this.x + 1)
        fun northWest() = north().copy(x = this.x - 1)

        fun neighbours() = listOf(east(), west(), southEast(), southWest(), northEast(), northWest())
    }

    fun solveOne(puzzleText: String): Int {
        val tiles = setInitialTiles(puzzleText)

        return tiles.values.count { it }
    }

    private fun setInitialTiles(puzzleText: String): MutableMap<Point, Boolean> {
        val tiles = mutableMapOf<Point, Boolean>()

        puzzleText.split("\n").forEach { line ->
            val point: Point = resolvePoint(line)
            tiles[point] = !(tiles[point] ?: false)
        }
        return tiles
    }

    private fun resolvePoint(line: String): Point {
        var current = Point(0, 0)
        var index = 0

        while (index < line.length) {

            val c1 = line[index]
            val c2 = line.getOrNull(index+1)

            when {
                c1 == 'e' -> {
                    current = current.east()
                    index++
                }
                c1 == 'w' -> {
                    current = current.west()
                    index++
                }
                c1 == 'n' && c2 == 'e' -> {
                    current = current.northEast()
                    index += 2
                }
                c1 == 'n' && c2 == 'w' -> {
                    current = current.northWest()
                    index += 2
                }
                c1 == 's' && c2 == 'e' -> {
                    current = current.southEast()
                    index += 2
                }

                c1 == 's' && c2 == 'w' -> {
                    current = current.southWest()
                    index += 2
                }
            }
        }

        return current
    }

    fun solveTwo(puzzleText: String): Int {
        var tiles: Map<Point, Boolean> = setInitialTiles(puzzleText)

        (1..100).forEach {
            tiles = iterate(tiles)
        }

        return tiles.values.count { it }
    }

    private fun iterate(tiles: Map<Point, Boolean>): Map<Point, Boolean> {
        val allPoints = tiles.keys.flatMap { it.neighbours() }.toSet()

        return allPoints.associate { point ->

            val numberOfBlackNeighbors = point.neighbours().count { tiles[it] == true }
            val pointIsBlack = tiles[point] == true

            if (pointIsBlack && (numberOfBlackNeighbors == 0 || numberOfBlackNeighbors > 2)) {
                point to !pointIsBlack
            }
            else if (!pointIsBlack && numberOfBlackNeighbors == 2) {
                point to !pointIsBlack
            }
            else {
                point to pointIsBlack
            }
        }
    }
}

