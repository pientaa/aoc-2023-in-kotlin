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
