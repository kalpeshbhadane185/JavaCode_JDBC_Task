package com.logilite.servelet;

import java.io.IOException;
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
import com.logilite.dao.UserDAO;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet
{
	private static final long	serialVersionUID	= 1L;
	private final UserDAO		userDAO				= new UserDAO();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession httpSession = request.getSession();
		User user = (User) httpSession.getAttribute("user");
		
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

				boolean registrationSuccess = userDAO.registerUser(user);
				if (registrationSuccess)
				{
					response.sendRedirect("admin.jsp");
					System.out.println("Success");
				}
				else
				{
					MyLogger.logger.info("please check query");
				}
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String handler = request.getParameter("admin");
		if (handler.equals("customerlist"))
		{
			List<User> userList;
			try
			{
				userList = userDAO.fetchUserData(user);
				request.setAttribute("userList", userList);
				RequestDispatcher dispatcher = request.getRequestDispatcher("customerList.jsp");
				dispatcher.forward(request, response);
			}
			catch (SQLException | ServletException | IOException e)
			{
				MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			}
		}
	}

	private static long generateRandom12DigitNumber()
	{
		Random random = new Random();
		return (long) (random.nextDouble() * 9_000_000_000_000L) + 1_000_000_000_000L;
	}
}