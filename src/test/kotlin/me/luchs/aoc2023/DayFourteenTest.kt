package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Matrix
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DayFourteenTest {

    private val input =
        """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()

    private val tilted = """
        OOOO.#.O..
        OO..#....#
        OO..O##..O
        O..#.OO...
        ........#.
        ..#....#.#
        ..O..#.O.O
        ..O.......
        #....###..
        #....#....
    """.trimIndent()

    private val cycle1 = """
        .....#....
        ....#...O#
        ...OO##...
        .OO#......
        .....OOO#.
        .O#...O#.#
        ....O#....
        ......OOOO
        #...O###..
        #..OO#....
    """.trimIndent()

    private val cycle2 = """
        .....#....
        ....#...O#
        .....##...
        ..O#......
        .....OOO#.
        .O#...O#.#
        ....O#...O
        .......OOO
        #..OO###..
        #.OOO#...O
    """.trimIndent()

    private val cycle3 = """
        .....#....
        ....#...O#
        .....##...
        ..O#......
        .....OOO#.
        .O#...O#.#
        ....O#...O
        .......OOO
        #...O###.O
        #.OOO#...O
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayFourteen(input).partOne()
        Assertions.assertEquals(136, result)
    }

    @Test
    fun string() {
        val map = Matrix(input).toString()
        Assertions.assertEquals(input, map)
    }

    @Test
    fun rotateMatrixLeft() {
        val result = Matrix(input).tiltUp(moving = 'O', fixed = '#', blank = '.')
            .rotateLeft()
            .rotateLeft()
            .rotateLeft()
            .rotateLeft().toString()
        Assertions.assertEquals(tilted, result)
    }

    @Test
    fun rotateMatrixRight() {
        val result = Matrix(input).tiltUp(moving = 'O', fixed = '#', blank = '.')
            .rotateRight()
            .rotateRight()
            .rotateRight()
            .rotateRight().toString()
        Assertions.assertEquals(tilted, result)
    }

    @Test
    fun tilted() {
        val result = Matrix(input).tiltUp(moving = 'O', fixed = '#', blank = '.').toString()
        Assertions.assertEquals(tilted, result)
    }

    @Test
    fun cycle1() {
        val result = Matrix(input).tiltCycle().toString()
        Assertions.assertEquals(cycle1, result)
    }

    @Test
    fun cycle2() {
        val result = Matrix(input).tiltCycle().tiltCycle().toString()
        Assertions.assertEquals(cycle2, result)
    }

    @Test
    fun cycle3() {
        val result = Matrix(input).tiltCycle().tiltCycle().tiltCycle().toString()
        Assertions.assertEquals(cycle3, result)
    }

    @Test
    fun partTwo() {
        val result = DayFourteen(input).partTwo()
        Assertions.assertEquals(64, result)
    }

}