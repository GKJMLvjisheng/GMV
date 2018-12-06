/* 
 * 以下是表格相关func
 *  QQQ
 */
var backwardStatus = 0; //快退标志位
var forwardStatus = 0; //快退标志位
var loadT;
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
			console.log("rows:",JSON.stringify(data.rows));
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
	if($("#play span").attr("class") == "glyphicon glyphicon-play"){
		$("#play span").attr("class","glyphicon glyphicon-pause");
	}
	var data = getData();
	var endTime = data.endTime;
	var newTime = progressTime();
	var time = {"startTime": newTime};
	var btn_p = $('.progress_btn').css("left");
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
			load(resData,newTime,endTime);
			$('.progress_btn').css("left",btn_p);
		}		
		  else{alert("回显失败！");}			
		}, 
		error: function(){
			alert("失败！");
		}
		}); 
}

//正常情况延迟加载
function load(data,startTime,endTime){
	var startT = startTime;
	if($("#play span").attr("class") == "glyphicon glyphicon-play"){
		return;
	}
	if(backwardStatus == 1 || forwardStatus == 1){
		backwardStatus = 0;
		forwardStatus = 0;
		return;
	}
	request(data,startTime,endTime);
	
	loadT = setTimeout(function(){
		load(data,startTime,endTime);
	},1000)
	
	return;
}

//快进延迟加载
function forwardLoad(data,startTime,endTime){
	if($("#play span").attr("class") == "glyphicon glyphicon-play"){
		return;
	}
	if(backwardStatus == 1){
		backwardStatus = 0;
		return;
	}
	request(data,startTime,endTime);
	setTimeout(function(){
		forwardLoad(data,startTime,endTime);
	},1000)
	return;
}

//快退延迟加载
function backwardLoad(data,startTime,endTime){
	if($("#play span").attr("class") == "glyphicon glyphicon-play"){
		return;
	}
	if(forwardStatus == 1){
		forwardStatus = 0;
		return;
	}
	request(data,startTime,endTime);
	setTimeout(function(){
		backwardLoad(data,startTime,endTime);
	},1000)
	return;
}

function request(data,startTime,endTime){
	var map = {};
	if(data.length == 0) {
/*		var date = new Date(startTime);
		startTime = increateTime(false,date);*/
		playInterval(startTime,endTime);
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
			var d = unitLength();
			console.log(d);
			progressBtn(d);
			data.splice(0,map[i].length);
			if(ii == 0) break;
		}
	}
}

//设置每隔一段时间向服务器请求一次
function playInterval(startTime, endTime){
	if($("#play span").attr("class") == "glyphicon glyphicon-play"){
		return;
	}
	var start = new Date(startTime.replace("-", "/").replace("-", "/"));
	var end = new Date(endTime.replace("-", "/").replace("-", "/"));
	if(end > start){  
		var date = new Date(startTime);
		startTime = increateTime(false,date);
	}else{
		$("#play span").attr("class","glyphicon glyphicon-play");
		return;
	}
	console.log(startTime);
	var time = {"startTime": startTime};
	console.log(time);	
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
			data=res.data.rows;
			data.splice(0,4);
			console.log("111",JSON.stringify(data));
			map={};
			clearTimeout(loadT);
			load(data,startTime,endTime);
		}		
		  else{alert("回显失败！");}			
		}, 
		error: function(){
			alert("失败！");
		}
		}); 
}

//进度条定位对应时间点
function progressTime(date1, date2){
	var data = getData();
	var startTime = data.startTime;
	var endTime = data.endTime;
	var date1 = new Date(startTime); 
	var date2 = new Date(endTime);
	var s1 = date1.getTime();
	var s2 = date2.getTime();
	var timeSum = (s2-s1) / 1000;
	var value = $('.progress_btn').css("left").replace('px','') / 800;
	console.log(value);
	var timeGap = timeSum * value;
	console.log(timeGap);
	date1.setTime(s1 + timeGap * 1000);
	var newTime = generateTime(false,date1);
	return newTime;
}

//进度小方块的移动
function progressBtn(d){ 
	var width = Number($('.progress_bar').css("width").replace('px',''));
	var left = Number($('.progress_btn').css("left").replace('px',''));
	width = width + d;
	left = left + d;
	$('.progress_bar').css("width", width);
	$('.progress_btn').css("left", left);
}

//一秒对应的进度条的长度
function unitLength(){
	var data = getData();
	var startTime = data.startTime;
	var endTime = data.endTime;
	var date1 = new Date(startTime); 
	var date2 = new Date(endTime);
	var s1 = date1.getTime();
	var s2 = date2.getTime();
	var timeSum = (s2-s1) / 1000
	var unitLength = 800 / timeSum;
	return unitLength;
}

//时间增加1秒请求一次
function increateTime(flag,t) {
    var t_s = t.getTime();//转化为时间戳毫秒数
    t.setTime(t_s + 1000 * 1);
    var time=generateTime(flag,t)
    return time     
}
