var timeOfBegin;
var timeOfEnd;
var status1,status2,status3,status4,status5,status6;
var commit = false;
/**
 * 图表的初始化
 */
$(function() {	
	initMap();	
	initAreaMaps();
	setTimeControl();
//	countStartTime("2018/12/27 18:00:00");
//	countEndTime("2018/12/27 18:00:00");
});
/**
 * 日期控件的设置，默认显示当天时间
 */
function setTimeControl(){
	$('#time1').datetimepicker({
        format: 'YYYY-MM-DD HH:mm:ss',
		locale: moment.locale('zh-cn'),
		//maxDate:todayDate()
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
	
	$('#time3').datetimepicker({
        format: 'YYYY-MM-DD HH:mm:ss',
		locale: moment.locale('zh-cn'),
		//maxDate:todayDate()
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime3").val();
		$('#time4').datetimepicker('minDate',startTime);
	 });
	 
	 $('#time4').datetimepicker({
        format: 'YYYY-MM-DD HH:mm:ss',
		locale: moment.locale('zh-cn'),
	}).on('dp.change', function (ev) {
		var startTime = $("#startTime3").val();
		$('#time4').datetimepicker('minDate',startTime);
 	});
	 
	document.getElementById("startTime3").value="";
	//document.getElementById("endTime3").value=todayDate() + "";
	 
	$('#time5').datetimepicker({
	        format: 'YYYY-MM-DD HH:mm:ss',
			locale: moment.locale('zh-cn'),
			//maxDate:todayDate()
		}).on('dp.change', function (ev) {
			var startTime = $("#startTime4").val();
			$('#time6').datetimepicker('minDate',startTime);
		 });
		 
		 $('#time6').datetimepicker({
	        format: 'YYYY-MM-DD HH:mm:ss',
			locale: moment.locale('zh-cn'),
		}).on('dp.change', function (ev) {
			var startTime = $("#startTime4").val();
			$('#time6').datetimepicker('minDate',startTime);
	 	});
		 
		document.getElementById("startTime5").value="";
		//document.getElementById("endTime5").value=todayDate() + "";
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
 * 关闭侧边栏
 */
function closeA(){
	var side = document.getElementById("side");
	side.style['margin-right'] = -470 + "px";
	$("#content").removeClass("col-8").addClass("col-11");
 	$(".con").removeClass("con").addClass("con-no-side");
}
/**
 * 新建实验，关闭侧边栏，页面未提交显示提示
 */
function closeAside(){	
	if($("#createExp1")[0].innerHTML=="新建实验"){
		if($("#expName").val() != "" || ($("#injectStyle").val() != "" && $("#injectStyle").val() != null) ||
		   $("#expTime").val() != "" || $("#expNumber").val() != "" || 
		   $("#startTime3").val() != ""  || $("#endTime3").val() != ""){
			//新建实验内容未提交
			if(commit != true){
				var msg = "页面正在编辑中，确认离开吗？"
				if (confirm(msg)==true){
					closeA();
				}						
			}else{				
				closeA();
			}			
		}else{
			closeA();
		}
	}else if($("#createExp1")[0].innerHTML=="实验安排和资源情况"){
		if($("#expName").val() != "" || ($("#injectStyle").val() != "" && $("#injectStyle").val() != null) ||
		   $("#expTime").val() != "" || $("#expNumber").val() != "" || 
		   $("#startTime3").val() != ""  || $("#endTime3").val() != "" || $("#startTime5").val() != ""  || 
		   $("#endTime5").val() != ""){
			//新建实验内容未提交
			if(commit != true){
				//实验不是已固定的手动注入类
				var expName = $("#expName").val();
				for(i in data_gantt){
					if(data_gantt[i].name == expName){
						var inStyle = data_gantt[i].data[0].inStyle;
						var fixed = data_gantt[i].data[0].fixed;
					}
				}
				if(inStyle=="手动注入" && fixed=="true"){
					closeA();	
				}else{					
					var msg = "页面正在编辑中，确认离开吗？"
						if (confirm(msg)==true){
							closeA();
						}
				}
									
			}else{				
				closeA();
			}			
		}else{
			closeA();
		}
	}	
}
/**
 * 清空侧边栏内容及按钮设置
 */
function clearAside(){
	$("#expName").val("");
    $("#injectStyle").val("");
	$("#expTime").val("");
	$("#expNumber").val("");
	$("#startTime3").val("");
	$("#endTime3").val("");
	$("#startTime5").val("");
	$("#endTime5").val("");
	$("#expPlanResult").val("");
	$("aside").find("button").removeAttr('disabled');
	$("aside").find("input").removeAttr('disabled');	        	    	
	$("select").removeAttr('disabled');
	$("#close").removeAttr('disabled');
	$("#expPlanResult").attr('disabled','disabled');
	$("#msg_expName").html("");
	$("#msg_injectStyle").html("");
	$("#msg_expTime").html("");
	$("#msg_expNumber").html("");
	$("#msg_arrangeTime").html("");
}
/**
 * 检查输入的实验名
 */
function checkExpName(){
	var expName = $("#expName").val();
	if(expName==""){
		$("#msg_expName").html("请输入项目名称！");
		$("#msg_expName").css({'color':'red','font-size':'12px'});
		status1 = 0;
	}else{
		$("#msg_expName").html("");
		status1 = 1;
	}
	return status1;	
}
/**
 * 检查输入的注入方式
 */
function checkInstyle(){
	var injectStyle = $("#injectStyle").val();
	//alert(JSON.stringify(injectStyle));
	if(injectStyle==null || injectStyle=="请选择"){
		$("#msg_injectStyle").html("请选择注入方式！");
		$("#msg_injectStyle").css({'color':'red','font-size':'12px'});
		status2 = 0;
	}else{
		$("#msg_injectStyle").html("");
		status2 = 1;
	}
	return status2;	
}
/**
 * 检查持续时间
 */
function checkExpTime(){
	var expTime = $("#expTime").val();
	if(expTime==""){
		$("#msg_expTime").html("请输入持续时间！");
		$("#msg_expTime").css({'color':'red','font-size':'12px'});
		status3 = 0;
	}else{
		if(/^[1-9]\d*[sS]$/.test(expTime)){
			$("#msg_expTime").html("");
			status3 = 1;
		}else{
			$("#msg_expTime").html("输入时间，如3s/3S");
			$("#msg_expTime").css({'color':'red','font-size':'10.7px'});
			status3 = 0;
		}		
	}
	return status3;	
}
/**
 * 检查重复次数
 */
function checkExpNumber(){
	var expNumber = $("#expNumber").val();
	if(expNumber==""){
		$("#msg_expNumber").html("请输入重复次数！");
		$("#msg_expNumber").css({'color':'red','font-size':'12px'});
		status4 = 0;
	}else{
		if(/^\d+$/.test(expNumber)){
			$("#msg_expNumber").html("");
			status4 = 1;
		}else{
			$("#msg_expNumber").html("请输入正整数");
			$("#msg_expNumber").css({'color':'red','font-size':'12px'});
			status4 = 0;
		}		
	}
	return status4;	
}
/**
 * 检查实验安排时间
 */
function checkArrangeTime(){
	var startTime3 = $("#startTime3").val();
	var endTime3 = $("#endTime3").val();
	if(startTime3=="" || endTime3==""){
		$("#msg_arrangeTime").html("请输入实验安排时间！");
		$("#msg_arrangeTime").css({'color':'red','font-size':'12px','margin-left':'24%'});
		status5 = 0;
	}else{
		$("#msg_arrangeTime").html("");
		status5 = 1;		
	}
	return status5;	
}
/**
 * 检查实验规划时间
 */
function checkPlanTime(){
	var startTime5 = $("#startTime5").val();
	var endTime5 = $("#endTime5").val();
	if(startTime5=="" || endTime5==""){
		$("#msg_PlanTime").html("请输入实验安排时间！");
		$("#msg_PlanTime").css({'color':'red','font-size':'12px','margin-left':'24%'});
		status6 = 0;
	}else{
		$("#msg_PlanTime").html("");
		status6 = 1;		
	}
	return status6;	
}
/**
 * 新建实验并提交
 */
function newExpCommit(){
	checkExpName();
	checkInstyle();
	checkExpTime();
	checkExpNumber();
	checkArrangeTime();	
	if($("#createExp1")[0].innerHTML=="新建实验"){
		if(status1==1 && status2==1 && status3==1 && status4==1 && status5==1){
			alert("输入内容符合要求");
			commit = true;
		}else{
			alert("请确认输入内容是否符合要求！");
		}
	}else if($("#createExp1")[0].innerHTML=="实验安排和资源情况"){
		checkPlanTime();
		if(status1==1 && status2==1 && status3==1 && status4==1 && status5==1 && status6==1){
			alert("输入内容符合要求");
		}else{
			alert("请确认输入内容是否符合要求！");
		}
	}
}
/**
 * 同步缩放效果，即当一个图表进行了缩放效果，其他图表也进行缩放
 */
function syncExtremes(e) {	
		var thisChart = this.chart || e.target.chart;
		if (e.trigger !== 'syncExtremes') { 
			Highcharts.each(Highcharts.charts, function (chart) {
				if (chart && (chart !== thisChart)) {
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
//		pointPadding: 0.3, //柱子之间的间隔(会影响到柱子的大小)
//		pointPlacement: -0.2,// 通过 pointPadding 和 pointPlacement 控制柱子位置
		data: [ 
			{
    			x: Date.UTC(2018, 11, 29, 07, 0, 0),//实验规划时间(色块)
    			x2: Date.UTC(2018, 11, 29, 18, 0, 0),
    			y: 0,
    			color: colors[2],
    			//borderColor: "#000000",
    			inStyle: '手动注入',
    			fixed: "false",
    			rejected:"false",
    			name: '项目1',
    			repeatNumber: 1,
    			x3:Date.UTC(2018, 11, 29, 02, 0, 0),//实验安排时间（虚线框）
    			x4:Date.UTC(2018, 11, 29, 24, 0, 0),
    			expPlanResult:'已通过',
			},   		
		],
	},
	{
		name: '项目2',
		//showInLegend: false, // 不显示图例
		pointWidth: 20,
		data: [
			{
				x: Date.UTC(2018, 11, 30, 12, 0, 0),
    			x2: Date.UTC(2018, 11, 30, 19, 0, 0),
    			y: 0,
    			//color: colors[1],
    			color: "#000000",  //已固定实验
    			inStyle: '手动注入',
    			fixed: "true",
    			rejected:"false",
    			name: '项目2',
    			repeatNumber: 2,
    			x3: Date.UTC(2018, 11, 30, 8, 0, 0),
    			x4: Date.UTC(2018, 11, 30, 24, 0, 0),
    			expPlanResult:'已通过',
			},     		
		],
	},
	{
		name: '项目3',
		//showInLegend: false, // 不显示图例
		pointWidth: 20,
		data: [
			{
				x: Date.UTC(2018, 11, 30, 8, 0, 0),
    			x2: Date.UTC(2018, 11, 30, 14, 0, 0),
    			y: 1,
    			color: "#a1afc9",
    			//color: "#000000",  //已固定实验
    			inStyle: '自动注入',
    			fixed: "false",
    			rejected:"true",
    			name: '项目3',
    			repeatNumber: 1,
    			x3: Date.UTC(2018, 11, 30, 7, 0, 0),
    			x4: Date.UTC(2018, 11, 30, 19, 0, 0),
    			expPlanResult:'未通过',
			},     		
		],
	},
	
	{
		name: '项目4',
		//showInLegend: false, // 不显示图例
		pointWidth: 20,   			    		
			data: [
	    		{
	    			x: Date.UTC(2018, 11, 31, 08, 0, 0),  	    				    			
	    			x2: Date.UTC(2018, 11, 31, 12, 0, 0),
	    			y: 2,
	    			color: colors[2],
	    			inStyle: '自动注入',
	    			fixed: "false",
	    			rejected:"false",
	    			name: '项目4',
	    			repeatNumber: 1,
	    			x3: Date.UTC(2018, 11, 31, 6, 0, 0),
	    			x4: Date.UTC(2018, 11, 31, 19, 0, 0),
	    			expPlanResult:'已通过',
	    		}
   		],	    		    		    		   		
	},
];
var data_area = [
	{
    	name: '项目1',	    	
   		color: colors[2],
   		data: [		    		
    			[ Date.UTC(2018, 11, 29, 07, 0, 0), 1],
    			[ Date.UTC(2018, 11, 29, 18, 0, 0), 1]		    					    		
		],
    },
    {
    	name: '项目2',
    	color: '#000',	    	
   		data: [		    		
    			[ Date.UTC(2018, 11, 30, 12 ,00 ,00), 1],
    			[ Date.UTC(2018, 11, 30, 19 ,00 ,00), 1]		    					    		
		],
    },
    {
    	name: '项目3',
    	color: '#000',	    	
   		data: [],
    },
    {
    	name: '项目4',
    	color: colors[2],	    	
   		data: [		    		
    			[ Date.UTC(2018, 11, 31, 08, 0, 0), 1],		    			
    			[ Date.UTC(2018, 11, 31, 12, 0, 0), 1]	    		    		
		],
    },
    
    {
		name: '功率资源上限',
		type: 'spline',
		yAxis: 0,
		data: [
			[ Date.UTC(2018, 11, 29, 02, 0, 0), 3.6],		    			
			[ Date.UTC(2018, 11, 29, 20, 0, 0), 3],
			[ Date.UTC(2018, 11, 30, 03, 0, 0), 2.9],		    			
			[ Date.UTC(2018, 11, 30, 14, 0, 0), 3.7],
			[ Date.UTC(2018, 11, 31, 09, 0, 0), 2.8],		    			
			[ Date.UTC(2018, 11, 31, 22, 0, 0), 3.3]
		],
		marker: {
			enabled: true
		},
		dashStyle: 'shortdot',
		tooltip: {
			enabled: false,
		}
	}, 
];

/**
 * 甘特图初始化
 */
function initMap(){
	var average = 0;
	var move_location = 0;	//拖动前的位置
	var resizeStart = 0;
	var rightStart = 0;
	var number;
	
	var chart1 = Highcharts.chart('container1', {
		chart: {
	    	type: 'xrange',
	        zoomType: 'x',
	        plotBackgroundColor: null,
	        plotBorderWidth: 0,
	        plotShadow: false,
	        backgroundColor: 'transparent',
	        spacing: [20, 20, 10, 20],  //图表上，右，下，左的空白
	        events: {	       
	        	//监听图表区域选择事件
	        	selection: function () {
	        	//动态修改
	        	//alert(1)
	        	//var c = chart.xAxis[0]
	        	}
	        },
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
	  		min: Date.UTC(2018, 11, 29, 00, 00, 00),
	  		max: Date.UTC(2019, 0, 1, 00, 00, 00),
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
		            click: function(e) {
		            	var color = e.point.color;
		            	var name =e.point.options.name;
		            	if(color=="#000000" && e.point.options.inStyle=="手动注入"){
		            		//alert("该实验已被固定");
		            		if($("#createExp1")[0].innerHTML=="实验安排和资源情况"){
		            			$(".fourConfig").css('display','block');
			        	    	$("#expConfigfour").css('background','#F5F5F5');
			        	    	$("#expConfigfive").css('background','#F5F5DC');
			        	    	
			        	    	$("aside").find("button").attr('disabled','disabled');
			        	    	$("aside").find("input").attr('disabled','disabled');			   
			        	    	$(".glyphicon glyphicon-trash").css("pointer-events","none");
			        	    	$("select").attr('disabled','disabled');
			        	    	$("#close").removeAttr('disabled');	
			        	    	timeOfBegin = e.point.options.x;
			        	    	timeOfEnd = e.point.options.x2;
			        	    	
		            		}
		            		
		            	}else{
		            		//window.open("https://www.baidu.com");
		            		$(".fourConfig").css('display','none');
		        	    	$("#expConfigfour").css('background','#F5F5DC');
		        	    	$("#expConfigfive").css('background','#F5F5F5');
		        	    	
		        	    	$("aside").find("button").removeAttr('disabled');
		        	    	$("aside").find("input").removeAttr('disabled');
		        	    	//$(".glyphicon glyphicon-trash").attr('disabled',true);		        	    	
		        	    	$("select").removeAttr('disabled');
		        	    	$("#close").removeAttr('disabled');
		        	    	$("#expPlanResult").attr('disabled','disabled');
		            	}
		            			            	
		            	if($("#createExp1")[0].innerHTML=="实验安排和资源情况"){
			            	for(i in data_gantt){
			            		var disTime = ((data_gantt[i].data[0].x2-data_gantt[i].data[0].x)/1000).toFixed(0)+"s";
			            		var start1 = timestampToTime(data_gantt[i].data[0].x);
			            		var end1 = timestampToTime(data_gantt[i].data[0].x2);
			            		var start2 = timestampToTime(data_gantt[i].data[0].x3);
			            		var end2 = timestampToTime(data_gantt[i].data[0].x4);
			            		if(data_gantt[i].name == name){
			            			$("#expName").val(name);
			            			$("#injectStyle").val(data_gantt[i].data[0].inStyle);
			            			$("#expTime").val(disTime);
			            			$("#expNumber").val(data_gantt[i].data[0].repeatNumber);
			            			$("#startTime3").val(start2);
			            			$("#endTime3").val(end2);
			            			$("#startTime5").val(start1);
			            			$("#endTime5").val(end1);
			            			$("#expPlanResult").val(data_gantt[i].data[0].expPlanResult);
			            			
			            		}
			            	}
		            	}
		            	
		            	
		            },
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
	   		enabled: false  
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
	          	/*if(borderColor=="#000000"){
	          		res += "<br/>" + "实验安排不可调整";
	          	}*/
	          	  
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
	
	var disX = $($(".highcharts-plot-background")[0])[0].attributes.x.value;  //x轴左侧边框长度
	var disY = $($(".highcharts-plot-background")[0])[0].attributes.y.value;  //y轴上侧边框长度
	
	for(i in data_gantt){
		if(data_gantt[i].data[0].inStyle == '手动注入'){
			var x_gantt = $($(".highcharts-partfill-original")[i])[0].attributes.x.value;  //甘特图的位置
			var y_gantt = $($(".highcharts-partfill-original")[i])[0].attributes.y.value;  //甘特图的位置
			chart1.renderer.image('/img/hand.jpg',  parseFloat(x_gantt) +parseFloat(disX) - 30, parseFloat(y_gantt) + parseFloat(disY) , 30, 20).add().addClass("img1");
		}
		
		if(data_gantt[i].data[0].fixed != 'true'){
			draggable(chart1);
			resizable(chart1); 
		}else{
			$($(".highcharts-partfill-original")[i]).draggable('disable');  
			$($(".highcharts-partfill-original")[i]).resizable('disable');  
		}
	}
	
	
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
	        spacing: [20, 20, 10, 20],
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
				min: Date.UTC(2018, 11, 29, 00, 00, 00),
		  		max: Date.UTC(2019, 0, 1, 00, 00, 00),
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
	   	    enabled: false , 
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
	            /*var series = this.series;
	            var dataX = series.processedXData;
		        var res = series.name + "<br/>";
		        res += "开始时间：" + timestampToTime(dataX[0]) + "<br/>" + "结束时间："+ timestampToTime(dataX[dataX.length-1])+"<br/>";
		        res += "功率：" + series.processedYData[0] + "w";*/
	            var res;
		        res = "时间：" + timestampToTime(this.x) + "<br/>";
		        res += "功率：" + this.y + "w";
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

function getAverageValue(chart){
	var averageTime = new Array();
	/*var startTimeStamp = chart.series[0].points[0].options.x;
	var endTimeStamp = chart.series[0].points[0].options.x2;
	var width = chart.series[0].points[0].shapeArgs.width;
	var average = (endTimeStamp - startTimeStamp)/width;*/
//	var startTimeStamp = chart.axes[0].dataMin;
//	var endTimeStamp = chart.axes[0].dataMax;
	var startTimeStamp = 1546041600000;
	var endTimeStamp = 1546300800000;
	var width = $($(".highcharts-plot-background")[0]).attr("width");
	var average = (endTimeStamp - startTimeStamp)/width;
	//average = 927835.0515463918;
	averageTime.push(average);	
	return averageTime;
}
/**
 * 甘特图拖拽
 */
function draggable(chart1){
	var number;
	var width;
	var rect_x;
	var averageTime;
	
	var left1;	
	var left2;	
	var width2;
	var left;	
	var dis_xAndLeft;
	
	$(".highcharts-partfill-original").draggable({
		axis: "h",
		cursor: "move",  //默认
		edge: 5,  //实际可拖动区域和指定的可拖动区域之间的边距,单位像素
		maxWidth: 1000,
		
		onStartDrag:function(e){
			move_location = e.clientX;  //拖动前的位置
			number = $(this).parent().parent().attr("class").split(" ")[1].replace("highcharts-series-","");//确定拖动元素是哪一个
			//left1 = $($(".highcharts-partfill-original")[number]).position().left; //甘特图的位置信息
			averageTime = getAverageValue(chart1);
		},
		onDrag: function(e) {
			var move_location2 = e.clientX;  //拖动后的位置
			var step = Math.abs(move_location2 - move_location);  //拖动的距离
			rect_x = Number($(this).attr('x'));  //x坐标	
			width = Number($(this).attr('width'));  //甘特图的宽度
			//left = $($(".highcharts-partfill-original")[number]).position().left;	
			
			if(move_location2 > move_location){
				$(this).attr('x',rect_x + step); 
			}else{
				$(this).attr('x',rect_x - step);
			}	
			
			move_location = move_location2;
			chart1.series[number].points[0].shapeArgs.x = $(this).attr('x');						
		},
		  
		onStopDrag: function(e) {								
			var after_x1 = rect_x-0.5;
			var after_x2 = rect_x + width - 0.5;
			//console.log(chart1.series[number].name + "  移动后的x1坐标：" + after_x1 + ",移动后的x2坐标：" + after_x2);
			
			//var startS = chart1.axes[0].dataMin;
			var startS = 1546041600000;
			var time1 = averageTime[0] * after_x1 + startS;
			var time2 = averageTime[0] * after_x2 + startS;
			var time3 = timestampToTime(time1);
			var time4 = timestampToTime(time2);
			console.log("drag："+ chart1.series[number].name + " 开始时间：" + time3 + ", 结束时间：" + time4);			
			
			if(data_gantt[number].data[0].rejected != 'true'){
				data_gantt[number].data.x = time1;
				data_gantt[number].data.x2 = time2;
				data_area[number].data[0][0] = time1;
				data_area[number].data[1][0] = time2;
				initAreaMaps();
			}	
	    },
	});
}
/**
 * 甘特图改变尺寸
 */
function resizable(chart1){
	var resize_location = 0;
	var rect_width;
	
	var resize_left1;	
	var resize_left2;
	var resize_width2;
	var resize_left;
	var rect_x;
	
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
			averageTime = getAverageValue(chart1);
			//确定拖动元素是哪一个
			number = $(this).parent().parent().attr("class").split(" ")[1].replace("highcharts-series-","");
			/*left1 = $($(".highcharts-partfill-original")[number]).position().left; //甘特图的位置信息
			left2 = $($("#dash" + number))[0].offsetLeft;	//虚线框的信息	
			width2 = $($("#dash" + number))[0].offsetWidth;*/	
		},
		onResize: function(e){
			var $drag_object = $(this);
			var resize_location2 = e.clientX;  //拖动后的屏幕位置			
			var step = Math.abs(Number(resize_location) - Number(resize_location2));  //拖动的距离
			var resize_width = e.data.width;  //拖动后的宽度
			//left = $($(".highcharts-partfill-original")[number]).position().left;  //甘特图的位置信息
			
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
			//console.log(chart1.series[number].name + "  改变尺寸后的x1坐标：" + after_x1 + ",改变尺寸后的x2坐标：" + after_x2);
			
			//var startS = chart1.axes[0].dataMin;
			var startS = 1546041600000;
			var time1 = averageTime[0] * after_x1 + startS;
			var time2 = averageTime[0] * after_x2 + startS;
			var time3 = timestampToTime(time1);
			var time4 = timestampToTime(time2);
			console.log("resizable："+ chart1.series[number].name + " 开始时间：" + time3 + ", 结束时间：" + time4);
			
			if(data_gantt[number].data[0].rejected != 'true'){
				data_area[number].data[0][0] = time1;
				data_area[number].data[1][0] = time2;
				initAreaMaps();
			}	
		}
	});
}