
function initEneryWalletGrid(data) {	

	$("#eneryWalletGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"topicId",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		sortName: 'topicId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,
		
		columns : [{

			title : "用户名",
			field : "topicId",
			align: 'center',
			valign: 'middle',
			width:  '60px',

		},
		{
			title : "交易类型",
			field : "question",
			align: 'center',
			valign: 'middle',
			width:  '200px',

		},
		{
			title : "交易积分",
			field : "question",
			align: 'center',
			valign: 'middle',
			width:  '200px',

		},
		{
			title : "获得算力",
			field : "question",
			align: 'center',
			valign: 'middle',
			width:  '200px',

		},
		{
			title : "兑换状态",
			field : "question",
			align: 'center',
			valign: 'middle',
			width:  '200px',

		},
			{
			title : "交易时间",
			field : "choiceA",
			align: 'center',
			valign: 'middle',
			width:  '120px',

		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}
