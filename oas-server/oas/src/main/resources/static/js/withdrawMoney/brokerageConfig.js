var check1;
var check2;
var check3;

$(function() {
	//初始加载	
	brokerageReady();
});

function brokerageReady(){
	
	var data2;
	 $.ajax({		
		url: " /api/v1/userWallet/getOasExtra",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'get',
		success: function(res) {
			//alert(JSON.stringify(res.data))
			data=res.data;
			var array = new Array();
			array[0] = data;
			initBrokeragedGrid(array);
		}, 
		error: function(){
			alert("提币手续费配置回显失败！")
		}
		}); 
}

//检查输入的提币单次限额是否符合要求
function checkMostAmount() {
	var mostAmount = $("#mostAmount").val();
	var len = mostAmount.length;
	if(len==0){ 
		$("#msg_mostAmount").html("请输入正数"); //判断%在不在最后一位
		$("#msg_mostAmount").css("color", "red");
	}else{
		if (/^\d+(?=\.{0,1}\d+$|$)/.test(mostAmount)) {
			if (parseFloat(mostAmount)>0) {								
				$("#msg_mostAmount").html("输入符合要求");  
				$("#msg_mostAmount").css("color", "green");
				check1 = 1;
				return check1;		
			}else{
				$("#msg_mostAmount").html("请输入正数");
		        $("#msg_mostAmount").css("color", "red");
		        check1 = 0;
				return check1;
			}
		}else{
			$("#msg_mostAmount").html("请输入正数");
	        $("#msg_mostAmount").css("color", "red");	
	        check1 = 0;
			return check1;
		}
	}	
}

//检查输入的手续费是否符合要求
function checkBrokerage() {
	var lessBrokerage = $("#lessBrokerage").val();
	var len = lessBrokerage.length;
	if(len==0){ 
		$("#msg_lessBrokerage").html("请输入正数"); //判断%在不在最后一位
		$("#msg_lessBrokerage").css("color", "red");
	}else{
		if (/^\d+(?=\.{0,1}\d+$|$)/.test(lessBrokerage)) {
			if (parseFloat(lessBrokerage)>0) {								
				$("#msg_lessBrokerage").html("输入符合要求");  // (?=(">)) 表示 匹配以(">)结尾的字符串，并且捕获(存储)到分组中
				$("#msg_lessBrokerage").css("color", "green");
				check2 = 1;
				return check2;		
			}else{
				$("#msg_lessBrokerage").html("请输入正数");
		        $("#msg_lessBrokerage").css("color", "red");
		        check2 = 0;
				return check2;
			}
		}else{
			$("#msg_lessBrokerage").html("请输入正数");
	        $("#msg_lessBrokerage").css("color", "red");	
	        check2 = 0;
			return check2;
		}
	}	
}

//检查输入的手续费比例是否符合要求
function checkBrokerageRate() {
	
	var brokerageMoney = $("#brokerageMoney").val();	
	var rate = brokerageMoney.indexOf("%"); //%出现的位置
	//alert(rate);
	var len = brokerageMoney.length;
	//alert(JSON.stringify(len));
	if(rate!=len-1){ 
		$("#msg_brokerageMoney").html("请输入0-100之间的数字并加%，例如2%"); //判断%在不在最后一位
		$("#msg_brokerageMoney").css("color", "red");
	}else{
		var str = brokerageMoney.split("%");
		var digit = str[0];
		var intRatio = parseFloat(digit);		
		//alert(JSON.stringify(digit));						
			
		if (/^\d+(?=\.{0,1}\d+$|$)/.test(digit)) {
			if (intRatio>0 && intRatio< 100) {								
				$("#msg_brokerageMoney").html("输入比例符合要求");  ///^\d+(?=\.{0,1}\d+$|$)/为正数，包括0
				$("#msg_brokerageMoney").css("color", "green");
				check3 = 1;
				return check3;		
			}else{
				$("#msg_brokerageMoney").html("请输入0-100之间的数字并加%，例如2%");
		        $("#msg_brokerageMoney").css("color", "red");
		        check3 = 0;
				return check3;
			}
		}else{
			$("#msg_brokerageMoney").html("请输入0-100之间的数字并加%，例如2%");
	        $("#msg_brokerageMoney").css("color", "red");
	        check3 = 0;
			return check3;
		}
		
	}
}


function updateBrokerage(){
	var mostAmount=$("#mostAmount").val();	
	var lessBrokerage=$("#lessBrokerage").val();
	
	var brokerageMoney1=$("#brokerageMoney").val();				
	var str1 = brokerageMoney1.split("%");
	var brokerageMoney2 = str1[0];  //类型为string
	var intRatio1 = parseFloat(brokerageMoney2);  //类型为number
	var brokerageMoney3 = intRatio1/100;
	var brokerageMoney = brokerageMoney3.toString();  //类型为string
	//alert(JSON.stringify(brokerageMoney3));
	
	var data={
		"valueMax":mostAmount,
		"valueMin":lessBrokerage,
		"value":brokerageMoney,
	}	
	checkMostAmount();
	checkBrokerage();
	checkBrokerageRate();
	
	if(check1==1 && check2==1 && check3==1){
		$.ajax({
			url:"/api/v1/userWallet/updateOasExtra",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data:JSON.stringify(data),
			processData : false,
			async : false,

			success:function(res){	
				//alert(JSON.stringify(res));
				if(res.code==0){		
					alert("修改成功");
					location.reload();
				}
				else{
					alert(res.message);
				}						
			},
			error:function(){
				alert("修改过程发生错误！");	
			},
		});		
	}else{
		alert("请确认修改内容是否符合要求！")
	}	
}