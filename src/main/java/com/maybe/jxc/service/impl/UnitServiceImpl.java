package com.maybe.jxc.service.impl;

import com.maybe.jxc.common.dto.UnitLevelDto;
import com.maybe.jxc.common.param.UnitParam;
import com.maybe.jxc.service.IUnitService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitServiceImpl implements IUnitService {

    @Override
    public List<UnitLevelDto> selectList(Integer category) {
        return null;
    }

    @Override
    public void insert(UnitParam param) {

    }

    @Override
    public void update(UnitParam param) {

    }

    @Override
    public void delete(Integer unitId) {

    }

    @Override
    public void up(Integer unitId) {

    }

    @Override
    public void down(Integer unitId) {

    }
}
