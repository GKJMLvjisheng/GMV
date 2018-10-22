//主界面用户表格回显
$(function() {
	//初始加载	
	initEthWalletGrid();	
//	initFundInGrid();
//	initFundOutGrid();
});

//时间控件
$(function () {
     $('#userEd').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime2 = $("#startTime2").val();
		$('#userEd1').datetimepicker('minDate',startTime2);
	 });
	 
	 $('#userEd1').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime2 = $("#startTime2").val();
		$('#userEd1').datetimepicker('minDate',startTime2);
 	});
	
	$('#userEd2').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime3 = $("#startTime3").val();
		$('#userEd3').datetimepicker('minDate',startTime3);
	 });

	$('#userEd3').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime3 = $("#startTime3").val();
		$('#userEd3').datetimepicker('minDate',startTime3);
 	});
});

function todayDate(){
	var day = new Date();
 	day.setTime(day.getTime());
  	var s = day.getFullYear()+"-" + (day.getMonth()+1) + "-" + day.getDate();
  	return s;
}

//function EthWalletReady(){
//    $('#eneryWalletGrid').bootstrapTable('destroy');
//	var data2;
//	 $.ajax({
//		
//		url: "/api/v1/ethWallet/inqureEthWalletTradeRecord",
//	    contentType : 'application/json;charset=utf8',
//		dataType: 'json',
//		cache: false,
//		type: 'post',
//		success: function(res) {
//			data2=res.data;
//			initEthWalletGrid(data2);
//		}, 
//		error: function(){
//			alert("交易钱包回显失败！")
//		}
//		}); 
//}
//
//function FundInReady(){	
//	var startTime2=$("#startTime2").val();
//	var endTime2=$("#endTime2").val();
//	data={"startTime":startTime2,"endTime":endTime2};
//    $('#fundInGrid').bootstrapTable('destroy');
//	var data2;
//	 $.ajax({		
//		url: "/api/v1/ethWallet/inqureEthWalletInTotalTradeRecord",
//	    contentType : 'application/json;charset=utf8',
//		dataType: 'json',
//		cache: false,
//		type: 'post',
//		data:JSON.stringify(data),
//		processData : false,
//		async : false,
//
//		success: function(res) {
//			data2=res.data;
//			initFundInGrid(data2);
//		}, 
//		error: function(){
//			alert("资金流入Top榜回显失败！")
//		}
//		}); 
//}
//function FundOutReady(){
//	var startTime3=$("#startTime3").val();
//	var endTime3=$("#endTime3").val();
//	data={"startTime":startTime3,"endTime":endTime3};
//
//    $('#fundOutGrid').bootstrapTable('destroy');
//	var data2;
//	 $.ajax({		
//		url: "/api/v1/ethWallet/inqureEthWalletOutTotalTradeRecord",
//	    contentType : 'application/json;charset=utf8',
//		dataType: 'json',
//		cache: false,
//		type: 'post',
//		data:JSON.stringify(data),
//		processData : false,
//		async : false,
//
//		success: function(res) {
//			data2=res.data;
//			initFundOutGrid(data2);
//		}, 
//		error: function(){
//			alert("资金流出Top榜回显失败！")
//		}
//		}); 
//}

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
