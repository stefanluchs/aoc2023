package me.luchs.aoc2023

data class DayNine(val input: String) : Day<Long> {
    override fun partOne(): Long {
        return Series(input).sumOf { it.predictNextValue() }
    }

    override fun partTwo(): Long {
        return Series(input).sumOf { it.predictPreviousValue() }
    }

    data class Series(val values: List<Long>) {
        companion object {
            operator fun invoke(input: String): List<Series> {
                return input.lines().map { line -> line.split(' ').map { it.toLong() } }.map { Series(it) }
            }
        }

        /**
         * Predicts the next value in a sequence of numbers.
         *
         * @return The predicted next value as a Long.
         */
        fun predictNextValue(): Long {
            return values.predictValue(number = { it.last() }, operation = Long::plus)
        }

        /**
         * Predicts the previous value in a sequence of numbers.
         *
         * @return The predicted previous value.
         */
        fun predictPreviousValue(): Long {
            return values.predictValue(number = { it.first() }, operation = Long::minus)
        }

        /**
         * Predicts the value based on the given list using the specified number and operation
         * functions.
         *
         * @param number The function used to compute a number from the list.
         * @param operation The function used to perform an operation on the computed number and the
         * predicted value.
         * @return The predicted value.
         */
        private fun List<Long>.predictValue(
            number: (List<Long>) -> Long,
            operation: (Long, Long) -> Long
        ): Long {
            val differenceSeries = this.differenceSeries()
            return if (differenceSeries.sumOf { it } == 0L && differenceSeries.size < 2) {
                number(this)
            } else {
                operation(number(this), differenceSeries.predictValue(number, operation))
            }
        }

        /**
         * Calculates the difference series of a list of long values.
         *
         * The difference series is obtained by subtracting each element from its next element in the
         * list. For example, given the list [1, 4, 2, 6], the difference series is [3, -2, 4].
         *
         * @return The difference series as a list of long values.
         */
        private fun List<Long>.differenceSeries(): List<Long> {
            return this.zipWithNext().map { it.second - it.first }
        }
    }
}
