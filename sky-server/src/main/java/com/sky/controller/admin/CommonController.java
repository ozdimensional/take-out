package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/*
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    // 通用接口
    /*
     * 上传文件
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件{}",file);
        if (file.isEmpty()) {
            return Result.error("上传失败，请选择文件");
        }

        try {
            // 文件存储路径
            String uploadDir = "E:\\takeout\\pic";
            // 创建目录（如果不存在）
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // 构建文件路径
            String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];
            String filePath = uploadDir + "/" + fileName;
            // 保存文件到本地
            file.transferTo(new File(filePath));
            log.info("文件上传成功: {}", filePath);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            return Result.error("上传失败");
        }
    }
}
