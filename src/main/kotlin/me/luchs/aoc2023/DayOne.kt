package me.luchs.aoc2023

class DayOne {

    companion object {
        fun partOne(input: String): Int {
            return input.lines()
                    .map { it.toCharArray() }
                    .map { it.filter { char -> char.isDigit() } }
                    .map { it.joinToString(separator = "") }
                    .sumOf { it.toCalibrationValue() }
        }

        fun partTwo(input: String): Int {
            return input.lines().sumOf { it.toCalibrationValueWithString() }
        }

        private fun String.toCalibrationValue(): Int {
            if (this.isEmpty()) {
                throw IllegalArgumentException("Cannot convert empty String to calibration value")
            }

            if (1 == this.length) {
                return (this + this).toInt()
            }

            if (2 == this.length) {
                return this.toInt()
            }

            return String(charArrayOf(this.first(), this.last())).toInt()
        }

        private fun String.toCalibrationValueWithString(): Int {

            val digits = mutableListOf<Digit>()

            val firstDigitIndex = this.toCharArray().indexOfFirst { it.isDigit() }
            if (firstDigitIndex >= 0) {
                val firstDigit = this[firstDigitIndex]
                digits.addFirst(Digit(firstDigit, firstDigitIndex))
            }

            val lastDigitIndex = this.toCharArray().indexOfLast { it.isDigit() }
            if (lastDigitIndex >= 0) {
                val lastDigit = this[lastDigitIndex]
                digits.addFirst(Digit(lastDigit, lastDigitIndex))
            }

            listOf(
                    Digit('1', this.indexOf("one")),
                    Digit('2', this.indexOf("two")),
                    Digit('3', this.indexOf("three")),
                    Digit('4', this.indexOf("four")),
                    Digit('5', this.indexOf("five")),
                    Digit('6', this.indexOf("six")),
                    Digit('7', this.indexOf("seven")),
                    Digit('8', this.indexOf("eight")),
                    Digit('9', this.indexOf("nine")))
                    .sortedBy { it.index }
                    .firstOrNull() { it.index >= 0 }
                    ?.let { digits.addFirst(it) }

            listOf(
                    Digit('1', this.lastIndexOf("one")),
                    Digit('2', this.lastIndexOf("two")),
                    Digit('3', this.lastIndexOf("three")),
                    Digit('4', this.lastIndexOf("four")),
                    Digit('5', this.lastIndexOf("five")),
                    Digit('6', this.lastIndexOf("six")),
                    Digit('7', this.lastIndexOf("seven")),
                    Digit('8', this.lastIndexOf("eight")),
                    Digit('9', this.lastIndexOf("nine")))
                    .sortedBy { it.index }
                    .lastOrNull() { it.index >= 0 }
                    ?.let { digits.addLast(it) }

            val first = digits.minByOrNull { it.index }!!.value
            val last = digits.maxByOrNull { it.index }!!.value

            return String(charArrayOf(first, last)).toInt()
        }

        private data class Digit(val value: Char, val index: Int)
    }

}