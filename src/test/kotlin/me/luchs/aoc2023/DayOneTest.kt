package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DayOneTest {

    @Test
    fun partOneExample() {
        val exampleInput = """1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet  
        """.trimIndent()

        val result = DayOne(exampleInput).partOne()

        Assertions.assertEquals(142, result)
    }

    @Test
    fun partTwoExample() {
        val exampleInput = """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
""".trimIndent()

        val result = DayOne(exampleInput).partTwo()

        Assertions.assertEquals(281, result)
    }

    @Test
    fun partTwoHiddenExample() {
        val exampleInput = """eighthree
sevenine
""".trimIndent()
        val result = DayOne(exampleInput).partTwo()

        Assertions.assertEquals(83 + 79, result)
    }

}