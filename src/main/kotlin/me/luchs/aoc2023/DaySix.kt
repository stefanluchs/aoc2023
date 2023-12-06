package me.luchs.aoc2023

import kotlin.math.*


data class DaySix(val input: String) : Day<Long> {

    override fun partOne(): Long {
        return Race(input).map { it.numberOfWaysToBeatDistance() }.reduce { acc, i -> acc * i }
    }

    override fun partTwo(): Long {
        return Race.ofAggregatedInput(input).numberOfWaysToBeatDistance()
    }

    data class Race(val time: Long, val distance: Long) {
        companion object {
            operator fun invoke(input: String): List<Race> {
                val (times, distances) = input.lines().map { it.parseValues() }
                return times.zip(distances).map { Race(it.first, it.second) }
            }

            fun ofAggregatedInput(input: String): Race {
                val (time, distance) = input.lines().map { it.parseAggregatedValue() }
                return Race(time, distance)
            }

            private fun String.parseValues(): List<Long> {
                return this.split(':')[1]
                    .trim()
                    .split(Regex(" +"))
                    .map { it.toLong() }
            }

            private fun String.parseAggregatedValue(): Long {
                return this.split(':')[1]
                    .trim()
                    .replace(" ", "")
                    .toLong()
            }
        }

        /**
         * Calculates the solution for part one of a problem.
         *
         * This method uses the formula to solve quadratic formulas to solve for the value of x,
         * which represents time.
         *
         * The formula used is:
         * x = (time +- sqrt(time^2 - 4 * (distance + 1))) / 2
         * */
        fun numberOfWaysToBeatDistance(): Long {
            val x1 = (time + sqrt(time.toDouble().pow(2) - 4 * (distance + 1))) / 2
            val x2 = (time - sqrt(time.toDouble().pow(2) - 4 * (distance + 1))) / 2
            return (floor(x1) - ceil(x2)).toLong() + 1
        }
    }

}
