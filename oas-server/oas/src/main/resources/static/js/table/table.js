var pageSize;
var pageNum;
//表格回显
$(function(){
	ready();
});
//初始化表格
function initTable(data) {	
	$("#table").bootstrapTable({
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
			title: "参数代码",  
			field: "minerName",
			align: 'center',
			valign: 'middle', 
			width:  '80px',
			},
			{
			title : "参数名称",
			field : "minerDescription",
			align: 'center',
			valign: 'middle',
			width:  '110px',
//		    },
//			{
//			title : "参数意义",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "参数源码",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "比特位",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "结果",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "超限",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "范围",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "单位",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
		}],
		
//		search : true,//搜索
//        searchOnEnterKey : true,
//		clickToSelect: false, 
	});
}

function ready(){
    $('#table').bootstrapTable('destroy');
	var data;
	var data1={};
	 $.ajax({
		
		url: "/api/v1/load/selectLoadMsg",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data: JSON.stringify(data1),
		async : false,
		success: function(res) {
		alert(JSON.stringify(res));
		if(res.code==0)
			{data=res.data.rows;}
		
		else{alert("回显失败！");}
			
		}, 
		error: function(){
			alert("失败！")
		}
		}); 
	 initTable(data);
}

