package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DaySixTest {

    private val input =
        """
        Time:      7  15   30
        Distance:  9  40  200
        """.trimIndent()

    @Test
    fun partOne() {
        val result = DaySix(input).partOne()
        assertEquals(288, result)
    }

    @ParameterizedTest(name = "solve for time: {0} with distance: {1} with expected result: {2}")
    @CsvSource("7,9,4", "15,40,8", "30,200,9")
    fun solveTest(time: Long, distance: Long, expected: Long) {
        val result = DaySix.Race(time, distance).numberOfWaysToBeatDistance()
        assertEquals(expected, result)
    }

    @Test
    fun parseRaces() {
        val races = DaySix.Race(input)
        assertEquals(3, races.size)

        assertEquals(7, races[0].time)
        assertEquals(9, races[0].distance)

        assertEquals(15, races[1].time)
        assertEquals(40, races[1].distance)

        assertEquals(30, races[2].time)
        assertEquals(200, races[2].distance)
    }

    @Test
    fun parseAggregatedRace() {
        val result = DaySix.Race.ofAggregatedInput(input)
        assertEquals(71530, result.time)
        assertEquals(940200, result.distance)
    }

    @Test
    fun partTwo() {
        val result = DaySix(input).partTwo()
        assertEquals(71503, result)
    }
}
