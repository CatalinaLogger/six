package com.maybe.sys.common.util;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;

public class SixUtil {

    public static Map<String, List<Integer>> handelKeys(List<Integer> oldKeys, List<Integer> newKeys) {
        Map<String, List<Integer>> map = new HashMap<>();
        List<Integer> addKeys = new ArrayList<>();
        List<Integer> delKeys = new ArrayList<>();
        addKeys.addAll(newKeys);
        addKeys.removeAll(oldKeys);
        delKeys.addAll(oldKeys);
        delKeys.removeAll(newKeys);
        if (addKeys.size() > 0) {
            map.put("addKeys", addKeys);
        }
        if (delKeys.size() > 0) {
            map.put("delKeys", delKeys);
        }
        return map;
    }

    public static void setOperate(Object obj) {
        try {
            BeanUtils.setProperty(obj, "operateIp", SessionLocal.getUser().getOperateIp());
            BeanUtils.setProperty(obj, "operateId", SessionLocal.getUser().getId());
            BeanUtils.setProperty(obj, "operateName", SessionLocal.getUser().getName());
            BeanUtils.setProperty(obj, "operateTime", new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
