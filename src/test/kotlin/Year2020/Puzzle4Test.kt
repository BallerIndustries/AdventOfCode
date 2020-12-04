package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle4Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle4.txt").readText().replace("\r", "")
    val puzzle = Puzzle4()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(239, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(188, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                "byr:1937 iyr:2017 cid:147 hgt:183cm\n" +
                "\n" +
                "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
                "hcl:#cfa07d byr:1929\n" +
                "\n" +
                "hcl:#ae17e1 iyr:2013\n" +
                "eyr:2024\n" +
                "ecl:brn pid:760753108 byr:1931\n" +
                "hgt:179cm\n" +
                "\n" +
                "hcl:#cfa07d eyr:2025 pid:166559648\n" +
                "iyr:2011 ecl:brn hgt:59in"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\n" +
                "hcl:#623a2f\n" +
                "\n" +
                "eyr:2029 ecl:blu cid:129 byr:1989\n" +
                "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm\n" +
                "\n" +
                "hcl:#888785\n" +
                "hgt:164cm byr:2001 iyr:2015 cid:88\n" +
                "pid:545766238 ecl:hzl\n" +
                "eyr:2022\n" +
                "\n" +
                "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(4, result)
    }
}

class Puzzle4 {
    fun solveOne(puzzleText: String): Int {
        val passports = puzzleText.split("\n\n")

        val requiredFields1 = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid")
        val requiredFields2 = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")


        return passports.count { text ->
            val aaaa = text.replace("\n", " ")

            val mappo: Map<String, String> = aaaa.split(" ").map {
                val (key: String, value: String) = it.split(":")
                key to value
            }.toMap()

            val keys = mappo.keys
            //val isValid = requiredFields1.all { requiredFields1.contains(it) } || keys.all { requiredFields2.contains(it) }

            val isValid = (keys.size == 7 && keys.all { requiredFields2.contains(it) }) ||
                    (keys.size == 8 && keys.all { requiredFields1.contains(it) })



            //println(isValid)

            isValid

        }


        //return 1
    }

    fun solveTwo(puzzleText: String): Int {
        val passports = puzzleText.split("\n\n")

        val requiredFields1 = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid")
        val requiredFields2 = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")


        return passports.count { text ->
            val aaaa = text.replace("\n", " ")

            val mappo: Map<String, String> = aaaa.split(" ").map {
                val (key: String, value: String) = it.split(":")
                key to value
            }.toMap()

            val keys = mappo.keys
            var isValid = (keys.size == 7 && keys.all { requiredFields2.contains(it) }) ||
                    (keys.size == 8 && keys.all { requiredFields1.contains(it) })

            if (isValid == false) {
                false
            }

            else {
                val byrIsValid = mappo["byr"]!!.toInt() >= 1920 && mappo["byr"]!!.toInt() <= 2002
                val iyrIsValid = mappo["iyr"]!!.toInt() >= 2010 && mappo["iyr"]!!.toInt() <= 2020
                val eyrIsValid = mappo["eyr"]!!.toInt() >= 2020 && mappo["eyr"]!!.toInt() <= 2030
                val hgtIsvalid = hgtIsValid(mappo)
                val eclIsValid = eclIsValid(mappo)
                val pidIsValid = pidIsValid(mappo)
                val hclIsValid = hclIsValid(mappo)

                isValid = isValid && byrIsValid
                isValid = isValid && iyrIsValid
                isValid = isValid && eyrIsValid
                isValid = isValid && hgtIsvalid
                isValid = isValid && eclIsValid
                isValid = isValid && pidIsValid
                isValid = isValid && hclIsValid


                //println(isValid)

                isValid
            }




        }


        //return 1
    }

    private fun hgtIsValid(mappo: Map<String, String>): Boolean {
        val height = mappo["hgt"]!!
        val heightValue = height.replace("cm", "").replace("in", "").toInt()

        if (height.endsWith("cm")) {
            return heightValue >= 150 && heightValue <= 193;
        }
        else if (height.endsWith("in")) {
            return heightValue >= 59 && heightValue <= 76;
        }
        else {
            return false
        }
    }

    private fun eclIsValid(mappo: Map<String, String>): Boolean {
        val ecl = mappo["ecl"]!!
        return "amb blu brn gry grn hzl oth".split(" ").contains(ecl)
    }

    private fun pidIsValid(mappo: Map<String, String>): Boolean {
        val pid = mappo["pid"]!!
        return pid.length == 9 && pid.all { it.isDigit() }
    }

    private fun hclIsValid(mappo: Map<String, String>): Boolean {
        val aaaa = mappo["hcl"]

        if (aaaa == null) {
            return false
            println("wtf")
        }

        val hairColour = aaaa!!
        return hairColour.length == 7 && hairColour[0] == '#' && (1..6).all {
            hairColour[it].isDigit() || hairColour[it] in 'a'..'f'
        }

    }
}

