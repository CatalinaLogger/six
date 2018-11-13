package com.maybe.sys.service;

import com.maybe.global.common.dto.ConfLevelDto;
import com.maybe.sys.common.dto.DeptLevelDto;
import com.maybe.sys.common.dto.MenuLevelDto;

import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/4/30
 */
public interface ISysTreeService {

    List<DeptLevelDto> deptTree();

    List<MenuLevelDto> menuTree();

    List<MenuLevelDto> menuTreeByUserId(Integer userId);

    List<ConfLevelDto> ConfTreeByCode(String code);

}
