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
	<title>商品详情</title>
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
		
		//保存信息
		function saveCommodity() {
			var communityStr = $("#community").combobox("getText");
			$("#community").combobox("setValue", communityStr);
			$("#frm_info").form("submit", {
			    url:'saveCommodity',
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
						window.location.href="commodity";
					}
			    }
			});			
		}
			
		function resetValues() {
			$("#commodityId").val("");
			$("#name").val("");
			$("#supplier").val("");
			$("#shop").combobox("setValue","");
			$("#nowPrice").val("");
			$("#oldPrice").val("");
			$("#supplyPrice").val("");
			$("#fee").val("");
			$("#introduce").val("");
			$("#stockNum").val("");
		}
		
	</script>
	
 	<form id="frm_info" method="post" enctype="multipart/form-data" style="margin-top:15px;">
	   	<input type="hidden" name="commodityId" value="${commodity.commodityId}" />
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">所属商户：</span>
			<select class="easyui-combobox" id="shop" name="shop" style="width:200px;" multiple="multiple" data-options="editable:false,required:true">
	        	<c:forEach items="${communityList}" var="item">
	        		<option value="${item.shop}"<c:if test="${fn:contains(commodity.shop,item.shop)}">selected="selected"</c:if>>${item.shop}</option>
	        	</c:forEach>
	        </select>
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">供应商：</span>
	  		<input type="text" id="supplier" name="supplier" value="${commodity.supplier}" class="easyui-validatebox" style="width:200px;" 
	  			data-options="prompt:'请输入标题.',required:true,validType:'maxLength[50]'" />
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">商品名称：</span>
	  		<input type="text" id="name" name="name" value="${commodity.name}" class="easyui-validatebox" style="width:200px;" 
	  			data-options="prompt:'请输入商城.',required:true,validType:'maxLength[50]'" />
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">选择图片：</span>
	  		<input type="text" id="imageUrl" name="imageUrl" value="${commodity.images}" class="easyui-filebox" multiple="multiple" required="true" style="width:200px;" accept="image/png,image/jpeg,image/gif"
	  			 data-options="prompt:'请选择图片',buttonText:'选择图片'" />
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">状态：</span>
	  		<input type="text" id="state" name="state" value="${commodity.state}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="value=value.replace(/[^\d]/g,'')"/>
	 	</div>
	   	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">市场价：</span>
	  		<input type="text" id="oldPrice" name="oldPrice" value="${commodity.oldPrice}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">现售价：</span>
	  		<input type="text" id="nowPrice" name="nowPrice" value="${commodity.nowPrice}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">供货价：</span>
	  		<input type="text" id="supplyPrice" name="supplyPrice" value="${commodity.supplyPrice}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">手续费：</span>
	  		<input type="text" id="fee" name="fee" value="${commodity.fee}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
	 	</div>
	 	<div style="margin:8px;">
	  		<span style="width:100px; display:inline-block;">商品库存：</span>
	  		<input type="text" id="stockNum" name="stockNum" value="${commodity.stockNum}" class="easyui-validatebox" 
	  			required="true" style="width:200px;" onkeyup="value=value.replace(/[^\d]/g,'')"/>
	 	</div>
	 	<div style="margin:8px;">
	 		<div style="width:100px;margin:10px 0;">详情介绍：</div>
	   		<textarea id="introduce" name="introduce" class="easyui-textbox" value="${commodity.introduce}" style="width:800px;height:320px;padding:8px;" 
	   			data-options="multiline:true,prompt:'请输入详情介绍...'" >${commodity.introduce}</textarea>
	   	</div>
  	</form>
   	<div style="margin:8px 8px 16px 8px;">
   		<a href="javascript:saveCommodity();" class="easyui-linkbutton" iconCls="icon-save">保存</a>
	</div>
</body>
</html>
