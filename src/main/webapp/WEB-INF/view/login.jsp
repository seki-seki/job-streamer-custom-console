<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="header.jsp" />
<html>
<body>
	<div class="main grid content full height">
		<h1 class="ui violet header">Login</h1>
		<form action="/login" method="post">
			<input type="text" name="username">
			<input type="password" name="password">
			<button type="submit">Login</button>
		</form>
	</div>
</body>
</html>