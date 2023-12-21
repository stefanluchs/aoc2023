package me.luchs.aoc2023

import me.luchs.aoc2023.shared.lcm

data class DayTwenty(val input: String) : Day<Long> {

    private val button = Pulse(value = false, source = "button", destination = "broadcaster")

    override fun partOne(): Long {

        val modules = modulesFromInput()

        var countLow = 0L
        var countHigh = 0L

        repeat(times = 1000) {
            val queue = ArrayDeque(listOf(button))
            while (queue.isNotEmpty()) {
                val pulse = queue.removeFirst()

                if (pulse.value) {
                    countHigh++
                } else {
                    countLow++
                }

                modules.process(pulse, queue)
            }
        }

        return countHigh * countLow
    }

    override fun partTwo(): Long {

        val modules = modulesFromInput()

        // nodes that have destination "zh" which is the only node to have destination "rx"
        // "zh" is a conjunction node -> all incoming pulses need
        // to be high in order for it to emmit a low pulse to "rx"

        // determine the lcm of iterations for the four independent cycles
        // in which these result in a high pulse respectively
        val nodes = mutableMapOf("ks" to 0L, "kb" to 0L, "sx" to 0L, "jt" to 0L)

        var index = 0L
        while (nodes.values.any { it == 0L }) {
            index++

            val queue = ArrayDeque(listOf(button))
            while (queue.isNotEmpty()) {
                val pulse = queue.removeFirst()

                if (pulse.source in nodes && pulse.value) {
                    nodes[pulse.source] = index
                }

                modules.process(pulse, queue)
            }
        }

        return nodes.values.lcm()
    }

    private fun modulesFromInput(): List<Module> = input
        .lines()
        .map { Module(it) }
        .registerConjunctionSources()

    private fun List<Module>.registerConjunctionSources(): List<Module> {
        val conjunctions = this.filterIsInstance<Conjunction>()
        conjunctions.forEach { conjunction ->
            this.filter { conjunction.name in it.destinations }
                .map { it.name }
                .forEach { conjunction.registerInput(it) }
        }
        return this
    }

    private fun List<Module>.process(pulse: Pulse, queue: ArrayDeque<Pulse>) = this
        .filter { pulse.destination == it.name }
        .flatMap { it.process(pulse) }
        .let { queue.addAll(it) }

    private data class Pulse(
        val value: Boolean = true,
        val source: String,
        val destination: String
    ) {
        // for command line visualization of the pluses
        override fun toString(): String {
            val pulse = if (value) "high" else "low"
            return "$source -$pulse-> $destination"
        }
    }

    private sealed interface Module {
        val name: String
        val destinations: List<String>
        fun process(pulse: Pulse): List<Pulse>

        companion object {
            operator fun invoke(row: String): Module = when {
                row.startsWith("broadcaster") -> Broadcaster(row)
                row.startsWith("%") -> FlipFlop(row)
                row.startsWith("&") -> Conjunction(row)
                else -> throw IllegalStateException("Can not process $row")
            }
        }
    }

    private data class FlipFlop(
        override val name: String,
        override val destinations: List<String>,
        var on: Boolean = false
    ) : Module {

        companion object {
            operator fun invoke(row: String): FlipFlop {
                val (name, target) = row.drop(1).split(" -> ")
                val destinations = target.split(", ")
                return FlipFlop(name, destinations)
            }
        }

        override fun process(pulse: Pulse): List<Pulse> =
            if (pulse.value) {
                emptyList()
            } else {
                on = !on
                destinations.map { Pulse(on, name, it) }
            }
    }

    private data class Conjunction(
        override val name: String,
        override val destinations: List<String>,
        var last: MutableMap<String, Boolean> = mutableMapOf()
    ) : Module {

        companion object {
            operator fun invoke(row: String): Conjunction {
                val (name, target) = row.drop(1).split(" -> ")
                val destinations = target.split(", ")
                return Conjunction(name, destinations)
            }
        }

        fun registerInput(name: String) {
            last[name] = false
        }

        override fun process(pulse: Pulse): List<Pulse> {
            last[pulse.source] = pulse.value
            val value = !last.all { it.value }
            return destinations.map { Pulse(value, name, it) }
        }
    }

    private data class Broadcaster(
        override val name: String,
        override val destinations: List<String>
    ) : Module {

        companion object {
            operator fun invoke(row: String): Broadcaster {
                val (name, target) = row.split(" -> ")
                val destinations = target.split(", ")
                return Broadcaster(name, destinations)
            }
        }

        override fun process(pulse: Pulse): List<Pulse> =
            destinations.map { Pulse(pulse.value, name, it) }
    }

}
