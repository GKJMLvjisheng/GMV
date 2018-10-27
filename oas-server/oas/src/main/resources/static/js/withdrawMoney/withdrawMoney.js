document.write("<script language=javascript src='/js/withdrawMoney/withdrawMoneyTable.js'></script>");

$(function() {
	
//初始加载提币审核请求	
	initRequestAuditGrid();;
});

function audit(){
	var uuid = $("#uuid").val();
	var status = $("#status").val();
	var data={
		"uuid":uuid,
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
				//location.reload();
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
			}			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="审核过程发生错误";
			$("#Tip").modal('show');
		}
	}); 
}

function refresh(){
	location.reload();
}
