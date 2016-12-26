<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="description" content="JobStreamer : JobStreamer Example." />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/semantic-ui/2.1.8/semantic.min.css"
	type="text/css" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/prism/1.5.0/themes/prism.css"
	type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/basic.css" type="text/css" />

<script src="https://cdn.jsdelivr.net/jquery/1.12.3/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/prism/1.5.0/prism.js"></script>
<script
	src="https://cdn.jsdelivr.net/prism/1.5.0/components/prism-java.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/prism/1.5.0/components/prism-bash.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/prism/1.5.0/plugins/line-numbers/prism-line-numbers.min.js"></script>
<script src="https://cdn.jsdelivr.net/semantic-ui/2.1.8/semantic.min.js"></script>
</head>
<body>
	<div class="ui fixed inverted teal menu">
		<div class="title item">
			<b>Job Streamer</b>
		</div>
		<div class="right menu">
		<!--  TODO: login 画面にはログアウトボタンを出さないようにする-->
		<a href="<%= request.getContextPath() + "/logout" %>"><i class = "sign out icon"></i>logout</a></div>
	</div>
</body>
</html>