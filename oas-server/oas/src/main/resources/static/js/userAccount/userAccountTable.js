var pageSize;
var pageNum;
var Data;

function initDownUserGrid() {	
	$("#downUserGrid").bootstrapTable('destroy');
	$("#downUserGrid").bootstrapTable({
		url: '/api/v1/userCenter/inquireInvitedUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams,//请求服务器时所传的参数
		responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'created', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '145px',
				//visible: false,
			}],		

			clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams(params){
	var name = $("#downName").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,    
        name: name,    
       
    }
}
//请求成功方法
function responseHandler(res){
	//alert(JSON.stringify(res));
    var code = res.code;//在此做了错误代码的判断
    Data = res.data.rows;
    if(code != 0){
        alert("下级用户查看回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initNormalGrid() {	
	$("#normalGrid").bootstrapTable('destroy');
	$("#normalGrid").bootstrapTable({
		url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams1,//请求服务器时所传的参数
		responseHandler:responseHandler1,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
			{
				title : "昵称",
				field : "nickname",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				
			},
			{
				title : "手机",
				field : "mobile",
				align: 'center',
				valign: 'middle',
				width:  '110px',
			},
			{
				title : "邮箱",
				field : "email",
				align: 'center',
				valign: 'middle',
				width:  '98px',
			},
			{
				title : "IMEI",
				field : "imei",
				align: 'center',
				valign: 'middle',
				width:  '95px',
				
			},{

				title : "重置IMEI",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter1
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '100px',
				//visible: false,
			},{
				title : "账号状态",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter5
				
			},{

				title : "操作",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				formatter: actionFormatter7
			},{

				title : "查看下级用户",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter6
			},{

				title : "角色授权",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter3
			},{

				title : "切换账号状态",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter4
			}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: true,         
	});
}
	
//请求服务数据时所传参数
function queryParams1(params){
	var searchValue = $("#user1").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,      
        roleId: 2,
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler1(res){
	//alert(JSON.stringify(res));
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("正常账号回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initTestGrid() {	
	$("#testGrid").bootstrapTable('destroy');
	$("#testGrid").bootstrapTable({
		url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams2,//请求服务器时所传的参数
		responseHandler:responseHandler2,//请求数据成功后，渲染表格前的方法		
		dataField: "data",
		
		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
			{
				title : "昵称",
				field : "nickname",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				
			},
			{
				title : "手机",
				field : "mobile",
				align: 'center',
				valign: 'middle',
				width:  '110px',
			},
			{
				title : "邮箱",
				field : "email",
				align: 'center',
				valign: 'middle',
				width:  '98px',
			},
			{
				title : "IMEI",
				field : "imei",
				align: 'center',
				valign: 'middle',
				width:  '95px',
				
			},{

				title : "重置IMEI",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter1
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '100px',
				//visible: false,
			},{
				title : "账号状态",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter5
				
			},{

				title : "操作",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				formatter: actionFormatter7
			},{

				title : "查看下级用户",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter6
			},{

				title : "角色授权",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter3
			},{

				title : "切换账号状态",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter4
			}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams2(params){
	var searchValue = $("#user2").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,  
        //测试账号
        roleId: 3,  
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler2(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("测试账号回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};


function initSystemGrid() {	
	$("#systemGrid").bootstrapTable('destroy');
	$("#systemGrid").bootstrapTable({
		url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams3,//请求服务器时所传的参数
		responseHandler:responseHandler3,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
			{
				title : "昵称",
				field : "nickname",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				
			},
			{
				title : "手机",
				field : "mobile",
				align: 'center',
				valign: 'middle',
				width:  '120px',
			},
			{
				title : "邮箱",
				field : "email",
				align: 'center',
				valign: 'middle',
				width:  '140px',
			},
			{
				title : "IMEI",
				field : "imei",
				align: 'center',
				valign: 'middle',
				width:  '120px',
				
			},{

				title : "重置IMEI",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter1
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '145px',
				//visible: false,
			},{

				title : "查看用户信息",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter2
			}
//			,{
//
//				title : "角色授权",
//				field : "name",
//				align: 'center',
//				valign: 'middle',
//				width:  '80px',
//				formatter: actionFormatter3
//			},{
//
//				title : "账号状态",
//				field : "name",
//				align: 'center',
//				valign: 'middle',
//				width:  '80px',
//				formatter: actionFormatter4
//			}
			],		
//		  search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams3(params){
	var searchValue = $("#user3").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,
        //系统账号
        roleId: 1,
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler3(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("系统账号回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function actionFormatter1(value, row, index) {
	 var name = value;
	 var roleId = row.roleId;
	var result = "";	
	result += "<input type='radio' onclick=\"reset('" + name + "', '" + roleId + "')\"' name='radio' id='reset' value='1'> 重置";
	return result;	
}

function actionFormatter2(value, row, index) {
    var name = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + name + "')\" title='查看用户信息'><span class='glyphicon glyphicon-search'></span></a>";      
    return result;
}

function actionFormatter7(value, row, index) {
	//alert(JSON.stringify(row));
    var name = value;
    var code = row.inviteFrom;
    var roleId = row.roleId;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + name + "')\" title='查看用户信息'><span class='glyphicon glyphicon-search'></span></a>";   
    result += "<a href='javascript:;' id='viewCode' class='btn btn-xs green' onclick=\"ViewCode('" + name + "','" + roleId + "','" + code + "')\" title='修改上级用户邀请码'><span class='glyphicon glyphicon-pencil'></span></a>";      
    return result;
}

function actionFormatter3(value, row, index) {
	 var name = value;
	 var roleId = row.roleId;
	 var result = "";	
	
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"role('" + name + "', '" + roleId + "')\">角色授权</a>";
	return result;	
}

function actionFormatter4(value, row, index) {
	var name = value;
	var roleId = row.roleId;
	var result = "";
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"accountStatus('" + name + "', '" + roleId + "')\">切换账号状态</a>";
	return result;	
}

function actionFormatter5(value, row, index) {
    var result = "";
    //alert(JSON.stringify(value))
    if(value==1){
	result += "<span>激活</span>";      
    return result;
	}else if(value==0){
	result += "<span>禁用</span>";      
    return result;
	}    
}	

function actionFormatter6(value, row, index) {
	 var name = value;
	 var roleId = row.roleId;
	 var result = "";	
	
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"downUser('" + name + "', '" + roleId + "')\">查看下级用户</a>";
	return result;	
}	

function ViewViewById(name){	
	var data = {
		"name": name
	};
	$.ajax({
	url:"/api/v1/userCenter/inquireUserKYCInfo",
	contentType : 'application/json;charset=utf8',
	dataType: 'json',
	cache: false,
	type: 'post',
	data:JSON.stringify(data),
	processData : false,
	async : false,

	success:function(res){				
		if(res.code==0){
			//alert(JSON.stringify(res));			
			var rows = res.data;
			var verifyStatus = rows.verifyStatus;
			var supriorUserInviteCode = rows.supriorUserInviteCode;
			if(verifyStatus==0){
				var status = "未认证";
				document.getElementById("userIdentityName1").style.display="none";
				document.getElementById("userIdentityNumber1").style.display="none";
				document.getElementById("remark1").style.display="none";
			}else if(verifyStatus==1){
				var status = "未审核";
				document.getElementById("userIdentityName1").style.display="none";
				document.getElementById("userIdentityNumber1").style.display="none";
				document.getElementById("remark1").style.display="none";
			}else if(verifyStatus==2){
				var status = "已通过";
				document.getElementById("userIdentityName1").style.display="block";
				document.getElementById("userIdentityNumber1").style.display="block";
				document.getElementById("remark1").style.display="none";
			}else {
				var status = "未通过";
				document.getElementById("userIdentityName1").style.display="none";
				document.getElementById("userIdentityNumber1").style.display="none";
				document.getElementById("remark1").style.display="block";
			}			
			
			$('#Qname').val(rows.name);
			$('#Qnickname').val(rows.nickname);
			$('#Qgender').val(rows.gender);
			$('#Qbirthday').val(rows.birthday);
			$('#Qmobile').val(rows.mobile);
			$('#Qemail').val(rows.email);
			$('#Qaddress').val(rows.address);
			$('#QinviteCode').val(rows.inviteCode);
			
			$('#QverifyStatus').val(status);				
			$('#QuserIdentityName').val(rows.userIdentityName);
			$('#QuserIdentityNumber').val(rows.userIdentityNumber);
			$('#Qremark').val(rows.remark);	
			
			$('#QupName').val(rows.supriorUserName);	
			$('#Qcode').val(supriorUserInviteCode);				
		
			$("#queryModal").modal("show");
		}

		else{
			alert("查询失败，错误码："+res.code);
			}						
	},

	error:function(){
		alert("查询过程发生错误！");
	},
	});					
}