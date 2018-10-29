package com.maybe.flow.dao;

import com.maybe.flow.model.FlowCheck;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlowCheckMapper {
    int insert(FlowCheck record);

    int insertSelective(FlowCheck record);
}