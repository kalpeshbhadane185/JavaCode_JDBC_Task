package com.logilite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;
import com.logilite.stringconst.Constants;
import com.logilite.stringconst.SQLQueries;

public class UserDAO
{
	Connection			connection	= null;
	PreparedStatement	pStatement	= null;

	public boolean registerUser(User user, int parent_id)
	{
		String query = SQLQueries.INSERT_USER_DETAILS;
		connection = Database_Connectivity.createDBConnection();
		try
		{
			pStatement = connection.prepareStatement(query);
			pStatement.setString(1, user.getUsername());
			pStatement.setString(2, user.getPassword());
			pStatement.setString(3, user.getUser_type());
			pStatement.setLong(4, user.getAccount_no());
			pStatement.setString(5, user.getEmail());
			pStatement.setLong(6, user.getMob_no());
			pStatement.setString(7, user.getGender());
			pStatement.setInt(8, parent_id);

			int executeUpdate = pStatement.executeUpdate();

			if (executeUpdate != 0)
			{
				return true;
			}
			return false;
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "SQLException :: ", e);
			return false;
		}
		finally
		{
			closeConnection();
		}
	}

	public List<User> fetchUserData(User user) throws SQLException
	{
		List<User> list = new ArrayList<>();
		String query = "select * from bank_user where parent_id=? and  user_type='Customer'";
		try
		{
			connection = Database_Connectivity.createDBConnection();
			pStatement = connection.prepareStatement(query);
			pStatement.setInt(1, user.getUser_id());

			ResultSet rs = pStatement.executeQuery();
			while (rs.next())
			{
				User userData = new User();
				userData.setUser_id(rs.getInt(Constants.USER_ID));
				userData.setUsername(rs.getString(Constants.USERNAME));
				userData.setAccount_no(rs.getLong(Constants.ACCOUNT_NO));
				userData.setMob_no(rs.getLong(Constants.MOBILE_NO));
				userData.setGender(rs.getString(Constants.GENDER));
				userData.setEmail(rs.getString(Constants.EMAIL));
				list.add(userData);
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

	public User authenticated(String username, String password)
	{
		try
		{
			User user = new User();
			String query = SQLQueries.USER_AUTHENTICATE;
			connection = Database_Connectivity.createDBConnection();
			pStatement = connection.prepareStatement(query);
			pStatement.setString(1, username);
			pStatement.setString(2, password);
			ResultSet rs = pStatement.executeQuery();
			while (rs.next())
			{
				user.setUser_id(rs.getInt(Constants.USER_ID));
				user.setUsername(rs.getString(Constants.USERNAME));
				user.setUser_type(rs.getString(Constants.USER_TYPE));
				user.setAccount_no(rs.getLong(Constants.ACCOUNT_NO));
				user.setMob_no(rs.getLong(Constants.MOBILE_NO));
				user.setEmail(rs.getString(Constants.EMAIL));
				user.setGender(rs.getString(Constants.GENDER));
				user.setParent_id(rs.getInt(Constants.PARENT_ID));
				return user;
			}
			rs.close();
			return null;
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

	public boolean deleteUser(String userId)
	{
		double accountBalance = 0;
		String selectQuery = SQLQueries.SELECT_ACCOUNT_BALANCE_BY_USER_ID;
		try
		{
			connection = Database_Connectivity.createDBConnection();
			pStatement = connection.prepareStatement(selectQuery);
			pStatement.setInt(1, Integer.parseInt(userId));
			ResultSet resultSet = pStatement.executeQuery();
			if (resultSet.next())
			{
				accountBalance = resultSet.getDouble(Constants.ACCOUNT_BALANCE);
			}
			resultSet.close();

			if (accountBalance < 600.0)
			{
				String deleteQuery = SQLQueries.DELETE_USER_BY_USER_ID;
				PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
				deleteStatement.setInt(1, Integer.parseInt(userId));
				deleteStatement.executeUpdate();
				MyLogger.logger.info(Constants.CUSTOMER_DELETED_SUCCESS);
				deleteStatement.close();
				pStatement.close();
				return true;
			}
			else
			{
				return false;
			}
		} // type code
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "SQLException :: Please Check Query", e);
		}
		finally
		{
			closeConnection();
		}
		return false;
	}

	public void closeConnection()
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