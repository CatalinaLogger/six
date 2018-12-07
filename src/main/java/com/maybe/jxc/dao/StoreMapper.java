package com.maybe.jxc.dao;

import com.maybe.jxc.model.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(Store record);

    int insertSelective(Store record);

    Store selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(Store record);

    int updateByPrimaryKey(Store record);

    List<Store> selectAll();
}