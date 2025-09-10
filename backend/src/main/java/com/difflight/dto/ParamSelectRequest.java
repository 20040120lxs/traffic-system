package com.difflight.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 参数选择请求DTO
 */
@Data
public class ParamSelectRequest {
    
    @NotBlank(message = "交通文件不能为空")
    private String trafficFile;
    
    @NotBlank(message = "路网文件不能为空")
    private String roadnetFile;
    
    @NotNull(message = "交叉路口数不能为空")
    private Integer numIntersections;
    
    @NotBlank(message = "缺失模式不能为空")
    private String missingPattern;
    
    @NotBlank(message = "缺失率不能为空")
    private String missingRate;
}