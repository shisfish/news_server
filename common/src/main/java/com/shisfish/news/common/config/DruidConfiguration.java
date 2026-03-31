package com.shisfish.news.common.config;

import com.shisfish.news.common.constant.DataSourceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfiguration {

	@Bean(name = "master")
	@ConfigurationProperties(prefix = "spring.datasource.druid.master")
	public DataSource master() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "slave")
	@ConfigurationProperties(prefix = "spring.datasource.druid.slave")
	public DataSource slave() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	public DynamicDataSource dynamicDataSource(@Qualifier("master") DataSource masterDataSource, @Qualifier("slave") DataSource slaveDataSource) {
		Map<Object, Object> dataSources = new HashMap<>();
		dataSources.put(DataSourceType.MASTER.name(), masterDataSource);
		dataSources.put(DataSourceType.SLAVE.name(), slaveDataSource);
		return new DynamicDataSource(masterDataSource, dataSources);
	}

}
