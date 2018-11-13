package com.maybe.global.service.impl;

import com.maybe.global.common.dto.ConfLevelDto;
import com.maybe.global.common.param.ConfDataParam;
import com.maybe.global.common.param.ConfParam;
import com.maybe.global.dao.GlobalConfDataMapper;
import com.maybe.global.dao.GlobalConfMapper;
import com.maybe.global.model.GlobalConf;
import com.maybe.global.model.GlobalConfData;
import com.maybe.global.service.IGlobalConfService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SessionLocal;
import com.maybe.sys.dao.SysUtilsMapper;
import com.maybe.sys.service.ISysTreeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class GlobalConfServiceImpl implements IGlobalConfService {

    @Autowired
    private GlobalConfMapper globalConfMapper;
    @Autowired
    private GlobalConfDataMapper globalConfDataMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;
    @Autowired
    private ISysTreeService sysTreeService;

    @Override
    public List<GlobalConf> findAll() {
        return globalConfMapper.findAll();
    }

    @Override
    public void insert(ConfParam param) {
        GlobalConf globalConf = new GlobalConf();
        BeanUtils.copyProperties(param, globalConf);
        if (checkExist(globalConf.getId(), "code", globalConf.getCode())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "编码已存在");
        }
        if (checkExist(globalConf.getId(), "name", globalConf.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "名称已存在");
        }
        globalConf.setOperateIp(SessionLocal.getUser().getOperateIp());
        globalConf.setOperateId(SessionLocal.getUser().getId());
        globalConf.setOperateName(SessionLocal.getUser().getName());
        globalConf.setOperateTime(new Date());
        globalConfMapper.insert(globalConf);
    }

    @Override
    public void update(ConfParam param) {
        GlobalConf after = new GlobalConf();
        BeanUtils.copyProperties(param, after);
        if (checkExist(after.getId(), "code", after.getCode())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "编码已存在");
        }
        if (checkExist(after.getId(), "name", after.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "名称已存在");
        }
        GlobalConf before = globalConfMapper.selectByPrimaryKey(after.getId());
        if (ObjectUtils.isEmpty(before)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待更新分类不存在");
        }
        after.setOperateIp(SessionLocal.getUser().getOperateIp());
        after.setOperateId(SessionLocal.getUser().getId());
        after.setOperateName(SessionLocal.getUser().getName());
        after.setOperateTime(new Date());
        globalConfMapper.updateByPrimaryKey(after);
    }

    @Override
    public void delete(Integer confId) {
        globalConfMapper.deleteByPrimaryKey(confId);
        globalConfDataMapper.deleteByConfId(confId);
    }

    @Override
    public void insertData(ConfDataParam param) {
        GlobalConfData globalConfData = new GlobalConfData();
        BeanUtils.copyProperties(param, globalConfData);
        GlobalConf globalConf = globalConfMapper.selectByPrimaryKey(globalConfData.getConfId());
        if (ObjectUtils.isEmpty(globalConf)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "所属字典类别不存在");
        }
        if (checkDataExist(globalConfData.getParentId(), globalConfData.getId(), "code", globalConfData.getCode())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该编码");
        }
        if (checkDataExist(globalConfData.getParentId(), globalConfData.getId(), "name", globalConfData.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该名称");
        }
        globalConfData.setConfCode(globalConf.getCode());
        globalConfData.setLevel(LevelUtil.calculateLevel(getLevel(globalConfData.getParentId()), globalConfData.getParentId()));
        globalConfData.setOperateIp(SessionLocal.getUser().getOperateIp());
        globalConfData.setOperateId(SessionLocal.getUser().getId());
        globalConfData.setOperateName(SessionLocal.getUser().getName());
        globalConfData.setOperateTime(new Date());
        globalConfDataMapper.insertSelective(globalConfData);
    }

    @Override
    public void updateData(ConfDataParam param) {
        GlobalConfData after = new GlobalConfData();
        BeanUtils.copyProperties(param, after);
        GlobalConf globalConf = globalConfMapper.selectByPrimaryKey(after.getConfId());
        if (ObjectUtils.isEmpty(globalConf)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "所属字典类别不存在");
        }
        if (checkDataExist(after.getParentId(), after.getId(), "code", after.getCode())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该编码");
        }
        if (checkDataExist(after.getParentId(), after.getId(), "name", after.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该名称");
        }
        after.setConfCode(globalConf.getCode());
        after.setLevel(LevelUtil.calculateLevel(getLevel(after.getParentId()), after.getParentId()));
        after.setOperateIp(SessionLocal.getUser().getOperateIp());
        after.setOperateId(SessionLocal.getUser().getId());
        after.setOperateName(SessionLocal.getUser().getName());
        after.setOperateTime(new Date());
        globalConfDataMapper.updateByPrimaryKey(after);
    }

    @Override
    public void deleteData(Integer confDataId) {
        GlobalConfData globalConfData = globalConfDataMapper.selectByPrimaryKey(confDataId);
        if (ObjectUtils.isEmpty(globalConfData)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待删除字典数据不存在");
        }
        globalConfDataMapper.deleteByPrimaryKey(confDataId);
        globalConfDataMapper.deleteByLevel(globalConfData.getLevel() + "."+ globalConfData.getId() + "%");
    }

    @Override
    public List<ConfLevelDto> dataTreeByConfCode(String code) {
        return sysTreeService.ConfTreeByCode(code);
    }

    private String getLevel(Integer id) {
        GlobalConfData globalConfData = globalConfDataMapper.selectByPrimaryKey(id);
        if (globalConfData == null) {
            return null;
        }
        return globalConfData.getLevel();
    }

    private boolean checkDataExist(Integer parentId, Integer dataId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("global_conf_data", parentId, dataId, field, value);
        return count > 0;
    }

    private boolean checkExist(Integer confId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("global_conf", null, confId, field, value);
        return count > 0;
    }
}
