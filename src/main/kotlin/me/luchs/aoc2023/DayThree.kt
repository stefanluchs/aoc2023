package me.luchs.aoc2023

import kotlin.math.abs

data class DayThree(val input: String) : Day<Int> {
    override fun partOne(): Int {
        val symbols = input
            .lines()
            .flatMapIndexed { row, line ->
                line.toCharArray()
                    .mapIndexed { column, c -> Symbol(c, column, row) }
                    .filter { it.char != '.' && !it.char.isDigit() }
            }
            .map { it.coordinate }

        val numbers = input.lines()
            .flatMapIndexed { row, line ->
                Regex("\\d+")
                    .findAll(line)
                    .filter { it ->
                        it.range.any { column -> symbols.any { it.isAdjacentTo(column, row) } }
                    }
                    .map { it.value }
            }
            .sumOf { it.toInt() }

        return numbers

    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    private data class Symbol(val char: Char, val coordinate: Coordinate2D) {
        constructor(char: Char, column: Int, row: Int) : this(char, Coordinate2D(column, row))
    }

    private data class Coordinate2D(val column: Int, val row: Int) {
        fun isAdjacentTo(column: Int, row: Int): Boolean {
            return this.column == column && this.row == row // identity
                    || this.column == column && abs(this.row - row) == 1 // above or below
                    || this.row == row && abs(this.column - column) == 1 // left or right
                    || abs(this.column - column) == 1 && abs(this.row - row) == 1 // diagonal
        }

    }
}