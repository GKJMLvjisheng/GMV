var pageSize;
var pageNum;

function initPurchaseDetailGrid() {	
	$("#purchaseDetailGrid").bootstrapTable('destroy');
	$("#purchaseDetailGrid").bootstrapTable({
		url: '/api/v1/miner/inquireUserPurchaseRecord',
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
				field : "userName",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
			{

				title : "矿机类型",
				field : "minerName",
				align: 'center',
				valign: 'middle',
				width:  '110px',
			},
			{
				title : "矿机台数",
				field : "minerNum",
				align: 'center',
				valign: 'middle',
				width:  '110px',
			},			
				{
				title : "购买时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '150px',
				//visible: false,
			},{

				title : "操作",
				field : "userName",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter
			}],		
		clickToSelect: true,         
	});
}
	
//请求服务数据时所传参数
function queryParams1(params){
	var searchValue = $("#miner").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,      
   
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler1(res){
	//alert(JSON.stringify(res));
    if(res.code != 0){
        alert("用户购买明细回显失败，错误代码:" + res.code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initMinerSellGrid(data) {	
	$("#minerSellGrid").bootstrapTable('destroy');
	$("#minerSellGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],		

		toolbar:"#toolbar",//工具栏
		
		data:data,				
			 
		columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return index + 1; 
				}  
			},{

			title : "矿机类型",
			field : "minerName",
			align: 'center',
			valign: 'middle',
			width:  '110px',
		},
			{

			title : "矿机台数",
			field : "minerNum",
			align: 'center',
			valign: 'middle',
			width:  '80px',

		}],
		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false, 
	});
}


function actionFormatter(value, row, index) {
    var name = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + name + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";      
    return result;
}

function ViewViewById(name){	
	var formData = new FormData();
	formData.append("name", name);
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
		$("#queryMinerModal").modal("show");
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