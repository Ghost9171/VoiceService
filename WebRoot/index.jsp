<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <link href="./js/bootstrap.min.css" rel="stylesheet">

<script src="./js/jquery-1.9.1.js"></script>
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
<script language="javascript">

	function send() {
		var sentence = $('#sentence').val();
		$.post("servlet/ServletTest", {
			"sentence" : sentence
		}, function(data) {
			$("#result").empty();
			$("#result").append(data);
		});
		return false;
	}
	
	
</script>
<body>
	<div class="row">
		<div class="col-md-3"></div>
		
		<div class="col-md-6" style="padding-top: 200px;">
			<form action="servlet/ServletTest" method="post" onsubmit="return send();">
				<div class="input-group">
					<input type="text" name="sentence" class="form-control"
						id="sentence" placeholder="Search for..."> <span
						class="input-group-btn">
						<button class="btn btn-success" type="button" onclick="send()">分析</button>
					</span>
				</div>
				<!-- /input-group -->
				<br>
				<p id="result"></p>
			</form>

		</div>
		<div class="col-md-3"></div>
	</div>
	
  </body>
</html>
