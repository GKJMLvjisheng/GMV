

$(function() {
	//初始加载	
	mapsReady();
});

function display(){
	document.getElementById("main2").style.display="block";
	document.getElementById("hide").style.display="block";
	document.getElementById("display").style.display="none";
	$("#hide").css("padding-left","960px");
}

function hide(){
	document.getElementById("main2").style.display="none";
	document.getElementById("hide").style.display="none";
	document.getElementById("display").style.display="block";
}

function mapsReady(){
	var myChart1 = echarts.init(document.getElementById('main1'));
	var myChart2 = echarts.init(document.getElementById('main2'));

	//设置主题皮肤
//		myChart1.setTheme("macarons");
//		myChart2.setTheme("macarons");								
	
	var optionStartTime = [
		new Date("2015/09/06"),
		new Date("2015/09/06"),
		new Date("2015/09/03"),
		new Date("2015/09/06"),                                   
	];
	
	var optionEndTime = [
		new Date("2015/09/07"),
		new Date("2015/09/08"),
		new Date("2015/09/05"),
		new Date("2015/09/08"),                                   
	];
	
	var arrangeStartTime = [
		new Date("2015/09/06"),							
		new Date("2015/09/06"),							
		new Date("2015/09/03"),							
		new Date("2015/09/06"),  
	];
	
	var arrangeEndTime = [
		new Date("2015/09/07"),
		new Date("2015/09/07"),
		new Date("2015/09/04"),
		new Date("2015/09/07"), 
	];
	var data_gatt = ['实验1', '实验2', '实验3', '实验4']; //纵坐标
	var time_ = ['可选开始时间', '可选时间段', '安排开始时间', '安排时间段'];  //可选时间段实为可选结束时间，安排时间段实为安排结束时间
	var time = {'可选开始时间': optionStartTime, '可选时间段': optionEndTime, '安排开始时间': arrangeStartTime, '安排时间段': arrangeEndTime};
	
	var myArray1 = [];

	for(var i=0;i<data_gatt.length;i++){
		
		var dict = {
				name: time_[i],
				type: 'bar',
				stack: 'test1',
				itemStyle: {
					normal: {
						color: 'rgba(0,0,0,0)'  //黑色，最后0代表透明度
					}
				},					
				data: time[time_[i]],
			};
		
		if(i==2 || i==3){
			dict['stack'] = 'test2'; //// 需要堆叠的柱子请配置相同的stack!
		}
		
		if(i==1 || i==3){
			delete dict['itemStyle'];
		}
			
		myArray1[i] = dict;
	}		
	//console.log(myArray1);
	
	
	var option1 =  {
		title: {
			text: '时间安排',
			left: 10,				
		},
		
		grid: {        
			left: '5%',        
			right: '10%',        
			bottom: '15%', 
			top:'20%',
			containLabel: true    //调整表格位置
		}, 
		
		//显示小图标
		legend: {
			//orient: 'vertical',
			top: '20px',
			right: '300px',
			data:['可选时间段','安排时间段'],
		},
		
		//工具箱
		toolbox: {
			show : true,
				// orient: 'horizontal',     
				x: '725',                                                                                                                                          
				y: '0',                                                                                                                                              
			feature : {
				//还原
				restore: {
					show: true,
				},
				saveAsImage : {
					show: true,  
				},
				
				//数据视图
				/* dataView :{
					show: true,
				}, */
			}
		},
		
		xAxis: {                        	
			type: 'time',
			axisLabel: {
				show: true,
				interval: 0,
				// rotate: 45
			},  				
			// 控制网格线是否显示
			splitLine: {
					show: true, 
					//  改变轴线颜色
					lineStyle: {							
					}                            
			},	                        	                     					
		},                                           

		yAxis: {
			'name':'用户实验',                      
			left: 10,
			// 控制网格线是否显示
			splitLine: {
				show: true, 
				//  改变轴线颜色
				lineStyle: {                                      
				}                            
			},
			data: data_gatt,     
		},

		//图表下方的伸缩条	
		dataZoom:[
			{
		　　//type: 'inside',  //滚动鼠标来缩放
			type: 'slider',
		　　show : true, //是否显示
		　　realtime : true, //拖动时，是否实时更新系列的视图
			//backgroundColor: '#FFCCFF',                    		 
		　　start : 0, //伸缩条开始位置（1-100），可以随时更改
		　　end : 100, //伸缩条结束位置（1-100），可以随时更改
			fillerColor: 'rgba(144,197,237,0.2)',   // 滑动条选中范围的填充颜色
			handleColor: '#00CC99 ',//手柄颜色
			dataBackground:{                        //数据阴影的样式。
			    lineStyle: {
	               // color: '#000'   //阴影的线条样式
	            },
	            areaStyle: {
	                //color: 'red'  //阴影的填充样式
	            }                     
	        },
	        
	        filterMode: 'empty', //当前数据窗口外的数据，被 设置为空。即 不会 影响其他轴的数据范围
		},
		
		{  
           type: 'inside', // 这个 dataZoom 组件是 inside 型 dataZoom 组件
		　　show : true, //是否显示
		　　realtime : true, //拖动时，是否实时更新系列的视图
			//backgroundColor: '#FFCCFF',                    		 
		　　start : 0, //伸缩条开始位置（1-100），可以随时更改
		　　end : 100, //伸缩条结束位置（1-100），可以随时更改						        
	       filterMode: 'empty', //当
		},
			],
		
		//提示框
		tooltip: {
			trigger: 'axis',  // 触发类型，默认数据触发
			formatter: function (params) {
				//console.log(params[0]);
				var res = params[0].name + "</br>"
				var date0 = params[0].data;
				var date1 = params[1].data;  
				
		
				date0 = date0.getFullYear() + "-" + (date0.getMonth() + 1) + "-" + date0.getDate();
				date1 = date1.getFullYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDate(); 
				
				
				//解决隐藏形状后报错的问题，两种颜色形状全隐藏
				if(params[2]==undefined && params[3]==undefined){
					return;
				}
				//隐藏一种颜色
				else if(params[3]==undefined){
					
					var date2 = params[2].data;
					date2 = date2.getFullYear() + "-" + (date2.getMonth() + 1) + "-" + date2.getDate();
					
					if(params[1].seriesName != "可选时间段"){
						res += params[2].seriesName + ":</br>" + date0 + "~" + date2 + "</br>"; 
					}else{
						res += params[1].seriesName + ":</br>" + date0 + "~" + date1 + "</br>"; 
					}						 					
					return res;
					
				}
				//不隐藏
				else{
					
					var date2 = params[2].data;
					var date3 = params[3].data;
					date2 = date2.getFullYear() + "-" + (date2.getMonth() + 1) + "-" + date2.getDate();
					date3 = date3.getFullYear() + "-" + (date3.getMonth() + 1) + "-" + date3.getDate();
					res += params[1].seriesName + ":</br>" + date0 + "~" + date1 + "</br>";  
					res += params[3].seriesName + ":</br>" + date2 + "~" + date3 + "</br>";  
					return res;
				}
			
//					res += params[0].seriesName + "~" + params[1].seriesName + ":</br>" + date0 + "~" + date1 + "</br>";  
//					res += params[2].seriesName + "~" + params[3].seriesName + ":</br>" + date2 + "~" + date3 + "</br>";
				
			}
		},
		
		//坐标区域显示的图形
		series: myArray1,
	};

	
	var data = ['实验1','实验2','实验3','实验4'];
	var color = ['#04bb74', '#ffc400', '#ff80bf', 'rgba(138, 43, 226, 0.8)'];
	var data_x = ['2015-09-03', '2015-09-04', '2015-09-05', '2015-09-06', '2015-09-07', '2015-09-08'];
	var array1 = ['-','-', '-', '1','1'];
	var array2 = ['-','-', '-', '1','1'];
	var array3 = [ '1','1'];		
	var array4 = ['-','-', '-', '1','1'];
	var dict1 = {'实验1': array1, '实验2': array2,'实验3': array3,'实验4': array4};	
	
	var myArray2 = [];

	for(var i=0;i<data.length;i++){
		
		var dict = {
			name: data[i],
			type: 'line',
			stack:'1', // 需要堆叠的柱子请配置相同的stack!
			smooth: true,
			data: dict1[data[i]],
			itemStyle: {
				normal: {
					label : {
						show:true,
						position:'top',
						formatter:'{c}'
					},
					areaStyle:{
						color:color[i],
					},
					color:color[i],
				}
			}
		};
		
		if(i==2){
			delete dict['stack'];
		}
			
		myArray2[i] = dict;
	}		
	//console.log(myArray);
	
	
	var option2= {

		tooltip: {        
			trigger: 'axis'    
		}, 																
		
		grid: {        
			left: '7%',        
			right: '10%',        
			bottom: '25%', 
			top:'15%',
			containLabel: true    //调整表格位置
		}, 
		
		toolbox: {					        
			right:100,
			top:0
		},
		
		//图表下方的伸缩条
		legend: {
			//orient: 'vertical',
			//left: 'left',
			data:data,  //显示不同颜色图标
		},

		xAxis : [
			{
				type : 'category', //类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据
				boundaryGap : false,  //为true时，坐标轴两边留白策略
				// 控制网格线是否显示
				splitLine: {
					show: true, 
					//  改变轴线颜色
					lineStyle: {
						
					}                            
				},
				axisLabel: {
					interval: 0, // 0 强制显示所有标签,1表示『隔一个标签显示一个标签』
					alignWithLabel: true,  //类目轴中在 boundaryGap 为 true 的时候有效，可以保证刻度线和标签对齐
				},
				data: data_x,				
			}
		],
		
		yAxis: {
			'name':'功率（w）',
			type: 'value',
			//min: 0,				
			splitNumber: 3, //将y轴平均分为3份
			// 控制网格线是否显示
			splitLine: {
				show: true, 
				//  改变轴线颜色
				lineStyle: {	                                   
					//color: ['red']
				}                            
			},				
			
			axisLabel: {                   
                 formatter: function (value, index) {           
                  	return value.toFixed(1);      //数值后面0位小数
                 }                
             },

		},
		
		//图表下方的伸缩条	
		dataZoom:[
			{
		   type: 'slider',
		   xAxisIndex: 0,
		　　show : true, //是否显示
		　　realtime : true, //拖动时，是否实时更新系列的视图
			//backgroundColor: '#FFCCFF',                    		 
		　　start : 0, //伸缩条开始位置（1-100），可以随时更改
		　　end : 100, //伸缩条结束位置（1-100），可以随时更改
			fillerColor: 'rgba(144,197,237,0.2)',   // 滑动条选中范围的填充颜色
			handleColor: '#00CC99 ',//手柄颜色
			dataBackground:{                        //数据阴影的样式。
			    lineStyle: {
	               // color: '#000'   //阴影的线条样式
	            },
	            areaStyle: {
	                //color: 'red'  //阴影的填充样式
	            }                     
	        },
	        
	        filterMode: 'empty', //当前数据窗口外的数据，被 设置为空。即 不会 影响其他轴的数据范围
		},
		
		{  
	           type: 'inside', // 这个 dataZoom 组件是 inside 型 dataZoom 组件
	           xAxisIndex: 0,
			　　show : true, //是否显示
			　　realtime : true, //拖动时，是否实时更新系列的视图
				//backgroundColor: '#FFCCFF',                    		 
			　　start : 0, //伸缩条开始位置（1-100），可以随时更改
			　　end : 100, //伸缩条结束位置（1-100），可以随时更改						        
		       filterMode: 'empty', //当
		},
		
		{
			   type: 'slider',
			   yAxisIndex: 0,
			　　show : true, //是否显示
			　　realtime : true, //拖动时，是否实时更新系列的视图			              		 
//				　　start : 0, //伸缩条开始位置（1-100），可以随时更改
//				　　end : 100, //伸缩条结束位置（1-100），可以随时更改
				fillerColor: 'rgba(144,197,237,0.2)',   // 滑动条选中范围的填充颜色
				handleColor: '#00CC99 ',//手柄颜色
				dataBackground:{                        //数据阴影的样式。
				    lineStyle: {
		               // color: '#000'   //阴影的线条样式
		            },
		            areaStyle: {
		                //color: 'red'  //阴影的填充样式
		            }                     
		        },
		        
		        filterMode: 'empty', //当前数据窗口外的数据，被 设置为空。即 不会 影响其他轴的数据范围
			},

			],
			
		series: myArray2,						
	}; 
							
	// 为echarts对象加载数据
	myChart1.setOption(option1);
	myChart2.setOption(option2);
	
	//联动配置               
	echarts.connect([myChart1, myChart2]);
}