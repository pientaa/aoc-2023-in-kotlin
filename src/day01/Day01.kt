fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }

            "$firstDigit$lastDigit".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { line: String -> line.replaceWordsWithNumbers() }
            .map { line: String -> line.replaceWordsWithNumbers() }
            .let { part1(it) }
    }

    val testInput = readInput("Day01_test")
    val input = readInput("Day01")

    part2(input).println()
}

fun String.replaceWordsWithNumbers(): String {
    val pattern = REGEXP_PATTERN.toRegex()
    return pattern.replace(this) { matchResult ->
        matchResult.value.first() + numberMapping[matchResult.value].toString() + matchResult.value.last()
    }
}

const val REGEXP_PATTERN = "(one|two|three|four|five|six|seven|eight|nine)"
val numberMapping = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)
