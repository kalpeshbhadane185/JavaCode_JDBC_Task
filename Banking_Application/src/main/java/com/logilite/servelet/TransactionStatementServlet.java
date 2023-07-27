package com.logilite.servelet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import com.logilite.dao.TransactionStateDAO;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

@WebServlet("/TransactionServlet")
public class TransactionStatementServlet extends HttpServlet
{
	private static final long			serialVersionUID	= 1L;

	public String						fromDate			= null;
	public String						toDate				= null;

	private final TransactionStateDAO	transactionStateDAO	= new TransactionStateDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String buttonName = request.getParameter("transactionDetails");
		try
		{
			List<Transaction_Activity> activity = null;
			if (buttonName.equalsIgnoreCase("3"))
			{
				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				activity = transactionStateDAO.fetchUserDataFromDatabase(user, buttonName, fromDate, toDate);
			}
			else
			{
				activity = transactionStateDAO.fetchUserDataFromDatabase(user, buttonName, null, null);
			}
			request.setAttribute("activity", activity);
			RequestDispatcher dispatcher = request.getRequestDispatcher("cu_bankstatement.jsp");
			dispatcher.forward(request, response);
		}
		catch (Exception e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String buttonName = request.getParameter("transaction");
		User user = (User) request.getSession().getAttribute("user");
		String transactionType = request.getParameter("transaction_type");
		double amount = Double.parseDouble(request.getParameter("amount"));

		if (buttonName.equalsIgnoreCase("submit"))
		{
			transactionStateDAO.insertTransactionActivity(user, transactionType, amount);
		}
	}
}
