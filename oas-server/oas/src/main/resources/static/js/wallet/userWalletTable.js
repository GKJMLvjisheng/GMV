
function initUserWalletGrid(data) {	

	$("#userWalletGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

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
			width:  '100px',

		},
			{
			title : "交易金额",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		},
		{
			title : "交易类型",
			field : "title",
			align: 'center',
			valign: 'middle',
			width:  '200px',
		},
		{
			title : "交易对方",
			field : "changeUserName",
			align: 'center',
			valign: 'middle',
			width:  '150px',

		},
			{
			title : "交易时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '200px',

		}, {

			title : "交易备注",
			field : "remark",
			align: 'center',
			valign: 'middle',
			width:  '120px',

		},{

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

function initFundBigGrid(data) {	

	$("#fundBigGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

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
			width:  '300px',

		},
			{
			title : "资金总额",
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
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}
	
function initFundInGrid(data) {	

	$("#fundInGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

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
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}


function initFundOutGrid(data) {	

	$("#fundOutGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

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

