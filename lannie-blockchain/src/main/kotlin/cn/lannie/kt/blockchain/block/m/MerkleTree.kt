package cn.lannie.kt.blockchain.block.m

import java.security.InvalidParameterException
import java.util.ArrayList


class MerkleTree {

    var root: MerkleNode? = null
        private set
    private val nodes: MutableList<MerkleNode>
    private val leaves: MutableList<MerkleNode>

    init {
        this.nodes = ArrayList()
        this.leaves = ArrayList()
    }

    fun getLeaves(): List<MerkleNode> {
        return leaves
    }

    fun getNodes(): List<MerkleNode> {
        return nodes
    }

    fun appendLeaf(node: MerkleNode): MerkleNode {
        this.nodes.add(node)
        this.leaves.add(node)
        return node
    }

    fun appendLeaves(nodes: Array<MerkleNode>) {
        for (node in nodes) {
            this.appendLeaf(node)
        }
    }

    fun appendLeaf(hash: MerkleHash): MerkleNode {
        return this.appendLeaf(MerkleNode(hash))
    }

    fun appendLeaves(hashes: Array<MerkleHash>): List<MerkleNode> {
        val nodes = ArrayList<MerkleNode>()
        for (hash in hashes) {
            nodes.add(this.appendLeaf(hash))
        }
        return nodes
    }

    fun addTree(tree: MerkleTree): MerkleHash {
        if (this.leaves.size <= 0) throw InvalidParameterException("Cannot add to a tree with no leaves!")
        tree.leaves.forEach(){
            this.appendLeaf(it)
        }
        return this.buildTree()
    }

    fun buildTree(): MerkleHash {
        if (this.leaves.size <= 0) throw InvalidParameterException("Cannot add to a tree with no leaves!")
        this.buildTree(this.leaves)
        return this.root!!.hash!!
    }

    fun buildTree(nodes: List<MerkleNode>) {
        if (nodes.size <= 0) throw InvalidParameterException("Node list not expected to be empty!")

        if (nodes.size == 1) {
            this.root = nodes[0]
        } else {
            val parents = ArrayList<MerkleNode>()
            var i = 0
            while (i < nodes.size) {
                val right = if (i + 1 < nodes.size) nodes[i + 1] else null
                val parent = MerkleNode(nodes[i], right!!)
                parents.add(parent)
                i += 2
            }
            buildTree(parents)
        }
    }

    fun auditProof(leafHash: MerkleHash): List<MerkleProofHash> {
        val auditTrail = ArrayList<MerkleProofHash>()

        val leafNode = this.findLeaf(leafHash)

        if (leafNode != null) {
            if (leafNode.parent == null) throw InvalidParameterException("Expected leaf to have a parent!")
            val parent = leafNode.parent
            this.buildAuditTrail(auditTrail, parent, leafNode)
        }

        return auditTrail
    }

    private fun findLeaf(hash: MerkleHash): MerkleNode? {
        return this.leaves.stream()
                .filter { leaf -> leaf.hash!!.equals(hash) }
                .findFirst()
                .orElse(null)
    }

    private fun buildAuditTrail(auditTrail: MutableList<MerkleProofHash>, parent: MerkleNode?, child: MerkleNode) {
        if (parent != null) {
            if (child.parent !== parent) {
                throw InvalidParameterException("Parent of child is not expected parent!")
            }

            val nextChild = if (parent.getLeftNode() === child) parent.getRightNode() else parent.getLeftNode()
            val direction = if (parent.getLeftNode() === child)
                MerkleProofHash.Branch.RIGHT
            else
                MerkleProofHash.Branch.LEFT

            if (nextChild != null) auditTrail.add(MerkleProofHash(nextChild.hash!!, direction))

            this.buildAuditTrail(auditTrail, parent.parent, child.parent!!)
        }
    }

    companion object {

        fun verifyAudit(rootHash: MerkleHash, leafHash: MerkleHash, auditTrail: List<MerkleProofHash>): Boolean {
            if (auditTrail.size <= 0) throw InvalidParameterException("Audit trail cannot be empty!")

            var testHash = leafHash

            for (auditHash in auditTrail) {
                testHash = if (auditHash.direction === MerkleProofHash.Branch.RIGHT)
                    MerkleHash.create(testHash, auditHash.hash)
                else
                    MerkleHash.create(auditHash.hash, testHash)
            }

            return testHash.equals(rootHash)
        }
    }
}
