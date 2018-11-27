package com.maybe.desk.service;

import com.maybe.desk.common.dto.WeeklyLevelDto;
import com.maybe.desk.common.param.WeeklyParam;
import com.maybe.desk.model.Weekly;

import java.util.Date;
import java.util.List;

public interface IReportService {
    Weekly selectBefore();

    Weekly selectWeekly();

    void insertWeekly(WeeklyParam param);

    List<WeeklyLevelDto> mergeWeekly(Date date);
}
