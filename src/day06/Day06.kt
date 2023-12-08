package day06

import readInput
import runParts

fun main() {

    fun part1(input: List<String>): Int {
        val raceDurations: List<Int> = input.raceDurations()
        val raceRecords: List<Int> = input.raceRecords()

        return raceDurations.zip(raceRecords) { raceDuration, raceRecord ->
            findAllPossibleWaysToWin(raceDuration, raceRecord).count()
        }
            .fold(1) { acc: Int, i: Int -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val raceDuration = input.raceDurations().fold("") { acc: String, i: Int -> "$acc$i" }.toLong()
        val raceRecord = input.raceRecords().fold("") { acc: String, i: Int -> "$acc$i" }.toLong()

        return countAllPossibleWaysToWin(raceDuration, raceRecord)
    }

    runParts(dayNumber = 6, readFromFileFn = ::readInput, partOneFn = ::part1, partTwoFn = ::part2)
}

fun List<String>.raceDurations(): List<Int> = first().substringAfter(':').trim().split(' ')
    .filter { it.isNotBlank() }.map { it.toInt() }

fun List<String>.raceRecords(): List<Int> = last().substringAfter(':').trim().split(' ')
    .filter { it.isNotBlank() }.map { it.toInt() }

private fun countAllPossibleWaysToWin(inTime: Long, withDistanceRecord: Long): Int = sequence {
    for (hold in 0..inTime) {
        val remainingTime = inTime - hold
        val result = hold * remainingTime
        if (result > withDistanceRecord) yield(result)
    }
}.count()

private fun findAllPossibleWaysToWin(inTime: Int, withDistanceRecord: Int): List<Int> =
    List(inTime + 1) { hold ->
        val remainingTime = inTime - hold
        hold * remainingTime
    }.filter { it > withDistanceRecord }
