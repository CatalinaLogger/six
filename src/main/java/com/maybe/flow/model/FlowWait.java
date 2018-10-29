package com.maybe.flow.model;

import lombok.Data;

import java.util.Date;

@Data
public class FlowWait {
    private String id;

    private Integer rev;

    private String executionId;

    private String procInstId;

    private String procDefId;

    private String procDefKey;

    private String procDefName;

    private String taskDefId;

    private String scopeId;

    private String subScopeId;

    private String scopeType;

    private String scopeDefinitionId;

    private String name;

    private String parentTaskId;

    private String description;

    private String taskDefKey;

    private String owner;

    private String assignee;

    private String delegation;

    private Integer priority;

    private Date createTime;

    private Date dueDate;

    private String category;

    private Integer suspensionState;

    private String tenantId;

    private String formKey;

    private Date claimTime;

    private Byte isCountEnabled;

    private Integer varCount;

    private Integer idLinkCount;

    private Integer subTaskCount;

    private String createUser;

    private String createDept;

    private Date startTime;

    private Integer level;
}