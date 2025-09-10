package com.difflight.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 结果轮播响应DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultCarouselResponse {
    
    /**
     * 选中的参数表格
     */
    private Map<String, Object> selectedParams;
    
    /**
     * 可用的图片条目
     */
    private List<ImageItem> images;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageItem {
        private String filename;     // 图片文件名
        private String title;        // 图片标题
        private String imageType;    // 图片类型
        private String url;          // 图片访问URL
    }
}