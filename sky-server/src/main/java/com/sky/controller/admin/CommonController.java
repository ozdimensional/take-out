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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            String uploadDir = "E:\\takeout\\front end\\nginx-1.20.2/html/sky/pic/";
            String uploadDir2 = "E:\\takeout\\wxapp\\mp-weixin\\pages\\index\\img\\pic";
            String uploadDir3 = "E:\\takeout\\wxapp\\mp-weixin\\pages\\order\\img\\pic";
            String uploadDir4 = "E:\\takeout\\wxapp\\mp-weixin\\pages\\details\\img\\pic";
            String uploadDir5 = "E:\\takeout\\wxapp\\mp-weixin\\pages\\historyOrder\\img\\pic";
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
            String targetFilePath = uploadDir2 + "/" + fileName;
            Path source = Paths.get(filePath);
            Path target = Paths.get(targetFilePath);
            // 复制文件到指定目录
            Files.copy(source, target);
            targetFilePath = uploadDir3 + "/" + fileName;
            target = Paths.get(targetFilePath);
            Files.copy(source, target);
            targetFilePath = uploadDir4 + "/" + fileName;
            target = Paths.get(targetFilePath);
            Files.copy(source, target);
            targetFilePath = uploadDir5 + "/" + fileName;
            target = Paths.get(targetFilePath);
            Files.copy(source, target);
            log.info("文件上传成功: {}", filePath);
            filePath = "/pic/" + fileName;
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage());
            return Result.error("上传失败");
        }
    }
}
