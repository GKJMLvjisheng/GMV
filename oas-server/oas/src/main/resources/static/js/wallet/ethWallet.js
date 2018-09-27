
//主界面用户表格回显
$(function() {
	//初始加载	
	EthWalletReady();
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
