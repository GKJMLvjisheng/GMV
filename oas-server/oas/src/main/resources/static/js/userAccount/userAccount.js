
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

function normal(name){
	var roleId = 2;
	$('#roleId').val(roleId);
	$('#roleName').val(name);
	document.getElementById("role").innerText="确认授权为正常账号吗？";
	$("#roleModal").modal("show");
}

function test(name){
	var roleId = 3;
	$('#roleId').val(roleId);
	$('#roleName').val(name);
	document.getElementById("role").innerText="确认授权为测试账号吗？";
	$("#roleModal").modal("show");
}

function system(name){
	var roleId = 1;
	$('#roleId').val(roleId);
	$('#roleName').val(name);
	document.getElementById("role").innerText="确认授权为系统账号吗？";
	$("#roleModal").modal("show");
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

function active(name){
	var status = 1;
	$('#status').val(status);
	$('#activeName').val(name);
	document.getElementById("active").innerText="确认激活该账号吗？";
	$("#activeModal").modal("show");
}

function ban(name){
	var status = 0;
	$('#status').val(status);
	$('#activeName').val(name);
	document.getElementById("active").innerText="确认禁用该账号吗？";
	$("#activeModal").modal("show");
}

function activeConfirm(){
	var status = $("#status").val();
	var name = $("#activeName").val();	
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