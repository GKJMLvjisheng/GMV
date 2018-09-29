/**
 * 
 */
//新闻管理界面创建bootstrapTable
document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");

var check1=1;
var check2=1;
var check3=1;
var check4=1;
var check5=1;
//主界面用户表格回显
$(function() {

	//初始加载	
	activityReady();
	
	
	
});

function activityReady(){
	
	
    $('#activityGrid').bootstrapTable('destroy');
	var data;
	 $.ajax({
		
		url: "/api/v1/activityConfig/selectAllActivity",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		async : false,
		success: function(res) {
		//alert(JSON.stringify(res));
		if(res.code==0)
			{data=res.data;}
		
		else{alert("回显失败！");}
			
		}, 
		error: function(){
			alert("回显失败！")
		}
		}); 
	 initActivityGrid(data);
}
$('#rewardName').on('change',function(){
    //获取对应值--后期作为类选择器
    var thisVal = $(this).val();
   if(thisVal=="请选择"){
	   return;
   }
   var data={"rewardCode":thisVal,
		   	 "sourceCode":$("#activitySoureId").val()};
     $.ajax({
		   type: 'post',
		   url: '/api/v1/activityConfig/inquireRewardByRewardCode',
		   data: JSON.stringify(data),
		   contentType : 'application/json;charset=utf8',
		   dataType: 'json',
		   cache: false,
		   async : false,
		   success: function (res) {
			  
		     if (res.code == 0) {
		    	 check1=1; 
		    	
             }else {
            	 check1=0;
		    	 alert("该活动已有此奖励!");
		     }
		   },
		   error: function (res) {
			  alert("option错误"+JSON.stringify(res));
		   },
		  
		  });
})
function checkBaseValue(){
	
	var baseValue=$("#baseValue").val();
	if(validate(baseValue)){
		check2=1;
		$("#msg_baseValue").html("");
		 //$("#msg_baseValue").css("color", "red");
	}else{
		 $("#msg_baseValue").html("请输入大于0的数值");
		 $("#msg_baseValue").css("color", "red");
		check2=0;
	}
}
function checkIncreaseSpeed(){
	var increaseSpeed=$("#increaseSpeed").val();
	if(validate(increaseSpeed)){
		check3=1;
		$("#msg_increaseSpeed").html("");
		 //$("#msg_baseValue").css("color", "red");
	}else{
		 $("#msg_increaseSpeed").html("请输入大于0的数值");
		 $("#msg_increaseSpeed").css("color", "red");
		check3=0;
	}
}
function checkMaxValue(){
	var maxValue=$("#maxValue").val();
	if(validate(maxValue)){
		check4=1;
		$("#msg_maxValue").html("");
		 //$("#msg_baseValue").css("color", "red");
	}else{
		 $("#msg_maxValue").html("请输入大于0的数值");
		 $("#msg_maxValue").css("color", "red");
		check4=0;
	}
}
function checkPeriod(){
	var period=$("#maxValue").val();
	if(validate(maxValue)){
		check4=1;
		$("#msg_period").html("");
		 //$("#msg_baseValue").css("color", "red");
	}else{
		 $("#msg_period").html("请输入大于0的数值");
		 $("#msg_period").css("color", "red");
		check4=0;
	}
}
function validate(num)
{

  var reg = /^[0-9]*[1-9][0-9]*$/;//不包括0和“”
	//var reg =/^[+]{0,1}(\d+)$/;//包括0不包括“”
  if(reg.test(num)){
	 
	  return true;}
  return false ;  
}

//新增活动
function addActivity(){
	
    
	if($("#sourceName").val()==="")
		{
		alert("活动名称不能为空");
		return;
		}

	
	var data={"sourceName":$("#sourceName").val(),
				"type":$("#sourceType").val()
			}
	$.ajax({
		url:"/api/v1/activityConfig/addActivity",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		async : false,
		success:function(res){	
			//alert(JSON.stringify(res));
				if(res.code==0)
				{document.getElementById("tipContent").innerText="新增成功";
				$("#Tip").modal('show');
				$("#addActivityModal").modal('hide');}
				else{
				document.getElementById("tipContent").innerText="新增失败";
				$("#Tip").modal('show');
				$("#addActivityModal").modal('hide');
				
				 
				//$("#newsGrid").bootstrapTable('refresh');	
				 }						
		},
		error:function(){
			document.getElementById("tipContent").innerText="新增失败";
			$("#Tip").modal('show');
			$("#addActivityModal").modal('hide');

		},
	});
	resetAddModal();
	activityReady();
}


//点击取消后清空表单中已写信息
function resetAddModal(){
	//document.getElementById("addActivityForm").reset();
	//document.getElementById("updateActivityRewardForm").reset();
	$("#addActivityForm").find('textarea,input[type=text],select,').each(function() {
        		$(this).val('');
        		$(this).html('');
    });
	
 }
//	 $("input[type=radio]").prop("checked",false);
//	 $("span").html("");
//	 $("#updateVersionForm").find('input[type=text],select,input[type=file],span').each(function() {
//         $(this).val('');
//     });
	
	//location.reload();

function resteAllotReward(){
	$("#allotRewardForm").find('textarea,input[type=text],select,span').each(function() {
        $(this).val('');
        $(this).html('');
      });
}

function resteUpdate(){
	$("#updateActivityRewardForm").find('textarea,input[type=text],select,span').each(function() {
		$(this).val('');
		$(this).html('');
});
}

function initActivityRewardGrid(data) {	

	$("#activityRewardGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:5,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"rewardCode",//Indicate an unique identifier for each row

		toolbar:"#rewardToolbar",//工具栏
		sortName: 'ID', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		data:data,
		columns : [{
            title: '序号',
            field: 'index',
            align: 'center',
			valign: 'middle',
            formatter: formatterIndex
            
        },{

			title : "奖励名称",

			field : "rewardName",
			align: 'center',
			valign: 'middle',
			//width:  '200px',

		},
			{

			title : "奖励基础值",

			field : "baseValue",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		},{

			title : "奖励增长速度",

			field : "increaseSpeed",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		}, 
		{

			title : "奖励增长速度单位",

			field : "increaseSpeedUnit",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		},
		{

			title : "奖励满值",

			field : "maxValue",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		},{

			title : "有效期",

			field : "period",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		},
		
		{

			title : "活动创建时间",

			field : "created",
			align: 'center',
			valign: 'middle',
			//width:  '90px',

		},
		{

			title : " 操作",
			
			field : "rewardCode",
			align: 'center',
			valign: 'middle',
			//width:  '90px',
			formatter: actionFormatterReward
		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}
function actionFormatterReward(value, row, index) {
    var id = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"editActivityRewardById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteActivityRewardById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

    return result;
}
function deleteActivityRewardById(id){
	Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"rewardCode":id,
		  "sourceCode":$("#activityId").val()};
	//alert(JSON.stringify(data));
	$.ajax({

		url:"/api/v1/activityConfig/deleteActivityReward",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#activityRewardGrid").bootstrapTable('removeByUniqueId', id);
				document.getElementById("tipContent").innerText="删除成功";
				$("#Tip").modal('show');
			}
			else{
				document.getElementById("tipContent").innerText="删除失败";
				$("#Tip").modal('show');
				}
			},
	
	     error:function(){
				alert("删除失败！");
			}
	});
});
}
function editActivityRewardById(id){
	 var rows=$("#activityRewardGrid").bootstrapTable('getRowByUniqueId', id);
	 alert(JSON.stringify(rows));
		$("#EActivitySoureId").val($('#activitySoureId').val());  
	    $("#EActivityRewardId").val(rows.rewardCode);
	    $("#ErewardName").val(rows.rewardName);  
		
		$("#EbaseValue").val(rows.baseValue);
		$("#EincreaseSpeed").val(rows.increaseSpeed);
		$("#EincreaseSpeedUnit").val(rows.increaseSpeedUnit);
		$("#EmaxValue").val(rows.maxValue);
		$("#Eperiod").val(rows.period);
		$("#updateActivityRewardModal").modal("show");
}
function updateActivityReward(){
	var sourceCode=$("#EActivitySoureId").val();  
	
    var rewardCode=$("#EActivityRewardId").val();
	
	var baseValue=$("#EbaseValue").val();
	var increaseSpeed=$("#EincreaseSpeed").val();
	var increaseSpeedUnit=$("#EincreaseSpeedUnit").val();
	var maxValue=$("#EmaxValue").val();
	var period=$("#Eperiod").val();
	var data={
			  "baseValue":baseValue,
			  "increaseSpeed":increaseSpeed,
			  "increaseSpeedUnit": increaseSpeedUnit,
			  "maxValue": maxValue,
			  "period":period,
			  "rewardCode": rewardCode,
			  "sourceCode":sourceCode
			};
		$.ajax({
			url:"/api/v1/activityConfig/updateActivityRewardConfig",
			data: JSON.stringify(data),
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			async : false,
			success:function(res){	
				alert(JSON.stringify(res));
					if(res.code==0)
					{
						document.getElementById("tipContent").innerText="修改成功";
						$("#Tip").modal('show');
						$("#updateActivityRewardModal").modal('hide');}
						else{
						document.getElementById("tipContent").innerText="配置失败";
						$("#Tip").modal('show');
						$("#updateActivityRewardModal").modal('hide');
				    	 }					
					 
			},
			error:function(res){
				alert(res.message);

			},
		});
		activityRewardReady(sourceCode);
		resteUpdate();
}
function allotReward(){
	//初始加载
	
	var sourceCode=$('#activityId').val();

	$('#activitySoureId').val(sourceCode);
	
$.ajax({
	url:"/api/v1/activityConfig/selectAllReward",
	//data: JSON.stringify(data),
	contentType : 'application/json;charset=utf8',
	dataType: 'json',
	cache: false,
	type: 'post',
	async : false,
	success:function(res){	
		alert(JSON.stringify(res));
			if(res.code==0)
			{
		    	 var optionData=res.data;
		    	 
		    	 var len=optionData.length;
		    	 
		    	 
		    	 var selections = document.getElementById("rewardName");
		    	 //var string=res.data[];
		    	 for(var i =0;i<len;i++){
                     //设置下拉列表中的值的属性
                     var option = document.createElement("option");
                         option.value = optionData[i].rewardCode;
                        
                         option.text= optionData[i].rewardName;
                     //将option增加到下拉列表中。
                     selections.options.add(option);
			
			 
			//$("#newsGrid").bootstrapTable('refresh');	
		    	 }
		    }else{
				 alert("下拉选择回显失败")
				 }						
			 
	},
	error:function(res){
		alert(res.message);

	},
});
	
	$("#allotRewardModal").modal("show");
}
function addAllotReward(){
	$("#allot").attr("onclick","addAllotReward()");
	var rewardCode=$("#rewardName").val();
	if(rewardCode=="请选择"||check1==0){
		alert("请配置该活动没有的奖励!");
		return;
	}
	if(check2==0||check3==0||check4==0||check5==0){
		alert("请给奖励配置正确参数!");
		return;
	}
	//var param = $("#allotRewardForm").serializeArray();
	
	var rewardCode=$("#rewardName").val();
	var baseValue=$("#baseValue").val();
	var increaseSpeed=$("#increaseSpeed").val();
	var increaseSpeedUnit=$("#increaseSpeedUnit").val();
	var maxValue=$("#maxValue").val();
	var period=$("#period").val();
	var sourceCode=$("#activitySoureId").val();
	var data={
		  "baseValue":baseValue,
		  "increaseSpeed":increaseSpeed,
		  "increaseSpeedUnit": increaseSpeedUnit,
		  "maxValue": maxValue,
		  "period":period,
		  "rewardCode": rewardCode,
		  "sourceCode":sourceCode
		};
	$.ajax({
		url:"/api/v1/activityConfig/activityRewardConfig",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		async : false,
		success:function(res){	
			alert(JSON.stringify(res));
				if(res.code==0)
				{
					document.getElementById("tipContent").innerText="配置成功";
					$("#Tip").modal('show');
					$("#allotRewardModal").modal('hide');}
					else{
					document.getElementById("tipContent").innerText="配置失败";
					$("#Tip").modal('show');
					$("#allotRewardModal").modal('hide');
			    	 }					
				 
		},
		error:function(res){
			alert(res.message);

		},
	});
	resteAllotReward();
	activityRewardReady(sourceCode);
}

function initActivityGrid(data) {	

	$("#activityGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"sourceCode",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortName: 'ID', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		data:data,
		columns : [{
            title: '序号',
            field: 'index',
            align: 'center',
			valign: 'middle',
            formatter: formatterIndex
            
        },
			{

			title : "活动名称",

			field : "sourceName",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		}, {

			title : "活动类型",

			field : "type",
			align: 'center',
			valign: 'middle',
			//width:  '200px',

		},
		
		{

			title : "活动创建时间",

			field : "created",
			align: 'center',
			valign: 'middle',
			//width:  '90px',

		},
		{

			title : " 操作",
			
			field : "sourceCode",
			align: 'center',
			valign: 'middle',
			//width:  '90px',
			formatter: actionFormatter
		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function formatterIndex(value, row, index){
	   var value="";
    var pageSize=10;
  	
    //获取当前是第几页        
    var pageNumber=1;       
    //返回序号，注意index是从0开始的，所以要加上1         
     value=pageSize*(pageNumber-1)+index+1;
     return value;
}

function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"viewActivityById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-zoom-in'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"editActivityById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteActivityById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

        return result;
}
 
function viewActivityById(id){	

		//获取选中行的数据		
		var rows=$("#activityGrid").bootstrapTable('getRowByUniqueId', id);
		var sourceName=rows.sourceName;
		//var sourceCode=rows.sourceCode;
		  
			 
				var str1="活动";
				var str2="的奖励配置";
				var str3=sourceName;
				var str=str1+"【"+str3+"】"+str2;
				
				document.getElementById("activityModalLabel").innerHTML=(str);
				$('#activityId').val(id);                         
				activityRewardReady(id);
				
			 $("#activityRewardModal").modal("show"); 

}	

function activityRewardReady(id)
{
	$('#activityRewardGrid').bootstrapTable('destroy');
	var data={"sourceCode":id};
	alert(data);
	var data1;
	$.ajax({
		
		url: "/api/v1/activityConfig/selectAllActivityReward",
		contentType : 'application/json;charset=utf8',
		data: JSON.stringify(data),
		dataType: 'json',
		cache: false,
		type: 'post',
		async : false,
		success: function(res) {
		alert(JSON.stringify("111"+res));
		if(res.code==0)
			{data1=res.data;}
		
		else{alert("回显失败！");}
			
		}, 
		error: function(){
			alert("回显失败！")
		}
		}); 
	initActivityRewardGrid(data1);
}
function editActivityById(id){
    
    //获取选中行的数据
    var rows=$("#activityGrid").bootstrapTable('getRowByUniqueId', id);
	$('#EactivityId').val(rows.uuid);  
    $('#EversionCode').val(rows.versionCode);
    if(rows.versionStatus=="体验版")
    	{$("#EversionStatus1").prop("checked",true)}
    if(rows.versionStatus=="开发版")
    	{$("#EversionStatus2").prop("checked",true)}
    if(rows.versionStatus=="稳定版")
		{$("#EversionStatus3").prop("checked",true)}
    $('#EversionPath').val(rows.appUrl);
	//$('#EnewsUrl').val(rows.newsUrl);
	
	//$('#versionFile').val(rows.versionFile); 
//	file_input_obj=document.getElementById("EversionFile");
//	file_input_obj.outerHTML=file_input_obj.outerHTML.replace(/(value=111\").+\"/i,"$1\"11");
//	
//	versionCode.setAttribute("readonly", "readonly" );
    $("#updateVersionModal").modal("show");           
  }


function deleteActivityById(id)
{ 
	 Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"sourceCode":id};
	//alert(JSON.stringify(data));
	$.ajax({

		url:"/api/v1/activityConfig/deleteActivity",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#activityGrid").bootstrapTable('removeByUniqueId', id);
				document.getElementById("tipContent").innerText="删除成功";
				$("#Tip").modal('show');
			}
			else{
				document.getElementById("tipContent").innerText="删除失败";
				$("#Tip").modal('show');
				}
			},
	
	     error:function(){
				alert("删除失败！");
			}
	});
});
}



