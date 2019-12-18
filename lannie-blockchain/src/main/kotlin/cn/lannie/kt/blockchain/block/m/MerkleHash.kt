package cn.lannie.kt.blockchain.block.m

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MerkleHash {

    /**
     * Hash value as byte array.
     */
    /**
     * Get the byte value of a MerkleHash.
     *
     * @return an array of bytes
     */
    var value: ByteArray? = null
        private set

    /**
     * Compare the MerkleHash with a given byte array.
     *
     * @param hash as byte array
     * @return boolean
     */
    fun equals(hash: ByteArray): Boolean {
        return Arrays.equals(this.value, hash)
    }

    /**
     * Compare the MerkleHash with a given MerkleHash.
     *
     * @param hash as MerkleHash
     * @return boolean
     */
    fun equals(hash: MerkleHash?): Boolean {
        var result = false
        if (hash != null) {
            result = Arrays.equals(this.value, hash.value)
        }
        return result
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(value)
    }

    /**
     * Encode in Base64 the MerkleHash.
     *
     * @return the string encoding of MerkleHash.
     */
    override fun toString(): String {
        return Base64.getEncoder().encodeToString(this.value)
    }

    /**
     * Compute SHA256 hash of a byte array.
     *
     * @param buffer of bytes
     */
    private fun computeHash(buffer: ByteArray) {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            this.value = digest.digest(buffer)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }

    companion object {

        /**
         * Create a MerkleHash from an array of bytes.
         *
         * @param buffer of bytes
         * @return a MerkleHash
         */
        fun create(buffer: ByteArray): MerkleHash {
            val hash = MerkleHash()
            hash.computeHash(buffer)
            return hash
        }

        /**
         * Create a MerkleHash from a string. The string needs
         * first to be transformed in a UTF8 sequence of bytes.
         * Used for leaf hashes.
         *
         * @param buffer string
         * @return a MerkleHash
         */
        fun create(buffer: String): MerkleHash {
            return create(buffer.toByteArray(StandardCharsets.UTF_8))
        }

        /**
         * Create a MerkleHash from two MerkleHashes by concatenation
         * of the byte arrays. Used for internal nodes.
         *
         * @param left  subtree hash
         * @param right subtree hash
         * @return a MerkleHash
         */
        fun create(left: MerkleHash, right: MerkleHash): MerkleHash {
            return create(concatenate(left.value!!, right.value!!))
        }

        /**
         * Concatenate two array of bytes.
         *
         * @param a is the first array
         * @param b is the second array
         * @return a byte array
         */
        fun concatenate(a: ByteArray, b: ByteArray): ByteArray {
            val c = ByteArray(a.size + b.size)
            System.arraycopy(a, 0, c, 0, a.size)
            System.arraycopy(b, 0, c, a.size, b.size)
            return c
        }
    }
}
