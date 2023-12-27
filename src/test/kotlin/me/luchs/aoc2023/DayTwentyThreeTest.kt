package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DayTwentyThreeTest {

    val example = """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayTwentyThree(example).partOne()
        Assertions.assertEquals(94, result)
    }

    @Test
    fun partTwo() {
        val result = DayTwentyThree(example).partTwo()
        Assertions.assertEquals(7, result)
    }

}