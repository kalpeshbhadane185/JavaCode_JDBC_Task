package com.logilite.servelet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;

import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String handler = request.getParameter("admin");
		if (handler != null)
		{
			if (handler.equals("Register"))
			{
				user.setUsername(request.getParameter("username"));
				user.setPassword(request.getParameter("password"));

				user.setMob_no(Long.parseLong(request.getParameter("mobileno")));

				user.setGender(request.getParameter("gender"));
				user.setEmail(request.getParameter("email"));
				user.setUser_type(request.getParameter("userType"));
				user.setAccount_no(generateRandom12DigitNumber());

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
					pStatement.setInt(8, user.getUser_id());

					int executeUpdate = pStatement.executeUpdate();

					if (executeUpdate != 0)
					{
						response.sendRedirect("admin.jsp");
						System.out.println("Success");
					}
					else
					{
						MyLogger.logger.info("please check query");
					}
				}
				catch (SQLException e)
				{
					MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
				}
			}
		}
	}

	public List<User> fetchUserData(User user) throws SQLException
	{
		List<User> list = new ArrayList<>();
		String query = "select * from bank_user where parent_id=" + user.getUser_id() + ";";
		Connection connection = Database_Connectivity.createDBConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(query);
		ResultSet rs = prepareStatement.executeQuery();
		while (rs.next())
		{
			user = new User();
			user.setUser_id(rs.getInt("user_id"));
			user.setUsername(rs.getString("username"));
			user.setAccount_no(rs.getLong("account_no"));
			user.setMob_no(rs.getLong("mobile_no"));
			user.setGender(rs.getString("gender"));
			user.setEmail(rs.getString("email"));
			list.add(user);
		}
		return list;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");

	    try {
	        String handler = request.getParameter("admin");
	        if (handler.equals("customerlist")) {
	            List<User> userList = null;
	            userList = fetchUserData(user);
	            request.setAttribute("userList", userList);

	            RequestDispatcher dispatcher = request.getRequestDispatcher("customerList.jsp");
	            dispatcher.forward(request, response);
	        }
	    } catch (SQLException e) {
	        MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
	    }
	}


	private static long generateRandom12DigitNumber()
	{
		Random random = new Random();
		return (long) (random.nextDouble() * 9_000_000_000_000L) + 1_000_000_000_000L;
	}
}