/**
 * 
 */
//新闻管理界面创建bootstrapTable
document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");

var check3=1;
var check2=1;
$(function () {
	
    $('#time').datetimepicker({

        format: 'YYYY-MM',
        locale: moment.locale('zh-cn'),
        //autoclose: true,
       // todayBtn: true,
    });
});
//主界面用户表格回显
$(function() {

	//初始加载	
	parameterReady();
	
	
});

function parameterReady(){
	
	
    $('#parameterGrid').bootstrapTable('destroy');
	var data;
	 $.ajax({
		
		url: "/api/v1/miner/selectSystemParameter",
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
	 initParameterGrid(data);
}

function checkEparameterValue(){
	
	var parameterValue=$("#EparameterValue").val();
	var num=parameterValue.split('.');
	if(validate(parameterValue)&&0<parameterValue&&parameterValue<=1){
		
		check2=1;
		$("#msg_EparameterValue").html("");
		 //$("#msg_baseValue").css("color", "red");
	}else{
		
		 $("#msg_EparameterValue").html("请输入大于0小于等于1的数值");
		 $("#msg_EparameterValue").css("color", "red");
		check2=0;
	}
	if(num[1].length>6){
		$("#msg_EparameterValue").html("矿机参数值精度为6");
		 $("#msg_EparameterValue").css("color", "red");
		 check2=0;
	}
}
function checkParameterValue(){
	var parameterValue=$("#parameterValue").val();
	var num=parameterValue.split('.');
	console.log(JSON.stringify(num))
	if(validate(parameterValue)&&0<parameterValue&&parameterValue<=1){
		
		check3=1;
		$("#msg_parameterValue").html("");
		 //$("#msg_baseValue").css("color", "red");
	}else{
		
		 $("#msg_parameterValue").html("请输入大于0小于等于1的数值");
		 $("#msg_parameterValue").css("color", "red");
		check3=0;
	}
	if(num[1]){
	if(num[1].length>6){
		$("#msg_parameterValue").html("矿机参数值精度为6");
		 $("#msg_parameterValue").css("color", "red");
		 check3=0;
	}
	}
}


//正数
function validate(num)
{

	var reg = /^\d+(?=\.{0,1}\d+$|$)/;//包括0不包括“”
  //var reg=/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;//不包括0
  if(reg.test(num)){
	 
	  return true;}
  return false ;  
}
//正整数
function validateInter(num)
{

	//var reg = /^[0-9]*[1-9][0-9]*$/;//不包括0和“”
	var reg =/^[+]{0,1}(\d+)$/;//包括0不包括“”
  //var reg =/^[1-9]\d*$/;//不包括0
  if(reg.test(num)){
	 
	  return true;}
  return false ;  
}

//新增活动
function addParameter(){
	

	if($("#parameterValue").val()==="")
		{
		alert("参数值不能为空");
		return;
		}
	if(check3==0)
	{
		alert("请配置正确的参数值");
		return;
		}
	if($("input[name='time']").val()==="")
	{
	alert("有效时间不能为空");
	return;
	}
	var data={"parameterValue":$("#parameterValue").val(),
				"parameterName":$("#parameterName").val(),
				"period": $("input[name='time']").val()	
			}
	$.ajax({
		url:"/api/v1/miner/addSystemParameter",
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
				$("#addParameterModal").modal('hide');}
				else{
				document.getElementById("tipContent").innerText="新增失败";
				$("#Tip").modal('show');
				$("#addParameterModal").modal('hide');
				
				 
				//$("#newsGrid").bootstrapTable('refresh');	
				 }						
		},
		error:function(){
			document.getElementById("tipContent").innerText="新增失败";
			$("#Tip").modal('show');
			$("#addParameterModal").modal('hide');

		},
	});
	check3=1;
	resetAddModal();
	parameterReady();
}


//点击取消后清空表单中已写信息
function resetAddModal(){
	//document.getElementById("addActivityForm").reset();
	//document.getElementById("updateActivityRewardForm").reset();
	$("#addParameterModal").find('textarea,input[type=text],select').each(function() {
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
	$("#updateParameterModal").find('textarea,input[type=text],select,span').each(function() {
		$(this).val('');
		$(this).html('');
});
}

function initParameterGrid(data) {	

	$("#parameterGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:5,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"uuid",//Indicate an unique identifier for each row

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
            
        },{

			title : "参数名称",

			field : "parameterName",
			align: 'center',
			valign: 'middle',
			//width:  '200px',

		},
			{

			title : "参数值",

			field : "parameterValue",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		},
		{

			title : "有效期",

			field : "period",
			align: 'center',
			valign: 'middle',
			//width:  '90px',

		},
		
		{

			title : "更新时间",

			field : "created",
			align: 'center',
			valign: 'middle',
			//width:  '90px',

		},
		{

			title : " 操作",
			
			field : "uuid",
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
    result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"editParameterById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
    //result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteParameterById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

    return result;
}
function deleteParameterById(id){
	Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"uuid":id,};
	//alert(JSON.stringify(data));
	$.ajax({

		url:"/api/v1/miner/deleteSystemParameter",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#parameterGrid").bootstrapTable('removeByUniqueId', id);
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
function editParameterById(id){
	 var rows=$("#parameterGrid").bootstrapTable('getRowByUniqueId', id); 
	    $("#EparameterId").val(rows.uuid);
	    $("#EparameterName").val(rows.parameterName); 
	    
		$("#EparameterValue").val(rows.parameterValue);
		$("#Etime").val(rows.period);
		$("#updateParameterModal").modal("show");
}
function updateparameter(){
	if(!$("#EparameterValue").val()){
		alert("参数值不能为空!");
		return;
	}
	if(check2==0){
		alert("请给参数值配置正确参数!");
		return;
	}
	var uuid=$("#EparameterId").val();  
	
    var parameterValue=$("#EparameterValue").val();
	
	var data={
			  "uuid":uuid,
			  "parameterValue":parameterValue
			};
		$.ajax({
			url:"/api/v1/miner/updatedSystemParameter",
			data: JSON.stringify(data),
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			async : false,
			success:function(res){	
				//alert(JSON.stringify(res));
					if(res.code==0)
					{
						document.getElementById("tipContent").innerText="修改成功";
						$("#Tip").modal('show');
						$("#updateParameterModal").modal('hide');}
						else{
						document.getElementById("tipContent").innerText="修改失败";
						$("#Tip").modal('show');
						$("#updateParameterModal").modal('hide');
				    	 }					
					 
			},
			error:function(res){
				alert(res.message);

			},
		});
		 
		 check2=1;
		
		parameterReady();
		resteUpdate();
}



