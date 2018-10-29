package com.maybe.global.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ConfDataParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "所属字典类别ID", required = true, example = "1")
    @NotNull(message = "类别不能为空")
    private Integer confId;
    @ApiParam(value = "字典数据编码", required = true)
    @NotBlank(message = "编码不能为空")
    @Length(max = 20, min = 1, message = "编码长度在1~20位之间")
    private String code;
    @ApiParam(value = "字典数据名称", required = true)
    @NotBlank(message = "名称不能为空")
    @Length(max = 20, min = 1, message = "名称长度在1~20位之间")
    private String name;
    @ApiParam(value = "字典数据数值", example = "1")
    private Integer value;
    @ApiParam(value = "父节点ID", example = "1")
    private Integer parentId = 0;
    @ApiParam(value = "排序号", required = true, example = "1")
    @NotNull(message = "排序号不能为空")
    private Integer seq;
}
