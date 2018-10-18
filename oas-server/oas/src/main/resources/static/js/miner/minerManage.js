
document.write("<script language=javascript src='/js/miner/minerManageTable.js'></script>");
var check1;
var check2;
var check3;
var check4;

var check5;
var check6;
var check7;
var check8;
$(function() {
	//初始加载	
	minerReady();
});

function minerReady(){
	
    $('#minerGrid').bootstrapTable('destroy');
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
			initMinerGrid(data2);
		}, 
		error: function(){
			alert("矿机详细信息回显失败！")
		}
		}); 
}

//发ajax请求到后台判断矿机是否重复--添加矿机
function checkName() {
	var minerName = $("#minerName").val(); 
	//alert(JSON.stringify(minerName));
    if (minerName != "") {
		var data = {
			"minerName" : minerName
		};
		$.ajax({
			url: "/api/v1/miner/inquireMinerName",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data:JSON.stringify(data),
			processData : false,
			async : false,

			success : function(res) {
				if (res.code == 0) {
					$("#msg_minerName").html("矿机名可以使用");
					$("#msg_minerName").css("color", "green");
					check1 = 1;
					return check1;
				} else{
					$("#msg_minerName").html("矿机名已存在");
					$("#msg_minerName").css("color", "red");
				}
			},
			error : function() {
				alert('检查矿机名是否存在发生错误');
			}
		});
    }else{
    	 $("#msg_minerName").html("请填写矿机名！");
         $("#msg_minerName").css("color", "red");
    }
}
//新增矿机--矿机单价的判断
function checkPrice() {
	
	var minerPrice = $("#minerPrice").val(); 
	var minerPrice1 = parseFloat(minerPrice);
	
	if(minerPrice!=""){
			
			if(minerPrice1!=0){
				if (/^\d+(?=\.{0,1}\d+$|$)/.test(minerPrice)) {
					 $("#msg_minerPrice").html("填写矿机单价符合要求");
			         $("#msg_minerPrice").css("color", "green");
			         check2 = 1;
					 return check2;
					
			    }else{
			    	
			         $("#msg_minerPrice").html("请填写大于0的数字！");
			         $("#msg_minerPrice").css("color", "red");
			    }
			}else{
				 $("#msg_minerPrice").html("请填写大于0的数字！");
		         $("#msg_minerPrice").css("color", "red");		   
			}
			 
		}else{
		   	 $("#msg_minerPrice").html("矿机单价不能为空！");
		     $("#msg_minerPrice").css("color", "red");		   
		}   
}
//新增矿机--算力奖励的判断
function checkPower() {

	var minerPower = $("#minerPower").val(); 
	var minerPower1 = parseFloat(minerPower);
	//alert(JSON.stringify(minerPower1));
	
	if(minerPower!=""){
		
		if(minerPower1!=0){
			if (/^\d+(?=\.{0,1}\d+$|$)/.test(minerPower)) {
				 $("#msg_minerPower").html("填写算力奖励符合要求");
		         $("#msg_minerPower").css("color", "green");
		         check3 = 1;
				 return check3;
				
		    }else{
		    	
		         $("#msg_minerPower").html("请填写大于0的数字！");
		         $("#msg_minerPower").css("color", "red");		       
		    }
		}else{
			 $("#msg_minerPower").html("请填写大于0的数字！");
	         $("#msg_minerPower").css("color", "red");	        
		}
		 
	}else{
	   	 $("#msg_minerPower").html("算力奖励不能为空！");
	     $("#msg_minerPower").css("color", "red");	     
	}   
}
//新增矿机--矿机周期的判断
function checkPeriod() {
	
	var minerPeriod = $("#minerPeriod").val(); 	
	//alert(JSON.stringify(minerPeriod));
	
	if(minerPeriod!=""){
		if (!/\D/.test(minerPeriod)) {
	    	 $("#msg_minerPeriod").html("填写矿机寿命符合要求");  //\D为非数字
	         $("#msg_minerPeriod").css("color", "green");
	         check4 = 1;
			 return check4;
			
	    }else{
	    	 $("#msg_minerPeriod").html("请填写正整数");
	         $("#msg_minerPeriod").css("color", "red");	       
	    }
	}else{
		$("#msg_minerPeriod").html("矿机寿命不能为空！");
        $("#msg_minerPeriod").css("color", "red");      
	}
	
}
//修改矿机--矿机名的判断
function EcheckName() {
	var minerName = $("#EminerName").val();
	var minerCode = $("#EminerCode").val();
	//alert(JSON.stringify(minerCode));
	var data = {
		"minerCode" : minerCode,
		"minerName" : minerName
	};
	if (minerName != "") {
		$.ajax({
			url: "/api/v1/miner/inquireUpdateMinerName",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data:JSON.stringify(data),
			processData : false,
			async : false,
			
			success : function(res) {
				if (res.code == 0) {
					$("#msg_EminerName").html("该矿机名可用");
					$("#msg_EminerName").css("color", "green");
					check5 = 1;
					return check5;
				} else{
					$("#msg_EminerName").html("矿机名已存在");
					$("#msg_EminerName").css("color", "red");
				}
			},
			error : function() {		
				alert("检查矿机名是否存在发生错误！");
			}
		});
	}else{
		$("#msg_EminerName").html("请填写矿机名！");
        $("#msg_EminerName").css("color", "red");
	}
}

//修改矿机--矿机单价的判断
function EcheckPrice() {
	
	var minerPrice = $("#EminerPrice").val(); 
	var minerPrice1 = parseFloat(minerPrice);
	
	if(minerPrice!=""){
			
			if(minerPrice1!=0){
				if (/^\d+(?=\.{0,1}\d+$|$)/.test(minerPrice)) {
					 $("#msg_EminerPrice").html("填写矿机单价符合要求");
			         $("#msg_EminerPrice").css("color", "green");
			         check6 = 1;
					 return check6;					
			    }else{			    	
			         $("#msg_EminerPrice").html("请填写大于0的数字！");
			         $("#msg_EminerPrice").css("color", "red");			 
			    }
			}else{
				 $("#msg_EminerPrice").html("请填写大于0的数字！");
		         $("#msg_EminerPrice").css("color", "red");		      
			}			 
		}else{
		   	 $("#msg_EminerPrice").html("矿机单价不能为空！");
		     $("#msg_EminerPrice").css("color", "red");
		}   
}
//修改矿机--算力奖励的判断
function EcheckPower() {

	var minerPower = $("#EminerPower").val(); 
	var minerPower1 = parseFloat(minerPower);
	//alert(JSON.stringify(minerPower1));
	
	if(minerPower!=""){		
		if(minerPower1!=0){
			if (/^\d+(?=\.{0,1}\d+$|$)/.test(minerPower)) {
				 $("#msg_EminerPower").html("填写算力奖励符合要求");
		         $("#msg_EminerPower").css("color", "green");
		         check7 = 1;
				 return check7;				
		    }else{		    	
		         $("#msg_EminerPower").html("请填写大于0的数字！");
		         $("#msg_EminerPower").css("color", "red");
		    }
		}else{
			 $("#msg_EminerPower").html("请填写大于0的数字！");
	         $("#msg_EminerPower").css("color", "red");
		}		 
	}else{
	   	 $("#msg_EminerPower").html("算力奖励不能为空！");
	     $("#msg_EminerPower").css("color", "red");
	}   
}
//修改矿机--矿机周期的判断
function EcheckPeriod() {
	
	var minerPeriod = $("#EminerPeriod").val(); 	
	//alert(JSON.stringify(minerPeriod));
	
	if(minerPeriod!=""){
		if (!/\D/.test(minerPeriod)) {
	    	 $("#msg_EminerPeriod").html("填写矿机寿命符合要求");  //\D为非数字
	         $("#msg_EminerPeriod").css("color", "green");
	         check8 = 1;
			 return check8;
			
	    }else{
	    	 $("#msg_EminerPeriod").html("请填写正整数");
	         $("#msg_EminerPeriod").css("color", "red");
	    }
	}else{
		$("#msg_EminerPeriod").html("矿机寿命不能为空！");
        $("#msg_EminerPeriod").css("color", "red");
	}	
}


//新增矿机信息
function addMiner(){	
	var minerName=$("#minerName").val();
	var minerPrice=$("#minerPrice").val();
	var minerGrade=$("#minerGrade").val();
	var minerPower=$("#minerPower").val();
	var minerPeriod=$("#minerPeriod").val();
	var minerDescription=$("#minerDescription").val();
	
	if(minerName==""||minerPrice==""||minerGrade=="请选择"||minerGrade==""||minerPower==""||minerPeriod==""){
		alert("请输入必填项");
	}else{
		if(check1==1 && check2==1 && check3==1 && check4==1){
			var data={
				"minerName": minerName,
				"minerPrice": minerPrice,
				"minerGrade": minerGrade,
				"minerPower": minerPower,
				"minerPeriod": minerPeriod,
				"minerDescription": minerDescription,
			}

			$.ajax({
				url:"/api/v1/miner/addMiner",
				contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success:function(res){					
					$("#Tip").modal('show');
					$("#addMinerModal").modal('hide');
					minerReady();
					$("#minerGrid").bootstrapTable('refresh');							
				},
				error:function(){
					document.getElementById("tipContent").innerText="新增失败";
					$("#Tip").modal('show');
					$("#minerModal").modal('hide');

				},
			});	   			
		}else{
			alert("请确认输入信息是否符合要求！");
		}	
	}
}

//点击取消后清空表单中已写信息
function resetAddModal(){
	location.reload();
}

function updateMiner(){	
	var minerCode=$("#EminerCode").val();
	var minerName=$("#EminerName").val();
	var minerPrice=$("#EminerPrice").val();
	var minerGrade=$("#EminerGrade").val();
	var minerPower=$("#EminerPower").val();
	var minerPeriod=$("#EminerPeriod").val();
	var minerDescription=$("#EminerDescription").val();
	
	var data={
		"minerCode":minerCode,
		"minerName":minerName,
		"minerPrice":minerPrice,
		"minerGrade":minerGrade,
		"minerPower":minerPower,
		"minerPeriod":minerPeriod,
		"minerDescription":minerDescription,
	}
	
	if(minerName==""||minerPrice==""||minerGrade=="请选择"||minerGrade==""||minerPower==""||minerPeriod==""){
		alert("必填项不能为空");
	}else{
		EcheckName();
		EcheckPrice();
		EcheckPower();
		EcheckPeriod();
		
		if(check5==1 && check6==1 && check7==1 && check8==1){
			$.ajax({
				url:"/api/v1/miner/updateMiner",
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
			alert("请确认输入信息是否符合要求！");
		}
	}
}
