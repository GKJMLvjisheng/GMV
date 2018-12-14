
/**
 * 图表的初始化
 */
$(function() {
	initMaps();	
	setTimeControl();
});
/**
 * 日期控件的设置，默认显示当天时间
 */
function setTimeControl(){
	$('#time1').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
		maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime").val();
		$('#time2').datetimepicker('minDate',startTime);
	 });
	 
	 $('#time2').datetimepicker({
        format: 'YYYY-MM-DD',
		locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime").val();
		$('#time2').datetimepicker('minDate',startTime);
 	});
	 
	document.getElementById("startTime").value=todayDate();
	document.getElementById("endTime").value=todayDate(); 
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
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear();
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1);
    var D = date.getDate() < 10 ? '0'+ date.getDate() : date.getDate();
    var h = date.getHours()-8 < 10 ? '0'+ (date.getHours()-8) : (date.getHours()-8);  //转换为北京时间
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
/**
 * 甘特图和面积堆叠图的初始化
 */
function initMaps(){
	var colors = ['#FF99CC', '#FFCC00', '#33FF33', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8', '#8d4653', '#91e8e1'];
	var chart1 = Highcharts.chart('container1', {
		chart: {
	    	type: 'xrange',
	        zoomType: 'x',
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
	  		min: Date.UTC(2014, 10, 2),
	  		max: Date.UTC(2014, 10, 10),
	  		events: {
				setExtremes: syncExtremes
			},
			tickInterval: 1000, //设置横标标尺的间隔长度1s
	    }],

	    yAxis: [{
	        title: {
	            align: 'high',
	            text: '用户实验',
	            offset: 0,
	            rotation: 0,
	            y:-10,
	        },
	        categories: ['0','1', '2', '3'], 
	        //隐藏y轴
	  		labels: {
	            enabled: false
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
		            click: function(e) {
		            	//console.log(e);
		            	//console.log(e.point.series.name);
		            	var inStyle = e.point.inStyle;
		            	if(inStyle == "自动注入"){
		            		alert("该实验为自动注入，不可修改");
		            	}else if(inStyle == "手动注入"){
		            		window.open("https://www.baidu.com");
		            	}		            	  
		            },
		            //点击图例触发事件
		        	legendItemClick: function(e) {		        		 
//		        		    	var index = e.target.index;
//		        			    var class1 = ".highcharts-series-" + index + " rect";
//		        			    //$(class1).not(".highcharts-partfill-original").unbind('click');
//		        			    //chart2.userOptions.plotOptions.series.events.legendItemClick = false;		        			    
//		        				$(class1).not(".highcharts-partfill-original").last().click();	        				
		        		    	
		        		  /*
		        			  var index = e.target.index;
		        			  var color_ = e.target.data[0].color;
		        			  chart1.options.visible = !(e.target.options.visible == undefined ? true:e.target.options.visible);
		        			  chart2.options.visible = !(e.target.options.visible == undefined ? true:e.target.options.visible);
		        			  var class1 = ".highcharts-series-" + index;
		        			  var $visibilty_arr = $(".highcharts-series-" + index).filter('.highcharts-markers');
		        			  if($visibilty_arr.length>0){
		        				  if(chart1.options.visible){
		        					  $(".highcharts-series-" + index).filter('.highcharts-markers').attr("visibility",'visible');
		        					  $(".highcharts-series-" + index).filter('.highcharts-tracker').attr("visibility",'visible');
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').filter('.highcharts-legend-item').addClass('highcharts-legend-item-hidden');
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').filter('.highcharts-legend-item').children("text").css({'color':'#000','fill':'#000'});
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').filter('.highcharts-legend-item').children("rect").attr('fill',color_);
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').not('.highcharts-legend-item').attr("visibility",'visible');
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series path').attr("visibility",'visible');
		        				  }else{
		        					  $(".highcharts-series-" + index).filter('.highcharts-markers').attr("visibility",'hidden');
		        					  $(".highcharts-series-" + index).filter('.highcharts-tracker').attr("visibility",'hidden');
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').filter('.highcharts-legend-item').removeClass('highcharts-legend-item-hidden');
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').filter('.highcharts-legend-item').children("text").css({'color':'#ccc','fill':'#ccc'});
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').filter('.highcharts-legend-item').children("rect").attr('fill','#ccc');
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series').not('.highcharts-legend-item').attr("visibility",'hidden');
		        					  $(".highcharts-series-" + index).filter('.highcharts-area-series path').attr("visibility",'hidden');
			        					 
		        				  }
		        			  }
	        				 */		        			 		        		  			
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
	    series: [
	    	{
	    		name: '项目1',
	    		pointWidth: 20,
	    		//showInLegend: false, // 不显示图例
	    		data: [ 
	    			{
		    			x: Date.UTC(2014, 10, 8, 07, 0, 1),
		    			x2: Date.UTC(2014, 10, 9, 08, 0, 1),
		    			y: 2.1,
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
		    			x: Date.UTC(2014, 10, 2, 03 ,0 ,0),
		    			x2: Date.UTC(2014, 10, 3, 05 ,0 ,0),
		    			y: 2,
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
	   	    			x: Date.UTC(2014, 10, 2, 03, 0, 0),
	   	    			x2: Date.UTC(2014, 10, 4, 05, 0, 0),
	   	    			y: 1,
	   	    			color: colors[2],
	   	    			inStyle: '自动注入',
	   	    		}
	       		],	    		    		    		   		
	    	},
	    ]
	});

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
		        				  
		        		//$(class1).not(".highcharts-partfill-original").first().unbind('click');
		        		//event.preventDefault();	
			        		 
						/*var index = e.target.index;
						var color_ = e.target.data[0].color;
						chart1.options.visible = !(e.target.options.visible == undefined ? true:e.target.options.visible);
						chart2.options.visible = !(e.target.options.visible == undefined ? true:e.target.options.visible);
						var class1 = ".highcharts-series-" + index;
						var $visibilty_arr = $(".highcharts-series-" + index).filter('.highcharts-markers');
						if($visibilty_arr.length>0){
							if(chart1.options.visible){
								$(".highcharts-series-" + index).filter('.highcharts-markers').attr("visibility",'visible');
								$(".highcharts-series-" + index).filter('.highcharts-tracker').attr("visibility",'visible');
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').filter('.highcharts-legend-item').addClass('highcharts-legend-item-hidden');
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').filter('.highcharts-legend-item').children("text").css({'color':'#000','fill':'#000'});
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').filter('.highcharts-legend-item').children("rect").attr('fill',color_);
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').not('.highcharts-legend-item').attr("visibility",'visible');
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series path').attr("visibility",'visible');
							}else{
								$(".highcharts-series-" + index).filter('.highcharts-markers').attr("visibility",'hidden');
								$(".highcharts-series-" + index).filter('.highcharts-tracker').attr("visibility",'hidden');
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').filter('.highcharts-legend-item').removeClass('highcharts-legend-item-hidden');
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').filter('.highcharts-legend-item').children("text").css({'color':'#ccc','fill':'#ccc'});
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').filter('.highcharts-legend-item').children("rect").attr('fill','#ccc');
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series').not('.highcharts-legend-item').attr("visibility",'hidden');
								$(".highcharts-series-" + index).filter('.highcharts-xrange-series path').attr("visibility",'hidden');
									
							}
						}*/		        		        			
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
	    series: [	    	
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
		    			[ Date.UTC(2014, 10, 2, 03 ,0 ,0), 1],
		    			[ Date.UTC(2014, 10, 3, 05 ,0 ,0), 1]		    					    		
	    		],
		    },
		    
		    {
		    	name: '项目3',
		    	//color: '#33FF33',	    	
		   		data: [		    		
		    			[ Date.UTC(2014, 10, 2, 03, 0, 0), 1],
		    			[ Date.UTC(2014, 10, 3, 05, 0, 0), 1],
		    			[ Date.UTC(2014, 10, 4, 05, 0, 0), 1]	    		    		
	    		],
		    },
	    ]
	});
}