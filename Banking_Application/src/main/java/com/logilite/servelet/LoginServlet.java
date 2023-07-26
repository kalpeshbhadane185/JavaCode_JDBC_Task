package com.logilite.servelet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		user = authenticated(username, password);

		if (user != null)
		{
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			System.out.println(user.getUser_type());

			if (user.getUser_type().equalsIgnoreCase("Admin"))
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
			request.setAttribute("errorMessage", "Incorrect username or password.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	private User authenticated(String username, String password)
	{
		try
		{
			User user = new User();
			Connection createDBConnection = Database_Connectivity.createDBConnection();
			if (createDBConnection != null)
			{
				Statement statement = createDBConnection.createStatement();
				String query = "select * from bank_user where username = '" + username + "' AND password = '" + password
						+ "'";
				ResultSet rs = statement.executeQuery(query);
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
		catch (Exception e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			return null;
		}
	}

	public static void processCustomerPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{
			Transaction_Activity activity = new Transaction_Activity();
			HttpSession session = request.getSession();
			User customer = (User) session.getAttribute("user");

			Connection createDBConnection = Database_Connectivity.createDBConnection();
			String query = "select * from bank_user b join tr_activity t on t.user_id = b.user_id "
					+ "where t.user_id = " + customer.getUser_id() + " order by  tr_date desc";
			PreparedStatement statement = createDBConnection.prepareStatement(query);

			ResultSet rs = statement.executeQuery();
			if (rs.next())
			{
				activity.setAccountBalance(rs.getDouble("account_balance"));
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
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp"); 
    }
}
