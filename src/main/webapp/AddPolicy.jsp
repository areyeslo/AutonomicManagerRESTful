<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Policy</title>
</head>
<body>
	<h1>Add a Policy</h1>
	<form action="AddPolicy" method="POST" target="_blank">
		<br />
		Maximum Number of Books: <input type="text" name="maxBooks">
		<br />
		<br />
		Year Books: <input type="text" name="yearBook" />
		<br />
		<br />
		<input type="checkbox" name="activate" checked="checked" /> Activate
		<br />
		<br />
		<input type="submit" value="Submit" />
	</form>
</body>
</html>