package com.revature.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	
	private static Properties properties = new Properties();
	private static Connection connection = null;
	
	private ConnectionFactory(){}
	
	public static Connection getConnection(){
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			properties.load(loader.getResourceAsStream("connection.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (connection == null) {
		
			try {
				String url = properties.getProperty("url");
				String username = properties.getProperty("username");
				String password = properties.getProperty("password");
				
				connection = DriverManager.getConnection(url, username, password);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return connection;
	}
	
	public static void closeConnection() throws SQLException {
		connection.close();
		System.out.println("Connection has been closed");
	}
}
