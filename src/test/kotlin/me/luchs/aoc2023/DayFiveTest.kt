package me.luchs.aoc2023

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DayFiveTest {

    val input = """
        seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayFive(input).partOne()
        assertEquals(35, result)
    }

    @Test
    fun partTwo() {
    }

    @Test
    fun seedsFromInput() {
        val result = DayFive(input).seedsFromInput()
        assertTrue(listOf(79L, 14L, 55, 13).toLongArray().contentEquals(result))
    }

    @Test
    fun almanacOfInput() {
        val result = DayFive.Almanac.ofInput(input)
        assertEquals(18, result.foodMaps.size)
    }

    @Test
    fun foodMapsOfInput() {
        // given: relevant lines of the input
        val inputLine = input.lines()
        val mapsInput = inputLine.subList(2, inputLine.size)

        // when: mappings are created
        val results = DayFive.FoodMap.ofMapsInput(DayFive.Food.SEED, DayFive.Food.SOIL, mapsInput)

        // then: number of entries equals rows for food
        assertEquals(2, results.size)
    }

    @ParameterizedTest
    @ValueSource(longs = [98, 99])
    fun isInSourceRangeTrue(seed: Long) {
        // given: foodMap
        val inputLine = input.lines()
        val mapsInput = inputLine.subList(2, inputLine.size)
        val foodMap =
            DayFive.FoodMap.ofMapsInput(DayFive.Food.SEED, DayFive.Food.SOIL, mapsInput)[0]

        // expect: seed to be in source range
        assertTrue(foodMap.isInSourceRange(seed))
    }

    @ParameterizedTest
    @ValueSource(longs = [97, 100])
    fun isInSourceRangeFalse(seed: Long) {
        // given: foodMap
        val inputLine = input.lines()
        val mapsInput = inputLine.subList(2, inputLine.size)
        val foodMap =
            DayFive.FoodMap.ofMapsInput(DayFive.Food.SEED, DayFive.Food.SOIL, mapsInput)[0]

        // expect: seed to be in source range
        assertFalse(foodMap.isInSourceRange(seed))
    }

    @ParameterizedTest
    @CsvSource(
        "79, 82",
        "14, 43",
        "55, 86",
        "13, 35",
    )
    fun seedToLocation(seed: Long, location: Long) {
        // given: almanac
        val almanac = DayFive.Almanac.ofInput(input)

        // when: seed to location mapping is called
        val result = almanac.seedToLocation(seed)

        // then: location
        assertEquals(location, result)
    }

}
