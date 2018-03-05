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
<title>最新资讯管理</title>
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
<style type="text/css">
	.datagrid-row {
		height: 50px;
	}
</style>
<script type="text/javascript">
	var url;
	$(function() {
		
	});
		
	//删除资讯
	function deleteNewStatus() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示","请选择您要删除的资讯");
		}
		var strId = [];
		for(var i = 0; i < selectedRows.length; i++){
			strId.push(selectedRows[i].statusId);
		}
		var ids = strId.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？",function(r){
			if(r){
			$.ajax({
				type : 'post',
				url : 'deleteNewStatus',
				dataType : 'json',
				data : {
					delIds : ids,
				},
				cache : false,
				success : function (result){
					if(result.success){
						$.messager.alert("系统提示","您已成功删除<font color=red>" + selectedRows.length + "</font>条数据");
						$("#dataGrid").datagrid("reload");
					}else{
						$.messager.alert("系统提示",result.errorMsg);
					}
				}
			});
		}
		});
		
	}
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
	//打开添加资讯的标签页
	function openAdminAddDialog(){
		closeTab("修改最新资讯");
		addTab("添加管理人员","addManager");
	}
	
	function showState(value,row){
		if(value == 0){
			return '<span style="color:red">未授权</span>';
		}else{
			return '<span style="color:green">已授权</span>';
		}
	}
	
	function showOperate(value,row){
		if(row.state == 0){
			return '<a href="javascript:void(0);" style="color:blue;" onclick="setOperate(\''+row.id+'\')">'+"授权"+'</a>';
		} else {
			return '<span style="color:green;">'+"已授权"+'</span>';
		}
	}
	
	function setOperate(listId){
		$.messager.confirm("系统提示", "确定要授权吗？", function(res) {
			if(res) {
				$.ajax({
					type:'post',
					url:'authorization',
					dataType: "json",
			        data: {
			        	listId : listId
			        },
			        cache:false,
			        success: function (result) {
						if (result.success) {
							$.messager.alert("系统提示", result.success);
							$("#dataGrid").datagrid("reload");
						} else {
							$.messager.alert("系统提示", result.errorMsg);
						}
			        }
       			});
			}
		})
	}
	function deleteManager() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示","请选择您要删除的管理人员");
		}
		var strId = [];
		for(var i = 0; i < selectedRows.length; i++){
			strId.push(selectedRows[i].id);
		}
		var ids = strId.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？",function(r){
			if(r){
			$.ajax({
				type : 'post',
				url : 'deleteManager',
				dataType : 'json',
				data : {
					delIds : ids,
				},
				cache : false,
				success : function (result){
					if(result.success){
						$.messager.alert("系统提示","您已成功删除<font color=red>" + selectedRows.length + "</font>条数据");
						$("#dataGrid").datagrid("reload");
					}else{
						$.messager.alert("系统提示",result.errorMsg);
					}
				}
			});
		}
		});
		
	}
</script>
</head>

<body style="margin: 5px;">
	<div id="div_tabs" class="easyui-tabs" fit="true" border="false">
		<div title="角色管理">
			<table id="dataGrid" title="角色管理" class="easyui-datagrid"
				data-options="
				iconCls:'icon-edit', 
				url:'managerList',
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
						<!-- <th field="billId" width="100" align="center">编号</th> -->
						<th field="username" width="100" align="center">用户名</th>
						<th field="mobile" width="100" align="center">电话</th>
						<th field="community" width="100" align="center">所属小区</th>
						<th field="committee" width="100" align="center">所属居委会</th>
						<th field="state" width="100" align="center" formatter="showState">使用状态</th>
						<th field="operate" width="100" align="center" formatter="showOperate">操作</th>
					</tr>
				</thead>
			</table>
			<div id="tb">
				<div style="margin:4px;">
					<a href="javascript:openAdminAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
					<a href="javascript:deleteManager()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
