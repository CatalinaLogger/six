package com.maybe.global.dao;

import com.maybe.global.model.GlobalConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GlobalConfMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(GlobalConf record);

    int insertSelective(GlobalConf record);

    GlobalConf selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(GlobalConf record);

    int updateByPrimaryKey(GlobalConf record);

    List<GlobalConf> findAll();
}