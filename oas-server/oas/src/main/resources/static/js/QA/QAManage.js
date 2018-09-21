/**
 * 答题管理js文件
 */
document.write("<script language=javascript src='/js/QA/QAManageTable.js'></script>");

//主界面用户表格回显
$(function() {
	//初始加载	
	questionReady();
});

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
//	var param = $("#addQuestionForm").serializeArray();		
	// var select=new Array();
	// select[0] = $("#choiceContent1").val();
	// select[1] = $("#choiceContent2").val();
	//alert(JSON.stringify(select));
	var formData = new FormData();
	formData.append("question", $("#question").val());
	formData.append("choiceA", $("#choiceA").val());
	formData.append("choiceB", $("#choiceB").val());
	formData.append("choiceC", $("#choiceC").val());
	formData.append("choiceRight", $("#choiceRight").val());
//  alert(JSON.stringify(formData));
	$.ajax({
		url:"/api/v1/computingPower/addTopic",
		data:formData,
		contentType : 'application/json;charset=utf8',
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
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		
		processData : false,
		contentType : false,
		async:false,

		success:function(res){	
			
			if(res.code==0){
				alert("success");
				location.reload();
			    //newsReady();
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
