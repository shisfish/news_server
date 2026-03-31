package com.shisfish.news.common.config;

import lombok.Data;

import javax.sql.DataSource;

@Data
public class BaseDataSourceProperties {

	/**
	 * Name of the datasource.
	 */
	private String name = "testdb";

	/**
	 * Fully qualified name of the connection pool implementation to use. By default, it
	 * is auto-detected from the classpath.
	 */
	private Class<? extends DataSource> type;

	/**
	 * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
	 */
	private String driverClassName;

	/**
	 * JDBC url of the database.
	 */
	private String url;

	/**
	 * Login user of the database.
	 */
	private String username;

	/**
	 * Login password of the database.
	 */
	private String password;

	/**
	 * JNDI location of the datasource. Class, url, username & password are ignored when
	 * set.
	 */
	private String jndiName;

	/**
	 * Populate the database using 'data.sql'.
	 */
	private boolean initialize = true;

	/**
	 * Platform to use in the schema resource (schema-${platform}.sql).
	 */
	private String platform = "all";

	/**
	 * Schema (DDL) script resource reference.
	 */
	private String schema;

	/**
	 * User of the database to execute DDL scripts (if different).
	 */
	private String schemaUsername;

	/**
	 * Password of the database to execute DDL scripts (if different).
	 */
	private String schemaPassword;

	/**
	 * Data (DML) script resource reference.
	 */
	private String data;

	/**
	 * User of the database to execute DML scripts.
	 */
	private String dataUsername;

	/**
	 * Password of the database to execute DML scripts.
	 */
	private String dataPassword;

	/**
	 * Do not stop if an error occurs while initializing the database.
	 */
	private boolean continueOnError = false;

	/**
	 * Statement separator in SQL initialization scripts.
	 */
	private String separator = ";";

	private int initialSize = 5;

	private int minIdle = 5;

	private int maxActive = 100;

	private int maxWait = 60000;
}