//矿机管理界面创建bootstrapTable
document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");

function initMinerGrid(data) {	

	$("#minerGrid").bootstrapTable({
		
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"minerCode",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'orderNum', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
		data:data,				
			 
		columns : [{  
			title: '序号',  
			field: 'orderNum',
			align: 'center',
			valign: 'middle', 
			width:  '60px', 
			visible: false,

			} ,{

			title : "矿机名",
			field : "minerName",
			align: 'center',
			valign: 'middle',
			width:  '110px',
		},
			{

			title : "矿机单价",
			field : "minerPrice",
			align: 'center',
			valign: 'middle',
			width:  '80px',

		}, 
			{

			title : "矿机等级",
			field : "minerGrade",
			align: 'center',
			valign: 'middle',
			width:  '80px',

		},{

			title : "算力奖励",
			field : "minerPower",
			align: 'center',
			valign: 'middle',
			width:  '80px',

		},
		{

			title : "矿机寿命",
			field : "minerPeriod",
			align: 'center',
			valign: 'middle',
			width:  '80px',

		},
		{

			title : "矿机描述",
			field : "minerDescription",
			align: 'center',
			valign: 'middle',
			//width:  '150px',
		}, 
		{

			title : "修改时间",
			field : "updated",
			align: 'center',
			valign: 'middle',
			width:  '160px',
		},
		{

			title : " 排序",			
			field : "minerCode",
			align: 'center',
			valign: 'middle',
			width:  '90px',
			formatter: actionFormatter1
		},
		{

			title : " 操作",			
			field : "minerCode",
			align: 'center',
			valign: 'middle',
			width:  '90px',
			formatter: actionFormatter2
		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false, 
	});
}
		
//当拖拽结束后，整个表格的数据            
//		onReorderRow: function (newData) { 
//			alert(JSON.stringify(newData));			
//		}

function actionFormatter1(value, row, index) {
    var minerCode = value;
    var result = "";

    result += "<a href='javascript:;' title='上移' class='btn btn-xs blue' onclick=\"up('" + minerCode + "')\"><span class='glyphicon glyphicon-upload' style='font-size:15px;'></span> </a>";
    result += "<a href='javascript:;' title='下移' class='btn btn-xs blue' onclick=\"down('" + minerCode + "')\"><span class='glyphicon glyphicon-download' style='font-size:15px;'></span></a>";

    return result;
}

function actionFormatter2(value, row, index) {
    var id = value;
    var result = "";

    result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditMinerById('" + id + "')\" title='修改'><span class='glyphicon glyphicon-pencil'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteMinerById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

    return result;
}
 
function up(minerCode){
	var data = {
		"minerCode" : minerCode,
	};
	
	$.ajax({
		url: "/api/v1/miner/upMiner",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,
		
		success : function(res) {
			if (res.message == "已是第一个，不能上移") {
				alert("已是第一个，不能上移！")
			} else if(res.message == "成功"){
				minerReady();
			}
		},
		error : function() {		
			alert("上移过程发生错误！");
		}
	});	
}

function down(minerCode){

	var data = {
		"minerCode" : minerCode,
	};
	
	$.ajax({
		url: "/api/v1/miner/downMiner",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,
		
		success : function(res) {
			if (res.message == "已是最后一个，不能下移"){
				alert("已是最后一个，不能下移！")
			}else if(res.message == "成功"){
				minerReady();
			}
		},
		error : function() {		
			alert("下移过程发生错误！");
		}
	});	
}

function EditMinerById(id){
    
    //获取选中行的数据
    var rows=$("#minerGrid").bootstrapTable('getRowByUniqueId', id);
	$('#EminerCode').val(rows.minerCode);
    $('#EminerName').val(rows.minerName);
	$('#EminerPrice').val(rows.minerPrice);
	$('#EminerGrade').val(rows.minerGrade);
	$('#EminerPower').val(rows.minerPower);
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