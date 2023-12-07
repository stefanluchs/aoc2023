package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DaySevenTest {

    private val input =
        """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
        """.trimIndent()

    @Test
    fun partOne() {
        val result = DaySeven(input).partOne()
        assertEquals(6440, result)
    }

    @Test
    fun handParseInput() {
        val hands = DaySeven.Hand.invoke(input, withJoker = false)
        assertEquals(5, hands.size)

        assertEquals(5, hands[0].cards.size)
        assertEquals(765, hands[0].bid)
    }

    @Test
    fun handCompareTo() {
        val hands = DaySeven.Hand.invoke(input, withJoker = false).sorted()
        assertEquals("32T3K", hands[0].id)
        assertEquals("KTJJT", hands[1].id)
        assertEquals("KK677", hands[2].id)
        assertEquals("T55J5", hands[3].id)
        assertEquals("QQQJA", hands[4].id)
    }

    @Test
    fun partTwo() {
        val result = DaySeven(input).partTwo()
        assertEquals(5905, result)
    }

    @Test
    fun handCompareToWithJoker() {
        val hands = DaySeven.Hand.invoke(input, withJoker = true).sorted()
        assertEquals("32T3K", hands[0].id)
        assertEquals("KK677", hands[1].id)
        assertEquals("T55J5", hands[2].id)
        assertEquals("QQQJA", hands[3].id)
        assertEquals("KTJJT", hands[4].id)
    }
}
