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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dao.UserDAO;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;
import com.logilite.stringconst.Constants;
import com.logilite.stringconst.SQLQueries;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet
{
	private static final long	serialVersionUID	= 1L;
	private final UserDAO		userDAO				= new UserDAO();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter(Constants.USERNAME);
		String password = request.getParameter(Constants.PASSWORD);
		
		String projectPath = System.getProperty("user.dir");
        System.out.println("Eclipse Project Path: " + projectPath);
        
        projectPath = System.getProperty("user.dir");
        System.out.println("Eclipse Project Path: " + projectPath);
        
        
		User user = userDAO.authenticated(username, password);

		if (user != null)
		{
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			System.out.println(user.getUser_type());

			if (user.getUser_type().equalsIgnoreCase(Constants.ADMIN))
			{
				response.sendRedirect("admin.jsp");
			}
			else
			{
				processCustomerPage(request, response);
			}
		}
		else
		{
			request.setAttribute("errorMessage", Constants.INCORRECT_USERNAME_PASSWORD);
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	public static void processCustomerPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		Connection connection = Database_Connectivity.createDBConnection();
		PreparedStatement pStatement = null;
		try
		{
			Transaction_Activity activity = new Transaction_Activity();
			HttpSession session = request.getSession();
			User customer = (User) session.getAttribute(Constants.USER);

			String query = SQLQueries.USER_ACCOUNT_BALANCE;
			pStatement = connection.prepareStatement(query);
			pStatement.setInt(1, customer.getUser_id());

			ResultSet rs = pStatement.executeQuery();
			if (rs.next())
			{
				activity.setAccountBalance(rs.getDouble(Constants.ACCOUNT_BALANCE));
			}
			session.setAttribute("tr_activity", activity);
			request.setAttribute("username", customer.getUsername());
			request.setAttribute("AccountNo", customer.getAccount_no());
			request.setAttribute("MobileNo", customer.getMob_no());
			request.setAttribute("Email", customer.getEmail());
			request.setAttribute("Gender", customer.getGender());
			request.setAttribute("AccountBalance", activity.getAccountBalance());

			RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
			dispatcher.forward(request, response);
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
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);
		if (session != null)
		{
			session.invalidate();
		}
		response.sendRedirect("login.jsp");
	}
}
