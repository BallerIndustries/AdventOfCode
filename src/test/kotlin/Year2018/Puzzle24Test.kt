package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle24Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle24.txt").readText().replace("\r", "")
    val puzzle = Puzzle24()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(420, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    enum class DamageType { BLUDGEONING, RADIATION, SLASHING, COLD, FIRE, }

    enum class Team { IMMUNE_SYSTEM, INFECTION }

    data class Group(val team: Team, val units: Int, val hp: Int, val damageType: DamageType, val damage: Int, val initiative: Int, val weaknesses: Set<DamageType>, val immunities: Set<DamageType>) {
        fun effectivePower() = units * damage

        fun damageReceivedFrom(attackingGroup: Group): Int {
            return when {
                this.immunities.contains(attackingGroup.damageType) -> 0
                this.weaknesses.contains(attackingGroup.damageType) -> attackingGroup.units * attackingGroup.damage * 2
                else -> attackingGroup.units * attackingGroup.damage
            }
        }
    }

    class Puzzle24 {

        private val groupCompare = Comparator<Group> { a, b ->
            if (a.effectivePower() != b.effectivePower()) {
                a.effectivePower().compareTo(b.effectivePower())
            }
            else {
                a.initiative.compareTo(b.initiative)
            }
        }

        fun solveOne(puzzleText: String): Int {
            val groups = parsePuzzleText(puzzleText)
            val sorted = groups.sortedWith(groupCompare)







            return -1
        }


















        private fun parsePuzzleText(puzzleText: String): List<Group> {
            val jur = puzzleText.split("\n").filter { it != "" }
            var team = Team.IMMUNE_SYSTEM

            fun getWeaknessesAndImmunities(line: String): Pair<Set<DamageType>, Set<DamageType>> {
                val weaknessesAndImmunitiesRegex = Regex("\\(.+\\)")
                val bracketedText = weaknessesAndImmunitiesRegex.find(line)?.value?.removeSurrounding("(", ")")
                    ?: return Pair(setOf(), setOf())

                if (bracketedText.contains(";")) {

                    val newText = bracketedText
                        .replace("immune to ", "")
                        .replace("weak to ", "")
                        .trim()

                    val tmp = newText.split(";")

                    val firstSet = tmp[0].split(", ").map { DamageType.valueOf(it.trim().toUpperCase()) }.toSet()
                    val secondSet = tmp[1].split(", ").map { DamageType.valueOf(it.trim().toUpperCase()) }.toSet()

                    if (bracketedText.startsWith("immune")) {
                        return Pair(secondSet, firstSet)
                    } else {
                        return Pair(firstSet, secondSet)
                    }
                } else if (bracketedText.contains("weak")) {
                    val newText = bracketedText.replace("weak to ", "")
                    val weaknesses = newText.split(", ").map { DamageType.valueOf(it.toUpperCase()) }.toSet()

                    return Pair(weaknesses, setOf())
                } else if (bracketedText.contains("immune")) {
                    val newText = bracketedText.replace("immune to ", "")
                    val immunities = newText.split(", ").map { DamageType.valueOf(it.toUpperCase()) }.toSet()

                    return Pair(setOf(), immunities)
                } else {
                    throw RuntimeException("sodifjs sdofij")
                }
            }

            val groups = jur.subList(1, jur.size).mapNotNull { line ->

                if (line.contains("Infection:")) {
                    team = Team.INFECTION
                    null
                } else {

                    val horse = line.split(" ")
                    val units = horse[0].toInt()
                    val hp = horse[4].toInt()
                    val initiative = horse.last().toInt()

                    val damageRegex = Regex("with an attack that does \\d+ \\w+\\sdamage")
                    val damage = damageRegex.find(line)!!.value.split(" ")[5].toInt()
                    val damageType = damageRegex.find(line)!!.value.split(" ")[6].toUpperCase().let { DamageType.valueOf(it) }


                    val (weaknesses, immunities) = getWeaknessesAndImmunities(line)

                    Group(team, units, hp, damageType, damage, initiative, weaknesses, immunities)
                }
            }

            return groups
        }

        fun solveTwo(puzzleText: String): Int {
            return 1337
        }
    }
}