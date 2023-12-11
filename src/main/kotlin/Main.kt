import me.luchs.aoc2023.DayEleven
import me.luchs.aoc2023.readInput

fun main() {
    val input = readInput("DayEleven")
    val day = DayEleven(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}
