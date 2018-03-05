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
<title>积分兑换商城管理</title>
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
	/* .datagrid-row {
		height: 50px;
	} */
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
	//图片显示
	function showImgages(value,row){
		var html = new Array();
		var imageUrl;
		if(value != "" && value != null){
			var imagesArray = value.split(",");
			for (var i = 0; i < imagesArray.length; i++) {
				imageUrl = imagesArray[i];
				html.push('<a target="_blank" href="'+imageUrl+'"><img src='+imageUrl+' height="50px" /></a> ');
			}
		}
		return html.join('');
	}
	
	//搜索条件查询
	function searchIntegralMall() {
		$('#dataGrid').datagrid('load', {
			mall : $('#l_mall').val(),
			community : $('#l_community').combobox("getValue"),
		});
	}

	//删除
	function deleteIntegralMall() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].mallId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteIntegralMall',
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
	
	function openIntegralMallAdd() {
		closeTab("修改项目信息");
		addTab("新增项目信息","addIntegralMall");
	}
	
	function openIntegralMallModify() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
	    closeTab("新增项目信息");
		addTab("修改项目信息","addIntegralMall?mallId="+row.mallId);
	}
	
</script>
</head>

<body style="margin: 5px;">
<div id="div_tabs" class="easyui-tabs" fit="true" border="false">
	
	<div data-options="title:'积分兑换商城'">
		<table id="dataGrid" class="easyui-datagrid"
			data-options="
			iconCls:'icon-edit', 
			url:'integralMallData', 
			pageSize:'20', 
			striped:true,
			fitColumns:true, 
			pagination:true, 
			rownumbers:false,	//行号
			nowrap:false,	//false为换行
			singleSelect:false,
			fit:true,
			toolbar:'#tb'">
			<thead>
				<tr>
					<th field="cb" checkbox="true"></th>
					<!-- <th field="mallId" width="100" align="center">编号</th> -->
					<th field="community" width="100" align="center">绑定小区</th>
					<th field="title" width="100" align="center">标题 </th>
					<th field="mall" width="100" align="center">商城</th>
					<th field="integral" width="100" align="center">所需积分</th>
					<th field="marketPrice" width="100" align="center">市场价（元） </th>
					<th field="introduce" width="200" align="center">详情介绍</th>
					<th field="images" width="200" align="left" formatter="showImgages">图片</th>
				</tr>
			</thead>
		</table>
		<div id="tb">
			<div style="margin:4px;">
				<a href="javascript:openIntegralMallAdd()" class="easyui-linkbutton"
					iconCls="icon-add" plain="true">添加</a> 
				<a href="javascript:openIntegralMallModify()"
					class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
				<a href="javascript:deleteIntegralMall()" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true">删除</a>
			</div>
			<div style="margin:4px;">
				&nbsp;&nbsp;商城： 
				<input type="text" name="l_mall" id="l_mall" size="12" />
				&nbsp;&nbsp;小区： 
				<select class="easyui-combobox" id="l_community" name="l_community" panelHeight="auto" editable="false" style="width:120px;"
					data-options="
					url:'communityItem',
		    		editable:false,
		    		valueField:'communityName',
		    		textField:'communityName' ">
				</select>
				&nbsp;&nbsp; 
				<a href="javascript:searchIntegralMall()" class="easyui-linkbutton"
					iconCls="icon-search" plain="false">搜索</a>
			</div>
		</div>
	</div>
	
</div>
</body>
</html>
