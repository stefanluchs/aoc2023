package me.luchs.aoc2023


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class DaySixTest {

    private val input = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DaySix(input).partOne()
        assertEquals(288, result)
    }

    @ParameterizedTest(name = "solve for time: {0} with distance: {1} with expected result: {2}")
    @CsvSource(
        "7,9,4",
        "15,40,8",
        "30,200,9"
    )
    fun solveTest(time: Int, distance: Int, expected: Int) {
        val result = DaySix.Race(time, distance).solvePartOne()
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
    fun partTwo() {
    }

}