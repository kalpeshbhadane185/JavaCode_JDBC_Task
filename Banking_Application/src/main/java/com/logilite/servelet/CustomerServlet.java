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
import com.logilite.dao.TransactionActiviryDAO;
import com.logilite.dao.UserDAO;
import com.logilite.logger.MyLogger;
import com.logilite.stringconst.Constants;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet
{
	private static final long		serialVersionUID	= 1L;
	private TransactionActiviryDAO	trActivityCust		= new TransactionActiviryDAO();
	private UserDAO					userDAO				= new UserDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String handler = request.getParameter(Constants.TRANSACTION);

			if (handler.equalsIgnoreCase(Constants.SUBMIT))
			{
				HttpSession session = request.getSession();
				User user = (User) session.getAttribute(Constants.USER);

				if (user == null)
				{
					RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
					dispatcher.forward(request, response);
					return;
				}
				else
				{
					Transaction_Activity activity = (Transaction_Activity) session.getAttribute(Constants.TR_ACTIVTIY);
					activity.setTransaction_type(request.getParameter(Constants.TRANSACTION_TYPE));
					activity.setAmmount(Double.parseDouble(request.getParameter(Constants.AMOUNT)));

					request.setAttribute("errorMessage", trActivityCust.insertTransactionActivity(user, activity));
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
			String operation = request.getParameter(Constants.OPERATION);
			if (operation != null && operation.equals(Constants.DELETE))
			{
				String userId = request.getParameter("userId");
				if (userDAO.deleteUser(userId))
				{
					request.setAttribute("deleteMessage", Constants.CUSTOMER_DELETED_SUCCESS);
				}
				else
				{
					request.setAttribute("notDeleteMessage", Constants.NOTDELETED);
				}
			}

			User user = (User) session.getAttribute(Constants.USER);
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
