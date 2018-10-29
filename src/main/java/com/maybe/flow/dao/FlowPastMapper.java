package com.maybe.flow.dao;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowPast;
import com.maybe.sys.common.param.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowPastMapper {

    int pastCountByUser(@Param("userId") Integer userId, @Param("param") FlowParam param, @Param("page") PageParam page);

    List<FlowPast> pastPageByUser(@Param("userId") Integer userId, @Param("param") FlowParam param, @Param("page") PageParam page);
}