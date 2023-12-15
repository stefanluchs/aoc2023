package me.luchs.aoc2023

import me.luchs.aoc2023.DayFifteen.Step.Companion.hash

data class DayFifteen(val input: String) : Day<Int> {

    override fun partOne(): Int = Step(input).sumOf { it.value.hash() }

    override fun partTwo(): Int = mutableMapOf<Int, List<Lens>>()
        .let {
            Step(input).forEach { step -> step.applyTo(it) }
            return@let it
        }
        .entries
        .flatMap { it.value.mapIndexed { slot, lens -> (it.key + 1) * (slot + 1) * lens.focalLength } }
        .sumOf { it }

    data class Lens(val label: String, val focalLength: Int)

    data class Step(
        val value: String,
        val label: String,
        val operation: Operation,
        val focalLength: Int?
    ) {
        companion object {
            operator fun invoke(input: String): List<Step> {
                return input
                    .split(',')
                    .map {
                        Step(
                            value = it,
                            label = it.split('=', '-')[0],
                            operation = if (it.contains('=')) Operation.EQUAL else Operation.DASH,
                            focalLength = if (it.contains('=')) it.split('=')[1].toInt() else null
                        )
                    }
            }

            fun String.hash(): Int = this.toCharArray()
                .map { it.code }
                .fold(initial = 0) { acc, next -> ((acc + next) * 17) % 256 }
        }

        fun applyTo(boxes: MutableMap<Int, List<Lens>>) {
            val key = this.label.hash()

            when (operation) {
                Operation.DASH -> applyOperationDashTo(key, boxes)
                Operation.EQUAL -> applyOperationEqualTo(key, boxes)
            }
        }

        private fun applyOperationDashTo(key: Int, boxes: MutableMap<Int, List<Lens>>) {
            val lenses = boxes[key]
            if (lenses != null) {
                // remove lens by label if it exists
                boxes[key] = lenses.filterNot { it.label == this.label }
            }
        }

        private fun applyOperationEqualTo(key: Int, boxes: MutableMap<Int, List<Lens>>) {
            val lenses = boxes[key]
            if (lenses == null) {
                // box has no entry -> add lens as first entry
                boxes[key] = mutableListOf(this.toLens())
            } else {
                val existingLensIndex = lenses.indexOfFirst { it.label == this.label }
                if (existingLensIndex >= 0) {
                    // lens already present in box -> replace focal length at current position
                    val mutableLenses = lenses.toMutableList()
                    mutableLenses[existingLensIndex] = this.toLens()
                    boxes[key] = mutableLenses.toList()
                } else {
                    // lens is not present in box -> add lens at last position
                    val mutableLenses = lenses.toMutableList()
                    mutableLenses.addLast(this.toLens())
                    boxes[key] = mutableLenses.toList()
                }
            }
        }

        private fun toLens(): Lens = Lens(this.label, this.focalLength!!)

        enum class Operation {
            DASH, EQUAL
        }

    }

}
