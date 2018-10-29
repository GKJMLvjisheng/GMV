/**
 * 
 */




//个人信息主页数据显示
function userinfo(){
	  var userName=$("#userName").val();

	   var data={
	       "name":userName
	   };
	   $.ajax({
	    url : "/api/v1/userCenter/inquireUserInfo",
	    type : "POST",
	    dataType : 'json',
	    async : false,
	    data : data,
	    success : function(res) {
	          //获取数据库中用户信息
	    	 
	          var userName=res.data.name;
	          var userProfile=res.data.profile;
	        
	          var userNickname=res.data.nickname;
	          var userGender=res.data.gender;
	          var userAddress=res.data.address;
	          var userBirthday=res.data.birthday;
	          var userMobile=res.data.mobile;
	          var userEmail=res.data.email;
	          var href=encodeURI(encodeURI("/userInfo/userInfo?userName="+userName+"&userProfile="+userProfile+"&userNickname="+userNickname+"&userGender="+userGender+"&userAddress="+userAddress+"&userBirthday="+userBirthday+"&userMobile="+userMobile+"&userEmail="+userEmail));
	          
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
	  
	  var userNickname=$("#userNickname").val();
	 
	  var userGender;
	  
	   if($("#userGender1").prop("checked")==true){
	     userGender='女';
	     
	  }else{
	     userGender='男';
	    
	     }
	  var userAddress=$("#userAddress").val();
	  var userBirthday=$("input[name='userBirthday']").val();
	  
	  data={
	     "name":userName,
	     //"EuserProfile": userProfile,
	     "nickname":userNickname,
	     "gender":userGender,
	     "address":userAddress,
	     "birthday":userBirthday
	  }
	  //alert(JSON.stringify(data));
	  $.ajax({
	    url:"/api/v1/userCenter/updateUserInfo",
	    method:"POST",
	    data:JSON.stringify(data),
	    dataType:"json",
	    contentType : 'application/json;charset=utf8',
	    success:function(res){  
	      alert("保存成功");
	    },
	    error:function(){      
	      alert("保存失败");
	      }
	  });
	}




//修改头像主页数据显示
function userimginfo(){
	
	var userName=$("#userName").val();
    
    var data={
 	       "name":userName
 	   };	  $.ajax({
     url : "/api/v1/userCenter/inquireUserInfo",
     type : "POST",
     dataType : 'json',
     async : false,
     data : JSON.stringify(data),
     contentType : 'application/json;charset=utf8',
     success : function(res) {
          //获取数据库中用户信息
          var userName=res.data.name;
          
          var userProfile=res.data.profile;
          
          var href=encodeURI(encodeURI("/userInfo/userImgInfo?userName="+userName+"&userProfile="+userProfile));
         window.location.href=href;
        },
        error : function() {
          alert('检查用户是否存在发生错误');
        }
      });
}



//安全设置主页数据显示
function usersecurityinfo(){
//  var userName=$("#userName").val();
//  
//  var data={
//        "name":userName
//    };
//    $.ajax({
//       url : "/api/v1/userCenter/inquireUserInfo",
//       type : "POST",
//       dataType : 'json',
//       async : false,
//       data : data,
//       success : function(res) {
//    	   
//    	   //获取数据库中用户信息
//    	   var userName=res.data.name;
//	       var userMobile=res.data.mobile;
//	       var userEmail=res.data.email;
//           var href=encodeURI(encodeURI("/userInfo/userSecurityInfo?userName="+userName+"&userMobile="+userMobile+"&userEmail="+userEmail));
//           window.location.href=href;
//          },
//          error : function() {
//            alert('检查用户是否存在发生错误');
//          }
//        });
	var href="/userInfo/userSecurityInfo";
	window.location.href=href;
  }

//个人信息二维码
function QRcode() {
				$("#QRuserinfocode").addClass("selected");
	            var userName=$("#userName").val();
	            var info="your account is:"+userName;
	            var qrcode=$('#qrcodeCanvas').qrcode(info).hide();
	   	        var canvas=qrcode.find('canvas').get(0);
	   	        $('#image').attr('src',canvas.toDataURL('img/gray.jpg'))         
	                  $("#qrcode").fadeIn("slow");
	              $("#qrcode").click(function() {
	                  $("#qrcode").fadeOut("slow");
	                  $("#QRuserinfocode").removeClass("selected");
	              })
	          }
//交易二维码
function QRtradecode() {
	$("#QRtradecode").addClass("selected");
	  var userName=$("#userName").val(); 
		 var num = '';
		 for (i = 0; i <= 31; i++){
			var tmp = Math.ceil(Math.random()*15); 
		    if(tmp > 9){
		           switch(tmp){  
		               case(10):
		                   num+='a';
		                   break; 
		               case(11):
		                   num+='b';
		                   break;
		               case(12):
		                   num+='c';
		                   break;
		               case(13):
		                   num+='d';
		                   break;
		               case(14):
		                   num+='e';
		                   break;
		               case(15):
		                   num+='f';
		                   break;
		           } 
		        }else{
		           num+=tmp;
		        }
		     }
         var info="your tradeaddress is:"+"0x"+num;
         var qrcode=$('#qrcodeCanvas').qrcode(info).hide();
	     var canvas=qrcode.find('canvas').get(0);
	        $('#image').attr('src',canvas.toDataURL('img/gray.jpg'))         
               $("#qrcode").fadeIn("slow");
           $("#qrcode").click(function() {
               $("#qrcode").fadeOut("slow");
               $("#QRtradecode").removeClass("selected");
           })
}

//刷新从数据库更新数据	
function refreshUserInfo(){
	var userName=$("#userName").val();
	   var data={
	       "name":userName
	   };
	
	   $.ajax({
	    url : "/api/v1/userCenter/inquireUserInfo",
	    type : "POST",
	    dataType : 'json',
	    async : false,
	    data : data,
	    success : function(res) {
	          //获取数据库中用户信息
	    	  
	          var userName=res.data.name;
	          //var userProfile=res.data.profile;
	         //alert(userProfile);
	          var userNickname=res.data.nickname;
	          var userGender=res.data.gender;
	          var userAddress=res.data.address;
	          var userBirthday=res.data.birthday;
	          var userMobile=res.data.mobile;
	          var userEmail=res.data.email;

	        $("#userName").val(userName);
	        var image='PNG,GIF,JPG,JPEG,BMP,png,gif,jpg,jpeg,bmp,blob';
            var imageList=res.data.profile.split('.');
            var imageList2=res.data.profile.split('-');
            if(image.indexOf(imageList[imageList.length-1])!=-1||image.indexOf(imageList2[imageList2.length-1])!=-1){
            	var userProfile=res.data.profile;
            }else{
            	var userProfile="../img/icon_default_user.png";
            	
            }
	      	$("#userProfile").attr("src", userProfile);
	      	$("#userNickname").val(userNickname);
	      	if(userGender=='女'){
	  		  $("#userGender1").prop("checked", true);
	  	
	  		  }
	  	  else if(userGender=='男'){
	  		  $("#userGender2").prop("checked", true);
	  		  }
	      	 $("#userAddress").val(userAddress);
	      	
	      	 $("input[name='userBirthday']").val(userBirthday);	
	      	 
	      	 $("#userMobile").val(userMobile);
	      	 $("#userEmail").val(userEmail);
//	      	 str="12345678"
//	      	
//	      	$("#mobileAddMail").val(str.replace(/(.{0}).*(.{0})/, "$1********$2"));
	        },
	        error : function() {
	          alert('检查用户是否存在发生错误');
	        }
	      });
};

// ————————————————————————————安全设置首页-修改手机和邮箱-SecurityStep1.html————————————————————
// 身份确认 - 密码确认
var checkPassowrdFlag = 0 ;
function checkPasswordIn(){
	
	var userName = $("#userName").val(); // 登录名
	var userPassword = $("#userPassword").val(); // 密码
	var data = {
			"name" : userName,
			"password" : userPassword,
		};
	$.ajax({
			url : "/api/v1/userCenter/checkPassword",
			type : "POST",
			dataType : 'json',
			async : false,
			data : JSON.stringify(data),
			contentType : 'application/json;charset=utf8',
			success : function(res) {

				if (res.data.state) {
					document.getElementById("password_span").style.color = "green";
					document.getElementById("password_span").innerHTML = "密码匹配正确";
					checkPassowrdFlag = 1 ;
					

				}

				else {
					document.getElementById("password_span").style.color = "red";
					document.getElementById("password_span").innerHTML = "密码不匹配，请重新输入";
					checkPassowrdFlag = 0 ;
					
				}
			},
			error : function(res) {
				
				alert('检查密码存在发生错误');
				checkPassowrdFlag = 0 ;
			}
		});
}
var check1=0
function checkUserPassword(){
	var userPassword = $("#userPassword").val(); // 密码
	var passwordRegex = /^[a-zA-Z0-9_]{4,15}$/;
	if (!passwordRegex.test(userPassword)) {
		document.getElementById("userPassword_span").style.color = "red";
		document.getElementById("userPassword_span").innerHTML = "密码长度4-12个字符之间 ";
	    check1 = 0;
	    //return check1;
	  }else{
		  document.getElementById("userPassword_span").innerHTML=""
		  check1 = 1;
	  }
}
function modifyPassword(){
	var userName = $("#userName").val(); // 登录名
	var userPassword = $("#userPassword").val(); // 密码
	var reUserPassword=$("#reUserPassword").val();
	if(userPassword!=reUserPassword){
		alert("密码不一致，请重新输入!");
		return;
	}
	var data = {
			"name" : userName,
			"password" : userPassword,
		};
	console.log(JSON.stringify(data))
	$.ajax({
			url : "/api/v1/userCenter/resetPassword",
			type : "POST",
			dataType : 'json',
			async : false,
			data : JSON.stringify(data),
			contentType : 'application/json;charset=utf8',
			success : function(res) {
				console.log(JSON.stringify(res))
				if (res.code==0) {
					var string1 = $("#labelPwd2").val();//标题
				      var strProgress = $("#labelProgressTwo2").val();// 修改邮箱 或者 修改手机
				      var str = $("#userMobileIn").val();// mobile
				      var userName = $("#userName").val();// 登录名
				      var result = "成功！";

				      var href = encodeURI(encodeURI("/userInfo/resetMMSuccess?string1=" + string1
						+ "&strProgress=" + strProgress + "&str=" + str + "&userName="
						+ userName + "&result="+ result));
				      window.location.href = href;	
				      //alert("reset Mobile successfully!");	
				      //document.getElementById("tipContent").innerText="修改密码成功";
						//$("#Tip").modal('show');
					

				}

				else {
					document.getElementById("tipContent").innerText="修改密码失败";
					$("#Tip").modal('show');
					
				}
			},
			error : function(res) {
				
				alert('检查密码存在发生错误');
				checkPassowrdFlag = 0 ;
			}
		});
}
// 功能： checkPassowrdFlag = 1 + 页面跳转到 resetMail 或者resetMobile
function checkLink1() {
	checkPasswordIn();
	var string1 = $("#labelPwd1").val();//标题
	var strProgress = $("#labelProgress2").val();// 修改邮箱 或者 修改手机

	var str = $("#mobileAddMailHiden").val();// mobileAddMailHiden mail\mobile
	
    var string2 = $("#labelMobileMail").val(); // labelMobileMail 原手机号或者邮箱或密码
  
	var userName = $("#userName").val();// 登录名
	
	if ((checkPassowrdFlag == 1) && ($("#labelProgress2").val() == "修改邮箱")) {
		
		var href = encodeURI(encodeURI("/userInfo/resetMail/resetMail?string1=" + string1
				+ "&strProgress=" + strProgress + "&str=" + str + "&string2="
				+ string2 + "&userName=" + userName));
		window.location.href = href;
		
	} else if ((checkPassowrdFlag == 1) && ($("#labelProgress2").val() == "修改手机")) {

		var href = encodeURI(encodeURI("/userInfo/resetMobile/resetMobile?string1=" + string1
				+ "&strProgress=" + strProgress + "&str=" + str + "&string2="
				+ string2 + "&userName=" + userName));
		window.location.href = href;
		
	} else if((checkPassowrdFlag == 1) && ($("#labelProgress2").val() == "修改密码")){
		var href = encodeURI(encodeURI("/userInfo/resetPassword/resetPassword?string1=" + string1
				+ "&strProgress=" + strProgress + "&str=" + str + "&string2="
				+ string2 + "&userName=" + userName));
		window.location.href = href;
		
	}else{
		alert("Error: no link! 请先输入正确用户登录密码");
	}

}
// ——————————————————————————————   修改邮箱相关的JS resetMail.html——————————————————————————————————————————

// 判断邮箱是否重复，重复则不可使用
var checkMailFlag = 0 ;
function checkUserMail(userMail) {
	
  var userMail = $("#userMailIn").val();
  if ((userMail != "") && (userMail.indexOf("@")) != -1) {
	  
      var data = {
        "email" : userMail
      };

      $.ajax({
        url : "/api/v1/userCenter/CheckUserEmail",
        type : "POST",
        dataType : 'json',
        async : false,
        data : JSON.stringify(data),
        contentType : 'application/json;charset=utf8',
        success : function(res) {
          //根据判断提示用户
      
          if (res.data == 0) {
        	
            $("#userMail_span").html("恭喜您，邮箱可以使用");
            $("#userMail_span").css("color", "green");
            checkMailFlag = 1;
            
          } else {
        	  
            $("#userMail_span").html("该邮箱已经存在");
            $("#userMail_span").css("color", "red");
            checkMailFlag = 0;
          }
         
        },
        error : function() {
          alert('检查邮箱是否存在发生错误');
          checkMailFlag = 0;
          
        }
      });
    }
  else {
    $("#userMail_span").html("请输入正确邮箱!");
    $("#userMail_span").css("color", "red");
    checkMailFlag = 0;
    
  }
}


//发送验证码
var btn = document.getElementsByTagName('button')[0];
function sendMailCode() {

	var userMail = $("#userMailIn").val();
	checkUserMail(userMail); // 确认邮箱是否可用

	var time = 60;//定义时间变量。用于倒计时用
	var timer = null;//定义一个定时器；

	if ((checkMailFlag == 1) && (time == 60)) {
		var mobileAndMail = $("#userMailIn").val();
		
		var data = {
			"email" : mobileAndMail,
		};
		timer = setInterval(function() {///开启定时器。函数内执行
			btn.disabled = true;
			//btn.innerText = time + "秒后重新发送"; //点击发生后，按钮的文本内容变成之前定义好的时间值。
			btn.innerText = time + "秒后重发";
			time--;//时间值自减
			if (time == 0) { //判断,当时间值小于等于0的时候
				btn.innerText = '获取验证码'; //其文本内容变成……点击重新发送……
				btn.disabled = false;
				clearInterval(timer); //清除定时器
			}
		}, 1000)

		$.ajax({
			url : "/api/v1/userCenter/sendMail",
			type : "POST",
			dataType : 'json',
			//async : false,
			data : JSON.stringify(data),
			contentType : 'application/json;charset=utf8',
			success : function(res) {
				
				if (res.data.state) {
					alert("验证码发送成功");
				}

				else {
					alert("验证码发送失败");
				}
			},
			error : function(res) {
				
				alert('验证码发送失败');
			}
		});
	}else{
		alert("验证码发送error！！！");
	}

}

// 检查邮箱验证码
var flag = 0 ; 
function checkMailIdentify() {
	
	var mailCode = $("#identifyCodeNext").val();
   
	var data = {
	
		"mailCode" : mailCode,
	};
    
	$.ajax({
		url : "/api/v1/userCenter/mailCheckCode",
		type : "POST",
		dataType : 'json',
		data : JSON.stringify(data),
		async:false,
		contentType : 'application/json;charset=utf8',
		success : function(res) {
			//根据判断提示用户
			if (res.data.state) {
				$("#msgNext").html("验证码正确！");
				$("#msgNext").css("color", "#00ff00");
				
				flag = 1;
			} else {
				$("#msgNext").html("验证码错误");
				$("#msgNext").css("color", "#ff0000");

				flag = 0 ;
			}
		},
		error : function() {
			alert('检查验证码存在发生错误');
			flag = 0 ;
		}
	});

}

// 修改邮箱到数据库
function addNewMail() {
	checkMailIdentify();
	if (flag){
		
		var userName = $("#userName").val();
		var userMail = $("#userMailIn").val();
		
		var data = {
				"name" : userName,
				"email" : userMail
			};
		
		$.ajax({
			url:"/api/v1/userCenter/resetMail",
			method:"POST",
			data:JSON.stringify(data),
			dataType:"json",
			contentType : 'application/json;charset=utf8',
			success:function(res){	
		
			//跳转页面	
			
		      var string1 = $("#labelPwd2").val();//标题
		      
		      var strProgress = $("#labelProgressTwo2").val();// 修改邮箱 或者 修改手机
		      
		      var str = $("#userMailIn").val();// mail
		     
		      var userName = $("#userName").val();// 登录名
		      
		      var result = "成功！";
		     	      
		      var href = encodeURI(encodeURI("/userInfo/resetMMSuccess?string1=" + string1
				+ "&strProgress=" + strProgress + "&str=" + str + "&userName="
				+ userName + "&result="+ result));
		      window.location.href = href;	
			  //alert("reset Mail successfully!");	
					
			},
			error:function(){
				document.getElementById("tipContent").innerText="重置邮箱失败";
				$("#Tip").modal('show');
						
				}
			});
	}
	else{
		
		alert("验证失败，修改未成功！！！！");
	}

}

// ---------------------------- 修改手机相关的JS resetMobile.html --------------------------

// 判断手机号是否重复，重复则不可使用
var checkMobileFlag = 0;
function checkUserMobile(userMobile) {

  var userMobile = $("#userMobileIn").val();
 
  if (userMobile != "") {
	  var reg = /^1\d{10}$/; //1开头的11位数字的正则表达式  
	  
      if (!reg.test(userMobile)) {
      $("#userMobile_span").html("请输入正确格式手机号码");
      $("#userMobile_span").css("color", "red");
      } 
      else {
      var data = {
        "mobile" : userMobile
      };

      $.ajax({
        url : "/api/v1/userCenter/CheckUserMobile",
        type : "POST",
        dataType : 'json',
        data : JSON.stringify(data),
        async : false,
        contentType : 'application/json;charset=utf8',
        success : function(res) {
          //根据判断提示用户
          if (res.data==0) {
            $("#userMobile_span").html("恭喜您，手机号可以使用");
            $("#userMobile_span").css("color", "green");
            checkMobileFlag = 1;
        
          } else {
            $("#userMobile_span").html("该手机号已经存在");
            $("#userMobile_span").css("color", "red");
            checkMobileFlag = 0;
          }
        },
        error : function() {
          alert('检查手机号是否存在发生错误');
          checkMobileFlag = 0;
        }
      });
    }
  } 
  else {
    $("#userMobile_span").html("请输入手机号!");
    $("#userMobile_span").css("color", "red");
    checkMobbileFlag = 0;
  }
}

   
//发送验证码
    var btn = document.getElementsByTagName('button')[0];
	function sendMobileCode() {
		var time = 60;//定义时间变量。用于倒计时用
		var timer = null;//定义一个定时器；
       
		if ((checkMobileFlag == 1) && (time == 60)) {
			
			var mobileAndMail = $("#userMobileIn").val();
			
			var data = {
				"mobile" : mobileAndMail,
			};
			
			$.ajax({
				
				url : "/api/v1/userCenter/sendMobile",
				type : "POST",
				dataType : 'json',
				async : false,
				data : JSON.stringify(data),
				contentType : 'application/json;charset=utf8',
				success : function(res) {

					if (res.data.state) {
						alert("验证码发送成功");
						
						// 开启定时器。函数内执行
						timer = setInterval(function() {
							btn.disabled = true;
							btn.innerText = time + "秒后重发"; //点击发生后，按钮的文本内容变成之前定义好的时间值。
							time--;//时间值自减
							if (time == 0) { //判断,当时间值小于等于0的时候
								btn.innerText = '获取验证码'; //其文本内容变成……点击重新发送……
								btn.disabled = false;
								clearInterval(timer); //清除定时器
							}
						}, 1000)

					}

					else {
						alert("验证码发送失败");
					}
				},
				error:function() {
					alert("sendMobile是否存在发生错误");
				}
			});
		}
		else{
			
			alert("请输入正确手机号");
		}

	}
	
	// 手机号验证码
    var checkCodeFlag = 0 ; 
	function checkMobileIdentify() {
		
		var mobileCode = $("#identifyCodeNext").val();
	
		var data = {
		
			"mobileCode" : mobileCode,
		};
	
		$.ajax({
			url : "/api/v1/userCenter/mobileCheckCode",
			type : "POST",
			dataType : 'json',
			data : JSON.stringify(data),
			async : false,
			contentType : 'application/json;charset=utf8',
			success : function(res) {
				//根据判断提示用户
				if (res.data.state) {
					$("#msgNext").html("验证码正确！");
					$("#msgNext").css("color", "#00ff00");
					
					checkCodeFlag = 1;
					
				} else {
					$("#msgNext").html("验证码错误");
					$("#msgNext").css("color", "#ff0000");

					checkCodeFlag = 0 ;
				}
				return checkCodeFlag;
			},
			error : function() {
				alert('检查用户是否存在发生错误');
				checkCodeFlag = 0 ;
			}
		});

	}

	function addNewMobile() {
		checkMobileIdentify();
		//alert(checkCodeFlag);
		if (checkCodeFlag){
			
			var userName = $("#userName").val();
			var userMobile = $("#userMobileIn").val();
			
			var data = {
					"name" : userName,
					"mobile" : userMobile
				};
			
			$.ajax({
				url:"/api/v1/userCenter/resetMobile",
				method:"POST",
				data:JSON.stringify(data),
				dataType:"json",
				contentType : 'application/json;charset=utf8',
				success:function(res){	
					
					//跳转页面	
					
				      var string1 = $("#labelPwd2").val();//标题
				      var strProgress = $("#labelProgressTwo2").val();// 修改邮箱 或者 修改手机
				      var str = $("#userMobileIn").val();// mobile
				      var userName = $("#userName").val();// 登录名
				      var result = "成功！";

				      var href = encodeURI(encodeURI("/userInfo/resetMMSuccess?string1=" + string1
						+ "&strProgress=" + strProgress + "&str=" + str + "&userName="
						+ userName + "&result="+ result));
				      window.location.href = href;	
				      //alert("reset Mobile successfully!");	
						
				},
				error:function(){
					document.getElementById("tipContent").innerText="修改手机失败";
					$("#Tip").modal('show');
					//$("#registerUserModal").modal('hide');
					//setTimeout(function(){window.location.href='Main';},5000);
							
					}
				});
		}
		else{
			alert("验证失败，修改未成功！！！！");
		}

	}
	//解析URL
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
