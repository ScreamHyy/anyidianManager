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
	<title>店铺详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
	
	<script type="text/javascript" src="<%=path%>/resources/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="<%=path%>/resources/js/ajaxfileupload.js"></script>
	
	<style type="text/css">
		.datagrid-row {
			height: 50px;
		}
    </style>
</head>

<body style="margin: 5px;">
	
	<script type="text/javascript">
		$(function() {
			
		});
		
		//选择图片，马上预览  
	    /* function showImages(obj) {  
	        var fl = obj.files.length;
	        for(var i=0; i<fl; i++){
	            var file = obj.files[i];  
	            var reader = new FileReader();  
	            //读取文件过程方法  
	            reader.onloadstart = function (e) {  
	                console.log("开始读取....");  
	            }  
	            reader.onprogress = function (e) {  
	                console.log("正在读取中....");  
	            }  
	            reader.onabort = function (e) {  
	                console.log("中断读取....");  
	            }  
	            reader.onerror = function (e) {  
	                console.log("读取异常....");  
	            }  
	            reader.onload = function (e) {  
	                console.log("成功读取....");
	                var imgstr = '<a target="_blank" href="'+e.target.result+'"><img style="height:100px;float:left;margin:8px 4px 16px 4px;overflow:hidden;" src="'+e.target.result+'"/>';  
	                var oimgbox = document.getElementById("imageShow");
	                var ndiv = document.createElement("div");
	                ndiv.innerHTML = imgstr;  
	                ndiv.className = "img-div";
	                oimgbox.appendChild(ndiv);  
	            }  
	            reader.readAsDataURL(file);  
	        }  
	    }  */
			
		//保存信息
		function saveIntegralMall() {
			var communityStr = $("#community").combobox("getText");
			$("#community").combobox("setValue", communityStr);
			$("#frm_info").form("submit", {
			    url:'saveIntegralMall',
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
						window.location.href="integralMall";
					}
			    }
			});			
		}
			
		function resetValues() {
			$("#mallId").val("");
			$("#community").combobox("setValue",""),
			$("#title").val("");
			$("#mall").val("");
			$("#integral").val("");
			$("#marketPrice").val("");
			$("#imageUrl").textbox("setValue","");
			$("#introduce").val("");
		}
		
	</script>
	
 	<form id="frm_info" method="post" enctype="multipart/form-data" style="margin-top:15px;">
	   	<input type="hidden" name="mallId" value="${integralMall.mallId}" />
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">绑定小区：</span>
			<select class="easyui-combobox" id="community" name="community" style="width:200px;" multiple="multiple" data-options="editable:false,required:true">
	        	<c:forEach items="${communityList}" var="item">
	        		<option value="${item.communityName}"<c:if test="${fn:contains(integralMall.community,item.communityName)}">selected="selected"</c:if>>${item.communityName}</option>
	        	</c:forEach>
	        </select>
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">标题：</span>
	  		<input type="text" id="title" name="title" value="${integralMall.title}" class="easyui-validatebox" style="width:200px;" 
	  			data-options="prompt:'请输入标题.',required:true,validType:'maxLength[50]'" />
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">商城：</span>
	  		<input type="text" id="mall" name="mall" value="${integralMall.mall}" class="easyui-validatebox" style="width:200px;" 
	  			data-options="prompt:'请输入商城.',required:true,validType:'maxLength[50]'" />
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">所需积分：</span>
	  		<input type="text" id="integral" name="integral" value="${integralMall.integral}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="value=value.replace(/[^\d]/g,'')"/>
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">市场价：</span>
	  		<input type="text" id="marketPrice" name="marketPrice" value="${integralMall.marketPrice}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">选择图片：</span>
	  		<input type="text" id="imageUrl" name="imageUrl" value="${integralMall.images}" class="easyui-filebox" multiple="multiple" required="true" style="width:200px;" accept="image/png,image/jpeg,image/gif"
	  			 data-options="prompt:'请选择图片',buttonText:'选择图片'" />
            <%-- <span style="width:100px; display:inline-block;">选择图片：</span>
            <input type="file" id="imageUrl" name="imageUrl" value="${integralMall.images}" multiple="multiple" style="width:200px;"
            	onchange="showImages(this)" accept="image/gif,image/jpeg,image/png" />
		    <div class="img-box" id="imageShow"></div> --%>
	 	</div>
	 	<div style="margin:8px;">
	 		<div style="width:100px;margin:10px 0;">详情介绍：</div>
	   		<textarea id="introduce" name="introduce" class="easyui-textbox" value="${integralMall.introduce}" style="width:800px;height:320px;padding:8px;" 
	   			data-options="multiline:true,prompt:'请输入详情介绍...'" >${integralMall.introduce}</textarea>
	   	</div>
  	</form>
   	<div style="margin:8px 8px 16px 8px;">
   		<a href="javascript:saveIntegralMall();" class="easyui-linkbutton" iconCls="icon-save">保存</a>
	</div>
</body>
</html>
