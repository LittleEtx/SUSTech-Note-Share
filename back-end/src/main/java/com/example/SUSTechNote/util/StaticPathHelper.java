package com.example.SUSTechNote.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

@Configuration
@SuppressWarnings("ResultOfMethodCallIgnored")
public class StaticPathHelper {

    private final Logger logger = LoggerFactory.getLogger(StaticPathHelper.class);

    @Value("${spring.web.resources.static-locations}")
    private String[] staticPaths;
    @Bean
    public String getStaticPath() {
         String path = Arrays.stream(staticPaths)
                .filter(s -> s.contains("file:"))
                .map(s -> s.replace("file:", ""))
                .findAny().orElseThrow(() -> new RuntimeException("static path not found"));
        //检查是相对路径还是绝对路径
        File basePath = new File(path);
        if (!basePath.isAbsolute()) {
            //相对路径，使用项目根路径
            basePath = new File(System.getProperty("user.dir"), path);
        }
        if (!basePath.exists()) {
            //如果目录不存在则创建
            logger.warn("static path not exist, will create file at: " + basePath.getAbsolutePath());
            basePath.mkdirs();
        }
        return basePath.getAbsolutePath();
    }
}
