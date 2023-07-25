package com.logilite.servelet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
		dispatcher.forward(request, response);
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
				int executeUpdate = deleteStatement.executeUpdate();
				deleteStatement.close();

				if (executeUpdate != 0)
				{
					MyLogger.logger.info("Customer deleted successfully");
				}
				else
				{
					MyLogger.logger.info("Please check query");
					return false;
				}
			}
			return true;
		}
		catch (SQLException e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			return false;
		}
	}
}

// <script>
//// Function to show the delete message and set the timer to hide it after 3000
// milliseconds (3 seconds)
// var deleteMessageFromServer = '${deleteMessage}';
// if (deleteMessageFromServer !== '') {
// showDeleteMessage(deleteMessageFromServer);
// }
// function showDeleteMessage(message) {
// var deleteMessageElement = document.getElementById('deleteMessage');
// deleteMessageElement.innerText = message;
// deleteMessageElement.style.display = 'block';
// setTimeout(function() {
// deleteMessageElement.style.display = 'none';
// }, 3000); // 3000 milliseconds = 3 seconds
// }
// </script>
