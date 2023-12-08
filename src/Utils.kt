import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> =
    Path("src/${name.take(5).lowercase(Locale.getDefault())}/$name.txt").readLines()

/**
 * Reads Text from the given input txt file.
 */
fun readText(name: String): String =
    Path("src/${name.take(5).lowercase(Locale.getDefault())}/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


/**
 * Splits the string by [delimiter] and trims each resulting substring.
 *
 * @param delimiter Character to split the string.
 * @return List of trimmed substrings.
 */
fun String.splitAndTrim(delimiter: Char) = split(delimiter).map { it.trim() }


fun <T, R> runParts(input: T, testInput: T, partOne: (T) -> R, partTwo: (T) -> R) {
    println("Part one test:")
    println(partOne(testInput))
    println("Part one:")
    println(partOne(input))
    println("Part two test:")
    println(partTwo(testInput))
    println("Part two:")
    println(partTwo(input))
}

fun <T, R> runParts(dayNumber: Int, readFromFileFn: (String) -> T, partOneFn: (T) -> R, partTwoFn: (T) -> R) {
    val number = if (dayNumber < 10) "0$dayNumber" else dayNumber
    val input = readFromFileFn("Day$number")
    val testInput = readFromFileFn("Day${number}_test")

    println("Part one test:")
    println(partOneFn(testInput))
    println("Part one:")
    println(partOneFn(input))
    println("Part two test:")
    println(partTwoFn(testInput))
    println("Part two:")
    println(partTwoFn(input))
}