package me.luchs.aoc2023

import me.luchs.aoc2023.shared.*
import kotlin.math.min

data class DayThirteen(val input: String) : Day<Int> {

    override fun partOne(): Int {
        return Mirrors(input).sumOf {
            val vertical = it.verticalReflectionIndex()
            val horizontal = it.horizontalReflectionIndex()
            (vertical ?: 0) + (horizontal ?: 0) * 100
        }
    }

    override fun partTwo(): Int {
        return Mirrors(input).sumOf {
            val verticalExclusion = it.verticalReflectionIndex()
            val horizontalExclusion = it.horizontalReflectionIndex()

            val vertical = it.verticalReflectionIndex(
                offBy = 1,
                offenders = 1,
                excludeIndex = verticalExclusion
            )
            val horizontal = it.horizontalReflectionIndex(
                offBy = 1,
                offenders = 1,
                excludeIndex = horizontalExclusion
            )
            (vertical ?: 0) + (horizontal ?: 0) * 100
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

        fun verticalReflectionIndex(
            offBy: Int = 0,
            offenders: Int? = null,
            excludeIndex: Int? = null
        ): Int? {
            return horizontalReflectionIndex2(
                offBy = offBy,
                offenders = offenders,
                sliceIndex = 0..matrix.maxColumn(),
                excludeIndex = excludeIndex,
                maxIndex = matrix.maxColumn()
            )
            { index -> column(index) }
        }

        fun horizontalReflectionIndex(
            offBy: Int = 0,
            offenders: Int? = null,
            excludeIndex: Int? = null
        ): Int? {
            return horizontalReflectionIndex2(
                offBy = offBy,
                offenders = offenders,
                sliceIndex = 0..matrix.maxRow(),
                excludeIndex = excludeIndex,
                maxIndex = matrix.maxRow()
            )
            { index -> row(index) }
        }

        private fun horizontalReflectionIndex2(
            offBy: Int = 0,
            offenders: Int? = null,
            sliceIndex: IntRange,
            maxIndex: Int,
            excludeIndex: Int?,
            sliceProvider: (Int) -> Binary
        ): Int? {
            val pairs = sliceIndex
                .associateWith { sliceProvider(it) }
                .entries
                .zipWithNext()
                .filterNot {
                    if (excludeIndex != null) {
                        excludeIndex == it.first.key || excludeIndex == it.second.key
                    } else false
                }

            val validPairs = pairs
                .filter { it.first.value.bitDifference(it.second.value) == 0 }
                .filter {
                    val pairs = (it.first.key to it.second.key).symmetricalPairs(maxIndex)
                    val matchingPairs = pairs.count {
                        sliceProvider(it.first).bitDifference(sliceProvider(it.second)) == offBy
                    }
                    matchingPairs == (offenders ?: pairs.size)
                }

            return validPairs.firstOrNull()?.second?.key
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
