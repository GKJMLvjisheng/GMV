/**
 * 
 */

document.write("<script language=javascript src='/js/deleteConfirm.js'></script>");
$(function() {

	//初始加载
	menuAuthReady();
});



//添加角色信息
function addRole(){
  

  var param = $("#addRoleForm").serializeArray();

  $.ajax({
    url:"/doAddRole",
    method:"post",
    data:param,
    dataType:"json",
    success:function(data){  
        alert("角色添加成功")
        ready();
    },
    error:function(){
        alert("角色添加失败");
      }
  });
  
  //initRoleGrid(data);
}



//删除角色
function deleteUserById(id)
{ 
	 Ewin.confirm({ message: "确认要删除选择的数据吗？" }).on(function (e) {
		if (!e) {
		  return;
		 }
	
	var ids = [];
	ids.push(id);
	$.ajax({

		url:"/doDeleteRole",

		dataType:"json",

		traditional: true,//属性在这里设置

		method:"post",

		data:{

			"ids":ids
		},

		success:function(data){
         
            alert("删除成功！");
            ready();
		     },
	
		     error:function(){
					alert("删除失败！")
				}
	});
});
}



function initAuthTable(data)
{	


    $("#menuAuthorGrid").bootstrapTable({
    	
    	//method:"get",

		//极为重要，缺失无法执行queryParams，传递page参数

		contentType : "application/x-www-form-urlencoded",
		dataType:"json",

        //url:'/treegrid.json',
        data:data,

        striped:true,

       
        pagination:false,//显示分页条：页码，条数等

		

		pageNumber:1,//首页页码
		sidePagination:"client",//在服务器分页

		pageSize:10,//分页，页面数据条数
		pageList:[5,10, 25, 50, 100],
		

		uniqueId:"menuId",

        idField:'menuId',
       

        columns:[
      	  {

                field:'ID',

                title:'序号',
                align: 'center',
    			valign: 'middle',
    			width:'120',
                formatter: function (value, row, index) {

                    //获取每页显示的数量
                      var value="";
                    var pageSize=10;
                  	
                    //获取当前是第几页
                   

                    var pageNumber=1;
                   

                    //返回序号，注意index是从0开始的，所以要加上1
                    

                     value=pageSize*(pageNumber-1)+index+1;
                     return value;

                }

            },
            {

                field:'menuName',

                title:'模块名称',
                //align: 'center',
    			valign: 'middle',
    			width:'220',
    			formatter: function (value, row, index){
    				var value="";
    				if(row.menuParentId!=0)
    					value="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+row.menuName;
    				else{
    					value="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+row.menuName;}
    				return value;
    			}

            },{

                field:'desc',

                title:'模块描述',
                align: 'center',
    			valign: 'middle',
               

            },{

                field:'menuParentId',

                title:'顶级模块',
                	visible: false,
               

            },{

                field:'roleMenuId',

                title:'顶级模块',
                visible: false,
               

            },
	
    		 {

    			title : "启用",

    			field : "menuId",//user_allot
    			align: 'center',
    			valign: 'middle',
    			width:'120',
    			formatter: function (value, row, index){
  				var id =value;
  				var result = "";
  				if(row.menuParentId!=0)
  		        {
					
					result +="<a href='javascript:;'  onclick=\"doById('" +index+"','"+id+ "')\" ><input type='checkbox' name='menuStart' id='menuStart' class='checkbox-inline' title='启用权限'></a>";
  		        }else{
  		        	result +="<a href='javascript:;'  onclick=\"doById('" +index+"','"+id+ "')\" ><input type='checkbox' style='display:none' name='menuStart' id='menuStart' class='checkbox-inline' title='启用权限'></a>";
  		        }
  		     
  		        return result;
  			}

          },

        ],

        treeShowField: 'menuName',

        parentIdField: 'menuParentId',
    	});
   
}

function doById(index,id)

{
		console.log(allMenuData);
		var row=$("#menuAuthorGrid").bootstrapTable('getRowByUniqueId',id);

		var rowAdd = new Object();
		//rowAdd['menuName']=row.menuName;
		//rowAdd['menuParentId']=row.menuParentId;
		rowAdd['menuId']=row.menuId;
		
		
		 objS=document.getElementsByName("menuStart");
		 if(objS[index].checked)
			{ 
			 Ewin.confirm({ message: "确认要给此模块授权吗？" +<br/>+"【注意】:"+"此操作会导致该模块启用!!!"}).on(function (e) {
					if (!e) {
						$("input:checkbox[name='menuStart']").eq(index).prop("checked", false);
					  return;}
			  $.ajax({
		     
	
				url:"/api/v1/usermCenter/addRoleMenu",
				contentType : 'application/json;charset=utf8',
				dataType:"json",
	
				traditional: true,//属性在这里设置
	
				method:"post",
	
				data:JSON.stringify(rowAdd),
	
				success:function(res){
	
					if(res.code==0){
						document.getElementById("tipContent").innerHTML="模块授权成功";
						$("#Tip").modal('show');
					}else{
						$("input:checkbox[name='menuStart']").eq(index).prop("checked", false);
						document.getElementById("tipContent").innerHTML="模块授权失败";
						$("#Tip").modal('show');
					}
				},
	
				error:function(){
					$("input:checkbox[name='menuStart']").eq(index).prop("checked", false);
					alert("角色授权失败！");
				}
	
				});
			 });
		     }
		  else{
			
			Ewin.confirm({ message: "确认要取消该模块权限吗？"+<br/>+"【注意】:"+"此操作会导致该模块禁用!!!" }).on(function (e) {
					if (!e) {
						$("input:checkbox[name='menuStart']").eq(index).prop("checked", true);
					  return;}
					
			  $.ajax({
				     

					url:"/api/v1/usermCenter/deleteRoleMenu",
					contentType : 'application/json;charset=utf8',
					dataType:"json",

					traditional: true,//属性在这里设置

					method:"post",

					data:JSON.stringify(rowAdd),

					success:function(res){

						if(res.code==0){
							document.getElementById("tipContent").innerHTML="模块取消授权成功";
							$("#Tip").modal('show');
						}else{
							$("input:checkbox[name='menuStart']").eq(index).prop("checked", true);
							document.getElementById("tipContent").innerHTML="模块取消授权失败";
							$("#Tip").modal('show');
						}
					},

					error:function(){
						$("input:checkbox[name='menuStart']").eq(index).prop("checked", true);
						alert("角色取消授权失败！");
						
					}

					});
			
				});
		  }
		  

		
    }
var allMenuData;
function menuAuthReady()
{
	$('#menuAuthorGrid').bootstrapTable('destroy');
	//var allMenuData;
	var allRoleMenuData;
	var lenMenu;
	var lenRoleMenu;
	var idsIndex=[];
	var idsRole=[];
	 $.ajax({
		 //所有模块
		url: "/api/v1/usermCenter/selectAllMenus",
		contentType : 'application/json;charset=utf8',
		type: 'post',
		async: false,
		dataType: "json",
		//data:{"roleId":id,},
		success: function(res) {
			if(res.code==0)
			 {allMenuData=res.data.menuList;
			 lenMenu=allMenuData.length;}
		}, error: function(){
		}
		 }); 
	 $.ajax({
		 //所有模块权限
		url: "/api/v1/usermCenter/selectAllRoleMenus",
		contentType : 'application/json;charset=utf8',
		type: 'post',
		async: false,
		dataType: "json",
		//data:{"roleId":id,},
		success: function(res) {
			if(res.code==0){
				
				allRoleMenuData=res.data.menuList;
				lenRoleMenu=allRoleMenuData.length;
				console.log(JSON.stringify(allRoleMenuData));
			}
		}, error: function(){
		}
		 }); 
	 for(var i=0; i<lenRoleMenu;i++)
	 { for(var j=0;j<lenMenu;j++)
		 {if(allRoleMenuData[i].menuId==allMenuData[j].menuId)
			 {idsIndex.push(j);
			 idsRole.push(i);}
		 }
	 }
	 initAuthTable(allMenuData); 
	 $("#menuAuthorGrid").treegrid({

		    initialState: 'collapsed',//收缩

		    treeColumn: 1,//指明第几列数据改为树形

		    expanderExpandedClass: 'glyphicon glyphicon-triangle-bottom',

		    expanderCollapsedClass: 'glyphicon glyphicon-triangle-right',
		  
		    onChange: function() {
		    	
		    	$("#menuAuthorGrid").bootstrapTable('resetWidth');
		    	

		    }
		});
	 var leni=idsIndex.length;
	 var lenr=idsRole.length;
	 for(var k=0; k<leni;k++)

	 	{$("input:checkbox[name='menuStart']").eq(idsIndex[k]).attr('checked', 'true');}
	 
	
	 }






