package com.maybe.desk.common.dto;

import com.maybe.desk.model.Weekly;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.List;

@Data
public class WeeklyLevelDto extends Weekly {

    private List<WeeklyLevelDto> children = new ArrayList<>();

    public static WeeklyLevelDto adapt(Weekly weekly) {
        WeeklyLevelDto dto = new WeeklyLevelDto();
        BeanUtils.copyProperties(weekly, dto);
        return dto;
    }
}
