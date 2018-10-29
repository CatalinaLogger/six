package com.maybe.flow.model;

import lombok.Data;

import java.util.Date;

@Data
public class FlowDefine {
    private String id;

    private Integer rev;

    private String category;

    private String name;

    private String key;

    private Integer version;

    private Date deployTime;

    private String deploymentId;

    private String resourceName;

    private String dgrmResourceName;

    private String description;

    private Byte hasStartFormKey;

    private Byte hasGraphicalNotation;

    private Integer suspensionState;

    private String tenantId;

    private String engineVersion;

    private String derivedFrom;

    private String derivedFromRoot;

    private Integer derivedVersion;
}