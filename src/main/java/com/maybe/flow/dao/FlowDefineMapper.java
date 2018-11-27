package com.maybe.flow.dao;

import com.maybe.flow.model.FlowDefine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlowDefineMapper {

    List<FlowDefine> defineListByNameAndCategory(@Param("name") String name, @Param("category") String category);

    FlowDefine selectByDefineId(@Param("defineId") String defineId);
}