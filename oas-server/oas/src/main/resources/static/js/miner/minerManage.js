
document.write("<script language=javascript src='/js/miner/minerManageTable.js'></script>");
var check1;
var check2;
$(function() {
	//初始加载	
	minerReady();
});

//发ajax请求到后台判断矿机是否重复
function checkName() {
	var minerName = $("#minerName").val(); 
	alert(JSON.stringify(minerName));
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
          if (res.data == 1) {
            $("#msg_minerName").html("矿机名可以使用");
            $("#msg_minerName").css("color", "green");
            check1 = 1;
            return check1;
          } else if(res.data == 0) {
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
//    	 $("#msg_minerName").html("请填写矿机名！");
         $("#msg_minerName").css("color", "red");
    }
}

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

//新增矿机信息
function addMiner(){	

	var minerName=$("#minerName").val();
	var minerPrice=$("#minerPrice").val();
	var minerEfficiency=$("#minerEfficiency").val();
	var minerPeriod=$("#minerPeriod").val();
	var minerDescription=$("#minerDescription").val();
	
	if(minerName==""||minerPrice==""||minerEfficiency==""||minerPeriod==""){
		alert("请输入必填项");
	}else{
		var data={
				"minerName":minerName,
				"minerPrice":minerPrice,
				"minerEfficiency":minerEfficiency,
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
	var minerEfficiency=$("#EminerEfficiency").val();
	var minerPeriod=$("#EminerPeriod").val();
	var minerDescription=$("#EminerDescription").val();
	
	var data={
			"minerCode":minerCode,
			"minerName":minerName,
			"minerPrice":minerPrice,
			"minerEfficiency":minerEfficiency,
			"minerPeriod":minerPeriod,
			"minerDescription":minerDescription,
			}
	
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
	
	}
