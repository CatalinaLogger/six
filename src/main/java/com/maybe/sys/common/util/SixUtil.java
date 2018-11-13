package com.maybe.sys.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
