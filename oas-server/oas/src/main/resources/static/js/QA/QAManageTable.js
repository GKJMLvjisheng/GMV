//答题管理界面创建bootstrapTable
document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");

function initQuestionGrid(data) {	

	$("#questionGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"questionId",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortable: true,//是否启用排序
		sortName: 'questionId', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,
		
		columns : [{

			title : "问题",

			//field : "questionContent",
			field : "newsTitle",
			align: 'center',
			valign: 'middle',
			width:  '200px',

		},
			{

			title : "A",

			//field : "choiceContent1",
			field : "newsTitle",
			align: 'center',
			valign: 'middle',
			width:  '120px',

		}, {

			title : "B",

			//field : "choiceContent2",
			field : "newsTitle",
			align: 'center',
			valign: 'middle',
			width:  '120px',

		},
		{

			title : "答案",

			//field : "answer",
			field : "newsTitle",
			align: 'center',
			valign: 'middle',
			width:  '120px',
		}, 
		{

			title : " 操作",
			
			//field : "questionId",
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

function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewQuestionById('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditQuestionById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteQuestionById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

        return result;
}
 
function ViewQuestionById(id){	

		//获取选中行的数据		
		var rows=$("#questionGrid").bootstrapTable('getRowByUniqueId', id);
		$('#QquestionId').val(rows.questionId);
		$('#QquestionContent').val(rows.questionContent);
		$('#QchoiceContent1').val(rows.choiceContent1);
		$('#QchoiceContent2').val(rows.choiceContent2);
		$('#Qanswer').val(rows.answer);
		$("#qureyQuestionModal").modal("show");
		

}	
function EditQuestionById(id){
    
    //获取选中行的数据
    var rows=$("#questionGrid").bootstrapTable('getRowByUniqueId', id);
	$('#EquestionId').val(rows.questionId);
	$('#EquestionContent').val(rows.questionContent);
	$('#EchoiceContent1').val(rows.choiceContent1);
	$('#EchoiceContent2').val(rows.choiceContent2);
	$('#Eanswer').val(rows.answer);				
    $("#updateQuestionModal").modal("show");           
  }


function deleteQuestionById(id)
{ 
	 Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"questionId":id};
	//alert(JSON.stringify(data));
	$.ajax({

		url:"/api/v1/userCenter/deleteQuestion",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#questionGrid").bootstrapTable('removeByUniqueId', id);
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