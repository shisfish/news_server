package com.shisfish.news.framework.security.config;


import com.shisfish.news.framework.security.config.bean.LoginProperties;
import com.shisfish.news.framework.security.config.bean.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 配置文件转换Pojo类的统一配置类
 * @Version: 1.0.0
 */
@Configuration
public class BeanConfig {
    /**
     * 登录认证相关配置
     */
    @Bean
    @ConfigurationProperties(prefix = "login", ignoreUnknownFields = true)
    public LoginProperties loginProperties() {
        return new LoginProperties();
    }

    /**
     * jwt相关信息配置
     */
    @Bean
    @ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = true)
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

}
