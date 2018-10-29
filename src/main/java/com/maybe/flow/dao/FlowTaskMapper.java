package com.maybe.flow.dao;

import com.maybe.flow.model.FlowTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowTaskMapper {

    List<FlowTask> taskListByProcessId(@Param("processId") String processId);
}