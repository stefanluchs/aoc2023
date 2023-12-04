package me.luchs.aoc2023

import kotlin.math.pow

data class DayFour(val input: String) : Day<Int> {
    override fun partOne(): Int {
        // calculate the sum of points for each card
        return Scratchcard.ofInput(input).sumOf { it.points() }
    }

    override fun partTwo(): Int {
        // get all cards
        val scratchcards = Scratchcard.ofInput(input)

        // create initial map card id to number of cards for id
        val numberOfCards = scratchcards.map { it.id }.associateWith { 1 }.toMutableMap()

        // iterate over all cards
        scratchcards.forEach {
            // determine ids of cards to copy
            val copyIds = it.copyIds()

            // get count for current card
            val numberOfScratchcard = numberOfCards[it.id]!!

            // add count of the current card to each of the cards to copy
            for (copyId in copyIds) {
                val copyNumber = numberOfCards[copyId]!!
                numberOfCards[copyId] = copyNumber + numberOfScratchcard
            }
        }

        // calculate sum of cards
        return numberOfCards.map { it.value }.sumOf { it }
    }

    data class Scratchcard(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {
        companion object {
            /**
             * Converts the given input string into a list of Scratchcard objects.
             *
             * @param input The input string to be converted.
             * @return A list of Scratchcard objects created from the input string.
             */
            fun ofInput(input: String): List<Scratchcard> {
                return input.lines().map { ofLine(it) }.toMutableList()
            }

            /**
             * Constructs a new Scratchcard object from the given line.
             *
             * @param line the input line containing the scratchcard information
             * @return the constructed Scratchcard object
             */
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

        /**
         * Returns a list of IDs copied from the current object.
         *
         * This method retrieves the number of points from the `matches()` method and creates a list of IDs starting from
         * the current ID plus 1 up to the current ID plus the number of points. If there are no points, an empty list is returned.
         *
         * @return A list of IDs copied from the current object.
         */
        fun copyIds(): List<Int> {
            val points = matches()
            return if (points > 0) {
                IntRange(id + 1, id + points).toList()
            } else {
                emptyList()
            }
        }

        /**
         * Calculates the number of points based on the number of matches.
         *
         * @return The calculated points
         */
        fun points(): Int {
            return 2.0.pow(matches() - 1).toInt()
        }

        /**
         * Calculates the number of matches between the 'numbers' list and the 'winningNumbers' list.
         *
         * @return The count of matching numbers.
         */
        private fun matches(): Int {
            return numbers.intersect(winningNumbers.toSet()).size
        }
    }

}