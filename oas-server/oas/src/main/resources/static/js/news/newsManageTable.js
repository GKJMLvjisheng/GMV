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
			formatter: title

		}, {

			title : "摘要",

			field : "newsAbstract",
			align: 'center',
			valign: 'middle',
			width:  '200px',
			formatter: abstract

		},
		{

			title : "图片",

			field : "newsPicturePath",
			align: 'center',
			valign: 'middle',
			formatter: picturePath

		}, 
		{

			title : "新闻链接",

			field : "newsUrl",
			align: 'center',
			valign: 'middle',
			formatter: url		

		},
		{

			title : " 操作",

			field : "newsId",
			align: 'center',
			valign: 'middle',
			width:  '90px',
			formatter: actionFormatter


		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//每行countInLine个字符
function changeLine(newsClass,countInLine){
	var str="";	    	
	var newsClass1 = newsClass.split('');	//字符串转数组	 
	for(var i = 0; i < newsClass1.length; i++){ 
	    if((i + 1) % countInLine == 0){ 
	        str += newsClass1[i] + "<br>"; 
	    } 
	    else { 
	        str += newsClass1[i]; 
	    } 
	}
	return str;
}


function title(value, row, index) {	
	var result = "";
	var newsTitle = value;
	var str=changeLine(newsTitle,20);
	 result +="<span>"+str+"</span>";    
	 return result;
}

function abstract(value, row, index) {	
	var result = "";
	var abstract = value;
	var str=changeLine(abstract,30);
	 result +="<span>"+str+"</span>";    
	 return result;
}
 
function picturePath(value, row, index) {
	var result = "";
	var newsPicturePath = value; 
	var str=changeLine(newsPicturePath,20);      
    result += "<img src="+newsPicturePath+ " width='120px' height='80'><br>"+
    			"<span>"+str+"</span>";    
    return result;
}

function url(value, row, index) {	
	var result = "";
	var newsUrl = value;    	
	var str=changeLine(newsUrl,40);
    result += "<a href="+str+">"+str+"</a>" ;    
    return result;
}

function actionFormatter(value, row, index) {
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