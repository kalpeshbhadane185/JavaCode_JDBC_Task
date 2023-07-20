package com.logilite.servelet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logilite.bean.User;

 @WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("kg");

		String username = "John Doe";

		request.setAttribute("username", username);

		RequestDispatcher dispatcher = request.getRequestDispatcher("customer.jsp");
		dispatcher.forward(request, response);
		System.out.println("kg");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("kp");
		doGet(request, response);
		System.out.println("kp");

	}

	private List<User> fetchCustomerDataFromDatabase()
	{
		List<User> list = new ArrayList<>();
		return list;
	}

}
