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
import com.logilite.dao.Tr_StatementDAO;
import com.logilite.stringconst.Constants;

@WebServlet("/bankStatement")
public class BankStatement extends HttpServlet
{
	private static final long				serialVersionUID		= 1L;
	private  Tr_StatementDAO	transactionStateDAO	= new Tr_StatementDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String operation = request.getParameter(Constants.TRANSACTIONDETAILS);
		if (operation != null && operation.equals(Constants.STATEMENT))
		{
			String userId = request.getParameter("userId");
			List<Transaction_Activity> tr_activity = transactionStateDAO.getUserTrData(userId);
			request.setAttribute("tr_activity", tr_activity);
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("ad_bankstatement.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}
}
