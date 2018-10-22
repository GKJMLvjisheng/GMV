
function initEnergyWalletGrid(data) {	
	
	$("#energyWalletGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",
		dataType:"json",
		striped:true,//隔行变色
		
		pagination:true,//显示分页条：页码，条数等
		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams,//请求服务器时所传的参数
		//queryParamsType:'limit',//查询参数组织方式

		
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		//sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,	
			
		columns : [{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle',  
		formatter: function (value, row, index) {  
			return index+1;  
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
		{
			title : "交易类型",
			field : "inOrOut",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			formatter: actionFormatter1
		},
		{
			title : "交易积分",
			field : "pointChange",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "获得算力",
			field : "powerChange",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},
		{
			title : "兑换状态",
			field : "status",
			align: 'center',
			valign: 'middle',
			width:  '150px',
			formatter: actionFormatter2
		},
			{
			title : "交易时间",
			field : "timeCreated",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},{

			title : "操作",
			field : "name",
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

	function actionFormatter1(value, row, index) {
		var result = "";
		if(value==1){
		result += "<span>获得</span>";      
        return result;
		}else if(value==0){
		result += "<span>支出</span>";      
        return result;
		}        
	}

	function actionFormatter2(value, row, index) {
        var result = "";
        if(value==1){
		result += "<span>未兑换</span>";      
        return result;
		}else if(value==0){
		result += "<span>已兑换</span>";      
        return result;
		}    
	}
	

	//请求服务数据时所传参数
	function queryParams(params){
	    return{
	        //每页多少条数据
	        pageSize: params.limit,
	        //当前页码
	        pageNum: params.offset / params.limit + 1,
	    }
	}

	
	//请求成功方法
	function responseHandler(res){
	    var code = res.code;//在此做了错误代码的判断
	    if(code != 0){
	        alert("错误代码:" + code);
	        return;
	    }
	    //如果没有错误则返回数据，渲染表格
	    return {
	        total : res.data.total, //总页数,前面的key必须为"total"
	        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
	    };
	};

	
function initFundBigGrid() {	

	$("#fundBigGrid").bootstrapTable({
		url: '/api/v1/energyPoint/inqureEnergyWalletBalanceRecord',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		
		pagination:true,//显示分页条：页码，条数等
		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams,//请求服务器时所传的参数
		responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法		
		//dataField: "data",

		
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,
		
		columns : [{  
		title: '序号',  
		field: '',
		align: 'center', 
		valign: 'middle', 
		formatter: function (value, row, index) {  
			return index+1;  
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},
			{
			title : "积分",
			field : "point",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},
		{
			title : "算力",
			field : "power",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},
		{
			title : "操作",
			field : "name",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter
		}],		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function initFundInGrid(data) {	

	$("#fundInGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",
		dataType:"json",
		pagination:true,//显示分页条：页码，条数等
		striped:true,//隔行变色
		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		//sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,

		columns : [{  
		title: '序号',  
		field: '',
		align: 'center', 
		valign: 'middle', 
		formatter: function (value, row, index) {  
			return index+1;  
			}  
		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '300px',
		},
			{
			title : "积分流入总额",
			field : "point",
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
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function initFundOutGrid(data) {	
	$("#fundOutGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",
		dataType:"json",
		pagination:true,//显示分页条：页码，条数等
		striped:true,//隔行变色
		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		//sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,	

		columns : [{  
		title: '序号',  
		field: '',
		align: 'center', 
		valign: 'middle', 
		formatter: function (value, row, index) {  
			return index+1;  
			}  

		}  ,{
			title : "用户名",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '300px',
		},

			{

			title : "积分流出总额",
			field : "point",
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

		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

	function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";      
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
			$('#Qname').val(rows.name);
			$('#Qnickname').val(rows.nickname);
			$('#Qgender').val(rows.gender);
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