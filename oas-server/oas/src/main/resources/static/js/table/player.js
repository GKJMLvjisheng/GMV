/**
 * 播放功能
 */

document.write("<script language=javascript src='/js/table/table.js'></script>");
/*
 * 进度条
 */
var index=0,map={},timer ;
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
    
    //clearInterval(timer)
});

/*
*快退
*/
function backward(){
	
}

/*
*播放
*/
function play(){
	index = 0;
	var data = getData();
	var startTime = data.startTime;
	console.log(startTime);
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
			data=res.data.rows;
			console.log("111",JSON.stringify(data));
			map={};
			load(data,data.length);
		}		
		  else{alert("回显失败！");}			
		}, 
		error: function(){
			alert("失败！");
		}
		}); 
//	timer = setInterval(function(){
//    	play();
//    },1000)
}

//延迟加载
function load(data,len){
	if(data.length == 0) return;
	if(index == len){
		return;
	}else{
		if(!map.hasOwnProperty(data[0].updated)){
        	map[data[0].updated] = Array();
        }
        var arr = map[data[0].updated];
        arr.push(data[0]);
        $("#test").text(arr.length);
    	initTable(arr);
    	data.splice(0,1);
    	setTimeout(function(){
    		load(data,len);
    	},500)
	}
	index++;
}


/*
*快进
*/
function forward(){
	
}