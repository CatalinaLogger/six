package com.maybe.jxc.dao;

import com.maybe.jxc.model.Property;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface PropertyMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Property record);

    int insertSelective(Property record);

    Property selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Property record);

    int updateByPrimaryKey(Property record);

    List<Property> selectByGroupId(@Param("groupId") Integer groupId);
}