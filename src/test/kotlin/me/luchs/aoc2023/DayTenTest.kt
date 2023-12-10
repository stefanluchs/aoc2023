package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DayTenTest {

    private val input = """
        -L|F7
        7S-7|
        L|7||
        -L-J|
        L|-JF

    """.trimIndent()

    private val input2 =
        """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.L
        """.trimIndent()

    @Test
    fun partOne() {
        val result = DayTen(input).partOne()
        Assertions.assertEquals(4, result)
    }

    @Test
    fun partOne_input2() {
        val result = DayTen(input2).partOne()
        Assertions.assertEquals(8, result)
    }

    @Test
    fun positionOf() {
        val result = DayTen.Maze(input).startPosition('S')
        Assertions.assertEquals(1, result.row)
        Assertions.assertEquals(1, result.column)
    }

    @Test
    fun neighborsOf() {
        val maze = DayTen.Maze(input)
        val position = maze.startPosition('S')
        val result = maze.neighborsOf(position)
        Assertions.assertEquals(4, result.size)
    }

    @Test
    fun connectionsOf() {
        val maze = DayTen.Maze(input)
        val position = maze.startPosition('S')
        val result = maze.connectionsOf(position, start = true)
        Assertions.assertEquals(2, result.size)
    }

    val example3 = """
        ...........
        .S-------7.
        .|F-----7|.
        .||.....||.
        .||.....||.
        .|L-7.F-J|.
        .|..|.|..|.
        .L--J.L--J.
        ...........
    """.trimIndent()

    val example4 = """
        .F----7F7F7F7F-7....
        .|F--7||||||||FJ....
        .||.FJ||||||||L7....
        FJL7L7LJLJ||LJ.L-7..
        L--J.L7...LJS7F-7L7.
        ....F-J..F7FJ|L7L7L7
        ....L7.F7||L7|.L7L7|
        .....|FJLJ|FJ|F7|.LJ
        ....FJL-7.||.||||...
        ....L---J.LJ.LJLJ...
    """.trimIndent()

    val example5 = """
        FF7FSF7F7F7F7F7F---7
        L|LJ||||||||||||F--J
        FL-7LJLJ||||||LJL-77
        F--JF--7||LJLJ7F7FJ-
        L---JF-JLJ.||-FJLJJ7
        |F|F-JF---7F7-L7L|7|
        |FFJF7L7F-JF7|JL---7
        7-L-JL7||F7|L7F-7F7|
        L.L7LFJ|||||FJL7||LJ
        L7JLJL-JLJLJL--JLJ.L
    """.trimIndent()

    val example6 = """
        ..........
        .S------7.
        .|F----7|.
        .||OOOO||.
        .||OOOO||.
        .|L-7F-J|.
        .|II||II|.
        .L--JL--J.
        ..........
    """.trimIndent()

    @Test
    fun partTwo() {
        val result = DayTen(example3).partTwo()
        Assertions.assertEquals(4, result)
    }

    @Test
    fun partTwo_example4() {
        val result = DayTen(example4).partTwo()
        Assertions.assertEquals(8, result)
    }

    @Test
    fun partTwo_example5() {
        val result = DayTen(example5).partTwo()
        Assertions.assertEquals(10, result)
    }

    @Test
    fun partTwo_example6() {
        val result = DayTen(example6).partTwo()
        Assertions.assertEquals(4, result)
    }


}