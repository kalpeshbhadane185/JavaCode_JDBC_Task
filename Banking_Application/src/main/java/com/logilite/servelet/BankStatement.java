package com.logilite.servelet;

import java.io.IOException;
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

import com.logilite.bean.Transaction_Activity;
import com.logilite.dataBase.Database_Connectivity;

@WebServlet("/bankStatement")
public class BankStatement extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		List<Transaction_Activity> tr_activity = null;
		String operation = request.getParameter("transactionDetails");
		if (operation != null && operation.equals("statement"))
		{
			String userId = request.getParameter("userId");
			tr_activity = fetchUserTransactionData(userId);
		}

		request.setAttribute("tr_activity", tr_activity);

		RequestDispatcher dispatcher = request.getRequestDispatcher("ad_bankstatement.jsp");
		dispatcher.forward(request, response);
	}

	public List<Transaction_Activity> fetchUserTransactionData(String userId)
	{
		try
		{
			List<Transaction_Activity> list = new ArrayList<>();
			String query = "select * from tr_activity where user_id=" + userId + " order by tr_date desc ;";
			Connection connection = Database_Connectivity.createDBConnection();
			PreparedStatement prepareStatement = connection.prepareStatement(query);
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next())
			{
				Transaction_Activity activity = new Transaction_Activity();
				activity.setTransaction_date(rs.getTimestamp("tr_date"));
				activity.setAmmount(rs.getDouble("amount"));
				activity.setTransaction_type(rs.getString("tr_type"));
				list.add(activity);
			}
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	}
}
