package Year2016

import javax.management.relation.InvalidRelationTypeException

class Puzzle3 {
    fun countValidTriangles(input: String): Int {
        val lines = input.split('\n')

        return lines.filter { line ->
            val sides = line.trim().split(Regex("\\s+")).map { it.trim().toInt() }
            (sides[0] + sides[1] > sides[2]) && (sides[0] + sides[2] > sides[1]) && (sides[1] + sides[2] > sides[0])
        }.count()
    }

    fun countValidTrianglesVertically(input: String): Int {
        val lines = input.split('\n')
        val horse = lines.map {  it.trim().split(Regex("\\s+")).map { it.toInt() } }
        val restructured = horse.map { it[0] } + horse.map { it[1] } + horse.map { it[2] }
        val restructuredInput = (0 until restructured.count() step 3).map { "${restructured[it]} ${restructured[it +1]} ${restructured[it +2]}" }.joinToString("\n")

        return countValidTriangles(restructuredInput)
    }
}