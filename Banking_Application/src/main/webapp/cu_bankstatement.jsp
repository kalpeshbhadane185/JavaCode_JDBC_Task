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
}

.logout-button {
	position: absolute;
	top: 20px;
	right: 20px;
}

.statement-table {
	margin-top: 50px;
	width: 80%;
	max-width: 900px;
	margin-left: auto;
	margin-right: auto;
	background-color: #ffffff;
	border-collapse: collapse;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	margin-right: auto;
}

.statement-table th, .statement-table td {
	padding: 8px;
	text-align: center;
	border-bottom: 1px solid #ddd;
}

.statement-table th {
	background-color: #f2f2f2;
}

.statement-table tr:hover {
	background-color: #f2f2f2;
}

.logout-button input[type="button"] {
	background-color: #4CAF50;
	color: #ffffff;
	padding: 6px 14px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.logout-button input[type="button"]:hover {
	background-color: #45a049;
}

.back-button input[type="submit"] {
	background-color: #4CAF50;
	color: #ffffff;
	padding: 6px 14px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	position: absolute;
	top: 20px;
	right: 100px;
}

.back-button input[type="sub,it"]:hover {
	background-color: #45a049;
}

.no-transaction {
	text-align: center;
	margin-top: 50px;
	font-size: 18px;
	color: #888888;
}

th, td {
	border: 1px solid #ddd;
	padding: 4px;
	text-align: justify;
}

th {
	background-color: #f2f2f2;
}
</style>

<script>
function showDateRangeOption() {
    var transactionDetails = document.querySelector('input[name="transactionDetails"]:checked').value;
    var dateRangeOption = document.getElementById('dateRangeOption');
    dateRangeOption.style.display = transactionDetails === '3' ? 'block' : 'none';
}
</script>
</head>
<body>
	<div class="logout-button">
		<input type="button" value="Logout"
			onclick="window.location.href='login.jsp'" />
	</div>

	<div class="back-button">
		<form action="CustomerServlet" method="get">
			<input type="submit" value="Back" name="transaction" />
		</form>
	</div>
	
	<div>
	<form action="TransactionServlet" method="get">
        <input type="radio" name="transactionDetails" value="1" onclick="showDateRangeOption();" checked/> Current Month
        <input type="radio" name="transactionDetails" value="2" onclick="showDateRangeOption();" /> Current Quarter
        <input type="radio" name="transactionDetails" value="3" onclick="showDateRangeOption();" /> Custom Range
        <div id="dateRangeOption" style="display: none;">
            From: <input type="date" name="fromDate" />
            To: <input type="date" name="toDate" />
        </div>
        <input type="submit" value="View Statement" />
    </form>
	
	</div>

	<c:if test="${not empty activity}">
		<table class="statement-table" border="1">
			<tr>
				<th>Date</th>
				<th>Amount</th>
				<th>Transaction Type</th>
				<th>Account Balance</th>

			</tr>
			<c:forEach items="${activity}" var="statement">
				<tr>
					<td>${statement.transaction_date}</td>
					<td>${statement.ammount}</td>
					<td>${statement.transaction_type}</td>
					<td>${statement.accountBalance}</td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<c:if test="${empty activity}">
		<p class="no-transaction">No transactions found.</p>
	</c:if>
</body>
</html>
