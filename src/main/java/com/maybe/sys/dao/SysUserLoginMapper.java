package com.maybe.sys.dao;

import com.maybe.sys.common.param.PageParam;
import com.maybe.sys.model.SysUserLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserLoginMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUserLogin record);

    int insertSelective(SysUserLogin record);

    SysUserLogin selectByPrimaryKey(Integer id);
    

    int updateByPrimaryKeySelective(SysUserLogin record);

    int updateByPrimaryKey(SysUserLogin record);

    void getInfoById();

    int loginCount(@Param("userId") Integer userId);

    List<SysUserLogin> loginPage(@Param("userId") Integer userId, @Param("page") PageParam page);
}