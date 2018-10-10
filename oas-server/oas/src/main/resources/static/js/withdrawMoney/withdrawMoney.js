
$(function() {
	
//初始加载手续费、提币审核请求	
	getBrokerage();
	requestAuditReady();
});

function requestAuditReady(){
	
    $('#requestAuditGrid').bootstrapTable('destroy');
	var data1;
	 $.ajax({
		
		url: "/api/v1/userWallet/getWithdrawList",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data1=res.data;
			initRequestAuditGrid(data1);
			audit();
		}, 
		error: function(){
			alert("提币请求审核回显失败！")
		}
		}); 
}

//审核
function audit(){
	$(":radio").click(function(){
		var choice=$(this).val();
		var uuid = $(this).parent().parent().find('.uu_style').text();
		// alert(choice);
		// alert(uuid);
		data={
			"uuid":uuid,
			"status":choice,
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
					requestAuditReady();
					//$("#requestAuditGrid").bootstrapTable('refresh');
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
	});
}


//functions of brokerageConfig   begin
function edit(){
	var brokerage=document.getElementById("brokerage").innerHTML;
	//alert(brokerage);		 
	$("#Ebrokerage").val(brokerage);	    
	$("#updateModal").modal("show");
}

function getBrokerage(){

	 $.ajax({		
		url: "/api/v1/userWallet/getOasExtra",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'get',
		processData : false,
		async : false,

		success: function(res) {			
			var brokerage1=res.data;
			//alert(brokerage1);
			document.getElementById("brokerage").innerHTML=brokerage1;
		}, 
		error: function(){
			alert("获取手续费失败！")
		}
		}); 
} 

function updateBrokerage(){
	var Ebrokerage=$("#Ebrokerage").val();
	data={
		"value":Ebrokerage,
		}

	 $.ajax({		
		url: "/api/v1/userWallet/updateOasExtra",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			document.getElementById("tipContent").innerText="手续费修改成功";
			$("#Tip").modal('show');
			document.getElementById("brokerage").innerHTML=Ebrokerage;
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="手续费修改失败";
			$("#Tip").modal('show');
		}
		}); 
}
//functions of brokerageConfig   end