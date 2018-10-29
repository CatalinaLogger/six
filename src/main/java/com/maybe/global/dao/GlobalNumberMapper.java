package com.maybe.global.dao;

import com.maybe.global.model.GlobalNumber;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GlobalNumberMapper {
    int deleteByPrimaryKey(String key);

    int insert(GlobalNumber record);

    int insertSelective(GlobalNumber record);

    GlobalNumber selectByPrimaryKey(String key);

    int updateByPrimaryKeySelective(GlobalNumber record);

    int updateByPrimaryKey(GlobalNumber record);
}