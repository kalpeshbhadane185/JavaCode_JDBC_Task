package com.logilite.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database_Connectivity
{

	public static Connection		connection = null;
	public static String	url			= "jdbc:postgresql://localhost:5432/postgres";
	public static String	username	= "postgres";
	public static String	password	= "root";

	public static Connection createDBConnection()
	{
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
