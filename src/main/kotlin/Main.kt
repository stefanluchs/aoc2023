import me.luchs.aoc2023.DayThree
import me.luchs.aoc2023.DayTwo

fun main() {
    val input = readInput("DayThree")
    val day = DayThree(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}

fun readInput(day: String): String {
    return object {}.javaClass.getResourceAsStream("/$day")?.bufferedReader()?.use { it.readText() }
        ?: throw IllegalArgumentException("Can not read file $day")
}
