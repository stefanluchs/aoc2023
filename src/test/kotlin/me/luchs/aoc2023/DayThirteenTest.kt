package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DayThirteenTest {

    private val input =
        """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()


    @Test
    fun map() {
        val result = DayThirteen.Mirrors(input)
        Assertions.assertEquals(2, result.size)
    }

    @Test
    fun partOne() {
        val result = DayThirteen(input).partOne()
        Assertions.assertEquals(405, result)
    }

    @Test
    fun partTwo() {
        val result = DayThirteen(input).partTwo()
        Assertions.assertEquals(400, result)
    }

}