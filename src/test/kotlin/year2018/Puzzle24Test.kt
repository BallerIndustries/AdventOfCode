package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle24.txt").readText().replace("\r", "")
    val puzzle = Puzzle24()

    @Test
    fun `part a example`() {
        val text = """
            Immune System:
            17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
            989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

            Infection:
            801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
            4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
        """.trimIndent()

        val result = puzzle.solveOne(text)
        assertEquals(5216, result)
    }

    @Test
    fun `puzzle part a`() {
        // 15885 too low
        val result = puzzle.solveOne(puzzleText)
        assertEquals(15919, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    enum class DamageType { BLUDGEONING, RADIATION, SLASHING, COLD, FIRE, }

    enum class Team { IMMUNE_SYSTEM, INFECTION }

    data class Group(val id: Int, val team: Team, var units: Int, val hp: Int, val damageType: DamageType, val damage: Int,
                     val initiative: Int, val weaknesses: Set<DamageType>, val immunities: Set<DamageType>) {
        fun effectivePower() = units * damage

        fun damageReceivedFrom(attackingGroup: Group): Int {
            return when {
                this.immunities.contains(attackingGroup.damageType) -> 0
                this.weaknesses.contains(attackingGroup.damageType) -> attackingGroup.units * attackingGroup.damage * 2
                else -> attackingGroup.units * attackingGroup.damage
            }
        }

        fun isAlive() = units > 0

        fun isDead() = !isAlive()

        fun getAttackedBy(attacker: Group) {
            if (this.isDead()) {
                println("HEy wtf! I am getting attacking, but I am dead!")
            }

            if (attacker.isDead()) {
                println("Hey wtf! I am getting attacked by a group that is dead!")
            }

            val damageDealt = damageReceivedFrom(attacker)

            // Okay now figure out how many units die.
            val unitsKilled = Math.min(damageDealt / this.hp, this.units)
            this.units -= unitsKilled
        }
    }

    class Puzzle24 {

        private val startingOrder = Comparator<Group> { a, b ->
            if (a.effectivePower() != b.effectivePower()) {
                a.effectivePower().compareTo(b.effectivePower())
            } else {
                a.initiative.compareTo(b.initiative)
            }
        }

        private fun generateToAttackOrder(attackingGroup: Group): java.util.Comparator<Group> {
            return Comparator { a, b ->

                val aDamageReceived = a.damageReceivedFrom(attackingGroup)
                val bDamageReceived = b.damageReceivedFrom(attackingGroup)

                val aEffectivePower = a.effectivePower()
                val bEffectivePower = b.effectivePower()

                when {
                    aDamageReceived != bDamageReceived -> aDamageReceived.compareTo(bDamageReceived)
                    aEffectivePower != bEffectivePower -> aEffectivePower.compareTo(bEffectivePower)
                    else -> a.initiative.compareTo(b.initiative)
                }
            }
        }

        fun solveOne(puzzleText: String): Int {
            val groups = parsePuzzleText(puzzleText)
            val immuneSystem = groups.filter { it.team == Team.IMMUNE_SYSTEM }
            val infection = groups.filter { it.team == Team.INFECTION }

            while (true) {
                outputStats(groups)
                runARound(groups)

                // One team is totally dead
                if (immuneSystem.all { it.isDead() } || infection.all { it.isDead() }) {
                    break
                }
            }

            val immuneSystemUnits = immuneSystem.filter { it.isAlive() }.sumBy { it.units }
            val infectionUnits = infection.filter { it.isAlive() }.sumBy { it.units }

            return Math.max(immuneSystemUnits, infectionUnits)
        }

        private fun outputStats(groups: List<Group>) {
//            println("Immune System:")
//            println(groups.filter { it.team == Team.IMMUNE_SYSTEM }.map { "Group ${it.id} contains ${it.units} units" }.joinToString("\n"))
//
//            println("Infection:")
//            println(groups.filter { it.team == Team.INFECTION}.map { "Group ${it.id} contains ${it.units} units" }.joinToString("\n"))
//            println()
        }

        private fun runARound(groups: List<Group>) {
            val attackerToTarget = acquireTargets(groups)
            attackTargets(attackerToTarget)
        }

        private fun attackTargets(attackerToTarget: List<Pair<Group, Group?>>) {
            attackerToTarget
                .filter { it.first.isAlive() }
                .sortedByDescending{ (attacker, _) -> attacker.initiative }
                .forEach { (attacker, target) -> target?.getAttackedBy(attacker) }
        }

        private fun acquireTargets(groups: List<Group>): List<Pair<Group, Group?>> {
            val sortedGroups = groups.sortedWith(startingOrder.reversed())
            val targetedGroups = mutableSetOf<Group>()
            val attackerToTarget = mutableListOf<Pair<Group, Group?>>()

            for (attackingGroup in sortedGroups) {
                if (attackingGroup.isDead()) {
                    continue
                }

                // All enemies
                val targetedEnemy = groups.filter { it != attackingGroup }
                    .filter { !targetedGroups.contains(it) }
                    .filter { it.team != attackingGroup.team }
                    .filter { it.isAlive() }
                    .sortedWith(generateToAttackOrder(attackingGroup).reversed())
                    .firstOrNull()

                if (targetedEnemy?.damageReceivedFrom(attackingGroup) == 0) {
                    continue
                }

                attackerToTarget.add(Pair(attackingGroup, targetedEnemy))

                // Add to set of targeted groups
                targetedEnemy?.let { targetedGroups.add(it) }
            }
            return attackerToTarget
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

            var groupId = 1

            return jur.subList(1, jur.size).mapNotNull { line ->

                if (line.contains("Infection:")) {
                    team = Team.INFECTION
                    groupId = 1
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

                    Group(groupId++, team, units, hp, damageType, damage, initiative, weaknesses, immunities)
                }
            }
        }

        private fun boostImmuneSystemAttack(groups: List<Group>, damageDelta: Int): List<Group> {
            return groups.map { group ->
                if (group.team == Team.IMMUNE_SYSTEM) group.copy(damage = group.damage + damageDelta) else group
            }
        }

        fun solveTwo(puzzleText: String): Int {
            val groups = parsePuzzleText(puzzleText)
            val boostAmount = 94
            val boostedGroups = boostImmuneSystemAttack(groups, boostAmount)

            val immuneSystem = boostedGroups.filter { it.team == Team.IMMUNE_SYSTEM }
            val infection = boostedGroups.filter { it.team == Team.INFECTION }

            while (true) {
                outputStats(boostedGroups)
                runARound(boostedGroups)

                // One team is totally dead
                if (immuneSystem.all { it.isDead() } || infection.all { it.isDead() }) {
                    break
                }
            }

            val immuneSystemLost = immuneSystem.all { it.isDead() }

            if (immuneSystemLost) {
                println("Immune system lost with a boost of $boostAmount")
            }
            else {
                println("Immune system won with a boost of $boostAmount. units remaining = ${immuneSystem.sumBy { it.units }}")

            }



            return 909230920
        }
    }
}