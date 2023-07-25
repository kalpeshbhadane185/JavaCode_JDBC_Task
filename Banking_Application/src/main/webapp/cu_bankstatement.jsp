<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
  }

  .statement-table th, .statement-table td {
    padding: 10px;
    text-align: left;
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
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .logout-button input[type="button"]:hover {
    background-color: #45a049;
  }

  .no-transaction {
    text-align: center;
    margin-top: 50px;
    font-size: 18px;
    color: #888888;
  }
</style>
</head>
<body>
  <div class="logout-button">
    <input type="button" value="Logout" onclick="window.location.href='login.jsp'" />
  </div>
  <c:if test="${not empty activity}">
    <table class="statement-table" border="1">
      <tr>
        <th>Sr. no</th>
        <th>Date</th>
        <th>Account Balance</th>
        <th>Amount</th>
        <th>Transaction Type</th>
      </tr>
      <c:set var="count" value="1" />
      <c:forEach items="${activity}" var="statement">
        <tr>
          <td>${count}</td>
          <td>${statement.transaction_date}</td>
          <td>${statement.accountBalance}</td>
          <td>${statement.ammount}</td>
          <td>${statement.transaction_type}</td>
        </tr>
        <c:set var="count" value="${count + 1}" />
      </c:forEach>
    </table>
  </c:if>
  <c:if test="${empty activity}">
    <p class="no-transaction">No transactions found.</p>
  </c:if>
</body>
</html>
