package com.logilite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

public class TransactionStateDAO
{

	public List<Transaction_Activity> fetchUserDataFromDatabase(User user, String buttonName, String fromDate,
			String toDate)
	{
		List<Transaction_Activity> list = new ArrayList<>();
		try
		{
			Connection connection = Database_Connectivity.createDBConnection();
			String query = null;
			if (buttonName.equalsIgnoreCase("statement"))
			{
				query = "select * from tr_activity where user_id = ? order by tr_date desc";
			}
			else if (buttonName.equalsIgnoreCase("1"))
			{
				query = "select * from tr_activity where user_id = ? and DATE_TRUNC('month', tr_date) = DATE_TRUNC('month', CURRENT_DATE) order by tr_date desc";
			}
			else if (buttonName.equalsIgnoreCase("2"))
			{
				query = "select * from tr_activity where user_id = ? and tr_date >= DATE_TRUNC('quarter', CURRENT_DATE) AND tr_date < DATE_TRUNC('quarter', CURRENT_DATE) "
						+ "+ INTERVAL '3 months' order by tr_date desc";
			}
			else if (buttonName.equalsIgnoreCase("3"))
			{
				query = "select * from tr_activity where user_id = ? and tr_date between ? AND ? order by tr_date desc" ;
			}

			PreparedStatement prepareStatement = connection.prepareStatement(query);
			prepareStatement.setInt(1, user.getUser_id());
			if (buttonName.equalsIgnoreCase("3"))
			{
				try
				{
				 	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		            Date fromDateObj = dateFormat.parse(fromDate);
		            Date toDateObj = dateFormat.parse(toDate);
		            Calendar c = Calendar.getInstance(); 
		            c.setTime(toDateObj); 
		            c.add(Calendar.DATE, 1);
		            toDateObj = c.getTime();
 
		            Timestamp fromDateTimestamp = new Timestamp(fromDateObj.getTime());
		            Timestamp toDateTimestamp = new Timestamp(toDateObj.getTime());

		            prepareStatement.setTimestamp(2, fromDateTimestamp);
		            prepareStatement.setTimestamp(3, toDateTimestamp);
				}
				catch (ParseException e)
				{
					MyLogger.logger.log(Level.ERROR, "ParseException :: ", e);
				}
			}

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next())
			{
				Transaction_Activity activity = new Transaction_Activity();
				activity.setTransaction_date(rs.getTimestamp("tr_date"));
				activity.setAccountBalance(rs.getDouble("account_balance"));
				activity.setAmmount(rs.getDouble("amount"));
				activity.setTransaction_type(rs.getString("tr_type"));

				list.add(activity);
			}
		}
		// create new object with above code and return the list
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}
		return list;
	}
	
	
	public List<Transaction_Activity> fetchUserTransactionData(String userId)
	{
		try
		{
			List<Transaction_Activity> list = new ArrayList<>();
			String query = "select * from tr_activity where user_id=? order by tr_date desc;";
			Connection connection = Database_Connectivity.createDBConnection();
			try (PreparedStatement prepareStatement = connection.prepareStatement(query))
			{
				prepareStatement.setInt(1, Integer.parseInt(userId));
				ResultSet rs = prepareStatement.executeQuery();
				while (rs.next())
				{
					Transaction_Activity activity = new Transaction_Activity();
					activity.setTransaction_date(rs.getTimestamp("tr_date"));
					activity.setAmmount(rs.getDouble("amount"));
					activity.setTransaction_type(rs.getString("tr_type"));
					list.add(activity);
				}
			}
			return list;
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "SQLException :: ", e);
			return null;
		}
	}

	public void insertTransactionActivity(User user, String transactionType, double amount)
	{
		String query = "insert into tr_activity (tr_Date, tr_type, account_balance, amount, user_id) values(?,?,?,?,?);";
		try
		{
			Connection createDBConnection = Database_Connectivity.createDBConnection();
			PreparedStatement pStatement = createDBConnection.prepareStatement(query);
			pStatement.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
			pStatement.setString(2, transactionType);
			double accountBalance = 0.0;
			if (transactionType.equalsIgnoreCase("Credit"))
			{
				accountBalance = accountBalance + amount;
			}
			else
			{
				if (accountBalance >= 500 && amount <= accountBalance)
				{
					accountBalance = accountBalance - amount;
				}
			}
			pStatement.setDouble(3, accountBalance);
			pStatement.setDouble(4, amount);
			pStatement.setInt(5, user.getUser_id());
			pStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}
	}
}
