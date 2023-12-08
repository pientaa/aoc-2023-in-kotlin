package day03

import readInput
import runParts

fun main() {
    fun part1(input: List<String>): Int {
        return input.findNumbersInEachLine(NOT_NUMBER_NOR_DOT_REGEX_PATTERN.toRegex())
            .map { line -> line.map { it.first } }
            .also { println("Found numbers: ${it.flatten()}") }
            .sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        val rowLength = input.first().length

        val numbers: List<List<Pair<Int, IntRange>>> = input.findNumbersInEachLine(GEAR_REGEX_PATTERN.toRegex())
            .map { line ->
                line.map { (number, range) ->
                    val extendedRange = IntRange(
                        start = if (range.first == 0) 0 else range.first - 1,
                        endInclusive = if (range.last == rowLength - 1) range.last else range.last + 1
                    )
                    number to extendedRange
                }
            }

        val gearsLocations: List<Pair<Int, Int>> = input.flatMapIndexed { rowIdx, line ->
            line.findGearsLocations().map { columnIdx ->
                Pair(rowIdx, columnIdx)
            }
        }

        return gearsLocations.mapNotNull { (row, col) ->
            val previousLineNumber = numbers.getOrNull(row - 1)?.filter { (number, range) -> range.contains(col) }
            val currentLineNumber = numbers[row].filter { (number, range) -> range.contains(col) }
            val nextLineNumber = numbers.getOrNull(row + 1)?.filter { (number, range) -> range.contains(col) }

            val gears = listOfNotNull(previousLineNumber, currentLineNumber, nextLineNumber).flatten()

            if (gears.size == 2) gears.first().first * gears.last().first
            else null
        }
            .sum()
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    runParts(input, testInput, ::part1, ::part2)
}

/**
 * @return [List] of "row"s; where "row" is a list of pairs [Int] to [IntRange]
 */
fun List<String>.findNumbersInEachLine(regex: Regex): List<List<Pair<Int, IntRange>>> {
    return mapIndexed { rowIdx, line ->
        val numbersInLine: List<Pair<Int, IntRange>> = line.findNumbersInEachLine()

        numbersInLine
            .filter { (_, range) ->
                val previousLine = getOrNull(rowIdx - 1)
                val nextLine = getOrNull(rowIdx + 1)
                val lines = listOfNotNull(previousLine, line, nextLine)

                lines.any { comparedLine ->
                    val rangeStart = if (range.first == 0) 0 else range.first - 1
                    val rangeEnd = if (range.last == comparedLine.length - 1) range.last else range.last + 1
                    comparedLine.substring(rangeStart, rangeEnd + 1).contains(regex)
                }
            }
    }
}

private fun String.findGearsLocations(): List<Int> {
    val regex = GEAR_REGEX_PATTERN.toRegex()
    return regex.findAll(this).map { it.range.first }.toList()
}

private fun String.findNumbersInEachLine(): List<Pair<Int, IntRange>> {
    val regex = NUMBER_REGEX_PATTERN.toRegex()
    return regex.findAll(this).map { it.value.toInt() to it.range }.toList()
}

private const val NUMBER_REGEX_PATTERN = "(\\d+)"
private const val NOT_NUMBER_NOR_DOT_REGEX_PATTERN = "([^0-9\\.])"
private const val GEAR_REGEX_PATTERN = "(\\*)"
