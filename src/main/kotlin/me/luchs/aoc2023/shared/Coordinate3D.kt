package me.luchs.aoc2023.shared

data class Coordinate3D(val x: Int, val y: Int, val z: Int) {

    fun down(): Coordinate3D = Coordinate3D(x, y, z - 1)

}

infix fun <T, U> Iterable<T>.cartesianProduct(other: Iterable<U>): List<Pair<T, U>> =
    flatMap { first -> other.map { second -> Pair(first, second) } }

fun Array<Int>.toCoordinate3D(): Coordinate3D =
    if (this.size == 3) Coordinate3D(this[0], this[1], this[2]) else throw IllegalArgumentException(
        "Can not convert $this into 3D Coordinate"
    )