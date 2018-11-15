var pageSize;
var pageNum;
var Data;

function initDownUserGrid() {	
	$("#downUserGrid").bootstrapTable('destroy');
	$("#downUserGrid").bootstrapTable({
		url: '/api/v1/userCenter/inquireInvitedUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams,//请求服务器时所传的参数
		responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'created', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '145px',
				//visible: false,
			}],		

			clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams(params){
	var name = $("#downName").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,    
        name: name,    
       
    }
}
//请求成功方法
function responseHandler(res){
	//alert(JSON.stringify(res));
    var code = res.code;//在此做了错误代码的判断
    Data = res.data.rows;
    if(code != 0){
        alert("下级用户查看回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initNormalGrid() {	
	$("#normalGrid").bootstrapTable('destroy');
	$("#normalGrid").bootstrapTable({
		url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams1,//请求服务器时所传的参数
		responseHandler:responseHandler1,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		//toolbar:"#toolbarNormal",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{
			
			checkbox:"true",
			
			align: 'center',// 居中显示
			valign: 'middle',
			
			field : "box",
			width:  '50px',
			
			
		},{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
//			{
//				title : "昵称",
//				field : "nickname",
//				align: 'center',
//				valign: 'middle',
//				width:  '90px',
//				
//			},
//			{
//				title : "手机",
//				field : "mobile",
//				align: 'center',
//				valign: 'middle',
//				width:  '110px',
//			},
			
			{
				title : "IMEI",
				field : "imei",
				align: 'center',
				valign: 'middle',
				width:  '95px',
				
			},{

				title : "重置IMEI",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter1
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '100px',
				//visible: false,
			},{
				title : "三级矿机购买授权",
				field : "minerThreeRestriction",
				align: 'center',
				valign: 'middle',
				width:  '98px',
				formatter:author
					
				
			},{
				title : "账号状态",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter5
				
			},{

				title : "操作",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter7
			},{

				title : "查看下级用户",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter6
			},{

				title : "角色授权",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter3
			},{

				title : "切换账号状态",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter4
			}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,   
		onCheck:function(row,index){
			var box=$("#normalGrid input[name='btSelectAll']");
			var data={name:row.name,
					minerThreeRestriction:row.minerThreeRestriction};
			var rows=$("#normalMinnerGrid").bootstrapTable('getData');
			
			for(var i=0;i<rows.length;i++){
				if(rows[i].name==data.name){
					alert("该条数据已选择");
					
					return ;
				}
				if(rows[i].minerThreeRestriction!=data.minerThreeRestriction){
					alert("请选择统一的授权状态用户");
					box[0].checked = false;
					return index.prop("checked",false)
				}
				
			};
			$('#normalMinnerGrid').bootstrapTable('prepend', data);
			
			//initNormalMinerGrid(data)
	        },
	        onCheckAll:function(rows){
	        	var str=JSON.stringify(rows)
	        	var rowBackup=JSON.parse(str)
	        	console.log(rows)
	        	//var table = document.getElementById("normalMinnerGrid");
	        	//var boxes = document.getElementsByName("btSelectItem");
	        	var boxes = $("#normalGrid input[name='btSelectItem']");
	        	var box=$("#normalGrid input[name='btSelectAll']");
	        	console.log(box)
//	        	console.log(boxes)
//	            for(i=0;i<boxes.length;i++){
//	                for(j=0;j<val.length;j++){
//	                    if(boxes[i].value == val[j]){
//	                        boxes[i].checked = true;
//	                        break
//	                    }
//	                }
//	            }
	        	var rowsMinner=$("#normalMinnerGrid").bootstrapTable('getData');
	        	for(var j=0;j<rowsMinner.length;j++)
	        	{
	        		for(var i=0,ii=0;i<rows.length;i++,ii++){
	        		
	        		if(rowsMinner[j].minerThreeRestriction!=rows[i].minerThreeRestriction){
	        			rows.splice(i,1);
	        			console.log(ii)
	        			boxes[ii].checked = false;
	        			box[0].checked = false;
						i--;
						}else if(rowsMinner[j].name==rows[i].name){
							rows.splice(i,1);
		        			
		        			
							i--;
						}

				}
	        };
	        //var author=[];unAuthor=[];
	        var flag=true;
	        	for(var k=0;k<rows.length-1;k++){
        			if(rows[k].minerThreeRestriction!=rows[k+1].minerThreeRestriction){
        				flag=false;
        				break;
        			}
	        	}
	        	if(!flag)
	        	{Ewin.confirm({ message: "由于所选用户状态不同，请确认即将对选择的用户进行授权吗？"+"<br/>"+"【确定】为对未授权用户进行授权操作！【取消】为对已授权用户进行取消授权！" }).on(function (e) {
	         		if (!e) {
	         			//var ii=0;
	         			for(var k=0 ,ii=0;k<rows.length;k++,ii++){
		        			if(rows[k].minerThreeRestriction!=1){
		        				rows.splice(k,1);
		        				//rowBackup
		        				boxes[ii].checked = false;
		        				box[0].checked = false;
		        				
								k--;
								console.log(rows)
		        			}
			        	}
	         		 }else{
		         		for(var k=0 ,ii=0;k<rows.length;k++,ii++){
		        			if(rows[k].minerThreeRestriction!=0){
		        				rows.splice(k,1);
		        				boxes[ii].checked = false;
		        				box[0].checked = false;
								k--;
		        			}
			        	}
	         		 }	
	         		$('#normalMinnerGrid').bootstrapTable('prepend', rows);
	         });
	        	}else{
	        	
	        
	        	$('#normalMinnerGrid').bootstrapTable('prepend', rows);  }
	        	
	 
	          },
	          onUncheckAll:function(rows){
	        	 
	        	  for(var i=0;i<rows.length;i++){
	        	  $("#normalMinnerGrid").bootstrapTable('removeByUniqueId', rows[i].name);  
	        	  }
	          },
	          
	          onUncheck:function(row){
	        	  $("#normalMinnerGrid").bootstrapTable('removeByUniqueId', row.name);       
	            },
//	            rowStyle: function (row, index) {
//	            if (row.minerThreeRestriction == 0) {
//                    strclass = 'success';//还有一个active
//                }
//                else if (row.minerThreeRestriction == 1) {
//                    strclass = 'danger';
//                }
//                else {
//                    return {};
//                }
//                return { classes: strclass }
//            },
	});
}
//function initNormalMinerGrid() {	
	//$("#normalMinnerGrid").bootstrapTable('destroy');

function initNormalMinerGrid() {
		$("#normalMinnerGrid").bootstrapTable('destroy');
	
	$("#normalMinnerGrid").bootstrapTable({
		//url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"client",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:5,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		//请求服务器时所传的参数
		responseHandler:responseHandler1,//请求数据成功后，渲染表格前的方法		
		//data: data,

		//toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {
				  var value="";
		           var pageSize=10;
		         	
		           //获取当前是第几页        
		           var pageNumber=1;       
		           //返回序号，注意index是从0开始的，所以要加上1         
		            value=pageSize*(pageNumber-1)+index+1;
		            
		            return value;
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},

			{
				title : "三级矿机购买授权",
				field : "minerThreeRestriction",
				align: 'center',
				valign: 'middle',
				width:  '98px',
				formatter: authorReady
					
				
			},{

				title : "操作",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: function action(value, row, index){
					var id=row.name
					var result="";
					result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";
				return result;
				}
			}],		

	});
	};

function author(value, row, index) {
    var result = "";
  if(value==1){
	result += "<span style='color:#c12e2a'>已授权</span>";      
    return result;
	}else if(value==0){
	result += "<span style='color:#3e8f3e'>未授权</span>";      
    return result;
	} 
}
function authorReady(value, row, index){
	 var result = "";
	  if(value==1){
		result += "<span style='color:#c12e2a'>准备取消授权</span>";      
	    return result;
		}else if(value==0){
		result += "<span style='color:#3e8f3e'>准备授权</span>";      
	    return result;
		} 
}
function deleteById(id){
	//var boxes = document.getElementsByName("btSelectItem");
	var boxes =$("#normalGrid input[name='btSelectItem']");
	var rows=$("#normalGrid").bootstrapTable('getData');
	$("#normalMinnerGrid").bootstrapTable('removeByUniqueId', id);
	for(var i=0;i<rows.length;i++){
		if(rows[i].name==id)
		{boxes[i].checked = false;}
	}

}
//请求服务数据时所传参数
function queryParams1(params){
	var searchValue = $("#searchValue").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,      
        roleId: 2,
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler1(res){
	//alert(JSON.stringify(res));
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("正常账号回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initTestGrid() {	
	$("#testGrid").bootstrapTable('destroy');
	$("#testGrid").bootstrapTable({
		url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams2,//请求服务器时所传的参数
		responseHandler:responseHandler2,//请求数据成功后，渲染表格前的方法		
		dataField: "data",
		
		//toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{
			
			checkbox:"true",
			
			align: 'center',// 居中显示
			valign: 'middle',
			
			field : "box",
			width:  '50px',
			
			
		},{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
//			{
//				title : "昵称",
//				field : "nickname",
//				align: 'center',
//				valign: 'middle',
//				width:  '90px',
//				
//			},
//			{
//				title : "手机",
//				field : "mobile",
//				align: 'center',
//				valign: 'middle',
//				width:  '110px',
//			},
//			{
//				title : "邮箱",
//				field : "email",
//				align: 'center',
//				valign: 'middle',
//				width:  '98px',
//			},
			{
				title : "IMEI",
				field : "imei",
				align: 'center',
				valign: 'middle',
				width:  '95px',
				
			},{

				title : "重置IMEI",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter1
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '100px',
				//visible: false,
			},{
				title : "三级矿机购买授权",
				field : "minerThreeRestriction",
				align: 'center',
				valign: 'middle',
				width:  '98px',
				formatter:author
					
				
			},{
				title : "账号状态",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter5
				
			},{

				title : "操作",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				formatter: actionFormatter7
			},{

				title : "查看下级用户",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter6
			},{

				title : "角色授权",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter3
			},{

				title : "切换账号状态",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter4
			}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,    
		onCheck:function(row,index){
			var box=$("#testGrid input[name='btSelectAll']");
			var data={name:row.name,
					minerThreeRestriction:row.minerThreeRestriction};
			var rows=$("#testMinnerGrid").bootstrapTable('getData');
			
			for(var i=0;i<rows.length;i++){
				if(rows[i].name==data.name){
					alert("该条数据已选择");
					
					return ;
				}
				if(rows[i].minerThreeRestriction!=data.minerThreeRestriction){
					alert("请选择统一的授权状态用户");
					box[0].checked = false;
					return index.prop("checked",false)
				}
				
			};
			$('#testMinnerGrid').bootstrapTable('prepend', data);
			
			//initNormalMinerGrid(data)
	        },
	        onCheckAll:function(rows){
	        	var str=JSON.stringify(rows)
	        	var rowBackup=JSON.parse(str)
	        	console.log(rows)
//	        	var boxes = document.getElementsByName("btSelectItem");
//	        	var box=document.getElementsByName("btSelectAll");
	        	var boxes = $("#testGrid input[name='btSelectItem']");
	        	var box=$("#testGrid input[name='btSelectAll']");
	        	//console.log(box)
//	        	console.log(boxes)
//	            for(i=0;i<boxes.length;i++){
//	                for(j=0;j<val.length;j++){
//	                    if(boxes[i].value == val[j]){
//	                        boxes[i].checked = true;
//	                        break
//	                    }
//	                }
//	            }
	        	var rowsMinner=$("#testMinnerGrid").bootstrapTable('getData');
	        	for(var j=0;j<rowsMinner.length;j++)
	        	{
	        		for(var i=0,ii=0;i<rows.length;i++,ii++){
	        		
	        		if(rowsMinner[j].minerThreeRestriction!=rows[i].minerThreeRestriction){
	        			rows.splice(i,1);
	        			console.log(ii)
	        			boxes[ii].checked = false;
	        			box[0].checked = false;
						i--;
						}else if(rowsMinner[j].name==rows[i].name){
							rows.splice(i,1);
		        			
		        			
							i--;
						}

				}
	        };
	        //var author=[];unAuthor=[];
	        var flag=true;
	        	for(var k=0;k<rows.length-1;k++){
        			if(rows[k].minerThreeRestriction!=rows[k+1].minerThreeRestriction){
        				flag=false;
        				break;
        			}
	        	}
	        	if(!flag)
	        	{Ewin.confirm({ message: "由于所选用户状态不同，请确认即将对选择的用户进行授权吗？"+"<br/>"+"【确定】为对未授权用户进行授权操作！【取消】为对已授权用户进行取消授权！" }).on(function (e) {
	         		if (!e) {
	         			//var ii=0;
	         			for(var k=0 ,ii=0;k<rows.length;k++,ii++){
		        			if(rows[k].minerThreeRestriction!=1){
		        				rows.splice(k,1);
		        				//rowBackup
		        				boxes[ii].checked = false;
		        				box[0].checked = false;
		        				
								k--;
								console.log(rows)
		        			}
			        	}
	         		 }else{
		         		for(var k=0 ,ii=0;k<rows.length;k++,ii++){
		        			if(rows[k].minerThreeRestriction!=0){
		        				rows.splice(k,1);
		        				boxes[ii].checked = false;
		        				box[0].checked = false;
								k--;
		        			}
			        	}
	         		 }	
	         		$('#testMinnerGrid').bootstrapTable('prepend', rows);
	         });
	        	}else{
	        	
	        
	        	$('#testMinnerGrid').bootstrapTable('prepend', rows);  }
	        	
	 
	          },
	          onUncheckAll:function(rows){
	        	 
	        	  for(var i=0;i<rows.length;i++){
	        	  $("#testMinnerGrid").bootstrapTable('removeByUniqueId', rows[i].name);  
	        	  }
	          },
	          
	          onUncheck:function(row){
	        	  $("#testMinnerGrid").bootstrapTable('removeByUniqueId', row.name);       
	            },

	});
}


function initTestMinerGrid() {
		$("#testMinnerGrid").bootstrapTable('destroy');
	
	$("#testMinnerGrid").bootstrapTable({
		//url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"client",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:5,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		
		//请求服务器时所传的参数
		responseHandler:responseHandler1,//请求数据成功后，渲染表格前的方法		
		//data: data,

		//toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {
				  var value="";
		           var pageSize=10;
		         	
		           //获取当前是第几页        
		           var pageNumber=1;       
		           //返回序号，注意index是从0开始的，所以要加上1         
		            value=pageSize*(pageNumber-1)+index+1;
		            
		            return value;
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},

			{
				title : "三级矿机购买授权",
				field : "minerThreeRestriction",
				align: 'center',
				valign: 'middle',
				width:  '98px',
				formatter: authorReady
					
				
			},{

				title : "操作",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: function action(value, row, index){
					var id=row.name
					var result="";
					result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteTestById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";
				return result;
				}
			}],		

	});
	};
function deleteTestById(id){
	//var boxes = document.getElementsByName("btSelectItem");
	var boxes=$("#testGrid input[name='btSelectItem']");
	var rows=$("#testGrid").bootstrapTable('getData');
	$("#testMinnerGrid").bootstrapTable('removeByUniqueId', id);
	for(var i=0;i<rows.length;i++){
		if(rows[i].name==id)
		{boxes[i].checked = false;}
	}
}
//请求服务数据时所传参数
function queryParams2(params){
	var searchValue = $("#user2").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,  
        //测试账号
        roleId: 3,  
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler2(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("测试账号回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};


function initSystemGrid() {	
	$("#systemGrid").bootstrapTable('destroy');
	$("#systemGrid").bootstrapTable({
		url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams3,//请求服务器时所传的参数
		responseHandler:responseHandler3,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
			{
				title : "昵称",
				field : "nickname",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				
			},
			{
				title : "手机",
				field : "mobile",
				align: 'center',
				valign: 'middle',
				width:  '120px',
			},
			{
				title : "邮箱",
				field : "email",
				align: 'center',
				valign: 'middle',
				width:  '140px',
			},
			{
				title : "IMEI",
				field : "imei",
				align: 'center',
				valign: 'middle',
				width:  '120px',
				
			},{

				title : "重置IMEI",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter1
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '145px',
				//visible: false,
			},{

				title : "查看用户信息",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter2
			}
//			,{
//
//				title : "角色授权",
//				field : "name",
//				align: 'center',
//				valign: 'middle',
//				width:  '80px',
//				formatter: actionFormatter3
//			},{
//
//				title : "账号状态",
//				field : "name",
//				align: 'center',
//				valign: 'middle',
//				width:  '80px',
//				formatter: actionFormatter4
//			}
			],		
//		  search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams3(params){
	var searchValue = $("#user3").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,
        //系统账号
        roleId: 1,
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler3(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("系统账号回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function initOperationGrid() {	
	$("#operationGrid").bootstrapTable('destroy');
	$("#operationGrid").bootstrapTable({
		url: '/api/v1/userCenter/selectAllUsers',
		contentType : "application/json",
		dataType:"json",
		method: 'post',
		striped:true,//隔行变色
		uniqueId:"name",
		
		pagination:true,//显示分页条：页码，条数等		
		sidePagination:"server",//在服务器分页
		pageNumber:1,//首页页码
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		queryParams:queryParams4,//请求服务器时所传的参数
		responseHandler:responseHandler4,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
			
	    columns : [{  
			title: '序号',  
			field: '',
			align: 'center',
			valign: 'middle', 
			width:  '50px',
			formatter: function (value, row, index) {  
				return pageSize * (pageNum - 1) + index + 1; 
				}  
			}  ,{
				title : "用户名",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '90px',
			},
			{
				title : "昵称",
				field : "nickname",
				align: 'center',
				valign: 'middle',
				width:  '90px',
				
			},
			{
				title : "手机",
				field : "mobile",
				align: 'center',
				valign: 'middle',
				width:  '110px',
			},
			{
				title : "邮箱",
				field : "email",
				align: 'center',
				valign: 'middle',
				width:  '120px',
			},
			{
				title : "IMEI",
				field : "imei",
				align: 'center',
				valign: 'middle',
				width:  '120px',
				
			},{

				title : "重置IMEI",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter1
			},
				{
				title : "创建时间",
				field : "created",
				align: 'center',
				valign: 'middle',
				width:  '145px',
				//visible: false,
			},{

				title : "账号状态",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter5
			},{

				title : "操作",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '50px',
				formatter: actionFormatter2
			}
			,{

				title : "切换账号状态",
				field : "name",
				align: 'center',
				valign: 'middle',
				width:  '80px',
				formatter: actionFormatter4
			}
			],		
//		  search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams4(params){
	var searchValue = $("#user5").val();
	pageSize = params.limit;
	pageNum = params.offset / params.limit + 1;	
	
    return{
        //每页多少条数据
        pageSize: pageSize,
        //当前页码
        pageNum: pageNum,
        //系统账号
        roleId: 4,
        searchValue: searchValue,
    }
}
//请求成功方法
function responseHandler4(res){
    var code = res.code;//在此做了错误代码的判断
    if(code != 0){
        alert("运营账号回显失败，错误代码:" + code);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
        total : res.data.total, //后面total总记录的条数,前面total总页数，前面的key必须为"total"
        data : res.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
};

function actionFormatter1(value, row, index) {
	 var name = value;
	 var roleId = row.roleId;
	var result = "";	
	result += "<input type='radio' onclick=\"reset('" + name + "', '" + roleId + "')\"' name='radio' id='reset' value='1'> 重置";
	return result;	
}

function actionFormatter2(value, row, index) {
    var name = value;
    var roleId = row.roleId;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + name + "','" + roleId + "')\" title='查看用户信息'><span class='glyphicon glyphicon-search'></span></a>";      
    return result;
}

function actionFormatter7(value, row, index) {
	//alert(JSON.stringify(row));
    var name = value;
    var code = row.inviteFrom;
    var roleId = row.roleId;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"ViewViewById('" + name + "')\" title='查看用户信息'><span class='glyphicon glyphicon-search'></span></a>";   
    result += "<a href='javascript:;' id='viewCode' class='btn btn-xs green' onclick=\"ViewCode('" + name + "','" + roleId + "','" + code + "')\" title='修改上级用户邀请码'><span class='glyphicon glyphicon-pencil'></span></a>";      
    return result;
}

function actionFormatter3(value, row, index) {
	 var name = value;
	 var roleId = row.roleId;
	 var result = "";	
	
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"role('" + name + "', '" + roleId + "')\">角色授权</a>";
	return result;	
}

function actionFormatter4(value, row, index) {
	var name = value;
	var roleId = row.roleId;
	var result = "";
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"accountStatus('" + name + "', '" + roleId + "')\">切换账号状态</a>";
	return result;	
}

function actionFormatter5(value, row, index) {
    var result = "";
    //alert(JSON.stringify(value))
    if(value==2){
    result += "<span>未激活</span>";
    return result;
    }else if(value==1){
	result += "<span>激活</span>";      
    return result;
	}else if(value==0){
	result += "<span>禁用</span>";      
    return result;
	}    
}	

function actionFormatter6(value, row, index) {
	 var name = value;
	 var roleId = row.roleId;
	 var result = "";	
	
	result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"downUser('" + name + "', '" + roleId + "')\">查看下级用户</a>";
	return result;	
}	

function ViewViewById(name,roleId){	
	var data = {
		"name": name
	};
	$.ajax({
	url:"/api/v1/userCenter/inquireUserKYCInfo",
	contentType : 'application/json;charset=utf8',
	dataType: 'json',
	cache: false,
	type: 'post',
	data:JSON.stringify(data),
	processData : false,
	async : false,

	success:function(res){				
		if(res.code==0){
			//alert(JSON.stringify(res));			
			var rows = res.data;
			var verifyStatus = rows.verifyStatus;
			var supriorUserInviteCode = rows.supriorUserInviteCode;
			if(verifyStatus==0){
				var status = "未认证";
				document.getElementById("userIdentityName1").style.display="none";
				document.getElementById("userIdentityNumber1").style.display="none";
				document.getElementById("remark1").style.display="none";
				
				document.getElementById("verifyStatus1").style.display="block";
				document.getElementById("upName1").style.display="block";
				document.getElementById("code1").style.display="block";
			}else if(verifyStatus==1){
				var status = "未审核";
				document.getElementById("userIdentityName1").style.display="none";
				document.getElementById("userIdentityNumber1").style.display="none";
				document.getElementById("remark1").style.display="none";
				
				document.getElementById("verifyStatus1").style.display="block";
				document.getElementById("upName1").style.display="block";
				document.getElementById("code1").style.display="block";
			}else if(verifyStatus==2){
				var status = "已通过";
				document.getElementById("userIdentityName1").style.display="block";
				document.getElementById("userIdentityNumber1").style.display="block";
				document.getElementById("remark1").style.display="none";
				
				document.getElementById("verifyStatus1").style.display="block";
				document.getElementById("upName1").style.display="block";
				document.getElementById("code1").style.display="block";
			}else if(verifyStatus==3){
				var status = "未通过";
				document.getElementById("userIdentityName1").style.display="none";
				document.getElementById("userIdentityNumber1").style.display="none";
				document.getElementById("remark1").style.display="block";
				
				document.getElementById("verifyStatus1").style.display="block";
				document.getElementById("upName1").style.display="block";
				document.getElementById("code1").style.display="block";
			}
			
			if(roleId==4){
				document.getElementById("verifyStatus1").style.display="none";
				document.getElementById("userIdentityName1").style.display="none";
				document.getElementById("userIdentityNumber1").style.display="none";
				document.getElementById("remark1").style.display="none";
				document.getElementById("upName1").style.display="none";
				document.getElementById("code1").style.display="none";
			}
			
			$('#Qname').val(rows.name);
			$('#Qnickname').val(rows.nickname);
			$('#Qgender').val(rows.gender);
			$('#Qbirthday').val(rows.birthday);
			$('#Qmobile').val(rows.mobile);
			$('#Qemail').val(rows.email);
			$('#Qaddress').val(rows.address);
			$('#QinviteCode').val(rows.inviteCode);
			
			$('#QverifyStatus').val(status);				
			$('#QuserIdentityName').val(rows.userIdentityName);
			$('#QuserIdentityNumber').val(rows.userIdentityNumber);
			$('#Qremark').val(rows.remark);	
			
			$('#QupName').val(rows.supriorUserName);	
			$('#Qcode').val(supriorUserInviteCode);				
		
			$("#queryModal").modal("show");
		}

		else{
			alert("查询失败，错误码："+res.code);
			}						
	},

	error:function(){
		alert("查询过程发生错误！");
	},
	});					
}