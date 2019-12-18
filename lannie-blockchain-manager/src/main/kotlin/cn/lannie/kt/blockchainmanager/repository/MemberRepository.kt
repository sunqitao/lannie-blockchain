package cn.lannie.kt.blockchainmanager.repository

import cn.lannie.kt.blockchainmanager.model.Member
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface MemberRepository {
    @Select("select * from member where name = #{name}")
    fun findByName(name: String): List<Member>

    /**
     * 查询同一个联盟内，除自己外其他的所有节点
     */
    @Select("select * from member where group_id = #{groupId} and  app_id!= #{id}")
    fun findByGroupIdAndAppIdNot(groupId:String,id:String):List<Member>

    /**
     *
     */
    @Select("select * from member where app_id = #{appId}")
    fun findFirstByAppId(appId:String):Member

    /**
     * 查询一个联盟内的所有节点
     */
    @Select("select * from member where group_id = #{groupId}")
    fun findByGroupId(groupId:String):List<Member>

    /**
     * 根据机构名称查询
     */
    @Select("select * from member where name = #{name} limit 0,1")
    fun findFirstByName(name:String):Member
}
