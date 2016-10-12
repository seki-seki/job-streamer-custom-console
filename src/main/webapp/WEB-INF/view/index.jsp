<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="header.jsp" />
<html>
<body>
	<div class="main grid content full height">
		<h1 class="ui violet header">Search</h1>
		<form action="./search" method="get">
			ジョブ名<input type="text" name="name"><br/>
			最終実行日<input type="date" name="since"> ～ <input type="date" name="until"><br/>
			最終実行ステータス<input type="text" name="status">
			<button type="submit" class="search button">
				<i class="search icon"></i>
			</button>
		</form>
		<h1 class="ui violet header">jobs</h1>
		<div class="column">
			<table class="ui table">
				<thead>
					<tr>
						<th rowspan="2">Job name</th>
						<th colspan="2">Last execution</th>
						<th rowspan="2">Operations</th>
					</tr>
					<tr>
						<th>Exit Status</th>
						<th>End Time</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${it}" var="job" varStatus="loop">
						<tr>
							<td>${job.name}</td>
							<td>${job.lastExitStatus}</td>
							<td>${job.lastExecutionEndTime}</td>
							<td>
								<form action="./${job.name}/execute/" method="post">
									<button type="submit"
										class="ui circular icon green basic button">
										<i class="play icon"></i>
									</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>