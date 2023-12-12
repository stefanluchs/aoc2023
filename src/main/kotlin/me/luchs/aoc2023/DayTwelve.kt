package me.luchs.aoc2023

data class DayTwelve(val input: String) : Day<Int> {
    override fun partOne(): Int {
        return Springs(input).sumOf { it.numberOfArrangement() }
    }

    override fun partTwo(): Int {
        return Springs(input).sumOf { it.numberOfArrangement() }
    }

    data class Springs(val record: String, val groups: List<Int>) {
        companion object {
            operator fun invoke(input: String): List<Springs> {
                return input.lines().map { line ->
                    val (record, groupInput) = line.split(' ')
                    val groups = groupInput.split(',').map { it.toInt() }
                    Springs(record, groups)
                }
            }
        }

        fun recordToGroups(): List<Int> {
            return record.toGroups()
        }

        fun numberOfArrangement(prefix: String? = null): Int {
            val unknown = record
                .mapIndexedNotNull { index, char -> if (char == '?') index else null }
                .toMutableList()

            val input = record.toCharArray()

            var arrangements: List<CharArray> = mutableListOf(input)

            while (unknown.isNotEmpty()) {
                val index = unknown.removeFirst()
                arrangements = arrangements.flatMap { it.fillAt(index) }
            }

            val possible = arrangements.map { it.toGroups() }.filter { it == groups }

            return possible.size
        }

        private fun CharArray.toGroups(): List<Int> {
            return this.joinToString("").toGroups()
        }

        private fun String.toGroups(): List<Int> {
            return this
                .split(Regex("[.]+"))
                .map { it.length }
                .filter { it > 0 }
        }

        private fun CharArray.fillAt(index: Int): List<CharArray> {
            val operational = this.clone()
            val damaged = this.clone()
            operational[index] = '.'
            damaged[index] = '#'
            return listOf(operational, damaged)
        }

    }

}
