package com.logilite.servelet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String handler = request.getParameter("transaction");

			if (handler.equalsIgnoreCase("submit"))
			{
				HttpSession session = request.getSession();
				User user = (User) session.getAttribute("user");
				Transaction_Activity activity = (Transaction_Activity) session.getAttribute("tr_activity");
				Connection createDBConnection = Database_Connectivity.createDBConnection();
				activity.setTransaction_type(request.getParameter("transaction_type"));
				activity.setAmmount(Double.parseDouble(request.getParameter("amount")));

				String query = "insert into tr_activity (tr_Date, tr_type, account_balance, amount, user_id) values(?,?,?,?,?);";

				Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

				PreparedStatement pStatement = createDBConnection.prepareStatement(query);
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
						request.setAttribute("errorMessage", "you can credit the maximum 200000 amount in your account"
								+ " and your credit amount is " + activity.getAmmount());
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
								request.setAttribute("errorMessage", "your withdraw amount is " + activity.getAmmount()
									+ " you can withdraw the max amount from your account " + temp);
						}
					}
					else
					{
						temp = temp - 500;
						request.setAttribute("errorMessage",
								"you can't withdraw amount because your debit amount is more rather than your account balance...balance is "
										+ accountBalance + "/n you can debit only " + temp);
					}
				}
				pStatement.setDouble(3, accountBalance);
				pStatement.setDouble(4, activity.getAmmount());
				pStatement.setInt(5, user.getUser_id());
				if (queryExecute)
				{
					pStatement.executeUpdate();
				}
			}
			LoginServlet.processCustomerPage(request, response);
		}
		catch (Exception e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			HttpSession session = request.getSession();
			String operation = request.getParameter("operation");
			if (operation != null && operation.equals("delete"))
			{
				String userId = request.getParameter("userId");
				boolean deleteUserDataFromDatabase = deleteUserDataFromDatabase(userId);
				if (deleteUserDataFromDatabase)
				{
					request.setAttribute("deleteMessage", "Delete successful");
				}
				else
				{
					request.setAttribute("deleteMessage", "you can not delete this customer ");
				}
			}

			AdminServlet adminServlet = new AdminServlet();
			User user = (User) session.getAttribute("user");
			List<User> userList = null;
			userList = adminServlet.fetchUserData(user);
			request.setAttribute("userList", userList);

			RequestDispatcher dispatcher = request.getRequestDispatcher("customerList.jsp");
			dispatcher.forward(request, response);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private boolean deleteUserDataFromDatabase(String userId)
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

			if (accountBalance < 600.0)
			{
				selectQuery = "delete from bank_user where user_id = ?";
				PreparedStatement deleteStatement = connection.prepareStatement(selectQuery);
				deleteStatement.setInt(1, Integer.parseInt(userId));
				deleteStatement.executeUpdate();
				deleteStatement.close();
				MyLogger.logger.info("Customer deleted successfully");
				return true;
			}
			else
			{
				if (accountBalance >= 600)
				{
					return false;
				}
			}
			return false;
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			return false;
		}
	}
}
