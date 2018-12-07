package com.maybe.jxc.service;

import com.maybe.jxc.common.param.GroupParam;
import com.maybe.jxc.common.param.PropertyParam;
import com.maybe.jxc.model.Property;
import com.maybe.jxc.model.PropertyGroup;

import java.util.List;

public interface IPropertyService {
    List<PropertyGroup> selectGroupList();

    void insertGroup(GroupParam param);

    void updateGroup(GroupParam param);

    void deleteGroup(Integer groupId);

    void upGroup(Integer groupId);

    void downGroup(Integer groupId);

    List<Property> selectList(Integer groupId);

    void insert(PropertyParam param);

    void update(PropertyParam param);

    void delete(Integer groupId);

    void up(Integer groupId);

    void down(Integer groupId);
}
