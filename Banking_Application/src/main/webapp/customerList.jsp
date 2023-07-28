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

#deletePopup {
	display: none;
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background-color: scrollbar;
	padding: 20px;
	border: 1px solid #ccc;
	box-shadow: 0 0 5px #888;
	border-radius: 10px;
}

#deletePopup h3 {
	margin-top: 0;
}

#deletePopup p {
	margin-bottom: 10px;
}

#deletePopup div {
	display: flex;
	justify-content: flex-end;
}

#deletePopup button {
	margin-left: 10px;
	padding: 5px 10px;
	border: none;
	background-color: #4CAF50;
	color: #fff;
	border-radius: 4px;
	cursor: pointer;
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
							<button type="button"
								onclick="confirmDelete(${customer.user_id}, 
						 '${customer.username}')">Delete</button>
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

			<div id="deletePopup">
				<h3>Are you sure you want to delete this customer?</h3>
				<p id="customerName"></p>
				<div>
					<button onclick="deleteCustomer()">Yes</button>
					<button onclick="cancelDelete()">No</button>
				</div>
			</div>
			<script>
        var deletePopup = document.getElementById('deletePopup');
        var customerName = document.getElementById('customerName');

        function confirmDelete(userId, username) {
            customerName.textContent =  username;
            deletePopup.style.display = 'block';
            document.getElementById('userId').value = userId;
        }

        function cancelDelete() {
            deletePopup.style.display = 'none';
        }

        function deleteCustomer() {
            document.getElementById('deleteForm').submit();
        }
    </script>
			<form id="deleteForm" action="CustomerServlet" method="post">
				<input type="hidden" name="operation" value="delete"> <input
					type="hidden" name="userId" id="userId" value="">
			</form>
	</div>
	<br>
	<%
	String errorMessage = (String) request.getAttribute("deleteMessage");
	if (errorMessage != null) {
	%>
	<p id="deleteMessage" style="color: green;"><%=errorMessage%></p>
	<script>
				// Automatically hide the deleteMessage after 5 seconds (5000 milliseconds)
				setTimeout(function() {
					var deleteMessage = document
							.getElementById('deleteMessage');
					if (deleteMessage) {
						deleteMessage.style.display = 'none';
					}
				}, 5000);
			</script>
	<%
}
%>
	<%
	String notDeleteMessage = (String) request.getAttribute("notDeleteMessage");
	if (notDeleteMessage != null) {
	%>
	<p id="notDeleteMessage" style="color: red;"><%=notDeleteMessage%></p>
	<script>
				// Automatically hide the deleteMessage after 5 seconds (5000 milliseconds)
				setTimeout(function() {
					var notDeleteMessage = document
							.getElementById('notDeleteMessage');
					if (notDeleteMessage) {
						notDeleteMessage.style.display = 'none';
					}
				}, 5000);
			</script>
	<%
}
%>
	</c:if>
	</div>
</body>
</html>
