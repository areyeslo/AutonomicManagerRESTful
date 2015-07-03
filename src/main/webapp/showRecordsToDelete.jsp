<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>Select a policy to be deleted:</h2>
	<table>
		<%-- JSTL foreach to loop a list retrieve from a servlet(DeletePolicy.java) in jsp --%>
		<c:forEach items="${policies}" var="policy">
			<tr>
				<td>ID:</td>
				<td><c:out value="${policy.id}" /></td>
				<td>Maximum books:</td>
				<td><c:out value="${policy.max_books}" /></td>
				<td>Year of Book:</td>
				<td><c:out value="${policy.year_book}" /></td>
				<td>Activated:</td>
				<td><c:out value="${policy.activate}" /></td>
			</tr>
		</c:forEach>
	</table>
	<p>Write the ID you would like to delete:</p>
	<form action="DeletePolicy">
		<input type="text" name="id" /><br> <input type="submit"
			value="submit">
	</form>
</body>
</html>