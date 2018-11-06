/**
 * 
 */
$(function() {
	initNormalGrid();
	initTestGrid();
	
	
});
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
			
			checkbox:"true",
			
			align: 'center',// 居中显示
			
			field : "box",
		},{  
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
				title : "转账金额",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '98px',
				editable:true,
				formatter:function action(value,row,index){
					value=0;
					return value;
				}
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
			
			checkbox:"true",
			
			align: 'center',// 居中显示
			
			field : "box",
		},{  
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
    function display1()
    {document.getElementById("page2").style.display="none";
    document.getElementById("page1").style.display="block";
    $('#btn1').removeClass('active1').addClass('active');
    $('#btn2').removeClass('active').addClass('active1');
    }
    function display2()
    {
    	//$("#page2").attr()
    	document.getElementById("page1").style.display="none";
    	document.getElementById("page2").style.display="block";
    	$('#btn2').removeClass('active1').addClass('active');
    	$('#btn1').removeClass('active').addClass('active1');
    }
    function getTableData(){
    
    	 var rows = $("#normalGrid").bootstrapTable("getSelections");
//    	var row=$.map($("#normalGrid").bootstrapTable('getSelections'),function(row){
//    		alert(12)
//    		return row ;
//    		});
    
    	 console.log(rows)
    	 
    }