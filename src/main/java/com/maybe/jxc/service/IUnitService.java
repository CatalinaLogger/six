package com.maybe.jxc.service;

import com.maybe.jxc.common.dto.UnitLevelDto;
import com.maybe.jxc.common.param.UnitParam;

import java.util.List;

public interface IUnitService {

    List<UnitLevelDto> selectList(Integer category);

    void insert(UnitParam param);

    void update(UnitParam param);

    void delete(Integer unitId);

    void up(Integer unitId);

    void down(Integer unitId);
}
