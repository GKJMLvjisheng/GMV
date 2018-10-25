/**
 * 答题管理js文件
 */
document.write("<script language=javascript src='/js/QA/QAManageTable.js'></script>");

//主界面用户表格回显
$(function() {
	//初始加载	
	questionReady();
});

//修改环保题目-检查标题
function EcheckQuestion() {
	var question = $("#Equestion").val(); 
	var len=question.length;
	//alert(len);
	if (len==40) {
		$("#msg_Equestion").html("输入标题长度为40个字符，已达上限");
        $("#msg_Equestion").css("color", "red");
	}
	else {
		$("#msg_Equestion").html("输入标题不超过40个字符，符合要求");
        $("#msg_Equestion").css("color", "green");
	}
}

//修改环保题目-检查选项A
function EcheckChoiceA() {
	var choiceA = $("#EchoiceA").val(); 
	var len=choiceA.length;
	//alert(len);
	if (len==20) {
		$("#msg_EchoiceA").html("输入标题长度为20个字符，已达上限");
        $("#msg_EchoiceA").css("color", "red");
	}
	else {
		$("#msg_EchoiceA").html("输入标题不超过20个字符，符合要求");
        $("#msg_EchoiceA").css("color", "green");
	}
}
//修改环保题目-检查选项B
function EcheckChoiceB() {
	var choiceB = $("#EchoiceB").val(); 
	var len=choiceB.length;
	//alert(len);
	if (len==20) {
		$("#msg_EchoiceB").html("输入标题长度为20个字符，已达上限");
        $("#msg_EchoiceB").css("color", "red");
	}
	else {
		$("#msg_EchoiceB").html("输入标题不超过20个字符，符合要求");
        $("#msg_EchoiceB").css("color", "green");
	}
}
//修改环保题目-检查选项C
function EcheckChoiceC() {
	var choiceC = $("#EchoiceC").val(); 
	var len=choiceC.length;
	//alert(len);
	if (len==20) {
		$("#msg_EchoiceC").html("输入标题长度为20个字符，已达上限");
        $("#msg_EchoiceC").css("color", "red");
	}
	else {
		$("#msg_EchoiceC").html("输入标题不超过20个字符，符合要求");
        $("#msg_EchoiceC").css("color", "green");
	}
}

function questionReady(){

    $('#questionGrid').bootstrapTable('destroy');
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
			initQuestionGrid(data2);
		}, 
		error: function(){
			alert("环保题目回显失败！")
		}
		}); 
}

//新增问题
function addQuestion(){		
	var formData = new FormData();
	var question=$("#question").val();
	var choiceA=$("#choiceA").val();
	var choiceB=$("#choiceB").val();
	var choiceC=$("#choiceC").val();
	var choiceRight=$("#choiceRight").val();
	if(question==""||choiceA==""||choiceB==""||choiceC==""||choiceRight==""){
		alert("请输入必填项");
	}else{
		formData.append("question", question);
		formData.append("choiceA", choiceA);
		formData.append("choiceB", choiceA);
		formData.append("choiceC", choiceC);	
		formData.append("choiceRight", choiceRight);
		$.ajax({
		url:"/api/v1/computingPower/addTopic",
		data:formData,
		//contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,		
		processData : false,
		contentType : false,
		async : false,

		success:function(res){					
			$("#Tip").modal('show');
			$("#addQuestionModal").modal('hide');
			questionReady();
			$("#questionGrid").bootstrapTable('refresh');							
		},
		error:function(){
			document.getElementById("tipContent").innerText="新增失败";
			$("#Tip").modal('show');
			$("#addQuestionModal").modal('hide');

		},
	});	   

	}
}

//点击取消后清空表单中已写信息
function resetAddModal(){
	//document.getElementById("addNewsForm").reset();
	location.reload();
}

function updateQuestion(){
	
	var formData = new FormData();
	formData.append("topicId", $("#EtopicId").val());	  	
	formData.append("question", $("#Equestion").val());
	formData.append("choiceA", $("#EchoiceA").val());
	formData.append("choiceB", $("#EchoiceB").val());
	formData.append("choiceC", $("#EchoiceC").val());
	formData.append("choiceRight", $("#EchoiceRight").val());	
	
	$.ajax({
		url:"/api/v1/computingPower/updateTopic",
		data:formData,
		//contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		
		processData : false,
		contentType : false,
		async:false,

		success:function(res){	
			
			if(res.code==0){		
				alert("修改成功");
				location.reload();
			}
			else{
				alert("修改失败");
				}						
		},
		error:function(){
			alert("修改失败");

		},
	});
	
	}
