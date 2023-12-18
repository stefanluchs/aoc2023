package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Coordinate
import me.luchs.aoc2023.shared.Direction

data class DayEighteen(val input: String) : Day<Long> {

    private val plans = Plan.fromInput(input, fromColor = false)

    private val plansFromColor = Plan.fromInput(input, fromColor = true)

    override fun partOne(): Long {
        return plans.volume()
    }

    override fun partTwo(): Long {
        return plansFromColor.volume()
    }

    private fun List<Plan>.volume(): Long {
        val shoelaceArea  = this
            .runningFold(Coordinate(0, 0)) { last, plan -> last.move(plan.direction, plan.range) }
            .reversed() // ensure counter-clock wise orientation of vertices for shoelace formula!
            .shoelaceArea()
        val border = this.sumOf { it.range.toLong() }
        val interior = interior(shoelaceArea, border)
        return interior + border
    }

    data class Plan(val direction: Direction, val range: Int) {
        companion object {
            
            fun fromInput(input: String, fromColor: Boolean): List<Plan> =
                input.lines().map { Plan(it, fromColor) }

            operator fun invoke(row: String, fromColor: Boolean): Plan {
                val (direction, range, color) = row.split(' ')
                return Plan(
                    direction = if (fromColor) color.directionFromColor() else
                        Direction.fromAbbreviation(direction.first())!!,
                    range = if (fromColor) color.substring(2..6).toInt(16) else range.toInt(),
                )
            }

            private fun String.directionFromColor(): Direction = this.dropLast(1).last().let {
                when (it) {
                    '0' -> Direction.RIGHT
                    '1' -> Direction.DOWN
                    '2' -> Direction.LEFT
                    '3' -> Direction.UP
                    else -> throw IllegalStateException()
                }
            }
        }
    }
}

// https://en.wikipedia.org/wiki/Pick%27s_theorem
fun interior(shoelaceArea: Long, border: Long): Long = shoelaceArea - (border / 2) + 1

// https://en.wikipedia.org/wiki/Shoelace_formula
fun List<Coordinate>.shoelaceArea(): Long = this
    .zipWithNext()
    .sumOf {
        val (x1, y1) = it.first
        val (x2, y2) = it.second
        x1.toLong() * y2 - x2.toLong() * y1
    } / 2L


