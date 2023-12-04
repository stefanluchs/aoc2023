package me.luchs.aoc2023

import kotlin.math.pow

data class DayFour(val input: String) : Day<Int> {
    override fun partOne(): Int {
        return Scratchcard.ofInput(input).map { it.points() }.sumOf { it }
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    data class Scratchcard(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {
        companion object {
            fun ofInput(input: String): List<Scratchcard> {
                return input.lines().map { ofLine(it) }
            }

            private fun ofLine(line: String): Scratchcard {
                val id = line
                    .split(':')[0]
                    .split(" ")
                    .filter { it.isNotBlank() }[1]
                    .toInt()

                val numberArrays = line
                    .split(':')[1]
                    .split('|')

                val winningNumbers = numberArrays[0]
                    .split(' ')
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }

                val numbers = numberArrays[1]
                    .split(' ')
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }

                return Scratchcard(id, winningNumbers, numbers)
            }
        }

        fun points(): Int {
            return 2.0.pow(matches() - 1).toInt()
        }

        private fun matches(): Int {
            return numbers.intersect(winningNumbers.toSet()).size
        }
    }

}