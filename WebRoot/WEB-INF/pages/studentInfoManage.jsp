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
<title>学生信息管理</title>
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
	function searchStudent() {
		$('#dg').datagrid('load', {
			stuNo : $('#stuNo').val(),
			stuName : $('#stuName').val(),
			sex : $('#sex').combobox("getValue"),
			bbirthday : $('#bbirthday').datebox("getValue"),
			ebirthday : $('#ebirthday').datebox("getValue"),
			gradeId : $('#gradeId').combobox("getValue")
		});
	}

	function deleteStudent() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].stuId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？", function(r) {
			if (r) {
				$.post("studentDelete", {delIds : ids}, function(result) {
					if (result.success) {
						$.messager.alert("系统提示", "您已成功删除" + result.delNums + "条数据");
						$("#dg").datagrid("reload");
					} else {
						$.messager.alert("系统提示", result.errorMsg);
					}
				}, "json");
			}
		});
	}

	function openStudentAddDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加学生信息");
		url = "studentSave";
	}
	
	function closeStudentDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}

	function saveStudent() {
		$("#fm").form("submit", {
			url : url,
			/*默认支持，少天不能提交*/
			onSubmit : function() {
				if ($('#sex').combobox("getValue") == "") {
					$.messager.alert("系统提示", "请选择性别");
					return fasle;
				}
				if ($('#gradeId').combobox("getValue") == "") {
					$.messager.alert("系统提示", "请选择所属班级");
					return fasle;
				}
				return $(this).form("validate");
			},
			success : function(result) {
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				} else {
					$.messager.alert("系统提示", "保存成功")
					resetValues();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}
			}
		});
	}

	function resetValues() {
		$("#stuNo").val("");
		$("#stuName").val("");
		$("#sex").combobox("setValue","");
		$("#birthday").datebox("setValue","")
		$("#gradeId").combobox("setValue","");
		$("#email").val("");
		$("#stuDesc").val("");
	}

	function openStudentModifyDialog() {
		var selectedRows=$("#dg").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "编辑班级信息");
		$("#fm").form("load", row);
		url = "studentSave?stuId=" + row.stuId;
	}
</script>
</head>

<body style="margin: 5px;">
	<table id="dg" title="学生信息" class="easyui-datagrid" fitColumns="true"
		pagination="true" rownumbers="true" url="studentList" fit="false"
		toolbar="#tb">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<th field="stuId" width="50" align="center">编号</th>
				<th field="stuNo" width="100" align="center">学号</th>
				<th field="stuName" width="100" align="center">名字</th>
				<th field="sex" width="100" align="center">性别</th>
				<th field="birthday" width="100" align="center">出生年月</th>
				<th field="gradeName" width="100" align="center">班级名称</th>
				<th field="gradeId" width="100" align="center" hidden="true">班级ID</th>
				<th field="email" width="200" align="center">邮箱</th>
				<th field="stuDesc" width="250" align="center">学生备注</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div>
			<a href="javascript:openStudentAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> 
			<a href="javascript:openStudentModifyDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteStudent()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div>
			&nbsp;学号：<input type="text" name="s_stuNo" id="s_stuNo" size="12" />
			&nbsp;&nbsp;姓名：<input type="text" name="s_stuName" id="s_stuName"
				size="12" /> &nbsp;&nbsp;性别：<select class="easyui-combobox"
				id="s_sex" name="s_s_sex" editable="false" panelHeight="auto">
				<option value="">请选择...</option>
				<option value="男">男</option>
				<option value="女">女</option>
				<option value="人妖">人妖</option>
			</select> &nbsp;&nbsp;出生日期：<input class="easyui-datebox" name="s_bbirthday"
				id="s_bbirthday" size="10" editable="true" />&nbsp;--&nbsp;<input
				class="easyui-datebox" name="s_ebirthday" id="s_ebirthday" size="10"
				editable="true" /> 
				&nbsp;&nbsp;所属班级：<input class="easyui-combobox"
				id="s_gradeId" name="s_gradeId" size="10"
				data-options="panelHeight:'auto', editable:false, valueField:'id', textField:'gradeName', url:'gradeComboList'" />
			<a href="javascript:searchStudent()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a>
		</div>

		<div id="dlg" class="easyui-dialog"
			style="width:570px; heigth:350px; padding:10px 20px;" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>学号：</td>
						<td><input type="text" name="stuNo" id="s_stuNo"
							class="easyui-validatebox" required="true" /></td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>姓名：</td>
						<td><input type="text" name="stuName" id="s_stuName"
							class="easyui-validatebox" required="true" /></td>
					</tr>
					<tr>
						<td>性别：</td>
						<td><select class="easyui-combobox" id="sex" name="sex"
							editable="false" panelHeight="auto" style="width:155px;">
								<option value="">请选择...</option>
								<option value="男">男</option>
								<option value="女">女</option>
								<option value="人妖">人妖</option>
						</select></td>
						<td></td>
						<td>出生日期：</td>
						<td><input class="easyui-datebox" name="birthday"
							id="birthday" editable="true" required="true" /></td>
					</tr>
					<tr>
						<td>班级名称：</td>
						<td><input class="easyui-combobox" id="gradeId"
							name="gradeId"
							data-options="panelHeight:'auto', editable:false, valueField:'id', textField:'gradeName', url:'gradeComboList'" /></td>
						<td></td>
						<td>邮箱：</td>
						<td><input type="text" name="email" id="email"
							class="easyui-validatebox" required="true" validType="email"></input></td>
					</tr>
					<tr>
						<td valign="top">学生备注：</td>
						<td colspan="4"><textarea rows="7" cols="50" name="stuDesc"
								id="stuDesc"></textarea></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveStudent()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeStudentDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
