package Year2018

import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle10Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle10.txt").readText()
    val puzzle = Puzzle10()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `can be a dog`() {
        val text = "position=< 30528, -20119> velocity=<-3,  2>"
        println(text.removeSurrounding("<", ">"))
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }

    class Puzzle10 {

        data class Point(val x: Int, val y: Int)

        data class Dog(val x: Int, val y: Int, val deltaX: Int, val deltaY: Int)

        fun solveOne(puzzleText: String): String {
            val dogs = puzzleText.split("\n").map { line ->

                val tmp = line.replace("position=", "")
                    .replace("velocity=", ",")
                        .replace("<", "")
                        .replace(">", "")
                        .split(",")
                        .map { it.trim() }
                        .map { it.toInt() }

            Dog(tmp[0], tmp[1], tmp[2], tmp[3])
            }


            // Now simlulate one thousand seconds
            val closest = (0 until 20000).map { second ->
                val points = dogs.map { dog ->
                    Point(dog.x + (dog.deltaX * second), dog.y + (dog.deltaY * second))
                }

                // Find the minimum area
                val minX = points.minBy { it.x }!!.x
                val maxX = points.maxBy { it.x }!!.x
                val minY = points.minBy { it.y }!!.y
                val maxY = points.maxBy { it.y }!!.y

                val width = maxX - minX
                val height = maxY - minY
                val area = width.toLong() * height.toLong()

                Triple(second, points, area)
            }
            .minBy {
                it.third
            }

            println("message appears at second = ${closest!!.first}")
            printPoints(closest.second)

            return ""
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }

        fun printPoints(points: List<Point>) {
            val pointSet = points.toSet()
            val minX = points.minBy { it.x }!!.x
            val maxX = points.maxBy { it.x }!!.x
            val minY = points.minBy { it.y }!!.y
            val maxY = points.maxBy { it.y }!!.y

            val width = maxX - minX + 1
            val height = maxY - minY + 1

            val normalisedPoints = pointSet.map { Point(it.x - minX, it.y - minY) }.toSet()
            val buffer: List<List<Char>> = (0 until height).map { y ->
                (0 until width).map { x ->
                    if (normalisedPoints.contains(Point(x, y))) '#' else '.'
                }
            }

            val gridText = buffer.map { it.joinToString("") }.joinToString("\n")
            println(gridText)
        }
    }
}