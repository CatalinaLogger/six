package com.maybe.jxc.service.impl;

import com.maybe.jxc.common.param.GroupParam;
import com.maybe.jxc.common.param.PropertyParam;
import com.maybe.jxc.dao.PropertyGroupMapper;
import com.maybe.jxc.dao.PropertyMapper;
import com.maybe.jxc.model.Property;
import com.maybe.jxc.model.PropertyGroup;
import com.maybe.jxc.service.IPropertyService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.SixUtil;
import com.maybe.sys.dao.SysUtilsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class PropertyServiceImpl implements IPropertyService {

    @Autowired
    private PropertyGroupMapper groupMapper;
    @Autowired
    private PropertyMapper propertyMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;

    @Override
    public List<PropertyGroup> selectGroupList() {
        return groupMapper.selectAll();
    }

    @Override
    public void insertGroup(GroupParam param) {
        if (checkGroupExist(null, "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "属性已存在");
        }
        PropertyGroup group = new PropertyGroup();
        BeanUtils.copyProperties(param, group);
        List<PropertyGroup> list = groupMapper.selectAll();
        if (list.size() > 0) {
            group.setSeq(list.get(list.size() - 1).getSeq() + 1);
        } else {
            group.setSeq(1);
        }
        SixUtil.setOperate(group);
        groupMapper.insert(group);
    }

    @Override
    public void updateGroup(GroupParam param) {
        if (checkGroupExist(param.getId(), "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "属性已存在");
        }
        PropertyGroup group = groupMapper.selectByPrimaryKey(param.getId());
        if (ObjectUtils.isEmpty(group)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待修改属性不存在");
        }
        BeanUtils.copyProperties(param, group);
        SixUtil.setOperate(group);
        groupMapper.updateByPrimaryKey(group);
    }

    @Override
    public void deleteGroup(Integer groupId) {
    }

    @Override
    @Transactional
    public void upGroup(Integer groupId) {
        PropertyGroup group = groupMapper.selectByPrimaryKey(groupId);
        List<PropertyGroup> list = groupMapper.selectAll();
        int index = list.indexOf(group);
        if (index > 0) {
            PropertyGroup up = list.get(index - 1);
            int seq = up.getSeq();
            up.setSeq(group.getSeq());
            group.setSeq(seq);
            groupMapper.updateByPrimaryKey(up);
            groupMapper.updateByPrimaryKey(group);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "已经到顶了");
        }
    }

    @Override
    @Transactional
    public void downGroup(Integer groupId) {
        PropertyGroup group = groupMapper.selectByPrimaryKey(groupId);
        List<PropertyGroup> list = groupMapper.selectAll();
        int index = list.indexOf(group);
        if (index < list.size() - 1) {
            PropertyGroup down = list.get(index + 1);
            int seq = down.getSeq();
            down.setSeq(group.getSeq());
            group.setSeq(seq);
            groupMapper.updateByPrimaryKey(down);
            groupMapper.updateByPrimaryKey(group);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "已经到底了");
        }
    }

    @Override
    public List<Property> selectList(Integer groupId) {
        return propertyMapper.selectByGroupId(groupId);
    }

    @Override
    public void insert(PropertyParam param) {
        if (checkExist(param.getGroupId(), null, "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "属性值已存在");
        }
        Property property = new Property();
        BeanUtils.copyProperties(param, property);
        List<Property> list = propertyMapper.selectByGroupId(param.getGroupId());
        if (list.size() > 0) {
            property.setSeq(list.get(list.size() - 1).getSeq() + 1);
        } else {
            property.setSeq(1);
        }
        SixUtil.setOperate(property);
        propertyMapper.insert(property);
    }

    @Override
    public void update(PropertyParam param) {
        if (checkExist(param.getGroupId(), param.getId(), "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "属性值已存在");
        }
        Property property = propertyMapper.selectByPrimaryKey(param.getId());
        if (ObjectUtils.isEmpty(property)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待修改属性值不存在");
        }
        property.setName(param.getName());
        SixUtil.setOperate(property);
        propertyMapper.updateByPrimaryKey(property);
    }

    @Override
    public void delete(Integer groupId) {

    }

    @Override
    @Transactional
    public void up(Integer groupId) {
        Property property = propertyMapper.selectByPrimaryKey(groupId);
        List<Property> list = propertyMapper.selectByGroupId(property.getGroupId());
        int index = list.indexOf(property);
        if (index > 0) {
            Property up = list.get(index - 1);
            int seq = up.getSeq();
            up.setSeq(property.getSeq());
            property.setSeq(seq);
            propertyMapper.updateByPrimaryKey(up);
            propertyMapper.updateByPrimaryKey(property);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "已经到顶了");
        }
    }

    @Override
    @Transactional
    public void down(Integer groupId) {
        Property property = propertyMapper.selectByPrimaryKey(groupId);
        List<Property> list = propertyMapper.selectByGroupId(property.getGroupId());
        int index = list.indexOf(property);
        if (index < list.size() - 1) {
            Property down = list.get(index + 1);
            int seq = down.getSeq();
            down.setSeq(property.getSeq());
            property.setSeq(seq);
            propertyMapper.updateByPrimaryKey(down);
            propertyMapper.updateByPrimaryKey(property);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "已经到底了");
        }
    }

    private boolean checkGroupExist(Integer groupId, String field, String value) {
        return sysUtilsMapper.countOfExist("jxc_property_group", null, groupId, field, value) > 0;
    }

    private boolean checkExist(Integer groupId, Integer propertyId, String field, String value) {
        return sysUtilsMapper.countOfExistByGroup("jxc_property", groupId, propertyId, field, value) > 0;
    }

}
