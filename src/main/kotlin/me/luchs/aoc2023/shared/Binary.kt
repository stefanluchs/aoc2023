package me.luchs.aoc2023.shared

typealias Binary = String

fun Binary.asString(): String {
    return this
}

fun Binary.oneBitOff(other: Binary): Boolean {
    if (this.length != other.length) {
        return false
    }
    return this.zip(other).count { it.first != it.second } == 1
}

fun Binary.bitDifference(other: Binary): Int {
    return this.zip(other).count { it.first != it.second }
}

fun Binary.asInt(): Int {
    return this.toInt(2)
}
