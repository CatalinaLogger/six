package com.maybe.flow.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 流程查询条件
 */
@Data
public class FlowParam {
    @ApiParam("流程名称")
    private String name;
    @ApiParam("任务名称")
    private String task;
    @ApiParam("流程发起人")
    private String user;
    @ApiParam(value = "紧急程度，示例：0 一般，1 普通...", example = "0")
    private Integer level;
    @ApiParam(value = "流程状态，示例：0 审核中，1 已结束", example = "0")
    private Integer status;
    @ApiParam("流程分类编码")
    private String category;
    @ApiParam(value = "开始时间（流程发起时间）", type = "string")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date start;
    @ApiParam(value = "结束时间（流程发起时间）", type = "string")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date end;
}
