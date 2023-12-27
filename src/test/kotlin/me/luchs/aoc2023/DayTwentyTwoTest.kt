package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


class DayTwentyTwoTest {

    private val input = """
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayTwentyTwo(input).partOne()
        Assertions.assertEquals(5, result)
    }

    @Test
    fun brick_volume_1() {
        val brick = DayTwentyTwo.Brick("2,2,2~2,2,2")
        Assertions.assertEquals(1, brick.volume)
    }

    @ParameterizedTest
    @ValueSource(strings = ["0,0,10~1,0,10", "0,0,10~0,1,10"])
    fun brick_volume_2(row: String) {
        val brick = DayTwentyTwo.Brick(row)
        Assertions.assertEquals(2, brick.volume)
    }

    @Test
    fun brick_volume_10() {
        val brick = DayTwentyTwo.Brick("0,0,1~0,0,10")
        Assertions.assertEquals(10, brick.volume)
    }

    @Test
    fun partTwo() {
        val result = DayTwentyTwo(input).partTwo()
        //Assertions.assertEquals(expected, result)
    }

}