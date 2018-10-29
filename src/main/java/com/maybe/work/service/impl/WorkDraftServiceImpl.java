package com.maybe.work.service.impl;

import com.maybe.sys.common.util.SessionLocal;
import com.maybe.work.dao.WorkDraftMapper;
import com.maybe.work.model.WorkDraft;
import com.maybe.work.service.IWorkDraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class WorkDraftServiceImpl implements IWorkDraftService {

    @Autowired
    private WorkDraftMapper workDraftMapper;

    @Override
    public String select(String defineId) {
        WorkDraft workDraft = workDraftMapper.selectByPrimaryKey(SessionLocal.getUser().getId() + defineId);
        return ObjectUtils.isEmpty(workDraft) ? null : workDraft.getJson();
    }

    @Override
    public void insert(String defineId, String jsonData) {
        WorkDraft workDraft = new WorkDraft();
        workDraft.setId(SessionLocal.getUser().getId() + defineId);
        workDraft.setJson(jsonData);
        if (ObjectUtils.isEmpty(workDraftMapper.selectByPrimaryKey(workDraft.getId()))) {
            workDraftMapper.insert(workDraft);
        } else {
            workDraftMapper.updateByPrimaryKeyWithBLOBs(workDraft);
        }
    }

    @Override
    public void delete(String defineId) {
        workDraftMapper.deleteByPrimaryKey(SessionLocal.getUser().getId() + defineId);
    }
}
