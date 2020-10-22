<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
   String  contextpath=application.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextpath%>/static/jquery.min.js"
	type="text/javascript"></script>
<script src="<%=contextpath%>/static/message.js" type="text/javascript"></script>
<title>任务调度</title>
<style type="text/css">
  label{
   display: inline-block;
   width: 100px;
  }
</style>
</head>
<body>
	<h2>任务调度</h2>
	<form action="addJob" method="post" id="addForm">
	<label>id:</label><input type="text" name="jobid" id="jobid"><p/>
	<label>name:</label> <input type="text" name="jobname" id="jobname"></input><p/>
	<label>group:</label> <input type="text" name="jobgroup" id="jobgroup"></input><p/>
	<label>状态: </label> <select name="jobstatus" id="jobstatus">
			    <option value="1">1</option>
			    <option value="0">0</option>
	         </select><p/>
	<label>运行状态: </label> <select name="jobpause" id="jobpause">
			    <option value="1">1</option>
			    <option value="0">0</option>
	         </select><p/>
	 <label>cron表达式:</label> <input type="text" name="cronexpression" id="cronexpression"></input><p/>
	 <label>描述:</label> <input type="text" name="description" id="description"></input><p/>
	 <label>是否并发:</label> <select name="isconcurrent" id="isconcurrent">
			    <option value="1">1</option>
			    <option value="0">0</option>
	         </select><p/>
	 <label>类路径:</label> <input type="text" name="beanclass" id="beanclass"></input><p/>
	 <label>spring id:</label> <input type="text" name="springid" id="springid"></input><p/>
	 <label>方法名:</label><input type="text" name="methodname" id="methodname"></input><p/>
	 <input type="button"  onclick="add()" value="提交" /><p/>
	</form>
	<table border="1" rules="all" >
		<thead>
			<tr>
				<td>id</td>
				<td>name</td>
				<td>group</td>
				<td>状态</td>
				<td>cron表达式</td>
				<td>描述</td>
				<td>是否并发</td>
				<td>类路径</td>
				<td>spring id</td>
				<td>方法名</td>
				<td>操作</td>
				<td>操作</td>
				<td>操作</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="job" items="${requestScope.taskList}">
				<tr>
					<td>${job.jobid }</td>
					<td>${job.jobname }</td>
					<td>${job.jobgroup }</td>
					<td>
					     <c:choose>
							<c:when test="${job.jobstatus=='1' }">
								<a href="javascript:;"
										onclick="changeJobStatus('${job.jobid}','stop')">停止</a>
								</c:when>
							<c:otherwise>
								<a href="javascript:;"
										onclick="changeJobStatus('${job.jobid}','start')">开启</a>
							</c:otherwise>
						</c:choose>
					</td>
					<td>${job.cronexpression }</td>
					<td>${job.description }</td>
					<td>${job.isconcurrent }</td>
					<td>${job.beanclass }</td>
					<td>${job.springid }</td>
					<td>${job.methodname }</td>
					<td><a href="javascript:;" onclick="updateCron('${job.jobid}')">更新cron</a></td>
					<td>
					     <c:choose>
							<c:when test="${job.jobpause =='1' }">
								<a href="javascript:;"
										onclick="changeJobPause('${job.jobid}','pause')">暂停</a>
								</c:when>
							<c:otherwise>
								<a href="javascript:;" 
										onclick="changeJobPause('${job.jobid}','reusme')">继续</a>
							</c:otherwise>
						</c:choose>
					</td>
					<td><a href="javascript:;" onclick="runAJobNow('${job.jobid}')">马上执行</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<br/>
备注：
   <p>是否并发: 0表示不能并发，1表示可以并发。不能并发是单任务的执行周期少于间隔周期时，等任务执行完再进行下一个任务。可以并发可能几个任务同时执行</p>
   <p>状态: 1表示任务正在运行，添加任务，0表示停止删除任务，任务是不可以恢复的。</p>
   <p>运行状态:1表示运行，0表示暂停，任务是可以恢复的</p>
</body>
</html>