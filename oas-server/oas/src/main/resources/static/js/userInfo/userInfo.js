/**
 * 
 */

document.write("<script language=javascript src='resetPassword.js'></script>");


//个人信息主页数据显示
function userinfo(){
	  var name=$("#userNickname").val();
	  alert("个人信息的名字"+name);
	   var data={
	       "userNickname":name
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
	    	  alert(JSON.stringify(data));
	          var userNickname=data.OasUser["userNickname"];
	          var userProfile=data.OasUser["userProfile"];
	          var userName=data.OasUser["userName"];
	          var userGender=data.OasUser["userGender"];
	          var userAddress=data.OasUser["userAddress"];
	          var userBirthday=data.OasUser["userBirthday"];
	          var userMobile=data.OasUser["userMobile"];
	          var userEmail=data.OasUser["userEmail"];
	          var href=encodeURI(encodeURI("userInfo?userNickname="+userNickname+"&userProfile="+userProfile+"&userName="+userName+"&userGender="+userGender+"&userAddress="+userAddress+"&userBirthday="+userBirthday+"&userMobile="+userMobile+"&userEmail="+userEmail));
	          //alert(href);
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
	  var userNickname=$("#userNickname").val();
	  //alert(userNickname);
	  //var userProfile=$("#userProfile").val();
	  var userName=$("#userName").val();
	  //var userGender=$("#userGender").val();
	  //alert(userGender);
	  var userGender;
	  //alert($("#userGender1").prop("checked"));
	   if($("#userGender1").prop("checked")==true){
	     userGender='女';
	     alert(userGender);
	  }else{
	     userGender='男';
	     alert(userGender);
	     }
	  var userAddress=$("#userAddress").val();
	  var userBirthday=$("input[name='userBirthday']").val();
	  alert(userBirthday);
	  data={
	     "EuserNickname":userNickname,
	     //"EuserProfile": userProfile,
	     "EuserName":userName,
	     "EuserGender":userGender,
	     "EuserAddress":userAddress,
	     "EuserBirthday":userBirthday
	  },
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




//修改头像主页数据显示
function userimginfo(){
	alert(1);
	var name=$("#userNickname").val();
    alert("userNickname-->"+name);
    var data={
 	       "userNickname":name
 	   };	  $.ajax({
     url : "/doSelectOasUser",
     type : "GET",
     dataType : 'json',
     async : false,
     data : data,
     success : function(data) {
          //获取数据库中用户信息
          var userNickname=data.OasUser["userNickname"];
          var userProfile=data.OasUser["userProfile"];
          var href=encodeURI(encodeURI("userImgInfo?userNickname="+userNickname+"&userProfile="+userProfile));
         window.location.href=href;
        },
        error : function() {
          alert('检查用户是否存在发生错误');
        }
      });
}



//安全设置主页数据显示
function usersecurityinfo(){
  var name=$("#userNickname").val();
  //alert("个人信息的名字"+name);
  var data={
        "userNickname":name
    };
    $.ajax({
       url : "/doSelectOasUser",
       type : "GET",
       dataType : 'json',
       async : false,
       data : data,
       success : function(data) {
    	   //获取数据库中用户信息
    	   var userNickname=data.OasUser["userNickname"];
	       var userMobile=data.OasUser["userMobile"];
	       var userEmail=data.OasUser["userEmail"];
           var href=encodeURI(encodeURI("userSecurityInfo?userNickname="+userNickname+"&userMobile="+userMobile+"&userEmail="+userEmail));
           window.location.href=href;
          },
          error : function() {
            alert('检查用户是否存在发生错误');
          }
        });
  }

//个人信息二维码
function QRcode() {
	            var userNickname=$("#userNickname").val();
	            var info="您的账号是:"+userNickname;
	            var qrcode=$('#qrcodeCanvas').qrcode(info).hide();
	   	        var canvas=qrcode.find('canvas').get(0);
	   	        $('#image').attr('src',canvas.toDataURL('img/gray.jpg'))         
	                  $("#qrcode").fadeIn("slow");
	              $("#qrcode").click(function() {
	                  $("#qrcode").fadeOut("slow");
	              })
	          }
//交易二维码
function QRtradecode() {
	  var userNickname=$("#userNickname").val(); 
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
         var info="您的交易地址是:"+"0x"+num;
         var qrcode=$('#qrcodeCanvas').qrcode(info).hide();
	     var canvas=qrcode.find('canvas').get(0);
	        $('#image').attr('src',canvas.toDataURL('img/gray.jpg'))         
               $("#qrcode").fadeIn("slow");
           $("#qrcode").click(function() {
               $("#qrcode").fadeOut("slow");
           })
}

//刷新从数据库更新数据	
function refreshUserInfo(){
	var name=$("#userNickname").val();
	alert(name);
	   var data={
	       "userNickname":name
	   };
	   $.ajax({
	    url : "/doSelectOasUser",
	    type : "GET",
	    dataType : 'json',
	    async : false,
	    data : data,
	    success : function(data) {
	          //获取数据库中用户信息
	    	  alert(JSON.stringify(data));
	          var userNickname=data.OasUser["userNickname"];
	          var userProfile=data.OasUser["userProfile"];
	          var userName=data.OasUser["userName"];
	          var userGender=data.OasUser["userGender"];
	          var userAddress=data.OasUser["userAddress"];
	          var userBirthday=data.OasUser["userBirthday"];
	          var userMobile=data.OasUser["userMobile"];
	          var userEmail=data.OasUser["userEmail"];  
	          $("#userNickname").val(userNickname);
	      	//$("#userProfile").val(userProfile);
	      	$("#userProfile").attr("src", userProfile);
	      	$("#userName").val(userName);
	      	//$("#userGender").val(userGender);
	      	if(userGender=='女'){
	  		  $("#userGender1").prop("checked", true);
	  		  //alert(userGender);
	  		  }
	  	  else{
	  		  $("#userGender2").prop("checked", true);
	  		  }
	      	 $("#userAddress").val(userAddress);
	      	 //alert(userBirthday);
	      	 $("input[name='userBirthday']").val(userBirthday);	
	      	 alert(userBirthday);
	      	 $("#userMobile").val(userMobile);
	      	 $("#userEmail").val(userEmail);
	        },
	        error : function() {
	          alert('检查用户是否存在发生错误');
	        }
	      });
};
