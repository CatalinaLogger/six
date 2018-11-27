package com.maybe.work.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class WorkParam {
    @ApiParam("流程定义ID")
    private String defineId;
    @ApiParam("任务ID")
    private String taskId;
    @ApiParam(value = "紧急程度，示例：0 一般，1 普通...", example = "0")
    private Integer level = 0;
    @ApiParam(value = "处理状态码，示例：1 同意，-1 驳回", required = true, example = "1")
    @NotNull(message = "处理编码不能为空")
    // @Pattern(regexp="^-?2$", message="发起状态不符合规则")
    private Integer taskCode;
    @NotEmpty(message = "处理结果不能为空")
    private String taskName;
    @ApiParam("处理意见")
    @Length(max = 1000, message = "处理意见长度不能超过1000个字符")
    private String taskNote;
}
