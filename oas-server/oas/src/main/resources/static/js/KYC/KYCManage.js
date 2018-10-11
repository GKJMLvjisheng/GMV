document.write("<script language=javascript src='/js/KYC/KYCManageTable.js'></script>");
var check1;
$(function() {
	
//初始加载提币审核请求	
	KYCReady();
});

function KYCReady(){
	
    $('#KYCGrid').bootstrapTable('destroy');
	var data1;
	$.ajax({
		
		url: "/api/v1/userCenter/inqureAllUserIdentityInfo",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data1=res.data;
			//alert(JSON.stringify(data1));
			initKYCGrid(data1);
		}, 
		error: function(){
			alert("KYC审核回显失败！")
		}
		}); 
}

//发ajax请求到后台判断身份证号是否重复
function checkCard() {
  var card = $("#card").val();
  //alert(card);
  if (card != "") {
	  var data = {
        "userIdentityNumber" : card
      };
      $.ajax({
        url: "/api/v1/energyPoint/inqureEnergyWalletInTotalPointTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

        success : function(res) {
          //根据判断提示用户
          if (res.state == true) {
            $("#msg_card").html("身份证号可以使用");
            $("#msg_card").css("color", "green");
            check1 = 1;
            return check1;
          } else {
            $("#msg_card").html("该身份证号已存在");
            $("#msg_card").css("color", "red");
            check1 = 0;
            return check1;
          }
        },
        error : function() {
          alert('检查身份证号是否存在发生错误');
        }
      });
    }
}

function addPostil(){
	var reId = $("#reId").val();
	var postil=$("#postil").val();
	if(postil!=""){
		data={
				"status":3,
				"userName":reId,
				"remark":postil,
				}

			 $.ajax({		
				url: "/api/v1/energyPoint/inqureEnergyWalletInTotalPointTradeRecord",
			    contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success: function(res) {
					document.getElementById("tipContent").innerText="批注添加成功";
					$("#Tip").modal('show');
					$("#postilModal").modal('hide');
					KYCReady();
					//$("#KYCGrid").bootstrapTable('refresh');
				}, 
				error: function(){
					document.getElementById("tipContent").innerText="批注添加失败";
					$("#Tip").modal('show');
					$("#postilModal").modal('hide');
				}
				}); 
	}else{
		alert("批注不能为空！");
	}
	
	
}

function addNC(){
	if(check1==1){
		var agId = $("#agId").val();
		var name=$("#name").val();
		var card=$("#card").val();
		
		if(name!=""){
			var data={
					"status":2,
					"userName":agId,
					"userIdentityName":name,
					"userIdentityNumber":card,
					}

				$.ajax({		
					url: "/api/v1/energyPoint/inqureEnergyWalletInTotalPointTradeRecord",
					contentType : 'application/json;charset=utf8',
					dataType: 'json',
					cache: false,
					type: 'post',
					data:JSON.stringify(data),
					processData : false,
					async : false,

					success: function(res) {
						document.getElementById("tipContent").innerText="信息添加成功";
						$("#Tip").modal('show');
						$("#addNCModal").modal('hide');
						KYCReady();
						//$("#KYCGrid").bootstrapTable('refresh');
					}, 
					error: function(){
						document.getElementById("tipContent").innerText="信息添加失败";
						$("#Tip").modal('show');
						$("#addNCModal").modal('hide');
					}
					}); 
		}else{
			alert("请确认输入信息！");
			$("#Tip").modal('show');
			location.reload();
		}		
		
	}else{
		alert("请确认输入信息！");
		$("#Tip").modal('show');
		location.reload();
	}
}