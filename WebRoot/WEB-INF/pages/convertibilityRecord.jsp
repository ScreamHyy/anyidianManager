<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>积分兑换记录</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var url;
	$(function() {
	
	});
	
	//搜索条件查询
	function searchIntegralRecorde() {
		$('#dataGrid').datagrid('load', {
			community : $('#r_community').combobox("getValue"),
		});
	}
	
	function openIntegralRecordeAddDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加积分兑换记录");
		url = "saveIntegralRecordeData";
	}
	
	function closeIntegralRecordeDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}

	//保存缴费单
	function saveIntegralRecorde() {
		$("#fm").form("submit", {
			url : url,
			/*默认支持，少天不能提交*/
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(result) {
				var result = JSON.parse(result);
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				} else {
					$.messager.alert("系统提示", result.success)
					resetValues();
					$("#dlg").dialog("close");
					$("#dataGrid").datagrid("reload");
				}
			}
		});
	}

	function resetValues() {
		$("#user").val("");
		$("#title").val("");
		$("#integral").val("");
	}

</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="积分兑换记录" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'integralRecordeData', 
		pageSize:'20', 
		fitColumns:true, 
		pagination:true, 
		rownumbers:true,
		singleSelect:true,
		fit:true,
		toolbar:'#tb'">
		<thead>
			<tr>
				<th field="user" width="100" align="center">手机号</th>
				<th field="title" width="100" align="center">兑换描述</th>
				<th field="date" width="100" align="center">兑换时间</th>
				<th field="integral" width="100" align="center">获得积分</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openIntegralRecordeAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加积分兑换记录</a> 
		</div>
		<div style="margin:4px;">
		&nbsp;&nbsp;手机号：
		<input type="text" name="b_mobile" id="b_mobile" size="12" />
		&nbsp;&nbsp;
		<a href="javascript:searchIntegralRecorde()" class="easyui-linkbutton"
			iconCls="icon-search" plain="false">搜索</a>
	</div>
		
		<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
			style="width:auto; heigth:auto; padding:10px 20px;" closed="true">
			<form id="fm" method="post">
				<div style="margin:8px;">
			  		<span style="width:100px; display:inline-block;">手机号：</span>
			  		<input type="text" id="user" name="user" class="easyui-validatebox" style="width:200px;" data-options="prompt:'请输入手机号',required:true,validType:'maxLength[50]'" />
		 		</div>
				<div style="margin:8px;">
			  		<span style="width:100px; display:inline-block;">兑换描述：</span>
			  		<input type="text" id="title" name="title" class="easyui-validatebox" style="width:200px;" data-options="prompt:'请输入兑换描述.',required:true" />
			 	</div>
			 	<div style="margin:8px;">
			  		<span style="width:100px; display:inline-block;">获得积分：</span>
			 		<input name="integral" id="integral" class="easyui-validatebox" required="true" placeholder="请输入整数" style="width:200px;" 
						onkeyup="value=value.replace(/[^\-?\d]/g,'')" />
			 	</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveIntegralRecorde()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeIntegralRecordeDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
