package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DayNineTest {

    private val input =
        """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent()

    @Test
    fun partOne() {
        val result = DayNine(input).partOne()
        Assertions.assertEquals(114L, result)
    }

    @Test
    fun partOneSeries0() {
        val result = DayNine.Series(input)[0].predictNextValue()
        Assertions.assertEquals(18L, result)
    }

    @Test
    fun partOneSeries1() {
        val result = DayNine.Series(input)[1].predictNextValue()
        Assertions.assertEquals(28L, result)
    }

    @Test
    fun partOneSeries2() {
        val result = DayNine.Series(input)[2].predictNextValue()
        Assertions.assertEquals(68L, result)
    }

    @Test
    fun partOneSeriesNegative() {
        val seriesInput =
            "-5 -8 -11 -14 -17 -20 -23 -26 -29 -32 -35 -38 -41 -44 -47 -50 -53 -56 -59 -62 -65"
        val result = DayNine.Series(seriesInput)[0].predictNextValue()
        Assertions.assertEquals(-68L, result)
    }

    @Test
    fun partOneSeriesLarge() {
        val seriesInput =
            "9 30 73 159 318 589 1020 1668 2599 3888 5619 7885 10788 14439 18958 24474 31125 39058 48429 59403 72154"
        val result = DayNine.Series(seriesInput)[0].predictNextValue()
        Assertions.assertEquals(86865L, result)
    }

    @Test
    fun series() {
        val result = DayNine.Series(input)
        assertEquals(3, result.size)
    }

    @Test
    fun partTwo() {
        val result = DayNine(input).partTwo()
        Assertions.assertEquals(2, result)
    }
}
