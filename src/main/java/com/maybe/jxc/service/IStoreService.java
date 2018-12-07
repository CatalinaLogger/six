package com.maybe.jxc.service;

import com.maybe.jxc.common.param.StoreParam;
import com.maybe.jxc.model.Store;

import java.util.List;

public interface IStoreService {
    List<Store> select();

    void insert(StoreParam param);

    void update(StoreParam param);

    void updateStatus(Integer storeId, Integer status);

    void delete(Integer storeId);
}
