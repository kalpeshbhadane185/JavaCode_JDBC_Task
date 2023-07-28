package com.logilite.dataBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Level;

import com.logilite.logger.MyLogger;
import com.logilite.stringconst.Constants;

public class Database_Connectivity
{
	public static Connection createDBConnection()
	{
		Connection connection = null;
		Properties properties = new Properties();
		try
		{
			 properties.load(new FileInputStream(
			 System.getProperty("user.dir") +
			 System.getProperty("file.separator") + "config.properties"));
//			properties.load(new FileInputStream("/home/kalpesh/git/repository/Banking_Application/config.properties"));
		}
		catch (IOException e)
		{
			MyLogger.logger.log(Level.ERROR, "IOException :: ", e);
		}
		String url = "jdbc:postgresql://localhost:" + properties.getProperty("db_port") + "/postgres";
		String username = properties.getProperty("db_username");
		String password = properties.getProperty("db_password");
		try
		{
			Class.forName(Constants.DB_DRIVER);
			connection = DriverManager.getConnection(url, username, password);
			return connection;
		}
		catch (ClassNotFoundException | SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Please Check DB Connection\n ClassNotFoundException | SQLException :: ",
					e);
			return null;
		}
	}
}
