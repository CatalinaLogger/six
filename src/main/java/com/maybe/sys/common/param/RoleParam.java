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
 * @date 2018/5/11
 */
@Data
public class RoleParam {
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(hidden = true)
    @NotBlank(message = "角色编码不能为空")
    @Length(max = 20, min = 1, message = "角色编码长度在1~20位之间")
    private String code;
    @ApiParam(hidden = true)
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 20, min = 1, message = "角色名称长度在1~20位之间")
    private String name;
    @ApiParam(value = "角色组ID", example = "0")
    private Integer groupId;
    @ApiParam(hidden = true)
    private List<Integer> menuKeys;
    @ApiParam(hidden = true)
    private List<Integer> userKeys;
}
