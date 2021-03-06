package com.maybe.sys.dao;

import com.maybe.sys.model.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDeptMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> findAll();
    /** 查询用户部门列表 */
    //TODO 待优化
    List<SysDept> findDeptListWithUser();

    List<SysDept> findChildrenByLevel(@Param("level") String level);

    void batchUpdateDeptLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    List<SysDept> findDeptListByUserId(@Param("userId") Integer userId);
}