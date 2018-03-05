<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.anyidian.util.StringUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>"></base>
<title>招标信息详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>

	<script type="text/javascript">
		//判断是否为空 空格
		function JTrim(s) {  
		    return s.replace(/(^\s*)|(\s*$)/g, "");  
		}
		
		//保存信息
		function saveNationalMarketingInfo() {
			var detail = $("#detail").val();
			if(JTrim(detail) == "") {
				$.messager.confirm("系统提示", "您确定不在消息详情里说点什么吗？", function(r) {
					if (r) {
						$("#frm_info").form("submit", {
						    url:'saveNationalMarketing',
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
									window.location.href="nationalMarketing";
								}
						    }
						});
					}
				});
			} else {
				var detailStr = detail.replace("//r/n/gi", "/n/n");
				$("#detail").textbox("setValue",detailStr);
				$("#frm_info").form("submit", {
				    url:'saveNationalMarketing',
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
							window.location.href="nationalMarketing";
						}
				    }
				});
			}
		}
		
		function resetValues() {
			$("#marketingId").val("");
			$("#houseType").combox("");
			$("#houseNum").val("");
			$("#name").val("");
			$("#mobile").val("");
			$("#toName").val("");
			$("#toMobile").val("");
		}
	</script>

	<form id="frm_info" method="post" enctype="multipart/form-data"
		style="margin-top:15px;">
		<input type="hidden" name="marketingId" value="${marketing.marketingId}" />
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">标题：</span>
			 <input type="text" id="title" name="title" value="${marketing.title}" class="easyui-validatebox" style="width:200px;"
				data-options="prompt:'请输入消息标题.',required:true,validType:'maxLength[50]'" />
		</div>
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">选择图片：</span> <input
				id="imageUrl" name="imageUrl" class="easyui-filebox"
				style="width:200px;" accept="image/png,image/jpeg,image/gif"
				data-options="prompt:'请选择图片',buttonText:'选择图片'" /> <a
				href="javascript:deleteImage()" class="easyui-linkbutton"
				iconCls="icon-no" plain="false" style="margin-left:4px;"></a>
		</div>
		<div id="divPreview" style="margin:8px;">
			<a target="_blank" id="imagePage" style="margin:8px;"> <img
				id="imageShow" />
			</a>
		</div>
		<div style="margin:8px;">
			<div style="width:100px;margin:10px 0;">内容：</div>
			<textarea id="detail" name="detail" class="easyui-textbox"
				style="width:800px;height:320px;padding:8px;"
				data-options="multiline:true,prompt:'请输入消息详情...'">${marketing.detail}</textarea>
		</div>
	</form>
	<div style="margin:8px 8px 16px 8px;">
		<a href="javascript:saveNationalMarketingInfo();" class="easyui-linkbutton"
			iconCls="icon-save">保存</a>
	</div>
</body>
</html>
