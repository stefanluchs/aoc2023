import me.luchs.aoc2023.DayThirteen
import me.luchs.aoc2023.DayTwelve
import me.luchs.aoc2023.readInput

fun main() {
    val input = readInput("DayThirteen")
    val day = DayThirteen(input.trimIndent())
    println("Part One: " + day.partOne())
    //println("Part Two: " + day.partTwo())
}
