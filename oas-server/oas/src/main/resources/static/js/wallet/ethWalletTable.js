var pageSize;
var pageNum;

function initEthWalletGrid() {	
	$("#ethWalletGrid").bootstrapTable('destroy');
	$("#ethWalletGrid").bootstrapTable({
		url: '/api/v1/ethWallet/inqureEthWalletTradeRecord',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams1,//请求服务器时所传的参数
		responseHandler:responseHandler1,//请求数据成功后，渲染表格前的方法		
		dataField: "data",
		
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
		columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle',  
			width:  '75px',
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
			title : "交易金额",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '90px',
		},
		{
			title : "交易类型",
			field : "title",
			align: 'center',
			valign: 'middle',
			width:  '130px',
		},
		{
			title : "交易细节",
			field : "subTitle",
			align: 'center',
			valign: 'middle',
			width:  '170px',
		},
			{
			title : "交易时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '170px',
		}, {
			title : "交易备注",
			field : "remark",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		}, {
			title : "账户余额",
			field : "restBalance",
			align: 'center',
			valign: 'middle',
			width:  '110px',
			formatter:function(value){
				return value == null?0:value
			}
		},{
			title : "操作",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter
		}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams1(params){
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;
	var searchValue = $("#eth1").val();
	//alert(JSON.stringify(searchValue));
    return{
        //每页多少条数据
        pageSize: params.limit,
        //当前页码
        pageNum: params.offset / params.limit + 1,
        searchValue: searchValue,
    }
}

//请求成功方法
function responseHandler1(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("交易钱包回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initFundInGrid() {	
	$("#fundInGrid").bootstrapTable('destroy');
	$("#fundInGrid").bootstrapTable({
		url: '/api/v1/ethWallet/inqureEthWalletInTotalTradeRecord',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams3,//请求服务器时所传的参数
		responseHandler:responseHandler3,//请求数据成功后，渲染表格前的方法		
		dataField: "data",
		
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		//sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		columns : [{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle',  
		formatter: function (value, row, index) {  
			return pageSize * (pageNum - 1) + index + 1;
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '300px',
		},
			{
			title : "资金流入总额",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '300px',
		},{
			title : "操作",
			field : "name",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter
		}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams3(params){
	var searchValue = $("#eth2").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,
        searchValue: searchValue,
        startTime: $("#startTime4").val(),
        endTime: $("#endTime4").val(),
    }
}

//请求成功方法
function responseHandler3(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("资金流入Top榜回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initFundOutGrid() {
	$("#fundOutGrid").bootstrapTable('destroy');
	$("#fundOutGrid").bootstrapTable({
		url: '/api/v1/ethWallet/inqureEthWalletOutTotalTradeRecord',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams4,//请求服务器时所传的参数
		responseHandler:responseHandler4,//请求数据成功后，渲染表格前的方法		
		dataField: "data",
		
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		//sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		
		columns : [{  
		title: '序号',  
		field: '',
		align: 'center', 
		valign: 'middle', 
		formatter: function (value, row, index) {  
			return pageSize * (pageNum - 1) + index + 1; 
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '300px',
		},
			{
			title : "资金流出总额",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '300px',
		},
		{
			title : "操作",
			field : "name",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter
		}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams4(params){
	var searchValue = $("#eth2").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,
        searchValue: searchValue,
        startTime: $("#startTime5").val(),
        endTime: $("#endTime5").val(),
    }
}

//请求成功方法
function responseHandler4(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("资金流出Top榜回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function actionFormatter(value, row, index) {
    var id = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";       
    return result;
}

function ViewViewById(id){	
	var formData = new FormData();
	$('#Qname').val(id);	
	formData.append("name", $("#Qname").val());
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
		rows = res.data;			
		$('#Qname').val(rows.name);
		$('#Qnickname').val(rows.nickname);
		$('#Qgender').val(rows.gender);
		$('#Qbirthday').val(rows.birthday);
		$('#Qmobile').val(rows.mobile);
		$('#Qemail').val(rows.email);
		$('#Qaddress').val(rows.address);
		$('#QinviteCode').val(rows.inviteCode);
		$("#queryEthModal").modal("show");
		}
		else{
			alert("查询失败1");
			}						
	},
	error:function(){
		alert("查询用户信息过程出现错误！");
	},
	});					
}
