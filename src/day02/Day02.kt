package day02

import readInput
import runParts
import splitAndTrim

fun main() {
    fun part1(input: List<String>): Int {
        return input.processGame()
            .filter { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
            .sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return input.processGame()
            .sumOf { it.red * it.green * it.blue }
    }

    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    runParts(input, testInput, ::part1, ::part2)
}

fun List<String>.processGame() = map { line ->
    val gameId = line.getGameId()
    val gameSummary = GameSummary(gameId)

    line.getGame()
        .splitAndTrim(';')
        .map { round ->
            round.splitAndTrim(',')
                .map { pair ->
                    val (value, color) = pair.split(' ')
                    gameSummary.updateColor(color, value.toInt())
                }
        }
    gameSummary
}


private fun String.getGameId(): Int = substringBefore(':').substringAfter(' ').toInt()
private fun String.getGame(): String = substringAfter(':').trim()

data class GameSummary(
    val id: Int,
    var red: Int = 0,
    var green: Int = 0,
    var blue: Int = 0,
) {
    fun updateColor(color: String, value: Int) {
        when (color) {
            "red" -> if (value > red) red = value
            "green" -> if (value > green) green = value
            "blue" -> if (value > blue) blue = value
        }
    }
}