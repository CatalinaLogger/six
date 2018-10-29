package com.maybe.work.dao;

import com.maybe.work.model.WorkDraft;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkDraftMapper {
    int deleteByPrimaryKey(String id);

    int insert(WorkDraft record);

    int insertSelective(WorkDraft record);

    WorkDraft selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WorkDraft record);

    int updateByPrimaryKeyWithBLOBs(WorkDraft record);
}