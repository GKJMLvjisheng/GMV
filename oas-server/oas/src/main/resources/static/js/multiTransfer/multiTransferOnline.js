/**
 * 
 */
document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");
$(function() {
	initNormalGrid();
	initTestGrid();
  
	
});
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
		pageSize:20,//分页，页面数据条数
		pageList:[20,50, 100],
		queryParams:queryParams1,//请求服务器时所传的参数
		responseHandler:responseHandler1,//请求数据成功后，渲染表格前的方法		
		dataField: "data",

		//toolbar:"#toolbar",//工具栏
		sortable: false,//是否启用排序
		sortName: 'uuid', // 要排序的字段
	    sortOrder: 'asc', // 排序规则
	    //maintainSelected:true,
	    columns : [{
			
			checkbox:"true",
			
			align: 'center',// 居中显示
			
			field : "box",
			formatter:function action(value,row,index){
				console.log(row.status)
//				if(row.status==1)
//				{return {checked : true }}
			}
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
			
			{
				title : "转账金额",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '98px',
				editable:true,
				formatter:function action(value,row,index){
					value=0;
					row.status=0;
					return value;
				}
			}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,    
		  onCheck:function(row){
	          console.log(row);       
	        },
	        onClickRow : function(row, td,flied){
	        	var data={}
	        	data['name']=row.name;
	        	data['value']=row.value;
	        	$.ajax({

	        		url:"/api/v1/userWallet/updateTransfer",
	        		//headers: {'Authorization': token},

	        		contentType : 'application/json;charset=utf8',
	        		dataType: 'json',
	        		cache: false,
	        		type: 'post',
	        		data:JSON.stringify(data),
	        			
	        		success:function(res){
	        			
	        			if(res.code==0)
	                 {  
	        				
	        					
	        					$("#Tip").modal('show');
	        					
	        					document.getElementById("tipContent").innerHTML="恭喜您，转账成功！";
	        					 //setTimeout(setMoney, 50000);
	        				
	                 }
	                 else{
	                	 alert("转账失败");
	                	
	                 	}
	        		     
	        		},
	        	
	        		error:function(){
	        					alert("请求失败！")
	        				}
	        	}); 
	        }
	        
	});
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
			
			field : "box",
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
			
			{
				title : "转账金额",
				field : "status",
				align: 'center',
				valign: 'middle',
				width:  '98px',
				editable:true,
				formatter:function action(value,row,index){
					value=0;
					row.status=0;
					return value;
				}
			}],		
//		search : true,//搜索
//        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

//请求服务数据时所传参数
function queryParams2(params){
	var searchValue = $("#searchValueTest").val();
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
    function display1()
    {document.getElementById("page2").style.display="none";
    document.getElementById("page1").style.display="block";
    $('#btn1').removeClass('active1').addClass('active');
    $('#btn2').removeClass('active').addClass('active1');
    }
    function display2()
    {
    	//$("#page2").attr()
    	document.getElementById("page1").style.display="none";
    	document.getElementById("page2").style.display="block";
    	$('#btn2').removeClass('active1').addClass('active');
    	$('#btn1').removeClass('active').addClass('active1');
    }
    function getTableData(){
    	 var Global=$("#globalValue").val();
    	 
    	 var rows = $("#normalGrid").bootstrapTable("getSelections");
    	 if(rows.length<1){
    		 alert("请选择要转账的用户");
    		 return;
    	 }
    	 var data = new Array(); 
    	 if(Global){
    		 
    		  Ewin.confirm({ message: "确认要全局配置金额？手动输入金额将不生效"}).on(function (e) {
    		if (!e) {
    			alert("确认不使用全局配置，请清空全局配置金额！")
    		  return;
    		 }
     	if(!validateValue(Global)){
     		alert("请输入正确的全局配置金额！");
     		return;
     	}
    	 for(var i=0;i<rows.length;i++){
    		 var row={};
    		 row['toUserName']=rows[i].name;
    		 row['value']=Global;
    		 data.push(row);
    	 }
    	 console.log(data);
    	 transfer(data);
    	
//    	var row=$.map($("#normalGrid").bootstrapTable('getSelections'),function(row){
//    		alert(12)
//    		return row ;
//    		});
    	 
    		  })  
    }else{
    	 for(var i=0;i<rows.length;i++){
    		 var row={};
    		 row['toUserName']=rows[i].name;
    		 row['value']=rows[i].status;
    		 data.push(row);
    	 }
    	 console.log(data);
    	 transfer(data);
    	}
    }
    //判断正数
    function validateValue(num)
    {
    	var flag=false;
        var flag1=false;
      var reg = /^\d+(?=\.{0,1}\d+$|$)/;//包括0不包括“”
      var reg1=/^0.*$/;
      if(reg.test(num)){
     	 flag=true;}
       if(!reg1.test(num)){
     	  console.log("0")
     	  flag1=true;}
       return flag&&flag1;   
    }
   
   
    //转账接口
    function transfer(data)
    {
        var tableDataLen=data.length;
        
        var sunmary=0;
        var flag=false;
        var flag1=false;
        
        for(var i=0;i<tableDataLen;i++)
        {
        	
         if(data[i]['toUserName']==0||data[i]['value']==0)
     	{		
    	 		flag=true;
    	 		break;}
         
         else if(!validateValue(data[i]['value']))
      	{		
        	
    	 		flag1=true;
    	 		break;
      	}
         //sunmary=sunmary+parseInt(rowAdd['amount']);
         sunmary=numAdd(sunmary,data[i]['value']);
         //sunmary=sunmary+parseFloat(data[i]['amount']);
         
    		}

       if(flag)
       	{	alert("输入金额不能为0");
       		return;}
       if(flag1)
    	{	alert("金额请输入大于0的正数");
    		return;}
    
      
//       if($("#money").text()<sunmary){
//    	   alert("账户余额小于转账金额！");
//    	   return;
//       }
        //var userAddress=$("#userAddress").val();
        var userName="FIRSTONE";
        Ewin.confirm({ message: "确认从账户【"+userName+"】转到【"+tableDataLen+"】个目标账户，总金额为【"+sunmary+"】." }).on(function (e) {
    		if (!e) {
    		  return;
    		 }
    		
       var dataSum={
        		
        		"fromName":userName,
        	    "multiTransferQuota":data,
        	}; 
       console.log(JSON.stringify(dataSum))
    $.ajax({

		url:"/api/v1/userWallet/multiTransfer",
		//headers: {'Authorization': token},

		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(dataSum),
			
		success:function(res){
			
			if(res.code==0)
         {  
				
					
					$("#Tip").modal('show');
					
					document.getElementById("tipContent").innerHTML="恭喜您，转账成功！";
					 //setTimeout(setMoney, 50000);
				
         }
         else{
        	 alert("转账失败");
        	
         	}
		     
		},
	
		error:function(){
					alert("请求失败！")
				}
	}); 
    });
    }
    /**
     * 加法运算，避免数据相加小数点后产生多位数和计算精度损失。
     * 
     * @author: QQQ
     * @param num1加数1 | num2加数2
     */
    function numAdd(num1, num2) {
        var baseNum, baseNum1, baseNum2;
        try {
            baseNum1 = num1.toString().split(".")[1].length;
        } catch (e) {
            baseNum1 = 0;
        }
        try {
            baseNum2 = num2.toString().split(".")[1].length;
        } catch (e) {
            baseNum2 = 0;
        }
        baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
        return (num1 * baseNum + num2 * baseNum) / baseNum;
    };
    function getTestData(){
    	var Global=$("#globalTestValue").val();
   	 
   	 var rows = $("#testGrid").bootstrapTable("getSelections");
   	 if(rows.length<1){
   		 alert("请选择要转账的用户");
   		 return;
   	 }
   	 var data = new Array(); 
   	 if(validateValue(Global)){
   		 
   		  Ewin.confirm({ message: "确认要全局配置金额？手动输入金额将不生效"}).on(function (e) {
   		if (!e) {
   			alert("确认不使用全局配置，请清空全局配置金额！")
   		  return;
   		 }
    	
   	 for(var i=0;i<rows.length;i++){
   		 var row={};
   		 row['toUserName']=rows[i].name;
   		 row['value']=Global;
   		 data.push(row);
   	 }
   	 console.log(data);
   	 transfer(data);
   	
//   	var row=$.map($("#normalGrid").bootstrapTable('getSelections'),function(row){
//   		alert(12)
//   		return row ;
//   		});
   	 
   		  })  
   }else{
   	 for(var i=0;i<rows.length;i++){
   		 var row={};
   		 row['toUserName']=rows[i].name;
   		 row['value']=rows[i].status;
   		 data.push(row);
   	 }
   	 console.log(data);
   	 transfer(data);
   	}
    	
    }