import me.luchs.aoc2023.DayEight
import me.luchs.aoc2023.readInput

fun main() {
    val input = readInput("DayEight")
    val day = DayEight(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}
