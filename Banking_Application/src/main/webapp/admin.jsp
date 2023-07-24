<%@page import="java.util.List"%>
<%@page import="com.logilite.bean.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Admin Page</title>
<style>
.form-container {
	display: none;
}
</style>
<script>
	function toggleAccountNoField() {
		var userType = document.getElementById("userType").value;
		var accountNoField = document.getElementById("accountNo");

		if (userType === "c") {
			accountNoField.disabled = false;
		} else {
			accountNoField.disabled = true;
		}
	}
	function toggleForm(formName) {
		var form = document.getElementById(formName);
		if (form.style.display === "none") {
			hideAllForms();
			form.style.display = "block";
		} else {
			form.style.display = "none";
		}
	}

	function hideAllForms() {
		var forms = document.getElementsByClassName("form-container");
		for (var i = 0; i < forms.length; i++) {
			forms[i].style.display = "none";
		}
	}
</script>
</head>
<body>
	<button onclick="toggleForm('createUserForm')">Create User</button>
	<!-- <button onclick="toggleForm('customerListForm')">Customer List</button>  -->
	<!-- 	<button onclick="toggleForm('transactionForm')">Customer List</button> -->

	<div id="createUserForm" class="form-container">
		<h2>Create User Form</h2>
		<form action="AdminServlet" method="post" name="customer_form">
			<label for="uname">Username:</label> <input type="text" id="uname"
				name="username" required /><br /> <br /> <label for="pass">Password:</label>
			<input type="password" id="pass" name="password" required /><br />
			<br /> <label for="mobno">Mobile Number:</label> <input type="text"
				id="mobno" name="mobileno" required /><br /> <br /> <label
				for="gender">Gender:</label> <input type="radio" id="gender"
				name="gender" value="Male" required /> Male <input type="radio"
				id="gender" name="gender" value="Female" required /> Female<br />
			<br /> <label for="email">Email:</label> <input type="email"
				id="email" name="email" required /><br /> <br /> <label
				for="userType">User Type:</label> <select id="userType"
				name="userType" required onchange="toggleAccountNoField()">
				<option value="Customer">Customer</option>
				<option value="Admin">Admin</option>
			</select> <br /> <br /> <!-- <label for="accountNo">Account Number:</label> <input
				type="text" id="accountNo" name="accountNo" required /><br /> <br /> -->
			<input type="submit" name="admin" value="Register" />
		</form>
	</div>

	<!-- <div id="customerListForm" class="form-container">
		<h2>Customer List</h2>
		<form action="AdminServlet" method="get">
			<input type="submit" value="getCustomerList" name="CustomerList" />
		</form>
	</div> -->
<br>
	<form action="AdminServlet" method="get">
		<input type="submit" name="admin" value="customerlist" />
	</form>

	<input type="button" value="Back"
		onclick="window.location.href='admin.jsp'" />
</body>
</html>