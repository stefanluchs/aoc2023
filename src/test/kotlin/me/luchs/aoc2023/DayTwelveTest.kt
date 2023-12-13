package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DayTwelveTest {

    private val inputClear =
        """
        #.#.### 1,1,3
        .#...#....###. 1,1,3
        .#.###.#.###### 1,3,1,6
        ####.#...#... 4,1,1
        #....######..#####. 1,6,5
        .###.##....# 3,2,1
        """.trimIndent()

    private val input =
        """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
        """.trimIndent()

    @Test
    fun partOne() {
        val result = DayTwelve(input).partOne()
        Assertions.assertEquals(21, result)
    }

    @Test
    fun springs() {
        val result = DayTwelve.Springs(inputClear)
        Assertions.assertEquals(6, result.size)
    }

    @Test
    fun springs_record_to_groups() {
        val result = DayTwelve.Springs(inputClear)
        Assertions.assertEquals(result[0].recordToGroups(), result[0].groups)
        Assertions.assertEquals(result[1].recordToGroups(), result[1].groups)
        Assertions.assertEquals(result[2].recordToGroups(), result[2].groups)
        Assertions.assertEquals(result[3].recordToGroups(), result[3].groups)
        Assertions.assertEquals(result[4].recordToGroups(), result[4].groups)
        Assertions.assertEquals(result[5].recordToGroups(), result[5].groups)
    }

    @Test
    fun partTwo() {
        val result = DayTwelve(input).partTwo()
        Assertions.assertEquals(525152, result)
    }
}
