document.write("<script language=javascript src='/js/wallet/energyWalletTable.js'></script>");
$(function() {
	initonlineWalletGrid();
	
	
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
		responseHandler:systemTransactionDetailHandle,//请求数据成功后，渲染表格前的方法	 	
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
function systemTransactionDetailHandle(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("system账户在线钱包回显失败，错误代码:" + code);
        return;
    }
    $("#systemUserWallet").text(res.data.rows.value);
    //console.log(JSON.stringify(res))
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows.list //行数据，前面的key要与之前设置的dataField的值一致.
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
		responseHandler:systemTransactionDetailHandle2,//请求数据成功后，渲染表格前的方法		
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
function systemTransactionDetailHandle2(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("system账户交易钱包回显失败，错误代码:" + code);
        return;
    }
    $("#systemEthWallet").text(res.data.rows.value);
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows.list //行数据，前面的key要与之前设置的dataField的值一致.
    };
};
function firstOneDetail() {	
	$("#firstoneGrid").bootstrapTable('destroy'); 
	$("#firstoneGrid").bootstrapTable({
		url: '/api/v1/userWallet/firstOneTransactionDetail',
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
		responseHandler:firstoneTransactionDetailHandle,//请求数据成功后，渲染表格前的方法		
		dataField: "data",
		//toolbar:"#toolbar",//工具栏
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
		},{
			title : "备注",
			field : "remark",
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
function firstoneTransactionDetailHandle(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("firstone账户交易回显失败，错误代码:" + code);
        return;
    }
    $("#firstValue").text(res.data.rows.value);
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //总页数,前面的key必须为"total"
        data : res.data.rows.list //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function display1(){
	document.getElementById("page2").style.display="none";
	document.getElementById("page1").style.display="block";
	document.getElementById("page3").style.display="none";
	$('#btn1').removeClass('active1').addClass('active');
	$('#btn2').removeClass('active').addClass('active1');
	$('#btn3').removeClass('active').addClass('active1');
	
}
function display2(){
	document.getElementById("page2").style.display="block";
	document.getElementById("page1").style.display="none";
	document.getElementById("page3").style.display="none";
	$('#btn2').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
	$('#btn3').removeClass('active').addClass('active1');
	tradeWalletGrid();
	getSystemDetail();
	
}
function display3(){
	document.getElementById("page3").style.display="block";
	document.getElementById("page2").style.display="none";
	document.getElementById("page1").style.display="none";
	$('#btn3').removeClass('active1').addClass('active');
	$('#btn2').removeClass('active').addClass('active1');
	$('#btn1').removeClass('active').addClass('active1');
	firstOneDetail();
	
}
function getSystemDetail(){
	 $.ajax({
		   type: 'post',
		   url: '/api/v1/ethWallet/systemETHandAddress',
		  // data: JSON.stringify(data),
		   contentType : 'application/json;charset=utf8',
		   dataType: 'json',
		   cache: false,
		   async : false,
		   success: function (res) {
			 
		     if (res.code == 0) {
		    	 var data=res.data.userCoin
		    	 $("#address").text(data[0].address)
		    	 $("#ETHmoney").text(data[0].ethBalance)
		     } else {
		    	 alert(res.message);
		     }
		   },
		   error: function (res) {
			  alert("option错误"+JSON.stringify(res));
		   },
		  
		  });
   
	 
  
}
function resetMoney(){
	
	var remark=$("#remarks").val();
	var firstMoney=$("#firstMoney").val();
	if(firstMoney===""||!validate(firstMoney)){
		alert("请输入正确的金额数");
		return;
	}
	if(remark===""){
		alert("请输入备注");
		return;
	}
	if(firstMoney>1000000000){
		alert("金额不能超过10亿");
		return;
	}
	data={"value":firstMoney,
			"remark":remark}
	console.log(JSON.stringify(data));
	$.ajax({
		   type: 'post',
		   url: '/api/v1/userWallet/setFirstOneUserBalance',
		   data: JSON.stringify(data),
		   contentType : 'application/json;charset=utf8',
		   dataType: 'json',
		   cache: false,
		   async : false,
		   success: function (res) {
			 
		     if (res.code == 0) {
		    	 alert("重置金额成功！");
		    	 resetAddModal();
		    	 firstOneDetail();
		    	 
		     } else {
		    	 alert(res.message);
		     }
		   },
		   error: function (res) {
			  alert(res.message);
		   },
		  
		  });
}
function resetAddModal(){
	document.getElementById("addfirstOneForm").reset();
	//document.getElementById("updateActivityRewardForm").reset();
	$("#addfirstOneForm").find('textarea,input[type=text],select').each(function() {
		
        		$(this).val('');
        		$(this).html('');
    });
	//$("span[id='msg_parameterValue']").html("");
	
 }
function validate(num)
{

	var reg = /^\d+(?=\.{0,1}\d+$|$)/;//包括0不包括“”
  //var reg=/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/;//不包括0
  if(reg.test(num)){
	 
	  return true;}
  return false ;  
}