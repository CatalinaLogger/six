package com.maybe.flow.model;

import lombok.Data;

@Data
public class FlowCheck {
    private String taskId;

    private Integer taskCode;

    private String taskNote;

    public FlowCheck(String taskId, Integer taskCode, String taskNote) {
        this.taskId = taskId;
        this.taskCode = taskCode;
        this.taskNote = taskNote;
    }
}