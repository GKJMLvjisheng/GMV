
$(function() {
	
//初始加载提币审核请求	
	KYCReady()
});

function KYCReady(){
	
    $('#KYCGrid').bootstrapTable('destroy');
	var data1;
	 $.ajax({
		
		url: "/api/v1/userCenter/selectAllNews",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data1=res.data.list;
			//alert(JSON.stringify(data1));
			initKYCGrid(data1);
		}, 
		error: function(){
			alert("KYC审核回显失败！")
		}
		}); 
}

function reject(){	    
	$("#postilModal").modal("show");
}

function agree(){	    
	$("#addNCModal").modal("show");
}

function addPostil(){
	var postil=$("#postil").val();
	//alert(Ebrokerage);
	//document.getElementById("brokerage").innerHTML=Ebrokerage;
	data={
		"postil":postil,
		}

	 $.ajax({		
		url: "/api/v1/energyPoint/inqureEnergyWalletInTotalPointTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			document.getElementById("tipContent").innerText="批注添加成功";
			$("#Tip").modal('show');
			$("#postilModal").modal('hide');
			KYCReady();
			$("#KYCGrid").bootstrapTable('refresh');
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="批注添加失败";
			$("#Tip").modal('show');
			$("#postilModal").modal('hide');
		}
		}); 
}

function addNC(){
	var name=$("#name").val();
	var card=$("#card").val();
	//alert(name);
	//document.getElementById("brokerage").innerHTML=Ebrokerage;
	data={
		"postil":postil,
		"card":card,
		}

	 $.ajax({		
		url: "/api/v1/energyPoint/inqureEnergyWalletInTotalPointTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			document.getElementById("tipContent").innerText="信息添加成功";
			$("#Tip").modal('show');
			$("#addNCModal").modal('hide');
			KYCReady();
			$("#KYCGrid").bootstrapTable('refresh');
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="信息添加失败";
			$("#Tip").modal('show');
			$("#addNCModal").modal('hide');
		}
		}); 
}