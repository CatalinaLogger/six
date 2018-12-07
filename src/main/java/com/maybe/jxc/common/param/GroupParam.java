package com.maybe.jxc.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Data
public class GroupParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    @Length(min = 1, max = 64, message = "名称长度在1~64位之间")
    private String name;
    @ApiParam(value = "父节点ID", example = "0")
    private Integer parentId = 0;
}
