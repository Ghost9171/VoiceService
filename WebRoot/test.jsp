<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="./js/bootstrap.min.css" rel="stylesheet">

<script src="./js/jquery-1.9.1.js"></script>
<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
</head>
<script language="javascript">
	function send() {
		var sentence = $('#sentence').val();
		var last = $('#last').val();
		$.post("servlet/ServletTest2", {
			"type" : "story",
			"sentence" : sentence,
			"lastsentence" : last
		}, function(data) {
			$("#result").empty();
			$("#result").append(data);
			//alert(data.关键字);
			var data1 = JSON.parse(data);
			var statu=data1.分析.statu;
			var keyword=data1.分析.keyword;
			var type=data1.分析.type;
			var control=data1.分析.control;
			
			$("#last").val("{"+'"statu":'+statu+',"keyword":"'+keyword+'","type":"'+type+'","control":"'+control+'"}');
		});
	}
	$(".sentence").keydown(function(event){
		if(event.which == "13")    
			send();
	});
</script>
<body>
	<div class="row">
		<div class="col-md-3"></div>
		
		<div class="col-md-6" style="padding-top: 200px;">
			<form action="servlet/ServletTest2" method="post" onsubmit="false">
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
				<input type="text" id="last"  class="form-control" name="分析"/>
			</form>

		</div>
		<div class="col-md-3"></div>
	</div>
	
	
</body>
</html>