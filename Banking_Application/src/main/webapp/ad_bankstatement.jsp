<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bank Statement</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f1f1f1;
	margin: 0;
	padding: 0;
}

.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
}

.logout-button {
	position: absolute;
	top: 25px;
	right: 25px;
	background-color: #4CAF50;
	color: gray;
	border: none;
	padding: 2px 8px;
	border-radius: 4px;
	cursor: pointer;
}

.logout-button:hover {
	background-color: #45a049;
}

table {
	width: 60%;
	border-collapse: inherit;
	margin: 20px auto;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

th {
	background-color: #f2f2f2;
}

tr:hover {
	background-color: #f2f2f2;
}

p {
	text-align: center;
	font-size: 18px;
	color: #888888;
	margin-top: 50px;
}

.form-buttons {
	top: 20px;
	right: 20px;
}

.form-buttons button {
	background-color: #4CAF50;
	color: #ffffff;
	padding: 8px 10px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin: 2px;
}

.form-buttons button:hover {
	background-color: #45a049;
}
</style>
<script>
	function showDateRangeOption() {
		var transactionDetails = document
				.querySelector('input[name="transactionDetails"]:checked').value;
		var dateRangeOption = document.getElementById('dateRangeOption');
		var fromDateInput = document.querySelector('input[name="fromDate"]');
		var toDateInput = document.querySelector('input[name="toDate"]');

		if (transactionDetails === 'customRange') {
			dateRangeOption.style.display = 'block';
			fromDateInput.setAttribute('required', 'true');
			toDateInput.setAttribute('required', 'true');
		} else {
			dateRangeOption.style.display = 'none';
			fromDateInput.removeAttribute('required');
			toDateInput.removeAttribute('required');
		}
	}
</script>
</head>
<body>

	<c:set var="userId" value="${param.userId}" />

	<div class="container">
		<!-- <div class="logout-button">
			<form action="loginServlet" method="get">
				<input type="submit" name="logout" value="logout" />
			</form>
		</div> -->
		<div class="logout-button">
			<input type="button" value="Logout"
				onclick="window.location.href='login.jsp'" />
		</div>

		<div class="form-buttons">
			<button onclick="location.href='AdminServlet?admin=customerlist'">Back</button>
		</div>

		<div>
			<form action="TransactionServlet" method="get">
				<input type="hidden" name="userId" value="${userId}"> <input
					type="radio" name="transactionDetails" value="currentMonth"
					onclick="showDateRangeOption();" checked /> Current Month <input
					type="radio" name="transactionDetails" value="currentQuarter"
					onclick="showDateRangeOption();" /> Current Quarter <input
					type="radio" name="transactionDetails" value="customRange"
					onclick="showDateRangeOption();" /> Custom Range
				<div id="dateRangeOption" style="display: none;">
					From: <input type="date" name="fromDate" value="fromDate" /> To: <input
						type="date" name="toDate" value="toDate" />
				</div>
				<input type="submit" value="View Statement" />
			</form>
		</div>


		<c:if test="${empty activity}">
			<p>No transactions found.</p>
		</c:if>
		<c:if test="${not empty activity}">
			<table>
				<tr>
					<th>Transaction Date</th>
					<th>Transaction Type</th>
					<th>Amount</th>
				</tr>
				<c:forEach items="${activity}" var="transaction">
					<tr>
						<td>${transaction.transaction_date}</td>
						<td>${transaction.transaction_type}</td>
						<td>${transaction.ammount}</td>
					</tr>
				</c:forEach>
			</table>
			<br>
		</c:if>
	</div>
</body>
</html>


