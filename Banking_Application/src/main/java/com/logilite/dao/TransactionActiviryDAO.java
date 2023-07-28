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
import com.logilite.stringconst.Constants;
import com.logilite.stringconst.SQLQueries;

public class TransactionActiviryDAO
{
	public String insertTransactionActivity(User user, Transaction_Activity activity)
	{
		Connection connection = null;
		PreparedStatement pStatement = null;
		try
		{
			String query = SQLQueries.INSERT_TRANSACTION_ACTIVITY;
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

			connection = Database_Connectivity.createDBConnection();
			pStatement = connection.prepareStatement(query);
			pStatement.setTimestamp(1, currentTimestamp);
			pStatement.setString(2, activity.getTransaction_type());
			double accountBalance = activity.getAccountBalance();
			boolean queryExecute = false;
			if (activity.getTransaction_type().equalsIgnoreCase(Constants.CREDIT))
			{
				if (activity.getAmmount() <= 200000)
				{
					accountBalance = accountBalance + activity.getAmmount();
					queryExecute = true;
				}
				else
				{
					return Constants.CREDIT_LIMIT + activity.getAmmount();
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
						return Constants.WITHDRAW_SUCCESS + activity.getAmmount() + Constants.WITHDRAW_LIMIT + temp;
					}
				}
				else
				{
					temp = temp - 500;
					return Constants.INSUFFICIENT_FUNDS + accountBalance + Constants.DEBIT_LIMIT + temp;
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
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}
		finally
		{
			try
			{
				if (connection != null)
				{
					connection.close();
				}
				if (pStatement != null)
				{
					pStatement.close();
				}
			}
			catch (SQLException e)
			{
				MyLogger.logger.log(Level.ERROR, "SQLException :: ", e);
			}
		}
		return activity.getTransaction_type() + " Transaction is success";
	}
}
