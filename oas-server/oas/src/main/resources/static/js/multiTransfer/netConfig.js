/**
 * 
 */
 $(function(){
    	 $.ajax({
         		   type: 'post',
         		   url: '/api/v1/ethWallet/listNetwork',
         		   //data: JSON.stringify(data),
         		   contentType : 'application/json;charset=utf8',
         		   dataType: 'json',
         		   cache: false,
         		   success: function (res) {
         			 
         		     if (res.code == 0) {
         		    	  
         		    	 var optionData=res.data;
         		    	 
         		    	 var len=optionData.length;
         		    	 var objNetwork=document.getElementById("network");
         		    	
                   // 将option增加到下拉列表中。
         		    	 for(var i =0;i<len;i++){
         		    		  //设置下拉列表中的值的属性
                            var option = document.createElement("option");
                                option.value = optionData[i];
                               
                                option.text= optionData[i];
                            //将option增加到下拉列表中。
                                objNetwork.options.add(option); 
                            	  }
         		    	$("#network option[value='mainnet']").prop("selected","selected");//根据值让option选中
                          }
         		     else {
         		    	 alert(res.message);
         		     }
         		   },
         		   error: function (res) {
         			  alert("option错误"+JSON.stringify(res));
         		   },
         		  
         		  });
     });
      $('#network').on('change',function(){
                //获取对应值--后期作为类选择器
		
                var thisVal = $(this).val();
                
//                if(thisVal=="kovan"||thisVal=="rinkeby")
//                {
//                	$("#network").removeAttr("selected");//根据值去除选中状态
//                	$("#network option[value='ropstrn']").prop("selected","selected");//根据值让option选中
//                	thisVal = $(this).val();
//                }
                window.parent.setValue(thisVal);
                var data={"preferNetwork":thisVal};
               $.ajax({
         		   type: 'post',
         		   url: '/api/v1/ethWallet/setPreferNetwork',
         		   data: JSON.stringify(data),
         		   contentType : 'application/json;charset=utf8',
         		   dataType: 'json',
         		   cache: false,
         		   success: function (res) {
         			 
         		     if (res.code == 0) {
         		    	 
         		     } else {
         		    	 alert(res.message);
         		     }
         		   },
         		   error: function (res) {
         			  alert("option错误"+JSON.stringify(res));
         		   },
         		  
         		  });
        	 
            });
  function getValue(){
	  var network=$("#network").val();
	  return network;
  }