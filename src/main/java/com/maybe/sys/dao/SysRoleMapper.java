package com.maybe.sys.dao;

import com.maybe.sys.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> findAll();

    int countByName(@Param("name") String name, @Param("id") Integer id);
}