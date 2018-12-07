package com.maybe.jxc.service;

import com.maybe.jxc.common.dto.ProductGroupLevelDto;
import com.maybe.jxc.common.param.GroupParam;

import java.util.List;

public interface IProductService {
    List<ProductGroupLevelDto> selectGroupTree();

    void insertGroup(GroupParam param);

    void updateGroup(GroupParam param);

    void deleteGroup(Integer groupId);

    void upGroup(Integer groupId);

    void downGroup(Integer groupId);
}
