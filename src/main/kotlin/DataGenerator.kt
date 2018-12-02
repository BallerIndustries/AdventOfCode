import java.io.File
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class DataGenerator()

fun main(args: Array<String>) {

    val random = Random()
    val potentialList = listOf("USD 5 Mio - 10 Mio", "> USD 20 Mio", "USD 1 Mio - 3 Mio", "< USD 1 Mio")

    val peopleNames = DataGenerator::class.java.getResource("names.txt").readText().split("\n")
    val companyNames = DataGenerator::class.java.getResource("company-names.txt").readText().replace(",", "").split("\n")

    val nnaCurrencies = listOf(WeightedOption("USD", 100), WeightedOption("SGD", 40))

    val targetNNAs = listOf(
        WeightedOption("100.000.000", 2),
        WeightedOption("30.000.000", 5),
        WeightedOption("20.000.000", 10),
        WeightedOption("15.000.000", 20),
        WeightedOption("12.000.000", 20),
        WeightedOption("10.000.000", 20),
        WeightedOption("5.000.000", 10),
        WeightedOption("3.000.000", 5)
    )

    val cities = listOf(
        WeightedOption("Singapore", 400),
        WeightedOption("Tokyo", 30),
        WeightedOption("Ipoh Perak", 20),
        WeightedOption("Merlimau Melaka", 20),
        WeightedOption("Johor", 20),
        WeightedOption("Gadong", 20),
        WeightedOption("Jakarta", 99),
        WeightedOption("Hong Kong", 99),
        WeightedOption("Jawa Barat", 10),
        WeightedOption("Bangkok", 99),
        WeightedOption("Ho Chi Minh", 30),
        WeightedOption("Hanoi", 40),
        WeightedOption("Surabaya", 40),
        WeightedOption("Bandung", 10),
        WeightedOption("Bekasi", 2),
        WeightedOption("Medan", 3),
        WeightedOption("Tangerang", 3),
        WeightedOption("Hai Phong", 3),
        WeightedOption("Depok", 3),
        WeightedOption("Manila", 80),
        WeightedOption("Davao City", 30),
        WeightedOption("Caloocan", 4),
        WeightedOption("Semarang", 4),
        WeightedOption("Palembang", 5),
        WeightedOption("South Tangerang", 6),
        WeightedOption("Makassar", 10),
        WeightedOption("Phnom Penh", 25)
    )

    val departments = listOf(
        WeightedOption(Department("PBWS 21", "Temasek 3"), random.nextInt(100)),
        WeightedOption(Department("PBWS 101", "APC Southeast Asia"), random.nextInt(100)),
        WeightedOption(Department("PBWS 12", "Raffles 1"), random.nextInt(100)),
        WeightedOption(Department("PBWS 28", "Temasek 1"), random.nextInt(100)),
        WeightedOption(Department("PBWS", "Market Group"), random.nextInt(100)),
        WeightedOption(Department("PBWS 52", "Singapore Market Vanda 2"), random.nextInt(100)),
        WeightedOption(Department("PBWS 10", "Raffles 9"), random.nextInt(100))
    )

    val isPepList = listOf(
        WeightedOption("Y", 10),
        WeightedOption("N", 90)
    )

    val isPlatinumClientList = listOf(
        WeightedOption("Y", 20),
        WeightedOption("N", 80)
    )




    val angus = WeightedOption(RelationshipManager("M292048", "PWTB 173", "Raffles 1337", "Global"), 50)

    val otherRms = (0 until 10).map {
        val department = chooseRandomWeightedOption(random, departments) as Department
        val pid = "M${getXRandomDigits(random, 6)}"
        val rm = RelationshipManager(pid, department.oeCode, department.name, "South East Asia")

        WeightedOption(rm, 10)
    }

    val allRelationshipManagers = otherRms + angus

    val headers = listOf(
        "STATUS",
        "PROSPECT_ID",
        "POTENTIAL",
        "FIRST_NAME",
        "LAST_NAME",
        "COMPANY_NAME",
        "DATE_PROFILED",
        "DATE_MET",
        "DATE_OPENED",
        "DATE_FUNDED",
        "FUNDING_DEADLINE",
        "DATE_DROPPED",
        "CURRENT_FUNDING",
        "TARGET_NNA_CURRENCY",
        "TARGET_NNA_MILLIONS",
        "CITY",
        "RELATIONSHIP_MANAGER_PID",
        "RELATIONSHIP_MANAGER_OECODE",
        "RELATIONSHIP_MANAGER_TEAM",
        "IS_POLITICALLY_EXPOSED_PERSON",
        "IS_PLATINUM_CLIENT",
        "HOBBIES"
    )

    val output = File("output.csv")
    if (output.exists()) output.delete()

    output.appendText(headers.joinToString(", ") + "\n")

    (0 until peopleNames.size).forEachIndexed { index, _ ->
        val status = getStatus(random)
        val prospectId = generateProspectId(random)
        val potential = potentialList.random()
        val companyName = companyNames[index]
        val dates = letsHaveADate(random, status)
        val currentFunding = getCurrentFunding(status, random)
        val (firstName, lastName) = peopleNames[index].split(" ")
        val nnaCurrency = chooseRandomWeightedOption(random, nnaCurrencies) as String
        val targetNNA = chooseRandomWeightedOption(random, targetNNAs) as String
        val city = chooseRandomWeightedOption(random, cities) as String

        val relationshipManager = chooseRandomWeightedOption(random, allRelationshipManagers) as RelationshipManager
        val isPep = chooseRandomWeightedOption(random, isPepList) as String
        val isPlatinumClient = chooseRandomWeightedOption(random, isPlatinumClientList) as String
        val hobbies = getHobbies(random)



        val row = listOf(status, prospectId, potential, lastName, firstName, companyName) + dates.toList() +
            listOf(currentFunding, nnaCurrency, targetNNA, city, relationshipManager.pid, relationshipManager.oeCode, relationshipManager.team, isPep, isPlatinumClient, hobbies)

        output.appendText(row.joinToString(", ") + "\n")
    }
}

fun getCurrentFunding(status: String, random: Random): String {
    if (status != "Funded") {
        return ""
    }

    val jur = random.nextInt(100)

    if (jur < 10) {
        return "${getXRandomDigits(random, 2)}.${getXRandomDigits(random, 2)}"
    }
    else if (jur < 50) {
        return "0.${getXRandomDigits(random, 2)}"
    }
    else {
        return "${getXRandomDigits(random, 1)}.${getXRandomDigits(random, 2)}"
    }
}

fun chooseRandomWeightedOption(random: Random, options: List<WeightedOption>): Any {
    val total = options.sumBy { it.weight }
    val randomInt = random.nextInt(total)
    var current = 0

    options.sortedBy { it.weight }

    val ranges = options.map {
        val range = Range(current, current + it.weight)
        current += it.weight
        range to it
    }

    val theOne = ranges.find { randomInt >= it.first.from && randomInt < it.first.to }
    return theOne!!.second.option
}

data class Department(val oeCode: String, val name: String)

data class RelationshipManager(val pid: String, val oeCode: String, val team: String, val market: String)

data class Range(val from: Int, val to: Int)

data class WeightedOption(val option: Any, val weight: Int)

data class BagOfDates(val dateProfiled: LocalDate, val dateMet: LocalDate? = null, val dateOpened: LocalDate? = null,
                      val dateFunded: LocalDate? = null, val fundingDeadline: LocalDate? = null,
                      val dateDropped: LocalDate? = null
) {

    val formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY")

    fun toList(): List<String> {
        return listOf(dateProfiled, dateMet, dateOpened, dateFunded, fundingDeadline, dateDropped).map {
            it?.format(formatter) ?: ""
        }
    }
}

fun getHobbies(random: Random): String {
    val richPeopleHobbies = listOf(
        "Golf", "Tennis", "Ballooning", "Scuba Diving", "Motorsports", "Yachting",
        "Software Development", "Knitting", "Startups", "Octopus Watching", "Bird watching",
        "Skiing", "Fishing", "Watches", "Hunting", "Reading", "Exercise", "Mountaineering",
        "Wine", "Fashion", "Art", "Philantropy")

    return richPeopleHobbies.shuffled().subList(0, 3 + random.nextInt(6)).joinToString("|")
}

fun letsHaveADate(random: Random, status: String): BagOfDates {
    val dateProfiled = getRandomDate(random)
    val dateMet = dateProfiled.plus(Period.ofDays(random.nextInt(20)))
    val dateOpened = dateMet.plus(Period.ofDays(random.nextInt(200)))
    val dateFunded = dateOpened.plus(Period.ofDays(random.nextInt(60)))
    val fundingDeadline = dateFunded.plus(Period.ofDays(random.nextInt(120)))
    val dateDropped = dateMet.plus(Period.ofDays(random.nextInt(300)))

    return when (status) {
        "Profiled" -> BagOfDates(dateProfiled)
        "Met" -> BagOfDates(dateProfiled, dateMet)
        "Dropped" -> BagOfDates(dateProfiled, dateDropped = dateDropped)
        "Funded" -> BagOfDates(dateProfiled, dateMet, dateOpened, dateFunded, fundingDeadline)
        else -> throw RuntimeException("BORKED!")
    }
}

fun getRandomDate(random: Random): LocalDate {
    val start = LocalDate.of(2014, 6, 1).toEpochDay()
    val end = LocalDate.of(2018, 11, 1).toEpochDay()
    val diff = end - start

    return LocalDate.ofEpochDay(start + random.nextInt(diff.toInt()))
}



fun getStatus(random: Random): String {
    val statusList = listOf("Met", "Profiled", "Dropped", "Funded")

    val horse = random.nextInt(1000)

    if (horse < 400) {
        return "Dropped"
    }
    else if (horse < 700) {
        return "Met"
    }
    else if (horse < 900) {
        return "Profiled"
    }
    else {
        return "Funded"
    }


    // 40% dorpped
    // 30% met
    // 20% profiled
    // 10% funded


}

//fun generateId(random: Random): String {
//    val randomVal = random.nextInt(1000)
//
//    if (randomVal < 200) {
//        return generateMfpNumber(random, 10)
//    }
//    else if (randomVal < 400) {
//
//    }
//
//}

fun generateProspectId(random: Random): String {
    val randomVal = random.nextInt(1000)

    if (randomVal < 500) {
        return getXRandomDigits(random, 8)
    }
    else {
        return getXRandomDigits(random, 7)
    }
}

fun getRandomCurrency(random: Random): String {
    val currencies = listOf("USD", "EUR", "USD", "HKD", "SGD", "AUD", "CAD", "CHF", "CNY", "JPY", "INR", "TWD")
    return currencies.random()!!
}

fun generateMfpNumber(random: Random, numDigits: Int): String {
    return "MFP${getXRandomDigits(random, numDigits)}"
}

fun getXRandomDigits(random: Random, count: Int): String {
    return (0 until count).map { random.nextInt(10) }.joinToString("")
}

fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null

