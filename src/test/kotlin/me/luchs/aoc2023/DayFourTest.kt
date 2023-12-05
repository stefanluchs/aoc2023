package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayFourTest {

    val input =
        """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent()

    @Test
    fun partOne() {
        val result = DayFour(input).partOne()
        assertEquals(13, result)
    }

    @Test
    fun partTwo() {
        val result = DayFour(input).partTwo()
        assertEquals(30, result)
    }

    @Test
    fun parseScratchcards() {
        // when: Scratchcard are parsed from input
        val results = DayFour.Scratchcard.ofInput(input)

        // then: number of results is number of lines of input
        assertEquals(6, results.size)

        // and: id is correct
        assertEquals(1, results[0].id)
        assertEquals(2, results[1].id)
        assertEquals(3, results[2].id)

        // and: winning numbers are correct
        assertEquals(listOf(41, 48, 83, 86, 17), results[0].winningNumbers)

        // and: values are correct
        assertEquals(listOf(83, 86, 6, 31, 17, 9, 48, 53), results[0].numbers)
    }

    @Test
    fun points() {
        // given: a Scratchcard
        val scratchcard = DayFour.Scratchcard.ofInput(input)[0]

        // when: copyIds are calculated
        val result = scratchcard.points()

        // then: points are calculated correctly
        assertEquals(8, result)
    }

    @Test
    fun scratchcardCopyIds() {
        // given: a Scratchcard
        val scratchcard = DayFour.Scratchcard.ofInput(input)[0]

        // when: copyIds are calculated
        val result = scratchcard.copyIds()

        // then: result contains only matches
        assertEquals(listOf(2, 3, 4, 5), result)
    }

}
