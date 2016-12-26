<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="header.jsp" />
<html>
<body>
	<div class="main grid content full height">
		<div class="ui middle aligned center aligned login grid">
			<div class="column">
				<form action=""<%= request.getContextPath() + "/login" %>" class="ui large login form error"
					method="post">
					<div class="ui stacked segment"
						style="width: 400px; margin: auto; margin-top: 100px;">
						<c:if test="${it}">
							<div class="ui error message">Login failed.</div>
						</c:if>
						<div class="field">
							<div class="ui left input">
								<input name="username" placeholder="User name" type="text">
							</div>
						</div>
						<div class="field">
							<div class="ui left input">
								<input name="password" placeholder="Password" type="password">
							</div>
						</div>
						<button class="ui fluid large teal submit button" type="submit">Login</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>