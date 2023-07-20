<%@ page import="com.logilite.servelet.LoginServlet"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>My Page</title>
</head>
<body>
<%-- 	<jsp:useBean id="myServlet" class="com.logilite.servelet.LoginServlet" />
 --%>		<h2>Customer Details</h2>

	<div align="left">

		<label>Name :- ${username}</label> <br> <br> <label>Account
			Number :- ${AccountNo}</label> <br> <br> <label>Mob No :- ${MobileNo}</label> <br> <br> <label>Email :- ${Email}</label> <br>
		<br> <label>Gender :- ${Gender}</label> <br> <br>
	</div>
	<div>
		<form action="loginServlet" method="get">
			<input type="button" value="AccountBalance" name="balance"> :- ${AccountBalance}
		</form>
		<br> <br>
	</div>
	
	<div>
		<form action="loginServlet" method="get">
		
		<label
				for="transaction type">transaction Type:</label> 
				<select id="transaction type" name="transaction type" >
				<option value="Credit">Credit</option>
				<option value="Debit">Debit</option>
			</select>
			<label>Amount </label>
			<input type="number">
			<input type="button" value="dotransaction" name="transaction">
		</form>
	</div>

</body>
</html>

