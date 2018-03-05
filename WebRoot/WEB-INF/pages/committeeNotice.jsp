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
<title>居委会通知管理</title>
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
	
	//图片显示
	function showImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<a target="_blank" href="'+value+'"><img src='+value+' style="width:80px; height:50px;"/>'; 
	}
	
	//搜索条件查询
	function searchCommittee() {
		$('#dataGrid').datagrid('load', {
			community : $('#n_community').combobox("getValue"),
			committee : $('#n_committee').combobox("getValue"),
			noticeType : '居委会',
		});
	}

	//删除居委会通知
	function deleteCommittee() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].noticeId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteCommittee',
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
			//$('#dataGrid').tabs('select',title);
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
	
	function openCommitteeAddDialog() {
		closeTab("修改居委会通知");
		addTab("新增居委会通知","addNoticeInfo");
	}
	
	function openCommitteeModifyDialog() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
	    closeTab("新增居委会通知");
		addTab("修改居委会通知","addNoticeInfo?noticeId="+row.noticeId);
	}
	
</script>
</head>

<body style="margin: 5px;">
<div id="div_tabs" class="easyui-tabs" fit="true" border="false">
<div title="居委会通知"> 
	<table id="dataGrid" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'noticeListData', 
		queryParams:{
			noticeType:'居委会',
		},
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
				<!-- <th field="noticeId" width="100" align="center">编号</th> -->
				<th field="community" width="100" align="center">推送小区</th>
				<th field="noticeType" width="100" align="center">啊啊啊</th>
				<th field="committee" width="100" align="center">发布居委会</th>
				<th field="date" width="100" align="center">发布时间</th>
				<th field="title" width="100" align="center">通知标题</th>
				<th field="detail" width="200" align="center">消息详情</th>
				<th field="image" width="100" align="center" formatter="showImg">图片</th>
				<th field="publisher" width="100" align="center">发布者</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openCommitteeAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> 
			<a href="javascript:openCommitteeModifyDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteCommittee()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div style="margin:4px;">
			&nbsp;&nbsp;选择小区： 
			<select class="easyui-combobox" id="n_community" name="n_community" panelHeight="auto" editable="false" style="width:120px;"
				data-options="
				url:'communityItem',
	    		editable:false,
	    		valueField:'communityName',
	    		textField:'communityName' ">
			</select> 
			&nbsp;&nbsp;居委会： 
			<select class="easyui-combobox" id="n_committee" name="n_committee" panelHeight="auto" editable="false" style="width:120px;"
				data-options="
				url:'committeeItem',
	    		editable:false,
	    		valueField:'committeeName',
	    		textField:'committeeName' ">
			</select> 
			&nbsp;&nbsp;
			<a href="javascript:searchCommittee()" class="easyui-linkbutton"
				iconCls="icon-search" plain="false">搜索</a>
		</div>
	</div>
</div>
</div>
</body>
</html>
