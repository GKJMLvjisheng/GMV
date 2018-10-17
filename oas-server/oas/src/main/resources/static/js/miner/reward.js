
document.write("<script language=javascript src='/js/miner/rewardTable.js'></script>");
var check1;
var check2;
var check3;
$(function() {
	//初始加载	
	rewardReady();
});

function rewardReady(){
	
    $('#rewardGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({		
		url: "/api/v1/userCenter/inqureAllPromotedRewardConfig",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//debugger;
			data2=res.data;
			initRewardGrid(data2);
		}, 
		error: function(){
			alert("矿机详细信息回显失败！")
		}
		}); 
}

function checkFrozenRate() {
	
	var rewardName = $("#rewardName").val();
	var frozenRatio = $("#frozenRatio").val();	
	var rate = frozenRatio.indexOf("%"); //%出现的位置
	//alert(rate);
	var len = frozenRatio.length;
	//alert(JSON.stringify(len));
	if(rate!=len-1){ 
		$("#msg_frozenRatio").html("请输入0-100之间的数字并加%，例如20%"); //判断%在不在最后一位
		$("#msg_frozenRatio").css("color", "red");
		check1 = 0;
		return check1;
	}else{
		var str = frozenRatio.split("%");
		var digit = str[0];
		var intRatio = parseFloat(digit);		
		//alert(JSON.stringify(intRatio));		
		
		if(rewardName=="算力"){
			check1 = 1;
			return check1;
		}else{
			if (intRatio>0 && intRatio< 100) {								
				$("#msg_frozenRatio").html("输入比例符合要求");
				$("#msg_frozenRatio").css("color", "green");
				check1 = 1;
				return check1;		
			}else{
				$("#msg_frozenRatio").html("请输入0-100之间的数字并加%，例如20%");
		        $("#msg_frozenRatio").css("color", "red");
		        check1 = 0;
				return check1;
			}
		}
	}
}

function checkRewardRate() {
	var rewardRatio = $("#rewardRatio").val();
	var rate = rewardRatio.indexOf("%"); //%出现的位置
	//alert(rate);
	var len = rewardRatio.length;
	//alert(JSON.stringify(len));
	if(rate!=len-1){ 
		$("#msg_rewardRatio").html("请输入0-100之间的数字并加%，例如20%"); //判断%在不在最后一位
		$("#msg_rewardRatio").css("color", "red");
		check2 = 0;
		return check2;
	}else{
		var str = rewardRatio.split("%");
		var digit = str[0];
		var intRatio = parseFloat(digit);		
		//alert(JSON.stringify(intRatio));
		if (intRatio>0 && intRatio< 100) {								
			$("#msg_rewardRatio").html("输入比例符合要求");
			$("#msg_rewardRatio").css("color", "green");
			check2 = 1;
			return check2;		
		}else{
			$("#msg_rewardRatio").html("请请输入0-100之间的数字并加%，例如20%");
	        $("#msg_rewardRatio").css("color", "red");
	        check2 = 0;
			return check2;
		}
	}
	
}

function checkGrade() {
	var maxPromotedGrade = $("#maxPromotedGrade").val();
	var intGrade = parseInt(maxPromotedGrade);
	if (intGrade>0 && intGrade<= 10) {								
		$("#msg_maxPromotedGrade").html("输入级别数字可用");
		$("#msg_maxPromotedGrade").css("color", "green");
		check3 = 1;
		return check3;		
	}else{
		$("#msg_maxPromotedGrade").html("请输入0-10之间的数字，包括10！");
        $("#msg_maxPromotedGrade").css("color", "red");
        check3 = 0;
		return check3;
	}
}

function updateReward(){
	var promotedId=$("#promotedId").val();
	
	var frozenRatio1=$("#frozenRatio").val();
	var str1 = frozenRatio1.split("%");
	var frozenRatio2 = str1[0];  //类型为string
	var intRatio1 = parseFloat(frozenRatio2);  //类型为number
	//alert(JSON.stringify(typeof(intRatio1)));
	var frozenRatio = intRatio1/100;
	
	var rewardRatio1=$("#rewardRatio").val();
	var str2 = rewardRatio1.split("%");
	var rewardRatio2 = str2[0];
	var intRatio2 = parseFloat(rewardRatio2);	
	var rewardRatio = intRatio2/100;
	//alert(JSON.stringify(rewardRatio));
	
	var maxPromotedGrade=$("#maxPromotedGrade").val();	
	
	var data={
		"promotedId":promotedId,
		"frozenRatio":frozenRatio,
		"rewardRatio":rewardRatio,
		"maxPromotedGrade":maxPromotedGrade,
	}	
	checkFrozenRate();
	checkRewardRate();
	checkGrade();
	if(check1==1 && check2==1 && check3==1){
		$.ajax({
			url:"/api/v1/userCenter/updatePromotedRewardConfig",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data:JSON.stringify(data),
			processData : false,
			async : false,

			success:function(res){	
				if(res.code==0){		
					alert("修改成功");
					location.reload();
				}
				else{
					alert("修改失败！");
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
