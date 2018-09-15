//新闻管理界面创建bootstrapTable
document.write("<script language=javascript src='js/deleteConfirm.js'></script>");

function initNewsGrid(data) {	

	$("#newsGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"newsId",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortName: 'ID', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		data:data,
		columns : [{

			title : "标题",

			field : "newsTitle",
			align: 'center',
			valign: 'middle',
			width:  '200px',

		}, {

			title : "摘要",

			field : "newsAbstract",
			align: 'center',
			valign: 'middle',
			width:  '230px',

		},
		{

			title : "图片",

			field : "newsPicturePath",
			align: 'center',
			valign: 'middle',
			width:  '200px',
			formatter: picturePath

		}, 
		{

			title : "新闻链接",

			field : "newsUrl",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter		

		},
		{

			title : " 操作",
			
			field : "newsId",
			align: 'center',
			valign: 'middle',
			width:  '90px',
			formatter: actionFormatter1
		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

 
function picturePath(value, row, index) {
	var result = "";
	var newsPicturePath = value;      
    result += "<img src="+newsPicturePath+ " width='120px' height='80'><br>"+
    			"<span>"+newsPicturePath+"</span>";    
    return result;
}

function actionFormatter(value, row, index) {	
	var result = "";
	var newsUrl = value;  
	result +="<a href="+newsUrl+ ">"+newsUrl+"</a>";
    return result;
}

function actionFormatter1(value, row, index) {
        var id = value;
        var result = "";
        
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditNewsById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteNewsById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

        return result;
}
 
function ViewNewsById(id){	

		//获取选中行的数据		
		var rows=$("#newsGrid").bootstrapTable('getRowByUniqueId', id);
		$('#QnewsId').val(rows.newsId);
		$('#QnewsTitle').val(rows.newsTitle);
		$('#QnewsAbstract').val(rows.newsAbstract);
		$('#QnewsUrl').val(rows.newsUrl);
		$('#QnewsPicturePath').val(rows.newsPicturePath);
		$("#qureyNewsModal").modal("show");
		

}	
function EditNewsById(id){
    
    //获取选中行的数据
    var rows=$("#newsGrid").bootstrapTable('getRowByUniqueId', id);
	$('#EnewsId').val(rows.newsId);  
    $('#EnewsTitle').val(rows.newsTitle);
	$('#EnewsAbstract').val(rows.newsAbstract);
	$('#EnewsUrl').val(rows.newsUrl);
	$('#EnewsPicturePath').val(rows.newsPicturePath); 				
    $("#updateNewsModal").modal("show");           
  }


function deleteNewsById(id)
{ 
	 Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"newsId":id};
	//alert(JSON.stringify(data));
	$.ajax({

		url:"/api/v1/userCenter/deleteNews",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#newsGrid").bootstrapTable('removeByUniqueId', id);
				alert("删除成功！");
			}
			else{
				alert("删除失败");
				}
			},
	
	     error:function(){
				alert("删除失败！");
			}
	});
});
}