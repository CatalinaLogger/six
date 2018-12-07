package com.maybe.sys.service.impl;

import com.maybe.global.common.dto.ConfLevelDto;
import com.maybe.global.dao.GlobalConfDataMapper;
import com.maybe.global.model.GlobalConfData;
import com.maybe.sys.common.config.ResultEnum;
import com.maybe.sys.common.dto.DeptLevelDto;
import com.maybe.sys.common.dto.MenuLevelDto;
import com.maybe.sys.common.exception.SixException;
import com.maybe.sys.common.util.LevelUtil;
import com.maybe.sys.dao.SysDeptMapper;
import com.maybe.sys.dao.SysMenuMapper;
import com.maybe.sys.dao.SysUserMapper;
import com.maybe.sys.model.SysDept;
import com.maybe.sys.model.SysMenu;
import com.maybe.sys.model.SysUser;
import com.maybe.sys.service.ISysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/4/30
 */
@Service
public class SysTreeServiceImpl implements ISysTreeService{

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private GlobalConfDataMapper globalConfDataMapper;

    /**
     * 获取部门树
     * @return
     */
    @Override
    public List<DeptLevelDto> deptTree() {
        List<SysDept> deptList = sysDeptMapper.findAll();
        List<SysUser> leadList = sysUserMapper.findLeadList();
        LinkedMultiValueMap<Integer, SysUser> leadMap = new LinkedMultiValueMap<>();
        for (SysUser user : leadList) {
            leadMap.add(user.getDeptId(), user);
        }
        List<DeptLevelDto> dtoList = new ArrayList<>();
        for (SysDept dept: deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dto.setLead(leadMap.get(dto.getId()));
            dtoList.add(dto);
        }
        return deptListToTree(dtoList, "0");
    }

    @Override
    public List<MenuLevelDto> menuTree() {
        List<SysMenu> menuList = sysMenuMapper.findAll();
        List<MenuLevelDto> dtoList = new ArrayList<>();
        for (SysMenu menu: menuList) {
            MenuLevelDto dto = MenuLevelDto.adapt(menu);
            dtoList.add(dto);
        }
        return menuListToTree(dtoList);
    }

    @Override
    public List<MenuLevelDto> menuTreeByUserId(Integer userId) {
        List<SysMenu> menuList = sysMenuMapper.findByUserId(userId);
        List<MenuLevelDto> dtoList = new ArrayList<>();
        for (SysMenu menu: menuList) {
            MenuLevelDto dto = MenuLevelDto.adapt(menu);
            dtoList.add(dto);
        }
        return menuListToTree(dtoList);
    }

    @Override
    public List<ConfLevelDto> ConfTreeByCode(String code) {
        if (ObjectUtils.isEmpty(code)) {
            throw new SixException(ResultEnum.ERROR_PARAM.getCode(), "请提供字典类别");
        }
        List<GlobalConfData> dictionaryDataList = globalConfDataMapper.findByConfCode(code);
        List<ConfLevelDto> dtoList = new ArrayList<>();
        for (GlobalConfData dictionaryData: dictionaryDataList) {
            ConfLevelDto dto = ConfLevelDto.adapt(dictionaryData);
            dtoList.add(dto);
        }
        return dictionaryDataListToTree(dtoList);
    }


    /**
     * 处理部门树根节点
     * @param dtoList
     * @return
     */
    private List<DeptLevelDto> deptListToTree(List<DeptLevelDto> dtoList, String rootLevel) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, DeptLevelDto> levelDeptMap = new LinkedMultiValueMap<>();
        List<DeptLevelDto> rootList = new ArrayList<>();
        for (DeptLevelDto dto: dtoList) {
            levelDeptMap.add(dto.getLevel(), dto);
            if (rootLevel.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, deptSeqComparator);
        transformDeptTree(rootList, rootLevel, levelDeptMap);
        return rootList;
    }
    private List<MenuLevelDto> menuListToTree(List<MenuLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, MenuLevelDto> levelMenuMap = new LinkedMultiValueMap<>();
        List<MenuLevelDto> rootList = new ArrayList<>();
        for (MenuLevelDto dto: dtoList) {
            levelMenuMap.add(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, menuSeqComparator);
        transformMenuTree(rootList, LevelUtil.ROOT, levelMenuMap);
        return rootList;
    }


    private List<ConfLevelDto> dictionaryDataListToTree(List<ConfLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, ConfLevelDto> levelConfDataMap = new LinkedMultiValueMap<>();
        List<ConfLevelDto> rootList = new ArrayList<>();
        for (ConfLevelDto dto: dtoList) {
            levelConfDataMap.add(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, dictionaryDataSeqComparator);
        transformConfDataTree(rootList, LevelUtil.ROOT, levelConfDataMap);
        return rootList;
    }

    /**
     * 递归处理部门树
     * @param deptLevelList
     * @param level
     * @param levelDeptMap
     */
    private void transformDeptTree(List<DeptLevelDto> deptLevelList, String level, LinkedMultiValueMap<String, DeptLevelDto> levelDeptMap) {
        for (DeptLevelDto deptLevelDto: deptLevelList) {
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            List<DeptLevelDto> tempDeptList = levelDeptMap.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempDeptList)) {
                Collections.sort(tempDeptList, deptSeqComparator);
                // 设置子部门数据
                deptLevelDto.setChildren(tempDeptList);
                // 处理子部门
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }
    private void transformMenuTree(List<MenuLevelDto> menuLevelList, String level, LinkedMultiValueMap<String, MenuLevelDto> levelMenuMap) {
        for (MenuLevelDto menuLevelDto: menuLevelList) {
            String nextLevel = LevelUtil.calculateLevel(level, menuLevelDto.getId());
            List<MenuLevelDto> tempMenuList = levelMenuMap.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempMenuList)) {
                Collections.sort(tempMenuList, menuSeqComparator);
                // 设置子部门数据
                menuLevelDto.setChildren(tempMenuList);
                // 处理子部门
                transformMenuTree(tempMenuList, nextLevel, levelMenuMap);
            }
        }
    }
    private void transformConfDataTree(List<ConfLevelDto> dictionaryDataLevelList, String level, LinkedMultiValueMap<String,ConfLevelDto> levelConfDataMap) {
        for (ConfLevelDto dictionaryDataLevelDto: dictionaryDataLevelList) {
            String nextLevel = LevelUtil.calculateLevel(level, dictionaryDataLevelDto.getId());
            List<ConfLevelDto> tempMenuList = levelConfDataMap.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempMenuList)) {
                Collections.sort(tempMenuList, dictionaryDataSeqComparator);
                // 设置子部门数据
                dictionaryDataLevelDto.setChildren(tempMenuList);
                // 处理子部门
                transformConfDataTree(tempMenuList, nextLevel, levelConfDataMap);
            }
        }
    }

    /**
     * 排序规则
     */
    private Comparator<DeptLevelDto> deptSeqComparator = Comparator.comparingInt(SysDept::getSeq);
    private Comparator<MenuLevelDto> menuSeqComparator = Comparator.comparingInt(SysMenu::getSeq);
    private Comparator<ConfLevelDto> dictionaryDataSeqComparator = Comparator.comparingInt(GlobalConfData::getSeq);
}
