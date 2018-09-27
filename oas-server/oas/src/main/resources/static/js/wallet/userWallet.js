/**
 * 答题管理js文件
 */
document.write("<script language=javascript src='/js/wallet/userWalletTable.js'></script>");

//主界面用户表格回显
$(function() {
	//初始加载	
	UserWalletReady();
});

function UserWalletReady(){
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
			initUserWalletGrid(data2);
		}, 
		error: function(){
			alert("能量钱包回显失败！")
		}
		}); 
}