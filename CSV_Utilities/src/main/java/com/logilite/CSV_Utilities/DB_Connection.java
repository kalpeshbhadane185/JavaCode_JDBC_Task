package com.logilite.CSV_Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Level;

public class DB_Connection
{
	static Connection connection;

	public static void getDbConnection()
	{
		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(
					System.getProperty("user.home") + System.getProperty("file.separator") + "config.properties"));

		}
		catch (IOException e)
		{
			CSV_Utility_Logger.logger.log(Level.ERROR, "IOException :: ", e);
		}
		String url = properties.getProperty("url");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		try
		{
			connection = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException e)
		{
			CSV_Utility_Logger.logger.log(Level.ERROR, "SQLException :: ", e);
		}
	}
}
