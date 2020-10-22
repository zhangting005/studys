function changeJobStatus(jobId, cmd){
	$.ajax({
		type : "POST",
		async : false,
		dataType : "JSON",
		cache : false,
		url : "changeJobStatus",
		data : {
			jobId : jobId,
			cmd : cmd
		},
		success : function(data) {
			if (data.flag) {
				location.reload();
			} else {
				alert(data.msg);
			}
		}
	});
}
function changeJobPause(jobId,cmd){
	$.ajax({
		type : "POST",
		async : false,
		dataType : "JSON",
		cache : false,
		url : "changeJobPause",
		data : {
			jobId : jobId,
			cmd : cmd
		},
		success : function(data) {
			if (data.flag) {
				location.reload();
			} else {
				alert(data.msg);
			}

		}//end-callback
	});//end-ajax
}
function runAJobNow(jobId){
		$.ajax({
			type : "POST",
			async : false,
			dataType : "JSON",
			cache : false,
			url : "runAJobNow",
			data : {
				jobId : jobId
			},
			success : function(data) {
				if (data.flag) {
					location.reload();
				} else {
					alert(data.msg);
				}

			} 
		});
}
function updateCron(jobId){
	var cron = prompt("输入cron表达式！", "");
	if (cron){
		$.ajax({
			type : "POST",
			async : false,
			dataType : "JSON",
			cache : false,
			url : "updateCron",
			data : {
				jobId : jobId,
				cron : cron
			},
			success : function(data) {
				if (data.flag) {
					location.reload();
				} else {
					alert(data.msg);
				}

			}//end-callback
		});//end-ajax
	}
}

function add() {
		$.ajax({
			type : "POST",
			async : false,
			dataType : "JSON",
			cache : false,
			url : "addJob",
			data : $("#addForm").serialize(),
			success : function(data) {
				if (data.flag) {
					location.reload();
				} else {
					alert(data.msg);
				}

			}//end-callback
		});//end-ajax
}