package me.luchs.aoc2023.shared

fun Iterable<Long>.product() = this.reduce { acc, l -> acc * l }
