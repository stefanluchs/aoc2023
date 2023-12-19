package me.luchs.aoc2023.shared

fun CharSequence.splitWithDelimiter(vararg delimiters: String): List<String> {
    return splitWithDelimiterToSequence(*delimiters).toList()
}

fun CharSequence.splitWithDelimiterToSequence(vararg delimiters: String): Sequence<String> {
    return sequence {
        var i = 0
        val current = StringBuilder()
        outer@ while (i < length) {
            for (d in delimiters) {
                if (startsWith(d, i)) {
                    yield(current.toString())
                    yield(d)
                    i += d.length
                    current.clear()
                    continue@outer
                }
            }
            current.append(this@splitWithDelimiterToSequence[i++])
        }
        yield(current.toString())
    }
}
