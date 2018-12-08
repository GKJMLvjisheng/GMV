/**
 * 播放功能
 */

document.write("<script language=javascript src='/js/table/table.js'></script>");
/*
 * 进度条
 */
//var index=0;

$(function(){
    var tag = false,ox = 0,left = 0,bgleft = 0;
    $('.progress_btn').mousedown(function(e) {
        ox = e.pageX - left;
        tag = true;
    });
 
    $(document).mouseup(function() {
        tag = false;
    });
 
    $('.progress').mousemove(function(e) {//鼠标移动
        if (tag) {
             left = e.pageX - ox;
             if (left <= 0) {
                left = 0;
             }else if (left > 800) {
                left = 800;
             }
            $('.progress_btn').css('left', left);
            $('.progress_bar').width(left);
            $('.text').html(parseInt((left/800)*100) + '%');
        }
    });
 
    $('.progress_bg').click(function(e) {//鼠标点击
         if (!tag) {
             bgleft = $('.progress_bg').offset().left;
             left = e.pageX - bgleft;
             if (left <= 0) {
                 left = 0;
            }else if (left > 800) {
                 left = 800;
            }
            $('.progress_btn').css('left', left);
            $('.progress_bar').animate({width:left},800);
            $('.text').html(parseInt((left/800)*100) + '%');
         }
    });
});

/*
*实时播放
*/
function playInTime(){
	connect(undefined,function(msg){
		console.log(msg);
		var reData = JSON.parse(msg);
		loadInTime(reData);
	},"topic");
}

//实时播放延迟加载
function loadInTime(data){
	var map = {};
	if(data.length == 0) {
		return;
	}else{
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
/*function loadInTime(data,len){
	if(data.length == 0) {
		return;
	}else{
		if(!map.hasOwnProperty(data[0].updated)){
        	map[data[0].updated] = Array();
        }
        var arr = map[data[0].updated];
        arr.push(data[0]);
    	initTable(arr);
    	data.splice(0,1);
    	setTimeout(function(){
    		loadInTime(data,len);
    	},500)
	}
}*/

/*
*点播放
*/
function play(){
	var data = getData();
	var startTime = progressTime();
	var endTime = data.endTime;
	var $play_span = $("#play span");
	if($play_span.attr("class") == "glyphicon glyphicon-play"){
		$play_span.attr("class","glyphicon glyphicon-pause");
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
//			alert(JSON.stringify(res));
			if(res.code==0){
				var resData=res.data.rows;
				console.log("111",JSON.stringify(resData));
				map={};
				load(resData,startTime,endTime);
			}		
			  else{alert("回显失败！");}			
			}, 
			error: function(){
				alert("失败！");
			}
			}); 
	}else{
		$play_span.attr("class","glyphicon glyphicon-play");
	}
	
}

/*
*快退
*/
function backward(){
	backwardStatus = 1;
	var $play_span = $("#play span");
	if($play_span.attr("class") != "glyphicon glyphicon-play"){
		var data = getData();
		var startTime = progressTime();
		var endTime = data.endTime;
		var date = new Date(startTime);
		newTime = fastDecreaseTime(false,date);
		var start = new Date(startTime.replace("-", "/").replace("-", "/"));
		var end = new Date(newTime.replace("-", "/").replace("-", "/"));
		if(end < start){  
			newTime = startTime;
		}
		var time = {"startTime": newTime};
		console.log(time);
//			alert(time);
		$.ajax({
			url: "/api/v1/load/selectLoadMsgByPeriod",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data: JSON.stringify(time),
			async : false,
			success: function(res) {
//				alert(JSON.stringify(res));
			if(res.code==0){
				var resData=res.data.rows;
				console.log("111",JSON.stringify(resData));
				map={};
/*				var d = 0 - unitLength();
				console.log(d);
				progressBtn(d);*/
				backwardLoad(resData,newTime,endTime);
			}		
			  else{alert("回显失败！");}			
			}, 
			error: function(){
				alert("失败！");
			}
			});
	}
}

/*
*快进
*/
function forward(){
	forwardStatus = 1;
	var $play_span = $("#play span");
	if($play_span.attr("class") != "glyphicon glyphicon-play"){
		var data = getData();
		var startTime = progressTime();
		var endTime = data.endTime;
		var date = new Date(startTime);
		newTime = fastIncreateTime(false,date);
		var start = new Date(newTime.replace("-", "/").replace("-", "/"));
		var end = new Date(endTime.replace("-", "/").replace("-", "/"));
		if(end < start){  
			newTime = endTime;
		}
		var time = {"startTime": newTime};
		console.log(time);
//		alert(time);
		$.ajax({
			url: "/api/v1/load/selectLoadMsgByPeriod",
			contentType : 'application/json;charset=utf8',
			dataType: 'json',
			cache: false,
			type: 'post',
			data: JSON.stringify(time),
			async : false,
			success: function(res) {
//			alert(JSON.stringify(res));
			if(res.code==0){
				var resData=res.data.rows;
				console.log("111",JSON.stringify(resData));
				map={};
/*				var d = unitLength();
				console.log(d);
				progressBtn(d);*/
				forwardLoad(resData,newTime,endTime);
			}		
			  else{alert("回显失败！");}			
			}, 
			error: function(){
				alert("失败！");
			}
			});
	}	 
}


/*
 * 时间的生成
 */

//快进一次1秒
function fastIncreateTime(flag,t) {
    var t_s = t.getTime();//转化为时间戳毫秒数
    t.setTime(t_s + 1000 * 2);
    var time=generateTime(flag,t)
    return time     
}

//快退一次1秒
function fastDecreaseTime(flag,t){
	var t_s = t.getTime();//转化为时间戳毫秒数
    t.setTime(t_s - 1000 * 2);
    var time=generateTime(flag,t)
    return time
}

function generateTime(flag,now){
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
   
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var ss=now.getSeconds();
    if(flag){
     //var clock = JSON.stringify(year);
    var clock = year + "-";
      
          if(month < 10)
            clock += "0";
      
        clock += month + "-";
      
          if(day < 10)
            clock += "0";
          
        clock += day;
              return (clock)
            }
    else{
      clock = year + "-";
   
    if(month < 10)
        clock += "0";
   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day + " ";
   
    if(hh < 10)
        clock += "0";
       
    clock += hh + ":";
    if (mm < 10) clock += '0'; 
    clock += mm+":"; 
     if (ss < 10) clock += '0'; 
    clock += ss; 
    return(clock); 
    }
}