package com.maybe.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 一些通用的数据库操作
 */
@Mapper
public interface SysUtilsMapper {

    /**
     * 查询已存在记录数
     * @param tableName 数据库表名
     * @param parentId 父级ID
     * @param id ID
     * @param field 属性名
     * @param value 属性值
     * @return 存在记录数
     */
    int countOfExist(@Param("tableName") String tableName, @Param("parentId") Object parentId, @Param("id") Object id, @Param("field") String field, @Param("value") Object value);

}