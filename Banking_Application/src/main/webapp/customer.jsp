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
            background-color: #f7f7f7;
            margin: 0;
            padding: 0;
        }

        .header {
            background-color: #4CAF50;
            padding: 10px;
            color: white;
            text-align: right;
        }

        .logout-button {
            position: absolute;
            top: 20px;
            right: 20px;
        }

        .statement-button {
            position: absolute;
            top: 50px;
            right: 20px;
        }

        .content {
            text-align: center;
            padding: 20px;
        }

        h2 {
            color: #4CAF50;
        }

        label {
            font-weight: bold;
        }

        input[type="number"] {
            width: 200px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #45a049;
        }

        .error-message {
            color: red;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="logout-button">
            <input type="button" value="Logout" onclick="window.location.href='login.jsp'" />
        </div>
        <div class="statement-button">
            <form action="TransactionServlet" method="get">
                <input type="submit" value="statement" name="transactionDetails">
            </form>
        </div>
    </div>
    <div class="content">
        <h2>Welcome ${username}</h2>

        <label>Name:</label> ${username} <br> <br>
        <label>Account Number:</label> ${AccountNo} <br> <br>
        <label>Mob No:</label> ${MobileNo} <br> <br>
        <label>Email:</label> ${Email} <br> <br>
        <label>Gender:</label> ${Gender} <br> <br>
        <label>Account Balance:</label> ${AccountBalance} <br> <br>
    </div>
    <div class="content">
        <form action="loginServlet" method="get">
            <label for="transaction-type">Transaction Type:</label>
            <select id="transaction-type" name="transaction_type">
                <option value="Credit">Credit</option>
                <option value="Debit">Debit</option>
            </select>
            <label>Amount:</label>
            <input type="number" name="amount" required>
            <input type="submit" value="Submit" name="transaction">
        </form>
        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
    </div>
</body>
</html>
