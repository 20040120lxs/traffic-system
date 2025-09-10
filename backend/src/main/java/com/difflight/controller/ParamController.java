package com.difflight.controller;

import com.difflight.dto.ParamSelectRequest;
import com.difflight.dto.ResultCarouselResponse;
import com.difflight.entity.ParameterEntry;
import com.difflight.entity.ParamOption;
import com.difflight.entity.ResultImage;
import com.difflight.repository.ParameterEntryRepository;
import com.difflight.repository.ParamOptionRepository;
import com.difflight.repository.ResultImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 参数控制器
 */
@RestController
@RequestMapping("/params")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class ParamController {

    @Autowired
    private ParamOptionRepository paramOptionRepository;

    @Autowired
    private ParameterEntryRepository parameterEntryRepository;

    @Autowired
    private ResultImageRepository resultImageRepository;

    @Value("${app.storage.results-dir}")
    private String resultsDir;

    /**
     * 获取所有参数选项，分组返回
     */
    @GetMapping("/options")
    public ResponseEntity<?> getParamOptions() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<ParamOption> allOptions = paramOptionRepository.findAllOrderByTypeAndSortOrder();
            
            // 按类型分组
            Map<String, List<Map<String, Object>>> groupedOptions = allOptions.stream()
                .collect(Collectors.groupingBy(
                    ParamOption::getType,
                    Collectors.mapping(option -> {
                        Map<String, Object> optionMap = new HashMap<>();
                        optionMap.put("value", option.getValue());
                        optionMap.put("displayName", option.getDisplayName() != null ? 
                                     option.getDisplayName() : option.getValue());
                        return optionMap;
                    }, Collectors.toList())
                ));

            response.put("success", true);
            response.put("data", groupedOptions);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取参数选项失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 获取有效的缺失模式和缺失率组合
     * 包含硬编码的业务规则
     */
    @GetMapping("/valid-missing")
    public ResponseEntity<?> getValidMissingOptions(
            @RequestParam String trafficFile,
            @RequestParam String roadnetFile, 
            @RequestParam Integer numIntersections) {
        
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, List<String>> validOptions = new HashMap<>();

            // 硬编码的业务规则
            if ("hangzhou".equals(trafficFile) && "roadnet_4_4".equals(roadnetFile) && numIntersections == 16) {
                validOptions.put("random_missing", Arrays.asList("10%", "30%", "50%"));
                validOptions.put("kriging_missing", Arrays.asList("6.25%", "12.5%", "18.75%"));
            } 
            else if ("jinan".equals(trafficFile) && "roadnet_3_4".equals(roadnetFile) && numIntersections == 16) {
                validOptions.put("random_missing", Arrays.asList("10%", "30%", "50%"));
                validOptions.put("kriging_missing", Arrays.asList("8.33%", "16.67%", "25%"));
            } 
            else {
                // 默认情况：返回数据库中所有的缺失模式和缺失率选项
                List<ParamOption> missingPatterns = paramOptionRepository.findByTypeOrderBySortOrderAsc("missing_pattern");
                List<ParamOption> missingRates = paramOptionRepository.findByTypeOrderBySortOrderAsc("missing_rate");
                
                for (ParamOption pattern : missingPatterns) {
                    List<String> rates = missingRates.stream()
                            .map(ParamOption::getValue)
                            .collect(Collectors.toList());
                    validOptions.put(pattern.getValue(), rates);
                }
            }

            response.put("success", true);
            response.put("data", validOptions);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取有效缺失选项失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 选择参数组合，返回结果轮播
     */
    @PostMapping("/select")
    public ResponseEntity<?> selectParams(@Validated @RequestBody ParamSelectRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 查找或创建参数条目
            Optional<ParameterEntry> entryOpt = parameterEntryRepository
                .findByTrafficFileAndRoadnetFileAndNumIntersectionsAndMissingPatternAndMissingRate(
                    request.getTrafficFile(),
                    request.getRoadnetFile(), 
                    request.getNumIntersections(),
                    request.getMissingPattern(),
                    request.getMissingRate()
                );

            ParameterEntry entry;
            if (entryOpt.isPresent()) {
                entry = entryOpt.get();
            } else {
                // 创建新的参数条目
                entry = new ParameterEntry();
                entry.setTrafficFile(request.getTrafficFile());
                entry.setRoadnetFile(request.getRoadnetFile());
                entry.setNumIntersections(request.getNumIntersections());
                entry.setMissingPattern(request.getMissingPattern());
                entry.setMissingRate(request.getMissingRate());
                entry = parameterEntryRepository.save(entry);
            }

            // 构建响应
            ResultCarouselResponse carouselResponse = new ResultCarouselResponse();
            
            // 设置选中参数表格
            Map<String, Object> selectedParams = new HashMap<>();
            selectedParams.put("trafficFile", request.getTrafficFile());
            selectedParams.put("roadnetFile", request.getRoadnetFile());
            selectedParams.put("numIntersections", request.getNumIntersections());
            selectedParams.put("missingPattern", request.getMissingPattern());
            selectedParams.put("missingRate", request.getMissingRate());
            carouselResponse.setSelectedParams(selectedParams);

            // 获取相关的结果图片
            List<ResultImage> images = resultImageRepository.findByParameterEntryId(entry.getId());
            List<ResultCarouselResponse.ImageItem> imageItems = images.stream()
                .map(img -> new ResultCarouselResponse.ImageItem(
                    img.getFilename(),
                    img.getTitle() != null ? img.getTitle() : img.getImageType(),
                    img.getImageType(),
                    "/results/public/image/" + img.getFilename()
                ))
                .collect(Collectors.toList());
            carouselResponse.setImages(imageItems);

            response.put("success", true);
            response.put("data", carouselResponse);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "选择参数失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 获取结果图片文件
     */
    @GetMapping("/results/public/image/{filename}")
    public ResponseEntity<Resource> getResultImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(resultsDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 管理员上传结果图片
     */
    @PostMapping("/admin/results/upload")
    public ResponseEntity<?> uploadResultImages(
            @RequestParam("parameterEntryId") Long parameterEntryId,
            @RequestParam(value = "fillImage", required = false) MultipartFile fillImage,
            @RequestParam(value = "noiseImage", required = false) MultipartFile noiseImage,
            @RequestParam(value = "denoiseImage", required = false) MultipartFile denoiseImage,
            @RequestParam(value = "customTitle", required = false) String customTitle) {
        
        Map<String, Object> response = new HashMap<>();

        try {
            // 确保参数条目存在
            Optional<ParameterEntry> entryOpt = parameterEntryRepository.findById(parameterEntryId);
            if (!entryOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "参数条目不存在");
                return ResponseEntity.badRequest().body(response);
            }

            // 确保结果目录存在
            Path resultsDirPath = Paths.get(resultsDir);
            if (!Files.exists(resultsDirPath)) {
                Files.createDirectories(resultsDirPath);
            }

            List<String> uploadedFiles = new ArrayList<>();

            // 上传填补结果图
            if (fillImage != null && !fillImage.isEmpty()) {
                String filename = saveImage(fillImage, "fill_" + System.currentTimeMillis());
                saveResultImage(parameterEntryId, "填补结果图", filename, customTitle);
                uploadedFiles.add(filename);
            }

            // 上传加噪过程图
            if (noiseImage != null && !noiseImage.isEmpty()) {
                String filename = saveImage(noiseImage, "noise_" + System.currentTimeMillis());
                saveResultImage(parameterEntryId, "加噪过程图", filename, customTitle);
                uploadedFiles.add(filename);
            }

            // 上传去噪过程图
            if (denoiseImage != null && !denoiseImage.isEmpty()) {
                String filename = saveImage(denoiseImage, "denoise_" + System.currentTimeMillis());
                saveResultImage(parameterEntryId, "去噪过程图", filename, customTitle);
                uploadedFiles.add(filename);
            }

            response.put("success", true);
            response.put("message", "图片上传成功");
            response.put("uploadedFiles", uploadedFiles);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "图片上传失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 保存图片文件
     */
    private String saveImage(MultipartFile file, String prefix) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String filename = prefix + extension;
        
        Path filePath = Paths.get(resultsDir).resolve(filename);
        Files.copy(file.getInputStream(), filePath);
        
        return filename;
    }

    /**
     * 保存结果图片记录
     */
    private void saveResultImage(Long parameterEntryId, String imageType, String filename, String customTitle) {
        ResultImage resultImage = new ResultImage();
        resultImage.setParameterEntryId(parameterEntryId);
        resultImage.setImageType(imageType);
        resultImage.setFilename(filename);
        resultImage.setFilepath(Paths.get(resultsDir, filename).toString());
        resultImage.setTitle(customTitle != null ? customTitle : imageType);
        
        resultImageRepository.save(resultImage);
    }
}