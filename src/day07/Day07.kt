package day07

import day07.HandWithBid.HandType.*
import readInput
import runParts
import kotlin.math.pow

fun main() {

    fun processAndOrderHandsWithBids(input: List<String>): List<HandWithBid> =
        input.map {
            val (hand, bid) = it.split(" ")
            HandWithBid.from(hand, bid)
        }

    fun part1(input: List<String>): Int {
        return processAndOrderHandsWithBids(input)
            .sortedBy { it.rank(part = Part.ONE) }
            .countBids()
    }

    fun part2(input: List<String>): Int {
        return processAndOrderHandsWithBids(input)
            .map { it.applyJokers() }
            .sortedBy { it.rank(part = Part.TWO) }
            .countBids()
    }

    runParts(dayNumber = 7, readFromFileFn = ::readInput, partOneFn = ::part1, partTwoFn = ::part2)
}

private fun List<HandWithBid>.countBids() = mapIndexed { index, handWithBid ->
    handWithBid.bid * (index + 1)
}.sum()

data class HandWithBid(
    val type: HandType,
    val content: String,
    val bid: Int,
) {
    companion object {
        fun from(hand: String, bid: String): HandWithBid =
            HandWithBid(type = HandType.from(hand), content = hand, bid.toInt())
    }

    enum class HandType(val rank: Int) {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIRS(3),
        ONE_PAIR(2),
        HIGH_CARD(1);

        companion object {
            fun from(hand: String): HandType {
                val occurrences: Map<Char, Int> = hand.groupingBy { it }.eachCount()

                return when {
                    occurrences.any { it.value == 5 } -> FIVE_OF_A_KIND
                    occurrences.any { it.value == 4 } -> FOUR_OF_A_KIND
                    occurrences.any { it.value == 3 } && occurrences.any { it.value == 2 } -> FULL_HOUSE
                    occurrences.any { it.value == 3 } -> THREE_OF_A_KIND
                    occurrences.filter { it.value == 2 }.size == 2 -> TWO_PAIRS
                    occurrences.any { it.value == 2 } -> ONE_PAIR
                    else -> HIGH_CARD

                }
            }

        }
    }

    fun rank(part: Part): Double =
        type.rank * 15.0.pow(5) + content.foldRightIndexed(0.0) { index: Int, c: Char, acc: Double ->
            (15.0.pow(4 - index) * getCardPoints(c, part)) + acc
        }

    fun applyJokers(): HandWithBid {
        val numberOfJokers = content.count { it == 'J' }
        val newType = if (numberOfJokers > 0) when (type) {
            FIVE_OF_A_KIND -> FIVE_OF_A_KIND
            FOUR_OF_A_KIND -> FIVE_OF_A_KIND
            FULL_HOUSE -> FIVE_OF_A_KIND
            THREE_OF_A_KIND -> FOUR_OF_A_KIND
            TWO_PAIRS -> if (numberOfJokers == 2) FOUR_OF_A_KIND else FULL_HOUSE
            ONE_PAIR -> THREE_OF_A_KIND
            HIGH_CARD -> ONE_PAIR
        } else type
        return copy(type = newType)
    }
}


private fun getCardPoints(c: Char, part: Part): Double =
    when (part) {
        Part.ONE -> cardPointsPartOne[c]!!
        Part.TWO -> cardPointsPartTwo[c]!!
    }.toDouble()

enum class Part {
    ONE, TWO;
}

val cardPointsPartOne: Map<Char, Int> = mapOf(
    'A' to 14,
    'K' to 13,
    'Q' to 12,
    'J' to 11,
    'T' to 10,
    '9' to 9,
    '8' to 8,
    '7' to 7,
    '6' to 6,
    '5' to 5,
    '4' to 4,
    '3' to 3,
    '2' to 2,
)

val cardPointsPartTwo: Map<Char, Int> = mapOf(
    'A' to 13,
    'K' to 12,
    'Q' to 11,
    'T' to 10,
    '9' to 9,
    '8' to 8,
    '7' to 7,
    '6' to 6,
    '5' to 5,
    '4' to 4,
    '3' to 3,
    '2' to 2,
    'J' to 1,
)
