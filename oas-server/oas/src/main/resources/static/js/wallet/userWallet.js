
document.write("<script language=javascript src='/js/wallet/userWalletTable.js'></script>");

//主界面用户表格回显
$(function() {
	//初始加载	
	UserWalletReady();
	FundBigReady();
	FundInReady();
	FundOutReady();
});

//时间控件
$(function () {	 
	$('#userEd2').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime2 = $("#startTime2").val();
		$('#userEd3').datetimepicker('minDate',startTime2);
	 });

	$('#userEd3').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime2 = $("#startTime2").val();
		$('#userEd3').datetimepicker('minDate',startTime2);
	 });
	 
	$('#userEd4').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime3 = $("#startTime3").val();
		$('#userEd5').datetimepicker('minDate',startTime3);
	 });

	$('#userEd5').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
    }).on('dp.change', function (ev) {
		var startTime3 = $("#startTime3").val();
		$('#userEd5').datetimepicker('minDate',startTime3);
 	});
});

function todayDate(){
	var day = new Date();
 	day.setTime(day.getTime());
  	var s = day.getFullYear()+"-" + (day.getMonth()+1) + "-" + day.getDate();
  	return s;
}

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
			data2=res.data;
			initUserWalletGrid(data2);
		}, 
		error: function(){
			alert("用户钱包回显失败！")
		}
		}); 
}

function FundBigReady(){
    $('#fundBigGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/userWallet/inqureUserWalletBalanceRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			alert(JSON.stringify(res));
			data2=res.data;
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
	data={"startTime":startTime2,"endTime":endTime2};
    $('#fundInGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/userWallet/inqureUserWalletInTotalTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			data2=res.data;
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
	data={"startTime":startTime3,"endTime":endTime3};
    $('#fundOutGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/userWallet/inqureUserWalletOutTotalTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			data2=res.data;
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
