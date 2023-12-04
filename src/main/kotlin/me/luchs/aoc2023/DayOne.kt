package me.luchs.aoc2023

data class DayOne(val input: String) : Day<Int> {

    override fun partOne(): Int {
        return input
            .lines()
            .map { it.toCharArray() }
            .map { it.filter { char -> char.isDigit() } }
            .sumOf { charsToInt(it.first(), it.last()) }
    }

    override fun partTwo(): Int {
        return input
            .lines()
            .map { it.toDigitsWithText() }
            .sumOf { charsToInt(it.first().value, it.last().value) }
    }

    private fun String.toDigitsWithText(): List<Digit> {
        // extract numbers as digits
        val digits: MutableList<Digit> =
            this.toCharArray()
                .mapIndexed { index, char -> filter { char.isDigit() }.map { Digit(char, index) } }
                .flatMap { it.asIterable() }
                .toMutableList()

        // add first and last text-based digits
        Number.entries
            .map { Digit(it.char, this.indexOf(it.string)) }
            .sortedBy { it.index }
            .firstOrNull() { it.index >= 0 }
            ?.let { digits.addFirst(it) }

        Number.entries
            .map { Digit(it.char, this.lastIndexOf(it.string)) }
            .sortedBy { it.index }
            .lastOrNull() { it.index >= 0 }
            ?.let { digits.addLast(it) }

        return digits.sortedBy { it.index }
    }

    private fun charsToInt(vararg char: Char): Int {
        return String(charArrayOf(*char)).toInt()
    }

    private data class Digit(val value: Char, val index: Int)

    private enum class Number(val char: Char, val string: String) {
        ONE('1', "one"),
        TWO('2', "two"),
        THREE('3', "three"),
        FOUR('4', "four"),
        FIVE('5', "five"),
        SIX('6', "six"),
        SEVEN('7', "seven"),
        EIGHT('8', "eight"),
        NINE('9', "nine")
    }
}
