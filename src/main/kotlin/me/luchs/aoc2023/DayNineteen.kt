package me.luchs.aoc2023

import me.luchs.aoc2023.shared.splitWithDelimiter
import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

data class DayNineteen(val input: String) : Day<BigInteger> {

    override fun partOne(): BigInteger {
        val (workflows, parts) = parseInput(input)

        val results = parts.associateWith { part ->
            var target = "in"
            var workflow = workflows[target]
            while (workflow != null) {
                target = workflow.rules.firstNotNullOf { it.evaluate(part) }
                workflow = workflows[target]
            }
            target
        }

        return results.entries
            .filter { it.value == "A" }
            .map { it.key }
            .sumOf { part -> part.values.map { it.value }.sumOf { it }.toBigInteger() }
    }

    override fun partTwo(): BigInteger {
        /**val (workflows, parts) = parseInput(input)

        val criteria = mutableMapOf<String, Map<String, List<Rule>>>()
        val targets = ArrayDeque(listOf("A"))

        while (targets.isNotEmpty()) {
        val target = targets.removeFirst()

        if (criteria[target] != null) {
        continue
        }

        val rules = rulesToTarget(workflows, target)
        rules.second.map { it.key }.filterNot { it in targets }.forEach { targets.add(it) }
        criteria[target] = rules.second
        }

        val source = "in"
        var paths = criteria["A"]!!.map { listOf(it.key, "A") }
        while (paths.any { it.first() != source }) {
        paths = paths.map { criteria[it.first()]!!.map { it.key } + it }
        }
        paths = paths.distinct()

        val pathRules: List<List<PartRule>> = paths.map { path ->
        path.zipWithNext().flatMap { step ->
        val workflowRules = workflows[step.first]!!.rules
        val rule = workflowRules.first { it.target() == step.second }
        if (rule.isFallback()) {
        workflowRules.filterNot { it.isFallback() }.map { it.invert() as PartRule }
        } else {
        listOf(rule as PartRule)
        }
        }
        }


        val ranges = pathRules.map {
        var ranges = mapOf(
        "x" to 1..4000,
        "m" to 1..4000,
        "a" to 1..4000,
        "s" to 1..4000
        )
        it.forEach {
        ranges = it.restrict(ranges)
        }
        ranges
        }

        val possibilities = ranges.map {
        it.values
        .map { it.count() }
        .map { it.toBigInteger() }
        .reduce { acc, i -> acc * i }
        }
         **/
        return 0.toBigInteger()// possibilities.sumOf { it }
    }

    private fun rulesToTarget(workflows: Map<String, Workflow>, target: String) =
        target to workflows.entries.associate {
            val targetRules = it.value.rules.filter { it.target == target }
            it.key to targetRules
        }.filter { it.value.isNotEmpty() }

    private fun parseInput(input: String): Pair<Map<String, Workflow>, List<Part>> {
        val (workflowInput, partsInput) = input.split("\n\n")
        val workflows = workflowInput.lines().map { Workflow(it) }.associateBy { it.name }
        val parts = partsInput.lines().map { Part(it) }
        return workflows to parts
    }

    data class Part(val values: Map<String, Int>) {

        // direct map like access to values
        operator fun get(name: String): Int = values[name] ?: error("Invalid name: $name")

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

    data class Rule(
        val input: String,
        val target: String,
        val condition: Condition?
    ) {

        companion object {
            operator fun invoke(input: String): Rule {
                if (!input.contains(':')) {
                    return Rule(input = input, target = input, condition = null)
                }

                val (operation, target) = input.split(':')
                val (conditional, operator, conditionValue) = operation.splitWithDelimiter("<", ">")
                val condition = Condition(
                    conditional = conditional,
                    operator = operator,
                    conditionValue = conditionValue.toInt()
                )
                return Rule(input, target, condition)
            }


        }

        fun evaluate(part: Part): String? {
            val condition = this.condition
            return if (condition != null && condition.evaluate(part)) {
                target
            } else if (condition == null) {
                target
            } else {
                null
            }
        }

        fun evaluate(part: PartRange): Pair<Pair<String, PartRange>, PartRange> {
            val condition = this.condition
            return if (condition != null) {
                val (targetRange, remainingRange) = condition.evaluate(part)
                (target to targetRange) to remainingRange
            } else {
                (target to part) to part
            }
        }

    }


    data class Workflow(val name: String, val rules: List<Rule>) {
        companion object {
            operator fun invoke(row: String): Workflow {
                val (name, p2) = row.split('{')
                val rules = p2.dropLast(1).split(',').map { Rule(it) }
                return Workflow(name, rules)
            }
        }


    }

    data class PartRange(val ranges: Map<String, IntRange>) {
        operator fun get(name: String): IntRange = ranges[name] ?: error("Invalid name: $name")

        /**
         * Total size of this part range
         */
        /** val totalSize: Long
        get() {
        return ranges.values.productOf { range ->
        max(0, range.last - range.first + 1).toLong()
        }
        }**/
    }


    data class Condition(
        val conditional: String,
        val operator: String,
        val conditionValue: Int
    ) {
        fun evaluate(part: Part): Boolean {
            val value = part[conditional]
            return when (operator) {
                "<" -> value < conditionValue
                ">" -> value > conditionValue
                else -> error("Invalid operator: $operator")
            }
        }

        /**
         * Returns the matching range and remaining range after applying the condition.
         * The remaining range may be applied to subsequent rules in a workflow.
         */
        fun evaluate(partRange: PartRange): Pair<PartRange, PartRange> {
            val range = partRange[conditional]
            val (matchingRange, remainingRange) = when (operator) {
                "<" ->
                    range.start..min(conditionValue - 1, range.endInclusive) to
                            max(range.start, conditionValue)..range.endInclusive

                ">" -> max(conditionValue + 1, range.start)..range.endInclusive to
                        range.start..min(range.endInclusive, conditionValue)

                else -> error("Invalid operator: $operator")
            }
            return PartRange(partRange.ranges + (conditional to matchingRange)) to
                    PartRange(partRange.ranges + (conditional to remainingRange))
        }
    }

}
