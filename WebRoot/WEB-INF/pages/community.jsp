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
<title>小区信息管理</title>
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
	
	//删除小区
	function deleteCommunity() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].communityId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteCommunityData',
					dataType: "json",
			        data: {
			        	delIds : ids
			        },
			        cache:false,
			        success: function (result) {
						if (result.success) {
							$.messager.alert("系统提示", "您已成功删除<font color=red>"+selectedRows.length+"</font>条数据");
							$("#dataGrid").datagrid("reload");
						} else {
							$.messager.alert("系统提示", result.errorMsg);
						}
			        }
       			});
			}
		});
	}
	
	function openCommunityAddDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加小区");
		url = "saveCommunityData";
	}
	
	function closeCommunityDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}

	//保存缴费单
	function saveCommunity() {
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
		$("#communityName").val("");
		$("#committee").val("");
		$("#city").val("");
		$("#propertyName").val("");
		$("#mobile").val("");
		$("#address").val("");
		$("#landArea").val("");
		$("#buildArea").val("");
	}

	function openCommunityModifyDialog() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "编辑小区");
		$("#fm").form("load", row);
		url = "saveCommunityData?communityId=" + row.communityId;
	}
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="小区信息" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'communityListData', 
		pageSize:'20', 
		fitColumns:true, 
		pagination:true, 
		rownumbers:true,
		singleSelect:false,
		fit:true,
		toolbar:'#tb'">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<!-- <th field="communityId" width="100" align="center">编号</th> -->
				<th field="city" width="100" align="center">城市</th>
				<th field="communityName" width="100" align="center">小区</th>
				<th field="committee" width="100" align="center">居委会</th>
				<th field="propertyName" width="100" align="center">物业名称</th>
				<th field="mobile" width="100" align="center">联系电话</th>
				<th field="address" width="200" align="center">地址</th>
				<th field="landArea" width="100" align="center">土地面积(m²)</th>
				<th field="buildArea" width="100" align="center">建筑面积(m²)</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openCommunityAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> 
			<a href="javascript:openCommunityModifyDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteCommunity()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
	</div>
		
		<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
			style="width:auto; heigth:340px; padding:10px 20px;" closed="true">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td style="width:100px;">城市：</td>
						<td><input type="text" name="city" id="city" class="easyui-validatebox" required="true" style="width:150px;"/></td>
						<td style="width:40px;"></td>
						<td style="width:100px;">小区：</td>
						<td><input type="text" name="communityName" id="communityName" class="easyui-validatebox" required="true" style="width:150px;"/></td>
					</tr>
					<tr>
						<td style="width:100px;">居委会：</td>
						<td><input type="text" name="committee" id="committee" class="easyui-validatebox" required="true" style="width:150px;"/></td>
						<td></td>
						<td>物业名称：</td>
						<td><input type="text" name="propertyName" id="propertyName" class="easyui-validatebox" required="true" style="width:150px;"/></td>
					</tr>
					<tr>
						<td>联系电话：</td>
						<td><input type="text" name="mobile" id="mobile" class="easyui-validatebox" required="true" style="width:150px;"/></td>
						<td></td>
						<td>地址：</td>
						<td><input type="text" name="address" id="address" class="easyui-validatebox" required="true" style="width:150px;"/></td>
					</tr>
					<tr>
						<td>土地面积：</td>
						<td><input type="text" name="landArea" id="landArea" class="easyui-validatebox" required="true" style="width:150px;"
							onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
						<td></td>
						<td>建筑面积：</td>
						<td><input type="text" name="buildArea" id="buildArea" class="easyui-validatebox" required="true" style="width:150px;"
							onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveCommunity()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeCommunityDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
