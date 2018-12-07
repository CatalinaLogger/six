package com.maybe.jxc.dao;

import com.maybe.jxc.model.PropertyGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PropertyGroupMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(PropertyGroup record);

    int insertSelective(PropertyGroup record);

    PropertyGroup selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(PropertyGroup record);

    int updateByPrimaryKey(PropertyGroup record);

    List<PropertyGroup> selectAll();
}