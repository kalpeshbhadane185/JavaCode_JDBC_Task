package com.logilite.dataBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Level;

import com.logilite.logger.MyLogger;

public class Database_Connectivity
{
	public static Connection connection;

	public static Connection createDBConnection()
	{

		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(
					System.getProperty("user.home") + System.getProperty("file.separator") + "config.properties"));

		}
		catch (IOException e)
		{
			MyLogger.logger.log(Level.ERROR, "IOException :: ", e);
		}
		String url = properties.getProperty("url");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		try
		{
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, username, password);
			return connection;
		}
		catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
