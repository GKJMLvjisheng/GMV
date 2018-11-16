document.write("<script language=javascript src='/js/withdrawMoney/withdrawMoneyTable.js'></script>");

$(function() {
	
//初始加载提币审核请求	
	initRequestAuditGrid();
	
});

function audit(){
	var uuid = $("#uuid").val();
	var status = $("#status").val();
	
	var array = new Array();
	array[0] = uuid;
	var data={
		"uuids":array,
		"status":status,
		}

	$.ajax({		
		url: "/api/v1/userWallet/setWithdrawResult",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			if(res.code==0){
				document.getElementById("tipContent").innerText="审核过程完成";
				$("#Tip").modal('show');
				var pageNumber = $("#requestAuditGrid").bootstrapTable('getOptions').pageNumber;
				$("#requestAuditGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
				
				debugger;
				$("#moneyGrid").bootstrapTable('removeByUniqueId', uuid);
				
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
				var pageNumber = $("#requestAuditGrid").bootstrapTable('getOptions').pageNumber;
				$("#requestAuditGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
				
				$("#moneyGrid").bootstrapTable('removeByUniqueId', uuid);
			}		
			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="审核过程发生错误";
			$("#Tip").modal('show');
			var pageNumber = $("#requestAuditGrid").bootstrapTable('getOptions').pageNumber;
			$("#requestAuditGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
		}
	}); 
}

function refresh(){
	var pageNumber = $("#requestAuditGrid").bootstrapTable('getOptions').pageNumber;
	$("#requestAuditGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
}

function deleteById(uuid){
	
	$("#moneyGrid").bootstrapTable('removeByUniqueId', uuid);

	var boxes = document.getElementsByName("btSelectItem");
    var box = document.getElementsByName("btSelectAll");    
	var rows=$("#requestAuditGrid").bootstrapTable('getData');//获取表格中当页的数据	
	//alert(JSON.stringify(rows.length));

	for(var i=0;i<rows.length;i++){
		if(rows[i].uuid==uuid)
		{
			boxes[i].checked = false;
			box[0].checked=false;
		}
	}
}
