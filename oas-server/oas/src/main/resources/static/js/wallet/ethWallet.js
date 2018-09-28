
//主界面用户表格回显
$(function() {
	//初始加载	
	EthWalletReady();
	// FundBigReady();
	FundInReady();
	FundOutReady();
});

function EthWalletReady(){
    $('#eneryWalletGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({
		
		url: "/api/v1/ethWallet/inqureEthWalletTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data;
			//alert(JSON.stringify(data2));
			initEthWalletGrid(data2);
		}, 
		error: function(){
			alert("交易钱包回显失败！")
		}
		}); 
}

function FundBigReady(){
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
			initFundBigGrid(data2);
		}, 
		error: function(){
			alert("资金大户Top榜回显失败！")
		}
		}); 
}
function FundInReady(){
    $('#fundInGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/ethWallet/inqureEthWalletInTotalTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
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
    $('#fundOutGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/ethWallet/inqureEthWalletOutTotalTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
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
