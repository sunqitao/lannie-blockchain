package cn.lannie.kt.blockchain.core.sqlparser

import cn.lannie.kt.blockchain.block.InstructionBase


/**
 */
interface InstructionParser {
    fun parse(instructionBase: InstructionBase): Boolean
}
