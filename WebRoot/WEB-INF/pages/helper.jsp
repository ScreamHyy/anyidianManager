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
<title>生活助手管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" 
	src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var url;
	$(function() {
	
	});
	
	//搜索条件查询
	function searchHelper() {
		$('#dataGrid').datagrid('load', {
			helperType : $('#h_helperType').val(),
		});
	}
	
	//图片图标
	function showIcon(value,row){
		return '<img src='+value+' style="width:50px; height:50px; border-radius:100%;"/>'; 
	}
	
	//图片显示
	function showImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<img src='+value+' style="width:50px; height:50px;"/>'; 
	}

	//删除缴费单
	function deleteHelper() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].helperId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteHelperData',
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
	
	function openHelperAddDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加生活助手");
		url = "saveHelperData";
	}
	
	function closeHelperDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}

	//保存缴费单
	function saveHelper() {
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
		$("#helperId").val("");
		$("#icon").val("");
		$("#helperType").val("");
		$("#website").val("");
		$("#iconUrl").textbox("setValue","");
	}

	function openHelperModifyDialog() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "编辑生活助手");
		$("#fm").form("load", row);
		url = "saveHelperData?helperId=" + row.helperId;
	}
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="生活助手" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'helperListData', 
		pageSize:'20', 
		fitColumns:true, 
		pagination:true,	//分页控件 
		rownumbers:true,	//行号
		singleSelect:false,
		fit:true,
		toolbar:'#tb'">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<!-- <th field="helperId" width="100" align="center">编号</th> -->
				<th field="icon" width="100" align="center" formatter="showIcon">分类图标</th>
				<th field="helperType" width="100" align="center">分类</th>
				<th field="website" width="100" align="center">网址</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openHelperAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> 
			<a href="javascript:openHelperModifyDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteHelper()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div style="margin:4px;">
			&nbsp;&nbsp;分类：
			<input type="text" name="h_helperType" id="h_helperType" size="12" />
			&nbsp;&nbsp;
			<a href="javascript:searchHelper()" class="easyui-linkbutton"
				iconCls="icon-search" plain="false">搜索</a>
		</div>

		<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
			style="width:auto; heigth:auto; padding:10px 20px;" closed="true">
			<form id="fm" method="post" enctype="multipart/form-data">
				<div style="margin:8px;">
					<span style="display:inline-block;">分类：</span>
					<input type="text" name="helperType" id="helperType" class="easyui-validatebox" required="true" style="width:200px;" />
				</div>
				<div style="margin:8px;">
					<span style="display:inline-block;">网址：</span>
					<input type="text" name="website" id="website" class="easyui-validatebox" required="true" style="width:200px;" />
				</div>
				<div style="margin:8px;">
					<input type="hidden" id="helperId" name="helperId" />
					<input type="hidden" id="icon" name="icon" />
			  		<span style="display:inline-block;">图标：</span>
			  		<input type="text" id="iconUrl" name="iconUrl" class="easyui-filebox" style="width:200px;" accept="image/png,image/jpeg,image/gif"
			  			 data-options="prompt:'请选择图片',buttonText:'选择图片'" />
			  	</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveHelper()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeHelperDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
