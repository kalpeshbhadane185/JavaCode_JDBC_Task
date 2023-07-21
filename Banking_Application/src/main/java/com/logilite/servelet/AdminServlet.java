package com.logilite.servelet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = new User();
		String buttonName = request.getParameter("Submit Customer Form");

		if (buttonName != null)
		{
			if (buttonName.equals("Register"))
			{
				user.setUsername(request.getParameter("username"));
				user.setPassword(request.getParameter("password"));

				Long intNo = Long.parseLong(request.getParameter("mobileno"));
				user.setMob_no(intNo);

				user.setGender(request.getParameter("gender"));
				user.setEmail(request.getParameter("email"));
				user.setUser_type(request.getParameter("userType"));
				intNo = Long.parseLong(request.getParameter("accountNo"));
				user.setAccount_no(intNo);

				String query = "INSERT INTO bank_user (username, password, user_type,"
						+ "account_no, email, mobile_no, gender, parent_id) VALUES(?,?,?,?,?,?,?,?);";
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

					if (LoginServlet.parent_id != -1)
					{
						pStatement.setInt(8, LoginServlet.parent_id);
					}
					else
					{
						throw new NullPointerException("parent id is -1");
					}

					int executeUpdate = pStatement.executeUpdate();

					if (executeUpdate != 0)
					{
						response.sendRedirect("login.jsp");
						System.out.println("Success");
					}
					else
					{
						System.out.println("Something wrong");
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private List<User> fetchUserDataFromDatabase(User user) throws SQLException
	{
		List<User> list = new ArrayList<>();
		String query = "select username, account_no, mobile_no, gender from bank_user where parent_id="
				+ LoginServlet.parent_id + ";";
		Connection connection = Database_Connectivity.createDBConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		ResultSet rs = prepareStatement.executeQuery();
		while (rs.next())
		{
			user = new User();
			user.setUsername(rs.getString(1));
			user.setAccount_no(rs.getLong(2));
			user.setMob_no(rs.getLong(3));
			user.setGender(rs.getString(4));
			list.add(user);
		}
		return list;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = new User();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String buttonName = request.getParameter("CustomerList");
		if (buttonName.equals("getCustomerList"))
		{
			List<User> userList = null;
			try
			{
				userList = fetchUserDataFromDatabase(user);
				out.print("<table border='1' width='50%' ");
				out.print("<tr><th>User Name</th><th>Account No</th><th>Mobile No</th>" + "<th>Gender</th></tr>");
				for (User user2 : userList)
				{
					out.print("<tr><th>" + user2.getUsername() + "</th>");
					out.print("<th>" + user2.getAccount_no() + "</th>");
					out.print("<th>" + user2.getMob_no() + "</th>");
					out.print("<th>" + user2.getGender() + "</th></tr>");
				}
				out.print("</table>");
				out.print("<br><button type=\"submit\" name=\"back\">\n" + "<a href=\"login.jsp\">Back \n"
						+ "	</button>");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			// request.setAttribute("userList", userList);
//			RequestDispatcher dispatcher = request.getRequestDispatcher("admin.jsp");
//			dispatcher.forward(request, response);
		}
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
	}

}