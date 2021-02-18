<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Find By Country</title>
</head>
<body>
<form method="post">
    <input type="text" name="country" value="${users.getCountry()}">
    <input type="submit" value="Find">
</form>
<h3>Result</h3>
<table>
    <tr>
        <td>${users.getId()}</td>
        <td>${users.getName()}</td>
        <td>${users.getEmail()}</td>
        <td>${users.getCountry()}</td>
    </tr>
</table>
</body>
</html>
