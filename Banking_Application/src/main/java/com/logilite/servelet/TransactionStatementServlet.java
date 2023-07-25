package com.logilite.servelet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.logilite.dataBase.Database_Connectivity;
import com.logilite.logger.MyLogger;

@WebServlet("/TransactionServlet")
public class TransactionStatementServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");
	    String buttonName = request.getParameter("transactionDetails");
	    
	    if (buttonName.equals("statement")) {
	        List<Transaction_Activity> activity = null;
	        try {
	            activity = fetchUserDataFromDatabase(user);
	            request.setAttribute("activity", activity);
	        } catch (Exception e) {
	            MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
	        }
	        
	        RequestDispatcher dispatcher = request.getRequestDispatcher("cu_bankstatement.jsp");
	        dispatcher.forward(request, response);
	    }
	}


	private List<Transaction_Activity> fetchUserDataFromDatabase(User user)
	{
		try
		{
			List<Transaction_Activity> list = new ArrayList<>();

			Connection connection = Database_Connectivity.createDBConnection();
			String query = "select * from tr_activity where user_id = " + user.getUser_id()
					+ " order by  tr_date desc";
			PreparedStatement prepareStatement = connection.prepareStatement(query);

			ResultSet rs = prepareStatement.executeQuery();
			while (rs.next())
			{
				Transaction_Activity activity = new Transaction_Activity();
				activity.setTransaction_id(rs.getInt("transaction_id"));
				activity.setTransaction_date(rs.getTimestamp("tr_date"));
				activity.setAccountBalance(rs.getDouble("account_balance"));
				activity.setAmmount(rs.getDouble("amount"));
				activity.setTransaction_type(rs.getString("tr_type"));

				list.add(activity);
			}
			return list;
		}
		catch (Exception e)
		{
			MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			return null;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String buttonName = request.getParameter("transaction");

		User user = new User();

		Connection createDBConnection = Database_Connectivity.createDBConnection();
		Transaction_Activity activity = new Transaction_Activity();
		activity.setTransaction_type(request.getParameter("transaction_type"));
		String parameter = request.getParameter("amount");
		Double amount = Double.parseDouble(parameter);
		activity.setAmmount(amount);

		String query = "insert into tr_activity (tr_Date, tr_type, account_balance, amount, user_id) values(?,?,?,?,?);";

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

		if (buttonName.equalsIgnoreCase("submit"))
		{
			try
			{
				PreparedStatement pStatement = createDBConnection.prepareStatement(query);
				pStatement.setTimestamp(1, currentTimestamp);
				pStatement.setString(2, activity.getTransaction_type());
				double accountBalance = 0.0;
				if (activity.getTransaction_type().equalsIgnoreCase("Credit"))
				{
					accountBalance = accountBalance + activity.getAmmount();
				}
				else
				{
					if (accountBalance >= 500 && activity.getAmmount()<= accountBalance) {
						accountBalance = accountBalance - activity.getAmmount();
					}
				}
				pStatement.setDouble(3, accountBalance);
				pStatement.setDouble(4, activity.getAmmount());
				pStatement.setInt(5, user.getUser_id());
				pStatement.executeUpdate();

			}
			catch (SQLException e)
			{
				MyLogger.logger.log(Level.ERROR, "Exception :: ", e);
			}
		}
	}
}
