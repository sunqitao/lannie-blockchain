package cn.lannie.kt.blockchain.block.m

import java.security.InvalidParameterException
import java.util.*


class MerkleNode {
    var hash: MerkleHash? = null
        private set
    private var leftNode: MerkleNode? = null
    private var rightNode: MerkleNode? = null
    var parent: MerkleNode? = null
        private set

    val isLeaf: Boolean
        get() = this.leftNode == null && this.rightNode == null


    constructor() {}

    constructor(hash: MerkleHash) {
        this.hash = hash
    }

    constructor(left: MerkleNode, right: MerkleNode) {
        this.leftNode = left
        this.rightNode = right
        this.leftNode!!.parent = this
        if (this.rightNode != null) this.rightNode!!.parent = this

        this.computeHash()
    }

    override fun toString(): String {
        return hash!!.toString()
    }

    fun setLeftNode(node: MerkleNode) {
        if (node.hash == null) {
            throw InvalidParameterException("Node hash must be initialized!")
        }

        this.leftNode = node
        this.leftNode!!.parent = this

        this.computeHash()
    }

    fun setRightNode(node: MerkleNode) {
        if (node.hash == null) {
            throw InvalidParameterException("Node hash must be initialized!")
        }

        this.rightNode = node
        this.rightNode!!.parent = this

        if (this.leftNode != null) {
            this.computeHash()
        }
    }

    fun canVerifyHash(): Boolean {
        return this.leftNode != null && this.rightNode != null || this.leftNode != null
    }

    fun verifyHash(): Boolean {
        if (this.leftNode == null && this.rightNode == null) return true
        if (this.rightNode == null) return hash!!.equals(leftNode!!.hash)

        if (this.leftNode == null) {
            throw InvalidParameterException("Left branch must be a node if right branch is a node!")
        }

        val leftRightHash = MerkleHash.create(this.leftNode!!.hash!!, this.rightNode!!.hash!!)
        return hash!!.equals(leftRightHash)
    }

    fun equals(other: MerkleNode): Boolean {
        return this.hash!!.equals(other.hash)
    }

    fun getLeftNode(): MerkleNode? {
        return leftNode
    }

    fun getRightNode(): MerkleNode? {
        return rightNode
    }

    fun computeHash() {
        if (this.rightNode == null) {
            this.hash = this.leftNode!!.hash
        } else {
            this.hash = MerkleHash.create(MerkleHash.concatenate(
                    this.leftNode!!.hash!!.value!!, this.rightNode!!.hash!!.value!!))
        }

        if (this.parent != null) {
            this.parent!!.computeHash()
        }
    }

    override fun hashCode(): Int {

        return Objects.hash(hash, leftNode, rightNode, parent)
    }
}
