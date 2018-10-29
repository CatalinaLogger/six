package com.maybe.global.common.dto;

import com.maybe.global.model.GlobalConfData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jin
 * @description:
 * @date 2018/4/30
 */
@Data
public class ConfLevelDto extends GlobalConfData {

    private List<ConfLevelDto> children = new ArrayList<>();

    public static ConfLevelDto adapt(GlobalConfData globalConfData) {
        ConfLevelDto dto = new ConfLevelDto();
        BeanUtils.copyProperties(globalConfData, dto);
        return dto;
    }
}
