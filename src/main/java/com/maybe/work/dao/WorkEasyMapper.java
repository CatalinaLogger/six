package com.maybe.work.dao;

import com.maybe.work.model.WorkEasy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkEasyMapper {
    int deleteByPrimaryKey(@Param("procInstId") String procInstId);

    int insert(WorkEasy record);

    int insertSelective(WorkEasy record);

    WorkEasy selectByPrimaryKey(@Param("procInstId") String procInstId);

    int updateByPrimaryKeySelective(WorkEasy record);

    int updateByPrimaryKeyWithBLOBs(WorkEasy record);

    int updateByPrimaryKey(WorkEasy record);
}