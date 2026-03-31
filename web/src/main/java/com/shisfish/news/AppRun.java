package com.shisfish.news;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 启动类
 * @Version: 1.0.0
 */
@EnableScheduling
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableCaching
@ForestScan("com.shisfish.news.service.service.forest")
@PropertySources({
        @PropertySource(value = "classpath:config/${spring.profiles.active}/database-multi.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:config/${spring.profiles.active}/oauth.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:config/${spring.profiles.active}/swagger.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:config/${spring.profiles.active}/log.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:config/${spring.profiles.active}/xss.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:config/${spring.profiles.active}/news.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:config/${spring.profiles.active}/wechat.properties", ignoreResourceNotFound = true)
})
public class AppRun {
    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }

}
