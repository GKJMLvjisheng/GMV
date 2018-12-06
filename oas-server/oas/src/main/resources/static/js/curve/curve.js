
/* 
 * 以下是曲线func，包括三个函数，一个用于回显，一个用于定时后台取数据动态显示曲线，一个显示实时曲线
 */
//通过Ajax获取静态图表数据
var startTime='';
var chart1;
var map2 ={};
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
	        var data2=res.data.rows;
	        var map={}
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
	     	        		//startTime=xtext[xtext.length-1];
	     	        		startTime=xtext[0];
	     	        		}	     	  
	     	        	var a ={};
	    	        	a.x =new Date(xtext[xtext.length-1]).getTime();
	    				a.y = arr[arr.length - 1].y;
	    	 	       //arr2.push(a);
	    	 	       //map2[data3.minerName] = arr2;
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
    
    
//实时曲线显示
var realMsg=[];
var chart;
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
    	map2[key].push(map3[key][0]);
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
	chart = Highcharts.chart('container2', {
		chart: {
			type: 'spline',
			marginRight: 10,
			events: {
				load: function () {
					/*var i=1;
					for(key in map2){
						var seriesi =this.series[i-1],
						i=i+1;
					}*/		
					series =this.series
					var i=0;
					chart = this;
					for(key in map2){
						var xi =map2[key][map2[key].length-1].x,
							yi =map2[key][map2[key].length-1].y;
							series[i].addPoint([xi, yi], true, true);
							i=i+1;
					}
					activeLastPointToolip(this);
					
					/*var series1 = this.series[0],
					    series2 = this.series[1],
					    series3 = this.series[2],
					    series4 = this.series[3],
						chart = this;
					var x1 =map2["温度"][map2["温度"].length-1].x,
					    y1 =map2["温度"][map2["温度"].length-1].y;
						series1.addPoint([x1, y1], true, true);
					var x2 =map2["湿度"][map2["湿度"].length-1].x,
					    y2 =map2["湿度"][map2["湿度"].length-1].y;
						series2.addPoint([x2, y2], true, true);
					var x3 =map2["电流"][map2["电流"].length-1].x,
					    y3 =map2["电流"][map2["电流"].length-1].y;
						series3.addPoint([x3, y3], true, true);
					var x4 =map2["电压"][map2["电压"].length-1].x,
					    y4 =map2["电压"][map2["电压"].length-1].y;
						series4.addPoint([x4, y4], true, true);
						activeLastPointToolip(this);*/		
					//activeLastPointToolip(chart);
					//setInterval(function () {
					//	var x = (new Date()).getTime(), // 当前时间
						//var x = (new Date("2017-12-04 15:58:17")).getTime(),
						//var x = Date.UTC(2018,12,4,15,47,0,01);
						//x=x1;
					//		y = Math.random();          // 随机值
					//		y = y1;
					//	series.addPoint([x, y], true, true);
						//series2.addPoint([x, y+1], true, true);
					//	activeLastPointToolip(chart);
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
			layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            borderWidth: 0
		},
		series: createSeries(),
		/*series: [{
			name: '温度',
			//data:map2["温度"]
			//data:map2["温度"].slice(map2["温度"].length-19-1,map2["温度"].length)
			data:(function () {
				if(map2["温度"].length<=20){
					var data=map2["温度"]
				}else{
					var data=map2["温度"].slice(map2["温度"].length-19-1,map2["温度"].length)
				}
				return data;
		}())
		},
		{
			name: '湿度',
			//data:map2["温度"]
			//data:map2["温度"].slice(map2["温度"].length-19-1,map2["温度"].length)
			data:(function () {
				if(map2["湿度"].length<=20){
					var data=map2["湿度"]
				}else{
					var data=map2["湿度"].slice(map2["湿度"].length-19-1,map2["湿度"].length)
				}
				return data;
		}())
		},
		{
			name: '电流',
			//data:map2["温度"]
			//data:map2["温度"].slice(map2["温度"].length-19-1,map2["温度"].length)
			data:(function () {
				if(map2["电流"].length<=20){
					var data=map2["电流"]
				}else{
					var data=map2["电流"].slice(map2["电流"].length-19-1,map2["电流"].length)
				}
				return data;
		}())
		},
		{
			name: '电压',
			//data:map2["温度"]
			//data:map2["温度"].slice(map2["温度"].length-19-1,map2["温度"].length)
			data:(function () {
				if(map2["电压"].length<=20){
					var data=map2["电压"]
				}else{
					var data=map2["电压"].slice(map2["电压"].length-19-1,map2["电压"].length)
				}
				return data;
		}())
		}
		]*/
	});
}

function createSeries() {	
	var series = new Array();
    var i=0;
    var mapSize;
    for(key in map2){
        //alert(key + map[key]); 
        series[i] = new Object();
        series[i].name = key;
        series[i].data = map2[key];
        mapSize=map2[key].length;
        i++;
    }
    if(mapSize<=20){
    	series =series;
    }else{
    	for(j in series){
    	series[j].data.slice(mapSize-20,mapSize)
    	}   	
    }    
    return series;
	
}

//});   
//}	

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
