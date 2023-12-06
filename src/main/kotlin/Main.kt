import me.luchs.aoc2023.DaySix
import me.luchs.aoc2023.readInput

fun main() {
    val input = readInput("DaySix")
    val day = DaySix(input.trimIndent())
    println("Part One: " + day.partOne())
    println("Part Two: " + day.partTwo())
}


