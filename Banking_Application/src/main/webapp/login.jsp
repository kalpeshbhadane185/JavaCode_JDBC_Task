<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
  body {
    margin: 0;
    padding: 0;
    font-family: Arial, sans-serif;
    background-color: #f1f1f1;
  }

  .header {
    background-color: purple;
    padding: 15px;
    color: #ffffff;
    text-align: center;
    font-size: 24px;
  }

  .login-container {
    background-color: #ffffff;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    width: 300px;
    max-width: 80%;
    margin: 20px auto;
  }

  .login-form {
    display: flex;
    flex-direction: column;
  }

  .form-input {
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }

  .form-input:focus {
    outline: none;
    border-color: #4CAF50;
  }

  .login-button {
    background-color: #4CAF50;
    color: #fff;
    padding: 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .login-button:hover {
    background-color: #45a049;
  }

  .error-message {
    color: red;
    text-align: center;
    margin-top: 10px;
  }

  .form-label {
    margin-bottom: 5px;
    font-weight: bold;
  }
</style>
</head>
<body>
  <div class="header">
    Welcome To Logilite Banking Application
  </div>
  <div class="login-container">
    <form class="login-form" action="loginServlet" method="POST">
      <label class="form-label">Username:</label>
      <input class="form-input" type="text" name="username" placeholder="Username" required>
      <label class="form-label">Password:</label>
      <input class="form-input" type="password" name="password" placeholder="Password" required>
      <input class="login-button" type="submit" value="Sign In">
    </form>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null) { %>
      <p class="error-message"><%= errorMessage %></p>
    <% } %>
  </div>
</body>
</html>
