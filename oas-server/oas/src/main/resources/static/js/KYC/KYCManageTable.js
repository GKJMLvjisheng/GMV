//提币请求审核创建bootstrapTable

function initKYCGrid(data) {	

	$("#requestAuditGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		toolbar:"#toolbar",//工具栏
		sortName: 'ID', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		data:data,
		columns : [{  
		title: 'id',  
		field: '',
		align: 'center',
		valign: 'middle', 
		//hidden:true, 
		},
		{  
		title: '序号',  
		field: '',
		align: 'center',
		valign: 'middle', 
		width:  '60px',
		hidden:true, 
		formatter: function (value, row, index) {  
			return index+1;  
			}  
		},
		{
			title : "身份证正面",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '90px',
			formatter: picturePath
		},
			{
			title : "身份证反面",
			field : "newsTitle",
			align: 'center',
			valign: 'middle',
			width:  '120px',
			formatter: picturePath
		}, {
			title : "手持身份证和账号",
			field : "newsAbstract",
			align: 'center',
			valign: 'middle',
			width:  '200px',
			formatter: picturePath
		},
		{
			title : "申请时间",
			field : "newsPicturePath",
			align: 'center',
			valign: 'middle',
			width:  '140px',
		}, 
		{
			title : "审核状态",
			field : "newsUrl",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter		
		},{
			title : "操作",
			field : "id",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter1
		}],		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function picturePath(value, row, index) {
	var result = "";
	var picturePath = value;      
    result += "<a href="+picturePath+"><img src="+picturePath+" width='120px' height='80'></a>";
    			//"<span>"+picturePath+"</span>";      
    return result;
}
function actionFormatter(value, row, index) {
	var status = value;
	var result = "";
	if(status==0){
		// result += "<input type='radio' onclick='agree()' name='radio1' id='agree' value='批准'>批准";
		// result += "<input type='radio' onclick='reject()' name='radio2' id='reject' value='拒绝'>拒绝";
		result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditQuestionById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>"; 
		return result;
	}else if(status==1){
		result += "<span>已通过</span>";      
		return result;
	}else if(status==2){
		result += "<span>未通过</span>";      
	return result;
	}       
}

function actionFormatter1(value, row, index) {
	var id = value;
	var result = "";
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";      
	return result;
}

$(":radio").click(function(){
	var choice=$(this).val();
	//$('#choice').val(choice);
	alert(choice);
	data={
		"choice":choice,
		}

	 $.ajax({		
		url: "/api/v1/energyPoint/inqureEnergyWalletInTotalPointTradeRecord",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
		processData : false,
		async : false,

		success: function(res) {
			document.getElementById("tipContent").innerText="审核过程完成";
			requestAuditReady();
			$("#KYCGrid").bootstrapTable('refresh');	
		}, 
		error: function(){
			document.getElementById("tipContent").innerText="审核过程发生错误";
			$("#Tip").modal('show');
		}
		}); 
});

function ViewViewById(id){	
		var formData = new FormData();
		$('#Qname').val(id);	
		formData.append("name", $("#Qname").val());
		$.ajax({
		url:"/api/v1/userCenter/inquireTradeRecordUserInfo",
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
			//alert(JSON.stringify(res));			
			rows = res.data;			
			$('#Qname').val(rows.name);
			$('#Qnickname').val(rows.nickname);
			$('#Qgender').val(rows.gender);
			$('#Qbirthday').val(rows.birthday);
			$('#Qmobile').val(rows.mobile);
			$('#Qemail').val(rows.email);
			$('#Qaddress').val(rows.address);
			$('#QinviteCode').val(rows.inviteCode);
			$("#queryUserModal").modal("show");
			}
			else{
				alert("查询失败1");
				}						
		},
		error:function(){
			alert("查询失败2");
		},
		});					
	}
