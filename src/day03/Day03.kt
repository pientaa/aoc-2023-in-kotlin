package day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.mapIndexed { rowIdx, line ->
            val numbersInLine: List<Pair<Int, IntRange>> = line.findNumbers()

            numbersInLine
                .filter { (_, range) ->
                    val previousLine = input.getOrNull(rowIdx - 1)
                    val nextLine = input.getOrNull(rowIdx + 1)
                    val lines = listOfNotNull(previousLine, line, nextLine)

                    lines.any { comparedLine ->
                        val rangeStart = if (range.first == 0) 0 else range.first - 1
                        val rangeEnd = if (range.last == comparedLine.length - 1) range.last else range.last + 1

                        comparedLine.substring(rangeStart, rangeEnd + 1).containsSymbol()
                    }
                }
                .map { it.first }
        }
            .also { println("Found numbers: ${it.flatten()}") }
            .sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        val rowLength = input.first().length

        val numbers: List<List<Pair<Int, IntRange>>> = input.mapIndexed { rowIdx, line ->
            val numbersInLine: List<Pair<Int, IntRange>> = line.findNumbers()

            numbersInLine
                .filter { (_, range) ->
                    val previousLine = input.getOrNull(rowIdx - 1)
                    val nextLine = input.getOrNull(rowIdx + 1)
                    val lines = listOfNotNull(previousLine, line, nextLine)

                    lines.any { comparedLine ->
                        val rangeStart = if (range.first == 0) 0 else range.first - 1
                        val rangeEnd = if (range.last == comparedLine.length - 1) range.last else range.last + 1
                        comparedLine.substring(rangeStart, rangeEnd + 1).containsGearSymbol()
                    }
                }
                .map { (number, range) ->
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

        gearsLocations.mapNotNull { (row, col) ->
            val previousLineNumber = numbers.getOrNull(row - 1)?.filter { (number, range) -> range.contains(col) }
            val currentLineNumber = numbers[row].filter { (number, range) -> range.contains(col) }
            val nextLineNumber = numbers.getOrNull(row + 1)?.filter { (number, range) -> range.contains(col) }

            val gears = listOfNotNull(previousLineNumber, currentLineNumber, nextLineNumber).flatten()

            if (gears.size == 2)
                gears.first().first.toLong() * gears.last().first.toLong()
            else null
        }
            .sum()
            .also { println("SUM: $it") }


        return input.size
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    part2(input).println()
}

private fun String.findGearsLocations(): List<Int> {
    val regex = GEAR_REGEX_PATTERN.toRegex()
    return regex.findAll(this).map { it.range.first }.toList()
}

private fun String.findNumbers(): List<Pair<Int, IntRange>> {
    val regex = NUMBER_REGEX_PATTERN.toRegex()
    return regex.findAll(this).map { it.value.toInt() to it.range }.toList()
}

private fun String.containsSymbol(): Boolean = NOT_NUMBER_NOR_DOT_REGEX_PATTERN.toRegex().containsMatchIn(this)
private fun String.containsGearSymbol(): Boolean = GEAR_REGEX_PATTERN.toRegex().containsMatchIn(this)

private const val NUMBER_REGEX_PATTERN = "(\\d+)"
private const val NOT_NUMBER_NOR_DOT_REGEX_PATTERN = "([^0-9\\.])"
private const val GEAR_REGEX_PATTERN = "(\\*)"
