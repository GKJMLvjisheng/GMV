/**
 * 答题管理js文件
 */
document.write("<script language=javascript src='/js/wallet/eneryWalletTable.js'></script>");

//主界面用户表格回显
$(function() {
	//初始加载	
	EneryWalletReady();
});

function EneryWalletReady(){
    $('#eneryWalletGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({
		
		url: "/api/v1/computingPower/selectAllTopic",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data.list;
			//alert(JSON.stringify(data2));
			initEneryWalletGrid(data2);
		}, 
		error: function(){
			alert("能量钱包回显失败！")
		}
		}); 
}

function display1()
{document.getElementById("page2").style.display="none";
document.getElementById("page1").style.display="block";
$('#btn1').removeClass('active1').addClass('active');
$('#btn2').removeClass('active').addClass('active1');
}
function display2()
{
	//$("#page2").attr()
	document.getElementById("page1").style.display="none";
	document.getElementById("page2").style.display="block";
	$('#btn2').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
}