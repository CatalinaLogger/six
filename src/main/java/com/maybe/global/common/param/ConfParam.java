package com.maybe.global.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ConfParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "字典编码", required = true)
    @NotBlank(message = "编码不能为空")
    @Length(max = 20, min = 1, message = "编码长度在1~20位之间")
    private String code;
    @ApiParam(value = "字典名称", required = true)
    @NotBlank(message = "名称不能为空")
    @Length(max = 20, min = 1, message = "名称长度在1~20位之间")
    private String name;
    @ApiParam(value = "排序号", required = true, example = "1")
    @NotNull(message = "序号不能为空")
    private Integer seq;
}
