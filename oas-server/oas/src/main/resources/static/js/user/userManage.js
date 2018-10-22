
document.write("<script language=javascript src='/js/user/userManageTable.js'></script>");

//主界面用户表格回显
$(function() {
	normalReady();
	testReady();
	systemReady();
});



function normalReady(){
    $('#normalGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({
		
		url: "/api/v1/energyPoint/inqureEnergyWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			data2=res.data;
			initNormalGrid(data2);
		}, 
		error: function(){
			alert("正常账号回显失败！")
		}
		}); 
}

function testReady(){
    $('#testGrid').bootstrapTable('destroy');
	var data2;

	 $.ajax({		
		url: "/api/v1/energyPoint/inqureEnergyWalletBalanceRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			data2=res.data.rows;
			initTestGrid(data2);
		}, 
		error: function(){
			alert("测试账号回显失败！")
		}
		}); 
}
function systemReady(){
	
    $('#systemGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/energyPoint/inqureEnergyWalletInTotalPointTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		processData : false,
		async : false,

		success: function(res) {
			data2=res.data;
			initSystemGrid(data2);
		}, 
		error: function(){
			alert("系统账号回显失败！");
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