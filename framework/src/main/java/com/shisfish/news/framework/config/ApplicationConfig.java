package com.shisfish.news.framework.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 程序注解配置，表示通过aop框架暴露该代理对象,AopContext能够访问
 * @Version: 1.0.0
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
public class ApplicationConfig {
    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }
}
