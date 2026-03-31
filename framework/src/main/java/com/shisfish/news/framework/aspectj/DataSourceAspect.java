package com.shisfish.news.framework.aspectj;

import com.shisfish.news.common.config.DataSource;
import com.shisfish.news.common.config.DynamicDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    @Pointcut("@annotation(com.shisfish.news.common.config.DataSource)" + "|| @within(com.shisfish.news.common.config.DataSource)")
    private void dataSourcePointCut() {}

    @Before("dataSourcePointCut()")
    public void before(JoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (Objects.nonNull(dataSource)) {
            DynamicDataSource.setDataSource(dataSource.value().name());
        } else {
            dataSource = method.getDeclaringClass().getAnnotation(DataSource.class);
            if (Objects.nonNull(dataSource)) {
                DynamicDataSource.setDataSource(dataSource.value().name());
            } else {
                throw new IllegalArgumentException("未指定数据源");
            }
        }
    }

    @After("dataSourcePointCut()")
    public void restoreDataSource(JoinPoint point) {
        // 恢复默认数据源
        DynamicDataSource.clearDataSource();
    }

}
