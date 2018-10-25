
//主界面用户表格回显
$(function() {

	//初始加载	
	newsReady();
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
			//alert(JSON.stringify(data2));
			initNewsGrid(data2);
			
		}, 
		error: function(){
			alert("新闻回显失败！")
		}
		}); 
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


//修改新闻-检查标题
function EcheckTitle() {
	var abstract = $("#EnewsTitle").val(); 
	var len=abstract.length;
	//alert(len);
	if (len==40) {
		$("#msg_EnewsTitle").html("输入标题长度为40个字符，已达上限");
        $("#msg_EnewsTitle").css("color", "red");
	}
	else {
		$("#msg_EnewsTitle").html("输入标题不超过40个字符，符合要求");
        $("#msg_EnewsTitle").css("color", "green");
	}
}

//修改新闻-检查摘要
function EcheckAbstract() {
	var abstract = $("#EnewsAbstract").val(); //获取上传的摘要
	var len=abstract.length;
	//alert(len);
	if (len==100) {
		$("#msg_EnewsAbstract").html("输入摘要长度为100个字符，已达上限");
        $("#msg_EnewsAbstract").css("color", "red");
	}
	else {
		$("#msg_EnewsAbstract").html("输入摘要不超过100个字符，符合要求");
        $("#msg_EnewsAbstract").css("color", "green");
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
	//alert(JSON.stringify(formData));
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
	//alert($("#EnewsId").val());
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
				location.reload();
			    //newsReady();
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

