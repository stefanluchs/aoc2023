package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DayTwentyFourTest {

    val example = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayTwentyFour(example).numberOfIntersectionsWithin(min = 7, max = 27)
        Assertions.assertEquals(2, result)
    }

    @Test
    fun partTwo() {
        val result = DayTwentyFour(example).partTwo()
        Assertions.assertEquals(47L, result)
    }

}