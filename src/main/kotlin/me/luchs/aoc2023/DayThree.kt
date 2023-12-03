package me.luchs.aoc2023

import kotlin.math.abs

data class DayThree(val input: String) : Day<Int> {
    override fun partOne(): Int {
        // find all symbols
        val symbols = input
            .lines()
            .flatMapIndexed { row, line ->
                line.toCharArray()
                    .mapIndexed { column, c -> Symbol(c, column, row) }
                    .filter { it.char != '.' && !it.char.isDigit() }
            }
            .map { it.coordinate }

        return input
            .lines()
            // find all numbers with adjacent symbol
            .flatMapIndexed { row, line ->
                Regex("\\d+")
                    .findAll(line)
                    .filter { it ->
                        it.range.any { column -> symbols.any { it.isAdjacentTo(column, row) } }
                    }
                    .map { it.value }
            }
            // compute sum of numbers with adjacent symbol
            .sumOf { it.toInt() }
    }

    override fun partTwo(): Int {
        // get all numbers with respective coordinates
        val numbers = input
            .lines()
            .flatMapIndexed { row, line ->
                Regex("\\d+")
                    .findAll(line)
                    .map { Number(it.value.toInt(), it.range, row) }
            }

        return input
            .lines()
            // find possible gear locations
            .flatMapIndexed { row, line ->
                line.toCharArray()
                    .mapIndexed { column, c -> Symbol(c, column, row) }
                    .filter { it.char == '*' }
            }
            .map { it.coordinate }
            // verify possible gear has exactly two adjacent numbers
            .filter { possibleGear ->
                numbers.filter { number ->
                    number.coordinates.any { possibleGear.isAdjacentTo(it) }
                }.size == 2
            }
            // calculate the sum of the product of the adjacent numbers of each gear
            .sumOf { gear ->
                numbers
                    .filter { number ->
                        number.coordinates.any { gear.isAdjacentTo(it) }
                    }
                    .map { it.value }
                    .reduce { n1, n2 -> n1 * n2 }
            }
    }

    private data class Number(val value: Int, val coordinates: List<Coordinate2D>) {
        constructor(value: Int, columns: IntRange, row: Int) : this(
            value,
            columns.map { Coordinate2D(it, row) })
    }

    private data class Symbol(val char: Char, val coordinate: Coordinate2D) {
        constructor(char: Char, column: Int, row: Int) : this(char, Coordinate2D(column, row))
    }

    private data class Coordinate2D(val column: Int, val row: Int) {
        fun isAdjacentTo(other: Coordinate2D): Boolean {
            return isAdjacentTo(other.column, other.row)
        }

        fun isAdjacentTo(column: Int, row: Int): Boolean {
            return this.column == column && this.row == row // identity
                    || this.column == column && abs(this.row - row) == 1 // above or below
                    || this.row == row && abs(this.column - column) == 1 // left or right
                    || abs(this.column - column) == 1 && abs(this.row - row) == 1 // diagonal
        }

    }
}