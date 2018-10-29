package com.maybe.sys.common.util;

import org.springframework.util.StringUtils;

/**
 * @author jin
 * @description:
 * @date 2018/4/26
 */
public class LevelUtil {

    public final static String SEPARATOR = ".";
    public final static String ROOT = "0";

    public static String calculateLevel(String parentLevel, Integer parentId) {
        if (StringUtils.isEmpty(parentLevel)) {
            return ROOT;
        } else {
            return parentLevel+SEPARATOR+parentId.toString();
        }
    }
}
