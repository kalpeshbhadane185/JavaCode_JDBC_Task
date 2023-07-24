package com.logilite.servelet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;

import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String operation = request.getParameter("operation");
		if (operation != null && operation.equals("delete"))
		{
			String userId = request.getParameter("userId");
			deleteUserDataFromDatabase(userId);
		}
		response.sendRedirect("admin.jsp");

	}

	private void deleteUserDataFromDatabase(String userId)
	{
		try
		{
			double accountBalance = 0;
			String selectQuery = "select * from bank_user b join tr_activity t on t.user_id = b.user_id "
					+ "where t.user_id = " + userId + " order by tr_date desc ";
			Connection connection = Database_Connectivity.createDBConnection();
			PreparedStatement prepareStatement = connection.prepareStatement(selectQuery);
			ResultSet resultSet = prepareStatement.executeQuery();
			if (resultSet.next())
			{
				accountBalance = resultSet.getDouble("account_balance");
			}
			resultSet.close();
			prepareStatement.close();

			if (accountBalance < 100.0)
			{
				selectQuery = "delete from bank_user where user_id = ?";
				PreparedStatement deleteStatement = connection.prepareStatement(selectQuery);
				deleteStatement.setInt(1, Integer.parseInt(userId));
				int executeUpdate = deleteStatement.executeUpdate();
				deleteStatement.close();

				if (executeUpdate != 0)
				{
					MyLogger.logger.info("Customer deleted successfully");
				}
				else
				{
					MyLogger.logger.info("Please check query");
				}
			}
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}
	}

}
