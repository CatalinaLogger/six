package com.maybe.sys.common.param;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author jin
 * @description:
 * @date 2018/5/5
 */
@Data
public class PageParam {
    // example = "1"   https://blog.csdn.net/lilyssh/article/details/82944507
    @ApiParam(value = "当前页码", example = "1")
    @Min(value = 1, message = "当前页码不合法")
    private Integer page = 1;
    @ApiParam(value = "每页数量", example = "10")
    @Min(value = 1, message = "每页数量不合法")
    private Integer size = 10;
    @ApiParam(hidden = true)
    private Integer offset;

    public Integer getOffset() {
        return (page - 1) * size;
    }
}
