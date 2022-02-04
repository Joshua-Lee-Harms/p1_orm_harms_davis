package com.revature.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	
	private static ConnectionFactory connectionFactory;
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
		
		// To establish a new connection if one doesn't exist, otherwise return to current connection
		if (connection == null) {
			//establish a new connection. Credentials: url, username, password
			try {
				String endpoint = properties.getProperty("endpoint");
				String url = "jdbc:postgresql://" + endpoint + "/postgres";
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
		connectionFactory = null;
		System.out.println("Connection has been closed");
	}
}
