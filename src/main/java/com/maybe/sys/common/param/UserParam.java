package com.maybe.sys.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
@Data
public class UserParam implements Serializable{
    @ApiParam(hidden = true)
    private Integer id;
    @ApiParam(value = "姓名", required = true)
    @NotBlank(message = "姓名不能为空")
    @Length(min = 1, max = 20, message = "姓名长度在1~20位之间")
    private String name;
    @ApiParam(value = "号码", required = true)
    @NotBlank(message = "号码不能为空")
    @Length(min = 1, max = 20, message = "号码长度在1~13位之间")
    private String phone;
    @ApiParam(value = "邮箱", required = true)
    @NotBlank(message = "邮箱不能为空")
    @Length(min = 5, max = 60, message = "邮箱长度在5~60位之间")
    private String mail;
    @ApiParam(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Length(min = 1, max = 20, message = "用户名长度在1~20位之间")
    private String username;
    @ApiParam(value = "状态码", required = true, example = "0")
    @NotNull(message = "状态码不能为空")
    @Min(value = 0, message = "状态码不合法")
    @Max(value = 2, message = "状态码不合法")
    private Integer status;
    @ApiParam("备注")
    @Length(max = 200, message = "备注长度不能超过200位")
    private String remark;
    @ApiParam(hidden = true)
    private List<Integer> deptKeys;
    @ApiParam(hidden = true)
    private List<Integer> roleKeys;
}
