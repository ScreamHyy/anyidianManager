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
<title>最新资讯详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>

	<script type="text/javascript">
	
		//增加标签页
		function addTab(title,url){
			if($('#div_tabs').tabs('exists',title)){
				closeTab(title);
			}
			$('#div_tabs').tabs('add',{
				title:title,
				href:url,
				closable:true
			});
		}
		//关闭标签页
		function closeTab(title){
			$("#div_tabs").tabs('close',title)
		}
	
		function saveAdminInfo() {
				$.messager.confirm("系统提示", "您确定添加吗？", function(r) {
					if (r) {
						$("#frm_info").form("submit", {
						    url:'saveManager',
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
									addTab("设置权限","addRole");
								}
						    }
						});
					}
				});
			
		}
		
		function resetValues() {
			$("#uname").val("");
			$("#password").val("");
			$("#rname").val("");
		}

	</script>

	
	<form id="frm_info" method="post" enctype="multipart/form-data"
		style="margin-top:15px;">
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">用户名：</span> 
			<input
				type="text" id="uname" name="uname"
				class="easyui-validatebox" style="width:200px;"
				data-options="prompt:'请输入用户名称.',required:true,validType:'maxLength[50]'" />
		</div>
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">密码：</span> 
			<input
				type="password" id="password" name="password"
				class="easyui-validatebox" style="width:200px;"
				data-options="required:true,validType:'maxLength[50]'" />
		</div>
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">管理员角色：</span> 
			<input
				type="text" id="rname" name="rname"
				class="easyui-validatebox" style="width:200px;"
				data-options="prompt:'请输入角色名称.',required:true,validType:'maxLength[50]'" />
		</div>
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">联系电话：</span> 
			<input
				type="text" id="mobile" name="mobile"
				class="easyui-validatebox" style="width:200px;"
				data-options="prompt:'请输入联系电话.',required:true,validType:'maxLength[50]'" />
		</div>
	</form>
	
	<div style="margin:8px 8px 16px 8px;">
		<a href="javascript:saveAdminInfo();" class="easyui-linkbutton"
			iconCls="icon-save">确认</a>
	</div>
</body>
</html>

