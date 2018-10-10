
//提币请求审核创建bootstrapTable

function initRequestAuditGrid(data) {	

	$("#requestAuditGrid").bootstrapTable({

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		toolbar:"#toolbar",//工具栏
		sortName: 'ID', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		data:data,
		columns : [{  
		title: 'uuid',  
		field: 'uuid',
		align: 'center',
		valign: 'middle',
		class:'uu_style', 
		//visible: false, 

		},
		{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle', 
		width:  '60px',
		formatter: function (value, row, index) {  
			return index+1;  
			}  
		},{
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
		},  
		{
			title : "审核状态",
			field : "status",
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
	var status = value;
	var result = "";
	if(status==0){
		result += "<input type='radio' name='radio' id='agree' value='1'>批准 ";
		result += "<input type='radio' name='radio' id='reject' value='2'>拒绝";
		return result;
	}else if(status==1){
		result += "<span>已通过</span>";      
		return result;
	}else if(status==2){
		result += "<span>未通过</span>";      
	return result;
	}       
}
