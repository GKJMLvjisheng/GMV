document.write("<script language=javascript src='/js/canvas/animate.js'></script>");
 
 localDB.deleteSpace("myDB");
     //创建一个叫myDB存储空间
     localDB.createSpace("myDB");

     var time=0;
     var photo=[];
     var startTime;
     var startTimeback;
     var endTime;
     var t=1;
     var sum;
     $(function(){
     	//bannerInit()
     	init();
     	  });    
    

 var event='';
 var eventQuest="";
 var playInternal;
 var cacheFlag=0;
 var btn = document.querySelector('.btn')
 btn.addEventListener('click', function () {
	 console.log(event)
	  if (!event) {
		   
		  if($('#range_speed').val()==$('#range_speed').attr("max")||$('#range_speed').val()==0){
			  init(); 
			  
		  }
		  
			  event=setInterval('autoBanner("1")', playInternal);
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
		    	var date1 = new Date(startTimeback)
		       //startTimeback=increateTime(false,date1,2)
		       
		       var endTime=increateTime(false,date1,2);//3秒
		       console.log("endtime",endTime)
		       console.log("startTimeback",startTimeback)
		       var data={"startTime":startTimeback,
		    		"endTime":endTime};
		      
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
			    	var row=res.data.testModelList
			    	 if(row.length==0){
			    		 clearInterval(eventQuest);
			    		 cacheFlag=1;
			    		 return;
			    	 }
			    	 for(var i=0;i<row.length;i++)
			    	{ 	
			    		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
				 			{ 
			    			 row.splice(i,1);
			 					i--;
			 				}
			    	}
			    	var k=0;
			    	 for(var i=0;i<row.length;i++)
			    		 {
			    		  if(getUrl(row[i].time)=="")
			    			{localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});}
			    		  	else if(i>0)
				    		  	{
				    		  	  if(row[i].time==row[i-1].time)
				    			  { k=i+1;
				    			  	localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
						    		 continue;
						    	  }
				    		  	}
				    		 else{
				    			 localDB.update("myDB", {url:row[i].picPath,time:row[i].time});
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
				startTimeback=increateTime(false,date1,1);;
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
     	startTimeback=res.data.startTime;
     	
     	//$("#range_speed").max=res.data.totalOfLast*2;
     	$('#range_speed').val(0);
     	time=0;
     	changeThumb(0);
     	var start=new Date(startTime)
     	var end=new Date(endTime)
     	console.log(end-start)
     	var max=Number((end-start)/1000)+1
     	 $(".range").attr({'max':max});  
     	 end=increateTime(false,end,1)//多了一秒
     	 $("#timeStart").val(startTime.substr(startTime.length-8));
     	$("#timeEnd").val(end.substr(endTime.length-8));
     	var row=res.data.testModelList;
     	console.log(row)
     	photo=[];
     	 for(var i=0;i<row.length;i++)
     	{ 
     		 console.log("tupian",row[i].picPath)
     		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
     		 	{ row.splice(i,1);
     		 		i--;
     		 	}else{
     		 		//localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
     		 		photo.push(row[i].picPath)
     		 	
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
  //time=0-Number(playInternal/1000);
  //$(".range").attr({'step':playInternal/1000}); 
 } 
 var questTime=0;
 function changeSpeed() {
	 if(eventReal)
		{$('#range_speed').val(0);
		 return;}
		var value = $('#range_speed').val();
		var sum=$("#range_speed").attr("max");
		console.log(value);
		console.log(sum);
		var valStr = value/sum*100 + "% 100%";
		console.log(questTime);
		$('#range_speed').css({
		  "background-size": valStr
		}) 
	
		if(value==sum){
			
			value=sum-1;
		}
			
		var date=new Date(startTime);
		//var num = new Number(value);
		 questTime=increateTime(false,date,value);
		 time=value;//滑动
		$("#timeStart").val(questTime.substr(questTime.length-8));
		if(time==0)
			{init();
			return;}
		photo=[];
		photo=getUrl(questTime);
		if(photo==""){
			console.log(5555)
			var data={
					"startTime":questTime,
					"endTime":questTime
				}
				$.ajax({

				   type: 'post',
				   url: '/api/v1/test/selectLoadMsgByPeriod',
				   data: JSON.stringify(data),
				   contentType : 'application/json;charset=utf8',
				   dataType: 'json',
				   cache: false,
				   async:false,
				   success: function (res) {
				     if (res.code == 0) {
				    	 console.log(res)
				    
				    	var row=res.data.testModelList;
				    	 sum=0;
				    	 
				    	 for(var i=0;i<row.length;i++)
				    	{ 
				    		 console.log("tupian",row[i].picPath)
				    		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
				    		 	{ row.splice(i,1);
				    		 		i--;
				    		 	}else{
				    		 	localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
				    		 	photo.push(row[i].picPath)
				    		 	sum++;
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
		var picList = getEvent('ibanner_pic' + "1").getElementsByTagName('a');
		var phoList = getEvent('ibanner_pic' + "1").getElementsByTagName('img');	
		phoList[0].src=photo[0]
		 picList[0].style.zIndex = '3';
         picList[0].style.left = '0px';
 }
function changeThumb(time){
	
	var value=time;
	//var sum=$("#timeb").val();
	var sum=$("#range_speed").attr("max")
	
	var valStr = value / sum * 100 + "% 100%";
	
	
	var date1 = new Date(startTime)
	var date2 = new Date(startTime)
	
	var start=increateTime(false,date1,parseInt(value))
	console.log(start)
	console.log(questTime)
	var date3 = new Date(questTime)
	var time2=increateTime(false,date2,1)
	var quest1=increateTime(false,date3,1);
	console.log("time2",time2)
	//var quest2=increateTime(false,date3,1);
	//var quest3=increateTime(false,date3,1);
	//if(questTime!=start)
	//{	
	
	if(value!=0)
		{//防止初始化拼接
			var p=getUrl(start);
			console.log(p)
			console.log(photo)
			if(p.length!=0)
			{
				
				 if(start!=time2)//防止第2秒重复
				{
					
					photo=photo.concat(p);
				}
//				 else if(cacheFlag){
//					photo=photo.concat(p);
//					
//				}
	
				 console.log("ppp",photo);
			}
		}
	

	
	
	//var date1 = new Date(start)
	//start=reduceTime(false,date1,1)
	$("#timeStart").val(start.substr(start.length-8))
	
	$('#range_speed').css({
	  "background-size": valStr
	})
	
	$('#range_speed').val(value);
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

 
 
 

function getUrl(time){
	var photo1=[];
	var data = localDB.select("myDB",time,false);
	for(var i=0;i<data.length;i++)
	{photo1.push(data[i].Obj.url);}
	return photo1;
}
    function getEvent(id) {
    	
        return document.getElementById(id);
    }
   
    
    function picZ(ibannerIndex) {
        var picList = getEvent('ibanner_pic' + ibannerIndex).getElementsByTagName('a');
        for (var i = 0; i < picList.length; i++) {
            picList[i].style.zIndex = '1';
        }
    }
   
 
    function autoBanner(ibannerIndex) {
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
			if(cacheFlag){
    		//alert("数据已播完")
    		 $('#buttonPlay').removeClass('btn2');
    		clearInterval(event);
    		clearInterval(eventReal);
    		event="";
    		eventReal="";
    		//var date1 = new Date(startTime)
    		startTimeback="";
    			//reduceTime(false,date1,1);
    		return;
			}
			else{
				time=Number(time)-Number(playInternal/1000);
			}
    	}else{
        for (var i = 0; i < phoList.length; i++) {
        	
        	
            phoList[i].src = photo[i]
            console.log("lujin",phoList[i].src)
              
            
        }
        picZ(ibannerIndex);
        //btnList[0].className = 'current';
        //picList[currentNum].style.zIndex = '2';
        picList[0].style.zIndex = '3';
        picList[0].style.left = '650px';
        
        //moveElement('picLi_' + ibannerIndex + '0', 0, 0, 0);
        //requestAnimationFrame(moveElement);
        moveElement();
    	}
       photo.splice(0, 1);
           
            console.log("time",time)
            time=Number(time)+Number(playInternal/1000);
         	console.log("111",time)
         	if(!eventReal){
         	if(parseInt(time, 10) === time)
         	{changeThumb(time);}
         	}
       // }
//         else {
//             classNormal(ibannerIndex);
//             picZ(ibannerIndex);
//             var nextNum = currentNum + 1;
//             btnList[nextNum].className = 'current';
//             picList[currentNum].style.zIndex = '2';
//             picList[nextNum].style.zIndex = '3';
//             picList[nextNum].style.left = '650px';
//             moveElement('picLi_' + ibannerIndex + nextNum, 0, 0, 10);
//         }
    }
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
    function reduceTime(flag,t,seconds)
    {//var t = new Date();//你已知的时间
        var t_s = t.getTime();//转化为时间戳毫秒数
        t.setTime(t_s - 1000 * seconds);
        //t.setTime(t.setHours(t.getHours() -8));
        var time=generateTime(flag,t)
        return time}
    function increateTime(flag,t,seconds) {
        //var t = new Date();//你已知的时间
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
	if(event){
	var value = Number($('#range_speed').val())+1;
	
	var sum=$("#range_speed").attr("max");
	console.log(value);
	console.log(sum);
	$('#range_speed').val(value);
	var valStr = value/sum*100 + "% 100%";
	console.log(questTime);
	$('#range_speed').css({
	  "background-size": valStr
	}) 

	if(value==sum){
		
		value=sum-1;
	}
		
	var date=new Date(startTime);
	//var num = new Number(value);
	 questTime=increateTime(false,date,value);
	 time=value;//滑动
	$("#timeStart").val(questTime.substr(questTime.length-8));
	if(time==0)
		{init();
		return;}
	photo=[];
	photo=getUrl(questTime);
	if(photo==""){
		console.log(5555)
		var data={
				"startTime":questTime,
				"endTime":questTime
			}
			$.ajax({

			   type: 'post',
			   url: '/api/v1/test/selectLoadMsgByPeriod',
			   data: JSON.stringify(data),
			   contentType : 'application/json;charset=utf8',
			   dataType: 'json',
			   cache: false,
			   async:false,
			   success: function (res) {
			     if (res.code == 0) {
			    	 console.log(res)
			    
			    	var row=res.data.testModelList;
			    	 sum=0;
			    	 
			    	 for(var i=0;i<row.length;i++)
			    	{ 
			    		 console.log("tupian",row[i].picPath)
			    		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
			    		 	{ row.splice(i,1);
			    		 		i--;
			    		 	}else{
			    		 	localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
			    		 	photo.push(row[i].picPath)
			    		 	sum++;
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
	var picList = getEvent('ibanner_pic' + "1").getElementsByTagName('a');
	var phoList = getEvent('ibanner_pic' + "1").getElementsByTagName('img');	
	phoList[0].src=photo[0]
	 picList[0].style.zIndex = '3';
     picList[0].style.left = '0px';
	}
}
function reduceSpeed(){
	if(eventReal)
		{return;}
	if(event){
	var value = Number($('#range_speed').val())-1;
	if(value==-1)
		{return;}
	var sum=$("#range_speed").attr("max");
	console.log(value);
	console.log(sum);
	$('#range_speed').val(value);
	var valStr = value/sum*100 + "% 100%";
	console.log(questTime);
	$('#range_speed').css({
	  "background-size": valStr
	}) 

	if(value==sum){
		
		value=sum-1;
	}
		
	var date=new Date(startTime);
	//var num = new Number(value);
	 questTime=increateTime(false,date,value);
	 time=value;//滑动
	$("#timeStart").val(questTime.substr(questTime.length-8));
	if(time==0)
		{init();
		return;}
	photo=[];
	photo=getUrl(questTime);
	if(photo==""){
		console.log(5555)
		var data={
				"startTime":questTime,
				"endTime":questTime
			}
			$.ajax({

			   type: 'post',
			   url: '/api/v1/test/selectLoadMsgByPeriod',
			   data: JSON.stringify(data),
			   contentType : 'application/json;charset=utf8',
			   dataType: 'json',
			   cache: false,
			   async:false,
			   success: function (res) {
			     if (res.code == 0) {
			    	 console.log(res)
			    
			    	var row=res.data.testModelList;
			    	 sum=0;
			    	 
			    	 for(var i=0;i<row.length;i++)
			    	{ 
			    		 console.log("tupian",row[i].picPath)
			    		 if(row[i].picPath.substr(row[i].picPath.length-4)=="null")
			    		 	{ row.splice(i,1);
			    		 		i--;
			    		 	}else{
			    		 	localDB.insert("myDB", {url:row[i].picPath,time:row[i].time});
			    		 	photo.push(row[i].picPath)
			    		 	sum++;
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
	var picList = getEvent('ibanner_pic' + "1").getElementsByTagName('a');
	var phoList = getEvent('ibanner_pic' + "1").getElementsByTagName('img');	
	phoList[0].src=photo[0]
	 picList[0].style.zIndex = '3';
     picList[0].style.left = '0px';
	}
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
	        	
    		connect(undefined,function(msg){
	    		console.log(msg);
	    		msg = JSON.parse(msg)
	    	
	    		for(var i=0;i<msg.length;i++)
	    		{
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
   
  
    

