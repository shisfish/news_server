package com.shisfish.news.common.config;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;

public class DecryptDruidDataSource extends DruidDataSource {
	private static final long serialVersionUID = 1L;

	public void setUsername(String username) {
		try {
			username = ConfigTools.decrypt(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setUsername(username);
	}

	public void setPassword(String password) {
		try {
			password = ConfigTools.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setPassword(password);
	}
}
