package com.maybe.flow.model;

import lombok.Data;

import java.util.Date;

@Data
public class FlowMine {
    private String id;

    private Integer rev;

    private String procInstId;

    private Integer level;

    private String procDefId;

    private String procDefKey;

    private String procDefName;

    private Date startTime;

    private Date endTime;

    private Long duration;

    private String startUserId;

    private String startActId;

    private String endActId;

    private String superProcessInstanceId;

    private String deleteReason;

    private String tenantId;

    private String name;

    private String callbackId;

    private String callbackType;

    private String category;

}