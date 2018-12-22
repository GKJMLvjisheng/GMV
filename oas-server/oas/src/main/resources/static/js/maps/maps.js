
/**
 * 图表的初始化
 */
$(function() {	
	initMap();	
	initAreaMaps();
	setTimeControl();
});
/**
 * 日期控件的设置，默认显示当天时间
 */
function setTimeControl(){
	$('#time1').datetimepicker({
        format: 'YYYY-MM-DD HH:mm:ss',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime").val();
		$('#time2').datetimepicker('minDate',startTime);
	 });
	 
	 $('#time2').datetimepicker({
        format: 'YYYY-MM-DD HH:mm:ss',
		locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime").val();
		$('#time2').datetimepicker('minDate',startTime);
 	});
	 
	document.getElementById("startTime").value=todayDate() + " 00:00:00";
	document.getElementById("endTime").value=todayDate() + " 23:59:59"; 
}
/**
 * 获取今天日期
 */
function todayDate(){
	var day = new Date();
 	day.setTime(day.getTime());
  	var s = day.getFullYear()+"-" + (day.getMonth()+1) + "-" + day.getDate();
  	return s;
}
/**
 * 时间戳转换成日期格式
 */
function timestampToTime(timestamp) {
	var timestamp1 = timestamp - 8*3600*1000;  //减去8小时
    var date = new Date(timestamp1);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear();
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1);
    var D = date.getDate() < 10 ? '0'+ date.getDate() : date.getDate();
    var h = date.getHours() < 10 ? '0'+ (date.getHours()) : (date.getHours());  //转换为北京时间
    var m = date.getMinutes() < 10 ? '0'+ date.getMinutes() : date.getMinutes();
    var s = date.getSeconds() < 10 ? '0'+ date.getSeconds() : date.getSeconds();
    
    return Y + '-'+ M + '-'+ D + ' ' + h + ':' + m + ':' + s;
}
/**
 * 同步缩放效果，即当一个图表进行了缩放效果，其他图表也进行缩放
 */
function syncExtremes(e) {
	var thisChart = this.chart;
	if (e.trigger !== 'syncExtremes') { 
		Highcharts.each(Highcharts.charts, function (chart) {
			if (chart !== thisChart) {
				if (chart.xAxis[0].setExtremes) { 
					chart.xAxis[0].setExtremes(e.min, e.max, undefined, false, { trigger: 'syncExtremes' });
				}
			}
		});
	}
}
/**
 * 图表的显示
 */
function display(){
	document.getElementById("container2").style.display="block";
	document.getElementById("hide").style.display="block";
	document.getElementById("display").style.display="none";
}
/**
 * 图表的隐藏
 */
function hide(){
	document.getElementById("container2").style.display="none";
	document.getElementById("hide").style.display="none";
	document.getElementById("display").style.display="block";
}
var colors = ['#FF99CC', '#FFCC00', '#33FF33', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'];
var data_gantt = [
	{
		name: '项目1',
		pointWidth: 20,	
		fillOpacity: 0,
		data: [ 
			{
    			x: Date.UTC(2014, 10, 8, 07, 0, 1),
    			x2: Date.UTC(2014, 10, 9, 08, 0, 1),
    			y: 0,
    			color: colors[0],
    			borderColor: "#000000",
    			inStyle: '手动注入',
			},   		
		],
	},
	{
		name: '项目2',
		//showInLegend: false, // 不显示图例
		pointWidth: 20,
		data: [
			{
    			x: Date.UTC(2014, 10, 02, 00 ,00 ,00),
    			x2: Date.UTC(2014, 10, 03, 00 ,00 ,00),
    			y: 1,
    			color: colors[1],
    			borderColor: "#000000",
    			inStyle: '手动注入',
			},     		
		],
	},
	
	{
		name: '项目3',
		//showInLegend: false, // 不显示图例
		pointWidth: 20,   			    		
			data: [
	    		{
	    			x: Date.UTC(2014, 10, 4, 03, 0, 0),
	    			x2: Date.UTC(2014, 10, 6, 05, 0, 0),
	    			y: 2,
	    			color: colors[2],
	    			inStyle: '自动注入',
	    		}
   		],	    		    		    		   		
	},
];
var data_area = [
	{
    	name: '项目1',	    	
   		//color: '#FF99CC',
   		fillOpacity: 0.8,  //填充区域的透明度
   		data: [		    		
    			[ Date.UTC(2014, 10, 8, 07, 0, 1), 1],
    			[ Date.UTC(2014, 10, 9, 08, 0, 1), 1]		    					    		
		],
    },
    {
    	name: '项目2',
    	//color: '#FFCC00',	    	
   		data: [		    		
    			[ Date.UTC(2014, 10, 02, 00 ,00 ,00), 1],
    			[ Date.UTC(2014, 10, 03, 00 ,00 ,00), 1]		    					    		
		],
    },
    
    {
    	name: '项目3',
    	//color: '#33FF33',	    	
   		data: [		    		
    			[ Date.UTC(2014, 10, 4, 03, 0, 0), 1],		    			
    			[ Date.UTC(2014, 10, 6, 05, 0, 0), 1]	    		    		
		],
    }
];

/**
 * 甘特图初始化
 */
function initMap(){
	var average = 0;
	var move_location = 0;	
	var resize_location = 0;
	var resizeStart = 0;
	var rightStart = 0;
	var number;
	var rect_width;
	
	var chart1 = Highcharts.chart('container1', {
		chart: {
	    	type: 'xrange',
	        //zoomType: 'x',
	        plotBackgroundColor: null,
	        plotBorderWidth: 0,
	        plotShadow: false,
	        backgroundColor: 'transparent',
	        spacing: [30, 0, 10, 0],  //图表上，右，下，左的空白
	       
		},		
		title: {
			text: '实验安排与资源占用情况'
		},
	    xAxis: [{
	    	type: 'datetime',
	  		// 时间格式化字符
	  		// 默认会根据当前的刻度间隔取对应的值，若当刻度间隔为一周时，取 week 值
	  		dateTimeLabelFormats: {			
	  			millisecond: '%H:%M:%S',
	  			second: '%H:%M:%S',
	  			minute: '%H:%M',
	  			hour: '%H:%M',
	  			day: '%Y-%m-%d',
	  			week: '%Y-%m-%d',
	  			month: '%Y-%m',
	  			year: '%Y'
	  		},
	  		min: Date.UTC(2014, 10, 02, 00, 00, 00),
	  		max: Date.UTC(2014, 10, 10, 00, 00, 00),
	  		events: {
				setExtremes: syncExtremes
			},
			tickInterval: 1000, //设置横标标尺的间隔长度1s
			gridLineWidth: 1,
	    }],

	    yAxis: [{
	        title: {
	            align: 'high',
	            text: '用户实验',
	            offset: 0,
	            rotation: 0,
	            y:-10,
	            gridLineWidth: 1,
	        },
	        categories: ['子系统1','子系统2', '子系统3'], 
	        //隐藏y轴
	  		labels: {
	            enabled: true
	        }, 
	        crosshair: {
	        	width: 1,
	        },
	    }],	  
	    //去掉右下角highchart.com
	  	credits:{
	  	    enabled: false,
	  	},
	  	//显示打印按钮
		exporting:{
			enabled: true,
		},

	    plotOptions: {
	    	xrange: {
	            marker: {
	            	enabled: true,
	            	radius: 1,
	            },	            
	        },
	                    
	        series : {
	        	events : {	        		 
		        	//点击甘特图触发事件		        	 
		            /*click: function(e) {
		            	var inStyle = e.point.inStyle;
//		            	if(inStyle == "自动注入"){
//		            		alert("该实验为自动注入，不可修改");
//		            	}else if(inStyle == "手动注入"){
		            		window.open("https://www.baidu.com");
//		            	}		            	  
		            },*/
		            //点击图例触发事件
		        	legendItemClick: function(e) {		        		 
        		    	/*var index = e.target.index;
        			    var class1 = ".highcharts-series-" + index + " rect";	        			    
        				$(class1).not(".highcharts-partfill-original").last().click();	*/        					        		 			 		        		  			
	        	    },
	            },
	        },
	     },
	      
	    legend: {
	   		align: "center",//程度标的目标地位
	    },    
	      
	    tooltip: {
	    	//shared: true,
	        enabled: true,
	        xDateFormat: '%Y-%m-%d %H:%M:%S',
	        formatter: function(){
	        	//console.log(this);
	          	var res = this.series.name + "<br/>";	          	
	          	var options = this.point.options;
	          	var borderColor = this.series.userOptions.data[0].borderColor;  //边框颜色
	          	var inStyle = this.series.userOptions.data[0].inStyle;  //注入方式
	          	res += "开始时间：" + timestampToTime(options.x) + "<br/>" + "结束时间："+ timestampToTime(options.x2);
	          	
	          	average = (options.x2 - options.x)/this.point.shapeArgs.width;
	          	//边框颜色为黑色，实验固定不可调整
	          	if(borderColor=="#000000"){
	          		res += "<br/>" + "实验安排不可调整";
	          	}
	          	  
	          	if(inStyle=="自动注入"){
	          		res += "<br/>" + "实验注入类型：自动注入";
	          	}else if(inStyle=="手动注入"){
	          		res += "<br/>" + "实验注入类型：手动注入";
	          	}	          		     				
	          	return res;	          	
	        }       
	      },
	    series: data_gantt,
	});
	
	//$(".highcharts-partfill-original").css("cursor", "move");
	
	draggable(chart1);
	resizable(chart1); 
}
/**
 * 面积堆叠图初始化
 */
function initAreaMaps(){
	var colors = ['#FF99CC', '#FFCC00', '#33FF33', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'];
	var chart2 = Highcharts.chart('container2', {
		chart: {
	        type: 'area',
	        zoomType: 'x',
	        plotBackgroundColor: null,
	        plotBorderWidth: 0,
	        plotShadow: false,
	        backgroundColor: 'transparent',
	        spacing: [20, 0, 10, 0],	       
	      },
	    title: {
	  		text: '功率曲线'
	  	},
	    xAxis: [	   
	        {
	    		type: 'datetime',	    		
	  		// 时间格式化字符
	  		// 默认会根据当前的刻度间隔取对应的值，若当刻度间隔为一周时，取 week 值
	  			dateTimeLabelFormats: {			
					millisecond: '%H:%M:%S',
					second: '%H:%M:%S',
					minute: '%H:%M',
					hour: '%H:%M',
					day: '%Y-%m-%d',
					week: '%Y-%m-%d',
					month: '%Y-%m',
					year: '%Y'
				},
				min: Date.UTC(2014, 10, 2),
				max: Date.UTC(2014, 10, 10),
				events: {
					setExtremes: syncExtremes, //当轴的最大值和最小值设置的时候，被触发
				},	
				plotLines:[{
			        dashStyle:'shortDash', //标示线的样式，默认是solid（实线），这里定义为长虚线
			        //value:3,   //定义在哪个值上显示标示线，这里是在y轴上刻度为3的值处垂直化一条线
			        width:2      //标示线的宽度，2px
				}],
				tickInterval: 1000, //设置横标标尺的间隔长度
				
	       }
	    ],

	    yAxis: [	       
	        {	
	        	title: {
					align: 'high',
					text: '功率曲线',
					offset: 0,
					rotation: 0,
					y: -10,
				},
				plotLines:[{
			        dashStyle:'shortDash', //标示线的样式，默认是solid（实线），这里定义为长虚线
			        //value:3,   //定义在哪个值上显示标示线，这里是在y轴上刻度为3的值处垂直化一条线
			        width:2      //标示线的宽度，2px
				}],
				opposite: false,
				tickInterval: 1,
	        },
	    ],
		  
	    //去掉右下角highchart.com
	  	credits:{
	  	    enabled: false,
	  	},
	  	//关闭打印按钮
	  	exporting:{
	  		enabled: false,
		  },
		  
	    plotOptions: {
	    	area: {
	            stacking: 'normal',
	            marker: {
	                enabled: true,
	               // radius: 1,
	            },
	        },
	            
	        series : {
		        events : {		        		 
		        	//点击图例触发事件
			        legendItemClick: function(e) {			        		  
			        	var index = e.target.index;
		        		var class1 = ".highcharts-series-" + index + " rect";
		        		$(class1).not(".highcharts-partfill-original").first().click();		        		        			        			
		        	},
		        },
		    },
	    },
	      
	    legend: {
	   	    align: "center",//程度标的目标地位
			/*labelFormatter: function () {
				console.log(this.color);
	           	return '<span style="color:'+this.color+'"> ' + this.name +"</span>";
	       }*/
	     }, 	     
	      
	    tooltip: {
	     	//shared: true,
	        enabled: true,
	        xDateFormat: '%Y-%m-%d %H:%M:%S',
	        formatter: function(){
	        	//console.log(this);
	            var series = this.series;
	            var dataX = series.processedXData;
		        var res = series.name + "<br/>";
		        res += "开始时间：" + timestampToTime(dataX[0]) + "<br/>" + "结束时间："+ timestampToTime(dataX[dataX.length-1])+"<br/>";
		        res += "功率：" + series.processedYData[0] + "w";
		        return res;	           	
	        }       
	    },
	    colors: colors,
	    series: data_area,
	});	
}
/**
 * 甘特图横轴根据位置获取每个位置所占时间戳
 */

function getAverageValue(chart1){
	var averageTime = new Array();
	/*var startTimeStamp = chart1.series[0].points[0].options.x;
	var endTimeStamp = chart1.series[0].points[0].options.x2;
	var width = chart1.series[0].points[0].shapeArgs.width;
	var average = (endTimeStamp - startTimeStamp)/width;*/
	average = 927835.0515463918;
	averageTime.push(average);	
	return averageTime;
}
/**
 * 甘特图拖拽
 */
function draggable(chart1){
	var number;
	var left1;	
	var left2;
	var width;
	var left;
	var rect_x;
	
	$(".highcharts-partfill-original").draggable({
		axis: "h",
		cursor: "move",  //默认
		edge: 5,  //实际可拖动区域和指定的可拖动区域之间的边距,单位像素
		maxWidth: 1000,
		
		onStartDrag:function(e){
			move_location = e.clientX;  //拖动前的位置
			//确定拖动元素是哪一个
			number = $(this).parent().parent().attr("class").split(" ")[1].replace("highcharts-series-","");
			$($(".highcharts-series-2")[number]).position();
			left1 = $($(".highcharts-partfill-original")[number]).position().left; //甘特图的位置信息
			//width1 = e.data.width;  //拖动前的宽度
			left2 = $($("#dash" + number))[0].offsetLeft;	//虚线框的信息	
			width2 = $($("#dash" + number))[0].offsetWidth;
		},
		onDrag: function(e) {
			//元素拖拽
			var $drag_object = $(this);		
			var move_location2 = e.clientX;  //拖动后的位置
			width1 = e.data.width;  //拖动前的宽度
			var resize_width = e.data.width;  //拖动后的宽度
			var step = Math.abs(move_location2 - move_location);  //拖动的距离
			rect_x = $drag_object.attr('x');  //拖动后的x坐标
			
			left = $($(".highcharts-partfill-original")[number]).position().left;
			
			//var d = e.data;
			//if (d.left < 0){d.left = 0}

			if(move_location2 > move_location){
				$drag_object.attr('x',Number(rect_x) + Number(step)); 
			}else{
				$drag_object.attr('x',Number(rect_x) - Number(step));
			}
			
			//移不出左边框
			if(left <= left2){
				left = left2;
				//$(this).attr('x',Number(rect_x)+5);
				$(this).attr('x', Math.abs(Number(left)-356.86+1));
			}
			//移不出右边框
			if (left + width1 > left2 + width2 ){
				left = left2 + width2 - width1;
				$(this).attr('x',Number(left)-356.86);
			}
			
			move_location = move_location2;
			chart1.series[number].points[0].shapeArgs.x = $drag_object.attr('x');
			chart1.series[number].points[0].shapeArgs.width = resize_width;
			
			var after_x1 = Number($drag_object.attr('x'))-0.5;
			var after_x2 = Number($drag_object.attr('x')) + Number(resize_width)-0.5;
			//console.log(chart1.series[number].name + "  移动后的x1坐标：" + after_x1 + ",移动后的x2坐标：" + after_x2);
			var averageTime = getAverageValue(chart1);
			var time1 = averageTime[0] * after_x1 + 1414886400000;
			var time2 = averageTime[0] * after_x2 + 1414886400000;
			var time3 = timestampToTime(time1);
			var time4 = timestampToTime(time2);
			//console.log("draggable："+ chart1.series[number].name + " 开始时间：" + time3 + ", 结束时间：" + time4);
		},
		  
		onStopDrag: function(e) {
			//元素拖拽结束	
			drag_Elocation = e.clientX;  //拖动结束的位置
			var resize_width = e.data.width;  //拖动后的宽度
			var stepMax = Math.abs(Number(drag_Elocation) - Number(move_location));  //拖动的距离
			
			var rect_x = $(this).attr('x');
			
			if(drag_Elocation > move_location){
				$(this).attr('x',Number(rect_x) + Number(stepMax)); 
			}else{
				$(this).attr('x',Number(rect_x) - Number(stepMax));
			}
			
			if(left <= left2){
				left = left2;
				//$(this).attr('x', Number(rect_x)+5);
				$(this).attr('x', Math.abs(Number(left)-356.86+1));
			}
			if (left + width1 > left2 + width2 ){
				left = left2 + width2 - width1;
				$(this).attr('x', Math.abs(Number(left)-356.86));
			}
			
			
			chart1.series[number].points[0].shapeArgs.x = $(this).attr('x');
			chart1.series[number].points[0].shapeArgs.width = resize_width;
			
			var after_x1 = Number($(this).attr('x'))-0.5;
			var after_x2 = Number($(this).attr('x')) + Number(resize_width)-0.5;
			//console.log(chart1.series[number].name + "  移动后的x1坐标：" + after_x1 + ",移动后的x2坐标：" + after_x2);
			var averageTime = getAverageValue(chart1);
			var time1 = averageTime[0] * after_x1 + 1414886400000;
			var time2 = averageTime[0] * after_x2 + 1414886400000;
			var time3 = timestampToTime(time1);
			var time4 = timestampToTime(time2);
			console.log("drag："+ chart1.series[number].name + " 开始时间：" + time3 + ", 结束时间：" + time4);
			
			data_gantt[number].data.x = time1;
			data_gantt[number].data.x2 = time2;
			data_area[number].data[0][0] = time1;
			data_area[number].data[1][0] = time2;
			initAreaMaps();
			//console.log(data_area);
	    },
	});
}
/**
 * 甘特图改变尺寸
 */
function resizable(chart1){
	$(".highcharts-partfill-original").resizable({
		handles: "e,w",
		edge: 5,  //实指可缩放区域的宽度
		maxWidth: 500,
		animate: false,
		//autoHide: false,  //当用户鼠标没有悬浮在元素上时是否隐藏手柄

		onStartResize: function(e){			
			resize_location = event.clientX;  //拖动前的位置
			resizeStart = $(this).attr('x');
			rightStart = $(this).attr('x2');
			rect_width = $(this).attr('width'); //拖动前的宽度
			
			
		},
		onResize: function(e){
			var $drag_object = $(this);
			//确定拖动元素是哪一个
			number = $drag_object.parent().parent().attr("class").split(" ")[1].replace("highcharts-series-","");
			var resize_location2 = e.clientX;  //拖动后的屏幕位置			
			var step = Math.abs(Number(resize_location) - Number(resize_location2));  //拖动的距离
			//var rect_width = $drag_object.attr('width'); //拖动前的宽度
			var resize_width = e.data.width;  //拖动后的宽度
			
			if(resize_location2 >= resize_location){
				//鼠标向右移动
				if(Number(resize_width) - Number(rect_width) < 0){
					//拖动左边框，右边框不动
					$drag_object.attr('x',Number(resizeStart) + step);
					$drag_object.attr('x2',rightStart);
				}											
			}else{
				//鼠标向左移动
				if(Number(resize_width) - Number(rect_width) > 0){
					//拖动左边框，右边框不动
					$drag_object.attr('x',Number(resizeStart) - step);
					$drag_object.attr('x2',rightStart);					
				}			
			}	
			
			//html显示resize后的数据
			chart1.series[number].points[0].shapeArgs.x = $drag_object.attr('x');
			chart1.series[number].points[0].shapeArgs.width = resize_width;
		
			var after_x1 = Number($drag_object.attr('x')) - 0.5;
			var after_x2 = Number($drag_object.attr('x')) + Number(resize_width) - 0.5;			
			console.log(chart1.series[number].name + "  改变尺寸后的x1坐标：" + after_x1 + ",改变尺寸后的宽度：" + resize_width);			
			//console.log(chart1.series[number].name + "  改变尺寸后的x1坐标：" + after_x1 + ",改变尺寸后的x2坐标：" + after_x2);
			
			/*var time1 = average1 * after_x1 + 1414886400000;
			var time2 = average1 * after_x2 + 1414886400000;
			var time3 = timestampToTime(time1);
			var time4 = timestampToTime(time2);
			//console.log("resizable："+ chart1.series[number].name + " 开始时间：" + time3 + ", 结束时间：" + time4);*/
		},
		
		onStopResize: function (e) {
			resize_Elocation = event.clientX;  //拖动结束的位置
			var stepMax = Math.abs(Number(resize_location) - Number(resize_Elocation));  //拖动的距离
			var resize_widthMax = e.data.width;  //拖动后的宽度
			
			if(resize_Elocation < resize_location){
				//鼠标向左移动
				if(Number(resize_widthMax) - Number(rect_width) > 0){
					//拖动左边框，右边框不动
					$(this).attr('x',Number(resizeStart) - stepMax);
					$(this).attr('x2',rightStart);					
				}											
			}else{
				//鼠标向右移动
				if(Number(resize_widthMax) - Number(rect_width) < 0){
					//拖动左边框，右边框不动
					$(this).attr('x',Number(resizeStart) + stepMax);
					$(this).attr('x2',rightStart);
				}		
			}
			
			chart1.series[number].points[0].shapeArgs.x = $(this).attr('x');
			chart1.series[number].points[0].shapeArgs.width = resize_widthMax;
		
			var after_x1 = Number($(this).attr('x')) - 0.5;
			var after_x2 = Number($(this).attr('x')) + Number(resize_widthMax) - 0.5;				
			console.log(chart1.series[number].name + "  改变尺寸后的x1坐标：" + after_x1 + ",改变尺寸后的x2坐标：" + after_x2);
			var averageTime = getAverageValue(chart1);
			
			/*var startTimeStamp = chart1.series[0].points[0].options.x;
			var endTimeStamp = chart1.series[0].points[0].options.x2;
			var width = chart1.series[0].points[0].shapeArgs.width;
			var average = (endTimeStamp - startTimeStamp)/width;*/
			
			var time1 = averageTime[0] * after_x1 + 1414886400000;
			var time2 = averageTime[0] * after_x2 + 1414886400000;
			var time3 = timestampToTime(time1);
			var time4 = timestampToTime(time2);
			console.log("resizable："+ chart1.series[number].name + " 开始时间：" + time3 + ", 结束时间：" + time4);
			data_area[number].data[0][0] = time1;
			data_area[number].data[1][0] = time2;
			initAreaMaps();
		}
	});
}