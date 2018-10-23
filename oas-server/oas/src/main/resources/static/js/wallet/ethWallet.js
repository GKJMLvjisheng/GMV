
document.write("<script language=javascript src='/js/wallet/ethWalletTable.js'></script>");

$(function() {
	//初始加载	
	initEthWalletGrid();	
	initFundInGrid();
	initFundOutGrid();
});

//时间控件
$(function () {
     $('#userEd').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime2 = $("#startTime4").val();
		$('#userEd1').datetimepicker('minDate',startTime2);
	 });
	 
	 $('#userEd1').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime2 = $("#startTime4").val();
		$('#userEd1').datetimepicker('minDate',startTime2);
 	});
	
	$('#userEd2').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime3 = $("#startTime5").val();
		$('#userEd3').datetimepicker('minDate',startTime3);
	 });

	$('#userEd3').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime3 = $("#startTime5").val();
		$('#userEd3').datetimepicker('minDate',startTime3);
 	});
});

function todayDate(){
	var day = new Date();
 	day.setTime(day.getTime());
  	var s = day.getFullYear()+"-" + (day.getMonth()+1) + "-" + day.getDate();
  	return s;
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
