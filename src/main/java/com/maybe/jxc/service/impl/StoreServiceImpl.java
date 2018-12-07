package com.maybe.jxc.service.impl;

import com.maybe.jxc.common.param.StoreParam;
import com.maybe.jxc.dao.StoreMapper;
import com.maybe.jxc.model.Store;
import com.maybe.jxc.service.IStoreService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.SixUtil;
import com.maybe.sys.dao.SysUserMapper;
import com.maybe.sys.dao.SysUtilsMapper;
import com.maybe.sys.model.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class StoreServiceImpl implements IStoreService {

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;

    @Override
    public List<Store> select() {
        return storeMapper.selectAll();
    }

    @Override
    public void insert(StoreParam param) {
        if (checkExist(null, "code", param.getCode())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "仓库编码已存在");
        }
        if (checkExist(null, "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "仓库名称已存在");
        }
        SysUser duty = sysUserMapper.selectByPrimaryKey(param.getDutyId());
        if (ObjectUtils.isEmpty(duty)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "责任人不存在");
        }
        Store store = new Store();
        BeanUtils.copyProperties(param, store);
        store.setDutyName(duty.getName());
        store.setDutyPhone(duty.getPhone());
        store.setStatus(1); // 设置初始状态为正常
        SixUtil.setOperate(store);
        storeMapper.insert(store);
    }

    @Override
    public void update(StoreParam param) {
        if (checkExist(param.getId(), "code", param.getCode())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "仓库编码已存在");
        }
        if (checkExist(param.getId(), "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "仓库名称已存在");
        }
        SysUser duty = sysUserMapper.selectByPrimaryKey(param.getDutyId());
        if (ObjectUtils.isEmpty(duty)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "责任人不存在");
        }
        Store store = storeMapper.selectByPrimaryKey(param.getId());
        if (ObjectUtils.isEmpty(store)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待修改仓库不存在");
        }
        BeanUtils.copyProperties(param, store);
        store.setDutyName(duty.getName());
        store.setDutyPhone(duty.getPhone());
        SixUtil.setOperate(store);
        storeMapper.updateByPrimaryKey(store);
    }

    @Override
    public void updateStatus(Integer storeId, Integer status) {
        Store store = storeMapper.selectByPrimaryKey(storeId);
        if (ObjectUtils.isEmpty(store)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待修改仓库不存在");
        }
        store.setStatus(status);
        storeMapper.updateByPrimaryKey(store);
    }

    @Override
    public void delete(Integer storeId) {
        storeMapper.deleteByPrimaryKey(storeId);
    }

    private boolean checkExist(Integer storeId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("jxc_store", null, storeId, field, value);
        return count > 0;
    }
}
