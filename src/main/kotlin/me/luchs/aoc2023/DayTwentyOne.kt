package me.luchs.aoc2023

import me.luchs.aoc2023.shared.CharMatrix
import me.luchs.aoc2023.shared.Coordinate

data class DayTwentyOne(val input: String) : Day<Long> {

    private val matrix = CharMatrix(input)

    override fun partOne(): Long {
        return positionsAfter(steps = 64).size.toLong()
    }

    override fun partTwo(): Long {
        TODO("Not yet implemented")
    }

    fun positionsAfter(steps: Int): Set<Coordinate> {
        val start = matrix['S'].first()

        var possibilities = mutableListOf(start)
        val visited = mutableSetOf<Coordinate>()

        repeat(times = steps) {

            val next = buildSet {
                while (possibilities.isNotEmpty()) {
                    val position = possibilities.removeFirst()

                    visited.add(position)

                    val neighbours = matrix.adjacent4WithValues(position, '.', 'S')

                    neighbours.forEach { add(it.key) }

                }
            }

            possibilities = next.toMutableList()

        }

        return possibilities.toSet()
    }

}

