package me.luchs.aoc2023

import me.luchs.aoc2023.shared.CharMatrix
import me.luchs.aoc2023.shared.Coordinate
import me.luchs.aoc2023.shared.mod

data class DayTwentyOne(val input: String) : Day<Long> {

    private val matrix = CharMatrix(input)

    val steps = 26501365

    override fun partOne(): Long {
        return positionsAfter(steps = 64)
    }

    override fun partTwo(): Long {

        val dimensions = matrix.dimensions

        val start = matrix['S'].first()

        var possibilities = mutableListOf(start)
        val visited = mutableSetOf<Coordinate>()


        val rest = steps % dimensions.first
        val times = 26501365L / dimensions.first

        // 26501365 = 202300 * 131 + 65


        val value65 = positionsAfter(65)
        val value65_131 = positionsAfter(65 + 131)
        val value65_262 = positionsAfter(65 + (131 * 2))

        // solve value65 + value131 x + value262 x^2 = 202300 for x to find the solution

        fun f(x: Long): Long =
            value65 * (x - 1) * (x - 2) / (0 - 1) / (0 - 2) +
                    value65_131 * (x - 0) * (x - 2) / (1 - 0) / (1 - 2) +
                    value65_262 * (x - 0) * (x - 1) / (2 - 0) / (2 - 1)

        val res = f(202300)

        // 3986414806023743
        // 636350496972143

        return 0L
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

