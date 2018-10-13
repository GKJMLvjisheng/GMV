
$(function() {
	
//初始加载手续费	
	getBrokerage();	
});

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
			if(res.code==0){
				document.getElementById("tipContent").innerText="手续费修改成功";
				$("#Tip").modal('show');
				document.getElementById("brokerage").innerHTML=Ebrokerage;
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
			}
			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="手续费修改失败";
			$("#Tip").modal('show');
		}
		}); 
}