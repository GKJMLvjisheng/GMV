/* 
 * 以下是表格相关func
 *  QQQ
 */
var pageSize;
var pageNum;

//表格回显
$(function(){
	ready();
});
/*$().ready(function() {
	 
	// 模拟进度条：百分数增加，0-30时为红色，30-60为黄色，60-90为蓝色，>90为绿色
	var value = 0;
	setInterval(function(e){
		if (value != 100) {
			value = parseInt(value) + 1;
			$("#prog").css("width", value + "%").text(value + "%");
			if (value>=0 && value<=30) {
				$("#prog").addClass("progress-bar-danger");
		    } else if (value>=30 && value <=60) {
		    	$("#prog").removeClass("progress-bar-danger");
		        $("#prog").addClass("progress-bar-warning");
		    } else if (value>=60 && value <=90) {
		        $("#prog").removeClass("progress-bar-warning");
		        $("#prog").addClass("progress-bar-info");
		    } else if(value >= 90 && value<100) {
		        $("#prog").removeClass("progress-bar-info");
		        $("#prog").addClass("progress-bar-success");    
		    }
		}
	}, 50);
});*/
//初始化表格
function initTable(data) {
	$('#table').bootstrapTable('destroy');
	$("#table").bootstrapTable({
		contentType : "application/x-www-form-urlencoded",
		dataType:"json",
		pagination:false,//显示分页条：页码，条数等
//		striped:true,//隔行变色
//		pageNumber:1,//首页页码
		sidePagination:"client",
		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],	
		toolbar:"#toolbar",//工具栏
		data:data,
		columns : [{  
			title: "参数代码",  
			field: "minerName",
			align: 'center',
			valign: 'middle', 
			width:  '40px',
			},
			{
			title : "参数名称",
			field : "minerDescription",
			align: 'center',
			valign: 'middle',
			width:  '40px',
		    },
			{
			title : "参数意义",
			field : "loadPicturePath",
			align: 'center',
			valign: 'middle',
			width:  '100px',
			},
			{
			title : "参数源码",
			field : "minerPrice",
			align: 'center',
			valign: 'middle',
			width:  '40px',
//			},
//			{
//			title : "比特位",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "结果",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "超限",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "范围",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
//			},
//			{
//			title : "单位",
//			field : "",
//			align: 'center',
//			valign: 'middle',
//			width:  '80px',
		}],
		
//		search : true,//搜索
//        searchOnEnterKey : true,
//		clickToSelect: false, 
	});
}
/*
 * 得到初始数据
 */
function getData(){
	var data;
	var data1={"number": 1};
	$.ajax({		
		url: "/api/v1/load/selectLoadMsg",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data: JSON.stringify(data1),
		async : false,
		success: function(res) {
//		alert(JSON.stringify(res));
		if(res.code==0)
			{data=res.data;
			console.log("111",JSON.stringify(data.rows));
			}		
		  else{alert("回显失败！");}			
		}, 
		error: function(){
			alert("失败！");
		}
		}); 
	    return data;
}

function ready(){
    $('#table').bootstrapTable('destroy');
	 var resData = getData();
	 var data = resData.rows;
	 console.log(JSON.stringify(data));
	 initTable(data);
}

function progress(){
	index = 0;
	var time = {"startTime": startTime};
	console.log(time);
//	alert(time);
	$.ajax({
		url: "/api/v1/load/selectLoadMsgByPeriod",
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data: JSON.stringify(time),
		async : false,
		success: function(res) {
//		alert(JSON.stringify(res));
		if(res.code==0){
			var resData=res.data.rows;
			console.log("111",JSON.stringify(resData));
			map={};
			load(resData,resData.length);
		}		
		  else{alert("回显失败！");}			
		}, 
		error: function(){
			alert("失败！");
		}
		}); 
}

//延迟加载
function load(data,len){
	var map = {};
	if(data.length == 0) {
		playInterval();
		return;
	}else{
		//var time_t = data[0].updated;
		for(var i=0; i<data.length; i++){
			if(!map.hasOwnProperty(data[i].updated)){
	        	map[data[i].updated] = Array();
	        	var arr = map[data[i].updated];
		        arr.push(data[i]);
	        }else{
	        	var arList = map[data[i].updated];
	        	arList.push(data[i]);
	        	map[data[i].updated] = arList;
	        }	        	
//	        console.log("arr:", arr);	    
		}
		/*map.sort(function(a,b){
			return a.key-b.key;
		});*/
		for(var i in map){
			var ii = 0;
			initTable(map[i]);
			data.splice(0,map[i].length);
			if(ii == 0) break;
		}
		setTimeout(function(){
    		load(data,len);
    	},1000)
		return;
	}
}

