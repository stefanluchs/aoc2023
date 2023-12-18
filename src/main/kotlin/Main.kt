import me.luchs.aoc2023.*

fun main() {
    val input = readInput("DayEighteen")
    val day = DayEighteen(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}
