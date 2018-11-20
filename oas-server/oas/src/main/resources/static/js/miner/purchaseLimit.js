document.write("<script language=javascript src='/js/miner/purchaseLimitTable.js'></script>");

var check1;
var check2;
var check3;
var check4;
var check5;
var check6;

$(function() {
	
	initminerLimitGrid();
	
	$('#userEd1').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		//maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime = $("#QstartTime").val();
		$('#userEd2').datetimepicker('minDate',startTime);
	 });

	$('#userEd2').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime = $("#QstartTime").val();
		$('#userEd2').datetimepicker('minDate',startTime);
	 });
	
	$('#userEd3').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		//maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime").val();
		$('#userEd4').datetimepicker('minDate',startTime);
	 });

	$('#userEd4').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime").val();
		$('#userEd4').datetimepicker('minDate',startTime);
	 });
	
});


function checkQlimitMiner(){
	
	var QlimitMiner = $("#QlimitMiner").val();
	
	if(/^[1-9]\d*$/.test(QlimitMiner)){
		$("#msg_QlimitMiner").html("输入符合要求");
		$("#msg_QlimitMiner").css("color","green");
		check1 = 1;
		return check1;
	}else{
		$("#msg_QlimitMiner").html("请输入正整数！");
		$("#msg_QlimitMiner").css("color","red");
		check1 = 0;
		return check1;
	}
}

function checkQstartTime(){
	var QstartTime = $("#QstartTime").val();
	
	if(QstartTime.length!=0){
		$("#msg_QstartTime").html("输入符合要求");
		$("#msg_QstartTime").css("color","green");
		check2 = 1;
		return check2;
	}else{
		$("#msg_QstartTime").html("请输入开始时间！");
		$("#msg_QstartTime").css("color","red");
		check2 = 0;
		return check2;
	}
}

function checkQendTime(){
	var QstartTime = $("#QendTime").val();
	
	if(QstartTime.length!=0){
		$("#msg_QendTime").html("输入符合要求");
		$("#msg_QendTime").css("color","green");
		check3 = 1;
		return check3;
	}else{
		$("#msg_QendTime").html("请输入结束时间！");
		$("#msg_QendTime").css("color","red");
		check3 = 0;
		return check3;
	}
}

function checkLimitMiner(){
	var limitMiner = $("#limitMiner").val();
	
	if(/^[1-9]\d*$/.test(limitMiner)){
		$("#msg_limitMiner").html("输入符合要求");
		$("#msg_limitMiner").css("color","green");
		check4 = 1;
		return check4;
	}else{
		$("#msg_limitMiner").html("请输入正整数！");
		$("#msg_limitMiner").css("color","red");
		check4 = 0;
		return check4;
	}
}

function checkStartTime(){
	var startTime = $("#startTime").val();
	
	if(startTime.length!=0){
		$("#msg_startTime").html("输入符合要求");
		$("#msg_startTime").css("color","green");
		check5 = 1;
		return check5;
	}else{
		$("#msg_startTime").html("请输入开始时间！");
		$("#msg_startTime").css("color","red");
		check5 = 0;
		return check5;
	}
}

function checkEndTime(){
	var endTime = $("#endTime").val();
	
	if(endTime.length!=0){
		$("#msg_endTime").html("输入符合要求");
		$("#msg_endTime").css("color","green");
		check6 = 1;
		return check6;
	}else{
		$("#msg_endTime").html("请输入结束时间！");
		$("#msg_endTime").css("color","red");
		check6 = 0;
		return check6;
	}
}

//单条矿机配置限制
function audit(){
	checkQlimitMiner();
	checkQstartTime();
	checkQendTime();
	if(check1==1 && check2==1 && check3==1){
		
		var uuid = $("#uuid").val();	
		var array = new Array();
		array[0] = uuid;
		
		var limit = $("#QlimitMiner").val();
		var startTime = $("#QstartTime").val();
		var endTime = $("#QendTime").val();
		var data={
			"uuids":array,
			"restriction":limit,
			"startTime":startTime,
			"endTime":endTime,
			}

		$.ajax({		
			url: "/api/v1/miner/updateUserMinerInfo",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data:JSON.stringify(data),
			processData : false,
			async : false,

			success: function(res) {
				if(res.code==0){
					document.getElementById("tipContent").innerText="限制过程完成";
					$("#Tip").modal('show');
					$("#miner1").attr("data-dismiss","modal");
					var pageNumber = $("#minerLimitGrid").bootstrapTable('getOptions').pageNumber;
					$("#minerLimitGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
					
					//$("#limitGrid").bootstrapTable('removeByUniqueId', uuid);
					refresh();
					
				}else{
					document.getElementById("tipContent").innerText=res.message;
					$("#Tip").modal('show');
					$("#miner1").attr("data-dismiss","modal");
					var pageNumber = $("#minerLimitGrid").bootstrapTable('getOptions').pageNumber;
					$("#minerLimitGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
					
					refresh();
				}		
				
			}, 
			error: function(){
				document.getElementById("tipContent").innerText="限制过程发生错误";
				$("#Tip").modal('show');
				$("#miner1").attr("data-dismiss","modal");
				refresh();
			}
		}); 
	}else{
		alert("请确认输入内容是否符合要求！");
		$("#miner1").removeAttr("data-dismiss");
	}
	
}

//单个取消矿机限制
function cancelLimit(){
	
	var uuid = $("#cancelId").val();	
	var array = new Array();
	array[0] = uuid;
	
	var data={
		"uuids":array
		}

	$.ajax({		
		url: "/api/v1/miner/updateUserMinerInfo",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			if(res.code==0){
				document.getElementById("tipContent").innerText="取消限制过程完成";
				$("#Tip").modal('show');
				
				var pageNumber = $("#minerLimitGrid").bootstrapTable('getOptions').pageNumber;
				$("#minerLimitGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页				
				
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
				
			}		
			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="取消限制过程发生错误";
			$("#Tip").modal('show');
		}
	}); 
}

function cancelManyLimit(){
	
	var array1 = new Array();	
	for(var i=0;i<array.length;i++){
		array1[i] = array[i].uuid;
	}
	if(array1.length==0){
		alert("请通过勾选复选框选择用户矿机配置之后，再执行此操作！");
	} else{ 
		
		$("#Tip2").modal('show');
	}
}


//批量取消矿机限制
function cancelMinerLimit(){
	
	var array3 = new Array();	
	for(var i=0;i<array.length;i++){
		array3[i] = array[i].uuid;
	}
	//alert(JSON.stringify(array3));
	
	var data={
		"uuids":array3
		}

	$.ajax({		
		url: "/api/v1/miner/updateUserMinerInfo",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			if(res.code==0){
				document.getElementById("tipContent").innerText="批量取消限制过程完成";
				$("#Tip").modal('show');
				
				initminerLimitGrid();
				array = [];	
				
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
				
				initminerLimitGrid();
				array = [];
				
			}		
			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="批量取消限制过程发生错误";
			$("#Tip").modal('show');
		}
	}); 
}

function refresh(){
	$("#QlimitMiner").val("");
	$("#QstartTime").val("");
	$("#QendTime").val("");
	$("#msg_QlimitMiner").html("");
	$("#msg_QstartTime").html("");
	$("#msg_QendTime").html("");
}

function refresh1(){
	$("#limitMiner").val("");
	$("#startTime").val("");
	$("#endTime").val("");
	$("#msg_limitMiner").html("");
	$("#msg_startTime").html("");
	$("#msg_endTime").html("");
}

function deleteById(uuid){
	
	$("#limitGrid").bootstrapTable('removeByUniqueId', uuid);

	var boxes = document.getElementsByName("btSelectItem");
    var box = document.getElementsByName("btSelectAll");    
	var rows=$("#minerLimitGrid").bootstrapTable('getData');//获取表格中当页的数据	
	//alert(JSON.stringify(rows.length));

	for(var i=0;i<rows.length;i++){
		if(rows[i].uuid==uuid)
		{
			boxes[i].checked = false;
			box[0].checked=false;
		}
	}
}
