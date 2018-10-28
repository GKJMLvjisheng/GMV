
//提币请求审核创建bootstrapTable
var pageSize;
var pageNum;

function initRequestAuditGrid() {	
	$('#requestAuditGrid').bootstrapTable('destroy');
	$("#requestAuditGrid").bootstrapTable({
		url: "/api/v1/userWallet/getWithdrawList",
		contentType : "application/json",
		dataType:"json",
		method:"post",
		pagination:true,//显示分页条：页码，条数等
		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"server",//在服务器分页
		dataField: "withdrawData",
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		queryParams:queryParams,
		responseHandler:responseHandler,
		
		toolbar:"#toolbar",//工具栏
		
		sortable: true,//是否启用排序
		sortName: "created", // 要排序的字段
	    sortOrder: "desc", // 排序规则	    	    
	    
		columns : [{  
		title: 'uuid',  
		field: 'uuid',
		align: 'center',
		valign: 'middle',
		//class:'uu_style', 
		visible: false, 
		},
		
		{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle',  
			width:  '80px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1;  
				}  
			},
		{
			title : "用户名",
			field : "userName",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
			{
			title : "提币数量",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "手续费",
			field : "extra",
			align: 'center',
			valign: 'middle',
			width:  '140px',
		},{
			title : "创建时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		}, {
			title : "操作",
			field : "userName",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter1
		}, 
		{
			title : "状态",
			field : "status",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter		
		}],		
		//search : true,//搜索
       // searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams(params){
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;
	
    return{
        //每页多少条数据
        pageSize: params.limit,
        //当前页码
        pageNum: params.offset / params.limit + 1,
    }
}

//请求成功方法
function responseHandler(res){
	if(res.code != 0 ){
		alert("提币请求审核回显失败！"+res.message)
		return{
			total:0,
			withdrawData : null
		}
	}
	return {
		 total : res.data.total, //总页数,前面的key必须为"total"
		 withdrawData : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
	}
};

function actionFormatter(value, row, index) {
	var id = row.uuid;
	var status = value;
	var result = "";
	if(status==0){
		result += "<input type='radio' onclick=\"agree('" + id + "')\"' name='radio' id='agree' value='1'>批准 ";
		result += "<input type='radio' onclick=\"reject('" + id + "')\" name='radio' id='reject' value='2'>拒绝";
		return result;
	}else if(status==1){
		result += "<span>审核已通过</span>";      
		return result;
	}else if(status==2){
		result += "<span>审核未通过</span>";      
		return result;
	} 
	else if(status==3){
		result += "<span>审核已通过-转账成功</span>";      
		return result;
	}else{
		result += "<span>审核未通过-转账失败</span>";      
		return result;
	}
}

function actionFormatter1(value, row, index) {
	var id = value;
	var result = "";
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"viewUserMessage('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";      
	return result;
}

function agree(id){
	var status = 1;
	$('#status').val(status);
	$('#uuid').val(id);
	document.getElementById("withdrawMoney").innerText="确认批准提币请求吗？";
	$("#agreeModal").modal("show");
}

function reject(id){
	var status = 2;
	$('#status').val(status);
	$('#uuid').val(id);
	document.getElementById("withdrawMoney").innerText="确认拒绝提币请求吗？";
	$("#agreeModal").modal("show");
}

function viewUserMessage(id){	
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
		$("#queryModal").modal("show");
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
