import me.luchs.aoc2023.DayNine
import me.luchs.aoc2023.readInput

fun main() {
    val input = readInput("DayNine")
    val day = DayNine(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}
