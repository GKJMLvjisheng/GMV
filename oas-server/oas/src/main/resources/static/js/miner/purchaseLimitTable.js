
var pageSize;
var pageNum;
var array = new Array;
var dataRes;  //请求的当页的数据

function initminerLimitGrid() {	
	$('#minerLimitGrid').bootstrapTable('destroy');
	$("#minerLimitGrid").bootstrapTable({
		url: "/api/v1/miner/inquireMinerOfUser",
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
			width:  '130px',
		},
			{
			title : "限制台数",
			field : "restriction",
			align: 'center',
			valign: 'middle',
			width:  '115px',
		},
		{
			title : "已购台数",
			field : "amount",
			align: 'center',
			valign: 'middle',
			width:  '115px',
			formatter: actionFormatter4
		},{
			title : "开始时间",
			field : "startTime",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		},{
			title : "结束时间",
			field : "endTime",
			align: 'center',
			valign: 'middle',
			width:  '150px',
		}, {
			title : "查看",
			field : "userName",
			align: 'center',
			valign: 'middle',
			width:  '50px',
			formatter: actionFormatter1
		}, 
		{
			title : "操作",
			field : "status",
			align: 'center',
			valign: 'middle',
			formatter: actionFormatter		
		}],		
		
		clickToSelect: false, 
		
		//点击每一个复选框时触发的操作
		onCheck: function (row,index) {
			//alert(JSON.stringify(index));			
			array.push(row);         	        
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
        	   	
        	
        	if(f==1){
        		alert("矿机限量配置中包含部分重复添加的配置请求，已自动为您去除！");
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
	       			if(rows[i].uuid == array[j].uuid){
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

function minerLimitList(){
	initLimitGrid(array);			
	$("#limitModal").modal("show");
}


function agreeMinerLimit(){
	var array1 = new Array();	
	for(var i=0;i<array.length;i++){
		array1[i] = array[i].uuid;
	}
	if(array1.length==0){
		alert("请通过勾选复选框选择用户矿机配置之后，再执行此操作！");
	} else{ 
		$("#array1").val(array1);
		$("#minerModal").modal("show");
	}
}

//矿机配置批量限制
function auditPi(){
		
	var array2 = new Array();	
	for(var i=0;i<array.length;i++){
		array2[i] = array[i].uuid;
	}
	//alert(JSON.stringify(array2));
	
	var limit = $("#limitMiner").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
		
	checkLimitMiner();
	checkStartTime();
	checkEndTime();
	
	if(check4==1 && check5==1 && check6==1){
		
		var data={
				"uuids":array2,
				"restriction":limit,
				"startTime":startTime,
				"endTime":endTime,
			}
	
		$.ajax({		
			url: "/api/v1/miner/updateUserMinerInfo",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data:JSON.stringify(data),
			processData : false,
			async : false,
	
			success: function(res) {
				if(res.code==0){
					document.getElementById("tipContent").innerText="矿机批量配置限制过程完成";
					$("#Tip").modal('show');
					$("#minerLimit").attr("data-dismiss","modal");//隐藏模态框
					initminerLimitGrid();
					array = [];
					refresh1();
					//location.reload();
				}else{
					document.getElementById("tipContent").innerText = res.message;
					$("#Tip").modal('show');
					$("#minerLimit").attr("data-dismiss","modal");
					initminerLimitGrid();
					array = [];
					refresh1();
				}			
			}, 
			error: function(){
				document.getElementById("tipContent").innerText="矿机批量配置限制过程发生错误";
				$("#Tip").modal('show');
				$("#minerLimit").attr("data-dismiss","modal");
				refresh1();
			}
		}); 
	
	}else{
		alert("请确认输入内容是否符合要求！");
		$("#minerLimit").removeAttr("data-dismiss");
	}
	
}

//矿机批量配置列表
function initLimitGrid(data) {	
	$('#limitGrid').bootstrapTable('destroy');
	$("#limitGrid").bootstrapTable({
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
				width:  '160px',
			},
				{
				title : "限制台数",
				field : "restriction",
				align: 'center',
				valign: 'middle',
				width:  '115px',
			},
			{
				title : "已购台数",
				field : "amount",
				align: 'center',
				valign: 'middle',
				width:  '115px',
				formatter: actionFormatter4
			},{
				title : "开始时间",
				field : "startTime",
				align: 'center',
				valign: 'middle',
				width:  '150px',
			},{
				title : "结束时间",
				field : "endTime",
				align: 'center',
				valign: 'middle',
				width:  '150px',
			}, {
				title : "查看",
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
	var searchValue = $("#miner").val();
    return{
        //每页多少条数据
        pageSize: params.limit,
        //当前页码
        pageNum: params.offset / params.limit + 1,
        searchValue: searchValue,
    }
}

//请求成功方法
function responseHandler(res){
	//alert(JSON.stringify(res));
	dataRes = res;
	if(res.code != 0 ){
		alert("限量配置矿机回显失败！"+res.message);
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
	var restriction = row.restriction;
	var result = "";
	result += "<a onclick=\"agree('" + id + "')\"'>限制 </a>"; 
	result += "<a onclick=\"cancel('" + id + "','" + restriction + "')\"'>&nbsp;&nbsp;取消限制</a>"; 
	return result;
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

function actionFormatter4(value, row, index) {
	var result = "";
	if(value==null){
		result +="<span>0</span>"; 
	}else{
		result +="<span>"+value+"</span>"; 
	}
	return result;
}

function agree(id){
	
	$('#uuid').val(id);
	$("#agreeModal").modal("show");
}

function cancel(id,restriction){
	
	$('#cancelId').val(id);
	$("#Tip1").modal('show');
	
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
