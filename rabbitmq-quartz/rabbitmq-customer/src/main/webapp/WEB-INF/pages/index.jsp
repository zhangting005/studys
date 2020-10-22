<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
   String  contextpath=application.getContextPath();
%> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextpath%>/static/jquery.min.js" type="text/javascript"></script>
<script src="<%=contextpath%>/static/message.js" type="text/javascript"></script>
<title>主界面</title>
</head>
<body>
    <h2>接收消息</h2>
     <table border="1" rules="all">
		<thead>
			<tr>
				<th>id</th>
				<th>type</th> 
				<th>status</th>
				<th>request</th>
				<th>entry_date</th>
				<th>comment</th>
			</tr>
		</thead>
		<tbody id="message">
		    <c:forEach items="${sessionScope.queueTableList}" var="queue">
		        <tr>
		           <td>${queue.id}</td>
		           <td>${queue.type}</td>
		           <td>${queue.status}</td>
		           <td>${queue.request}</td>
		           <td>${queue.entryDate}</td>
		            <td>${queue.comment}</td>
		        </tr>
		    </c:forEach>
		</tbody>
	</table>
	<br/>
	<br/>
	<form action="clearMessage" method="get">
	   <input type="submit" value="清空">
	</form>
</body>
</html>