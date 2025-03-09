package com.sky.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
public class URLConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径
        registry.addResourceHandler("/pic/**").addResourceLocations("file:E:\\takeout\\front end\\nginx-1.20.2\\html\\sky\\pic");
        WebMvcConfigurer.super.addResourceHandlers(registry);

    }
//    @PostConstruct
//    public void checkResourceMapping() {
//        System.out.println("静态资源映射 /pic/** -> E:/takeout/pic/ 已加载");
//    }
}