/**
 * 用户管理js文件
 */
document.write("<script language=javascript src='js/news/newsManageTable.js'></script>");

//主界面用户表格回显
$(function() {

	//初始加载	
	newsReady();
	//newsIndexReady();
});

function newsReady(){
	
    $('#newsGrid').bootstrapTable('destroy');
	var data2;
	 $.ajax({
		
		url: "/api/v1/userCenter/selectAllNews",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		success: function(res) {
			//alert(JSON.stringify(res));
			data2=res.data.list;
			alert(JSON.stringify(data2));
			initNewsGrid(data2);
		}, 
		error: function(){
			
		}
		}); 
	 
	 //initNewsGrid(data2);
}

//检查图片格式是否符合要求
function checkPic() {
	var animateimg = $("#newsPicturePath").val(); //获取上传的图片名 0\1\2
    //alert("PicPath="+animateimg);
	if (animateimg=="") {
		$("#msg_newsPicPath").html("图片不能为空");
        $("#msg_newsPicPath").css("color", "red");
        return;
	}
    var imgarr=animateimg.split('\\'); //分割
    var myimg=imgarr[imgarr.length-1]; // 获取图片名
    //alert("picName="+myimg);
    var houzui = myimg.lastIndexOf('.'); //获取 . 出现的位置
    var ext = myimg.substring(houzui, myimg.length).toUpperCase();  //切割 . 获取文件后缀
    
    var file = $('#newsPicturePath').get(0).files[0]; //获取上传的文件
    var fileSize = file.size;           //获取上传的文件大小
    //alert("fileSize="+fileSize/1000+"KB");
    var maxSize = 10485760;//最大10MB
    
    if(ext !='.PNG' && ext !='.GIF' && ext !='.JPG' && ext !='.JPEG' && ext !='.BMP'){
        $("#msg_newsPicPath").html("图片类型错误,请上传.png,.gif,.jpg,.jpeg,.bmp类型的图片");
        $("#msg_newsPicPath").css("color", "red");
       
    }
    else if(parseInt(fileSize) >= parseInt(maxSize)){
        $("#msg_newsPicPath").html("上传的文件不能超过1MB");
        $("#msg_newsPicPath").css("color", "red");
        
    }
    else {
    	$("#msg_newsPicPath").html("上传的图片符合要求");
        $("#msg_newsPicPath").css("color", "green");
        
    }
}
//新增新闻
function addNews(){		
    
	var formData = new FormData();
	var img_file = document.getElementById("newsPicturePath");
	var fileobj = img_file.files[0];
	 
	formData.append("file",fileobj);//添加fileobj到formData的键file中
	formData.append("newsTitle", $("#newsTitle").val());
	formData.append("newsAbstract", $("#newsAbstract").val());
	formData.append("newsUrl", $("#newsUrl").val());
	
	$.ajax({
		url:"/api/v1/userCenter/addNews",
		data:formData,
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		
		processData : false,
		contentType : false,
		async:false,

		success:function(res){	
				
				document.getElementById("tipContent").innerText="上传成功";
				$("#Tip").modal('show');
				$("#addNewsModal").modal('hide');
				 newsReady();
				$("#newsGrid").bootstrapTable('refresh');
				
			
		},
		error:function(){
			document.getElementById("tipContent").innerText="上传失败";
			$("#Tip").modal('show');
			$("#addNewsModal").modal('hide');

		},
	});
    

	   
}




//点击取消后清空表单中已写信息
function resetAddModal(){
	//document.getElementById("addNewsForm").reset();
	location.reload();
}


function updateNews(){
	  
	
	var formData = new FormData();
	var img_file = document.getElementById("AnewsPicturePath");//获取类型为文件的输入元素
	//alert(img_file);
	var fileobj = img_file.files[0];//使用files获取文件
	//alert(fileobj);
	formData.append("file",fileobj);//添加fileobj到formData的键file中
	formData.append("newsId", $("#EnewsId").val());
	formData.append("newsTitle", $("#EnewsTitle").val());
	formData.append("newsAbstract", $("#EnewsAbstract").val());
	formData.append("newsUrl", $("#EnewsUrl").val());
	formData.append("newsPicturePath", $("#EnewsPicturePath").val());
	
	
	$.ajax({
		url:"/api/v1/userCenter/updateNews",
		data:formData,
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		
		processData : false,
		contentType : false,
		async:false,

		success:function(res){	
			
			if(res.code==0){
				alert("success");
			    newsReady();
			}
			else{
				alert("修改失败");
				}			
			
		},
		error:function(){
			alert("修改失败");

		},
	});
	
	}

//批量删除
//function deleteNews(){
//  var rows = $("#newsGrid").bootstrapTable("getSelections");
//    var ids = [];
//  var len = rows.length;
//  //alert(len);
//    if (len== 0) {
//        alert("请先选择要删除的记录!");
//        return;
//    }
//    Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
//		if (!e) {
//		  return;
//		 }
//  for(var i=0;i<len;i++){
//    ids.push(rows[i]['newsId']);
//
//  }
//	$.ajax({
//
//		url:"/doDeleteNews",
//
//		dataType:"json",
//
//		traditional: true,//属性在这里设置
//
//		method:"post",
//
//		data:{
//
//			"ids":ids
//		},
//
//		success:function(data){
//
//            DeleteByIds();
//            
//            alert("删除成功！");
//		     },
//
//		error:function(){
//			alert("删除失败！")
//		}
//
//	});
//    });
//}
//function DeleteByIds(){
//
//    var ids = $.map($('#newsGrid').bootstrapTable('getSelections'),function(row){
//        return row.newsId;
//    });
//
//    $('#newsGrid').bootstrapTable('remove',{
//        field : 'newsId',
//        values : ids
//    });
//}
function newsIndexReady() {
	alert(1);
	   $.ajax({
	    url: "/api/v1/userCenter/selectAllNews",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
	    success: function(res) {
	    	//alert(JSON.stringify(res),);
	    	var len=res.data.list.length;
	    	
	    	//var picturePath=[len];
	    	
	      if (len>0) {
	        list="";
	        for(i=0;i<len;i++){

	          list+=
	            "<div class='new-list-item clearfix'>"+
	            "<div class='col-xs-1'></div>"+
	            "<div class='col-xs-6'>"+
	            "<a href="+res.data.list[i].newsUrl+ " class='title'>"+res.data.list[i].newsTitle+"</a>"+
	              "<div class='abstract'>"+res.data.list[i].newsAbstract+"</div>"+
	              "</div>"+
	              "<div class='col-xs-4'>"+
	              						"<img  src="+res.data.list[i].newsPicturePath+ ">"+
	                                "</div></div>";
	        }
	      } 
	      else {
	        alert("数据库中没有新闻数据");
	      }
	      document.getElementById("Grid").innerHTML=list;
	    }, 
	    error: function(){
	    	alert("新闻首页回显失败！");
	    }}); 
	}
	  


