package com.logilite.servelet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet
{
	public static Transaction_Activity	activity			= new Transaction_Activity();

	private static final long			serialVersionUID	= 1L;
	public static int					parent_id			= -1;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user = authenticated(username, password);
		System.out.println(user.getUser_type());
		if (user != null)
		{
			if (user.getUser_type().equalsIgnoreCase("Admin"))
			{
				response.sendRedirect("admin.jsp");
			}
			else
			{
				processCustomerPage(request, response, user);
			}
		}
		else
		{
			request.setAttribute("errorMessage", "Incorrect username or password.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	public User kal(User user)
	{
		try
		{
			Connection createDBConnection = Database_Connectivity.createDBConnection();
			String query = "select * from bank_user where username='" + user.getUsername() + "'";
			PreparedStatement statement = createDBConnection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				user.setUsername(rs.getString(2));
				user.setUser_type(rs.getString(4));
				user.setAccount_no(rs.getLong(5));
				user.setMob_no(rs.getLong(7));
				user.setEmail(rs.getString(6));
				user.setGender(rs.getString(8));
			}

			String query2 = "select account_balance from tr_activity where user_id=" + user.getUser_id()
					+ " order by tr_date desc limit 1";
			PreparedStatement statement2 = createDBConnection.prepareStatement(query2);
			ResultSet rs2 = statement2.executeQuery();
			if (rs2.next())
			{
				activity.setAccountBalance(rs2.getDouble(1));
				// break;
			}
			return user;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			User user = new User();
			Connection createDBConnection = Database_Connectivity.createDBConnection();
			Transaction_Activity activity = new Transaction_Activity();
			String query = "select * from bank_user where username='" + user.getUsername() + "'";
			PreparedStatement statement = createDBConnection.prepareStatement(query);
			ResultSet rs = statement.executeQuery();
			while (rs.next())
			{
				user.setUsername(rs.getString(2));
				user.setAccount_no(rs.getLong(5));
				user.setMob_no(rs.getLong(7));
				user.setEmail(rs.getString(6));
				user.setGender(rs.getString(8));
			}
			request.setAttribute("username", user.getUsername());
			request.setAttribute("AccountNo", user.getAccount_no());
			request.setAttribute("MobileNo", user.getMob_no());
			request.setAttribute("Email", user.getEmail());
			request.setAttribute("Gender", user.getGender());

			String query2 = "select account_balance from tr_activity where user_id=" + user.getUser_id()
					+ " order by tr_date desc";
			PreparedStatement statement2 = createDBConnection.prepareStatement(query2);
			ResultSet rs2 = statement2.executeQuery();
			while (rs2.next())
			{
				activity.setAccountBalance(rs2.getDouble(1));
			}
			request.setAttribute("AccountBalance", activity.getAccountBalance());
			RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
			dispatcher.forward(request, response);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
				String query = "select * from bank_user where username = '"+username+"' AND password = '"
						+ password + "'";
				ResultSet rs = statement.executeQuery(query);
				while (rs.next())
				{
					user.setUser_type(rs.getString("user_type"));;
					user.setUser_id(rs.getInt("user_id"));
					user.setAccount_no(rs.getLong("account_no"));
					user.setMob_no(rs.getLong("mobile_no"));
					user.setEmail(rs.getString("email"));
					user.setGender(rs.getString("gender"));
					parent_id = rs.getInt("parent_id");
				}
			}
			return user;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void processCustomerPage(HttpServletRequest request, HttpServletResponse response, User user)
			throws ServletException, IOException
	{
		User customer = kal(user);
		request.setAttribute("username", customer.getUsername());
		request.setAttribute("AccountNo", customer.getAccount_no());
		request.setAttribute("MobileNo", customer.getMob_no());
		request.setAttribute("Email", customer.getEmail());
		request.setAttribute("Gender", customer.getGender());
		request.setAttribute("AccountBalance", activity.getAccountBalance());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
		dispatcher.forward(request, response);
	}
}
//1. write create tables queries in database server required for project
//2. create login page fields in jsp page
//3. write a servlet code for login page
//


