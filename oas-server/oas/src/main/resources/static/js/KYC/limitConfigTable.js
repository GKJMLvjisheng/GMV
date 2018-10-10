//提币请求审核创建bootstrapTable

function initLimitGrid(data) {	

	$("#limitGrid").bootstrapTable({

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
			title : "审核状态",
			field : "newsUrl",
			align: 'center',
			valign: 'middle',	
		},{
			title : "提现次限额",
			field : "newsUrl",
			align: 'center',
			valign: 'middle',	
		},{
			title : "提现日限额",
			field : "newsUrl",
			align: 'center',
			valign: 'middle',	
		},{
			title : "转账次限额",
			field : "newsUrl",
			align: 'center',
			valign: 'middle',	
		},{
			title : "转账日限额",
			field : "newsUrl",
			align: 'center',
			valign: 'middle',	
		},{
			title : "操作",
			field : "name",
			align: 'center',
			valign: 'middle',
			width:  '60px',
			formatter: actionFormatter
		}],		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function actionFormatter(value, row, index) {
	var id = value;
	var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>"; 
    result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"EditNewsById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";     
	return result;
}

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
    
function EditNewsById(id){   
    //获取选中行的数据
    var rows=$("#limitGrid").bootstrapTable('getRowByUniqueId', id);
	$('#EnewsId').val(rows.newsId);  
    $('#EnewsTitle').val(rows.newsTitle);
	$('#EnewsAbstract').val(rows.newsAbstract);
	$('#EnewsUrl').val(rows.newsUrl);
	$('#EnewsPicturePath').val(rows.newsPicturePath); 				
    $("#updateLimitModal").modal("show");           
  }
