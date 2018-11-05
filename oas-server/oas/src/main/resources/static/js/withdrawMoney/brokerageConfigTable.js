//矿机奖励配置界面创建bootstrapTable

function initBrokeragedGrid(data) {	
	$('#brokerageGrid').bootstrapTable('destroy');
	$("#brokerageGrid").bootstrapTable({
		
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		uniqueId:"valueMax",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		data:data,
		
		columns : [
			{

			title : "单次提币限额",
			field : "valueMax",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		},
			{

			title : "手续费比例",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '100px',
			formatter: actionFormatter1

		}, 
			{

			title : "最低手续费",
			field : "valueMin",
			align: 'center',
			valign: 'middle',
			width:  '100px',

		},
		{

			title : " 操作",			
			field : "valueMax",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter2
		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}


function actionFormatter1(value, row, index) {
	var ratio = parseFloat(value);	
	var ratio1 =(ratio*100).toFixed(0);	
	//alert(JSON.stringify(ratio1));	
	var ratio2 = ratio1.toString()+"%";	
	//alert(JSON.stringify(ratio2));
	
    var result = "";
    result += "<span>"+ratio2+"</span>";
    return result;
}

function actionFormatter2(value, row, index) {
    var valueMax = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditRewardById('" + valueMax + "')\" title='修改'><span class='glyphicon glyphicon-pencil'></span></a>";
    return result;
}
 
function EditRewardById(valueMax){
    
    //获取选中行的数据
    var rows=$("#brokerageGrid").bootstrapTable('getRowByUniqueId', valueMax);
	
	var value1 = (rows.value*100).toFixed(0);
	var value = value1+"%";
		

    $('#mostAmount').val(rows.valueMax);
	$('#brokerageMoney').val(value);
	$('#lessBrokerage').val(rows.valueMin);
	
	$("#msg_mostAmount").html("请输入正数，不超过500,000");
	$("#msg_mostAmount").css("color", "green");
	
	$("#msg_brokerageMoney").html("请输入0-100之间的数字并加%，例如2%");
	$("#msg_brokerageMoney").css("color", "green");
	
	$("#msg_lessBrokerage").html("请输入正数，不少于500");
    $("#msg_lessBrokerage").css("color", "green");
	
	$("#updateBrokerageModal").modal("show");           
  }
