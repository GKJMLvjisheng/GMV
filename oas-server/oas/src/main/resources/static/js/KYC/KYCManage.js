document.write("<script language=javascript src='/js/KYC/KYCManageTable.js'></script>");
var check1;
$(function() {
	
//初始加载提币审核请求	
	initKYCGrid();
});

//发ajax请求到后台判断身份证号是否重复
function checkCard(card) {
	
    if (card != "") {
	  var data = {
        "userIdentityNumber" : card
      };
      $.ajax({
        url: "/api/v1/userCenter/checkIdentityNumber",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

        success : function(res) {
          if (res.data == 1) {
//            $("#msg_card").html("身份证号可以使用");
//            $("#msg_card").css("color", "green");
            check1 = 1;
            return check1;
          } else if(res.data == 0) {
//            $("#msg_card").html("身份证号已存在");
//            $("#msg_card").css("color", "red");
            check1 = 0;
            return check1;
          }
        },
        error : function() {
          alert('检查身份证号是否存在发生错误');
        }
      });
    }else{
//    	 $("#msg_card").html("请填写身份证号！");
//         $("#msg_card").css("color", "red");
    }
}

function addPostil(){
	var reId = $("#reId").val();
	var postil=$("#postil").val();	
	
	if(postil!=""){
		data={
				"verifyStatus":3,
				"uuid":reId,
				"remark":postil,
				}

			 $.ajax({		
				url: "/api/v1/userCenter/checkUserIdentity",
			    contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success: function(res) {
					document.getElementById("tipContent").innerText="批注添加成功";
					$("#Tip").modal('show');
					var pageNumber = $("#KYCGrid").bootstrapTable('getOptions').pageNumber;
					$("#KYCGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
				}, 
				error: function(){
					document.getElementById("tipContent").innerText="批注添加失败";
					$("#Tip").modal('show');
					var pageNumber = $("#KYCGrid").bootstrapTable('getOptions').pageNumber;
					$("#KYCGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
					
				}
				}); 
	}else{
		alert("批注不能为空！");
	}	
}

function addNC(){
	
	var agId = $("#agId").val();
	var agStatus = $("#agStatus").val();
	var name=$("#name").val();
	var card=$("#card").val();	
	
	if(agStatus=="2"||agStatus=="3"){
		alert("审核已完成，不再允许提交信息！")
	}else{
		var data={
				"uuid":agId,
				"userIdentityName":name,
				"userIdentityNumber":card,
				}

			$.ajax({		
				url: "/api/v1/userCenter/checkUserIdentity",
				contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success: function(res) {
					document.getElementById("tipContent").innerText="信息添加成功";
					$("#Tip").modal('show');
					var pageNumber = $("#KYCGrid").bootstrapTable('getOptions').pageNumber;
					$("#KYCGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
				}, 
				error: function(){
					document.getElementById("tipContent").innerText="信息添加失败";
					$("#Tip").modal('show');
					var pageNumber = $("#KYCGrid").bootstrapTable('getOptions').pageNumber;
					$("#KYCGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
				}
				}); 	
	}		
}

function agree(id){
	
	var rows=$("#KYCGrid").bootstrapTable('getRowByUniqueId', id);
	var uuid = id;	
	$('#name').val(rows.userIdentityName);
	$('#card').val(rows.userIdentityNumber);
	var name=rows.userIdentityName;
	var card=rows.userIdentityNumber;

	var data={
		"verifyStatus":2,
		"uuid":uuid,
		}
	
	if(name!=null && name!="" && card!=null && card!="" ){
		
		checkCard(card);
		//alert(JSON.stringify(check1));
		if(check1==1){
			
			$.ajax({		
				url: "/api/v1/userCenter/checkUserIdentity",
				contentType : 'application/json;charset=utf8',
				dataType: 'json',
				cache: false,
				type: 'post',
				data:JSON.stringify(data),
				processData : false,
				async : false,

				success: function(res) {
					if(res.code==0){
						document.getElementById("tipContent").innerText="批准成功";
						$("#Tip").modal('show');
						var pageNumber = $("#KYCGrid").bootstrapTable('getOptions').pageNumber;
						$("#KYCGrid").bootstrapTable('selectPage',pageNumber);  //刷新当前页
						
					}else{
						document.getElementById("tipContent").innerText="批准过程发生错误1";
						$("#Tip").modal('show');
					}
					
				}, 
				error: function(){
					document.getElementById("tipContent").innerText="批准过程发生错误2";
					$("#Tip").modal('show');
				}
				}); 	
		}else if(check1==0){
			alert("该身份证号已存在！")
		}
	}else{
		alert("请输入姓名、身份证号！")
	}				
}

function rowStyle(row, index) {
	var classes = ['active', 'success', 'info', 'warning', 'danger']; 
	var status = row.verifyStatus;
	var style = "";    
	
	if(status==1){
		 style='info'; 
		 return { classes: style };
		
	}else if(status==2){
		 style='warning';  
		 return { classes: style };
		
	}else if(status==3){
		 style='active';
		 return { classes: style };
		
	}                          
}
