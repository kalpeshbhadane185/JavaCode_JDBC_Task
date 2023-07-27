package com.logilite.servelet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logilite.bean.Transaction_Activity;
import com.logilite.dao.TransactionStateDAO;

@WebServlet("/bankStatement")
public class BankStatement extends HttpServlet
{
	private static final long				serialVersionUID		= 1L;
	private final TransactionStateDAO	transactionStateDAO	= new TransactionStateDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		List<Transaction_Activity> tr_activity = null;
		String operation = request.getParameter("transactionDetails");
		if (operation != null && operation.equals("statement"))
		{
			String userId = request.getParameter("userId");
			tr_activity = transactionStateDAO.fetchUserTransactionData(userId);
		}
		request.setAttribute("tr_activity", tr_activity);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ad_bankstatement.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}
}
