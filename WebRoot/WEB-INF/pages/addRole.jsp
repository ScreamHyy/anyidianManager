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
<title>设置管理权限</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>

	<script type="text/javascript">
	
		$(function(){
			$("#tt").tree({
				url : 'getAllMenu',
				checkbox : true,
				onlyLeafCheck : true,
			});
		});
		
		var nodes = $('#tt').tree('getChecked');//获取checked的节点
		var s = '';
		for(var i = 0; i < nodes.length; i++){
			if(s != '') s += ',';
			s+=nodes[i].id;
		}
		
		function saveRoleInfo(){
			var nodes = $('#tt').tree('getChecked');//获取checked的节点
			var s = '';
			for(var i = 0; i < nodes.length; i++){
				if(s != '') s += ',';
				s+=nodes[i].id;
			}
			$.ajax({
					type:'post',
					url:'addUserRole',
					dataType : 'json',
			        data: {
			        	str : s
			        },
			        cache:false,
			        success: function (result) {
						if (result.success) {
							$.messager.alert("系统提示", result.success);
							window.location.href="manager";
						} else {
							$.messager.alert("系统提示", result.errorMsg);
						}
			        }
       			});
		}
		
		function getChecked(){
			var nodes = $('#tt').tree('getChecked');//获取checked的节点
			var s = '';
			for(var i = 0; i < nodes.length; i++){
				if(s != ''){
					s += ',';
				} 
				s+=nodes[i].id;
			}
			alert(s);
		}
	</script>
	
	<ul id="tt"></ul>
	<div style="margin:8px 8px 16px 8px;">
		<a href="javascript:saveRoleInfo();" class="easyui-linkbutton"
			iconCls="icon-save">确认</a>
	</div>
</body>
</html>

