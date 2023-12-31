package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Coordinate3D
import me.luchs.aoc2023.shared.cartesianProduct
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val number = AtomicInteger()

data class DayTwentyTwo(val input: String) : Day<Int> {

    private val bricks = input.lines().map { Brick(it) }

    override fun partOne(): Int {
        val (_, map) = bricks.settle()
        val unsafe = map.unsafeBricks()
        return bricks.size - unsafe.size
    }

    override fun partTwo(): Int {
        val (settled, map) = bricks.settle()
        return map.unsafeBricks().sumOf { settled.countFallingWithout(it) }
    }

    private fun Iterable<Brick>.countFallingWithout(brick: Brick): Int {
        val bricks = this.toMutableList()
        bricks.remove(brick)
        val (_, _, count) = bricks.settle()
        return count
    }

    private fun Iterable<Brick>.settle(): Triple<List<Brick>, Map<Brick, List<Brick>>, Int> {
        val settled = mutableListOf<Brick>()
        val map = mutableMapOf<Brick, List<Brick>>()
        val falling = mutableSetOf<Int>()

        val queue = this.sortedBy { it.minZ() }.toMutableList()
        while (queue.isNotEmpty()) {
            val brick = queue.removeFirst()
            val below = brick.isOnOtherBrick(settled)
            if (brick.isOnGround() || below.isNotEmpty()) {
                println("Brick $brick has settled")
                settled += brick
                map[brick] = below
            } else {
                println("Move brick $brick down")
                queue.addFirst(brick.moveDown())
                falling.add(brick.id)
            }
        }

        return Triple(settled, map, falling.size)
    }

    private fun Map<Brick, List<Brick>>.unsafeBricks(): List<Brick> = this.entries
        .filter { it.value.size < 2 }
        .flatMap { it.value }
        .distinct()


    data class Brick(val id: Int, val start: Coordinate3D, val end: Coordinate3D) {
        companion object {
            operator fun invoke(row: String): Brick {
                val (start, end) = row
                    .split('~')
                    .map { it.split(',').map { it.toInt() } }
                    .map { Coordinate3D(it[0], it[1], it[2]) }
                return Brick(number.getAndIncrement(), start, end)
            }
        }

        val volume: Int
            get() = abs(start.x - end.x) + abs(start.y - end.y) + abs(start.z - end.z) + 1

        fun moveDown(): Brick = Brick(id, start.down(), end.down())

        fun isOnOtherBrick(other: List<Brick>): List<Brick> = other
            .filter { max(it.start.z, it.end.z) + 1 == min(start.z, end.z) }
            .filter { it.isBelow(this) }

        fun isBelow(other: Brick): Boolean {
            val thisX = this.xRange()
            val thisY = this.yRange()

            val otherX = other.xRange()
            val otherY = other.yRange()

            val xRange = thisX.intersect(otherX)
            val yRange = thisY.intersect(otherY)
            return xRange.isNotEmpty() && yRange.isNotEmpty()
        }

        private fun xRange(): IntRange = min(start.x, end.x)..max(start.x, end.x)
        private fun yRange(): IntRange = min(start.y, end.y)..max(start.y, end.y)

        fun minZ(): Int = min(start.z, end.z)

        fun isSupporting(other: List<Brick>): Boolean {
            return other.any { this.isBelow(it) }
        }

        fun isOnGround(): Boolean = start.z == 1 || end.z == 1

    }

}
