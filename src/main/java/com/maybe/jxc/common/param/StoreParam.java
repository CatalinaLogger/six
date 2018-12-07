package com.maybe.jxc.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StoreParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "仓库编码", required = true)
    @NotBlank(message = "仓库编码不能为空")
    @Length(max = 64, min = 1, message = "仓库编码长度在1~64位之间")
    private String code;
    @ApiParam(value = "仓库名称", required = true)
    @NotBlank(message = "仓库名称不能为空")
    @Length(max = 64, min = 1, message = "仓库名称长度在1~64位之间")
    private String name;
    @ApiParam(value = "仓库地址")
    @Length(max = 200, message = "仓库地址不能超过200位")
    private String address;
    @ApiParam(value = "责任人ID", required = true)
    @NotNull(message = "责任人ID不能为空")
    private Integer dutyId;
    @ApiParam(value = "联系电话")
    private String dutyLink;
    @ApiParam(value = "排序号", required = true, example = "1")
    @NotNull(message = "排序号不能为空")
    private Integer seq;
    @ApiParam("备注")
    @Length(max = 150, message = "备注长度不能超过150位")
    private String remark;
}
