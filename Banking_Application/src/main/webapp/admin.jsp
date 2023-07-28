<%@page import="java.util.List"%>
<%@page import="com.logilite.bean.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Admin Page</title>
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
	padding: 15px;
}

.header {
	background-color: #333;
	color: #fff;
	padding: 10px;
	text-align: center;
}

.header h2 {
	margin: 0;
}

.logout-button {
	float: right;
	margin-top: 1px;
	background-color: #4CAF50;
	color: #fff;
	border: thin;
	padding: 4px 8px;
	border-radius: 4px;
	cursor: pointer;
}

.form-buttons {
	margin-top: 30px;
	text-align: center;
}

.form-buttons button {
	background-color: #4CAF50;
	color: #ffffff;
	padding: 10px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin: 5px;
}

.form-buttons-s form input[type="submit"] {
	float: right;
	margin-top: 10px;
	background-color: #4CAF50;
	border: thin;
	padding: 4px 8px;
	border-radius: 4px;
	cursor: pointer;
	color: #ffffff;
	cursor: pointer;
	margin: 0px;
}

.form-buttons button:hover {
	background-color: #45a049;
}

.form-container {
	display: none;
	margin-top: 20px;
	background-color: #ffffff;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	padding: 20px;
}

.form-container h2 {
	margin-bottom: 20px;
	color: #333333;
}

.form-container form label {
	display: block;
	margin-bottom: 10px;
	color: #333333;
}

.form-container form input[type="text"], .form-container form input[type="password"],
	.form-container form input[type="email"], .form-container form select {
	width: 60%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.form-container form input[type="radio"] {
	margin-right: 5px;
}

.form-container form input[type="submit"] {
	background-color: #4CAF50;
	color: #ffffff;
	padding: 10px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.form-container form input[type="submit"]:hover {
	background-color: #45a049;
}

.form-container form select {
	width: 30%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.no-transaction {
	text-align: center;
	margin-top: 50px;
	font-size: 18px;
	color: #888888;
}
</style>
<script>
	function toggleAccountNoField() {
		var userType = document.getElementById("userType").value;
		var accountNoField = document.getElementById("accountNo");

		if (userType === "Customer") {
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
	<div class="header">
		<div class="container">
			<h2>Welcome ${user.username}</h2>
			<div class="form-buttons-s">
				<form action="loginServlet" method="get">
					<input type="submit" name="logout" value="logout" />
				</form>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="form-buttons">
			<button onclick="toggleForm('createUserForm')">Create User</button>
			<button onclick="location.href='AdminServlet?admin=customerlist'">Customer
				List</button>
		</div>

		<div id="createUserForm" class="form-container">
			<h2>Create User Form</h2>
			<form action="AdminServlet" method="post" name="customer_form">
				<label for="uname">Username:</label> <input type="text" id="uname"
					name="username" required /><br /> <br /> <label for="pass">Password:</label>
				<input type="password" id="pass" name="password" required /><br />
				<br /> <label for="mobno">Mobile Number:</label> <input type="text"
					id="mobno" name="mobile_no" pattern="[0-9]{10}" required /> <br />
				<br /> <label for="gender">Gender:</label> <input type="radio"
					id="gender" name="gender" value="Male" required /> Male <input
					type="radio" id="gender" name="gender" value="Female" required />
				Female<br /> <br /> <label for="email">Email:</label> <input
					type="email" id="email" name="email" required
					pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" /><br /> <br />
				<label for="userType">User Type:</label> <select id="userType"
					name="userType" required onchange="toggleAccountNoField()">
					<option value="Customer">Customer</option>
					<option value="Admin">Admin</option>
				</select> <br /> <br /> <input type="submit" name="admin" value="Register" />
			</form>
		</div>
	</div>
</body>
</html>
