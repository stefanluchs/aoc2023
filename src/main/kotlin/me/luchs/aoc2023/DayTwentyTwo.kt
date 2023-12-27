package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Coordinate3D
import me.luchs.aoc2023.shared.cartesianProduct
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val number = AtomicInteger()

data class DayTwentyTwo(val input: String) : Day<Int> {


    override fun partOne(): Int {
        val bricks = input.lines().map { Brick(it) }

        val settled = mutableListOf<Brick>()
        val queue = bricks.sortedBy { it.minZ() }.toMutableList()

        val map = mutableMapOf<Brick, List<Brick>>()

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
            }
        }

        val rows = settled.groupBy { it.minZ() }

        val unsafe = map.entries
            .filter { it.value.size < 2 }
            .flatMap { it.value }
            .distinct()

        return bricks.size - unsafe.size
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

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
