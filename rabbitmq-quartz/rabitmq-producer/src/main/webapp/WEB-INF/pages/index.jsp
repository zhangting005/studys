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
    <h2>发送消息</h2>
     <form action="sendMessage" method="post">
        originOrderType :<input type="text" name="originOrderType"/><br/><br/>
        originOrderNo: <input type="text"  name="originOrderNo"/><br/><br/>
        originOrderQty: <input type="text"  name="originOrderQty"/><br/><br/>
        newOrderNo: <input type="text"  name="newOrderNo"/><br/><br/>
        newOrderQty: <input type="text"  name="newOrderQty"/><br/><br/>
        <input type="submit" value="发送"/><br/><br/>
     </form>
     
     <h2>所有请求</h2>
     <table border="1" rules="all">
		<thead>
			<tr>
				<th>id</th>
				<th>type</th> 
				<th>status</th>
				<th>request</th>
				<th>entry_date</th>
			</tr>
		</thead>
		<tbody id="message">
		    <c:forEach items="${sessionScope.queueTableList}" var="queue">
		        <tr>
		           <td>${queue.id}</td>
		           <td>${queue.type}</td>
		           <td style="color: red">${queue.status}</td>
		           <td>${queue.request}</td>
		           <td>${queue.entryDate}</td>
		        </tr>
		    </c:forEach>
		</tbody>
	</table>
	<br/>
	<br/>
	<form action="resetMessage" method="get">
	   <input type="submit" value="全部重新发送">
	</form>
	<br/>
	
	<!-- <form action="stopJob" method="get">
	   <input type="submit" value="暂停">
	</form>
	<br/> -->
	
	<form action="restartJob" method="get">
	   <input type="submit" value="马上开始">
	</form>
</body>
</html>