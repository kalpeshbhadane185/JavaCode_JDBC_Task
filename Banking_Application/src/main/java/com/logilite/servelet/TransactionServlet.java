package com.logilite.servelet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logilite.bean.Transaction_Activity;
import com.logilite.bean.User;
import com.logilite.dataBase.Database_Connectivity;

@WebServlet("/TransactionServlet")
public class TransactionServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = new User();
		String query = "select * from tr_activity where user_id="+user.getUser_id();
		
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
				double accountBalance = LoginServlet.activity.getAccountBalance();;
				if (activity.getTransaction_type().equalsIgnoreCase("Credit"))
				{
					accountBalance = accountBalance + activity.getAmmount();
				}else {
					accountBalance = accountBalance - activity.getAmmount();
				}
				pStatement.setDouble(3, accountBalance);
				pStatement.setDouble(4, activity.getAmmount());
				pStatement.setInt(5, user.getUser_id());
				System.out.println("Hello Kalpesh");
				int executeUpdate = pStatement.executeUpdate();

				if (executeUpdate != 0)
				{
//					response.sendRedirect("status.jsp?status=success");
//					request.getSession().setAttribute("transactionSuccess", true);

					LoginServlet loginServlet = new LoginServlet();
					loginServlet.processCustomerPage(request, response, user);
					System.out.println("Success");
					// After the transaction is completed successfully

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
