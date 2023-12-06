package me.luchs.aoc2023

import kotlin.math.*


data class DaySix(val input: String) : Day<Int> {

    override fun partOne(): Int {
        val races: List<Race> = Race(input)
        return races
            .map { it.solvePartOne() }
            .reduce { acc, i -> acc * i }
    }


    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    data class Race(val time: Int, val distance: Int) {
        companion object {
            operator fun invoke(input: String): List<Race> {

                val times = input.lines()[0]
                    .split(':')[1]
                    .trim()
                    .split(Regex(" +"))
                    .map { it.toInt() }

                val distances = input.lines()[1]
                    .split(':')[1]
                    .trim()
                    .split(Regex(" +"))
                    .map { it.toInt() }

                return times.zip(distances).map { Race(it.first, it.second) }
            }
        }

        /**
         * Calculates the solution for part one of a problem.
         *
         * This method uses a mathematical formula to solve for the value of x,
         * which represents time.
         *
         * The formula used is:
         * x = (time +- sqrt(time^2 - 4 * (distance + 1)))
         * */
        fun solvePartOne(): Int {
            //x = time/2 +- /(time² - 4 * distance) // 2
            val x1 = (time + sqrt(time.toDouble().pow(2) - 4 * (distance + 1))) / 2
            val x2 = (time - sqrt(time.toDouble().pow(2) - 4 * (distance + 1))) / 2
            return (floor(x1) - ceil(x2)).toInt() + 1
        }
    }

}
