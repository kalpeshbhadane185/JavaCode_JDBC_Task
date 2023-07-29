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
import com.logilite.stringconst.Constants;
import com.logilite.stringconst.SQLQueries;

public class Tr_StatementDAO
{
	private Connection			connection	= null;
	private PreparedStatement	pStatement	= null;

	public List<Transaction_Activity> fetchUserTransactionData(User user, String hanlderName, String fromDate,
			String toDate)
	{
		List<Transaction_Activity> list = new ArrayList<>();
		try
		{
			String query = null;
			if (hanlderName.equalsIgnoreCase(Constants.CURRENTMONTH))
			{
				query = SQLQueries.TR_ACTIVITY_BY_MONTH;
			}
			else if (hanlderName.equalsIgnoreCase(Constants.CURRENTQUARTER))
			{
				query = SQLQueries.TR_ACTIVITY_BY_QUARTER;
			}
			else if (hanlderName.equalsIgnoreCase(Constants.CUSTOMRANGE))
			{
				query = SQLQueries.TR_ACTIVITY_BY_DATE_RANGE;
			}
			connection = Database_Connectivity.createDBConnection();
			pStatement = connection.prepareStatement(query);
			pStatement.setInt(1, user.getUser_id());
			if (hanlderName.equalsIgnoreCase(Constants.CUSTOMRANGE))
			{
				try
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date from = dateFormat.parse(fromDate);
					Date to = dateFormat.parse(toDate);
					Calendar c = Calendar.getInstance();
					c.setTime(to);
					c.add(Calendar.DATE, 1);
					to = c.getTime();

					Timestamp fromDateTimestamp = new Timestamp(from.getTime());
					Timestamp toDateTimestamp = new Timestamp(to.getTime());

					pStatement.setTimestamp(2, fromDateTimestamp);
					pStatement.setTimestamp(3, toDateTimestamp);
				}
				catch (ParseException e)
				{
					MyLogger.logger.log(Level.ERROR, "ParseException :: ", e);
				}
			}

			ResultSet rs = pStatement.executeQuery();
			while (rs.next())
			{
				Transaction_Activity activity = new Transaction_Activity();
				activity.setTransaction_date(rs.getTimestamp(Constants.TR_DATE));
				activity.setAccountBalance(rs.getDouble(Constants.ACCOUNT_BALANCE));
				activity.setAmmount(rs.getDouble(Constants.AMOUNT));
				activity.setTransaction_type(rs.getString(Constants.TR_TYPE));

				list.add(activity);
			}
			rs.close();
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			return null;
		}
		finally
		{
			closeConnection();
		}
		return list;
	}

	public List<Transaction_Activity> getUserTrData(String userId)
	{
		try
		{
			List<Transaction_Activity> list = new ArrayList<>();
			String query = SQLQueries.TR_ACTIVITY_BY_USER_ID;
			connection = Database_Connectivity.createDBConnection();
			pStatement = connection.prepareStatement(query);
			pStatement.setInt(1, Integer.parseInt(userId));
			ResultSet rs = pStatement.executeQuery();
			while (rs.next())
			{
				Transaction_Activity activity = new Transaction_Activity();
				activity.setTransaction_date(rs.getTimestamp(Constants.TR_DATE));
				activity.setAmmount(rs.getDouble(Constants.AMOUNT));
				activity.setTransaction_type(rs.getString(Constants.TR_TYPE));
				list.add(activity);
			}
			rs.close();
			return list;
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "SQLException :: ", e);
			return null;
		}
		finally
		{
			closeConnection();
		}
	}

	private void closeConnection()
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

}
