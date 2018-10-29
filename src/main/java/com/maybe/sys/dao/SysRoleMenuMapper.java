package com.maybe.sys.dao;

import com.maybe.sys.model.SysRole;
import com.maybe.sys.model.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMenuMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);

    SysRoleMenu selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysRoleMenu record);

    int updateByPrimaryKey(SysRoleMenu record);

    List<Integer> selectMenuKeysByRoleId(@Param("roleId") Integer roleId);

    void insertRoleMenu(@Param("sysRole") SysRole sysRole, @Param("menuKeys") List<Integer> menuKeys);

    void removeRoleMenu(@Param("sysRole") SysRole sysRole, @Param("menuKeys") List<Integer> menuKeys);

    void deleteRoleMenuByRoleId(@Param("roleId") Integer roleId);

    void deleteRoleMenuByMenuId(@Param("menuId") Integer menuId);
}