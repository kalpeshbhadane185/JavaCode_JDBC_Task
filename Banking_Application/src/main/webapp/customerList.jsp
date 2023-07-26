<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Customer List</title>
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

.back-button {
	position: absolute;
	top: 10px;
	right: 20px;
	background-color: #4CAF50;
	color: #ffffff;
	border: none;
	padding: 5px 10px;
	border-radius: 4px;
	cursor: pointer;
}

.back-button:hover {
	background-color: #45a049;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

th {
	background-color: buttonhighlight;
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
</style>
</head>
<body>
	<div class="container">
		<div class="back-button">
			<input type="button" value="Back"
				onclick="window.location.href='admin.jsp'" />
		</div>
		<c:if test="${empty userList}">
			<p>No customers found.</p>
		</c:if>
		<c:if test="${not empty userList}">
			<table>
				<tr>
					<th>Sr. No</th>
					<th>User Name</th>
					<th>Account No</th>
					<th>Mobile No</th>
					<th>Gender</th>
					<th>Email</th>
					<th>Delete</th>
					<th>BankStatement</th>
				</tr>
				<c:forEach items="${userList}" var="customer" varStatus="loop">
					<tr>
						<td>${loop.count}</td>
						<td>${customer.username}</td>
						<td>${customer.account_no}</td>
						<td>${customer.mob_no}</td>
						<td>${customer.gender}</td>
						<td>${customer.email}</td>
						<td>
							<form action="CustomerServlet" method="post">
								<input type="hidden" name="operation" value="delete"> <input
									type="hidden" name="userId" value="${customer.user_id}">
								<button type="submit">Delete</button>
							</form>
						</td>
						<td>
							<form action="bankStatement" method="get">
								<input type="hidden" name="transactionDetails" value="statement">
								<input type="hidden" name="userId" value="${customer.user_id}">
								<button type="submit">BankStatement</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</table>
			<br>
			<%
			String errorMessage = (String) request.getAttribute("deleteMessage");
			if (errorMessage != null) {
			%>
			<p id="deleteMessage" style="color: red;"><%=errorMessage%></p>
			<script>
				// Automatically hide the deleteMessage after 5 seconds (5000 milliseconds)
				setTimeout(function() {
					var deleteMessage = document
							.getElementById('deleteMessage');
					if (deleteMessage) {
						deleteMessage.style.display = 'none';
					}
				}, 3000);
			</script>
			<%
}
%>
		</c:if>
	</div>
</body>
</html>
