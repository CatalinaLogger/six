package com.maybe.global.service;


import com.maybe.global.common.dto.ConfLevelDto;
import com.maybe.global.common.param.ConfDataParam;
import com.maybe.global.common.param.ConfParam;
import com.maybe.global.model.GlobalConf;

import java.util.List;

public interface IGlobalConfService {

    List<GlobalConf> findAll();

    void insert(ConfParam param);

    void update(ConfParam param);

    void delete(Integer confId);

    void insertData(ConfDataParam param);

    void updateData(ConfDataParam param);

    void deleteData(Integer confDataId);

    List<ConfLevelDto> dataTreeByConfCode(String code);
}
