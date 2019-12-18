package cn.lannie.kt.blockchain.common.algorithm

import java.lang.RuntimeException
import java.math.BigInteger
import java.util.*
import kotlin.experimental.and

/**
 * Base58 is a way to encode Bitcoin addresses (or arbitrary data) as
 * alphanumeric strings.
 * <p>
 * Note that this is not the same base58 as used by Flickr, which you may find
 * referenced around the Internet.
 * <p>
 * You may want to consider working with {@link VersionedChecksummedBytes}
 * instead, which adds support for testing the prefix and suffix bytes commonly
 * found in addresses.
 * <p>
 * Satoshi explains: why base-58 instead of standard base-64 encoding?
 * <ul>
 * <li>Don't want 0OIl characters that look the same in some fonts and could be
 * used to create visually identical looking account numbers.</li>
 * <li>A string with non-alphanumeric characters is not as easily accepted as an
 * account number.</li>
 * <li>E-mail usually won't line-break if there's no punctuation to break at.</li>
 * <li>Doubleclicking selects the whole number as one word if it's all
 * alphanumeric.</li>
 * </ul>
 * <p>
 * However, note that the encoding/decoding runs in O(n&sup2;) time, so it is
 * not useful for large data.
 * <p>
 * The basic idea of the encoding is to treat the data bytes as a large number
 * represented using base-256 digits, convert the number to be represented using
 * base-58 digits, preserve the exact number of leading zeros (which are
 * otherwise lost during the mathematical operations on the numbers), and
 * finally represent the resulting base-58 digits as alphanumeric ASCII
 * characters.
 */
object Base58AIgorithm {
    val ALPHABET =   "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray()
    private val ENCODED_ZERO = ALPHABET[0]
    private val INDEXES = IntArray(128)

    init {
        Arrays.fill(INDEXES,-1)
        ALPHABET.forEach {
        }
        for (i in 0 .. ALPHABET.size-1){
            INDEXES[ALPHABET[i].toInt()] = i
        }
    }

    /**
     * encodes the given bytes as a base58 string(no checksum is appended)
     */
    fun encode(input:ByteArray):String{
        if(input.size<1){
            return ""
        }
        var zeros = 0

        while (zeros < input.size && input[zeros].toInt() == 0){
            ++zeros
        }
        var tinput = Arrays.copyOf(input,input.size)

        var encoded = CharArray(tinput.size * 2)
        var outputStart = encoded.size
        var inputStart = zeros
        while(inputStart < input.size){
            encoded[--outputStart] = ALPHABET[divmod(input,inputStart,256,58).toInt()]
            if(input[inputStart].toInt() == 0){
                ++inputStart
            }
        }
        while (outputStart < encoded.size && encoded[outputStart] == ENCODED_ZERO){
            ++outputStart
        }
        while (--zeros >= 0){
            encoded[--outputStart] = ENCODED_ZERO
        }
        return String(encoded,outputStart,encoded.size-outputStart)
    }

    /**
     * decodes the given base58 string into the original data bytes,using the
     * checksum in the last 4 bytes of the decoded data to verify that the rest
     * are correct . the checksum is removed from the returned data
     * @param input
     *            the base58-encoded string to decode (which should include the
     *            checksum)
     * @throws AddressFormatException
     *             if the input is not base 58 or the checksum does not
     *             validate.
     *
     *             public static byte[] decodeChecked(String input) throws
     *             AddressFormatException { byte[] decoded = decode(input); if
     *             (decoded.length < 4) throw new
     *             AddressFormatException("Input too short"); byte[] data =
     *             Arrays.copyOfRange(decoded, 0, decoded.length - 4); byte[]
     *             checksum = Arrays.copyOfRange(decoded, decoded.length - 4,
     *             decoded.length); byte[] actualChecksum =
     *             Arrays.copyOfRange(Sha256Hash.hashTwice(data), 0, 4); if
     *             (!Arrays.equals(checksum, actualChecksum)) throw new
     *             AddressFormatException("Checksum does not validate"); return
     *             data; }
     */
    fun decode(input:String):ByteArray{
        if(input.length < 1){
            return ByteArray(0)
        }
        var input58 = ByteArray(input.length)
        for (i in 0..input.length){
            var c = input.toCharArray()[i]
            var digit =  if (c.toInt() < 128) INDEXES[c.toInt()] else -1
            if(digit < 0){
                throw RuntimeException("Illegal character $c at position $i")
            }
            input58[i] = digit.toByte()
        }
        var zeros = 0
        while (zeros < input58.size && input58[zeros].toInt() == 0){
            ++zeros
        }
        var decoded = ByteArray(input.length)
        var outputStart = decoded.size
        var inputStart = zeros
        while(inputStart < input58.size){
            decoded[--outputStart] = divmod(input58,inputStart,58,256)
            if(input58[inputStart].toInt() == 0){
                ++inputStart
            }
        }
        while (outputStart < decoded.size && decoded[outputStart].toInt() == 0){
            ++outputStart
        }
        return Arrays.copyOfRange(decoded,outputStart - zeros,decoded.size)
    }

    fun decodeToBigInteger(input : String):BigInteger{
        return BigInteger(1,decode(input))
    }

    /**
     * divides a number,represented as an array of bytes each containing a single digit in the specified base,
     * by the given divisor.the given number is modified in-place to contain the quotient,and the return value
     * is the remainder
     * @param number
     *            the number to divide
     * @param firstDigit
     *            the index within the array of the first non-zero digit (this
     *            is used for optimization by skipping the leading zeros)
     * @param base
     *            the base in which the number's digits are represented (up to
     *            256)
     * @param divisor
     *            the number to divide by (up to 256)
     * @return the remainder of the division operation
     */
    private fun divmod(number:ByteArray,firstDigit:Int,base:Int,divisor:Int):Byte{
        var remainder = 0
        for (i in firstDigit .. number.size-1){
            var digit = (number[i] and 0xFF.toByte()).toInt()
            var temp = remainder * base + digit
            number[i] = (temp / divisor).toByte()
            remainder = temp % divisor
        }
        return remainder.toByte()
    }
}