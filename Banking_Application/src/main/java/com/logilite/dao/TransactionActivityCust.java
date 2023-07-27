package com.logilite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Level;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

public class TransactionActivityCust
{

	public String insertTransactionActivity(User user, Transaction_Activity activity)
	{
		try
		{
			Connection createDBConnection = Database_Connectivity.createDBConnection();
			String query = "insert into tr_activity (tr_Date, tr_type, account_balance, amount, user_id) values(?,?,?,?,?);";
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
			try (PreparedStatement pStatement = createDBConnection.prepareStatement(query))
			{
				pStatement.setTimestamp(1, currentTimestamp);
				pStatement.setString(2, activity.getTransaction_type());
				double accountBalance = activity.getAccountBalance();
				boolean queryExecute = false;
				if (activity.getTransaction_type().equalsIgnoreCase("Credit"))
				{
					if (activity.getAmmount() <= 200000)
					{
						accountBalance = accountBalance + activity.getAmmount();
						queryExecute = true;
					}
					else
					{
						return "you can credit the maximum 200000 amount in your account"
								+ " and your credit amount is " + activity.getAmmount();
					}
				}
				else
				{
					double temp = accountBalance;
					if (accountBalance >= 500 && activity.getAmmount() <= accountBalance)
					{
						accountBalance = accountBalance - activity.getAmmount();
						if (accountBalance >= 500)
						{
							queryExecute = true;
						}
						else
						{
							temp = temp - 500;
							return "your withdraw amount is " + activity.getAmmount()
								+ " you can withdraw the max amount from your account " + temp;
						}
					}
					else
					{
						temp = temp - 500;
						return "you can't withdraw amount because your debit amount is more rather than your account balance...balance is "
										+ accountBalance + "/n you can debit only " + temp;
					}
				}
				if (queryExecute)
				{
					pStatement.setDouble(3, accountBalance);
					pStatement.setDouble(4, activity.getAmmount());
					pStatement.setInt(5, user.getUser_id());
					pStatement.executeUpdate();
				}
			}
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}
		return "Transaction is success";
	}

	public boolean deleteUserDataFromDatabase(String userId)
	{
		try
		{
			double accountBalance = 0;
			String selectQuery = "SELECT * FROM bank_user b JOIN tr_activity t ON t.user_id = b.user_id "
					+ "WHERE t.user_id = ? ORDER BY tr_date DESC;";
			Connection connection = Database_Connectivity.createDBConnection();
			try (PreparedStatement prepareStatement = connection.prepareStatement(selectQuery))
			{
				prepareStatement.setInt(1, Integer.parseInt(userId));
				try (ResultSet resultSet = prepareStatement.executeQuery())
				{
					if (resultSet.next())
					{
						accountBalance = resultSet.getDouble("account_balance");
					}
				}
			}
			if (accountBalance < 600.0)
			{
				String deleteQuery = "DELETE FROM bank_user WHERE user_id = ?";
				try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery))
				{
					deleteStatement.setInt(1, Integer.parseInt(userId));
					deleteStatement.executeUpdate();
					MyLogger.logger.info("Customer deleted successfully");
					return true;
				}
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}
		return false;
	}
}
