package cn.lannie.kt.blockchain.core.manager

import cn.lannie.kt.blockchain.block.Block
import cn.lannie.kt.blockchain.block.Instruction
import cn.lannie.kt.blockchain.common.PermissionType
import cn.lannie.kt.blockchain.core.bean.Permission
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet


/**
 * 对Permission信息的存储和使用
 *
 */
@Service
class PermissionManager {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 校验block内的所有指令的权限是否合法
     * @param block 区块
     * @return 合法
     */
    fun checkPermission(block: Block): Boolean {
        val instructions = block.blockBody!!.instructions!!
        return checkPermission(instructions)
    }

    fun checkPermission(instructions: List<Instruction>): Boolean {
        for (instruction in instructions) {
            val publicKey = instruction.publicKey
            val tableName = instruction.table
            val operation = instruction.operation
            //TODO 这块要优化，循环次数太多，需要精简
            if (!checkOperation(publicKey!!, tableName!!, operation)) {
                return false
            }
        }
        return true
    }


    /**
     * 校验某用户对某表的某个操作是否有权限
     *
     * @param publicKey
     * 公钥
     * @param tableName
     * 表名
     * @param operation
     * 操作
     * @return 有权限true
     */
    private fun checkOperation(publicKey: String, tableName: String, operation: Byte): Boolean {
        val permissionList = PERMISSION_MAP[tableName]!!

        val userPermissionSet = HashSet<Byte>()
        for (permission in permissionList) {
            //如果是不限用户的情况，取到该表的所有公开的权限
            if ("*" == permission.publicKey) {
                userPermissionSet.add(permission.permissionType)
            } else {
                //找到该publicKey的所有权限
                if (publicKey == permission.publicKey) {
                    userPermissionSet.add(permission.permissionType)
                }
            }
        }

        //判断该用户的权限是否包含operation
        return (userPermissionSet.contains(PermissionType.OWNER)
                || userPermissionSet.contains(PermissionType.ALL)
                || userPermissionSet.contains(operation))
    }


    /**
     * 保存权限信息，static常驻内存，按table划分到map里
     *
     * @param permissions
     * permissions
     */
    fun savePermissionList(permissions: List<Permission>) {
        PERMISSION_MAP.clear()
        for (permission in permissions) {
            val key = permission.tableName
            if (!PERMISSION_MAP.containsKey(key)) {
                PERMISSION_MAP[key!!] = ArrayList<Permission>()
            }
            PERMISSION_MAP[key]?.add(permission)
        }
        logger.info("所有的权限信息：$PERMISSION_MAP")
    }

    companion object {

        /**
         * 将权限信息常驻内存
         */
        val PERMISSION_MAP: MutableMap<String, ArrayList<Permission>> = HashMap<String, ArrayList<Permission>>()
    }

}
