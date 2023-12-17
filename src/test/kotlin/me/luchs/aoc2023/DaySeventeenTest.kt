package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DaySeventeenTest {

    private val input = """
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
    """.trimIndent()


    @Test
    fun partOne() {
        val result = DaySeventeen(input).partOne()
        Assertions.assertEquals(102, result)
    }

    @Test
    fun partTwo() {
        val result = DaySeventeen(input).partTwo()
        //Assertions.assertEquals(51, result)
    }

}