package com.maybe.sys.dao;

import com.maybe.sys.model.SysDeptUser;
import com.maybe.sys.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDeptUserMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysDeptUser record);

    int insertSelective(SysDeptUser record);

    SysDeptUser selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysDeptUser record);

    int updateByPrimaryKey(SysDeptUser record);

    List<Integer> selectDeptKeysByUserId(@Param("userId") Integer userId);

    void insertUserDept(@Param("sysUser") SysUser sysUser, @Param("deptKeys") List<Integer> deptKeys);

    void removeUserDept(@Param("sysUser") SysUser sysUser, @Param("deptKeys") List<Integer> deptKeys);
}