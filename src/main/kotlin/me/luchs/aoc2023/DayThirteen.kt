package me.luchs.aoc2023

import me.luchs.aoc2023.shared.*
import kotlin.math.min

data class DayThirteen(val input: String) : Day<Int> {

    override fun partOne(): Int {
        return Mirrors(input).sumOf {
            val vertical = it.reflectionIndex(transposed = false)
            val horizontal = it.reflectionIndex(transposed = true)
            vertical + horizontal * 100
        }
    }

    override fun partTwo(): Int {
        return Mirrors(input).sumOf {
            val vertical = it.reflectionIndex(transposed = false, expectedDiffCount = 1)
            val horizontal = it.reflectionIndex(transposed = true, expectedDiffCount = 1)
            vertical + horizontal * 100
        }
    }

    data class Mirrors(val matrix: List<Point>) {

        companion object {
            operator fun invoke(input: String): List<Mirrors> {
                return input.split("\n\n")
                    .map { block ->
                        block.lines().flatMapIndexed { row, line ->
                            line.toCharArray()
                                .mapIndexed { column, char -> Point(row, column, char) }
                        }.let { Mirrors(it) }
                    }
            }
        }

        fun reflectionIndex(
            transposed: Boolean,
            expectedDiffCount: Int = 0,
        ): Int {

            val sliceProvider: (Int) -> Binary =
                { index -> if (transposed) row(index) else column(index) }

            val maxIndex = if (transposed) matrix.maxRow() else matrix.maxColumn()

            return (0..maxIndex)
                .associateWith { sliceProvider(it) }
                .entries
                // create pairs with next row / column
                .zipWithNext()
                // remove all pairs with too much difference
                .filterNot { it.first.value.bitDifference(it.second.value) > expectedDiffCount }
                // keep number of differences for center rows / columns for later comparison
                .associateWith { it.first.value.bitDifference(it.second.value) }
                // compare sum of the differences of all relevant symmetrical rows / column with expected diff count
                .filter {
                    val diff = (it.key.first.key to it.key.second.key)
                        .symmetricalPairs(maxIndex)
                        .sumOf { sliceProvider(it.first).bitDifference(sliceProvider(it.second)) }
                    // sum of differences + differences of center rows / columns
                    diff + it.value == expectedDiffCount
                }
                // map to second row / column index
                .map { it.key.second.key }
                .sum()
        }

        private fun Pair<Int, Int>.symmetricalPairs(maxIndex: Int): List<Pair<Int, Int>> {
            return (1..min(this.first, maxIndex - this.second))
                .map { this.first - it to this.second + it }
        }

        private fun column(index: Int): Binary {
            return matrix.slice(index, vertical = true).toBinary()
        }

        private fun row(index: Int): Binary {
            return matrix.slice(index, vertical = false).toBinary()
        }

        private fun List<Point>.toBinary(): Binary {
            return this.asString()
                .replace('#', '1')
                .replace('.', '0')
        }

    }

}
