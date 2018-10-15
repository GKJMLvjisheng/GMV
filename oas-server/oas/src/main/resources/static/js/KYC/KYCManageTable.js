//提币请求审核创建bootstrapTable

function initKYCGrid(data) {	

	$("#KYCGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色
		uniqueId:"uuid",//Indicate an unique identifier for each row
		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		toolbar:"#toolbar",//工具栏
		sortName: 'ID', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		data:data,
		columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			hidden:true, 
			formatter: function (value, row, index) {  
				return index+1;  
				}  
			},
		{  
			title: '用户名',  
			field: 'userName',
			align: 'center',
			valign: 'middle',
			width:  '80px',
			},
		{
			title : "身份证正面",
			field : "frontOfPhoto",
			align: 'center',
			valign: 'middle',
			formatter: picturePath
		},
			{
			title : "身份证反面",
			field : "backOfPhoto",
			align: 'center',
			valign: 'middle',
			formatter: picturePath
		}, {
			title : "手持身份证和账号",
			field : "holdInHand",
			align: 'center',
			valign: 'middle',
			formatter: picturePath
		},{
			title : "姓名",
			field : "userIdentityName",
			align: 'center',
			valign: 'middle',
			width:  '80px',
		}, {
			title : "身份证号",
			field : "userIdentityNumber",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},{
			title : "批注",
			field : "remark",
			align: 'center',
			valign: 'middle',
			width:  '90px',
		},
		{
			title : "申请时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '91px',
		},
		{
			title : "操作",
			field : "userName",
			align: 'center',
			valign: 'middle',
			width:  '40px',
			formatter: actionFormatter1
		},
		{
			title : "审核状态",
			field : "verifyStatus",
			align: 'center',
			valign: 'middle',
			//width:  '80px',
			formatter: actionFormatter		
		}],		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function picturePath(value, row, index) {
	var result = "";
	var id = row.uuid;
	var picturePath = value;
    result += "<a href='javascript:;' onclick=\"addMessage('" + id + "','" + picturePath + "')\" ><img src="+picturePath+" width='120px' height='80'></a>";
    			//"<span>"+picturePath+"</span>";      
    return result;
}

function actionFormatter(value, row, index) {
	var status = value;
	var id = row.uuid;
	var result = "";
	if(status==1){
		result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"agree('" + id + "')\"><span style='font-size:14px;'>批准</span></a>";
		result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"reject('" + id + "')\"><span style='font-size:14px;'>拒绝</span></a>";
		return result;
	}else if(status==2){
		result += "<span>已通过</span>";      
		return result;
	}else if(status==3){
		result += "<span>未通过</span>";      
	return result;
	}       
}

function actionFormatter1(value, row, index) {
	var id = value;
	var result = "";
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"viewUser('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";      
	return result;
}

function addMessage(id,picturePath){		
	$('#agId').val(id);
	var rows=$("#KYCGrid").bootstrapTable('getRowByUniqueId', id);
	$('#agStatus').val(rows.verifyStatus);
	$('#name').val(rows.userIdentityName);
	$('#card').val(rows.userIdentityNumber);
	document.getElementById('image').src = picturePath;	
	$("#addNCModal").modal("show");
}

function reject(id){
	$('#reId').val(id);
	$("#postilModal").modal("show");
}

function viewUser(id){	
	var formData = new FormData();
	formData.append("name", id);
	$.ajax({
	url:"/api/v1/userCenter/inquireTradeRecordUserInfo",
	data:formData,
	//contentType : 'application/json;charset=utf8',
	dataType: 'json',
	type: 'post',
	cache: false,		
	processData : false,
	contentType : false,
	async:false,

	success:function(res){				
		if(res.code==0){	
		var rows = res.data;			
		$('#Qname').val(rows.name);
		$('#Qnickname').val(rows.nickname);
		$('#Qgender').val(rows.gender);
		$('#Qbirthday').val(rows.birthday);
		$('#Qmobile').val(rows.mobile);
		$('#Qemail').val(rows.email);
		$('#Qaddress').val(rows.address);
		$('#QinviteCode').val(rows.inviteCode);
		$("#queryUserModal").modal("show");
		}

		else{
			alert("查询失败1");
			}						
	},

	error:function(){
		alert("查询失败2");
	},
	});					
}
