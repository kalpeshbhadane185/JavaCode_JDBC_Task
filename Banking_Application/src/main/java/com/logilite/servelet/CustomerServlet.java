package com.logilite.servelet;

import java.io.IOException;
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
import com.logilite.dao.TransactionActivityCust;
import com.logilite.dao.UserDAO;
import com.logilite.logger.MyLogger;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet
{
	private static final long				serialVersionUID		= 1L;
	private final TransactionActivityCust	transactionActivityCust	= new TransactionActivityCust();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String handler = request.getParameter("transaction");

			if (handler.equalsIgnoreCase("submit"))
			{
				HttpSession session = request.getSession();
				User user = (User) session.getAttribute("user");
				
				if (user == null)
				{
					RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
					dispatcher.forward(request, response);
					return;
				}else {
					
					Transaction_Activity activity = (Transaction_Activity) session.getAttribute("tr_activity");
					activity.setTransaction_type(request.getParameter("transaction_type"));
					activity.setAmmount(Double.parseDouble(request.getParameter("amount")));

					String insertTransactionActivity = transactionActivityCust.insertTransactionActivity(user,
							activity);
					request.setAttribute("errorMessage",
							request.getParameter("transaction_type") + " " + insertTransactionActivity);
				}
			}
			LoginServlet.processCustomerPage(request, response);
		}
		catch (Exception e)
		{
			MyLogger.logger.log(Level.ERROR, "SQLException :: ", e);
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
				boolean deleteUserDataFromDatabase = transactionActivityCust.deleteUserDataFromDatabase(userId);
				if (deleteUserDataFromDatabase)
				{
					request.setAttribute("deleteMessage", "Customer Sucessfully Deleted");
				}
				else
				{
					request.setAttribute("notDeleteMessage",
							"You can't delete this customer because his maintain " + "their account balance");
				}
			}

			User user = (User) session.getAttribute("user");
			UserDAO dao = new UserDAO();
			List<User> userList = dao.fetchUserData(user);
			request.setAttribute("userList", userList);

			RequestDispatcher dispatcher = request.getRequestDispatcher("customerList.jsp");
			dispatcher.forward(request, response);
		}
		catch (Exception e)
		{
			MyLogger.logger.log(Level.ERROR, "SQLException :: ", e);
		}
	}
}
