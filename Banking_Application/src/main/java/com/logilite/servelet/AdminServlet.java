package com.logilite.servelet;

import java.io.IOException;
import java.sql.SQLException;
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
import com.logilite.logger.MyLogger;
import com.logilite.stringconst.Constants;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet
{
	private static final long	serialVersionUID	= 1L;
	private final UserDAO		userDAO				= new UserDAO();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession httpSession = request.getSession();
		User adminUser = (User) httpSession.getAttribute(Constants.USER);
		User user = new User();
		user.setUsername(request.getParameter(Constants.USERNAME));
		user.setPassword(request.getParameter(Constants.PASSWORD));
		user.setMob_no(Long.parseLong(request.getParameter(Constants.MOBILE_NO)));
		user.setGender(request.getParameter(Constants.GENDER));
		user.setEmail(request.getParameter(Constants.EMAIL));
		user.setUser_type(request.getParameter(Constants.USERTYPE));
		if (!request.getParameter(Constants.USERTYPE).equalsIgnoreCase(Constants.ADMIN)) {
			user.setAccount_no(generateRandom12DigitNumber());
		}

		if (userDAO.registerUser(user, adminUser.getUser_id()))
		{
			response.sendRedirect("admin.jsp");
			System.out.println("Success");
		}
		else
		{
			response.sendRedirect("login.jsp");
			MyLogger.logger.info("Something wrong please check query");
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.USER);

		if (request.getParameter(Constants.ADMIN).equalsIgnoreCase(Constants.CUSTOMERLIST))
		{
			try
			{
				List<User> userList = userDAO.fetchUserData(user);
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