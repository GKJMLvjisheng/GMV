
document.write("<script language=javascript src='/js/user/userManageTable.js'></script>");

//主界面用户表格回显
$(function() {
	initNormalGrid();
	initTestGrid();
	initSystemGrid();
});

function reset(name){
	var status = 2;
	
	$('#status').val(status);
	$('#name').val(name);
	document.getElementById("confirm").innerText="确认重置IMEI吗？";
	$("#IMEIModal").modal("show");
}

function Coordinate(name){
	var status = 3;
	var msg = "*";
	
	$('#status').val(status);
	$('#msg').val(msg);
	$('#name').val(name);
	document.getElementById("confirm").innerText="确认通配IMEI吗？";
	$("#IMEIModal").modal("show");
}

function disorder(name){
	var status = 1;
	var msg = "";
	$('#status').val(status);
	$('#msg').val(msg);
	$('#name').val(name);
	document.getElementById("confirm").innerText="确认紊乱IMEI吗？";
	$("#IMEIModal").modal("show");
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