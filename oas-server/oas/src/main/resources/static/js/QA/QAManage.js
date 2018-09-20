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
		
		url: "/api/v1/userCenter/selectAllQuestion",
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
	//var param = $("#addUserForm").serializeArray();	
	var formData = new FormData();
	var select = {};
	select["a"] = $("#choiceContent1").val();
	select["b"] = $("#choiceContent2").val();
	alert(JSON.stringify(select));

	formData.append("questionContent", $("#questionContent").val());
	//formData.append("choiceContent1", $("#choiceContent1").val());
	//formData.append("choiceContent2", $("#choiceContent2").val());
	formData.append("list", select);
	formData.append("answer", $("#answer").val());

	$.ajax({
		url:"/api/v1/userCenter/addQuestion",
		data:formData,
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		
		processData : false,
		contentType : false,
		async:false,

		success:function(res){	
				
				$("#Tip").modal('show');
				$("#addNewsModal").modal('hide');
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
	formData.append("questionContent", $("#EquestionContent").val());
	formData.append("choiceContent1", $("#EchoiceContent1").val());
	formData.append("choiceContent2", $("#EchoiceContent2").val());
	formData.append("answer", $("#Eanswer").val());	
	
	$.ajax({
		url:"/api/v1/userCenter/updateQuestion",
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
