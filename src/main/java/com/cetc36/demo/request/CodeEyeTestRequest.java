package com.cetc36.demo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.commons.nullanalysis.NotNull;


/**
 * Mossad Test 请求
 *
 * @author liuyang
 */
@Getter
@Setter
@ToString
public class CodeEyeTestRequest {

    @ApiModelProperty(value = "任务流水号")
    @NotNull
    private Long id;
}