//矿机奖励配置界面创建bootstrapTable

function initRewardGrid(data) {	

	$("#rewardGrid").bootstrapTable({
		
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"promotedId",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'created', // 要排序的字段
	    sortOrder: 'desc', // 排序规则
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

			title : "奖励名称",
			field : "rewardName",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		},
			{

			title : "冻结返现比例",
			field : "frozenRatio",
			align: 'center',
			valign: 'middle',
			width:  '100px',
			formatter: actionFormatter1

		}, 
			{

			title : "奖励比例",
			field : "rewardRatio",
			align: 'center',
			valign: 'middle',
			width:  '100px',
			formatter: actionFormatter2

		},{

			title : "返现级别",
			field : "maxPromotedGrade",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		},
		{

			title : "修改时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '130px',

		},
		{

			title : " 操作",			
			field : "promotedId",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter3
		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function actionFormatter1(value, row, index) {
	var rewardName = row.rewardName;
	if(rewardName!="算力"){
		var intRatio = parseFloat(value);
		var intRatio1 = intRatio*100;
		var ch = "%";
		var intRatio2 = intRatio1+ch;
	    //alert(JSON.stringify(intRatio2));
	    var result = "";
	    result += "<span>"+intRatio2+"</span>";
	    return result;
	}	
}

function actionFormatter2(value, row, index) {
	var ratio = parseFloat(value);
	var ratio1 = ratio*100;
	var ch = "%";
	var ratio2 = ratio1+ch;
    //alert(JSON.stringify(ratio2));
    var result = "";
    result += "<span>"+ratio2+"</span>";
    return result;
}

function actionFormatter3(value, row, index) {
    var id = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditRewardById('" + id + "')\" title='修改'><span class='glyphicon glyphicon-pencil'></span></a>";
    return result;
}
 
function EditRewardById(id){
    
    //获取选中行的数据
    var rows=$("#rewardGrid").bootstrapTable('getRowByUniqueId', id);
    var rewardName = rows.rewardName;
	if(rewardName=="算力"){
		document.getElementById("frozen").style.display="none"; 
	}else if(rewardName=="代币"){
		document.getElementById("frozen").style.display="block"; 
	}
	//alert(JSON.stringify(rows))
	
	var frozenRatio1 = rows.frozenRatio*100;
	var ch = "%";
	var frozenRatio = frozenRatio1+ch;
	
	var rewardRatio1 = rows.rewardRatio*100;
	var rewardRatio = rewardRatio1+ch;
		
    $('#promotedId').val(rows.promotedId);
	$('#rewardName').val(rows.rewardName);
    $('#frozenRatio').val(frozenRatio);
	$('#rewardRatio').val(rewardRatio);
	$('#maxPromotedGrade').val(rows.maxPromotedGrade);
	
	$("#msg_frozenRatio").html("请输入0-100之间的数字并加%，例如20%");
	$("#msg_frozenRatio").css("color", "green");
	
	$("#msg_rewardRatio").html("请输入0-100之间的数字并加%，例如20%");
	$("#msg_rewardRatio").css("color", "green");
	
	$("#msg_maxPromotedGrade").html("请输入0-10之间的数字，包括10");
    $("#msg_maxPromotedGrade").css("color", "green");
	
	$("#updateRewardModal").modal("show");           
  }
