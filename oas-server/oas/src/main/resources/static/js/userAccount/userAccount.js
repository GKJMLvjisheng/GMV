
document.write("<script language=javascript src='/js/userAccount/userAccountTable.js'></script>");

//主界面用户表格回显
$(function() {
	initNormalGrid();
	initTestGrid();
	initSystemGrid();
	
});

function reset(name){
	
	$('#name').val(name);
	document.getElementById("confirm").innerText="确认重置IMEI吗？";
	$("#IMEIModal").modal("show");
}

function Confirm(){
	
	var name = $("#name").val();	
	var data={		
		"name":name,		
		}

	$.ajax({		
		url: "/api/v1/userCenter/updateIMEI",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			if(res.code==0){
				document.getElementById("tipContent").innerText="重置过程完成";
				$("#Tip").modal('show');	
//				var pageNumber = $("#KYCGrid").bootstrapTable('getOptions').pageNumber;
//				$("#KYCGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
					initNormalGrid();			
					initTestGrid();				
					initSystemGrid();								
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
			}			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="重置过程发生错误";
			$("#Tip").modal('show');
		}
	}); 
}

function role(name,roleId){
	if(roleId==2){
		var rows=$("#normalGrid").bootstrapTable('getRowByUniqueId', name);
	}else if(roleId==3){
		var rows=$("#testGrid").bootstrapTable('getRowByUniqueId', name);
	}else if(roleId==1){
		var rows=$("#systemGrid").bootstrapTable('getRowByUniqueId', name);  //系统账号
	}
    
    //alert(JSON.stringify(rows))
    $('#roleId').val(rows.roleId);
	$('#roleName').val(rows.name);
	$('#status').val(rows.status);
	
	$(":radio[name='radio1'][value='" + rows.roleId + "']").prop("checked", "checked");			
	$("#roleModal").modal("show");  
	
	$(":radio").click(function(){
		var choice=$(this).val();
		$('#roleId').val(choice);
	});
}

function roleConfirm(){
	var roleId = $("#roleId").val();
	var name = $("#roleName").val();	
	var data={		
		"name":name,
		"roleId":roleId,
		}

	$.ajax({		
		url: "/api/v1/userCenter/updateUserRole",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			if(res.code==0){
				document.getElementById("tipContent").innerText="授权过程完成";
				$("#Tip").modal('show');				
					initNormalGrid();			
					initTestGrid();				
					initSystemGrid();								
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
			}			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="授权过程发生错误";
			$("#Tip").modal('show');
		}
	}); 
}

function control(name,roleId){
	if(roleId==2){
		var rows=$("#normalGrid").bootstrapTable('getRowByUniqueId', name);
	}else if(roleId==3){
		var rows=$("#testGrid").bootstrapTable('getRowByUniqueId', name);
	}else if(roleId==1){
		var rows=$("#systemGrid").bootstrapTable('getRowByUniqueId', name);
	}
    //alert(JSON.stringify(rows));
    //$('#roleId').val(rows.roleId);
	$('#controlName').val(rows.name);
	$('#status').val(rows.status);

	
	$(":radio[name='radio2'][value='" + rows.status + "']").prop("checked", "checked");			
	$("#controlModal").modal("show");
	
	$(":radio").click(function(){
		var choice=$(this).val();
		$('#status').val(choice);
	});
}

function controlConfirm(){
	var status = $("#status").val();
	var name = $("#controlName").val();	
	var data={		
		"name":name,
		"status":status,
		}

	$.ajax({		
		url: "/api/v1/userCenter/updateUserStatus",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			if(res.code==0){
				document.getElementById("tipContent").innerText="操作过程完成";
				$("#Tip").modal('show');				
					initNormalGrid();			
					initTestGrid();				
					initSystemGrid();								
			}else{
				document.getElementById("tipContent").innerText=res.message;
				$("#Tip").modal('show');
			}			
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="操作过程发生错误";
			$("#Tip").modal('show');
		}
	}); 
}

//点击取消后清空表单中已写信息
function resetModal(){
	//document.getElementById("addNewsForm").reset();
	location.reload();
}

function display1(){
	document.getElementById("page3").style.display="none";
	document.getElementById("page4").style.display="none";
	document.getElementById("page1").style.display="block";
	$('#btn1').removeClass('active1').addClass('active');
	$('#btn3').removeClass('active').addClass('active1');
	$('#btn4').removeClass('active').addClass('active1');
}

function display3(){
	document.getElementById("page1").style.display="none";
	document.getElementById("page4").style.display="none";
	document.getElementById("page3").style.display="block";
	$('#btn3').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
	$('#btn4').removeClass('active').addClass('active1');
}
function display4(){
	document.getElementById("page1").style.display="none";
	document.getElementById("page3").style.display="none";
	document.getElementById("page4").style.display="block";
	$('#btn4').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
	$('#btn3').removeClass('active').addClass('active1');
}