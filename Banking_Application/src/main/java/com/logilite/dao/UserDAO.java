package com.logilite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

public class UserDAO
{

	public boolean registerUser(User user)
	{
		String query = "INSERT INTO bank_user (username, password, user_type, account_no, email, mobile_no, gender, parent_id) VALUES(?,?,?,?,?,?,?,?);";
		Connection cn = Database_Connectivity.createDBConnection();
		try
		{
			PreparedStatement pStatement = cn.prepareStatement(query);
			pStatement.setString(1, user.getUsername());
			pStatement.setString(2, user.getPassword());
			pStatement.setString(3, user.getUser_type());
			pStatement.setLong(4, user.getAccount_no());
			pStatement.setString(5, user.getEmail());
			pStatement.setLong(6, user.getMob_no());
			pStatement.setString(7, user.getGender());
			pStatement.setInt(8, user.getUser_id());

			int executeUpdate = pStatement.executeUpdate();

			return executeUpdate != 0;
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			return false;
		}
	}

	public List<User> fetchUserData(User user) throws SQLException
	{
		List<User> list = new ArrayList<>();
		String query = "select * from bank_user where parent_id=" + user.getUser_id() + ";";
		Connection connection = Database_Connectivity.createDBConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		ResultSet rs = prepareStatement.executeQuery();
		while (rs.next())
		{
			User userData = new User();
			userData.setUser_id(rs.getInt("user_id"));
			userData.setUsername(rs.getString("username"));
			userData.setAccount_no(rs.getLong("account_no"));
			userData.setMob_no(rs.getLong("mobile_no"));
			userData.setGender(rs.getString("gender"));
			userData.setEmail(rs.getString("email"));
			list.add(userData);
		}
		return list;
	}

	public User authenticated(String username, String password)
	{
		try
		{
			User user = new User();
			Connection createDBConnection = Database_Connectivity.createDBConnection();
			if (createDBConnection != null)
			{
				String query = "select * from bank_user where username = ? AND password = ?";
				PreparedStatement preparedStatement = createDBConnection.prepareStatement(query);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next())
				{
					user.setUser_id(rs.getInt("user_id"));
					user.setUsername(rs.getString("username"));
					user.setUser_type(rs.getString("user_type"));
					user.setAccount_no(rs.getLong("account_no"));
					user.setMob_no(rs.getLong("mobile_no"));
					user.setEmail(rs.getString("email"));
					user.setGender(rs.getString("gender"));
					user.setParent_id(rs.getInt("parent_id"));
					return user;
				}
			}
			return null;
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			return null;
		}
	}
}
