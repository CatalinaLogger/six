package com.maybe.sys.dao;

import com.maybe.sys.model.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> findChildrenByLevel(@Param("level") String level);

    void batchUpdateMenuLevel(List<SysMenu> sysMenuList);

    List<SysMenu> findAll();

    List<SysMenu> findByUserId(@Param("userId") Integer userId);

}