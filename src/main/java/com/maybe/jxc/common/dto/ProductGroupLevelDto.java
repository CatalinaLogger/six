package com.maybe.jxc.common.dto;

import com.maybe.jxc.model.ProductGroup;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductGroupLevelDto extends ProductGroup {

    private List<ProductGroupLevelDto> children = new ArrayList<>();

    public static ProductGroupLevelDto adapt(ProductGroup group) {
        ProductGroupLevelDto dto = new ProductGroupLevelDto();
        BeanUtils.copyProperties(group, dto);
        return dto;
    }
}
