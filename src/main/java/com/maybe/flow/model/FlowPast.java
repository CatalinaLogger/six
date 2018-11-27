package com.maybe.flow.model;

import lombok.Data;

import java.util.Date;

@Data
public class FlowPast {
    private String id;

    private Integer rev;

    private String procDefId;

    private String taskDefId;

    private String taskDefKey;

    private String procInstId;

    private String procDefKey;

    private String procDefName;

    private String executionId;

    private String scopeId;

    private String subScopeId;

    private String scopeType;

    private String scopeDefinitionId;

    private String name;

    private String parentTaskId;

    private String description;

    private String owner;

    private String assignee;

    private Date startTime;

    private Date claimTime;

    private Date endTime;

    private Long duration;

    private String deleteReason;

    private Integer priority;

    private Date dueDate;

    private String formKey;

    private String category;

    private String tenantId;

    private Date lastUpdatedTime;

    private String createUser;

    private String createDept;

    private Date createTime;

    private Date finishTime;

    private Integer level;
}