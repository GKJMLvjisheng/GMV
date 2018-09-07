/**
 * 
 */
var check1 = 0;
var check2 = 0;
var check3 = 0;
var check4 = 0;
var check5 = 0;
//$("#CreateCheckCode").trigger("click");



//判断用户名是否存在
function checkName() {
  var userame = $("#pwdUserName").val();
  
  if (userName != "") {
    var reg = /^[\w]{1,}$/;
    if (!reg.test(userName)) {
      $("#msgN").html("用户名由数字、字母、下划线组成");
      $("#msgN").css("color", "#ff0000");
      
      check1 = 0;
      return check1;
    } else {
      var data = {
        "userName" : userName,
      };
      
      $.ajax({
        url : "/doCheckUserName",
        type : "POST",
        dataType : 'json',
        async : false,
        data : data,
        success : function(data) {
          //根据判断提示用户
          if (data.state == true) {
            $("#msgN").html("用户名不存在！");
            $("#msgN").css("color", "#ff0000");
            check1 = 0;
            return check1;
          } else {
            $("#msgN").html("该用户名可用");
            
            $("#msgN").css("color", "#00ff00");
            check1 = 1;
            return check1;
          }
        },
        error : function() {
          alert('检查用户是否存在发生错误');
        }
      });

    }
  } else {
    $("#msgN").html("用户名不能为空!");
    $("#msgN").css("color", "#ff0000");
  }
}
/*$(function(){// 获取input输入框的光标焦点事件，点击当前输入框进入该方法
	$("#checkCode").focus(function(){
	    $(this).removeClass("blur");

	});
	// 点击输入框外的其它任何地方移除焦点
	$("#checkCode").blur(function(){
	    
	    $(this).addClass("blur");
	    var code = $(this).val();
	    // 由于ajax异步请求，在还没返回json的时候该方法已经执行完毕
	    var $that = $(this); 
	    alert(code);
	    // 当输入的文本长度符合需求时，可调用ajax请求验证
	    if(code.length == 4){
	        $.ajax({
	            type : "post",
	            url  : "/contrastCode",
	            data : {"code" : code},
	            dataType : "json",
	            success : function(flag){
	                // 判断后端对比验证码后是否一致
	                 if(flag == true){
	                     alert("验证码成功！");
	                    
	                     $that.removeClass("blur");
	                 } else{
	                   
	                     alert("验证码错误！");
	                 }
	            },
	            error: function(){
	                
	                alert('发生错误');
	                
	            }
	        });
	    }
	    else{
	         alert("验证码错误！");   
	    }
	});
	});*/
//检查log验证码正确与否
function checkIdentifyFirst()
{var identifyCode = $("#identifyCode").val();

    var data = {
      "identifyCode" : identifyCode,
    };
    
    $.ajax({
      url : "/contrastCode",
      type : "POST",
      dataType : 'json',
      async : false,
      data : data,
      success : function(flag) {
        //根据判断提示用户
        if (flag == true) {
          $("#msgFirst").html("验证码正确！");
          $("#msgFirst").css("color", "#00ff00");
          check2 = 1;
          return check2;
        } else {
          $("#msgFirst").html("验证码错误");
          
          $("#msgFirst").css("color", "#ff0000");
          check2 = 0;
          return check2;
        }
      },
      error : function() {
        alert('检查用户是否存在发生错误');
      }
    });

  }


//检查邮箱/手机号验证码
function checkIdentify()
{var numberAndMobileCode = $("#identifyCodeNext").val();
var userName=$("#username").val();
var numbAddMail=$("#numbAddMailHiden").val();
    var data = {
      //"userNickName":userNickName,
     // "numbAddMail":numbAddMail,
      "numberAndMobileCode" : numberAndMobileCode,
    };
    
    $.ajax({
      url : "/mailCheckCode",
      type : "POST",
      dataType : 'json',
      async : false,
      data : data,
      success : function(data) {
        //根据判断提示用户
        if (data.state) {
          $("#msgNext").html("验证码正确！");
          $("#msgNext").css("color", "#00ff00");
          check3 = 1;
          return check3;
        } else {
          $("#msgNext").html("验证码错误");
          $("#msgNext").css("color", "#ff0000");
          
          check3 = 0;
          return check3;
        }
      },
      error : function() {
        alert('检查用户是否存在发生错误');
      }
    });

  }
function getInfoAndAnalysis(url){
	var result=new Array();
	var parm=""; 
	if(url.indexOf("?")!=-1){
		for(var i=0;i<url.length;i++){
			if(url[i]=="?"){
				parm=url.substring(i+1, url.length);
			}
		}
			
	}
	function DivideTwoElement(parm){
		strs=parm.split("&");
		$.each(strs,function(i,n){
			var a=(urid(n)==""?null:urid(n));
			result.push(a);
		});
		return result;
	}
	var urid=function(parm){
		var p="";
		for(var i=0;i<parm.length;i++){
			if(parm[i]=="="){
				p=parm.substring(i+1, parm.length);
			}
		}
		return p;
	}
	
	return DivideTwoElement(parm);
}

//跳转
function checkLink() {
	//var str3=1234567;
	  //alert(str3);
	 // var string1="通过邮箱验证码找回";
	 // var str="请通过123456找回";
	  //var string=string1+string3+string2;
	 // var string2="邮箱号:";
	 // var strProgress="验证邮箱";
	// window.location.href="findPwd?string1="+string1+"&strProgress="+strProgress+"&str="+str+"&string2="+string2+"&str3="+str3;
	 
	
	//document.getElementById("labelProgress1").innerHTML=("验证邮箱");
	  if (check1 && check2 ) {
		  
		  var str1="请通过";
    	  
    	  
    	  var userName=$("#pwdUserName").val();
    	  var data={ "userName":userName,};
		  if($("#mail").prop("checked")==true)
			{
			  var str2="邮箱获取6位数字验证码";
			
			  $.ajax({
				//找后台要邮箱
				
				      url : "/getUserMailAddress",
				      type : "POST",
				      dataType : 'json',
				      async : false,
				      data : data,
				      success : function(data) {
				        //根据判断提示用户
				    	 
				    	  var str3=data.userEmail;
				    	  
				    	  var string1="通过邮箱验证码找回";
				    	  var str=str1+str3+str2;
				    	  //var string=string1+string3+string2;
				    	  var string2="邮箱号:";
				    	  var strProgress="验证邮箱";
				    	  //document.getElementById("labelProgress1").innerHTML=(strProgress);
				    	  //document.getElementById("labelPwd1").innerHTML=(string1);
				    	  //document.getElementById("labelContent").innerHTML=(str);
				    	 // document.getElementById("labelNumbMail").innerHTML=(string);
				    	 // $("#userNiname").val(userNickname);
				    	 //$("#numbAddMail").val(data.邮箱);
				    	  //document.getElementById("identifyCode").reset();
				    	 var href=encodeURI(encodeURI("findPwd?string1="+string1+"&strProgress="+strProgress+"&str="+str+"&string2="+string2+"&str3="+str3+"&userName="+userName));
				    	//window.location.href="findPwd?string1="+string1+"&strProgress="+strProgress+"&str="+str+"&string2="+string2+"&str3="+str3+"&userNickname="+userNickname;
				    	  //var href=encodeURIComponent(encodeURIComponent("findPwd?string1="+string1+"&strProgress="+strProgress+"&str="+str+"&string2="+string2+"&str3="+str3+"&userNickname="+userNickname));
				    	
				    	 window.location.href=href;
				      },
				      error : function() {
				        alert('检查用户是否存在发生错误');
				      }
				    });
			  }
		  else if($("#mobile").prop("checked")==true)
			{	
			  var str2="手机号获取6位数字验证码";
			  $.ajax({//找后台要手机号
			      url : "/getUserMobile",
			      type : "POST",
			      dataType : 'json',
			      async : false,
			      data : data,
			      success : function(data) {
			        //根据判断提示用户
			    	 
			    	  var str3=data.userMobile;
			    	  
			    	  var string2="手机号:";
			    	  var str=str1+str3+str2;
			    	  var strProgress="验证手机";
			    	  var string1="通过短信验证码找回";
			    	 // document.getElementById("labelPwd1").innerHTML=(string);
			    	  //document.getElementById("labelNumbMail").innerHTML=(str);
			    	  //document.getElementById("labelContent").innerHTML=(str);
			    	 // $("#userNiname").val(userNickname);
			    	 //$("#numbAddMail").val(data.手机号);
			    	  var href=encodeURI(encodeURI("findPwd?string1="+string1+"&strProgress="+strProgress+"&str="+str+"&string2="+string2+"&str3="+str3+"&userName="+userName));
			    	 window.location.href=href;
			      },
			      error : function() {
			        alert('检查用户是否存在发生错误');
			      }
			    }); 
			  }else{
				  $("#msgMenth").html("验证方式不能为空");
	          
				  $("#msgMenth").css("color", "#ff0000");}
		  
	  } else if (check3 ) {


			  var string1=$("#labelPwd1").val();
			
			  var strProgress=$("#labelProgress1").val();
			
			  //document.getElementById("labelPwd2").innerHTML=(text);
			  //document.getElementById("labelProgressTwo1").innerHTML=(strProgress);
			  var userName= $("#username").val();
			
			  //$("#userNickName").val(userNickname);
			  //var Number = $("#numberPhone").val();
			  //$("#userNickName").val(Number);
			 // window.location.href='resetPwd';
			  var href=encodeURI(encodeURI("resetPwd?string1="+string1+"&strProgress="+strProgress+"&userName="+userName));
		     window.location.href=href;
		  } else{

			  alert("必填项不能为空")
			  //$("#msgNext").html("验证码错误");
	          
			  //$("#msgNext").css("color", "#ff0000");
	  }
	 
	}  
//发送验证码
var wait = 60;
function sendCode(node)
{	 if(wait == 60)
	{	var mobileAndMail = $("#mobileAddMailHiden").val();
		
		var data = {
	      "mobileAndMail" : mobileAndMail,
			};
		
		$.ajax({
	     url : "/sendMail",
	    type : "POST",
	    dataType : 'json',
	    //async : false,
	    data : data,
	    success : function(data) {
	      //根据判断提示用户
	    
	    	if(data.state)
	    	{alert("验证码发送成功");
	    	//$("#message").html("验证码发送成功！");
	    	//$("#message").css("color", "#00ff00");
	    	}

	    	else{
	    		alert("验证码发送失败");
	    	
	    	
		         // $("#message").html("验证码发送错误");
		          //$("#message").css("color", "#ff0000");
	    	}
	     
	    },
	    error : function(data) {
	    	
	      alert('检查用户是否存在发生错误');
	    }
	  });
	}
		 if(wait ==0){
	         node.removeAttribute('disabled');//禁用
	         node.value= '重新发送';
	         wait = 60;
	     }else{
	    	
	    	 node.value= "已发送("+wait+")";
	         node.setAttribute('disabled',true);
	        
	         wait--;
	         setTimeout(function(){sendCode(node)},1000);
	     	}
}
//重置第一个验证码
function resetCode()
{	
	document.getElementById("identifyCode").reset();
	ajax({//找后台要log验证码
	url:"/doRegisterUser",
	method:"post",
	//data:param,
	async : false,
	dataType:"json",
	success:function(data){					
			code=data.验证码
		document.getElementById("codeInfo").innerHTML=(code); 
		//window.location.href='findUserPwd';
			
			
	},
	error:function(){
		
			alert("重置验证码失败");	
	}
	});
}
//后台发回验证码
/*var code;
$(function(){ 
	//var code="123456";
	//document.getElementById("codeInfo").innerHTML=(code); 
	
	$.ajax({//找后台要log验证码
			url:"/doObtainIdentifyCodeFirst",
			method:"post",
			//data:param,
			dataType:"json",
			success:function(data){					
					code=data.code;
					//alert(data.code);
				document.getElementById("codeInfo").innerHTML=(code); 
				
					
			},
			error:function(){
				
						
			}
		});
});*/
function addNewPwd(node)
{	var param = $("#resetPasswordForm").serializeArray();
	
	if(check4&&check5)
	{
		$.ajax({
			url:"/doResetPassword",
			method:"POST",
			data:param,
			async : false,
			dataType:"json",
			success:function(data){					
					
			    $('i').removeClass('grayness').addClass('orange');
				$('#labelProgressTwo3').removeClass('content').addClass('content3');
				node.setAttribute('disabled',true);
				$("#Tip").modal('show');
				
				document.getElementById("tipContent").innerText="恭喜您，重置密码成功";
				//location.reload();
				window.location.href='Main';
				//setTimeout(function(){window.location.href='Main';},1000);
					
					
			},
			error:function(){
				document.getElementById("tipContent").innerText="重置密码失败";
				$("#Tip").modal('show');
				//$("#registerUserModal").modal('hide');
				setTimeout(function(){window.location.href='Main';},5000);
						
				}
			});
	}

}
//判断密码约束
function checkPassword(node) { //当鼠标离开节点时调用此方法

	  //密码格式：
	  var passwordRegex = /^[\w%*!$#]{6,12}$/; //6-12位 字母、数字、下划线和特殊字符% * ! $ # 组成 
	  var password = node.value;

	  if (!passwordRegex.test(password)) {
	    byId("password_span").style.color = "red";
	    byId("password_span").innerHTML = "密码长度6-12个字符之间, 使用字母、数字、下划线和特殊字符 ";
	    check4 = 0;
	    return check4;
	  } else {
	    //进行密码强度判断

	    var lv = checkPswStrong(password);

	    if (lv == 0) {
	      // 6位连续数字 密码强度：过低，无法完成注册，请重新输入密码
	      byId("password_span").innerHTML = "密码强度：过低，无法完成注册，请重新输入密码 ";
	      byId("password_span").style.color = "red";
	      check4 = 0;
	      return check4;

	    } else if (lv == 1) {
	      // 数字、字母和特殊字符中的一种 密码强度：低
	      byId("password_span").innerHTML = "密码强度：低 ";
	      byId("password_span").style.color = "green";
	      check4 = 1;
	      return check4;

	    } else if (lv == 2) {
	      // 数字、字母和特殊字符中的两种 密码强度：中
	      byId("password_span").innerHTML = "密码强度：中";
	      byId("password_span").style.color = "green";
	      check4 = 1;
	      return check4;

	    } else if (lv == 3) {
	      // 数字、字母和特殊字符三种  密码强度：高
	      byId("password_span").innerHTML = "密码强度：高";
	      byId("password_span").style.color = "green";
	      check4 = 1;
	      return check4;

	    }
	  }
	}

	//输出密码强度等级 0，1 ,2, 3 
	function checkPswStrong(password) {

	  var lv = 0;
	  //判断这个字符串中有没有数字
	  if (/[0-9]/.test(password)) {
	    lv++;
	    //判断是否为连续6位数字
	    if (password == "123456" || password == "234567"
	        || password == "345678" || password == "456789"
	        || password == "567890")
	      lv = 0;
	  }
	  //判断字符串中有没有字母
	  if (/[a-zA-Z]/.test(password)) {
	    lv++;
	  }
	  //判断字符串中有没有特殊符号
	  if (/[^0-9a-zA-Z_]/.test(password)) {
	    lv++;
	  }
	  return lv;
	}

	//确认密码
	function checkRePassword(node) { //当鼠标离开节点时调用此方法，验证节点内容是否符合注册规范

	  var rePassword = node.value;
	  var password = byId("userPassword").value;
	  //var rePassword = byId("reUserPassword").value;
	  if (!(password == rePassword)) {
	    byId("rePassword_span").style.color = "red";
	    byId("rePassword_span").innerHTML = "您两次输入的密码不一样";
	    check5 = 0;
	    return check5;

	  } 
	  else if (rePassword == ""){
	    byId("rePassword_span").style.color = "red";
	    byId("rePassword_span").innerHTML = "请确认密码";
	    check5 = 0;
	    return check5;
	  }
	  else {
	    byId("rePassword_span").style.color = "green";
	    byId("rePassword_span").innerHTML = "pass";
	    check5 = 1;
	    return check5;
	  }
	}
function byId(id) { //自定义方法，用于获取传递过来的ID值对应的节点对象
		  return document.getElementById(id);
		}

//用户退出
//function logout()
//{  
//	var data="";
// alert(data);
//  $.ajax({
//      type: "POST",
//      url: "/doUserLogout",
//      data: data,
//      
//      success: function(data){
//        
//      alert("退出成功！");
//      window.location.href="/Main";
//      hide();
//      },
//  error:function(){
//      alert("退出失败！");
//  }
//   });
//}

function logout(){
	data="";
	
    $.ajax({
           type: "POST",
           url: "/doUserLogout",
           data: data,
           success: function(data){
           alert("退出成功！");
           window.location.href="/Main";
           hide();
           },
      error:function(){
    	  
    	  alert("退出失败！");
      }
        });
}
function userinfo(){
	  var name=$("#userName").val();
	 
	  //var href=encodeURI(encodeURI("userInfo?userNickname="+userNickname));
	  //window.location.href=href;
	   var data={
	       "userName":name
	   };
	   //alert("2"+JSON.stringify(data));
	   $.ajax({
	    url : "/doSelectOasUser",
	    type : "GET",
	    dataType : 'json',
	    async : false,
	    data : data,
	    success : function(data) {
	          //获取数据库中用户信息
	    	  
	          var userName=data.OasUser["name"];
	          var userProfile=data.OasUser["profile"];
	          var userNickame=data.OasUser["nickname"];
	          var userGender=data.OasUser["gender"];
	          var userAddress=data.OasUser["address"];
	          var userBirthday=data.OasUser["birthday"];
	          var userMobile=data.OasUser["mobile"];
	          var userEmail=data.OasUser["email"];
	          var href=encodeURI(encodeURI("userInfo?userName="+userName+"&userProfile="+userProfile+"&userNickname="+userNickname+"&userGender="+userGender+"&userAddress="+userAddress+"&userBirthday="+userBirthday+"&userMobile="+userMobile+"&userEmail="+userEmail));
	        
	          window.location.href=href;  
	        },
	        error : function() {
	          alert('检查用户是否存在发生错误');
	        }
	      });
	}

	//保存用户个人信息
	function saveUserInfo(){  
	  
	  $("#save").attr("onclick","saveUserInfo()");
	  var userName=$("#userName").val();
	  
	  var userProfile=$("#userProfile").val();
	  var userNickname=$("#userNickname").val();
	  //var userGender=$("#userGender").val();
	  
	  var userGender;
	  
	   if($("#userGender1").prop("checked")==true){
	     userGender='女';
	    
	  }else{
	     userGender='男';
	     
	     }
	  var userAddress=$("#userAddress").val();
	  
	  var userBirthday=$("input[name='userBirthday']").val();
	  
	  data={
	     "EuserName":userName,
	     //"EuserProfile": userProfile,
	     "EuserNickname":userNickname,
	     "EuserGender":userGender,
	     "EuserAddress":userAddress,
	     "EuserBirthday":userBirthday
	  },
	  //var param = $("#UserInfoForm").serializeArray();
	    
	  $.ajax({
	    url:"/doUpdateOasUser",
	    method:"post",
	    data:data,
	    dataType:"json",
	    success:function(result){  
	      alert("保存成功");
	    },
	    error:function(){      
	      alert("保存失败");
	      }
	  });
	}


	function userimginfo(){
		
		var name=$("#userName").val();
	    
	    var data={
	 	       "userName":name
	 	   };	  $.ajax({
	     url : "/doSelectOasUser",
	     type : "GET",
	     dataType : 'json',
	     async : false,
	     data : data,
	     success : function(data) {
	          //根据判断提示用户
	    	 
	          var userName=data.OasUser["name"];
	          var userProfile=data.OasUser["profile"];
	          var href=encodeURI(encodeURI("userImgInfo?userName="+userName+"&userProfile="+userProfile));
	         window.location.href=href;
	        },
	        error : function() {
	          alert('检查用户是否存在发生错误');
	        }
	      });
	}


	function usersecurityinfo(){
	  var name=$("#userName").val();
	  
	  //alert("个人信息的名字"+name);
//	    var href=encodeURI(encodeURI("Userinfo?userNickname="+name));
//	     window.location.href=href;
	    var data={
	        "userName":name
	    };
	    $.ajax({
	       url : "/doSelectOasUser",
	       type : "GET",
	       dataType : 'json',
	       async : false,
	       data : data,
	       success : function(data) {
	            //根据判断提示用户
	    	   var userName=data.OasUser["name"];
		       var userMobile=data.OasUser["mobile"];
		       var userEmail=data.OasUser["email"];
	           var href=encodeURI(encodeURI("userSecurityInfo?userName="+userName+"&userMobile="+userMobile+"&userEmail="+userEmail));
	           window.location.href=href;
	          },
	          error : function() {
	            alert('检查用户是否存在发生错误');
	          }
	        });
	  }

