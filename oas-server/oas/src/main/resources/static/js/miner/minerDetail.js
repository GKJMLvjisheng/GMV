
$(function() {
	initPurchaseDetailGrid();
	minerReady();
	
});

function minerReady(){
	
	var data2;
	 $.ajax({		
		url: "/api/v1/miner/inquireWebMiner",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data;
			initMinerSellGrid(data2);
		}, 
		error: function(){
			alert("矿机销售统计回显失败！")
		}
		}); 
}

function display1(){
	document.getElementById("page2").style.display="none";
	document.getElementById("page1").style.display="block";
	$('#btn1').removeClass('active1').addClass('active');
	$('#btn2').removeClass('active').addClass('active1');
}

function display2(){
	document.getElementById("page1").style.display="none";
	document.getElementById("page2").style.display="block";
	$('#btn2').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
}