package com.maybe.sys.common.dto;

import com.maybe.sys.model.SysDept;
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
public class DeptLevelDto extends SysDept {

    private List<DeptLevelDto> children = new ArrayList<DeptLevelDto>();

    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
