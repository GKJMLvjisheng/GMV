
document.write("<script language=javascript src='/js/userAccount/userAccountTable.js'></script>");

//主界面用户表格回显
$(function() {
	initNormalGrid();
	initTestGrid();
	initSystemGrid();
	
});

function reset(name,roleId){
	
	$('#name').val(name);
	$('#roleId3').val(roleId);
	document.getElementById("confirm").innerText="确认重置IMEI吗？";
	$("#IMEIModal").modal("show");
}

function Confirm(){
	var roleId3 = $("#roleId3").val();	
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
				if(roleId3==2){
					var pageNumber1 = $("#normalGrid").bootstrapTable('getOptions').pageNumber;
					$("#normalGrid").bootstrapTable('selectPage',pageNumber1);  //刷新当前页
					
				}else if(roleId3==3){
					var pageNumber2 = $("#testGrid").bootstrapTable('getOptions').pageNumber;
					$("#testGrid").bootstrapTable('selectPage',pageNumber2);  //刷新当前页
					
				}else if(roleId3==1){
					var pageNumber3 = $("#systemGrid").bootstrapTable('getOptions').pageNumber;
					$("#systemGrid").bootstrapTable('selectPage',pageNumber3);  //刷新当前页
					
				}		
				
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
    $('#roleId1').val(rows.roleId);
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
	var roleId1 = $("#roleId1").val();
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
				if(roleId1==2){
					var pageNumber1 = $("#normalGrid").bootstrapTable('getOptions').pageNumber;
					$("#normalGrid").bootstrapTable('selectPage',pageNumber1);  //刷新正常账号当前页
					initTestGrid();				
					initSystemGrid();
				}else if(roleId1==3){
					var pageNumber2 = $("#testGrid").bootstrapTable('getOptions').pageNumber;
					$("#testGrid").bootstrapTable('selectPage',pageNumber2);  //刷新测试账号当前页
					initNormalGrid();			
					initSystemGrid();
				}
//				else if(roleId1==1){
//					var pageNumber3 = $("#systemGrid").bootstrapTable('getOptions').pageNumber;
//					$("#systemGrid").bootstrapTable('selectPage',pageNumber3);  //刷新系统账号当前页
//					initNormalGrid();			
//					initTestGrid();
//				}
											
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
    $('#roleId2').val(rows.roleId);
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
	var roleId2 = $("#roleId2").val();
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
				
				if(roleId2==2){
					var pageNumber1 = $("#normalGrid").bootstrapTable('getOptions').pageNumber;
					$("#normalGrid").bootstrapTable('selectPage',pageNumber1);  //刷新正常账号当前页
					
				}else if(roleId2==3){
					var pageNumber2 = $("#testGrid").bootstrapTable('getOptions').pageNumber;
					$("#testGrid").bootstrapTable('selectPage',pageNumber2);  //刷新测试账号当前页
					
				}
//				else if(roleId2==1){
//					var pageNumber3 = $("#systemGrid").bootstrapTable('getOptions').pageNumber;
//					$("#systemGrid").bootstrapTable('selectPage',pageNumber3);  //刷新当前页
//					
//				}							
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

//重置IMEI 点击取消后清空表单中已写信息
function resetIMEIModal(){
	var roleId3 = $("#roleId3").val();
	if(roleId3==2){
		var pageNumber1 = $("#normalGrid").bootstrapTable('getOptions').pageNumber;
		$("#normalGrid").bootstrapTable('selectPage',pageNumber1);  //刷新当前页
		
	}else if(roleId3==3){
		var pageNumber2 = $("#testGrid").bootstrapTable('getOptions').pageNumber;
		$("#testGrid").bootstrapTable('selectPage',pageNumber2);  //刷新当前页
		
	}else if(roleId3==1){
		var pageNumber3 = $("#systemGrid").bootstrapTable('getOptions').pageNumber;
		$("#systemGrid").bootstrapTable('selectPage',pageNumber3);  //刷新当前页
		
	}
	//location.reload();
}

//角色授权 点击取消后清空表单中已写信息
function resetRoleModal(){
	var roleId1 = $("#roleId1").val();
	if(roleId1==2){
		var pageNumber1 = $("#normalGrid").bootstrapTable('getOptions').pageNumber;
		$("#normalGrid").bootstrapTable('selectPage',pageNumber1);  //刷新当前页
		
	}else if(roleId1==3){
		var pageNumber2 = $("#testGrid").bootstrapTable('getOptions').pageNumber;
		$("#testGrid").bootstrapTable('selectPage',pageNumber2);  //刷新当前页
		
	}
}

//账号状态 点击取消后清空表单中已写信息
function resetControlModal(){
	var roleId2 = $("#roleId2").val();
	if(roleId2==2){
		var pageNumber1 = $("#normalGrid").bootstrapTable('getOptions').pageNumber;
		$("#normalGrid").bootstrapTable('selectPage',pageNumber1);  //刷新当前页
		
	}else if(roleId2==3){
		var pageNumber2 = $("#testGrid").bootstrapTable('getOptions').pageNumber;
		$("#testGrid").bootstrapTable('selectPage',pageNumber2);  //刷新当前页
		
	}
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