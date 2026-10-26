package com.jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnPool {
	public static void main(String[] args) {
		HikariDataSource hikariDataSource = null;
		// HikariCP is a Connection Pooling framework. It is a third party framework, not integrated in JDBC
		// But Spring Boot uses it as default CP.
		// HikariCP needs slf4j jar to work.
		
		/* HikariCP maintains a pool of reusable database connections, 
		 * so the application borrows an available connection instead of creating a new one for every query.
		 * When the query finishes, the connection is returned to the pool, 
		 * where it stays ready for the next request, making database access much faster.
		 */
		
		try {
			FileInputStream fis = new FileInputStream("config.properties");
			Properties properties = new Properties();
			
			properties.load(fis);
			// load the DB utils from config.properties
			
			HikariConfig hikariConfig = new HikariConfig();
			// We need to provide the DB utils to the HikariConfig
			
			hikariConfig.setJdbcUrl((String) properties.get("mysql.url"));
			hikariConfig.setUsername((String) properties.get("mysql.user"));
			hikariConfig.setPassword((String) properties.get("mysql.password"));
			// The url, username, password are provided from the Properties obj to the HikariConfig obj.
			
			hikariConfig.setMaximumPoolSize(30);
			// We can actual set the Max no. of connections in the Pool. By default it is set to 10.
			// After 30 connections are created, then it will further not create any.
			
			hikariConfig.setMinimumIdle(15);
			// We can also set the Min no. of idle connections that are ready to use but not used. 
			// By default it is set to 10.
			
			hikariDataSource = new HikariDataSource(hikariConfig);
			
			Connection connection = hikariDataSource.getConnection();
			// We now get the connection through this hikariDataSource which is the CP.
			
			System.out.println(connection);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			hikariDataSource.close();
			// Now when we close the connection.close(), the connection doesn't get close but it gets returned to the Pool.
			// the HikariDataSource contains the actually connection pool, so we close it to close all the connections in the pool.
			
		}
	}
}
