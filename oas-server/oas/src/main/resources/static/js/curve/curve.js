
/* 
 * 以下是曲线func，包括三个函数，一个用于回显，一个用于定时后台取数据动态显示曲线，一个显示实时曲线
 */
//通过Ajax获取静态图表数据
var startTime='';
var endTime ="";
var chart1;
var arr2 ={};
$(function(){
	var data1={
			"number":20
	};
    var x = [];//X轴
    var y = [];//Y轴
    var xtext = [];//X轴TEXT
	$.ajax({
		url: "/api/v1/load/selectLoadMsg",
	    contentType : 'application/json;charset=utf8',
	    dataType: 'json',
	    cache: false,
	    type: 'post',
	    data:JSON.stringify(data1),
	    async : false,
	    success: function(res) {
	    	alert(JSON.stringify(res));
	    	endTime = res.data.endTime;
	        var data2=res.data.rows;
	        var map={};
	        var map2 ={};
	        for (var i in data2){
	        	//alert(JSON.stringify(data2[i]));
	            var data3 = data2[i];
	            //alert(JSON.stringify(data3.minerName));
	            if(!map.hasOwnProperty(data3.minerName)){
	            	map[data3.minerName] = Array();
	            	map2[data3.minerName] = Array();
	            	}
	            var arr = map[data3.minerName];
	            //var arr2 =map2[data3.minerName]
	            arr2 =map2[data3.minerName];
	            arr.push(data3);
	            if(arr.length == 0){
	            	//arr[0].x =data3.updated;
	            	arr[0].y = Number(data3.minerDescription)
	     	        xtext.push(data3.updated);//给X轴TEXT赋值
	     	        }else{
	     	        	//arr[arr.length-1].x =data3.updated;
	     	        	arr[arr.length-1].y = Number(data3.minerDescription)   //给Y轴赋值
	     	        	if(xtext[xtext.length-1]!=data3.updated){
	     	        		xtext.push(data3.updated);//给X轴TEXT赋值
	     	        		startTime=xtext[0];
	     	        		//startTime=xtext[xtext.length-1];
	     	        		}	     	  
	     	        	var a ={};
	    	        	a.x =new Date(xtext[xtext.length-1]).getTime();
	    				a.y = arr[arr.length - 1].y;
	    	 	       //arr2.push(a);
	    	 	       //map2[data3.minerName] = arr2;
	     	        	}
	            } 
	        $("#timeStart").val(startTime.substr(startTime.length-8));
	        $("#timeEnd").val(endTime.substr(startTime.length-8));
	        var options = {
	        //var chart1 = new Highcharts.Chart({
	        	chart: {
	        		renderTo: "container",
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
    })
    
    
//实时曲线显示
var realMsg=[];
var chart;
var map2={};
function realTimeCurve(){
	   connect(undefined,function(msg){
		console.log(msg);
		realMsg = JSON.parse(msg);
		realTimePointCreate();
		createSeries();
		generateChart();
		//activeLastPointToolip(chart);
	},"topic");   
}

function realTimePointCreate(){
var map={};
var map3 ={};
for (var i in realMsg){
	//alert(JSON.stringify(data2[i]));
    var data3 = realMsg[i];
    //alert(JSON.stringify(data3.minerName));
    if(!map.hasOwnProperty(data3.minerName)){
    	map[data3.minerName] = Array();
    	//map2[data3.minerName] = Array();
    	map3[data3.minerName] = Array();
    	if(map2[data3.minerName] === undefined || map2[data3.minerName].length == 0){			            	
    		map2[data3.minerName] = Array();
        	}
    	}
    var arr = map[data3.minerName];
    //var arr2 =map2[data3.minerName];
    var arr3 =map3[data3.minerName];
    arr.push(data3);
	arr[arr.length-1].x = data3.updated;
	arr[arr.length-1].y = Number(data3.minerDescription)   //给Y轴赋值
	var a ={};
	//a.x =new Date(xtext[xtext.length-1]).getTime();
	a.x =new Date(arr[arr.length-1].x).getTime();
	a.y = arr[arr.length - 1].y;
    //arr2.push(a);
	arr3.push(a);
    //map2[data3.minerName] = arr2;
	map3[data3.minerName] = arr3;
    }
    //map2["温度"].push(map3["温度"][0]);
    for(key in map3){
        //alert(key + map[key]); 
    	//map2[key].push(map3[key][0]);
    	var group = map2[key];
    	group.push(map3[key][0]);
    	map2[key] = group;
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
	chart = Highcharts.chart('container', {
		chart: {
			type: 'spline',
			marginRight: 10,
			events: {
				load: function () {
					series =this.series
					var i=0;
					chart = this;
					for(key in map2){       
						var xi =map2[key][map2[key].length-1].x,
							yi =map2[key][map2[key].length-1].y;
							series[i].addPoint([xi, yi], true, true);
							i=i+1;
					}
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
			enabled: false,
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
	$.extend( true , map_copy,map2);
    for(key in map_copy){
        //alert(key + map[key]); 
        series[i] = new Object();
        series[i].name = key;
        series[i].data = map_copy[key];
        mapSize=map_copy[key].length;
        i++;
    }
    
    if(mapSize<=20){
    	series =series;
    }else{
    	for(j in series){
    	series[j].data.splice(0,mapSize-20)
    	}
    	
    }   
    return series;	
}

//此处往下为动态获取非实时数据，一秒一次，一次一个数据

//动态获取非实时曲线点
var date;
var dynamicPointMap ={};
function dynamicPointCreate(){	
	 var data1 = {
			    "startTime" : startTime,
			    "endTime":startTime
			  };
			$.ajax({
				url: "/api/v1/load/selectLoadMsgByPeriod",
			    contentType : 'application/json;charset=utf8',
			    dataType: 'json',
			    cache: false,
			    type: 'post',
			    data:JSON.stringify(data1),
			    async : false,
			    processData : false,
			    success: function(res) {
			    	//alert(JSON.stringify(res));
			        var data2=res.data.rows;
			        var map={}
			        var map4={}
			        for (var i in data2){
			        	//alert(JSON.stringify(data2[i]));
			            var data3 = data2[i];
			            //alert(JSON.stringify(data3.minerName));
			            if(!map.hasOwnProperty(data3.minerName)){
			            	map[data3.minerName] = Array();
			            	map4[data3.minerName] = Array();
			            	if(dynamicPointMap[data3.minerName] === undefined || dynamicPointMap[data3.minerName].length == 0){			            	
			            	dynamicPointMap[data3.minerName] = Array();
			            	}
			            }
			            var arr = map[data3.minerName];
			            arr4 = map4[data3.minerName];
			            arr.push(data3);
			            arr[arr.length-1].x = data3.updated;
			            date = new Date( data3.updated);			           			           
		 	        	arr[arr.length-1].y = Number(data3.minerDescription)   //给Y轴赋值
		 	        	var a ={};
		 	        	a.x =new Date(arr[arr.length-1].x).getTime();
		 	        	a.y = arr[arr.length - 1].y;
		 	        	arr4.push(a);
		 	        	map4[data3.minerName] = arr4;
		 	        	//j=Number(map4[data3.minerName].length)-1;
		 	        	//dynamicPointMap[data3.minerName].push(map4[data3.minerName][j]);
			            }
			        for(key in map4){
			            //alert(key + map[key]); 
			        	var group = dynamicPointMap[key];
			        	group.push(map4[key][0]);
			        	dynamicPointMap[key] = group;
			        }
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
function play(){
	var startTimeFinal;
	var endTimeFinal;
	startTimeFinal = Number(new Date(startTime).getTime());
	endTimeFinal = Number(new Date(endTime).getTime());
	if(startTimeFinal>=endTimeFinal){
		alert("数据已全部播放完");
	}else{
		var $play_span = $("#play span");	
		if($play_span.attr("class") == "glyphicon glyphicon-play"){
			$play_span.attr("class","glyphicon glyphicon-pause");	
		interval =setInterval(function(){
		dynamicPointCreate();
		createDynamicPointSeries();
		generateChart1();
		if(startTime == endTime){
			clearInterval(interval);
			$play_span.attr("class","glyphicon glyphicon-play");
			}	
		$("#timeStart").val(startTime.substr(startTime.length-8));
        startTime = fastIncreateTime(false,date);
	}, 1000);
		}else{
			clearInterval(interval);
			$play_span.attr("class","glyphicon glyphicon-play");
		}
	}
	
};

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
					activeLastPointToolip(this);
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
      series[i].data = map_copy[key];
      mapSize=map_copy[key].length;
      i++;
  }
  if(mapSize<=10){
  	series =series;
  }else{
  	for(j in series){
  	series[j].data.splice(0,mapSize-10)
  	}    	
  }    
  return series;	
}


//加一秒1秒
function fastIncreateTime(flag,t) {
  var t_s = t.getTime();//转化为时间戳毫秒数
  t.setTime(t_s + 1000 * 1);
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