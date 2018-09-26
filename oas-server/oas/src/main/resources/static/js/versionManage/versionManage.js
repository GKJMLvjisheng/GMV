/**
 * 
 */
//新闻管理界面创建bootstrapTable
document.write("<script language=javascript src='js/deleteConfirm.js'></script>");
var check1=1;
var check2=1;
var check3=1;
//主界面用户表格回显
$(function() {

	//初始加载	
	versionReady();
	versionCode.setAttribute("maxlength",40);
	
	
});

function versionReady(){
	
    $('#versionGrid').bootstrapTable('destroy');
	var data;
	 $.ajax({
		
		url: "/api/v1/userCenter/selectAllApps",
	    contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		async : false,
		success: function(res) {
		//alert(JSON.stringify(res));
		if(res.code==0)
			{data=res.data;}
			
		else{alert("回显失败！");}
			
		}, 
		error: function(){
			alert("回显失败！")
		}
		}); 
	 initVersionGrid(data);
}

//检查图片格式是否符合要求
$(function(){
$('#versionFile').change(function(e) {
	var animateimg = $("#versionFile").val();
	var imgarr=animateimg.split('\\'); //分割
	var myimg=imgarr[imgarr.length-1]; // 获取图片名
	//alert("picName="+myimg);
	var houzui = myimg.lastIndexOf('.'); //获取 . 出现的位置
	var ext = myimg.substring(houzui, myimg.length).toUpperCase();  //切割 . 获取文件后缀
	console.log(ext);
	var file = $("#versionFile").get(0).files[0]; //获取上传的文件
	var fileSize = file.size;           //获取上传的文件大小
	//alert("fileSize="+fileSize/1000+"KB");
	var maxSize = 10485760;//最大10MB
	check2=1;
	if(ext !='.APK'){
		
	    $("#msg_versionFile").html("app类型错误,请上传.apk后缀的文件");
	    $("#msg_versionFile").css("color", "red");
	    check2=0;
	    return;
	   
	}
	//else if(parseInt(fileSize) >= parseInt(maxSize)){
	//    $("#msg_versionFile").html("上传的文件不能超过1MB");
	//    $("#msg_versionFile").css("color", "red");
	//    
	//}
	else {
		$("#msg_versionFile").html("上传的版本格式符合要求");
	    $("#msg_versionFile").css("color", "green");
	    
	}
});
	$('#EversionFile').change(function(e) {
	
		var animateimg = $("#EversionFile").val();
		var imgarr=animateimg.split('\\'); //分割
		
		var myimg=imgarr[imgarr.length-1]; // 获取图片名
		//alert("picName="+myimg);
		var houzui = myimg.lastIndexOf('.'); //获取 . 出现的位置
		var ext = myimg.substring(houzui, myimg.length).toUpperCase();  //切割 . 获取文件后缀
		console.log(ext);
		var file = $('#EversionFile').get(0).files[0]; //获取上传的文件
		var fileSize = file.size;           //获取上传的文件大小
		//alert("fileSize="+fileSize/1000+"KB");
		var maxSize = 10485760;//最大10MB
		check3=1;
		if(ext !='.APK'){
			
		    $("#msg_EversionFile").html("app类型错误,请上传.apk后缀的文件");
		    $("#msg_EversionFile").css("color", "red");
		    check3=0;
		    return;
		   
		}
		//else if(parseInt(fileSize) >= parseInt(maxSize)){
		//    $("#msg_versionFile").html("上传的文件不能超过1MB");
		//    $("#msg_versionFile").css("color", "red");
		//    
		//}
		else {
			$("#msg_EversionFile").html("上传的版本格式符合要求");
		    $("#msg_EversionFile").css("color", "green");
		    
		}
	});

});



function validateNumber(num)
{
 
  var reg = /^\d+(?=\.{0,1}\d+$|$)/;
	
  if(reg.test(num)) return true;
  return false ;  
}
//检查输入版本号
function checkVersionCode() {
	var abstract = $("#versionCode").val(); 
	var len=abstract.length;
	//alert(len);
	check1=1;
	if(!validateNumber(abstract))
		{
		
		check1=0;
		$("#msg_versionCode").html("输入版本号由0-9十位数字组成");
        $("#msg_versionCode").css("color", "red");
		
		return;}
	if (len==40) {
		$("#msg_versionCode").html("输入版本号长度为40个字符，已达上限");
        $("#msg_versionCode").css("color", "red");
	}
	else {
		$("#msg_versionCode").html("");
        $("#msg_versionCode").css("color", "green");
	}
}

//检查版本状态
function checkVersionStatus() {
	var abstract = $("#versionStatus").val(); 
	var len=abstract.length;
	//alert(len);
	if (len==40) {
		$("#msg_versionStatus").html("输入版本状态长度为40个字符，已达上限");
        $("#msg_versionStatus").css("color", "red");
	}
	else {
		$("#msg_versionStatus").html("");
        $("#msg_versionStatus").css("color", "green");
	}
}

//新增版本
function addVersion(){
	
    
	if($("#versionCode").val()==="")
		{
		alert("版本号不能为空");
		return;
		}

	if(check1===0)
		{alert("请输入正确的版本号");
		return;}
	 var versionStatus = document.getElementsByName("versionStatus");
	 
	 var status=null;
	 for(var i = 0; i < versionStatus.length; i++)
	    {

	        if(versionStatus[i].checked)

	        {
	        status=versionStatus[i].value;}

	    }
	 console.log("status"+status);
	 if(status==null)
		 {alert("请选择版本状态");
		 return;}
	 var animateimg = $("#versionFile").val(); //获取上传的图片名 0\1\2
	    //alert("PicPath="+animateimg);
		if (animateimg=="") {
			$("#msg_versionFile").html("版本上传不能为空");
	        $("#msg_versionFile").css("color", "red");
	        return;
		}
		//checkVersionFile(animateimg);
		if(check2===0)
			{return;}
		var formData = new FormData();
		var version_file = document.getElementById("versionFile");
		var fileobj = version_file.files[0];
		 
		formData.append("file",fileobj);//添加fileobj到formData的键file中
		formData.append("versionCode", $("#versionCode").val());
		formData.append("versionStatus", status);
	//alert(JSON.stringify(formData));
	$.ajax({
		url:"/api/v1/userCenter/upLoadApp",
		data:formData,
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		
		processData : false,
		contentType : false,
		async:false,

		success:function(res){	
			//alert(JSON.stringify(res));
				if(res.code==0)
				{document.getElementById("tipContent").innerText="上传成功";
				$("#Tip").modal('show');
				$("#addVersionModal").modal('hide');}
				else{
				document.getElementById("tipContent").innerText="上传失败";
				$("#Tip").modal('show');
				$("#addVersionModal").modal('hide');
				
				 
				//$("#newsGrid").bootstrapTable('refresh');	
				 }						
		},
		error:function(){
			document.getElementById("tipContent").innerText="上传失败";
			$("#Tip").modal('show');
			$("#addVersionModal").modal('hide');

		},
	});
	resetAddModal();
	versionReady();
}


//点击取消后清空表单中已写信息
function resetAddModal(){
	//document.getElementById("addVersionForm").reset();
	 $("#addVersionForm").find('textarea,input[type=file],select').each(function() {
		          $(this).val('');
		      });
	 $("input[type=radio]").prop("checked",false);
	 $("span").html("");
//	 $("#updateVersionForm").find('input[type=text],select,input[type=file],span').each(function() {
//         $(this).val('');
//     });
	
	//location.reload();
}
function resteUpdate(){
	$("#updateVersionForm").find('textarea,input[type=file],select').each(function() {
        $(this).val('');
    });
	$("input[type=radio]").prop("checked",false);
	$("span").html("");
}

function updateVersion(){

	if(check3===0)
		{return;}
	var formData = new FormData();
	var version_file = document.getElementById("EversionFile");//获取类型为文件的输入元素
	//alert(img_file);
	var fileobj = version_file.files[0];//使用files获取文件
	//alert(fileobj);
	//alert($("#EnewsId").val());
	formData.append("file",fileobj);//添加fileobj到formData的键file中
	formData.append("uuid", $("#EversionId").val());
	formData.append("versionCode", $("#EversionCode").val());
	
	formData.append("appUrl", $("#EversionPath").val());
	 var versionStatus = document.getElementsByName("EversionStatus");
	 var status=null;
	 for(var i = 0; i < versionStatus.length; i++)
	    {

	        if(versionStatus[i].checked)

	        {
	        status=versionStatus[i].value;}

	    }
	 console.log("status"+status);
	 formData.append("versionStatus", status);
	
	$.ajax({
		url:"/api/v1/userCenter/updateApp",
		data:formData,
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,
		
		processData : false,
		contentType : false,
		async:false,

		success:function(res){	
			//alert(JSON.stringify(res));
			
			if(res.code==0){
				alert("修改成功");
				
				versionReady();
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
	resteUpdate();
	
	}

function initVersionGrid(data) {	

	$("#versionGrid").bootstrapTable({

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",

		dataType:"json",

		pagination:true,//显示分页条：页码，条数等

		striped:true,//隔行变色

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"uuid",//Indicate an unique identifier for each row

		toolbar:"#toolbar",//工具栏
		sortName: 'ID', // 要排序的字段
	    sortOrder: 'asc', // 排序规则

		data:data,
		columns : [{
            title: 'ID',
            field: 'index',
            align: 'center',
			valign: 'middle',
            formatter: formatterIndex
            
        },
			{

			title : "版本号",

			field : "versionCode",
			align: 'center',
			valign: 'middle',
			//width:  '120px',

		}, {

			title : "版本状态",

			field : "versionStatus",
			align: 'center',
			valign: 'middle',
			//width:  '200px',

		},
		{

			title : "版本下载地址",

			field : "appUrl",
			align: 'center',
			valign: 'middle',
			//width:  '140px',
			formatter: versionPath

		}, 
		{

			title : "创建时间",

			field : "created",
			align: 'center',
			valign: 'middle',
			//width:  '90px',

		},
		{

			title : " 操作",
			
			field : "uuid",
			align: 'center',
			valign: 'middle',
			//width:  '90px',
			formatter: actionFormatter
		}],
		
		search : true,//搜索
        searchOnEnterKey : true,
		clickToSelect: false,         
	});
}

function formatterIndex(value, row, index){
	   var value="";
    var pageSize=10;
  	
    //获取当前是第几页        
    var pageNumber=1;       
    //返回序号，注意index是从0开始的，所以要加上1         
     value=pageSize*(pageNumber-1)+index+1;
     return value;
}
function versionPath(value, row, index) {
	var result = "";
	
	var versionPath = value; 
	var imgarr=versionPath.split('/'); //分割
	var myimg=imgarr[imgarr.length-1]; // 获取图片名
	
    result += "<a href='javascript:;' onclick=\"displayQrcode('" + versionPath + "')\">"+myimg+"</a>";
    //<img src="+versionPath+" width='120px' height='80'>
    			//"<span>"+newsPicturePath+"</span>";      
    return result;
}

//function actionFormatter(value, row, index) {	
//	var result = "";
//	var newsUrl = value;  
//	result +="<a href="+newsUrl+ ">"+newsUrl+"</a>";
//    return result;
//}

function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        
        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"editVersionById('" + id + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"deleteVersionById('" + id + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";

        return result;
}
 
function ViewNewsById(id){	

		//获取选中行的数据		
		var rows=$("#versionGrid").bootstrapTable('getRowByUniqueId', id);
		$('#QnewsId').val(rows.newsId);
		$('#QnewsTitle').val(rows.newsTitle);
		$('#QnewsAbstract').val(rows.newsAbstract);
		$('#QnewsUrl').val(rows.newsUrl);
		$('#QnewsPicturePath').val(rows.newsPicturePath);
		$("#qureyNewsModal").modal("show");
		

}	
function editVersionById(id){
    
    //获取选中行的数据
    var rows=$("#versionGrid").bootstrapTable('getRowByUniqueId', id);
	$('#EversionId').val(rows.uuid);  
    $('#EversionCode').val(rows.versionCode);
    if(rows.versionStatus=="体验版")
    	{$("#EversionStatus1").prop("checked",true)}
    if(rows.versionStatus=="开发版")
    	{$("#EversionStatus2").prop("checked",true)}
    if(rows.versionStatus=="稳定版")
		{$("#EversionStatus3").prop("checked",true)}
    $('#EversionPath').val(rows.appUrl);
	//$('#EnewsUrl').val(rows.newsUrl);
	
	//$('#versionFile').val(rows.versionFile); 
//	file_input_obj=document.getElementById("EversionFile");
//	file_input_obj.outerHTML=file_input_obj.outerHTML.replace(/(value=111\").+\"/i,"$1\"11");
//	
//	versionCode.setAttribute("readonly", "readonly" );
    $("#updateVersionModal").modal("show");           
  }


function deleteVersionById(id)
{ 
	 Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }

	data={"uuid":id};
	//alert(JSON.stringify(data));
	$.ajax({

		url:"/api/v1/userCenter/deleteApp",
		data: JSON.stringify(data),
		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		type: 'post',
		cache: false,

		success:function(res){
			
			if(res.code==0){
				$("#versionGrid").bootstrapTable('removeByUniqueId', id);
				alert("删除成功！");
			}
			else{
				alert("删除失败");
				}
			},
	
	     error:function(){
				alert("删除失败！");
			}
	});
});
}
function displayQrcode(value) {
	//$("#QRuserinfocode").addClass("selected");
    //var userName=$("#userName").val();
	//alert(value);
    var info=value;
    var qrcode=$('#qrcodeCanvas').qrcode(info).hide();
       var canvas=qrcode.find('canvas').get(0);
       $('#image').attr('src',canvas.toDataURL('img/1.jpg'));        
      
          $("#qrcode").fadeIn("slow");
      $("#qrcode").click(function() {
          $("#qrcode").fadeOut("slow");
          //$("#QRuserinfocode").removeClass("selected");
      })
  }