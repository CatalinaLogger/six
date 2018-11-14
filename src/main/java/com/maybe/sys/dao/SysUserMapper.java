package com.maybe.sys.dao;

import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface SysUserMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKeyWithBLOBs(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int countByDeptId(@Param("deptId") Integer deptId, @Param("query") String query);

    List<SysUser> findUserPageByDeptId(@Param("deptId") Integer deptId, @Param("query") String query, @Param("page") PageParam page);

    int countByDeptLevel(@Param("level") String level, @Param("query") String query);

    List<SysUser> findUserPageByDeptLevel(@Param("level") String level, @Param("query") String query, @Param("page") PageParam page);

    SysUser findByUsername(@Param("username") String username);

    SysUser findByKeyword(@Param("keyword") String keyword);

    void deleteByUserKeys(@Param("userKeys") List<Integer> userKeys);

    /** 用户领导的人员 */
    List<SysUser> withListOfUser(@Param("parentId") Integer parentId);
    /** 没有领导的人员 */
    int userCountPageOfNone(@Param("query") String query);
    List<SysUser> userPageOfNone(@Param("query") String query, @Param("page") PageParam page);
    /** 绑定用户的下属 */
    void underPush(@Param("under") SysUser under, @Param("userKeys") List<Integer> userKeys);
    /** 解绑用户的下属 */
    void underPull(@Param("parentId") Integer parentId, @Param("userKeys") List<Integer> userKeys);
    /** 解绑用户的下属... */
    void underPullChild(@Param("userKey") Integer userKey);
    /** 根据角色编码来获取用户列表 */
    List<SysUser> userListByRoleCode(@Param("roleCode") String roleCode);
    /** 根据用户姓名来获取用户列表 */
    List<SysUser> userListByName(@Param("name") String name);
    /** 根据流程实例查询任务处理人 */
    List<SysUser> userListByProcessId(@Param("processId") String processId);

    String findJsonInfoByUserId(@Param("id") Integer id);
    /** 获取部门主管列表 */
    List<SysUser> findLeadListByDeptId(@Param("deptId") Integer deptId);
    /** 获取所有主管列表 */
    List<SysUser> findLeadList();
    /** 根据用户userId字段获取用户的主管列表 */
    List<SysUser> findLeadListByUserId(@Param("userId") Integer userId);

}