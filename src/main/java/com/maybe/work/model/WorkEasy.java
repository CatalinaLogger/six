package com.maybe.work.model;

import lombok.Data;

import java.util.Date;
@Data
public class WorkEasy {
    private String procInstId;

    private String code;

    private Integer userId;

    private String userName;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private String operateIp;

    private Integer operateId;

    private String operateName;

    private Date operateTime;

    private String content;

    private String carbon;
}