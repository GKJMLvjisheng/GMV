/**
 * 答题管理js文件
 */
document.write("<script language=javascript src='/js/wallet/userWalletTable.js'></script>");

//主界面用户表格回显
$(function() {
	//初始加载	
	UserWalletReady();
	// FundBigReady();
	// FundInReady();
	// FundOutReady();
});

//时间控件
$(function () {
    $('#userEd').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	});	
	$('#userEd1').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	});
	$('#userEd2').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	});
	$('#userEd3').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	});
	$('#userEd4').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	});
	$('#userEd5').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
    });
});

function UserWalletReady(){
    $('#fundBigGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/userWallet/inqureUserWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data;
			//alert(JSON.stringify(data2));
			initUserWalletGrid(data2);
		}, 
		error: function(){
			alert("用户钱包回显失败！")
		}
		}); 
}

function FundBigReady(){
	var formData = new FormData();
	formData.append("startTime",$("#startTime1").val());
	formData.append("endTime",$("#endTime1").val());
	alert(JSON.stringify($("#startTime1").val()));
	alert(JSON.stringify($("#endTime1").val()));

    $('#fundBigGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		//url: "/api/v1/userWallet/inqureUserWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:formData,
		processData : false,
		async : false,

		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data;
			//alert(JSON.stringify(data2));
			initFundBigGrid(data2);
		}, 
		error: function(){
			alert("资金大户Top榜回显失败！")
		}
		}); 
}
function FundInReady(){
	var formData = new FormData();
	formData.append("startTime",$("#startTime2").val());
	formData.append("endTime",$("#endTime2").val());
	alert(JSON.stringify($("#startTime2").val()));
	alert(JSON.stringify($("#endTime2").val()));	

    $('#fundInGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		//url: "/api/v1/userWallet/inqureUserWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:formData,
		processData : false,
		async : false,

		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data;
			//alert(JSON.stringify(data2));
			initFundInGrid(data2);
		}, 
		error: function(){
			alert("资金流入Top榜回显失败！")
		}
		}); 
}
function FundOutReady(){
	var formData = new FormData();
	formData.append("startTime",$("#startTime3").val());
	formData.append("endTime",$("#endTime3").val());
	alert(JSON.stringify($("#startTime3").val()));
	alert(JSON.stringify($("#endTime3").val()));

    $('#fundOutGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		//url: "/api/v1/userWallet/inqureUserWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:formData,
		processData : false,
		async : false,

		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data;
			//alert(JSON.stringify(data2));
			initFundOutGrid(data2);
		}, 
		error: function(){
			alert("资金流出Top榜回显失败！")
		}
		}); 
}

function display1(){
	document.getElementById("page2").style.display="none";
	document.getElementById("page3").style.display="none";
	document.getElementById("page4").style.display="none";
	document.getElementById("page1").style.display="block";
	$('#btn1').removeClass('active1').addClass('active');
	$('#btn2').removeClass('active').addClass('active1');
	$('#btn3').removeClass('active').addClass('active1');
	$('#btn4').removeClass('active').addClass('active1');
}
function display2(){
	document.getElementById("page2").style.display="block";
	document.getElementById("page1").style.display="none";
	document.getElementById("page3").style.display="none";
	document.getElementById("page4").style.display="none";	
	$('#btn2').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
	$('#btn3').removeClass('active').addClass('active1');
	$('#btn4').removeClass('active').addClass('active1');
}

function display3(){
	document.getElementById("page1").style.display="none";
	document.getElementById("page2").style.display="none";
	document.getElementById("page4").style.display="none";
	document.getElementById("page3").style.display="block";
	$('#btn3').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
	$('#btn2').removeClass('active').addClass('active1');
	$('#btn4').removeClass('active').addClass('active1');
}
function display4(){
	document.getElementById("page1").style.display="none";
	document.getElementById("page2").style.display="none";
	document.getElementById("page3").style.display="none";
	document.getElementById("page4").style.display="block";
	$('#btn4').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
	$('#btn2').removeClass('active').addClass('active1');
	$('#btn3').removeClass('active').addClass('active1');
}
