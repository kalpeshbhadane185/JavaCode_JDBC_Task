package com.logilite.CSV_Utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Level;

public class InsertRecrdsInDB
{
	public static void insertRecords()
	{
		Statement statement = null;
		try
		{
			File file = new File(Main.map.get("filepath"));
			String tableName = file.getName().replace(".csv", "").toLowerCase();
			if (!file.isFile())
			{
				throw new FileNotFoundException("Please Provide correct csvFilePath");
			}
			else
			{
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String readLine = reader.readLine();
				String[] header = readLine.split(",");

				DB_Connection.getDbConnection();
				if (DB_Connection.connection != null)
				{
					StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + tableName + " (");

					for (String column : header)
					{
						createTableQuery.append(column).append(" VARCHAR(255), ");
					}
					createTableQuery.setLength(createTableQuery.length() - 2);
					createTableQuery.append(")");

					statement = DB_Connection.connection.createStatement();
					DatabaseMetaData dbm = DB_Connection.connection.getMetaData();
					ResultSet resultSet = dbm.getTables(null, null, tableName, null);
					try
					{
						if (resultSet.next())
						{
							String deleteQuery = "DROP TABLE " + tableName;
							statement.executeUpdate(deleteQuery);
							CSV_Utility_Logger.logger.info(tableName + " Table Delete Successfull");
						}
					}
					catch (Exception e)
					{
						CSV_Utility_Logger.logger.log(Level.ERROR, "Exception :: ", e);
					}
					finally
					{
						if (resultSet != null)
						{
							resultSet.close();
						}
					}
					statement.executeUpdate(createTableQuery.toString());
					StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " (");
					for (String column : header)
					{
						insertQuery.append(column).append(", ");
					}
					insertQuery.setLength(insertQuery.length() - 2);
					insertQuery.append(") VALUES (");
					for (int i = 0; i < header.length; i++)
					{
						insertQuery.append("?, ");
					}
					insertQuery.setLength(insertQuery.length() - 2);
					insertQuery.append(")");

					PreparedStatement preparedStatement = DB_Connection.connection
							.prepareStatement(insertQuery.toString());

					String line;
					while ((line = reader.readLine()) != null)
					{
						String[] nextLine = line.split(",");

						for (int i = 0; i < nextLine.length; i++)
						{
							preparedStatement.setString(i + 1, nextLine[i]);
						}
						preparedStatement.executeUpdate();
					}

					CSV_Utility_Logger.logger.info(tableName + " Table created and data inserted successfully.");
				}
			}
		}
		catch (Exception e)
		{
			CSV_Utility_Logger.logger.log(Level.ERROR, "Exception :: ", e);
		}
		finally
		{
			try
			{
				if (DB_Connection.connection != null)
				{
					DB_Connection.connection.close();
				}
				if (statement != null)
				{
					statement.close();
				}
			}
			catch (SQLException e)
			{
				CSV_Utility_Logger.logger.log(Level.ERROR, "SQLException :: ", e);
			}
		}
	}
}
