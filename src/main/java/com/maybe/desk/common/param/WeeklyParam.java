package com.maybe.desk.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class WeeklyParam {
    @ApiParam(value = "本周完成工作", required = true)
    @NotBlank(message = "本周完成工作不能为空")
    @Length(max = 1000, message = "本周完成工作字数不能超过1000")
    private String done;
    @ApiParam(value = "本周工作总结")
    @Length(max = 1000, message = "本周工作总结字数不能超过1000")
    private String sum;
    @ApiParam(value = "下周工作计划", required = true)
    @NotBlank(message = "下周工作计划不能为空")
    @Length(max = 1000, message = "下周工作计划字数不能超过1000")
    private String plan;
    @ApiParam(value = "需要协调与帮助")
    @Length(max = 1000, message = "需要协调与帮助字数不能超过1000")
    private String help;
    @ApiParam(value = "备注")
    @Length(max = 1000, message = "备注字数不能超过1000")
    private String remark;
}
