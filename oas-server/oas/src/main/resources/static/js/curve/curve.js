
/* 
 * 以下是曲线func，包括三个函数，一个用于回显，一个用于定时后台取数据动态显示曲线，一个显示实时曲线
 */
//通过Ajax获取静态图表数据
var startTime='';
//var msg={};
var chart1;
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
	        var data2=res.data.rows;
	        //msg = data2;
	        var map={}
	        for (var i in data2){
	        	//alert(JSON.stringify(data2[i]));
	            var data3 = data2[i];
	            //alert(JSON.stringify(data3.minerName));
	            if(!map.hasOwnProperty(data3.minerName)){
	            	map[data3.minerName] = Array();
	            	}
	            var arr = map[data3.minerName];
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
	     	        		startTime=xtext[xtext.length-1];
	     	        		}
	     	        	}
	            } 
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
	                /* series: [{
	                	type: 'line',
	                	name: '温度值',
	                	//data:[1,2,3,4,5]
	                },{
	                	type: 'line',
	                	name: '湿度值',
	                	//data:[1,2,3,4,5]
	                },
	                ] */
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
    
    
//动态显示曲线
//var y1;

function dynamicCurve(){
	  var data1 = {
	    "startTime" : startTime
	  };
    var x = [];//X轴
    var y = [];//Y轴
    var xtext = [];//X轴TEXT
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
	    	alert(JSON.stringify(res));
	        var data2=res.data.rows;
	        var map={}
	        for (var i in data2){
	        	//alert(JSON.stringify(data2[i]));
	            var data3 = data2[i];
	            //alert(JSON.stringify(data3.minerName));
	            if(!map.hasOwnProperty(data3.minerName)){
	            	map[data3.minerName] = Array();
	            	}
	            var arr = map[data3.minerName];
	            arr.push(data3);
	            if(arr.length == 0){
	            	arr[0].y = Number(data3.minerDescription)
	     	        xtext.push(data3.updated);//给X轴TEXT赋值
	     	        //y1=12;
	     	        }else{
	     	        	arr[arr.length-1].y = Number(data3.minerDescription)   //给Y轴赋值
	     	        	if(xtext[xtext.length-1]!=data3.updated){
	     	        		xtext.push(data3.updated);//给X轴TEXT赋值
	     	        		startTime =data3.updated;
	     	        		//y1=12;
	     	        		}
	     	        	}
	            } 
	        var chart1 = new Highcharts.Chart({
	        	chart: {
	        		renderTo: "container1",
	                plotBackgroundColor: null,
	                plotBorderWidth: null,
	                plotShadow: false,
	                },
	                title: {
	                	text: '动态曲线图'
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
	                series: [{
	                	type: 'line',
	                	name: '温度值',
	                	//data:[1,2,3,4,5]
	                       },{
	                	type: 'line',
	                	name: '湿度值',
	                	//data:[1,2,3,4,5]
	                	   },
	                		]
	                });
	        //alert(map);
	        chart1.series[0].setData(map["温度"]);
	        chart1.series[1].setData(map["湿度"]);
	        // alert((chart1.series[0].data));
	        },
	        error: function(){
	        	alert("数据获取失败！")
	        	}
	        }); 
}



//实时曲线显示
var realTimeList = [];
var xtext =[];
var realLength ;
var realMsg=[];
var x1;
var x2;
var y1; 
var points1 ={};
function realTimeCurve(){
$(function(){
	   connect(undefined,function(msg){
		console.log(msg);
		realMsg = JSON.parse(msg);
		realTimeLineCreate();
		createRealTimeLine();
	},"topic");   
 	 /* realMsg =msg;
	realTimeLineCreate();   */
});
}

var map2={};
function realTimeLineCreate(){
var map={};
for (var i in realMsg){
	//alert(JSON.stringify(data2[i]));
    var data3 = realMsg[i];
    //alert(JSON.stringify(data3.minerName));
    if(!map.hasOwnProperty(data3.minerName)){
    	map[data3.minerName] = Array();
    	map2[data3.minerName] = Array();
    	}
    var arr = map[data3.minerName];
    var arr2 =map2[data3.minerName];
    arr.push(data3);
    if(arr.length == 0){
    	arr[0].x =data3.updated;
    	arr[0].y = Number(data3.minerDescription)
	        //xtext.push(data3.updated);//给X轴TEXT赋值
	        xtext.push(data3.updated);
	       var a ={};
	       a.x =arr[0].x;
	       a.y =arr[0].y;
	       arr2.push(a);
	       //realTimeList.push(a);
	        }else{
	        	arr[arr.length-1].x = data3.updated;
	        	arr[arr.length-1].y = Number(data3.minerDescription)   //给Y轴赋值
	        	 if(xtext[xtext.length-1]!=data3.updated){
	        		xtext.push(data3.updated);//给X轴TEXT赋值
	        		//realTimeList.push[xtext[xtext.length-1], arr[arr.length-1].y];
	        		}
	        	var a ={};
	        	//a.x =xtext[xtext.length-1];
	        	a.x = data3.updated;
	 	        //a.y =arr[arr.length-1].y;
	 	        //a.x = new Date(data3.updated).getTime();
				a.y = arr[arr.length - 1].y;
	 	       arr2.push(a);
	 	      map2[data3.minerName] = arr2;
	 	      //x=new Date(data3.updated).getTime();
	 	      //y =12;
	 	      //x2=map["温度"][0].x;
	 	      y1=map["温度"][0].y;
	        	//realTimeList.push(a);
	        	}
 
    }
};  

function createRealTimeLine(){
  $(function(){
Highcharts.setOptions({
	global: {
		useUTC: false
	}
});
//var points1 ={};
function activeLastPointToolip(chart) {
	var points = chart.series[0].points;
	//var points = points1;
	chart.tooltip.refresh(points[points.length -1]);
	//points1= chart.series[0].points;
}
var chart = Highcharts.chart('container2', {
	chart: {
		type: 'spline',
		marginRight: 10,
		events: {
			load: function () {
				var series = this.series[0],
					chart = this;
				activeLastPointToolip(chart);
				//setInterval(function () {
					var x = (new Date()).getTime(), // 当前时间
						//y = Math.random();          // 随机值
						y = y1;
					series.addPoint([x, y], true, true);
					activeLastPointToolip(chart);
				//}, 1000);
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
		enabled: false
	},
	series: [{
		name: '随机数据',
		data: (function () {
			// 生成随机值
			//var data = [1,1];
			 var data = [],
				time = (new Date()).getTime(),
				i;
			for (i = -19; i <= 0; i += 1) {
				data.push({
					x: time + i * 1000,
					//y: Math.random()
					y:1
				});
			} 
			return data;
		}())
	}]
});
});   
}
