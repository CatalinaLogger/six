package com.maybe.sys.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/4/26
 */
@Data
public class DeptParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "部门名称", required = true)
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 15, min = 2, message = "部门名称长度在2~15位之间")
    private String name;
    @ApiParam(value = "父节点ID", example = "0")
    private Integer parentId = 0;
    @ApiParam(hidden = true)
    private List<Integer> leadKeys;
    @ApiParam(value = "排序号", required = true, example = "1")
    @NotNull(message = "排序号不能为空")
    private Integer seq;
    @ApiParam("备注")
    @Length(max = 150, message = "备注长度不能超过150位")
    private String remark;
}
