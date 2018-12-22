/**
 * 
 */
$(function(){
	var height = $(document).height();
	
	$("#myDIV").height(height);
	$("#myDIV").css({"background": "#EAEAEA"})
	document.getElementById("myDIV").addEventListener("contextmenu", myFunction);
	$("#myModal").draggable();//为模态对话框添加拖拽
	
	
})

	window.onclick=function(e){ 
		console.log(1)
		//用户触发click事件就可以关闭了，因为绑定在window上，按事件冒泡处理，不会影响菜单的功能
		document.querySelector('#menu').style.width=0;
		epMenu.destory();
	} 
	//window.oncontextmenu=function(e){
	//取消默认的浏览器自带右键 很重要！！
	function myFunction(){
		 console.log(2)
		 var menuNode=document.getElementById('epMenu');
		//var e = window.event;
		//e.preventDefault();
		 var evt = window.event || arguments[0];
	     evt.preventDefault();//关闭右键菜单，很简单
	     var rightedge = evt.clientX;
	     var bottomedge = evt.clientY;
	     epMenu.create({left:rightedge,top:bottomedge},[{name:'添加容器','action':addContainer},{name:'容器缩略图','action':hideContainer},{name:'c3','action':addContainer},{name:'c4','action':addContainer}]);
		//获取我们自定义的右键菜单
		//var menu=document.querySelector("#menu");
	
		//根据事件对象中鼠标点击的位置，进行定位
		//menu.style.left=e.clientX+'px';
		//menu.style.top=e.clientY+'px';
	
		//改变自定义菜单的宽，让它显示出来
		//menu.style.width='125px';
		//}
	}
	
		
	 
	
		  $("#myDIV").mouseleave(function(){
			  in_or_out = 2;
		 }); 
		 
	
		 
	var epMenu={
			    create:function(point,option){
				        var menuNode=document.getElementById('epMenu');
				        if(!menuNode){
				            //没有菜单节点的时候创建一个
				            menuNode=document.createElement("div");
				            menuNode.setAttribute('class','epMenu');
				            menuNode.setAttribute('id','epMenu');
				        }else $(menuNode).html('');//清空里面的内容
		
				        $(menuNode).css({left:point.left+'px',top:point.top+'px'});
				        for(var x in option){
				            var tempNode=document.createElement("a");
				            $(tempNode).text(option[x]['name']).on('click',option[x].action);
				            menuNode.appendChild(tempNode);
				        }
		
				        $("body").append(menuNode);
			    },
			    destory:function(){
			        	$(".epMenu").remove();
			    }    
			};
	
	function addContainer(){
	     $("#myModal").modal('show');
		 epMenu.destory();
			}
	
	function hideSysMenu(flag) {
	     return flag;
			} 
	function generateContainer(){
		 var name=$("#containerName").val();
		 var type=$("#containerType").val();
		 var w1= '<div  id ="page1" class="container">'
			 	+'<div class="ibanner" style="width:650px; height:300px;" id="ibanner1">'
			 	+'<div class="ibanner_pic" id="ibanner_pic1">'
			 	+'<a href="#" id="picLi_10"><img style="width:650px; height:300px;" src="" alt="" /></a>'
			 	+'</div>'
			 	+'</div>'
			 	+'<div  class="col-center-block">'
			 	+'<em class="btnleft "style="display:inline-block" onclick="reduceSpeed()"></em>'
			 	+'<em id="buttonPlay" class="btn_middle "style="display:inline-block" ></em>'
			 	+'<em class="btnright "style="display:inline-block"  onclick="increateSpeed()"></em>'
			 	+'<input type="range" style="display:inline-block" id="range_speed" min="0" max="100" class="range" value="0"oninput="changeSpeed()" />'
			 	+'<input type="button"style="display:inline-block" value="00:00:00" id="timeStart" class="time" />'
			 	+'<input type="button"style="display:inline-block" value="00:00:00" id="timeEnd" class="timeEnd" /> '
			 	+'<input type="button"style="display:inline-block" value="订阅" id="real" class="real" onclick="real()" /> '
			 	+'<select style="display:inline-block"  id="accelerate" class="accelerate"  > '
			 	+'<option value="0">请选择</option>'
			 	+'<option value="-1">慢速</option>'
			 	+'<option value="1">快速</option>'
			 	+'</select>'
			 	+'</div>'
			 	+'</div>'
			 	$("#myDIV").html(w1);
		 var number=2;
		
		 	var data={
		 		  "number": number};
		 	var photo=[];
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
		    
		     	var row=res.data.testModelList;
		     	console.log(row)
		     	
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
		     		 		
		     	
		      }
		     		 } 
		      }else {
		     	 	alert(res.message);
		      		}
		    },
		    error: function (res) {
		 	  alert("11"+JSON.stringify(res));
		    },
		    complete: function () {
		    }
		   });
		 var phoList = document.getElementById("ibanner_pic1").getElementsByTagName('img');


		  for (var i = 0; i < phoList.length; i++) {
		      phoList[i].src = photo[i];
		      console.log("lujin",phoList[i].src)
		         
		      
		  }

		 
		 
	}	
	var rects=[];
	   function rectar(name,x,y,width,height,id){
		   	  this.name=name;
	          this.x = x;
	          this.y = y;
	          this.width = width;
	          this.height = height;
	          this.id = id;
	      };
	function hideContainer(){
		//$(".container").style.display="none";
		$(".container").attr("style","display:none;");//隐藏div
		//document.getElementById("page1").style.display="none";
		 // 在页面创建 box
		var name=$("#containerType").val();
		if(!rects.length)
		{
			/*var active_box = document.createElement("div");
        active_box.id = "active_box";
        active_box.className = "box ";
        //active_box.className="col-center-block";
        active_box.style.top =20 + 'px';
        active_box.style.left =100 + 'px';
        active_box.style.width = 100 + 'px';
        active_box.style.height = 100 + 'px';
        var active_p = document.createElement("p");
        active_p.innerHTML=name;
        active_p.style.color="white"
        active_p.className="text"
        document.getElementById("myDIV").appendChild(active_box);
        document.getElementById("active_box").appendChild(active_p);*/
			var X=0,Y=0;
			var x=X+"px",y=Y+"px";
			var width1=($("#myDIV").width()-30)/3,height1=200;
			
			var width=width1+"px",height=height1+"px"
			var w1= '<div  id ="page1" class="page_container" style="top:'+y+';left:'+x+';width:'+width+';height:'+height+'" >'
			 	+'<div class="ibanner" style="width:80%; height:70%;" id="ibanner1">'
			 	+'<div class="ibanner_pic" id="ibanner_pic1">'
			 	+'<a href="#" id="picLi_10"><img style="width:100%; height:100%;" src="../img/3.jpg" alt="" /></a>'
			 	+'</div>'
			 	+'</div>'
			 	+'<div  class="col-center-block">'
			 	//+'<em class="btnleft "style="display:inline-block" onclick="reduceSpeed()"></em>'
			 	//+'<em id="buttonPlay" class="btn_middle "style="display:inline-block" ></em>'
			 	//+'<em class="btnright "style="display:inline-block"  onclick="increateSpeed()"></em>'
			 	+'<img id="backward" class="img_style"src="../img/left.jpg"></img>'
			 	+'<img id="backward2" class="img_style"src="../img/play.jpg"></img>'
			 	+'<img id="backward3" class="img_style"src="../img/right.jpg"></img>'
			 	+'<input type="range" style="display:inline-block" id="range_speed" min="0" max="100" class="range" value="0"oninput="changeSpeed()" />'
			 	+'<input type="button"style="display:inline-block;font-size:1px" value="00:00:00" id="timeStart" class="time" />'
			 	+'<input type="button"style="display:inline-block;font-size:1px" value="00:00:00" id="timeEnd" class="timeEnd" /> '

			 	
			 	//+'<div class="col-center-block-child-end"></div>'
			 	+'</div>'
			 	+'</div>'
			 	
			 	$("#myDIV").append(w1);
			//   $("#page1").css({ "top":x, "left": y});
			var X = $('#page1').position().top;
			var Y = $('#page1').position().left;
			var X1 = $('#page1').offset().top;
			var Y1 = $('#page1').offset().left;
			console.log(X)
			console.log(Y)
			console.log(X1)
			console.log(Y1)
			//$("#page1").offset({ top: X, left: Y})
			
        var rect=new rectar(name,X,Y,width1,height1,"page1");
        // 把它保存在数组中
        rects.push(rect);
		}else {//用于新增容器，暂且不考虑滚动条宽度
			var listX=[],listY=[];
			for(var i=0;i<rects.length;i++){
				
				listY.push(rects[i].y);
			}
			var maxY = Math.max.apply(null,listY);//寻找最靠后的那个值
			console.log(maxY);
			for(var i=0;i<rects.length;i++){
				if(rects[i].y==maxY){
					listX.push(rects[i].x);
				
				}
			}
			var maxX=Math.max.apply(null,listX);
			if(maxX==($("#myDIV").width()-30)/3*2+20){
				var X=0,Y=maxY+210;
			}else{
				var X=maxX+($("#myDIV").width()-30)/3+10,Y=maxY;
			}
			//var X=($("#myDIV").width()-30)/3+10,Y=0;
			
			var x=X+"px",y=Y+"px"
			var width1=($("#myDIV").width()-30)/3,height1=200;
			var number=Number(rects.length)+1;
			var id="page"+number;
			
			var width=width1+"px",height=height1+"px";
			var w1= '<div  id ='+id+' class="page_container" style="top:'+y+';left:'+x+';width:'+width+';height:'+height+'">'
			 	+'<div class="ibanner" style="width:80%; height:70%;" id="ibanner1">'
			 	+'<div class="ibanner_pic" id="ibanner_pic1">'
			 	+'<a href="#" id="picLi_10"><img style="width:100%; height:100%;" src="../img/3.jpg" alt="" /></a>'
			 	+'</div>'
			 	+'</div>'
			 	+'<div  class="col-center-block">'
			 	//+'<em class="btnleft "style="display:inline-block" onclick="reduceSpeed()"></em>'
			 	//+'<em id="buttonPlay" class="btn_middle "style="display:inline-block" ></em>'
			 	//+'<em class="btnright "style="display:inline-block"  onclick="increateSpeed()"></em>'
			 	+'<img id="backward" class="img_style"src="../img/left.jpg"></img>'
			 	+'<img id="backward2" class="img_style"src="../img/play.jpg"></img>'
			 	+'<img id="backward3" class="img_style"src="../img/right.jpg"></img>'
			 	+'<input type="range" style="display:inline-block" id="range_speed" min="0" max="100" class="range" value="0"oninput="changeSpeed()" />'
			 	+'<input type="button"style="display:inline-block;font-size:1px" value="33:00:00" id="timeStart" class="time" />'
			 	+'<input type="button"style="display:inline-block;font-size:1px" value="00:00:00" id="timeEnd" class="timeEnd" /> '

			 	
			 	//+'<div class="col-center-block-child-end"></div>'
			 	+'</div>'
			 	+'</div>'
			 	
			 	$("#myDIV").append(w1);
			if($("#myDIV").height()<$(document).height())
			{$("#myDIV").height($(document).height());}
			 var rect=new rectar(name,X,Y,width1,height1,id);
			 console.log(rect)
		        rects.push(rect);
		}
        $('.page_container').resizable({
        	handles:'se', //'e'是east，允许拖动右侧边框的意思
        	maxWidth:($("#myDIV").width()-30)/3,
        	minWidth:200,
        	minHeight:100,
        	maxHeight:200,
        	 //grid: 10,
        	 containment: "#myDIV",
        	 aspectRatio:(($("#myDIV").width()-30)/3)/200,
        	//resize方法在#left大小改变后被执行
        	resize:function(event,ui){ //动态调整时间字体
        		console.log(event.clientX)
        		console.log(ui.originalPosition)
        		console.log(ui.position)
        		if(ui.size.width<650&&ui.size.width>600){
        			$("#timeStart").css({"font-size":"10px"});
        			$("#timeEnd").css({"font-size":"10px"});
        		}else if(ui.size.width<600&&ui.size.width>550){
        			$("#timeStart").css({"font-size":"8px"})
        			$("#timeEnd").css({"font-size":"8px"})
        		}else if(ui.size.width<550&&ui.size.width>500){
        			$("#timeStart").css({"font-size":"6px"})
        			$("#timeEnd").css({"font-size":"6px"})
        		}else if(ui.size.width<500&&ui.size.width>400){
        			$("#timeStart").css({"font-size":"1px"})
        			$("#timeEnd").css({"font-size":"1px"})
        		}
        		
        	}
        })
       
        $(".page_container").draggable({
        	
        	//cursor: "move",
        	containment: "parent",
        	scroll: false,
        	grid: [ ($("#myDIV").width()-30)/3+10, 200+10],
        	drag( event, ui ){
        		
        	},
        	stop:function(event,ui){
        	console.log(event.clientX)
        	console.log(ui.originalPosition)
        	console.log(ui.position)
        	
        	for(var i=0;i<rects.length;i++){
				if(rects[i].y==ui.position.top&&rects[i].x==ui.position.left){//目标位置
					var id=rects[i].id;
					$("#"+id+"").css({ "left":ui.originalPosition.left+"px", "top": ui.originalPosition.top+"px"});
					rects[i].x=ui.originalPosition.left;
					rects[i].y=ui.originalPosition.top;
					console.log(rects)
					for(var j=0;j<rects.length;j++){
						if(rects[j].x==ui.originalPosition.left&&rects[j].y==ui.originalPosition.top&&i!=j){
							rects[j].x=ui.position.left;
							rects[j].y=ui.position.top;
							console.log(rects)
						}
					
					}
					break;
	        	}else if(rects[i].y==ui.originalPosition.top&&rects[i].x==ui.originalPosition.left){
	        		rects[i].x=ui.position.left;
	        		rects[i].y=ui.position.top;
	        		console.log(rects)
	        		//break;
	        	}
				
			}
        	
		}
        	
        });
        $(".box").draggable({
        	containment: "#myDIV",
        	scroll: false,
        	//grid: [ $("#myDIV").width()/3, $("#myDIV").width()/3] ,
        	stop:function(event,ui){
        	console.log(event.clientX)
        	}}
        );
       /* active_box = null;
		    e =  window.event;
		    // startX, startY 为鼠标点击时初始坐标
		    // diffX, diffY 为鼠标初始坐标与 box 左上角坐标之差，用于拖动
		    var startX, startY, diffX, diffY;
		    // 是否拖动，初始为 false
		    var dragging = false;
		          
		    // 鼠标按下
		    document.getElementById("myDIV").onmousedown = function(e) {
		        startX = e.pageX;
		        startY = e.pageY;
		          
		        // 如果鼠标在 box 上被按下
		        if(e.target.className.match(/box/)) {
		            // 允许拖动
		            dragging = true;
		          
		            // 设置当前 box 的 id 为 moving_box
		            if(document.getElementById("moving_box") !== null) {
		            	console.log(8)
		                document.getElementById("moving_box").removeAttribute("id");
		            }
		            e.target.id = "moving_box";
		          
		            // 计算坐标差值
		            diffX = startX - e.target.offsetLeft;
		            diffY = startY - e.target.offsetTop;
		        }
		        else {
		            // 在页面创建 box
		            var active_box = document.createElement("div");
		            active_box.id = "active_box";
		            active_box.className = "box";
		            active_box.style.top = startY + 'px';
		            active_box.style.left = startX + 'px';
		            document.body.appendChild(active_box);
		            active_box = null;
		        }
		    };
		           
		    // 鼠标移动
		    document.onmousemove = function(e) {
		        // 更新 box 尺寸
		        if(document.getElementById("active_box") !== null) {
		        	console.log(33)
		            var ab = document.getElementById("active_box");
		            ab.style.width = e.pageX - startX + 'px';
		            ab.style.height = e.pageY - startY + 'px';
		        }
		           
		        // 移动，更新 box 坐标
		        if(document.getElementById("moving_box") !== null && dragging) {
		        	console.log(55)
		            var mb = document.getElementById("moving_box");
		            mb.style.top = e.pageY - diffY + 'px';
		            mb.style.left = e.pageX - diffX + 'px';
		        }
		    };
		           
		    // 鼠标抬起
		    document.onmouseup = function(e) {
		        // 禁止拖动
		        dragging = false;
		        if(document.getElementById("active_box") !== null) {
		            var ab = document.getElementById("active_box");
		            ab.removeAttribute("id");
		            // 如果长宽均小于 3px，移除 box
		            if(ab.offsetWidth < 3 || ab.offsetHeight < 3) {
		                document.body.removeChild(ab);
		            }
		        }
		    };*/
		
		

		    
	}	
	/*window.onload = function() {
        canvas = document.getElementById("canvas");
        context = canvas.getContext("2d");
        addRandomRect();
        canvas.onmousedown = canvasClick;
        canvas.onmouseup = stopDragging;
        canvas.onmouseout = stopDragging;
        canvas.onmousemove =dragRect;
  };
  // canvas 矩形框集合
  var rects=[];
   function rectar(x,y,width,height){
          this.x = x;
          this.y = y;
          this.width = width;
          this.height = height;
          this.isSelected = false;
      };
      function drawRect() {
          // 清除画布，准备绘制
          context.clearRect(0, 0, canvas.width, canvas.height);

          // 遍历所有矩形框
          for(var i=0; i<rects.length; i++) {
            var rect = rects[i];

            // 绘制矩形
            //context.strokeStyle="#FF0000";//红色
            context.fillStyle="#0000ff";
            //context.strokeRect(rect.x,rect.y,rect.width,rect.height,rect.color);//strokeRect() 方法绘制矩形（不填色）。笔触的默认颜色是黑色。
            context.fillRect(rect.x,rect.y,rect.width,rect.height,rect.color);//fillRect() 方法绘制“已填色”的矩形。默认的填充颜色是黑色
            if (rect.isSelected) {
              context.lineWidth = 50;
            }
            else {
              context.lineWidth = 10;
            }
          }
        }
      function addRandomRect() {
          var x=10;
          var y=10;
          var width=100;
          var height=100;
           // 创建一个新的矩形对象
           var rect=new rectar(x,y,width,height);

           // 把它保存在数组中
           rects.push(rect);
           // 重新绘制画布
          drawRect();
      };
      var SelectedRect;
      var x1;
      var y1;
      var right=false;
      var widthstart,widthend;
      var heightstart,heightend;

  function canvasClick(e) {
        // 取得画布上被单击的点
        var clickX = e.pageX - canvas.offsetLeft-$("canvas").parent().offset().left;
        var clickY = e.pageY - canvas.offsetTop-$("canvas").parent().offset().top;
    
        // 查找被单击的矩形框
        for(var i=rects.length-1; i>=0; i--) {
          var rect = rects[i];

              widthstart=rect.x;
              widthend=rect.x+rect.width;

              heightstart=rect.y;
              heightend=rect.y+rect.height;
              console.log("widthstart",widthstart)
              console.log("widthend",widthend)
              console.log("heightstart",heightstart)
              console.log("heightend",heightend)
              console.log("clickX",clickX)
               console.log("clickY",clickY)
          // 判断这个点是否在矩形框中
          if ((clickX>widthstart)&&(clickX<widthend)&&(clickY>heightstart)&&(clickY<heightend)) {
            console.log(clickX);
            // 清除之前选择的矩形框
            
            if (SelectedRect != null) SelectedRect.isSelected = false;
            SelectedRect = rect;
            x1=clickX-SelectedRect.x;
            y1=clickY-SelectedRect.y;
            //选择新圆圈
            rect.isSelected = true;

            // 使圆圈允许拖拽
            isDragging = true;

            //更新显示
            drawRect();
            //停止搜索
            break;
          };
          
            //设置拉伸的界限。

              if ((clickX=widthend)||(clickX=widthstart)||(clickY=heightend)&&(clickY=heightstart)) 
               {
               SelectedRect = rect;
                right=true; 
                break;
                }
        }
    
  }
  function dragRect(e) {
	  
      // 判断矩形是否开始拖拽
      if (isDragging == true) {
        // 判断拖拽对象是否存在
        if (SelectedRect != null) {
          // 取得鼠标位置
          var x = e.pageX - canvas.offsetLeft-$("canvas").parent().offset().left;
          var y = e.pageY - canvas.offsetTop-$("canvas").parent().offset().top;
          // 将圆圈移动到鼠标位置
          SelectedRect.x= x-x1;
          SelectedRect.y= y-y1;

         // 更新画布
         drawRect();
        }
      }
      //判断是否开始拉伸
      if (right) {
    	 
      //设置拉伸最小的边界
        if ((e.pageX - canvas.offsetLeft-$("canvas").parent().offset().left-SelectedRect.x)>50) {
           SelectedRect.width=e.pageX - canvas.offsetLeft-SelectedRect.x-$("canvas").parent().offset().left;
        }
        else {
          SelectedRect.width=50;
        }
       console.log(SelectedRect.width);
       if((e.pageY - canvas.offsetTop-$("canvas").parent().offset().top-SelectedRect.y)>50){
         SelectedRect.height=e.pageY - canvas.offsetTop-SelectedRect.y;

       }
       else {
         SelectedRect.height=50;
       }
        drawRect();
      }
    };
    var isDragging = false;
    function stopDragging() {
      isDragging = false;
      right=false;
    };
   function clearCanvas() {
     // 去除所有矩形
      rects = [];
    // 重新绘制画布.
    drawCircles();
    }
*/