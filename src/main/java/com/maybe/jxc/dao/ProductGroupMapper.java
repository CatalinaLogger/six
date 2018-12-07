package com.maybe.jxc.dao;

import com.maybe.jxc.model.ProductGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductGroupMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(ProductGroup record);

    int insertSelective(ProductGroup record);

    ProductGroup selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(ProductGroup record);

    int updateByPrimaryKey(ProductGroup record);

    List<ProductGroup> selectAll();

    List<ProductGroup> selectByParentId(@Param("parentId") Integer parentId);
}