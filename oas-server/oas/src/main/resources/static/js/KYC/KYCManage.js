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
			data1=res.data;
			initKYCGrid(data1);
		}, 
		error: function(){
			alert("KYC审核回显失败！")
		}
		}); 
}

//发ajax请求到后台判断身份证号是否重复
function checkCard(card) {
	
    if (card != "") {
	  var data = {
        "userIdentityNumber" : card
      };
      $.ajax({
        url: "/api/v1/userCenter/checkIdentityNumber",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

        success : function(res) {
          if (res.data == 1) {
//            $("#msg_card").html("身份证号可以使用");
//            $("#msg_card").css("color", "green");
            check1 = 1;
            return check1;
          } else if(res.data == 0) {
//            $("#msg_card").html("身份证号已存在");
//            $("#msg_card").css("color", "red");
            check1 = 0;
            return check1;
          }
        },
        error : function() {
          alert('检查身份证号是否存在发生错误');
        }
      });
    }else{
//    	 $("#msg_card").html("请填写身份证号！");
//         $("#msg_card").css("color", "red");
    }
}

function addPostil(){
	var reId = $("#reId").val();
	var postil=$("#postil").val();	
	
	if(postil!=""){
		data={
				"verifyStatus":3,
				"uuid":reId,
				"remark":postil,
				}

			 $.ajax({		
				url: "/api/v1/userCenter/checkUserIdentity",
			    contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success: function(res) {
					alert("批注添加成功");
					location.reload();
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
	
	var agId = $("#agId").val();
	var agStatus = $("#agStatus").val();
	var name=$("#name").val();
	var card=$("#card").val();	
	
	if(agStatus=="2"||agStatus=="3"){
		alert("审核已完成，不再允许提交信息！")
	}else{
		var data={
				"uuid":agId,
				"userIdentityName":name,
				"userIdentityNumber":card,
				}

			$.ajax({		
				url: "/api/v1/userCenter/checkUserIdentity",
				contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success: function(res) {
					alert("信息添加成功");
					location.reload();
				}, 
				error: function(){
					document.getElementById("tipContent").innerText="信息添加失败";
					$("#Tip").modal('show');
					$("#addNCModal").modal('hide');
				}
				}); 	
	}		
}

function agree(id){
	
	var rows=$("#KYCGrid").bootstrapTable('getRowByUniqueId', id);
	var uuid = id;
	alert(JSON.stringify(uuid))
	$('#name').val(rows.userIdentityName);
	$('#card').val(rows.userIdentityNumber);
	var name=rows.userIdentityName;
	var card=rows.userIdentityNumber;
//	alert(JSON.stringify(name));
//	alert(JSON.stringify(card));
	var data={
		"verifyStatus":2,
		"uuid":uuid,
		}
	
	if(name!=null && name!="" && card!=null && card!="" ){
		
		checkCard(card);
		//alert(JSON.stringify(check1));
		if(check1==1){
			
			$.ajax({		
				url: "/api/v1/userCenter/checkUserIdentity",
				contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success: function(res) {
					if(res.code==0){
						alert("批准成功");
						location.reload();
					}else{
						document.getElementById("tipContent").innerText="批准过程发生错误1";
						$("#Tip").modal('show');
					}
					
				}, 
				error: function(){
					document.getElementById("tipContent").innerText="批准过程发生错误2";
					$("#Tip").modal('show');
				}
				}); 	
		}else if(check1==0){
			alert("该身份证号已存在！")
		}
	}else{
		alert("请输入姓名、身份证号！")
	}				
}
