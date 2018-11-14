package com.maybe.sys.dao;

import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysRole;
import com.maybe.sys.model.SysRoleUser;
import com.maybe.sys.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface  SysRoleUserMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<SysRole> roleListByUserId(@Param("userId") Integer userId);

    void insertRoleUser(@Param("sysRole") SysRole sysRole, @Param("userKeys") List<Integer> userKeys);

    void removeRoleUser(@Param("sysRole") SysRole sysRole, @Param("userKeys") List<Integer> userKeys);

    void removeRoleUserByUserDept(@Param("userId") Integer userId, @Param("deptKeys") List<Integer> deptKeys);

    int countBoundByRoleId(@Param("roleId") Integer roleId, @Param("query") String query);

    List<SysUser> pageBoundByRoleId(@Param("roleId") Integer roleId, @Param("query") String query, @Param("page") PageParam page);

    int countUnboundByRoleId(@Param("roleId") Integer roleId, @Param("query") String query);

    List<SysUser> pageUnboundByRoleId(@Param("roleId") Integer roleId, @Param("query") String query, @Param("page") PageParam page);

    void deleteRoleUserByUserId(@Param("userId") Integer userId);

    void deleteRoleUserByUserKeys(@Param("userKeys") List<Integer> userKeys);

    void deleteRoleUserByRoleId(@Param("roleId") Integer roleId);
}