<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
</head>
<body>

 <div align="center">
	<h2>Welcome To Logilite Banking Application </h2>
	<form action="loginServlet" method="POST">
		Username: <input type="text" name="username"><br> Password: 
		<input type="password" name="password"><br> <br></a><input
			type="submit" value="Sign In">
	</form>
	
	<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null) { %>
        <p style="color: red;"><%= errorMessage %></p>
    <% } %>
	
</div>
	<!-- <form action="signup.jsp">
		<input type="submit" value="Sign Up">
	</form> -->
</body>
</html>
