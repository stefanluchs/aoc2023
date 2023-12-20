import me.luchs.aoc2023.*

fun main() {
    val input = readInput("DayTwenty")
    val day = DayTwenty(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}
