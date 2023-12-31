package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DayThreeTest {

    val input =
        """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
        """.trimIndent()

    @Test
    fun partOne() {
        val result = DayThree(input).partOne()
        assertEquals(4361, result)
    }

    @Test
    fun partTwo() {
        val result = DayThree(input).partTwo()
        assertEquals(467835, result)
    }
}
