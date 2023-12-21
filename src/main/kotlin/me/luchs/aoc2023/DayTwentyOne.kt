package me.luchs.aoc2023

import me.luchs.aoc2023.shared.CharMatrix
import me.luchs.aoc2023.shared.Coordinate
import me.luchs.aoc2023.shared.mod

data class DayTwentyOne(val input: String) : Day<Long> {

    private val matrix = CharMatrix(input)

    override fun partOne(): Long {
        return positionsAfter(steps = 64)
    }

    override fun partTwo(): Long {

        val steps = 26501365

        val size = matrix.dimensions.first // first == second == 131

        val rest = steps % size
        val times = steps / size

        // 26501365 = (times = 202300) * (size = 131) + (rest = 65)

        val value65 = positionsAfter(rest) // 65
        val value65_131 = positionsAfter(rest + size) // 65 + 131
        val value65_262 = positionsAfter(rest + (size * 2)) // 65 + 2 * 131

        // solve value65 + value131 x + value262 x^2 = 202300 for x to find the solution

        fun f(x: Int): Long = value65 * (x - 1) * (x - 2) / 2 +
                value65_131 * x * (x - 2) / -1 +
                value65_262 * x * (x - 1) / 2

        // 636350496972143
        return f(x = times)
    }

    fun positionsAfter(steps: Int): Long {
        val dimensions = matrix.dimensions
        val start = matrix['S'].first()

        var possibilities = mutableListOf(start)
        val visited = mutableSetOf<Coordinate>()

        repeat(times = steps) {

            val next = buildSet {
                while (possibilities.isNotEmpty()) {
                    val position = possibilities.removeFirst()

                    visited.add(position)

                    val neighbours = position.adjacent4()
                        .distinct()
                        .filterNot { matrix[it.mod(dimensions)] == '#' }

                    neighbours.forEach { add(it) }

                }
            }

            possibilities = next.toMutableList()

        }

        return possibilities.distinct().size.toLong()
    }

}

