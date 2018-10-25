document.write("<script language=javascript src='/js/wallet/energyWalletTable.js'></script>");
$(function() {
	initonlineWalletGrid();
	tradeWalletGrid();
	
});
var pageSize;
var pageNum;

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
//在线钱包	
function initonlineWalletGrid() {
	$("#onlineWalletGrid").bootstrapTable('destroy');
	$("#onlineWalletGrid").bootstrapTable({
		url: '/api/v1/userWallet/systemTransactionDetail',
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
		responseHandler:responseHandler1,//请求数据成功后，渲染表格前的方法	 	
		dataField: "data",
		
		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		sortName: 'created', // 要排序的字段
	    sortOrder: 'asc', // 排序规则	
	    uniqueId:"uuid",	
		columns : [{  
		title: '序号',  
		field: 'ID',
		align: 'center',
		valign: 'middle',  
		width:  '80px',
		formatter: function (value, row, index) {  
			return pageSize * (pageNum - 1) + index + 1;  
			}  
		}  ,{
			title : "交易类型",
			field : "title",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
//		{
//			title : "交易类型",
//			field : "inOrOut",
//			align: 'center',
//			valign: 'middle',
//			width:  '130px',
//			formatter: actionFormatter1
//		},
		{
			title : "交易细节",
			field : "subTitle",
			align: 'center',
			valign: 'middle',
			width:  '130px',
		},{
			title : "交易金额",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '130px',
		},
		
		{
			title : "账户余额",
			field : "restBalance",
			align: 'center',
			valign: 'middle',
			width:  '130px',
			//formatter: actionFormatter2
		},
			{
			title : "交易时间",
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
			visible: false,
			formatter: actionFormatter
		}],		
		//search : true,//搜索
       // searchOnEnterKey : true,
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

//请求成功方法
function responseHandler1(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("能量钱包回显失败，错误代码:" + code);
        return;
    }
    console.log(JSON.stringify(res))
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

//交易钱包	
function tradeWalletGrid() {	
	$("#tradeWalletGrid").bootstrapTable('destroy'); 
	$("#tradeWalletGrid").bootstrapTable({
		url: '/api/v1/ethWallet/systemTransactionDetail',
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
		responseHandler:responseHandler2,//请求数据成功后，渲染表格前的方法		
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
		formatter: function (value, row, index) {  
			//return index+1;  
			return pageSize * (pageNum - 1) + index + 1;  
			}  
		}  ,{
			title : "交易类型",
			field : "title",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
//		{
//			title : "交易类型",
//			field : "inOrOut",
//			align: 'center',
//			valign: 'middle',
//			width:  '130px',
//			formatter: actionFormatter1
//		},
		{
			title : "交易细节",
			field : "subTitle",
			align: 'center',
			valign: 'middle',
			//width:  '130px',
		},{
			title : "交易金额",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '130px',
		},
		
		{
			title : "账户余额",
			field : "restBalance",
			align: 'center',
			valign: 'middle',
			width:  '130px',
			//formatter: actionFormatter2
		},
			{
			title : "交易时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '170px',
		},{

			title : "操作",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			visible: false,
			formatter: actionFormatter,
			
		}
		],	
		//search : true,//搜索
        //searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求成功方法
function responseHandler2(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("积分大户Top榜回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function display1(){
	document.getElementById("page2").style.display="none";
	document.getElementById("page1").style.display="block";
	$('#btn1').removeClass('active1').addClass('active');
	$('#btn2').removeClass('active').addClass('active1');
	
}
function display2(){
	document.getElementById("page2").style.display="block";
	document.getElementById("page1").style.display="none";	
	$('#btn2').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
}
