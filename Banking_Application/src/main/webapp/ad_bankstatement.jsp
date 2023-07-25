<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bank Statement</title>
<style>
    /* CSS style to position the logout button */
    .logout-button {
        position: absolute;
        top: 20px;
        right: 20px;
    }
</style>
</head>
<body>
<div class="logout-button">
		<input type="button" value="Logout"
			onclick="window.location.href='login.jsp'" />
	</div>
	<c:if test="${empty tr_activity}">
		<p>No transactions found.</p>
	</c:if>
	<c:if test="${not empty tr_activity}">
		<table border="1" width="70%">
			<tr>
				<th>Sr. No</th>
				<th>Transaction Date</th>
				<th>Amount</th>
				<th>Transaction Type</th>
			</tr>
			<c:forEach items="${tr_activity}" var="transaction" varStatus="loop">
				<tr>
					<td>${loop.count}</td>
					<td>${transaction.transaction_date}</td>
					<td>${transaction.ammount}</td>
					<td>${transaction.transaction_type}</td>
				</tr>
			</c:forEach>
		</table>
		<br>
	</c:if>
	
</body>
</html>
