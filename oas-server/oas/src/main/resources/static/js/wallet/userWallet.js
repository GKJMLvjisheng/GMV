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
	var startTime1=$("#startTime1").val();
	var endTime1=$("#endTime1").val();
	// alert(JSON.stringify(startTime1));
	// alert(JSON.stringify(endTime1));
	data={"startTime":startTime1,"endTime":endTime1};

    $('#fundBigGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		//url: "/api/v1/userWallet/inqureUserWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
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
	var startTime2=$("#startTime2").val();
	var endTime2=$("#endTime2").val();
	// alert(JSON.stringify(startTime2));
	// alert(JSON.stringify(endTime2));
	data={"startTime":startTime2,"endTime":endTime2};

    $('#fundInGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		//url: "/api/v1/userWallet/inqureUserWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
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
	var startTime3=$("#startTime3").val();
	var endTime3=$("#endTime3").val();
	// alert(JSON.stringify(startTime3));
	// alert(JSON.stringify(endTime3));
	data={"startTime":startTime3,"endTime":endTime3};

    $('#fundOutGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		//url: "/api/v1/userWallet/inqureUserWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
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
