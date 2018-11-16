
//提币请求审核创建bootstrapTable
var pageSize;
var pageNum;
var array = new Array;
var allData = "";  //全选数据中去除status!=0的数据
var dataRes;  //请求的当页的数据

function initRequestAuditGrid() {	
	$('#requestAuditGrid').bootstrapTable('destroy');
	$("#requestAuditGrid").bootstrapTable({
		url: "/api/v1/userWallet/getWithdrawList",
		contentType : "application/json",
		dataType:"json",
		method:"post",
		pagination:true,//显示分页条：页码，条数等
		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"server",//在服务器分页
		dataField: "withdrawData",
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		queryParams:queryParams,
		responseHandler:responseHandler,
		
		 rowStyle:rowStyle,//通过自定义函数设置行样式

		toolbar:"#toolbar",//工具栏
		
		sortable: true,//是否启用排序
		sortName: "created", // 要排序的字段
	    sortOrder: "desc", // 排序规则	    	    
	    
		columns : [{  
		title: 'uuid',  
		field: 'uuid',
		align: 'center',
		valign: 'middle',
		//class:'uu_style', 
		visible: false, 
		},
		{			
			checkbox:"true",			
			align: 'center',// 居中显示			
			field : "box",
			
		},
		{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle',  
			width:  '50px',
			formatter: function (value, row, index) { 
				return pageSize * (pageNum - 1) + index + 1;  
				}  
			},
		{
			title : "用户名",
			field : "userName",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
			{
			title : "提币数量",
			field : "value",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},
		{
			title : "手续费",
			field : "extra",
			align: 'center',
			valign: 'middle',
			width:  '100px',
		},{
			title : "创建时间",
			field : "created",
			align: 'center',
			valign: 'middle',
			width:  '160px',
		},{
			title : "钱包地址",
			field : "address",
			align: 'center',
			valign: 'middle',
			width:  '190px',
			formatter: actionFormatter2
		}, {
			title : "操作",
			field : "userName",
			align: 'center',
			valign: 'middle',
			width:  '50px',
			formatter: actionFormatter1
		}, 
		{
			title : "状态",
			field : "status",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter		
		}],		
		
		clickToSelect: false, 
		
		//点击每一个复选框时触发的操作
		onCheck: function (row,index) {
			//alert(JSON.stringify(index));			
			if(row.status!=0){
				alert("该提币请求已审核完毕，请勿重复审核！");
				index.prop("checked",false);
			}else{				
				array.push(row);										  
			}            	        
	    },
	    
	    //取消每一个复选框时触发的操作
	    onUncheck:function(row) {
	    	uuid = row.uuid;
	    
	    	for (var i = 0; i < array.length; i++) {
	    		if (JSON.stringify(array[i]).indexOf(uuid) != -1) {	   //找到uuid 			
	    			//alert(JSON.stringify(i));
	    			array.splice(i,1);
	    		}
	    	}
	    },
	    
	  //点击全选框时触发的操作
        onCheckAll:function(rows){
        	var boxes = document.getElementsByName("btSelectItem");
            var box = document.getElementsByName("btSelectAll");
//            console.log(JSON.stringify(boxes));
//            console.log(JSON.stringify(box));
            
        	for(var i=0;i<rows.length;i++){
        		if(rows[i].status!=0){
        			boxes[i].checked = false;
        		}        		
        	} 
        	
        	if(array.length==0){
        		array = rows;
        	}else{
   		
        		for(var i=0;i<array.length;i++){
            		
            		for(var j=0;j<rows.length;j++){
            			if(array[i].uuid==rows[j].uuid){
            				rows.splice(j,1);//删除rows中重复的提币请求            				            				
            				var f = 1;            	
            			}
            		}
            	}        		
    			var allData = new Array;
    			allData = rows;
    			array = array.concat(allData); //数组拼接    		
        	}        	
        	
        	//删除一条数据后，数组索引值已变
        	for(var i=array.length-1;i>=0;i--){
        		if(array[i].status!=0){
        			array.splice(i,1);//删除已审核完的提币请求       			      			       			
        			var d = 1;
        		}        		
        	}        	
        	
        	if(d==1 || f==1){
        		alert("提币请求中包含部分已审核完毕或重复添加的请求，已自动为您去除！");
        	}        	
        },
        
        //取消所有
        onUncheckAll: function (row) {          	    		
    		var row =  dataRes.data.rows;	
    		//alert(JSON.stringify(row));
        	for(var i=0;i<array.length;i++){
        		
        		for(var j=0;j<row.length;j++){
        			
        			if(array[i].uuid==row[j].uuid){
        				array.splice(i,1);//删除array中重复的提币请求
        			}
        		}
        	}        		  			    		
        	        	
        }, 
      
        onLoadSuccess :function(data){
	       	var boxes = document.getElementsByName("btSelectItem");
	       	var box = document.getElementsByName("btSelectAll");
	        var rows =  dataRes.data.rows;	        
//	        alert(JSON.stringify(array));//已选择的提币请求
//	        alert(JSON.stringify(rows));//当页的数据
	        var s = 0;
	        for(var i=0;i<rows.length;i++){
	        	for(var j=0;j<array.length;j++){
	       			if(rows[i].uuid == array[j].uuid && rows[i].status == 0){
	       				boxes[i].checked = true;
	       				s += 1;
	       			}  
	       		}		      		
	       	}
	        if(s == rows.length){
	        	box[0].checked = true;	        	
	        }
       },
          	 
	});
}

function withdrowMoneyList(){
	initMoneyGrid(array);			
	$("#moneyModal").modal("show");
}


function agreeWithdrawMoney(){
	var status = 1;
	$('#status1').val(status);
	document.getElementById("withdrawMoney1").innerText="确认批准批量提币请求吗？";
	$("#confirmModal").modal("show");
}

function rejectWithdrawMoney(){
	
	var status = 2;
	$('#status1').val(status);
	document.getElementById("withdrawMoney1").innerText="确认拒绝批量提币请求吗？";
	$("#confirmModal").modal("show");
}

function withdrawMoney(){
	
	var status = $("#status1").val();
	var array1 = new Array();	
	for(var i=0;i<array.length;i++){
		array1[i] = array[i].uuid;
	}
	//alert(JSON.stringify(status));
	
	if(array1.length==0){
		alert("请通过勾选复选框选择提币请求之后，再执行此操作！");
	} else{ 
	
		var data={
			"uuids":array1,
			"status":status,
			}
	
		$.ajax({		
			url: "/api/v1/userWallet/setWithdrawResult",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data:JSON.stringify(data),
			processData : false,
			async : false,
	
			success: function(res) {
				if(res.code==0){
					document.getElementById("tipContent").innerText="批量提币审核过程完成";
					$("#Tip").modal('show');
					initRequestAuditGrid();
					array = [];
					//location.reload();
				}else{
					document.getElementById("tipContent").innerText = res.message;
					$("#Tip").modal('show');
					initRequestAuditGrid();
					array = [];
				}			
			}, 
			error: function(){
				document.getElementById("tipContent").innerText="批量提币审核过程发生错误";
				$("#Tip").modal('show');
			}
		}); 
	}
}

//批量提币列表
function initMoneyGrid(data) {	
	$('#moneyGrid').bootstrapTable('destroy');
	$("#moneyGrid").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		uniqueId:"uuid",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		data:data,
		
		columns : [
			{  
				title: '序号',  
				field: '',
				align: 'center',
				valign: 'middle',  
				width:  '70px',
				formatter: function (value, row, index) {  
					return index + 1;  
					}  
				},
			{
				title : "用户名",
				field : "userName",
				align: 'center',
				valign: 'middle',
				width:  '120px',
			},
				{
				title : "提币数量",
				field : "value",
				align: 'center',
				valign: 'middle',
				width:  '120px',
			},
			{
				title : "手续费",
				field : "extra",
				align: 'center',
				valign: 'middle',
				width:  '120px',
			},{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '190px',
			}, {
				title : "操作",
				field : "uuid",
				align: 'center',
				valign: 'middle',
				width:  '60px',
				formatter: actionFormatter3
			}],
		
		//search : true,//搜索
        //searchOnEnterKey : true,
		clickToSelect: false,  
	})	
}

//请求服务数据时所传参数
function queryParams(params){
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;
	
    return{
        //每页多少条数据
        pageSize: params.limit,
        //当前页码
        pageNum: params.offset / params.limit + 1,
    }
}

//请求成功方法
function responseHandler(res){
	//alert(JSON.stringify(res));
	dataRes = res;
	if(res.code != 0 ){
		alert("提币请求审核回显失败！"+res.message)
		return{
			total:0,
			withdrawData : null
		}
	}
	return {
		 total : res.data.total, //总页数,前面的key必须为"total"
		 withdrawData : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
	}
};

function actionFormatter(value, row, index) {
	var id = row.uuid;
	var status = value;
	var result = "";
	if(status==0){
		result += "<input type='radio' onclick=\"agree('" + id + "')\"' name='radio' id='agree' value='1'>批准 ";
		result += "<input type='radio' onclick=\"reject('" + id + "')\" name='radio' id='reject' value='2'>拒绝";
		return result;
		
	}else if(status==1){
		result += "<span>审核已通过</span>";      
		return result;
		
	}else if(status==2){
		result += "<span>审核未通过</span>";      
		return result;
		
	} 
	else if(status==3){
		result += "<span>审核已通过-转账成功</span>";      
		return result;
	}else{
		result += "<span>审核已通过-转账失败</span>";      
		return result;
	}
}

function actionFormatter1(value, row, index) {
	var id = value;
	var result = "";
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"viewUserMessage('" + id + "')\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";      
	return result;
}

function actionFormatter2(value, row, index) {
	var s = "https://etherscan.io/token/";
	var s1 = s+value;
	var result = "";
	result +="<a href="+s1+ ">"+value+"</a>"; 
	return result;
}

function actionFormatter3(value, row, index) {
	var uuid = value;
	var result = "";	
	result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteById('" + uuid + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";
	return result;
	
}

function rowStyle(row, index) {
	var classes = ['active', 'success', 'info', 'warning', 'danger']; 
	var status = row.status;
	var style = "";    
	
	if(status==0){
		 style='info'; 
		 return { classes: style };
		
	}else if(status==1){
		 style='success';  
		 return { classes: style };
		
	}else if(status==2){
		 style='active';
		 return { classes: style };
		
	} 
	else if(status==3){
		 style='warning';  
		 return { classes: style };
	}else{
		 style='danger';
		 return { classes: style };
	}	                         
}

function agree(id){
	var status = 1;
	$('#status').val(status);
	$('#uuid').val(id);
	document.getElementById("withdrawMoney").innerText="确认批准提币请求吗？";
	$("#agreeModal").modal("show");
}

function reject(id){
	var status = 2;
	$('#status').val(status);
	$('#uuid').val(id);
	document.getElementById("withdrawMoney").innerText="确认拒绝提币请求吗？";
	$("#agreeModal").modal("show");
}

function viewUserMessage(id){	
	var formData = new FormData();
	formData.append("name", id);
	$.ajax({
	url:"/api/v1/userCenter/inquireTradeRecordUserInfo",
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
		var rows = res.data;			
		$('#Qname').val(rows.name);
		$('#Qnickname').val(rows.nickname);
		$('#Qgender').val(rows.gender);
		$('#Qbirthday').val(rows.birthday);
		$('#Qmobile').val(rows.mobile);
		$('#Qemail').val(rows.email);
		$('#Qaddress').val(rows.address);
		$('#QinviteCode').val(rows.inviteCode);
		$("#queryModal").modal("show");
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
