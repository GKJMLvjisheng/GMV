
function initNormalGrid() {	
	
	$("#normalGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"uuid",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,
			
		columns : [{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle',  
		formatter: function (value, row, index) {  
			return index + 1;  
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
		{
			title : "昵称",
			field : "nickName",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			
		},
		{
			title : "手机",
			field : "mobile",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "邮箱",
			field : "email",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "IMEI",
			field : "status",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			
		},
			{
			title : "创建时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},{

			title : "操作",
			field : "uuid",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter
		}],		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}
	
function initTestGrid() {	

	$("#testGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"uuid",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,
			
		columns : [{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle',  
		formatter: function (value, row, index) {  
			return index + 1;  
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
		{
			title : "昵称",
			field : "nickName",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			
		},
		{
			title : "手机",
			field : "mobile",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "邮箱",
			field : "email",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "IMEI",
			field : "status",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			
		},
			{
			title : "创建时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},{

			title : "操作",
			field : "uuid",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter
		}],		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}


function initSystemGrid() {	

	$("#systemGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"uuid",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,
			
		columns : [{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle',  
		formatter: function (value, row, index) {  
			return index + 1;  
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
		{
			title : "昵称",
			field : "nickName",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			
		},
		{
			title : "手机",
			field : "mobile",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "邮箱",
			field : "email",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "IMEI",
			field : "status",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			
		},
			{
			title : "创建时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},{

			title : "查看",
			field : "uuid",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter1
		},{

			title : "操作",
			field : "uuid",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter2
		}],		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function actionFormatter1(value, row, index) {
    var id = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";      
    return result;
}

function actionFormatter2(value, row, index) {
	var id = row.uuid;
	var result = "";
	
	result += "<input type='radio' onclick=\"agree('" + id + "')\"' name='radio' id='agree' value='1'>批准 ";
	result += "<input type='radio' onclick=\"reject('" + id + "')\" name='radio' id='reject' value='2'>拒绝";
	return result;	
}

function ViewViewById(id){	
	var formData = new FormData();
	formData.append("name", id);
	$.ajax({
	url:"/api/v1/userCenter/inquireTradeRecordUserInfo",
	data:formData,
	contentType : 'application/json;charset=utf8',
	dataType: 'json',
	type: 'post',
	cache: false,		
	processData : false,
	contentType : false,
	async:false,

	success:function(res){				
		if(res.code==0){
		//alert(JSON.stringify(res));			
		var rows = res.data;			
		$('#QverifyStatus').val(rows.verifyStatus);
		$('#QuserIdentityName').val(rows.userIdentityName);
		$('#QuserIdentityNumber').val(rows.userIdentityNumber);
		$('#Qbirthday').val(rows.birthday);
		$('#Qmobile').val(rows.mobile);
		$('#Qemail').val(rows.email);
		$('#Qaddress').val(rows.address);
		$('#QinviteCode').val(rows.inviteCode);
		$("#queryEnergyModal").modal("show");
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