/**
 * 页面加载后执行
 */
function repetRequest(){
  $.ajax({
	  url:"getQueueTable",
	  type:"GET",
	  success:function(result){
		 var json = eval('(' + result + ')'); 
		 // 销毁原来的table,删除子元素不删除他自己
		 $("#message").empty();
		 $.each(json,function(index,item){
             var tr =$("<tr></tr>");
             tr.append($("<td>"+item.id+"</td>"));
             tr.append($("<td>"+item.type+"</td>"));
             tr.append($("<td>"+item.status+"</td>"));
             tr.append($("<td>"+item.request+"</td>"));
             tr.append($("<td>"+item.entryDate+"</td>"));
             $("#message").append(tr);
         });
	  }
  });	
}
$(document).ready(function() { 
	//30秒执行一次
	setInterval("repetRequest()","1000");
}); 