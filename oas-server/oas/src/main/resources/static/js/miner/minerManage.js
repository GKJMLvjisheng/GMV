
document.write("<script language=javascript src='/js/miner/minerManageTable.js'></script>");
var check1;
var check2;
$(function() {
	//初始加载	
	minerReady();
});

function minerReady(){

    $('#minerGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({
		
		url: "/api/v1/miner/inquireMiner",
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
            check1 = 0;
            return check1;
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
//发ajax请求到后台判断矿机是否重复--修改矿机
function checkEName(minerName) {
	
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
//            $("#msg_EminerName").html("矿机名可以使用");
//            $("#msg_EminerName").css("color", "green");
            check2 = 1;
            return check1;
          } else{
//            $("#msg_EminerName").html("矿机名已存在");
//            $("#msg_EminerName").css("color", "red");
            check2 = 0;
            return check1;
          }
        },
        error : function() {
          alert('检查矿机名是否存在发生错误');
        }
      });
    }else{
//    	 $("#msg_EminerName").html("请填写矿机名！");
//         $("#msg_EminerName").css("color", "red");
         check2 = 2;
         return check1;
    }
}

//新增矿机信息
function addMiner(){	

	var minerName=$("#minerName").val();
	var minerPrice=$("#minerPrice").val();
	var minerGrade=$("#minerGrade").val();
	//alert(JSON.stringify(minerGrade));
	var minerPower=$("#minerPower").val();
	var minerPeriod=$("#minerPeriod").val();
	var minerDescription=$("#minerDescription").val();
	
	if(minerName==""||minerPrice==""||minerGrade=="请选择"||minerEfficiency==""||minerPeriod==""){
		alert("请输入必填项");
	}else{
		if(check1==1){
			var data={
					"minerName":minerName,
					"minerPrice":minerPrice,
					"minerPower":minerPower,
					"minerPeriod":minerPeriod,
					"minerDescription":minerDescription,
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
			
		}else if(check1==0){
			alert("矿机名已存在！");
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
	var minerPower=$("#minerPower").val();
	var minerPeriod=$("#EminerPeriod").val();
	var minerDescription=$("#EminerDescription").val();
	
	var data={
			"minerCode":minerCode,
			"minerName":minerName,
			"minerPrice":minerPrice,
			"minerPower":minerPower,
			"minerPeriod":minerPeriod,
			"minerDescription":minerDescription,
			}
	
	checkEName(minerName);
	if(check2==1){
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
	}else if(check2==0){
		alert("矿机名已存在！");
	}else if(check2==2){
		alert("请填写矿机名！");
	}	
}
