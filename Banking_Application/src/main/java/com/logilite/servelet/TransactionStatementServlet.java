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
import com.logilite.dao.Tr_StatementDAO;
import com.logilite.dao.TransactionActiviryDAO;
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;
import com.logilite.stringconst.Constants;

@WebServlet("/TransactionServlet")
public class TransactionStatementServlet extends HttpServlet
{
	private static final long	serialVersionUID	= 1L;
	private Tr_StatementDAO		transactionStateDAO	= new Tr_StatementDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.USER);
		String hanlderName = request.getParameter(Constants.TRANSACTIONDETAILS);
		try
		{
			List<Transaction_Activity> activity = null;
			if (hanlderName.equalsIgnoreCase(Constants.CUSTOMRANGE))
			{
				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				activity = transactionStateDAO.fetchUserTransactionData(user, hanlderName, fromDate, toDate);
			}
			else
			{
				activity = transactionStateDAO.fetchUserTransactionData(user, hanlderName, null, null);
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
	}
}
