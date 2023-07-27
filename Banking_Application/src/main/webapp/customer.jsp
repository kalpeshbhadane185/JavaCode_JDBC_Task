<%@ page import="com.logilite.servelet.LoginServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>My Page</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f1f1f1;
	margin: 0;
	padding: 0;
}

.header {
	background-color: #333;
	padding: 10px;
	display: flex;
	justify-content: flex-end;
	align-items: center;
	color: #fff;
}

.header .logout-button, .header .statement-button {
	margin-left: 10px;
}

.header .logout-button input[type="button"], .header .statement-button input[type="submit"]
	{
	background-color: #4CAF50;
	color: #ffffff;
	padding: 8px 15px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.header .logout-button input[type="button"]:hover, .header .statement-button input[type="submit"]:hover
	{
	background-color: #45a049;
}

.content {
	padding: 20px;
	margin: 20px auto;
	max-width: 600px;
	background-color: #ffffff;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.content h2 {
	color: #333;
}

.content label {
	font-weight: bold;
}

.content input[type="number"] {
	padding: 5px;
	border: 1px solid #ddd;
	border-radius: 4px;
}

.content input[type="submit"] {
	background-color: #4CAF50;
	color: #ffffff;
	padding: 8px 15px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin-top: 10px;
}

.content input[type="submit"]:hover {
	background-color: #45a049;
}

.error-message {
	color: #ff0000;
}
</style>
</head>
<body>
	<div class="header">
		<div class="statement-button">
			<form action="loginServlet" method="get">
				<input type="submit" value="logout">
			</form>
		</div>
		<!-- <div class="statement-button">
			<form action="TransactionServlet" method="get">
				<input type="submit" value="Statement" name="transactionDetails">
			</form>
		</div> -->

		<div class="statement-button">
			<form action="cu_bankstatement.jsp">
				<input type="submit" value="Statement" name="transactionDetails">
			</form>
		</div>

	</div>
	<div class="content" align="center">
		<h2>Welcome ${username}</h2>
		<label>Name:</label> ${username} <br> <br> <label>Account
			Number:</label> ${AccountNo} <br> <br> <label>Mob No:</label>
		${MobileNo} <br> <br> <label>Email:</label> ${Email} <br>
		<br> <label>Gender:</label> ${Gender} <br> <br> <label>Account
			Balance:</label> ${AccountBalance} <br> <br>
	</div>
	<div class="content">
		<form action="CustomerServlet" method="get">
			<label for="transaction-type">Transaction Type:</label> <select
				id="transaction-type" name="transaction_type">
				<option value="Credit">Credit</option>
				<option value="Debit">Debit</option>
			</select> <label>Amount:</label> <input type="text" id="numberInput"
				name="amount" pattern="^\d+(\.\d{1,2})?$" required> <input
				type="submit" value="Submit" name="transaction">
		</form>
		<c:if test="${not empty errorMessage}">
			<p class="error-message" id="errorMessage" style="color: green;">${errorMessage}</p>
			<script>
				setTimeout(function() {
					var errorMessage = document.getElementById('errorMessage');
					if (errorMessage) {
						errorMessage.style.display = 'none';
					}
				}, 5000);
			</script>
		</c:if>

	</div>
</body>
</html>
