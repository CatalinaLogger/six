package com.maybe.flow.dao;

import com.maybe.flow.common.param.FlowParam;
import com.maybe.flow.model.FlowMine;
import com.maybe.sys.common.param.PageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowMineMapper {

    int mineCountByUser(@Param("username") String username, @Param("param") FlowParam param, @Param("page") PageParam page);

    List<FlowMine> minePageByUser(@Param("username") String username, @Param("param") FlowParam param, @Param("page") PageParam page);

}