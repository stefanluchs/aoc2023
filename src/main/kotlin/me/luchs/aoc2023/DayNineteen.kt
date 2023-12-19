package me.luchs.aoc2023

import me.luchs.aoc2023.shared.splitWithDelimiter
import kotlin.math.max
import kotlin.math.min

data class DayNineteen(val input: String) : Day<Long> {

    override fun partOne(): Long {
        val (workflows, parts) = parseInput(input)
        return parts
            .associateWith { part ->
                var target = "in"
                var workflow = workflows[target]
                // go through workflow until target is reached
                while (workflow != null) {
                    target = workflow.evaluatePart(part)
                    workflow = workflows[target]
                }
                target
            }
            .entries
            .filter { it.value == "A" }
            .map { it.key }
            .sumOf { part -> part.values.map { it.value }.sumOf { it.toLong() } }
    }

    override fun partTwo(): Long {
        val (workflows, _) = parseInput(input)

        val queue = ArrayDeque<Pair<String, Ranges>>()

        // start at 'in' with max range (1..4000) for each category
        queue += "in" to Ranges(
            mapOf(
                "x" to 1..4000,
                "a" to 1..4000,
                "m" to 1..4000,
                "s" to 1..4000
            )
        )

        var result = 0L

        while (queue.isNotEmpty()) {
            val (name, range) = queue.removeFirst()
            val workflow = workflows[name]!!
            val outcomes = workflow.evaluateRanges(range)
            for ((outcomeName, outcomeRange) in outcomes) {

                // bad end -> ignore values
                if (outcomeName == "R") continue

                // finish here and add number of possibilities to result
                if (outcomeName == "A") {
                    result += outcomeRange.numberOfPossibilities
                    continue
                }

                // target not reached -> add to queue
                queue += outcomeName to outcomeRange
            }
        }

        return result
    }

    private fun parseInput(input: String): Pair<Map<String, Workflow>, List<Part>> {
        val (workflowInput, partsInput) = input.split("\n\n")
        val workflows = workflowInput.lines().map { Workflow(it) }.associateBy { it.name }
        val parts = partsInput.lines().map { Part(it) }
        return workflows to parts
    }

    data class Part(val values: Map<String, Int>) {

        // direct map like access to values
        operator fun get(name: String): Int = values[name]!!

        companion object {
            operator fun invoke(row: String): Part =
                Part(row.drop(1).dropLast(1)
                    .split(',')
                    .associate {
                        val (key, value) = it.split('=')
                        key to value.toInt()
                    })
        }
    }

    data class Ranges(val ranges: Map<String, IntRange>) {

        // map like direct access to ranges
        operator fun get(name: String): IntRange = ranges[name]!!

        val numberOfPossibilities: Long
            get() {
                return ranges.values
                    .map { range -> max(0, range.last - range.first + 1).toLong() }
                    .reduce { acc, l -> acc * l }
            }
    }


    /**
     * A workflow consists of a set of rules and a unique name.
     * Each rule applies to one category.
     */
    data class Workflow(val name: String, val rules: List<Rule>) {
        companion object {
            operator fun invoke(row: String): Workflow {
                val (name, p2) = row.split('{')
                val rules = p2.dropLast(1).split(',').map { Rule(it) }
                return Workflow(name, rules)
            }
        }

        /**
         * @return the target name for the Part when places into the Workflow
         */
        fun evaluatePart(part: Part): String = rules.firstNotNullOf { it.evaluatePart(part) }

        /**
         * @return list of possible targets with the respective ranges
         */
        fun evaluateRanges(ranges: Ranges): List<Pair<String, Ranges>> {
            val result = mutableListOf<Pair<String, Ranges>>()
            var range = ranges
            for (rule in rules) {
                val (targetToMatchingRange, inverseRange) = rule.evaluateRanges(range)
                result += targetToMatchingRange
                range = inverseRange // use inverse range here for other rules
            }
            return result
        }
    }

    /**
     * A rule consisting of a optional condition and the target property
     */
    data class Rule(val target: String, val condition: Condition?) {

        companion object {
            operator fun invoke(input: String): Rule {
                if (!input.contains(':')) {
                    // fallback rule without condition
                    return Rule(target = input, condition = null)
                }

                val (operation, target) = input.split(':')
                val (category, operator, value) = operation.splitWithDelimiter("<", ">")
                val condition = Condition(category, operator, value.toInt())
                return Rule(target, condition)
            }
        }

        /**
         * @return target name if the rule is satisfied, null otherwise
         */
        fun evaluatePart(part: Part): String? {
            if (condition == null) {
                // fallback rule is always true
                return target
            }
            return if (condition.evaluatePart(part)) target else null
        }

        /**
         * Evaluate rule for part ranges
         * @return Pair:
         * First is target and the range
         * which is required to reach the target as the pair target name to range.
         * Second is the inverse range which remains when the rule is not satisfied.
         */
        fun evaluateRanges(ranges: Ranges): Pair<Pair<String, Ranges>, Ranges> {
            if (condition == null) {
                // fallback rule does not apply any restriction to ranges
                return (target to ranges) to ranges
            }

            val (targetRange, remainingRange) = condition.evaluateRanges(ranges)
            return (target to targetRange) to remainingRange
        }

    }

    /**
     * Condition for a 'category' in the form of 'value' 'operator' 'conditionValue', e.g. value < 45
     */
    data class Condition(val category: String, val operator: String, val conditionValue: Int) {

        /**
         * @return true if the condition is satisfied
         */
        fun evaluatePart(part: Part): Boolean {
            val value = part[category]
            return when (operator) {
                "<" -> value < conditionValue
                ">" -> value > conditionValue
                else -> error("could not evaluate: $operator")
            }
        }

        /**
         * Restrict ranges for the part based on the condition
         *
         * @return pair of ranges:
         * first is the matching range (condition is satisfied),
         * second ist the inverse range (condition is not satisfied)
         */
        fun evaluateRanges(ranges: Ranges): Pair<Ranges, Ranges> {
            val range = ranges[category]

            val (matchingRange, inverseRange) = when (operator) {
                "<" -> range.first..min(conditionValue - 1, range.last) to
                        max(range.first, conditionValue)..range.last

                ">" -> max(conditionValue + 1, range.first)..range.last to
                        range.first..min(range.last, conditionValue)

                else -> error("could not evaluate: $operator")
            }

            val matchingRanges = Ranges(ranges.ranges + (category to matchingRange))
            val inverseRanges = Ranges(ranges.ranges + (category to inverseRange))

            return matchingRanges to inverseRanges
        }
    }
}
