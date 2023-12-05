package day04

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val winningNumbers = line.winningNumbers()
            val myNumbers = line.myNumbers()

            winningNumbers.filter { myNumbers.contains(it) }
                .let { it.fold(0) { acc: Int, _: Int -> if (acc == 0) 1 else 2 * acc } }
        }
    }


    fun part2(input: List<String>): Int {
        val occurrences = mutableListOf<Int>()
        input.fold(List(input.size) { 1 }) { acc: List<Int>, line: String ->
            val winningNumbers = line.winningNumbers()
            val myNumbers = line.myNumbers()

            val matchingNumbers = winningNumbers.filter { myNumbers.contains(it) }
            val quantityOfCurrentCard = acc.first()
            val quantitiesOfNextCardsCopies = List(matchingNumbers.size) { quantityOfCurrentCard }

            occurrences.add(quantityOfCurrentCard)

            acc.drop(1).addWith(quantitiesOfNextCardsCopies)
        }

        return occurrences.sum()
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    println("Part one test:")
    part1(testInput).println()
    println("Part one:")
    part1(input).println()
    println("Part two test:")
    part2(testInput).println()
    println("Part two:")
    part2(input).println()
}

private fun List<Int>.addWith(listToAdd: List<Int>): List<Int> {
    val maxLength = maxOf(this.size, listToAdd.size)
    val extendedList1 = this + List(maxLength - this.size) { 0 }
    val extendedList2 = listToAdd + List(maxLength - listToAdd.size) { 0 }

    return extendedList1.zip(extendedList2) { a, b -> a + b }
}

private fun String.winningNumbers() = substringAfter(':').substringBefore('|').trim().split(' ')
    .mapNotNull { if (it.isBlank()) null else it.toInt() }

private fun String.myNumbers() = substringAfter('|').trim().split(' ')
    .mapNotNull { if (it.isBlank()) null else it.toInt() }