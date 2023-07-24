<%@ page import="com.logilite.servelet.LoginServlet"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>My Page</title>
</head>
<body>
	<h2>Customer Details</h2>
	<div align="left">
		<label>Name :- ${username}</label> <br> <br> <label>Account
			Number :- ${AccountNo}</label> <br> <br> <label>Mob No :-
			${MobileNo}</label> <br> <br> <label>Email :- ${Email}</label> <br>
		<br> <label>Gender :- ${Gender}</label> <br> <br> <label>Account
			Balance :- ${AccountBalance}</label> <br> <br>
	</div>
	<div>
		<form action="loginServlet" method="get">
			<label for="transaction type">transaction Type:</label> <select
				id="transaction type" name="transaction_type">
				<option value="Credit">Credit</option>
				<option value="Debit">Debit</option>
			</select> <label>Amount </label> <input type="number" name="amount" required>
			<input type="submit" value="submit" name="transaction">
		</form>
	</div>

	<div>
		<form action="TransactionServlet" method="get">
			<input type="submit" value="statement" name="transactionDetails">
		</form>
	</div>

	<form action=loginServlet method=get>
		<button type=button">Back  Page</button>
	</form>

	<!-- <div>
		<input type="button" value="Back"
			onclick="window.location.href='login.jsp'" />
	</div> -->

	<div id="successMessage" style="display: none;">Transaction
		successful!</div>

</body>
</html>

