package me.luchs.aoc2023

data class DayTwenty(val input: String) : Day<Long> {

    override fun partOne(): Long {

        val modules = parseInput()
        modules.registerConjunctionSources()

        var countLow = 0L
        var countHigh = 0L

        repeat(1000) {
            val queue = ArrayDeque<Pulse>()
            queue.add(Button().pulse())

            while (queue.isNotEmpty()) {
                val pulse = queue.removeFirst()
                println(pulse)

                if (pulse.value) {
                    countHigh++
                } else {
                    countLow++
                }

                val results = modules
                    .filter { pulse.destination == it.name }
                    .flatMap { it.process(pulse) }

                queue.addAll(results)
            }
        }

        return countHigh * countLow
    }

    override fun partTwo(): Long {
        TODO("Not yet implemented")
    }

    fun parseInput(): List<Module> = input.lines()
        .map {
            when {
                it.startsWith("broadcaster") -> Broadcaster(it)
                it.startsWith("%") -> FlipFlop(it)
                it.startsWith("&") -> Conjunction(it)
                else -> throw IllegalStateException("Can not process $it")
            }
        }

    private fun List<Module>.registerConjunctionSources() {
        val conjunctions = this.filterIsInstance<Conjunction>()
        conjunctions.forEach { conjunction ->
            this.filter { conjunction.name in it.destinations }
                .map { it.name }
                .forEach { conjunction.registerInput(it) }
        }
    }


    data class Pulse(
        val value: Boolean = true,
        val source: String,
        val destination: String
    ) {
        override fun toString(): String {
            val pulse = if (value) "high" else "low"
            return "$source -$pulse-> $destination"
        }
    }

    sealed interface Module {
        val name: String
        val destinations: List<String>
        fun process(pulse: Pulse): List<Pulse>
    }

    data class FlipFlop(
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

    data class Conjunction(
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

    data class Broadcaster(
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

    class Button {
        fun pulse(): Pulse = Pulse(value = false, source = "button", destination = "broadcaster")
    }

}
