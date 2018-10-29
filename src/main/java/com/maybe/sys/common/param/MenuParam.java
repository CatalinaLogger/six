package com.maybe.sys.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jin
 * @description:
 * @date 2018/4/26
 */
@Data
public class MenuParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "菜单名称", required = true)
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 10, min = 1, message = "菜单名称长度在1~10位之间")
    private String name;
    @ApiParam(value = "路由编码", required = true)
    @NotBlank(message = "路由编码不能为空")
    @Length(max = 50, min = 1, message = "路由编码长度在1~50位之间")
    private String path;
    @ApiParam("重定向路径")
    @Length(max = 100, message = "重定向路径不能超过100位")
    private String redirect;
    @ApiParam("菜单图标")
    private String icon;
    @ApiParam(value = "是否隐藏：0 否，1 是", required = true, example = "0")
    @NotNull(message = "是否隐藏值不能为空")
    @Min(value = 0, message = "是否隐藏值只能为0或1")
    @Max(value = 1, message = "是否隐藏值只能为0或1")
    private Integer hidden;
    @ApiParam(value = "是否缓存：0 否，1 是", required = true, example = "0")
    @NotNull(message = "是否缓存值不能为空")
    @Min(value = 0, message = "是否缓存值只能为0或1")
    @Max(value = 1, message = "是否缓存值只能为0或1")
    private Integer cache;
    @ApiParam(value = "父节点ID", example = "0")
    private Integer parentId = 0;
    @ApiParam(value = "排序号", required = true, example = "1")
    @NotNull(message = "排序号不能为空")
    private Integer seq;
    @ApiParam(value = "类型值：0 目录，1 菜单，2 按钮", required = true, example = "0")
    @NotNull(message = "类型值不能为空")
    @Min(value = 0, message = "类型值只能为0,1,2")
    @Max(value = 2, message = "类型值只能为0,1,2")
    private Integer type;
    @ApiParam("备注")
    @Length(max = 150, message = "备注长度不能超过150位")
    private String remark;

}
