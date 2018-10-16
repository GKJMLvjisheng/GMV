/**
 * 
 */
//新闻管理界面创建bootstrapTable
document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");
//document.write("<script language=javascript src='js/qrcode.js'></script>");
var check1=1;
var check2=1;
var check3=1;
//主界面用户表格回显
$(function() {

	//初始加载	
	rewardReady();
	//versionCode.setAttribute("maxlength",40);
	
	
});

function rewardReady(){
	
	
    $('#rewardGrid').bootstrapTable('destroy');
	var data;
	 $.ajax({
		
		url: "/api/v1/activityConfig/selectAllReward",
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
	 initRewardGrid(data);
}

function checkRewardName() {
	var abstract = $("#rewardName").val(); 
	var len=abstract.length;
	//alert(len);
	if (len>10) {
		$("#msg_rewardName").html("输入版本状态长度为10个字符，已达上限");
        $("#msg_rewardName").css("color", "red");
	}
	else {
		$("#msg_rewardName").html("");
        $("#msg_rewardName").css("color", "green");
	}
}
//检查版本状态
function checkRewardDescription() {
	var abstract = $("#rewardDescription").val(); 
	var len=abstract.length;
	//alert(len);
	if (len>50) {
		$("#msg_rewardDescription").html("输入版本状态长度为50个字符，已达上限");
        $("#msg_rewardDescription").css("color", "red");
	}
	else {
		$("#msg_rewardDescription").html("");
        $("#msg_rewardDescription").css("color", "green");
	}
}
function checkErewardDescription(){
	var abstract = $("#ErewardDescription").val(); 
	var len=abstract.length;
	//alert(len);
	if (len>50) {
		$("#msg_ErewardDescription").html("输入版本状态长度为50个字符，已达上限");
        $("#msg_ErewardDescription").css("color", "red");
	}
	else {
		$("#msg_ErewardDescription").html("");
        $("#msg_ErewardDescription").css("color", "green");
	}
}
//新增版本
function addReward(){
	
    
	if($("#rewardName").val()==="")
		{
		alert("奖励名称不能为空");
		return;
		}

	var rewardName=$("#rewardName").val();
	var rewardDescription=$("#rewardDescription").val();
	var data={
			"rewardName":rewardName,
			"rewardDescription":rewardDescription
	};
	$.ajax({
		url:"/api/v1/activityConfig/addRewardType",
		data:JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		async:false,
		success:function(res){	
			//alert(JSON.stringify(res));
				if(res.code==0)
				{document.getElementById("tipContent").innerText="新增成功";
				$("#Tip").modal('show');
				$("#addRewardModal").modal('hide');}
				else{
				document.getElementById("tipContent").innerText="新增失败";
				$("#Tip").modal('show');
				$("#addRewardModal").modal('hide');				

				 }						
		},
		error:function(){
			document.getElementById("tipContent").innerText="新增失败";
			$("#Tip").modal('show');
			$("#addRewardModal").modal('hide');

		},
	});
	
	resetAddModal();
	rewardReady();
}


//点击取消后清空表单中已写信息
function resetAddModal(){
	document.getElementById("addRewardForm").reset();
	 $("#addRewardForm").find('input[type=text],textarea,input[type=file],select,span').each(function() {
		          $(this).val('');
		          $(this).html('');
		      });
	 //$("input[type=radio]").prop("checked",false);
	 //$("span").html("");
//	 $("#updateVersionForm").find('input[type=text],select,input[type=file],span').each(function() {
//         $(this).val('');
//     });
	
	//location.reload();
}
function resteUpdate(){
	$("#updateRewardForm").find('input[type=text],textarea,input[type=file],select').each(function() {
        $(this).val('');
        $(this).html('');
    });
//	$("input[type=radio]").prop("checked",false);
//	$("span").html("");
}
//修改没写完
function updateReward(){
	var rewardCode=$("#ErewardId").val();
	var rewardDescription=$("#ErewardDescription").val();
	var data={"rewardDescription":rewardDescription,
		"rewardCode":rewardCode}
	
	$.ajax({
		url:"/api/v1/userCenter/updateApp",
		data:JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		async:false,

		success:function(res){	
			//alert(JSON.stringify(res));
			
			if(res.code==0){
				alert("修改成功");
				
				versionReady();
			    //newsReady();
			}
			else{
				alert("修改失败");
				}			
			
		},
		error:function(){
			alert("修改失败");

		},
	});
	resteUpdate();
	
	}

function initRewardGrid(data) {	

	$("#rewardGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"rewardCode",//Indicate an unique identifier for each row

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

			title : "奖励名称",

			field : "rewardName",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		}, {

			title : "奖励描述",

			field : "rewardDescription",
			align: 'center',
			valign: 'middle',
			//width:  '200px',

		},
		
		{

			title : "创建时间",

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
        
//        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"editRewardById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteRewardById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

        return result;
}
 

function editRewardById(id){
    
    //获取选中行的数据
    var rows=$("#rewardGrid").bootstrapTable('getRowByUniqueId', id);
	$('#ErewardId').val(rows.rewardCode);  
    $('#ErewardDescription').val(rows.rewardDescription);
    
	
    $("#updateRewardModal").modal("show");           
  }


function deleteRewardById(id)
{ 
	 Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"rewardCode":id};
	//alert(JSON.stringify(data));
	$.ajax({

		url:"/api/v1/activityConfig/deleteReward",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#rewardGrid").bootstrapTable('removeByUniqueId', id);
				alert("删除成功！");
			}
			else{
				alert("删除失败");
				}
			},
	
	     error:function(){
				alert("删除失败！");
			}
	});
});
}


