package com.maybe.jxc.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class PropertyParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "属性值", required = true)
    @NotBlank(message = "属性值不能为空")
    @Length(min = 1, max = 64, message = "属性值长度在1~64位之间")
    private String name;
    @ApiParam(hidden = true)
    private Integer groupId;
}
