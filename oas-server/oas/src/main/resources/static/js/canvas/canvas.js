document.write("<script language=javascript src='/js/canvas/animate.js'></script>");
 
 localDB.deleteSpace("myDB");
     //创建一个叫myDB存储空间
     localDB.createSpace("myDB");

     var time=0;
     var photo=[];
     var startTime;
     var startTimeback;
     var endTime;
     var accelerate=1;
     var sumSecond;
     $(function(){
     	//bannerInit()
     	init();
     	  });    
    

 var event='';
 var eventQuest="";
 var playInternal;
 var cacheFlag=0;
 var internalChange;
 var acceleChange;
 var btn = document.querySelector('.btn')
 btn.addEventListener('click', function () {
	 console.log(event)
	  if (!event) {
		   
		  if($('#range_speed').val()==$('#range_speed').attr("max")){
			  init(); 
			  
		  }
		  
			  //event=setInterval('autoBanner("1")', playInternal);
			
			  event=setTimeout('autoBanner("1")', playInternal)
			  if(!cacheFlag)
			  { eventQuest=setInterval(quest,1000);}
		  
		 
		  //clearInterval(event);
		 
		  //event=setTimeout(autoBanner('1'),playInternal);
		 
		  $('#buttonPlay').addClass('btn2');
	  } else {
		  
		  $('#buttonPlay').removeClass('btn2');
		  clearInterval(event);
		  event="";
	  }
	})
function quest(){
	 console.log("123")
	  
	 if(startTimeback==""){
		 clearInterval(eventQuest);
		 return;
	 }
	
		    	//var date1 = new Date(startTimeback)
		       //startTimeback=increateTime(false,date1,2)
		      
		       var endTimeQuest=increateTime(false,startTimeback,2);//3秒
		       console.log("endtime",endTimeQuest)
		       //console.log("startTimeback2",startTimeback2)
		       console.log("startTimeback",startTimeback)
		        
		     
		      if(startTimeback>endTime){
		    	  
		    	  cacheFlag=1;
		    	  clearInterval(eventQuest);
		    	  eventQuest="";
		 		 return;
		      }
//		       var p=getUrl(startTimeback);
//		      if(p.photo1.length!=0){
//		    	   clearInterval(eventQuest);
//		    	   eventQuest="";
//		    	   return;
//		       }
		       var data={"startTime":startTimeback,
		    		"endTime":endTimeQuest};
		      

		       questAjax(data);
				startTimeback=increateTime(false,endTimeQuest,1);
 }  
function questAjax(data)
{
	$.ajax({
		
		   type: 'post',
		   url: '/api/v1/test/selectTestMsgByPeriod',
		   data: JSON.stringify(data),
		   contentType : 'application/json;charset=utf8',
		   dataType: 'json',
		   cache: false,
		   async:true,
		   success: function (res) {
		     if (res.code == 0) {
		    	var row=res.data.testModelList;
		    	
		    	var imgs=[];
		    	var m=-1;
		    	var k=0;
		    	 for(var i=0;i<row.length;i++)
		    	{ 	
		    		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
			 			{ 
		    			 row.splice(i,1);
		 					i--;
		 				}else{
		 					

		 					  var p=getUrl(row[i].time);
		   		    		 
		   		    		  if(p.photo1.length==0)
		   		    			{
		   		    			  imgs[i]=new Image();
		   		    			  imgs[i].src=row[i].picPath;
		   		    			  localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
		   		    			 
		   		    			}
		   		    		  	else if(i>0)
		   			    		  	{ if(!p.sum)
		   		    		  			{ 
		   				    		  		if(row[i].time==row[i-1].time)
		   					    			  { imgs[i]=new Image();
		   				     		 			imgs[i].src=row[i].picPath;
		   					    			  	localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
		   					    			  	
		   					    			  	if(i!=row.length-1)
		   					 		 			{
		   					     		 			if(row[i].time!=row[i+1].time)
		   					     		 			{  
		   					     		 				console.log("22223333",i)
		   					     		 				console.log("2222",m)
		   					         		 			localDB.insert("myDB", {time:row[i].time,sum:i-m});
		   					         		 			
		   					         		 			m=i;
		   					         		 			
		   					         		 		
		   					         		 		}
		   					 		 			}else{
		   					 		 			
		   					 		 				localDB.insert("myDB", {time:row[i].time,sum:i-m});
		   					 		 				}
		   					    			  	
		   							    	  }
		   		    		  				}else{
				   		    		  				if(i!=row.length-1)
			   					 		 			{
			   					     		 			if(row[i].time!=row[i+1].time)
			   					     		 			{  
			   					         		 			m=i;

			   					         		 		}
			   					 		 			}
				   		    		  			
		   		    		  				}
		   			    		  			
		   			    		  	  
		   			    		  		}
//		   			    		 else{
//		   			    			 localDB.update("myDB", {url:row[i].picPath,time:row[i].time});
//		   			    		 }
		   			    	   
		 				}
		    	}

		     } else {
		    	 alert(res.message);
		     }
		   },
		   error: function (res) {
			  alert("11"+JSON.stringify(res));
		   },
		   complete: function () {
		   }
		  });
	}
var number=2;
 function init(){
 	var data={
 		  "number": number};
 	var total;
 $.ajax({

    type: 'post',
    url: '/api/v1/test/selectTestMsg',
    data: JSON.stringify(data),
    contentType : 'application/json;charset=utf8',
    dataType: 'json',
    cache: false,
    async:false,
    success: function (res) {
      if (res.code == 0) {
     	 console.log(res)
     	 total=res.data.total;
     	startTime=res.data.startTime;
     	endTime=res.data.endTime;
     	//startTimeback=res.data.startTime;
     	
     	//$("#range_speed").max=res.data.totalOfLast*2;
     	$('#range_speed').val(0);
     	time=0;
     	changeThumb(0);
     	console.log(startTime)
     	var start=new Date(startTime);
     	var end=new Date(endTime);
     	console.log(endTime)
     	startTimeback=increateTime(false,startTime,2);
     	var max=Number((end-start)/1000)+1;
     	console.log(end-start)
     	
     	$(".range").attr({'max':max});  
     	var endDisplay=increateTime(false,endTime,1);//多了一秒
     	$("#timeStart").val(startTime.substr(startTime.length-8));
     	$("#timeEnd").val(endDisplay.substr(endDisplay.length-8));
     	var row=res.data.testModelList;
     	console.log(row)
     	photo=[];
     	var imgs=[];
     	var m=-1;
     	 for(var i=0;i<row.length;i++)
     	{ 
     		 console.log("tupian",row[i].picPath)
     		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
     		 	{ row.splice(i,1);
     		 		i--;
     		 	}else{

     		 		//localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
     		 		photo.push(row[i].picPath)
     		 		console.log(photo)

     		 		var p=getUrl(row[i].time)
			    		
			    		  if(p.photo1.length==0)
			    			{ imgs[i]=new Image();
		     		 		  imgs[i].src=row[i].picPath;
			    			  localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
			    			 
			    			}
			    		  	else if(i>0)
				    		  	{
			    		  		 if(!p.sum)//已插入
			    		  			{ 
					    		  		if(row[i].time==row[i-1].time)//第一次插入导致重复
						    			  { 
					    		  			imgs[i]=new Image();
					         		 		imgs[i].src=row[i].picPath;
						    			  	localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
						    			  	
						    			  	if(i!=row.length-1)//对i处理
						 		 			{console.log("2222")
						     		 			if(row[i].time!=row[i+1].time)//插入sum
						     		 			{  
						     		 				console.log("22223333")
						         		 			localDB.insert("myDB", {time:row[i].time,sum:i-m});
						     		 				sumSecond=i-m;
						         		 			m=i;
						         		 			
						         		 		
						         		 		}
						 		 			}else{
						 		 			
						 		 				localDB.insert("myDB", {time:row[i].time,sum:i-m});
						 		 				}
						    			  	
								    	  }
			    		  				}
			    		  		
			    		  		
			    		  		
				    		  	
				    		  	}
//				    		 else{
//				    			 localDB.update("myDB", {url:row[i].picPath,time:row[i].time});
//				    		 }
			    		 
			    		 }

     		 	}
     		
     	
      } else {
     	 	alert(res.message);
      		}
    },
    error: function (res) {
 	  alert("11"+JSON.stringify(res));
    },
    complete: function () {
    }
   });
 var phoList = getEvent('ibanner_pic' + 1).getElementsByTagName('img');


  for (var i = 0; i < phoList.length; i++) {
      phoList[i].src = photo[i];
      console.log("lujin",phoList[i].src)
         
      
  }

  playInternal=1000*number/total;
  internalChange=playInternal;
  acceleChange=1;
 
  //time=0-Number(playInternal/1000);
  //$(".range").attr({'step':playInternal/1000}); 
 } 
 var eventChange;
 var questTime;
 function changeSpeed() {
	 if(eventReal)
		{$('#range_speed').val(0);
		 return;}
		var value = $('#range_speed').val();
		var sum=$("#range_speed").attr("max");
		console.log(value);
		console.log(sum);
		var valStr = value/sum*100 + "% 100%";
		
		$('#range_speed').css({
		  "background-size": valStr
		}) 
	
		if(value==sum){
			
			value=sum-1;
		}
			
		//var date=new Date(startTime);
		//var num = new Number(value);
		 questTime=increateTime(false,startTime,value);
		 console.log(questTime);
		 time=value;//滑动时间
		 mc=0;//每秒的数据量计算
		$("#timeStart").val(questTime.substr(questTime.length-8));
		if(time==0)
			{init();
			return;}
		photo=[];
		var p=getUrl(questTime);
		if(p.photo1.length==0){
			console.log(5555)
			var data={
					"startTime":questTime,
					"endTime":questTime
				}
			soloAjax(data);
			//if(event){
			questTime=increateTime(false,questTime,1);
			clearInterval(eventChange);
			
			eventChange=setInterval(changeQuest,1000);
			//}

				
		}else{
			photo=p.photo1;	
		}
		var picList = getEvent('ibanner_pic' + "1").getElementsByTagName('a');
		var phoList = getEvent('ibanner_pic' + "1").getElementsByTagName('img');	
		phoList[0].src=photo[0]
		 picList[0].style.zIndex = '3';
         picList[0].style.left = '0px';
 }
 function soloAjax(data){
		$.ajax({

		    type: 'post',
		    url: '/api/v1/test/selectTestMsgByPeriod',
		    data: JSON.stringify(data),
		    contentType : 'application/json;charset=utf8',
		    dataType: 'json',
		    cache: false,
		    async:false,
		    success: function (res) {
		      if (res.code == 0) {
		   
		     	var row=res.data.testModelList;
		     	
		     	
		     	var imgs=[];
		     	var m=-1;
		     	 for(var i=0;i<row.length;i++)
		     	{ 
		     		 console.log("tupian",row[i].picPath)
		     		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
		     		 	{ row.splice(i,1);
		     		 		i--;
		     		 	}else{
		     		 		
		     		 		photo.push(row[i].picPath);
		     		 		imgs[i]=new Image();
				     		imgs[i].src=row[i].picPath;//一秒的数据不用判断重复
					    	localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});  	
				    			  	if(i!=row.length-1)
				 		 			{
				     		 			if(row[i].time!=row[i+1].time)
				     		 			{  
				     		 				
				         		 			localDB.insert("myDB", {time:row[i].time,sum:i-m});
				     		 				sumSecond=i-m;
				         		 			m=i;
				         		 			
				         		 		
				         		 		}
				 		 			}else{
				 		 			
				 		 				localDB.insert("myDB", {time:row[i].time,sum:i-m});
				 		 				}
				    			  	
//				    			  	 else{
//						    			 localDB.update("myDB", {url:row[i].picPath,time:row[i].time});
//						    		 }
	    		  				}
	    		 }

		      } else {
		     	 	alert(res.message);
		      		}
		    },
		    error: function (res) {
		 	  alert("11"+JSON.stringify(res));
		    },
		    complete: function () {
		    }
		   });
 }
function changeQuest(){
	
	//var date1 = new Date(questTime)
  
    var endTimeQuest=increateTime(false,questTime,2);//3秒
    console.log("endtimechange",endTimeQuest)
   
    console.log("startTimechange",questTime)
    if(questTime>endTime){
 	   console.log(111)
 	   
  	  clearInterval(eventChange);
 	   eventChange="";
 		 return;
    }
    var p=getUrl(questTime);
    if(p.photo1.length!=0){
 	   clearInterval(eventChange);
 	  eventChange="";
 	   return;
    }
  
    var data={"startTime":questTime,
 		"endTime":endTimeQuest};
   
    questAjax(data);
    questTime=increateTime(false,endTimeQuest,1);
}
function changeThumb(time){
	
	var value=time;
	//var sum=$("#timeb").val();
	var sum=$("#range_speed").attr("max")
	
	var valStr = value / sum * 100 + "% 100%";
	

	var start=increateTime(false,startTime,parseInt(value))
	console.log(start)

	var time2=increateTime(false,startTime,1);
	
	if(value!=0)
		{//防止初始化拼接
			var p=getUrl(start);
			
			console.log(p)
			console.log(p.photo1)
			if(p.photo1.length!=0)
			{
				sumSecond=p.sum;
				 if(start!=time2)//防止第2秒重复
				{
					
					photo=photo.concat(p.photo1);
				}
	
				 console.log("ppp",photo);
			}
		}
	
	$("#timeStart").val(start.substr(start.length-8))
	
	$('#range_speed').css({
	  "background-size": valStr
	})
	
	$('#range_speed').val(value);
	console.log(value);
	console.log(valStr);
}

 //将秒数转为00:00格式   
 function timeToStr(time) {   
     var m = 0,   
     s = 0,   
     _m = '00',   
     _s = '00';   
    time = Math.floor(time % 3600);   
     m = Math.floor(time / 60);   
     s = Math.floor(time % 60);   
     _s = s < 10 ? '0' + s : s + '';   
     _m = m < 10 ? '0' + m : m + '';   
     return _m + ":" + _s;   
 }   
 //触发播放事件   
 $('.play').on('click',function(){   
     var audio=document.getElementById('ao');   
     audio.play();   
     setInterval(function(){   
         var t=parseInt(audio.currentTime);   
     $(".range").attr({'max':751});   
     $('.max').html(timeToStr(751));   
         $(".range").val(t);   
     $('.cur').text(timeToStr(t));   
     },1000);   
 });   
 //监听滑块，可以拖动   
 $(".range").on('change',function(){   
	 if(eventReal)
		{$('#range_speed').val(0);
		 return;}
     //document.getElementById('ao').currentTime=this.value;
     $(".range").val(this.value);   
 });  

 
 
 

function getUrl(time)
{
	var photo1=[];
	var sum="";
	var data = localDB.select("myDB",time,false);
	
	if(data!="")
	{	if(data[data.length-1].Obj.sum)
		{
			 sum=data[data.length-1].Obj.sum;
			 for(var i=0;i<data.length-1;i++)
				{
				 photo1.push(data[i].Obj.url);
				}
		}else{
			for(var i=0;i<data.length;i++)
			{
				photo1.push(data[i].Obj.url);
			
			}
		}
		
	}
	return {photo1,sum};
	
}
    function getEvent(id) 
    {
    	
        return document.getElementById(id);
    }
   
    
    function picZ(ibannerIndex) 
    {
        var picList = getEvent('ibanner_pic' + ibannerIndex).getElementsByTagName('a');
        for (var i = 0; i < picList.length; i++) {
            picList[i].style.zIndex = '1';
        }
    }
   
 var mc=0;
    function autoBanner(ibannerIndex) 
    {
    	//|| !getEvent('ibanner_btn' + ibannerIndex)|| autoKey
        if (!getEvent('ibanner' + ibannerIndex) || !getEvent('ibanner_pic' + ibannerIndex)  ) {
            return;
        }
   
    	
        
       
        //var btnList = getEvent('ibanner_btn' + ibannerIndex).getElementsByTagName('span');
        var picList = getEvent('ibanner_pic' + ibannerIndex).getElementsByTagName('a');
        var phoList = getEvent('ibanner_pic' + ibannerIndex).getElementsByTagName('img');
        
        
        //ajax
        //var photo=[];
       // console.log(picList1)
      
       console.log("photo.length",photo.length)
       if(photo.length==0){
    	   if(eventReal){
    		   clearInterval(eventReal);
    		   eventReal="";
    		   return;
    	   }
			if($('#range_speed').val()==$('#range_speed').attr("max")){
       		//alert("数据已播完")
       		 $('#buttonPlay').removeClass('btn2');
       		clearInterval(event);
       		event="";
       		startTimeback="";
       			
       		return;
			}
			else{
				
				//time=Number(time)-Number(playInternal/(1000*accelerate));
				if(mc==0){
					mc=sumSecond-1;
					console.log("oooomc",mc)
					time--;
				}else{
					mc--;
				}
				
			}
   	}else{
       for (var i = 0; i < phoList.length; i++) {
       	
       	
           phoList[i].src = photo[i]
           console.log("lujin",phoList[i].src)
             
           
       }
       //picZ(ibannerIndex);
       //btnList[0].className = 'current';
       //picList[currentNum].style.zIndex = '2';
       picList[0].style.zIndex = '3';
       picList[0].style.left = '0px';
       
       //moveElement('picLi_' + ibannerIndex + '0', 0, 0, 0);
       //requestAnimationFrame(moveElement);
       //moveElement();
   	}
      photo.splice(0, 1);
    
       		 
            console.log("mc",mc)
//            if(parseInt(time, 10) === time){
//            	playInternal=internalChange;
//            	accelerate=acceleChange;
//            }
           
            //time=Number(time)+Number(playInternal/(1000*accelerate));//实际一秒显示后端传来的一秒的数据量（后端每秒数据量不一致会导致加速减速出现问题）
         	console.log("time",time)
         	if(!eventReal){
         		mc++; 
         		 if(mc==sumSecond)//每秒的数据量，用mc变量计算，隔离playInternal，保证每张图片显示时间一致
         	//if(parseInt(time, 10) === time)
         	{		time++;
         			 changeThumb(time);
         			 mc=0;
         			 }
         	}
            
    		
         	
         	console.log("sumSecond",sumSecond)
         	event=setTimeout('autoBanner("1")', playInternal)

    }
  
    	$('#accelerate').on('change',function(){
    		 
            //获取对应值--后期作为类选择器
    		if($(this).val()==0)
    		{
    			return;
    		}else if($(this).val()==-1)
    			{
    			playInternal = playInternal*4;
    			//acceleChange=accelerate*4;
    			}else
    				{
    				playInternal = playInternal/4;
    				//acceleChange=accelerate/4;
    				}
	  
    		
            console.log(playInternal )

    })
    function changeTime(time){
		
		var newTime = parseInt(time);
		var hour = toZero(Math.floor(newTime/3600));//Math.floor返回小于等于x的最大整数:
		var min = toZero(Math.floor(newTime%3600/60));
		var sec = toZero(Math.floor(newTime%60));
		return hour+":"+min+":"+sec;
		function toZero(num){
			if(num<10){
				return '0'+num;
			} else{
				return num;
			}
		}
	}
    function reduceTime(flag,time1,seconds)
    {	var t = new Date(time1);//你已知的时间
        var t_s = t.getTime();//转化为时间戳毫秒数
        t.setTime(t_s - 1000 * seconds);
        //t.setTime(t.setHours(t.getHours() -8));
        var time=generateTime(flag,t)
        return time}
    function increateTime(flag,time1,seconds) {
        var t = new Date(time1);//你已知的时间
        var t_s = t.getTime();//转化为时间戳毫秒数
        t.setTime(t_s + 1000 * seconds);
        //t.setTime(t.setHours(t.getHours() -8));
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
    
    
function increateSpeed(){
	if(eventReal)
	{return;}
	//if(event){
	var value = Number($('#range_speed').val())+1;
	
	var sum=$("#range_speed").attr("max");
	console.log(value);
	console.log(sum);
	$('#range_speed').val(value);
	var valStr = value/sum*100 + "% 100%";
	
	

	if(value==sum){
		
		value=sum-1;
	}

	 var increateQuestTime=increateTime(false,startTime,value);
	 console.log(increateQuestTime);
	 time=value;//滑动
	 mc=0;
	$("#timeStart").val(increateQuestTime.substr(increateQuestTime.length-8));
	if(time==0)
		{init();
		return;}
	photo=[];
	var p=getUrl(increateQuestTime);
	
	if(p.photo1==""){
		console.log(5555)
		var data={
				"startTime":increateQuestTime,
				"endTime":increateQuestTime
			}
		soloAjax(data);	
	}else{
		photo=p.photo1;
	}
	$('#range_speed').css({
		  "background-size": valStr
		}) 
	var picList = getEvent('ibanner_pic' + "1").getElementsByTagName('a');
	var phoList = getEvent('ibanner_pic' + "1").getElementsByTagName('img');	
	 phoList[0].src=photo[0]
	 picList[0].style.zIndex = '3';
     picList[0].style.left = '0px';
	//}
}
function reduceSpeed(){
	if(eventReal)
		{return;}
	//if(event){
	var value = Number($('#range_speed').val())-1;
	if(value==-1)
		{return;}
	var sum=$("#range_speed").attr("max");
	console.log(value);
	console.log(sum);
	$('#range_speed').val(value);
	var valStr = value/sum*100 + "% 100%";
	

	if(value==sum){
		
		value=sum-1;
	}

	var reducequestTime=increateTime(false,startTime,value);
	 time=value;//滑动
	 mc=0;
	$("#timeStart").val(reducequestTime.substr(reducequestTime.length-8));
	if(time==0)
		{init();
		return;}
	photo=[];
	var p=getUrl(reducequestTime);
	if(p.photo1==""){
		console.log(5555)
		var data={
				"startTime":reducequestTime,
				"endTime":reducequestTime
			}
		soloAjax(data);
			
	}else{
		photo=p.photo1;
	}
	console.log(reducequestTime);
	$('#range_speed').css({
	  "background-size": valStr
	}) 
	var picList = getEvent('ibanner_pic' + "1").getElementsByTagName('a');
	var phoList = getEvent('ibanner_pic' + "1").getElementsByTagName('img');	
	phoList[0].src=photo[0]
	 picList[0].style.zIndex = '3';
     picList[0].style.left = '0px';
	//}
}
    var photoReal;
    var eventReal;
    function real()
    {
    	 if(!eventReal)
    	 {
    		 event="";
    		 photo=[];
    		 var date=new Date("0");
    		 console.log(date);
    		 var time0=generateTime(false,date);
    		 console.log(time0)
    		 startTime="0";
    		 $("#timeStart").val(time0.substr(time0.length-8));
	         $("#timeEnd").val(time0.substr(time0.length-8));
	        	var imgs=[];
    		connect(undefined,function(msg){
	    		console.log(msg);
	    		msg = JSON.parse(msg)
	    	
	    		for(var i=0;i<msg.length;i++)
	    		{
	    			imgs[i]=new Image();
		     		imgs[i].src=row[i].picPath;
	    			photo.push(msg[i].picPath)
	    		}
	    		$(".range").attr({'max':photo.length});   
	        	
	        	if(!eventReal){
	        		$('#range_speed').val(0);
	            	time=0;
	            	changeThumb(0);
	            $('#buttonPlay').addClass('btn2');
	            	eventReal=setInterval('autoBanner("1")', playInternal);
	        	}
    		},"topic");

    	}else{
    		 clearInterval(eventReal);
    		$('#buttonPlay').removeClass('btn2');
    		eventReal="";
    	}
    }
   
  
    

