import me.luchs.aoc2023.DayOne

fun main() {
    val input = readInput("DayOne")
    val day = DayOne(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}

fun readInput(day: String): String {
    return object {}.javaClass.getResourceAsStream("/$day")?.bufferedReader()?.use { it.readText() }
        ?: throw IllegalArgumentException("Can not read file $day")
}
