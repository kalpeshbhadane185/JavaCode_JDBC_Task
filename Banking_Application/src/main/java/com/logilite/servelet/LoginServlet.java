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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet
{
	User						user				= new User();

	private static final long	serialVersionUID	= 1L;
	public static int			parent_id			= -1;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		user.setUsername(username);
		user.setPassword(password);
		String authenticated = authenticated(user);
		System.out.println(authenticated);
		if (authenticated != null)
		{
			if (authenticated.equalsIgnoreCase("Admin"))
			{
				response.sendRedirect("admin.jsp");
			}
			else
			{

				request.setAttribute("username", user.getUsername());
				request.setAttribute("AccountNo", user.getAccount_no());
				request.setAttribute("MobileNo", user.getMob_no());
				request.setAttribute("Email", user.getEmail());
				request.setAttribute("Gender", user.getGender());
				RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
				dispatcher.forward(request, response);
			}
		}
		else
		{
			request.setAttribute("errorMessage", "Incorrect username or password.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			Connection createDBConnection = Database_Connectivity.createDBConnection();

			String buttonName = request.getParameter("balance");
			Transaction_Activity activity = new Transaction_Activity();
			if (buttonName.equals("AccountBalance"))
			{
				String query = "select * from bank_user where username='" + user.getUsername() + "'";
				PreparedStatement statement = createDBConnection.prepareStatement(query);
				ResultSet rs = statement.executeQuery();
				while (rs.next())
				{
					user.setUsername(rs.getString(2));
					user.setAccount_no(rs.getInt(5));
					user.setMob_no(rs.getInt(7));
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String authenticated(User user)
	{
		try
		{
			String s = null;
			Connection createDBConnection = Database_Connectivity.createDBConnection();
			if (createDBConnection != null)
			{
				Statement statement = createDBConnection.createStatement();
				String query = "select *  from bank_user where username = '" + user.getUsername() + "' AND password = '"
						+ user.getPassword() + "'";
				ResultSet rs = statement.executeQuery(query);
				while (rs.next())
				{
					user.setUser_id(rs.getInt("user_id"));
					user.setAccount_no(rs.getInt("account_no"));
					user.setMob_no(rs.getInt("mobile_no"));
					user.setEmail(rs.getString("email"));
					user.setGender(rs.getString("gender"));
					parent_id = rs.getInt("parent_id");
					return rs.getString("user_type");
				}
			}
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
