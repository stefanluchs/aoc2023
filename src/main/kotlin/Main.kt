import me.luchs.aoc2023.DayTen
import me.luchs.aoc2023.readInput

fun main() {
    val input = readInput("DayTen")
    val day = DayTen(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}
