
 
 // 进入网页读取前20秒数进行回显；利用函数allPointCreate()获取最后一圈全部数据，为非实时动态曲线缓存数据
 
//通过Ajax获取静态图表数据
var startTime='';          //全局，记录滑动条的时间
var startTimeInitial ='';  //最后一圈的初始时间
var endTime ="";           //最后一圈的终止时间
$(function(){
	var data1={
			"number":20
	};
    var x = [];//X轴
    var y = [];//Y轴
    var xtext = [];//X轴TEXT
	$.ajax({
		url: '/api/v1/test/selectTestMsg',
	    contentType : 'application/json;charset=utf8',
	    dataType: 'json',
	    cache: false,
	    type: 'post',
	    data:JSON.stringify(data1),
	    async : false,
	    success: function(res) {
	    	alert(JSON.stringify(res));
	    	startTimeInitial =res.data.startTime;
	    	endTime = res.data.endTime;
	        var data2=res.data.testModelList;
	        var map={};
	        var map2 ={};
	        for (var i in data2){
	        	//alert(JSON.stringify(data2[i]));
	            var data3 = data2[i];
	            //alert(JSON.stringify(data3.parameter));
	            if(!map.hasOwnProperty(data3.parameter)){
	            	map[data3.parameter] = Array();
	            	map2[data3.parameter] = Array();
	            	}
	            var arr = map[data3.parameter];
	            //var arr2 =map2[data3.parameter]
	            arr2 =map2[data3.parameter];
	            arr.push(data3);
 	        	//arr[arr.length-1].x =data3.time;
 	        	arr[arr.length-1].y = Number(data3.value)   //给Y轴赋值
 	        	if(xtext[xtext.length-1]!=data3.time){
 	        		xtext.push(data3.time);//给X轴TEXT赋值
 	        		//startTime=xtext[0];
 	        		startTime=xtext[xtext.length-1];   //动态取值的初始时间：startTime+1
 	        		
 	        		}	     	  
 	        	var a ={};
	        	a.x =new Date(xtext[xtext.length-1]).getTime();
				a.y = arr[arr.length - 1].y;
	 	        arr2.push(a);
	 	        map2[data3.parameter] = arr2;
	            } 
	        $("#timeStart").val(startTimeInitial.substr(startTimeInitial.length-8));
	        $("#timeEnd").val(endTime.substr(endTime.length-8));
	        var options = {
	        //var chart1 = new Highcharts.Chart({
	        	chart: {
	        		renderTo: "container",
	        		zoomType:"x",
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false,
	                },
	                title: {
	                	text: '曲线图'
	                	},
	                xAxis: {
	                	categories: xtext
	                	},
	                yAxis: {                            //设置Y轴
	                	title: { 
	                	text: 'Values' 
	                	}
	                },
	                credits:{                          //右下角文本不显示
	                	enabled: false
	                	},
	                tooltip: {
	                	pointFormat: '{series.name}: <b>{point.y}</b>'
	                	},
	                plotOptions: {
	                	pie: {
	                		allowPointSelect: true,
	                		cursor: 'pointer',
	                		dataLabels: {
	                				enabled: true,
	                				format: '<b>{point.name}</b>: {point.percentage:.1f} %',
	                				style: {
	                					color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                					}
	                				},
	                				//showInLegend: true
	                		}
	                	},
	                series: [{}]
	                //});
	        }
	        options.series = new Array();
	        //alert(map);
	        var i=0;
	        for(key in map){
	            //alert(key + map[key]); 
	            options.series[i] = new Object();
	            options.series[i].type = 'line';
	            options.series[i].name = key;
	            options.series[i].data = map[key];
	            i++;
	        }
	        //chart1.series[0].setData(map["温度"]);
	        //chart1.series[1].setData(map["湿度"]);
	        //alert((chart1.series[0].data));
	        chart1 = new Highcharts.Chart(options);
	        },
	        error: function(){
	        	alert("数据获取失败！")
	        	}
	        }); 
	allPointCreate();
    })

//获取最后一圈全部数据并存入totalMap中   
var totalMap={};
function allPointCreate(){	
	 var data1 = {
			    "startTime" : startTimeInitial,
			    "endTime":endTime
			  };
			$.ajax({
				url: "/api/v1/test/selectTestMsgByPeriod",
			    contentType : 'application/json;charset=utf8',
			    dataType: 'json',
			    cache: false,
			    type: 'post',
			    data:JSON.stringify(data1),
			    async : false,
			    processData : false,
			    success: function(res) {
			    	//alert(JSON.stringify(res));
			        var data2=res.data.testModelList;
			        var map={}
			        var map4={}
			        for (var i in data2){
			        	//alert(JSON.stringify(data2[i]));
			            var data3 = data2[i];
			            //alert(JSON.stringify(data3.parameter));
			            if(!map.hasOwnProperty(data3.parameter)){
			            	map[data3.parameter] = Array();
			            	map4[data3.parameter] = Array();
			            	if(totalMap[data3.parameter] === undefined || totalMap[data3.parameter].length == 0){			            	
			            		totalMap[data3.parameter] = Array();
			            	}
			            }
			            var arr = map[data3.parameter];
			            arr4 = map4[data3.parameter];
			            arr.push(data3);
			            arr[arr.length-1].x = data3.time;
			            //date = new Date( data3.time);			           			           
		 	        	arr[arr.length-1].y = Number(data3.value)   //给Y轴赋值
		 	        	var a ={};
		 	        	a.x =new Date(arr[arr.length-1].x).getTime();
		 	        	a.y = arr[arr.length - 1].y;
		 	        	arr4.push(a);
		 	        	map4[data3.parameter] = arr4;
		 	        	//j=Number(map4[data3.parameter].length)-1;
		 	        	//dynamicPointMap[data3.parameter].push(map4[data3.parameter][j]);
			            }
			        totalMap =map4;
			        },
			        error: function(){
			        	alert("数据获取失败！")
			        	}
			        });  
}    
    
    
    
    

//此处往下为动态获取非实时数据，一秒一次，一次一个数据

//动态获取非实时曲线点
var date;
var dynamicPointMap ={};
function dynamicPointCreate(){	
	 var data1 = {
			    "startTime" : startTime,
			    "endTime":endTime
			  };
			$.ajax({
				url: "/api/v1/test/selectTestMsgByPeriod",
			    contentType : 'application/json;charset=utf8',
			    dataType: 'json',
			    cache: false,
			    type: 'post',
			    data:JSON.stringify(data1),
			    async : false,
			    processData : false,
			    success: function(res) {
			    	//alert(JSON.stringify(res));
			        var data2=res.data.testModelList;
			        var map={}
			        var map4={}
			        for (var i in data2){
			        	//alert(JSON.stringify(data2[i]));
			            var data3 = data2[i];
			            //alert(JSON.stringify(data3.parameter));
			            if(!map.hasOwnProperty(data3.parameter)){
			            	map[data3.parameter] = Array();
			            	map4[data3.parameter] = Array();
			            	if(dynamicPointMap[data3.parameter] === undefined || dynamicPointMap[data3.parameter].length == 0){			            	
			            	dynamicPointMap[data3.parameter] = Array();
			            	}
			            }
			            var arr = map[data3.parameter];
			            arr4 = map4[data3.parameter];
			            arr.push(data3);
			            arr[arr.length-1].x = data3.time;
			            //date = new Date( data3.time);			           			           
		 	        	arr[arr.length-1].y = Number(data3.value)   //给Y轴赋值
		 	        	var a ={};
		 	        	a.x =new Date(arr[arr.length-1].x).getTime();
		 	        	a.y = arr[arr.length - 1].y;
		 	        	arr4.push(a);
		 	        	map4[data3.parameter] = arr4;
		 	        	//j=Number(map4[data3.parameter].length)-1;
		 	        	//dynamicPointMap[data3.parameter].push(map4[data3.parameter][j]);
			            }
			        var i=0;
			        interval =setInterval(function(){
			        if(i<=map4[key].length-1){
			        	for(key in map4){
				            //alert(key + map[key]); 
				        	var group = dynamicPointMap[key];
				        		group.push(map4[key][i]);
					        	dynamicPointMap[key] = group;
					        	date=new Date(map4[key][i].x);
				        }
			        		generateChart1();
			        		
			        		startTime = generateTime(false,date);
			        		$("#timeStart").val(startTime.substr(startTime.length-8));
			                //startTime = fastIncreateTime(false,date);
			                i = i+1;
			        		if(startTime == endTime){
			        			clearInterval(interval);	
			        			$play_span.attr("class","glyphicon glyphicon-play");
			        			}	
			        		var d =unitLength();
			        		if(startTime>startTimeInitial){
			        			console.log(d);
			        			progressBtn(d);
			        		}
		        	}
			        }, 1000);
			        },
			        error: function(){
			        	alert("数据获取失败！")
			        	}
			        });  
}

//动态曲线显示
var chart1;
//创建定时调用
var interval;    //设置定时器
var $play_span;
//点击播放后
var dynamicPointMap = {};
$.extend( true , dynamicPointMap,totalMap);
function play(){
	$play_span = $("#play span");
	//var date = new Date(startTime);
	//startTime = fastIncreateTime(false,date);  //时间+1秒
	if(startTime>=endTime){
		alert("数据已全部播放完");
	}else{	
		if($play_span.attr("class") == "glyphicon glyphicon-play"){
			$play_span.attr("class","glyphicon glyphicon-pause");
			initialProgressLength();
			addPointToDynamicMap();
		}else{
			$play_span.attr("class","glyphicon glyphicon-play");
			myStopFunction();
		}
	}
	
};

function myStopFunction() {
    clearInterval(interval);
}

function addPointToDynamicMap(){
	var slideTime = progressTime();     //取进度条时间
	var n1 = new Date(startTimeInitial).getTime();
	var n2 = new Date(slideTime).getTime();
	var n =(n2 - n1) / 1000 +1 ;
	for(key in totalMap){
		dynamicPointMap[key] = totalMap[key].slice(0,n);
	  }
	var i=n;
    interval =setInterval(function(){
    if(i<=totalMap[key].length-1){
    	for(key in totalMap){
            //alert(key + map[key]); 
        	var group = dynamicPointMap[key];
        		group.push(totalMap[key][i]);
	        	dynamicPointMap[key] = group;
	        	date=new Date(totalMap[key][i].x);
        }
    		generateChart1();
    		
    		startTime = generateTime(false,date);
    		$("#timeStart").val(startTime.substr(startTime.length-8));
            i = i+1;
    		if(startTime == endTime){
    			clearInterval(interval);	
    			$play_span.attr("class","glyphicon glyphicon-play");
    			}	
    		var d =unitLength();
    		if(startTime>startTimeInitial){
    			console.log(d);
    			progressBtn(d);
    		}
	}
    }, 1000);
}

	function activeLastPointToolip(chart1) {
		var points1 = chart1.series[0].points;
		chart1.tooltip.refresh(points1[points1.length -1]);
}


function generateChart1(){
	chart1 = Highcharts.chart('container', {
		chart: {
			type: 'spline',
			marginRight: 10,
			events: {
				load: function () {	
					series =this.series
					var i=0;
					chart = this;
					for(key in dynamicPointMap){
							var xi =dynamicPointMap[key][dynamicPointMap[key].length-1].x,
							yi =dynamicPointMap[key][dynamicPointMap[key].length-1].y;
							series[i].addPoint([xi, yi], true, true);
							i=i+1;
					}
					//activeLastPointToolip(this);
				}
			}
		},
		title: {
			text: '动态模拟实时数据'
		},
		xAxis: {
			type: 'datetime',
			tickPixelInterval: 150
		},
		yAxis: {
			title: {
				text: null
			}
		},
		tooltip: {
			formatter: function () {
				return '<b>' + this.series.name + '</b><br/>' +
					Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
					Highcharts.numberFormat(this.y, 2);
			}
		},
		legend: {
			//enabled: false,
			layout: 'vertical',
          align: 'right',
          verticalAlign: 'top',
          borderWidth: 0
		},
		series: createDynamicPointSeries(),
	});
}

function createDynamicPointSeries() {	
	var series = new Array();
  var i=0;
  var mapSize;
  var map_copy = {};
	$.extend( true , map_copy,dynamicPointMap);
  for(key in map_copy){
      //alert(key + map[key]); 
      series[i] = new Object();
      series[i].name = key;
      series[i].data = map_copy[key].slice(0,map_copy[key].length-1);
      mapSize=map_copy[key].length-1;
      i++;
  }
  if(mapSize<20){
	  for(j in series){
		  	series[j].data.unshift({x:map_copy[key][0].x,y:map_copy[key][0].y})
		  	}
	  var xx =Number(map_copy[key][map_copy[key].length-1].x);
	  for(var k=1;k<20-mapSize;k++){
		  xx = xx+Number(1000);
		  for(j in series){
			  	series[j].data.push({x:xx,y:[map_copy[key].length-1].y})
			  	}
	  }
  }else{
  	for(j in series){
  	series[j].data.splice(0,mapSize-20)
  	}    	
  }    
  return series;	
}




//快进

function forward(){
	myStopFunction();
	$play_span = $("#play span");
	//取此刻进度条上的时间，加1秒，然后判断进行取数据，此刻的开始时间显示图标跟随点击的次数进行变化
	var clickTimeSlide = progressTime();     //取进度条时间
	var date = new Date(clickTimeSlide);
	startTime = fastIncreateTime(false,date);  //时间+1秒
	if(startTime<=endTime){
		$("#timeStart").val(startTime.substr(startTime.length-8));
		var d = unitLength();
		console.log(d);
		progressBtn(d);
		if($play_span.attr("class") == "glyphicon glyphicon-pause"){
			addPointToDynamicMap();
		}
	}else{
		alert("数据已播放完")
	}
}



//快退

function backward(){
	myStopFunction();
	$play_span = $("#play span");
	var clickTimeSlide = progressTime();   //取进度条时间
	var date = new Date(clickTimeSlide);
	//首先判断是否在时间轴内
	if(startTime>startTimeInitial){
		startTime = fastDecreaseTime(false,date);   //时间减1秒进行取数据显示
		$("#timeStart").val(startTime.substr(startTime.length-8));
		var d = 0 - unitLength();
		console.log(d);
		progressBtn(d);
		if($play_span.attr("class") == "glyphicon glyphicon-pause"){
			addPointToDynamicMap();
		}
	}else{
		alert("数据已从头开始播放，请点击播放按钮")
	}
}


//点击滑动条之后，从此刻开始计时取数据
function progress(){	
	myStopFunction();
	$play_span = $("#play span");
	var clickTimeSlide = progressTime();
	$("#timeStart").val(clickTimeSlide.substr(clickTimeSlide.length-8));
	startTime = clickTimeSlide;
	if($play_span.attr("class") == "glyphicon glyphicon-pause"){
		addPointToDynamicMap();
	}
}

//加一秒1秒   快进一次加1秒
function fastIncreateTime(flag,t) {
  var t_s = t.getTime();//转化为时间戳毫秒数
  t.setTime(t_s + 1000 * 1);
  var time=generateTime(flag,t)
  return time     
}

//快退一次1秒
function fastDecreaseTime(flag,t){
	var t_s = t.getTime();//转化为时间戳毫秒数
    t.setTime(t_s - 1000 * 1);
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




 // 进度条
 
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
	var date1 = new Date(startTimeInitial); 
	var date2 = new Date(endTime);
	var s1 = date1.getTime();
	var s2 = date2.getTime();
	var timeSum = (s2-s1) / 1000
	var unitLength = 800 / timeSum;
	return unitLength;
}


//进度条定位对应时间点
function progressTime(date1, date2){
	var date1 = new Date(startTimeInitial); 
	var date2 = new Date(endTime);
	var s1 = date1.getTime();
	var s2 = date2.getTime();
	var timeSum = (s2-s1) / 1000;
	var value = $('.progress_btn').css("left").replace('px','') / 800;
	console.log(value);
	var timeGap = timeSum * value +0.001;   //加0.01是为了补偿因为精度问题而丢失的小数
	console.log(timeGap);
	date1.setTime(s1 + timeGap * 1000);
	var newTime = generateTime(false,date1);
	return newTime;
}



//初始进度条的位置
function initialProgressLength(date1, date2){
	var date1 = new Date(startTimeInitial); 
	var date2 = new Date(endTime);
	var s1 = date1.getTime();
	var s2 = date2.getTime();
	var timeSum = (s2-s1) / 1000;
	var s3 = new Date(startTime).getTime();
	var timeInitialSum =(s3-s1) / 1000;
	var initialProgressLength = 800 / timeSum * timeInitialSum;
	$('.progress_btn').css('left', initialProgressLength);
    $('.progress_bar').animate({width:initialProgressLength},800);
    $('.text').html(parseInt((initialProgressLength/800)*100) + '%');
}




//此处往下为实时曲线的绘制  
//实时曲线显示
var realMsg=[];
var chart;
var realTimeMap={};
//点击实时播放，调用函数利用kafka推送数据，一秒推送一个
function realTimeCurve(){
	   connect(undefined,function(msg){
		console.log(msg);
		realMsg = JSON.parse(msg);
		realTimePointCreate();
		//createSeries();
		generateChart();
		//activeLastPointToolip(chart);
	},"topic");   
}

//实时曲线数据点的生成和处理
function realTimePointCreate(){
var map={};
var map3 ={};
for (var i in realMsg){
	//alert(JSON.stringify(data2[i]));
    var data3 = realMsg[i];
    //alert(JSON.stringify(data3.parameter));
    if(!map.hasOwnProperty(data3.parameter)){
    	map[data3.parameter] = Array();
    	//map2[data3.parameter] = Array();
    	map3[data3.parameter] = Array();
    	if(realTimeMap[data3.parameter] === undefined || realTimeMap[data3.parameter].length == 0){			            	
    		realTimeMap[data3.parameter] = Array();
        	}
    	}
    var arr = map[data3.parameter];
    //var arr2 =map2[data3.parameter];
    var arr3 =map3[data3.parameter];
    arr.push(data3);
	arr[arr.length-1].x = data3.time;
	arr[arr.length-1].y = Number(data3.value)   //给Y轴赋值
	var a ={};
	//a.x =new Date(xtext[xtext.length-1]).getTime();
	a.x =new Date(arr[arr.length-1].x).getTime();
	a.y = arr[arr.length - 1].y;
    //arr2.push(a);
	arr3.push(a);
    //map2[data3.parameter] = arr2;
	map3[data3.parameter] = arr3;
    }
    //map2["温度"].push(map3["温度"][0]);
    for(key in map3){
        //alert(key + map[key]); 
    	//map2[key].push(map3[key][0]);
    	var group = realTimeMap[key];
    	group.push(map3[key][0]);
    	realTimeMap[key] = group;
    }
    
};  

Highcharts.setOptions({
	global: {
		useUTC: false
	}
});

	function activeLastPointToolip(chart) {
		var points = chart.series[0].points;
		chart.tooltip.refresh(points[points.length -1]);
}

function generateChart(){
	chart = Highcharts.stockChart('container', {
		chart: {
			type: 'spline',
			marginRight: 10,
			events: {
				load: function () {
					series =this.series
					var i=0;
					chart = this;
					for(key in realTimeMap){       
						var xi =realTimeMap[key][realTimeMap[key].length-1].x,
							yi =realTimeMap[key][realTimeMap[key].length-1].y;
							series[i].addPoint([xi, yi], true, true);
							i=i+1;
					}
					//activeLastPointToolip(this);
				}
			}
		},
		rangeSelector: {
			buttons: [{
					count: 1,
					type: 'minute',
					text: '1M'
			}, {
					count: 5,
					type: 'minute',
					text: '5M'
			}, {
					type: 'all',
					text: 'All'
			}],
			inputEnabled: false,
			selected: 0
	},
		title: {
			text: '动态模拟实时数据'
		},
		xAxis: {
			type: 'datetime',
			tickPixelInterval: 150
		},
		yAxis: {
			title: {
				text: null
			}
		},
		tooltip: {
			/*formatter: function () {
				return '<b>' + this.series.name + '</b><br/>' +
					Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
					Highcharts.numberFormat(this.y, 2);
			}*/
			split: false
		},
		legend: {
			//enabled: false,
			layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            borderWidth: 0
		},
		series: createSeries(),
	});
}

function createSeries() {	
	var series = new Array();
    var i=0;
    var mapSize;
    var map_copy = {};
	$.extend( true , map_copy,realTimeMap);
    for(key in map_copy){
        //alert(key + map[key]); 
        series[i] = new Object();
        series[i].name = key;
        series[i].data = map_copy[key];
        mapSize=map_copy[key].length;
        i++;
    }    
    if(mapSize<1000){
  	  for(j in series){
  		  	series[j].data.unshift({x:map_copy[key][0].x,y:map_copy[key][0].y})
  		  	}
  	  /*var xx =Number(map_copy[key][map_copy[key].length-1].x);
  	  for(var k=1;k<20-mapSize;k++){
  		  xx = xx+Number(1000);
  		  for(j in series){
  			  	series[j].data.push({x:xx,y:[map_copy[key].length-1].y})
  			  	}
  	  }*/
    }else{
    	for(j in series){
    	series[j].data.splice(0,mapSize-1000)
    	}
    	
    }   
    return series;	
}




/*$(function(){
	Highcharts.setOptions({
		global : {
				useUTC : false
		}
	});
	//Create the chart
	Highcharts.stockChart('container1', {
		chart : {
				events : {
						load : function () {
								// set up the updating of the chart each second
								var series = this.series[0];
								setInterval(function () {
										var x = (new Date()).getTime(), // current time
												y = Math.round(Math.random() * 100);
										series.addPoint([x, y], true, true);
								}, 1000);
						}
				}
		},
		rangeSelector: {
				buttons: [{
						count: 1,
						type: 'minute',
						text: '1M'
				}, {
						count: 5,
						type: 'minute',
						text: '5M'
				}, {
						type: 'all',
						text: 'All'
				}],
				inputEnabled: false,
				selected: 0
		},
		title : {
				text : 'Live random data'
		},
		tooltip: {
				split: false
		},
		exporting: {
				enabled: false
		},
		series : [{
				name : '随机数据',
				data : (function () {
						// generate an array of random data
						var data = [], time = (new Date()).getTime(), i;
						for (i = -999; i <= 0; i += 1) {
								data.push([
										time + i * 1000,
										Math.round(Math.random() * 100)
								]);
						}
						return data;
				}())
		}]
	});
})*/
