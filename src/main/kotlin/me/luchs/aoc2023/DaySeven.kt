package me.luchs.aoc2023

import kotlin.comparisons.compareBy

data class DaySeven(val input: String) : Day<Int> {

    override fun partOne(): Int {
        return Hand(input, withJoker = false).calculateTotalWinnings()
    }

    override fun partTwo(): Int {
        return Hand(input, withJoker = true).calculateTotalWinnings()
    }

    private fun List<Hand>.calculateTotalWinnings(): Int {
        return this.sorted().mapIndexed { index, hand -> hand.bid * (index + 1) }.sumOf { it }
    }

    data class Hand(val id: String, val cards: List<Int>, val groups: List<Int>, val bid: Int) :
        Comparable<Hand> {

        companion object {

            // the joker id is chosen as an arbitrary number which is smaller than any other card id
            private const val JOKER_ID = 1

            // possible card ids when using the String.toCards method to transform the non numeric
            // cards into an integer representation
            private val CARD_IDS = 2..14

            operator fun invoke(input: String, withJoker: Boolean): List<Hand> {
                return input.lines().map { line ->
                    val (hand, bid) = line.split(' ')
                    val cards = hand.toCards(withJoker)
                    val groups = cards.toGroups(withJoker)
                    Hand(hand, cards, groups, bid.toInt())
                }
            }

            /**
             * Converts a string representation of cards to a list of integers.
             *
             * Each character in the input string is mapped to its corresponding card value. The values
             * are determined as follows: 'T' is mapped to 10, 'J' is mapped to 11 if `withJoker` is true,
             * otherwise is mapped to JOKER_ID, 'Q' is mapped to 12, 'K' is mapped to 13, 'A' is mapped to
             * 14, all other characters are mapped to their integer representation.
             *
             * @param withJoker A flag indicating whether to treat 'J' as 11 (true) or as JOKER_ID
             * (false).
             *
             * @return A list of integers representing the cards.
             */
            private fun String.toCards(withJoker: Boolean): List<Int> {
                return this.map { card ->
                    when (card) {
                        'A' -> 14
                        'K' -> 13
                        'Q' -> 12
                        'J' -> if (withJoker) JOKER_ID else 11
                        'T' -> 10
                        else -> card.digitToInt() // 9 - 2
                    }
                }
            }

            /**
             * Returns a list of grouped integers based on the input list of integers.
             *
             * @param withJoker Whether to use the joker as a wildcard for any card. If true, the method
             * calculates all possible groups when using the joker as a replacement for any card. If
             * false, the method groups the cards by card id and sorts them in descending order by the
             * group size.
             * @return A list of grouped integers. If withJoker is true, the method returns the best
             * possible solution for the replacement by sorting all possible outcomes and returning the
             * best. If withJoker is false, the method returns a list of group sizes, sorted in descending
             * order.
             */
            private fun List<Int>.toGroups(withJoker: Boolean): List<Int> {
                return if (withJoker) {
                    // calculate all possible groups when using the joker as wildcard for any card id
                    val possibleGroups =
                        CARD_IDS.map { cardId ->
                            this.map { if (it == JOKER_ID) cardId else it }.toGroups(false)
                        }
                    // find the best possible solution for the replacement by sorting all possible outcomes
                    possibleGroups.sortedWith(compareBy({ it.getOrNull(0) }, { it.getOrNull(1) })).last()
                } else {
                    // group cards by card id and sort descending by the group size
                    this.groupBy { it }.map { it.value.size }.sortedByDescending { it }
                }
            }
        }

        override fun compareTo(other: Hand): Int {
            return compareBy<Hand>(
                // compare groups (largest to smallest)
                { it.groups.getOrNull(0) },
                { it.groups.getOrNull(1) },
                // compare cards in ascending order
                { it.cards[0] },
                { it.cards[1] },
                { it.cards[2] },
                { it.cards[3] },
                { it.cards[4] }
            )
                .compare(this, other)
        }
    }
}
