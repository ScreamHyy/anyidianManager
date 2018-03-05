<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" import="com.anyidian.util.StringUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>商家详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
</head>

<body style="margin: 5px;">
	
	<script type="text/javascript">
		$(function() {
			$.extend($.fn.filebox.methods, {  
			    getFile: function (myself) {
			        var temp = $(myself).next().children("[type='file']");  
			        var file = document.getElementById(temp.attr("id"));  
			  
			        if (file.files.length > 0) {  
			            // 若选中一个文件，则返回该文件的File对象  
			            return file.files[0];  
			        }  
			        // 若未选中任何文件，则返回null  
			        return null;  
			    }  
			});
			
			//显示原本的图片
			if(${shop.image != null && shop.image != ''}) {
				$("#imageShow").attr("height", "400px");
				$("#imageShow").attr("src", "${shop.image}");
				$("#imagePage").attr("href", "${shop.image}");
			}
			
			//显示选中的图片
			$("#imageUrl").filebox({
			    onChange: function (event) { 
			        // 获取所选文件的File对象  
			        var file = $(this).filebox("getFile");  
			        var reader = new window.FileReader();
			        if (file != null) {
			            // 创建FileReader对象  
			            // 定义reader的onload事件  
			            // 当读完文件信息后触发onload事件  
			            reader.onload = function (e) {  
			                // reader.result保存着产生的虚拟URL 
			                divPreview.style.display = 'block'; //显示 
			                $("#imageShow").attr("height", "400px");
			                $("#imageShow").attr("src", this.result);
			                $("#imagePage").attr("href", this.result);
			            }  
			            // 读取指定文件并形成URL
			            reader.readAsDataURL(file);
			        }
			    }
			});  
		});
		
		//删除图片按钮
		function deleteImage() {
			$("#image").val("");
			$("#imageUrl").textbox("setValue","");
			//$("#imageShow").attr("height", "0");
			$("#imageShow").attr("src", "");
			divPreview.style.display = 'none'; //隐藏
		}
			
		//保存信息
		function saveShop() {
			var communityStr = $("#community").combobox("getText");
			$("#community").combobox("setValue", communityStr);
			//$("#communityStr").val(communityStr); 
			$("#frm_info").form("submit", {
			    url:'saveShop',
	            async: false,
	            cache: false,
	            contentType: false,
			    onSubmit: function() {
			        return $(this).form('validate'); 
			    },
			    success:function(result){
			    	var result = JSON.parse(result);
					if (result.errorMsg) {
						$.messager.alert("系统提示", result.errorMsg);
						return;
					} else {
						//$.messager.alert("系统提示", result.success)
						resetValues();
						window.location.href="businessManage";
					}
			    }
			});			
		}
			
		function resetValues() {
			$("#shopId").val("");
			$("#image").val("");
			$("#businessId").combobox("setValue",""),
			$("#community").combobox("setValue",""),
			$("#name").val("");
			$("#tel").val("");
			$("#address").val("");
			$("#imageUrl").textbox("setValue","");
		}
	</script>
	
 	<form id="frm_info" method="post" enctype="multipart/form-data" style="margin-top:15px;">
	   	<input type="hidden" name="shopId" value="${shop.shopId}" />
	   	<input type="hidden" id="image" name="image" value="${shop.image}" />
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">绑定小区：</span>
	  		<input type="hidden" id="communityStr" name="communityStr">
			<select class="easyui-combobox" id="community" name="community" style="width:200px;" multiple="multiple" data-options="editable:false,required:true">
	        	<c:forEach items="${communityList}" var="item">
	        		<option value="${item.communityName}"<c:if test="${fn:contains(shop.community,item.communityName)}">selected="selected"</c:if>>${item.communityName}</option>
	        	</c:forEach>
	        </select>
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">商家类型：</span>
			<select class="easyui-combobox" id="businessId"  name="businessId" panelHeight="auto" editable="false" style="width:200px;">
		        <c:forEach items="${businessList}" var="item">
		        	<option value="${item.businessId}" 
		        		<c:if test="${item.businessId == shop.businessId}">selected="selected"</c:if>>${item.name}
		        	</option>
		        </c:forEach>
		    </select>
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">商家名称：</span>
	  		<input type="text" id="shopName" name="shopName" value="${shop.shopName}" class="easyui-validatebox" style="width:200px;" data-options="prompt:'请输入商家名称.',required:true,validType:'maxLength[50]'" />
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">商家地址：</span>
	  		<input type="text" id="address" name="address" value="${shop.address}" class="easyui-validatebox" style="width:200px;" data-options="prompt:'请输入商家地址.',required:true,validType:'maxLength[50]'" />
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">联系电话：</span>
	  		<input type="text" id="tel" name="tel" value="${shop.tel}" class="easyui-validatebox" style="width:200px;" data-options="prompt:'请输入联系电话.',required:true,validType:'maxLength[50]'" />
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">选择图片：</span>
	  		<input id="imageUrl" name="imageUrl" class="easyui-filebox" style="width:200px;" accept="image/png,image/jpeg,image/gif"
	  			 data-options="prompt:'请选择图片',buttonText:'选择图片'" />
	  		<a href="javascript:deleteImage()" class="easyui-linkbutton"
				iconCls="icon-no" plain="false" style="margin-left:4px;"></a>
	 	</div>
	 	<div id="divPreview" style="margin:8px;">
	 		<a target="_blank" id="imagePage" style="margin:8px;">
	 			<img id="imageShow" />
	 		</a>
	 	</div>
  	</form>
   	<div style="margin:8px 8px 16px 8px;">
   		<a href="javascript:saveShop();" class="easyui-linkbutton" iconCls="icon-save">保存</a>
	</div>
</body>
</html>
