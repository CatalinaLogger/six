package com.maybe.sys.dao;

import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysDeptLead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDeptLeadMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysDeptLead record);

    int insertSelective(SysDeptLead record);

    SysDeptLead selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysDeptLead record);

    int updateByPrimaryKey(SysDeptLead record);

    List<Integer> selectLeadKeysByDeptId(@Param("deptId") Integer deptId);

    void insertDeptLead(@Param("sysDept") SysDept sysDept, @Param("leadKeys") List<Integer> leadKeys);

    void removeDeptLead(@Param("sysDept") SysDept sysDept, @Param("leadKeys") List<Integer> leadKeys);

    void removeDeptLeadByUserDept(@Param("userId") Integer userId, @Param("deptKeys") List<Integer> deptKeys);

    void deleteDeptLeadByUserId(@Param("userId") Integer userId);

    void deleteDeptLeadByUserKeys(@Param("userKeys") List<Integer> userKeys);
}