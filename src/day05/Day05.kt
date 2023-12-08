package day05

import readText
import runParts

fun main() {

    fun part1(input: String): Long {
        val almanacParts: List<String> = input.getAlmanacPartsSplit()
        val seeds: List<Long> = almanacParts.getSeedsLineSingleNumbers()
        return getTheLowestLocation(almanacParts, seeds)
    }

    fun part2(input: String): Long {
        val almanacParts = input.getAlmanacPartsSplit()
        val seeds: List<LongRange> = almanacParts.getSeedsLineRangeNumbers()
        return getTheLowestLocationUsingRanges(almanacParts, seeds)
    }

    runParts(dayNumber = 5, readFromFileFn = ::readText, partOneFn = ::part1, partTwoFn = ::part2)
}

private fun getTheLowestLocationUsingRanges(almanacParts: List<String>, seeds: List<LongRange>): Long {
    val listOfMaps: List<List<Pair<LongRange, Long>>> = almanacParts.getTheListOfMaps()

    return listOfMaps.fold(seeds) { acc: List<LongRange>, map: List<Pair<LongRange, Long>> ->
        val usedInMerges = mutableListOf<LongRange>()
        acc.flatMap { inputRange ->
            val merges: List<LongRange> = map.mapNotNull { (range, diff) ->
                inputRange.mergeWith(range)?.let {
                    usedInMerges.add(it)
                    LongRange(it.first + diff, it.last + diff)
                }
            }

            if (usedInMerges.isNotEmpty()) merges + inputRange.missingRanges(usedInMerges)
            else listOf(inputRange)
        }
    }.minOf { it.first }
}

fun LongRange.mergeWith(other: LongRange): LongRange? {
    val start = maxOf(this.first, other.first)
    val end = minOf(this.last, other.last)

    return if (start <= end) start..end else null
}

fun LongRange.missingRanges(ranges: List<LongRange>): List<LongRange> {
    val allRanges = mutableListOf<LongRange>()
    allRanges.add(start..<ranges.first().first)

    for (i in 0 until ranges.size - 1) {
        val endOfCurrent = ranges[i].last
        val startOfNext = ranges[i + 1].first
        if (endOfCurrent < startOfNext - 1) {
            allRanges.add((endOfCurrent + 1)..<startOfNext)
        }
    }

    allRanges.add((ranges.last().last + 1)..endInclusive)

    return allRanges.filter { it.first in this && it.last in this }
}

private fun getTheLowestLocation(almanacParts: List<String>, seeds: List<Long>): Long {
    val listOfMaps: List<List<Pair<LongRange, Long>>> = almanacParts.getTheListOfMaps()

    return listOfMaps
        .fold(seeds) { acc: List<Long>, map: List<Pair<LongRange, Long>> ->
            acc.map { input -> input + (map.firstOrNull { input in it.first }?.second ?: 0) }
        }
        .min()
}

private fun List<String>.getTheListOfMaps(): List<List<Pair<LongRange, Long>>> =
    removeSeedsLine()
        .map { map ->
            map.splitToLines()
                .map { mapLine ->
                    val (destination, source, range) = mapLine.split(" ").map { s -> s.toLong() }
                    LongRange(source, source + range - 1) to destination - source
                }
        }

private fun String.getAlmanacPartsSplit() = split("\n\n")
private fun List<String>.getSeedsLineSingleNumbers(): List<Long> =
    first().substringAfter(":").trim().split(" ").map { it.toLong() }

private fun List<String>.getSeedsLineRangeNumbers(): List<LongRange> =
    getSeedsLineSingleNumbers().windowed(2, 2)
        .map { LongRange(it.first(), it.first() + it.last() - 1) }

private fun String.splitToLines() = split("\n").drop(1)
private fun List<String>.removeSeedsLine() = drop(1)

