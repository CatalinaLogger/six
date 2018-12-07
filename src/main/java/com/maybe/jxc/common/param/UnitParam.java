package com.maybe.jxc.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UnitParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "单位名称", required = true)
    @NotBlank(message = "单位名称不能为空")
    @Length(max = 64, min = 1, message = "单位名称长度在1~64位之间")
    private String name;
    @ApiParam(value = "换算关系", required = true)
    private Integer num = 1;
    @ApiParam(value = "单位分类", required = true)
    @NotNull(message = "单位分类不能为空")
    private Integer category;
    @ApiParam(value = "副单位数组")
    private List<UnitParam> children;
}
