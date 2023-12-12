package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Point
import me.luchs.aoc2023.shared.distinctPairs

data class DayEleven(val input: String) : Day<Long> {
    override fun partOne(): Long {
        val space = Space(input).expand()
        val pairs = space.galaxies.distinctPairs()
        return pairs.sumOf { it.first.manhattanDistanceTo(it.second) }
    }

    override fun partTwo(): Long {
        return partTwo(1000000)
    }

    fun partTwo(expansionFactor: Long): Long {
        val space = Space(input).expand(expansionFactor)
        val pairs = space.galaxies.distinctPairs()
        return pairs.sumOf { it.first.manhattanDistanceTo(it.second) }
    }

    data class Space(val galaxies: List<Point>) {
        companion object {
            operator fun invoke(input: String): Space {
                val galaxies: List<Point> =
                    input
                        .lines()
                        .flatMapIndexed { row, line ->
                            line.toCharArray().mapIndexed { column, char ->
                                Point(row.toLong(), column.toLong(), char)
                            }
                        }
                        .filterNot { it.value == '.' }
                return Space(galaxies)
            }
        }

        /**
         * Expands the current Space by increasing the number of rows and columns based on the expansion
         * factor.
         *
         * @param expansionFactor The factor by which to expand the Space. Default value is 2.
         * @return The expanded Space object.
         */
        fun expand(expansionFactor: Long = 2): Space {
            val rows = 0..galaxies.maxOf { it.row }
            val columns = 0..galaxies.maxOf { it.column }

            val expansionRows = rows.subtract(galaxies.map { it.row }.toSet())
            val expansionColumns = columns.subtract(galaxies.map { it.column }.toSet())

            val expandedGalaxies =
                galaxies.map { galaxy ->
                    val row =
                        galaxy.row + (expansionRows.filter { it < galaxy.row }.size * (expansionFactor - 1))
                    val column =
                        galaxy.column +
                            (expansionColumns.filter { it < galaxy.column }.size * (expansionFactor - 1))
                    Point(row, column, galaxy.value)
                }

            return Space(expandedGalaxies)
        }

    }
}
