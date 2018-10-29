package com.maybe.global.dao;

import com.maybe.global.model.GlobalConfData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GlobalConfDataMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GlobalConfData record);

    int insertSelective(GlobalConfData record);

    GlobalConfData selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GlobalConfData record);

    int updateByPrimaryKey(GlobalConfData record);

    List<GlobalConfData> findByConfCode(@Param("code") String code);

    void deleteByConfId(@Param("confId") Integer confId);

    void deleteByLevel(@Param("level") String level);
}