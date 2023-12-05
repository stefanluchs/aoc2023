import me.luchs.aoc2023.DayFive

fun main() {
    val input = readInput("DayFive")
    val day = DayFive(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}

fun readInput(day: String): String {
    return object {}.javaClass.getResourceAsStream("/input/$day")?.bufferedReader()?.use { it.readText() }
        ?: throw IllegalArgumentException("Can not read file $day")
}
