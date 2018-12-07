package com.maybe.jxc.service.impl;

import com.maybe.jxc.common.dto.ProductGroupLevelDto;
import com.maybe.jxc.common.param.GroupParam;
import com.maybe.jxc.dao.ProductGroupMapper;
import com.maybe.jxc.model.ProductGroup;
import com.maybe.jxc.service.IProductService;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.common.util.SixUtil;
import com.maybe.sys.dao.SysUtilsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductGroupMapper groupMapper;
    @Autowired
    private SysUtilsMapper sysUtilsMapper;

    @Override
    public List<ProductGroupLevelDto> selectGroupTree() {
        List<ProductGroup> groupList = groupMapper.selectAll();
        List<ProductGroupLevelDto> dtoList = new ArrayList<>();
        for (ProductGroup group: groupList) {
            ProductGroupLevelDto dto = ProductGroupLevelDto.adapt(group);
            dtoList.add(dto);
        }
        return groupListToTree(dtoList);
    }

    @Override
    public void insertGroup(GroupParam param) {
        if (checkExist(param.getParentId(), null, "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该分类");
        }
        ProductGroup group = new ProductGroup();
        BeanUtils.copyProperties(param, group);
        group.setLevel(LevelUtil.calculateLevel(getLevel(group.getParentId()), group.getParentId()));
        List<ProductGroup> list = groupMapper.selectByParentId(param.getParentId());
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
        if (checkExist(param.getParentId(), param.getId(), "name", param.getName())) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "当前层级下已存在该分类");
        }
        ProductGroup group = groupMapper.selectByPrimaryKey(param.getId());
        if (ObjectUtils.isEmpty(group)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "待修改产品分类不存在");
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
        ProductGroup group = groupMapper.selectByPrimaryKey(groupId);
        List<ProductGroup> list = groupMapper.selectByParentId(group.getParentId());
        int index = list.indexOf(group);
        if (index > 0) {
            ProductGroup up = list.get(index - 1);
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
        ProductGroup group = groupMapper.selectByPrimaryKey(groupId);
        List<ProductGroup> list = groupMapper.selectByParentId(group.getParentId());
        int index = list.indexOf(group);
        if (index < list.size() - 1) {
            ProductGroup down = list.get(index + 1);
            int seq = down.getSeq();
            down.setSeq(group.getSeq());
            group.setSeq(seq);
            groupMapper.updateByPrimaryKey(down);
            groupMapper.updateByPrimaryKey(group);
        } else {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "已经到底了");
        }
    }

    private boolean checkExist(Integer parentId, Integer groupId, String field, String value) {
        int count = sysUtilsMapper.countOfExist("jxc_product_group", parentId, groupId, field, value);
        return count > 0;
    }

    private String getLevel(Integer id) {
        ProductGroup group = groupMapper.selectByPrimaryKey(id);
        return ObjectUtils.isEmpty(group) ? null : group.getLevel();
    }


    private List<ProductGroupLevelDto> groupListToTree(List<ProductGroupLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, ProductGroupLevelDto> levelGroupMap = new LinkedMultiValueMap<>();
        List<ProductGroupLevelDto> rootList = new ArrayList<>();
        for (ProductGroupLevelDto dto: dtoList) {
            levelGroupMap.add(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, groupSeqComparator);
        transformGroupTree(rootList, LevelUtil.ROOT, levelGroupMap);
        return rootList;
    }

    private void transformGroupTree(List<ProductGroupLevelDto> groupLevelList, String level, LinkedMultiValueMap<String,ProductGroupLevelDto> levelGroupMap) {
        for (ProductGroupLevelDto groupLevelDto: groupLevelList) {
            String nextLevel = LevelUtil.calculateLevel(level, groupLevelDto.getId());
            List<ProductGroupLevelDto> tempGroupList = levelGroupMap.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempGroupList)) {
                Collections.sort(tempGroupList, groupSeqComparator);
                // 设置子部门数据
                groupLevelDto.setChildren(tempGroupList);
                // 处理子部门
                transformGroupTree(tempGroupList, nextLevel, levelGroupMap);
            }
        }
    }

    private Comparator<ProductGroupLevelDto> groupSeqComparator = Comparator.comparingInt(ProductGroup::getSeq);
}
