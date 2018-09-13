/**
 * 表格编辑
 */


        /** 

    * JS实现可编辑的表格   

    * 用法:EditTables(tb1,tb2,tb2,......); 

    * Create  at 2018-08-27 

    **/  
//     // 设置表格可编辑  

//     // 可一次设置多个，例如：EditTables(tb1,tb2,tb2,......)  
document.write("<script language=javascript src='js/deleteConfirm.js'></script>");
var token;
$(function ()
	{

	
	token=$("#userToken",parent.document).val();
	//data={"name":userName}
	
	$.ajax({
		   type: 'post',
		   url: '/api/v1/ethWallet/listCoin',
		  // data: JSON.stringify(data),
		   contentType : 'application/json;charset=utf8',
		   dataType: 'json',
		   cache: false,
		   success: function (res) {
			  
		     if (res.code == 0) {
		    	 
		    	 $("#userAddress").val(res.data[0].address);
		    	 var objAddress=document.getElementById("userAddress");
		    	// $("#userAddress").text(res.data[0].address);
		    	 objAddress.innerHTML=res.data[0].address;
		    	 //$("#precision").val(6000000);
		    	 var optionData=res.data;
		    	 
		    	 var len=optionData.length;
		    	 
		    	 
		    	 var selections = document.getElementById("contractSymbol");
		    	 //var string=res.data[];
		    	 for(var i =0;i<len;i++){
                     //设置下拉列表中的值的属性
                     var option = document.createElement("option");
                         option.value = optionData[i].contract;
                        
                         option.text= optionData[i].symbol;
                     //将option增加到下拉列表中。
                     selections.options.add(option);
                 }
			     
		     } else {
		    	 alert(res.message);
		     }
		   },
		   error: function (res) {
			  alert("option错误"+JSON.stringify(res));
		   },
		  
		  });
	var tabProduct = document.getElementById("tabProduct");    
	
    editTables(tabProduct);  
	});

//	$("#contractSymbol").change(function(){
//	alert($(this).children('option:selected').val()); 
//	})
	$('#contractSymbol').on('change',function(){
                //获取对应值--后期作为类选择器
		 var objContractAddress=document.getElementById("contractAddress");
		 var objMoney=document.getElementById("money");
		 var objPrecision=document.getElementById("precision");
                var thisVal = $(this).val();
                if(thisVal!="请选择")
                { $.ajax({
         		   type: 'post',
         		   url: '/api/v1/ethWallet/listCoin',
         		   //data: JSON.stringify(data),
         		   contentType : 'application/json;charset=utf8',
         		   dataType: 'json',
         		   cache: false,
         		   success: function (res) {
         			  
         		     if (res.code == 0) {
         		    	  
         		    	 var optionData=res.data;
         		    	 
         		    	 var len=optionData.length;

         		    	 for(var i =0;i<len;i++){
         		    		 
                              if(thisVal==optionData[i].contract)
                            	 {
                            	 
                  		    	// $("#userAddress").text(res.data[0].address);
                  		    	 objContractAddress.innerHTML=optionData[i].contract;
                            	  //$("#contractAddress").val(optionData[i].contract);
                  		    	
                  		    	objMoney.innerHTML=optionData[i].balance;
                            	 // $("#money").val(optionData[i].balance);
                            	  var precision=optionData[i].weiFactor;
                            	  var pre=JSON.stringify(precision).length-1;
                            	  //alert(Math.log(precision)/Math.log(10));
                            	 
                    		    	objPrecision.innerHTML=pre;
                            	  $("#precision").val(pre);
                            	  }
                          }
         			     
         		     } else {
         		    	 alert(res.message);
         		     }
         		   },
         		   error: function (res) {
         			  alert("option错误"+JSON.stringify(res));
         		   },
         		  
         		  });
                }
                else{
//              $("#contractAddress").val("");
//          	  $("#money").val("");
//        	  $("#precision").val("");
               objContractAddress.innerHTML="";
               objMoney.innerHTML="";
               objPrecision.innerHTML="";
        	  }
               
            })
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
         		    	 for(var i =0;i<len;i++){
         		    		  //设置下拉列表中的值的属性
                            var option = document.createElement("option");
                                option.value = optionData[i];
                               
                                option.text= optionData[i];
                            //将option增加到下拉列表中。
                                objNetwork.options.add(option);
                            	  }
                          }
         		     else {
         		    	 alert(res.message);
         		     }
         		   },
         		   error: function (res) {
         			  alert("option错误"+JSON.stringify(res));
         		   },
         		  
         		  });
     })
     $('#network').on('change',function(){
                //获取对应值--后期作为类选择器
		
                var thisVal = $(this).val();
                
                if(thisVal=="kovan"||thisVal=="rinkeby")
                {
                	$("#network").removeAttr("selected");//根据值去除选中状态
                	$("#network option[value='ropstrn']").prop("selected","selected");//根据值让option选中
                	thisVal = $(this).val();
                }
                data={"preferNetwork":thisVal};
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
        	 
            })
//子窗口会跳转到大页面外window.parent.location.href=url ;
    //设置多个表格可编辑  

    function editTables(){  

    for(var i=0;i<arguments.length;i++){  

       setTableCanEdit(arguments[i]);  

    }  

    }  
//解析url
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

    //设置表格是可编辑的  

    function setTableCanEdit(table){  

    for(var i=1; i<table.rows.length;i++){  

       setRowCanEdit(table.rows[i]);  

    }  

    }  

      

    function setRowCanEdit(row){  

    for(var j=0;j<row.cells.length; j++){  

      

       //如果当前单元格指定了编辑类型，则表示允许编辑  

       var editType = row.cells[j].getAttribute("EditType");  

       if(!editType){  

        //如果当前单元格没有指定，则查看当前列是否指定  

        editType = row.parentNode.rows[0].cells[j].getAttribute("EditType");  

       }  

       if(editType){  

        row.cells[j].onclick = function (){  

         editCell(this);  

        }  

       }  

    }  

      

    }  

      

    //设置指定单元格可编辑  

    function editCell(element, editType){  

      

    var editType = element.getAttribute("EditType");  

    if(!editType){  

       //如果当前单元格没有指定，则查看当前列是否指定  

       editType = element.parentNode.parentNode.rows[0].cells[element.cellIndex].getAttribute("EditType");  

    }  

      

    switch(editType){  

       case "TextBox":  

        createTextBox(element, element.innerHTML);  

        break;  

       case "DropDownList":  

        createDropDownList(element);  

        break;  

       default:  

        break;  

    }  

    }  

      

    //为单元格创建可编辑输入框  

    function createTextBox(element, value){  

    //检查编辑状态，如果已经是编辑状态，跳过  

    var editState = element.getAttribute("EditState");  

    if(editState != "true"){  

       //创建文本框  

       var textBox = document.createElement("INPUT");  //创建一个input类型的标签对象

       textBox.type = "text";  

       textBox.className="EditCell_TextBox";  

        

        

       //设置文本框当前值  

       if(!value){  

        value = element.getAttribute("Value");  

       }    

       textBox.value = value;  

        

       //设置文本框的失去焦点事件  

       textBox.onblur = function (){  

        cancelEditCell(this.parentNode, this.value);  

       }  

       //向当前单元格添加文本框  

       clearChild(element);  

       element.appendChild(textBox);  //把对象加载到body里

       textBox.focus();  

       textBox.select();  

        

       //改变状态变量  

       element.setAttribute("EditState", "true");  

       element.parentNode.parentNode.setAttribute("CurrentRow", element.parentNode.rowIndex);  

    }  

      

    }  

      

      

    //为单元格创建选择框  

    function createDropDownList(element, value){  

    //检查编辑状态，如果已经是编辑状态，跳过  

    var editState = element.getAttribute("EditState");  

    if(editState != "true"){  

       //创建下接框  

       var downList = document.createElement("Select");  

       downList.className="EditCell_DropDownList";  

        

       //添加列表项  

       var items = element.getAttribute("DataItems");  

       if(!items){  

        items = element.parentNode.parentNode.rows[0].cells[element.cellIndex].getAttribute("DataItems");  

       }  

        

       if(items){  

        items = eval("[" + items + "]");  //使用eval解析JSON对象，如果为表达式，返回表达式的值 

        for(var i=0; i<items.length; i++){  

         var oOption = document.createElement("OPTION");  

         oOption.text = items[i].text;  

         oOption.value = items[i].value;  

         downList.options.add(oOption);  

        }  

       }  

        

       //设置列表当前值  

       if(!value){  

        value = element.getAttribute("Value");  

       }  

       downList.value = value;  

      

       //设置创建下接框的失去焦点事件  

       downList.onblur = function (){  

        cancelEditCell(this.parentNode, this.value, this.options[this.selectedIndex].text);  

       }  

        

       //向当前单元格添加创建下接框  

       clearChild(element);  

       element.appendChild(downList);  

       downList.focus();  

        

       //记录状态的改变  

       element.setAttribute("EditState", "true");  

       element.parentNode.parentNode.setAttribute("LastEditRow", element.parentNode.rowIndex);  

    }  

      

    }  

      

      

    //取消单元格编辑状态  

    function cancelEditCell(element, value, text){  

    element.setAttribute("Value", value);  

    if(text){  

       element.innerHTML = text;  

    }else{  

       element.innerHTML = value;  

    }  

    element.setAttribute("EditState", "false");  

      

    //检查是否有公式计算  

    checkExpression(element.parentNode);  

    }  

      

    //清空指定对象的所有字节点  

    function clearChild(element){  

    element.innerHTML = "";  

    }  

      

    //添加行  

    function addRow(table, index){  

    var lastRow = table.rows[table.rows.length-1];  
    
    var newRow = lastRow.cloneNode(true);  

    //计算新增加行的序号，需要引入jquery 的jar包

    var startIndex = $.inArray(lastRow,table.rows);//查找lastRow在table里的索引
    
   // var endIndex = table.rows.length; 

    //alert(endIndex);
    newRow.cells[2].setAttribute("Value", 0);  
    newRow.cells[2].innerHTML=0;
    newRow.cells[3].setAttribute("Value", 0);  
    newRow.cells[3].innerHTML=0;
    table.tBodies[0].appendChild(newRow);  

    //newRow.cells[1].innerHTML=endIndex-startIndex;
    newRow.cells[1].innerHTML=table.rows.length-1;
    setRowCanEdit(newRow);  
    numberRowsInTable=table.rows.length;
    return newRow;  

      

    }  

      

      

    //删除行  

    function deleteRow(table, index){  
    var len=table.rows.length-1;
    var flag=false;
    for(var i=table.rows.length - 1; i>0;i--){  

       var chkOrder = table.rows[i].cells[0].firstChild;  

       if(chkOrder){  

        if(chkOrder.type = "CHECKBOX"){  

         if(chkOrder.checked){  
			flag=true;
          //执行删除  

          table.deleteRow(i);  
          if(i!=len)
          { var len1=table.rows.length;
        
          	 for(var j=i;j<len1;j++)
          		{
          		table.rows[j].cells[1].innerHTML=j;
          		}
          	numberRowsInTable=table.rows.length;
          	}
          }
         

         }  

        }  

       }
    if(!flag)
    	{alert("请选择要删除的行");}

    }  
       
    function selectAll(table)  
    { var chkOrder = table.rows[0].cells[0].firstChild;
    	if(chkOrder.checked)
    	{$("input:checkbox").prop("checked", true);}
    	else{$("input:checkbox").prop("checked", false);}
    	//if($("#mail").prop("checked")==true)}
    }
      

    //提取表格的值,JSON格式  

    function getTableData(table){  
    var contract=$('#contractSymbol option:selected') .val();
    if(contract=="请选择")
    {alert("请选择货币类型");
    return;}
    var gasPrice=$("#gasPrice").val();
	var gasLimit=$("#gasLimit").val();
	if(gasLimit==="")
    {
		gasLimit="";
		
    }else if(gasLimit<1)
    	{
    	alert("gasLimit请输入大于0的数或不输入");
    	 return;}
	
    var tableData = new Array();  

    for(var i=1; i<table.rows.length;i++){  

       tableData.push(getRowData(table.rows[i]));  

    }  
   
    var tableDataLen=tableData.length;
    var tableData1=[];
    var sunmary=null;
    for(var i=0;i<tableDataLen;i++)
    {
     var rowAdd={};
     rowAdd['toUserAddress']=tableData[i]['toUserAddress'];
		
     rowAdd['amount']=tableData[i]['candy'];
    sunmary=sunmary+parseInt(rowAdd['amount']);
     tableData1.push(rowAdd);
		}
    alert(JSON.stringify(tableData1))
    var userAddress=$("#userAddress").val();
    var userName=$("#userNickname",parent.document).text();
    var symbol=$('#contractSymbol option:selected') .text();
    Ewin.confirm({ message: "确认从账户【"+userName+"】的地址【"+userAddress+"】转到【"+tableDataLen+"】个目标账户，总金额为【"+sunmary+"】"+symbol+"." }).on(function (e) {
		if (!e) {
		  return;
		 }
		
    data={
    		"contract": contract,
    		"gasPrice":gasPrice,
    		"gasLimit":gasLimit,
    	    "quota":tableData1,
    	};
    alert(token);
    alert(JSON.stringify(data));
    
    transfer(data);
    });
    //return tableData;  

    }  
    //转账接口
    function transfer(data)
    {
    	 
    $.ajax({

		url:"/api/v1/ethWallet/multiTtransfer",
		headers: {'Authorization': token},

		contentType : 'application/json;charset=utf8',
		dataType: 'json',
		cache: false,
		type: 'post',
		data:JSON.stringify(data),
			
		success:function(res){
			alert("success"+JSON.stringify(res));
			if(res.code==0)
         {  
				//alert("转账成功！");
				res.data.taxHash;
				$("#Tip").modal('show');
				document.getElementById("tipContent").innerText="恭喜您，转账成功";
         }
         else{
        	 alert("转账失败");
         	}
		     
		},
	
		error:function(){
					alert("请求失败！")
				}
	}); 
    }

    //提取指定行的数据，JSON格式  

    function getRowData(row){  

    var rowData = {};  

    for(var j=0;j<row.cells.length; j++){  

       name = row.parentNode.rows[0].cells[j].getAttribute("Name");  

       if(name){  

        var value = row.cells[j].getAttribute("Value");  

        if(!value){  

         value = row.cells[j].innerHTML;  

        }  

         

        rowData[name] = value;  

       }  

    }  

   // alert("ProductName:" + rowData.ProductName);  

    //或者这样：alert("ProductName:" + rowData["ProductName"]);  
    

    return rowData;  

      

    }  

      

    //检查当前数据行中需要运行的字段  

    function checkExpression(row){  

    for(var j=0;j<row.cells.length; j++){  

       expn = row.parentNode.rows[0].cells[j].getAttribute("Expression");  //表达式

       //如指定了公式则要求计算  

       if(expn){  

       // var result = Expression(row,expn);  

        var format = row.parentNode.rows[0].cells[j].getAttribute("Format");  

        if(format){  

         //如指定了格式，进行字值格式化  

         row.cells[j].innerHTML = formatNumber(expression(row,expn), format);  

        }else{  

         row.cells[j].innerHTML = expression(row,expn);  

        }  

       }  

        

    }  

    }  

      

    //计算需要运算的字段  

    function expression(row, expn){  

    var rowData = getRowData(row);  

    //循环代值计算  

    for(var j=0;j<row.cells.length; j++){  

       name = row.parentNode.rows[0].cells[j].getAttribute("Name");  
      
       if(name){  
		//alert("111");
        var reg = new RegExp(name, "i");  //如果regexp(a,b)里a是是正则表达式，那么b可省略；b包含属性 "g"、"i" 和 "m"，分别用于指定全局匹配、区分大小写的匹配和多行匹配
    	   
        expn = expn.replace(reg, rowData[name].replace(/\,/g, ""));  //用reg表达式或字符串去匹配expn，匹配上了用rowData[name].replace取代
        //expn = expn.replace(reg, rowData[name]);rowData[name]是值 
       
       }  
    } 
    //alert($("#precision").val());
    var reg = new RegExp("precision", "i");
    expn = expn.replace(reg, $("#precision").val());
   // Math.pow(2,4);//返回的是浮点数，最好再取整

    //var c=Math.round(Math.pow(2,4)); 
    var value=eval(expn)
    return Math.round(value);  

    }  

      

    ///////////////////////////////////////////////////////////////////////////////////  

    /** 

    * 格式化数字显示方式   

    * 用法 

    * formatNumber(12345.999,'#,##0.00'); 

    * formatNumber(12345.999,'#,##0.##'); 

    * formatNumber(123,'000000'); 

    * @param num 

    * @param pattern 

    */  

    /* 以下是范例 

    formatNumber('','')=0 

    formatNumber(123456789012.129,null)=123456789012 

    formatNumber(null,null)=0 

    formatNumber(123456789012.129,'#,##0.00')=123,456,789,012.12 

    formatNumber(123456789012.129,'#,##0.##')=123,456,789,012.12 

    formatNumber(123456789012.129,'#0.00')=123,456,789,012.12 

    formatNumber(123456789012.129,'#0.##')=123,456,789,012.12 

    formatNumber(12.129,'0.00')=12.12 

    formatNumber(12.129,'0.##')=12.12 

    formatNumber(12,'00000')=00012 

    formatNumber(12,'#.##')=12 

    formatNumber(12,'#.00')=12.00 

    formatNumber(0,'#.##')=0 

    */  

    function formatNumber(num,pattern){    

    var strarr = num?num.toString().split('.'):['0'];   //toString() 方法可把一个逻辑值转换为字符串，并返回结果。split()是使对象按这个方式划分 
    

    var fmtarr = pattern?pattern.split('.'):[''];    
   
    var retstr='';    

        

    // 整数部分    

    var str = strarr[0];    

    var fmt = fmtarr[0];    

    var i = str.length-1;      

    var comma = false;    

    for(var f=fmt.length-1;f>=0;f--){    
    	alert("lenf"+f);
        switch(fmt.substr(f,1)){   //substr() 方法可在字符串中抽取从 start 下标开始的指定数目的字符。substr() 的参数指定的是子串的开始位置和长度 

          case '#':    

            if(i>=0 ) retstr = str.substr(i--,1) + retstr;  //循环得到sum的整数  
			alert("sum"+retstr);
            break;    

          case '0':    

            if(i>=0) retstr = str.substr(i--,1) + retstr;    

            else retstr = '0' + retstr;    

            break;    

          case ',':    

            comma = true;    

            retstr=','+retstr;    

            break;    

        }    

    }    
    alert("i"+i);
    if(i>=0){    

        if(comma){    

          var l = str.length;    

          for(;i>=0;i--){    

            retstr = str.substr(i,1) + retstr;    

            if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;     

          }    

        }    

        else retstr = str.substr(0,i+1) + retstr;    

    }    

        

    retstr = retstr+'.';    

    // 处理小数部分    

    str=strarr.length>1?strarr[1]:'';    

    fmt=fmtarr.length>1?fmtarr[1]:'';    

    i=0;    

    for(var f=0;f<fmt.length;f++){    

        switch(fmt.substr(f,1)){    

          case '#':    

            if(i<str.length) retstr+=str.substr(i++,1);    

            break;    

          case '0':    

            if(i<str.length) retstr+= str.substr(i++,1);    

            else retstr+='0';    

            break;    

        }    

    }    

    return retstr.replace(/^,+/,'').replace(/\.$/,'');    
    
    }  
    
    var transferData=[]; 
    $(document).ready(function(){

    $('#excelFile').change(function(e) {
    	
        var files = e.target.files;

        var fileReader = new FileReader();
        fileReader.onload = function(ev) {
            try {
                var data = ev.target.result,
                    workbook = XLSX.read(data, {
                        type: 'binary'
                    });// 以二进制流方式读取得到整份excel表格对象
                   
            } catch (e) {
                console.log('文件类型不正确');
                alert("上传文件类型不匹配");
                return;
            }
            var persons=[]; // 存储获取到的数据
            // 表格的表格范围，可用于判断表头是否数量是否正确
            var fromTo = '';
            // 遍历每张表读取
            for (var sheet in workbook.Sheets) {
            	
                if (workbook.Sheets.hasOwnProperty(sheet)) {
                    fromTo = workbook.Sheets[sheet]['!ref'];
                   
                    console.log(fromTo);
                    persons = persons.concat(XLSX.utils.sheet_to_json(workbook.Sheets[sheet]));
                    // break; // 如果只取第一张表，就取消注释这行
                }
            }
            console.log(persons);
           
            
          
            var headStr = '_to(address),Number of Candy,Decimals of Precision';
            
            
            for (var i=0; i<persons.length; i++) {
            	
            	
                if (Object.keys(persons[i]).join(',') !== headStr) {//join(',')数组创建字符串，用逗号来隔离
                	
                    persons.splice(i, 1);//splice() 方法向/从数组中添加/删除项目，然后返回被删除的项目。第i行的数据被删掉
                    i--;
                }
            }

            console.log(persons);
           // if (fromTo[0] === 'A' && fromTo[3] === 'C') {
           //     excelIsOk = true;
          // }
           
            var excelData = new Array();
            var personsLen=persons.length
            for(var i=0;i<personsLen;i++)
           {
            var rowAdd={};
            rowAdd['toUserAddress']=persons[i]['_to(address)'];
    		var amount=persons[i]['Number of Candy'];
    			//persons[i]['Number of Candy']*Math.pow(10,persons[i]['Decimals of Precision'])
    		rowAdd['amount']=amount;
    		//alert(JSON.stringify(rowAdd));
    		excelData.push(rowAdd);
    		}
            transferData=excelData;
            
        };

        // 以二进制方式打开文件
        fileReader.readAsBinaryString(files[0]);
    });
    });
    function getExcelData()
    {	
    	
    	 var contract=$('#contractSymbol option:selected') .val();
    	 if(contract=="请选择")
    	    {alert("请选择货币类型");
    	    return;}
    		Ewin.confirm({ message: "确认要提交导入的excel数据吗？" }).on(function (e) {
    			if (!e) {
    			  return;
    			 }
    	    data={
    	    		"contract": contract,
    	    	    "quota":transferData,
    	    	};
    	    alert(JSON.stringify(data));
    	   transfer(data);
    		});
    	
    }
    //表格
  $(function(){
	 
    $('#excelTable').bootstrapTable({
        //url: createUrl(''),
        striped: true,
        uniqueId: 'attrValue',
        pagination: true,
        pageSize:3,//分页，页面数据条数
        data:[{"index":"2","attrValue":"123","sellPrice":"100"}],
        columns: [{
            title: 'ID',
            field: 'index',
            formatter: formatterIndex
        },{
            title: '规格',
            field: 'attrValue',
            class: 'editable',
            //editable:true,

        },{
            title: '价格',
            field: 'sellPrice',
            class: 'editable'
        },{
            title: '操作',
            field: 'operate',
            formatter: formatterOperate
        }]
    });
  });
    function formatterIndex(value, row, index){
    	
        var i = index + 1;
        if(i < 10){
            return "0" + i;
        }else{
            return i;
        }
    }
    function formatterOperate(value, row, index){
        return "<button onclick='saveRow("+index+")' class='btn small'><i class='fa fa-edit'></i> 保存</button><button onclick='editRow("+index+")' class='btn small blue'><i class='fa fa-edit'></i> 编辑</button><button onclick='delRow(\""+row.attrValue+"\")' class='btn small red'><i class='fa fa-trash-o'></i> 删除</button>";
// var result = "";
//        
//        result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"editRow('" + index + "')\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
//        result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"delRow('" + row.attrValue + "')\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";
//
//        return result;
    }
    var num = 1;  //计数器
   function add(){
    	alert("1");
        var data = {
            attrValue: '',
            sellPrice: ''
        };
        //$('#transferTable').bootstrapTable('selectPage', 1);
       // $('#transferTable').bootstrapTable('prepend', data);
        $('#transferTable').bootstrapTable('insertRow',{
            index : num,
            row : data
        });
        //num++;
       // $("#transferTable").bootstrapTable('append', data);
        $("#transferTable tr:first-child td.editable").each(function(){
        	alert("add");
            $(this).html("<input>");
        });
        //$("#dataTable tr:eq(1) td:eq(0)").trigger("dblclick");

       // $("#dataTable input")[0].focus();
    }
    //var num = 1;  //计数器
    function primaryAssets(){
        var data = {   //要插入的数据，这里要和table列名一致
                ID : num,
                recUid : 1,
                assetsType : $('#assetsType').val(),  //获取模态框input的值
                assetsAmt : $('#assetsAmt').val(),
                coefficient : $('#coefficient').val(),
                valueAmt : $('#valueAmt').val()
        }
        $('#tb_assets').bootstrapTable('insertRow',{
            index : num,
            row : data
        });
        num++;
       }
    function saveRow(index, value){
        var obj = $("#transferTable tr:nth-child("+ (index+1) +") td.editable");
        var attrValue = obj.first().find("input").val().trim();
        var sellPrice = obj.last().find("input").val().trim();
        var newData = {
            attrValue: attrValue,
            sellPrice: sellPrice
        };
        $("#transferTable").bootstrapTable('updateRow', {
            index: index,
            row: newData
        });
        obj.find("input").remove();
    }
    function editRow(index){
    	alert("1");
        $("#transferTable tr:nth-child("+ (index+1) +") td.editable").each(function(){
        	alert("2");
            var value = $(this).text();
            $(this).html("<input value='"+value+"'>");
        });
    }
    function delRow(value){
        $("#transferTable").bootstrapTable('removeByUniqueId', value);
    }
function display1()
{document.getElementById("page2").style.display="none";
document.getElementById("page1").style.display="block";
$('#btn1').removeClass('active1').addClass('active');
$('#btn2').removeClass('active').addClass('active1');
}
function display2()
{
	//$("#page2").attr()
	document.getElementById("page1").style.display="none";
	document.getElementById("page2").style.display="block";
	$('#btn2').removeClass('active1').addClass('active');
	$('#btn1').removeClass('active').addClass('active1');
}