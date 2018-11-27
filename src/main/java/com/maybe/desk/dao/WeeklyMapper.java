package com.maybe.desk.dao;

import com.maybe.desk.model.Weekly;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WeeklyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Weekly record);

    int insertSelective(Weekly record);

    Weekly selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Weekly record);

    int updateByPrimaryKey(Weekly record);

    Weekly selectByOperateIdAndWeekAndYear(@Param("operateId") Integer operateId, @Param("week") Integer week, @Param("year") Integer year);

    List<Weekly> selectByWeekAndYearAndLevelLike(@Param("week") Integer week, @Param("year") Integer year, @Param("level") Integer level);

}