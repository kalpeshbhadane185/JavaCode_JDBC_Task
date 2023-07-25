<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Customer List</title>
<style>
    .back-button {
        position: absolute;
        top: 20px;
        right: 20px;
    }
</style>
</head>
<body>

<div class="back-button">
		<input type="button" value="Back"
			onclick="window.location.href='admin.jsp'" />
	</div>
	<c:if test="${empty userList}">
		<p>No customers found.</p>
	</c:if>
	<c:if test="${not empty userList}">
		<table border="1" width="50%">
			<tr>
				<th>User Name</th>
				<th>Account No</th>
				<th>Mobile No</th>
				<th>Gender</th>
				<th>Email</th>
				<th>Delete</th>
				<th>BankStatement</th>

			</tr>
			<c:forEach items="${userList}" var="customer">
				<tr>
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
		String errorMessage = (String) request.getAttribute("errorMessage");
		%>
		<%
		if (errorMessage != null)
		{
		%>
		<p style="color: red;"><%=errorMessage%></p>
		<%
		}
		%>
	</c:if>
</body>
</html>
