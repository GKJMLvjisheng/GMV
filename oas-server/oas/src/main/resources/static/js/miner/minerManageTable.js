//答题管理界面创建bootstrapTable
document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");

function initMinerGrid(data) {	

	$("#minerGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"minerCode",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		//sortable: true,//是否启用排序
		sortName: 'minerCode', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,
		
		columns : [{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle', 
		width:  '60px', 
		formatter: function (value, row, index) {  
			return index+1;  
			}  
		}  ,
			{

			title : "矿机名",
			field : "minerName",
			align: 'center',
			valign: 'middle',
			width:  '120px',

		},
			{

			title : "矿机单价",
			field : "minerPrice",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		}, {

			title : "矿机效率",
			field : "minerEfficiency",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		},
		{

			title : "矿机寿命",
			field : "minerPeriod",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		},
		{

			title : "矿机描述",
			field : "minerDescription",
			align: 'center',
			valign: 'middle',
			width:  '170px',
		}, 
		{

			title : "修改时间",
			field : "updated",
			align: 'center',
			valign: 'middle',
			width:  '160px',
		}, 
		{

			title : " 操作",			
			field : "minerCode",
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
    
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditMinerById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteMinerById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

        return result;
}
 
function EditMinerById(id){
    
    //获取选中行的数据
    var rows=$("#minerGrid").bootstrapTable('getRowByUniqueId', id);
	$('#EminerCode').val(rows.minerCode);
    $('#EminerName').val(rows.minerName);
	$('#EminerPrice').val(rows.minerPrice);
	$('#EminerEfficiency').val(rows.minerEfficiency);
	$('#EminerPeriod').val(rows.minerPeriod);
	$('#EminerDescription').val(rows.minerDescription);			
	$("#updateMinerModal").modal("show");           
  }


function deleteMinerById(id)
{ 
	 Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"minerCode":id};
	//alert(JSON.stringify(id));
	$.ajax({

		url:"/api/v1/miner/deleteMiner",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#minerGrid").bootstrapTable('removeByUniqueId', id);
				alert("删除成功！");
			}
			else{
				alert("删除失败");
				}
			},
	
	     error:function(){
				alert("删除过程发生错误！");
			}
	});
});
}