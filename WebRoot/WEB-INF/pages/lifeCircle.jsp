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
<title>本地生活圈管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
	
<style type="text/css">
	.datagrid-row {
		height: 50px;
	}
</style>
	
<script type="text/javascript">
	var url;
	$(function() {
	
	});
	
	//图片图标
	function showIcon(value,row){
		return '<img src='+value+' style="width:50px; height:50px; border-radius:100%;"/>'; 
	}
	
	//图片显示
	function showImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<a target="_blank" href="'+value+'"><img src='+value+' style="width:80px; height:50px;"/>'; 
	}
	
	//搜索条件查询
	function searchLifeCircle() {
		$('#dataGrid').datagrid('load', {
			name : $('#l_name').val(),
			typeName : $('#l_typeId').combobox("getValue"),
		});
	}

	//删除
	function deleteLifeCircle() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].lifeId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteLifeCircle',
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
	
	function closeTab(title){
		$('#div_tabs').tabs('close',title);
	}
	
	function openLifeCircleAdd() {
		closeTab("修改项目信息");
		addTab("新增项目信息","addLifeCircle");
	}
	
	function openLifeCircleModify() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		//$("#dlg").dialog("open").dialog("setTitle", "编辑项目信息");
	    closeTab("新增项目信息");
		addTab("修改项目信息","addLifeCircle?lifeId="+row.lifeId);
	}
	
	///////////////////////////////////////////////////////////////商家分类
	//删除商家分类
	function deleteLifeType() {
		var selectedRows = $("#dataGrid_item").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].itemId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteLifeType',
					dataType: "json",
			        data: {
			        	delIds : ids
			        },
			        cache:false,
			        success: function (result) {
						if (result.success) {
							$.messager.alert("系统提示", "您已成功删除<font color=red>"+selectedRows.length+"</font>条数据");
							$("#dataGrid_item").datagrid("reload");
						} else {
							$.messager.alert("系统提示", result.errorMsg);
						}
			        }
       			});
			}
		});
	}
	
	function openLifeTypeAdd() {
		$("#dlg").dialog("open").dialog("setTitle", "添加商家类型");
		url = "saveLifeTypeData";
	}
	
	function closeLifeType() {
		$("#dlg").dialog("close");
		resetValues()
	}
	
	function openLifeTypeModify() {
		var selectedRows = $("#dataGrid_item").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "编辑商家类型");
		$("#fm").form("load", row);
		url = "saveLifeTypeData?itemId=" + row.itemId;
	}

	//保存商家类型
	function saveLifeType() {
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
					$("#dataGrid_item").datagrid("reload");
				}
			}
		});
	}
	
	function resetValues() {
		$("#name").val("");
		$("#iconUrl").textbox("setValue","");
	}
	
</script>
</head>

<body style="margin: 5px;">
<div id="div_tabs" class="easyui-tabs" fit="true" border="false">
	
	<div data-options="title:'商家详情'">
		<table id="dataGrid" class="easyui-datagrid"
			data-options="
			iconCls:'icon-edit', 
			url:'lifeCircleData', 
			pageSize:'20', 
			striped:true,
			fitColumns:true, 
			pagination:true, 
			rownumbers:true,
			singleSelect:false,
			fit:true,
			toolbar:'#tb'">
			<thead>
				<tr>
					<th field="cb" checkbox="true"></th>
					<!-- <th field="lifeId" width="100" align="center">编号</th> -->
					<th field="icon" width="100" align="center" formatter="showIcon">分类图片</th>
					<th field="lifeType" width="100" align="center">商家类型</th>
					<th field="name" width="100" align="center">商家名称 </th>
					<th field="image" width="100" align="center" formatter="showImg">图片</th>
					<th field="address" width="100" align="center">商家地址</th>
					<th field="tel" width="100" align="center">联系电话 </th>
				</tr>
			</thead>
		</table>
		<div id="tb">
			<div style="margin:4px;">
				<a href="javascript:openLifeCircleAdd()" class="easyui-linkbutton"
					iconCls="icon-add" plain="true">添加</a> 
				<a href="javascript:openLifeCircleModify()"
					class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
				<a href="javascript:deleteLifeCircle()" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true">删除</a>
			</div>
			<div style="margin:4px;">
				&nbsp;&nbsp;商家名称： 
				<input type="text" name="l_name" id="l_name" size="12" />
				&nbsp;&nbsp;商家类型： 
				<select class="easyui-combobox" id="l_typeId" name="l_typeId" panelHeight="auto" editable="false" style="width:120px;"
					data-options="
					url:'lifeTypeItem',
		    		editable:false,
		    		valueField:'name',
		    		textField:'name' ">
				</select>
				&nbsp;&nbsp; 
				<a href="javascript:searchLifeCircle()" class="easyui-linkbutton"
					iconCls="icon-search" plain="false">搜索</a>
			</div>
		</div>
	</div>
	
	<div data-options="title:'商家类型'">
		<table id="dataGrid_item" class="easyui-datagrid"
			data-options="
			iconCls:'icon-edit', 
			url:'lifeTypeData', 
			pageSize:'20', 
			striped:true,
			fitColumns:true, 
			pagination:true, 
			rownumbers:true,
			singleSelect:false,
			fit:true,
			toolbar:'#tb_item'">
			<thead>
				<tr>
					<th field="cb" checkbox="true"></th>
					<!-- <th field="itemId" width="100" align="center">编号</th> -->
					<th field="icon" width="100" align="center" formatter="showIcon">分类图片</th>
					<th field="name" width="100" align="center">商家类型</th>
				</tr>
			</thead>
		</table>
		<div id="tb_item">
			<div style="margin:4px;">
				<a href="javascript:openLifeTypeAdd()" class="easyui-linkbutton"
					iconCls="icon-add" plain="true">添加</a> 
				<a href="javascript:openLifeTypeModify()"
					class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
				<a href="javascript:deleteLifeType()" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true">删除</a>
			</div>
		</div>
		
		<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
				style="width:auto; heigth:auto; padding:10px 20px;" closed="true">
			<form id="fm" method="post" enctype="multipart/form-data">
				<div style="margin:8px;">
					<span style="display:inline-block;">商家类型：</span>
					<input type="text" name="name" id="name" class="easyui-validatebox" required="true" style="width:200px;" />
				</div>
				<div style="margin:8px;">
					<input type="hidden" name="icon" />
					<input type="hidden" name="itemId" />
			  		<span style="display:inline-block;">分类图标：</span>
			  		<input type="text" id="iconUrl" name="iconUrl" class="easyui-filebox" style="width:200px;" accept="image/png,image/jpeg,image/gif"
			  			 data-options="prompt:'请选择图片',buttonText:'选择图片'" />
			  	</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveLifeType()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeLifeType()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</div>
</body>
</html>
