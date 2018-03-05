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
<title>轮播图管理</title>
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
	
	//图片显示
	function showImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<a target="_blank" href="'+value+'"><img src='+value+' style="width:80px; height:50px;"/>'; 
	}	
	
	//删除资讯
	function deleteTopBanner() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示","请选择您要删除的资讯");
		}
		var strId = [];
		for(var i = 0; i < selectedRows.length; i++){
			strId.push(selectedRows[i].bannerId);
		}
		var ids = strId.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？",function(r){
			if(r){
			$.ajax({
				type : 'post',
				url : 'deleteTopBanner',
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
	function openTopBannerAddDialog(){
		closeTab("修改最新资讯");
		addTab("添加轮播图","addTopBannerInfo");
	}
	//打开修改咨询的标签页
	function openTopBannerModifyDialog(){
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1){
			$.messager.alert("系统提示","请选择一条您要修改的轮播图");
			return;
		}
		var row = selectedRows[0];
		closeTab("添加轮播");
		addTab("修改轮播","addTopBannerInfo?bannerId="+row.bannerId);
	}
	
	function searchTopBanner(){
		$('#dataGrid').datagrid('load', {
			community : $('#s_community').combobox("getValue"),
			bannerType : $('#s_bannerType').combobox("getValue"),
			title : $('#s_title').val()
		});
	}
	
</script>
</head>

<body style="margin: 5px;">
	<div id="div_tabs" class="easyui-tabs" fit="true" border="false">
		<div title="轮播图管理">
			<table id="dataGrid" class="easyui-datagrid"
				data-options="
				iconCls:'icon-edit', 
				url:'topBannerListData',
				queryParams:{
					bannerType:'主页',
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
						<th field="community" width="100" align="center">小区</th>
						<th field="bannerType" width="100" align="center">类型</th>
						<th field="image" width="100" align="center" formatter="showImg">图片</th>
						<th field="title" width="100" align="center">标题</th>
						<th field="introduce" width="100" align="center">详情</th>
					</tr>
				</thead>
			</table>
			<div id="tb">
				<div style="margin:4px;">
					<a href="javascript:openTopBannerAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a> 
					<a href="javascript:deleteTopBanner()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
				</div>
				<div style="margin:4px">
					&nbsp;&nbsp;选择小区：
					<select class="easyui-combobox" id="s_community" name="s_community" panelHeight="auto" editable="false" style="width:100px;"
						data-options="
						url:'communityItem',
			    		editable:false,
			    		valueField:'communityName',
			    		textField:'communityName' ">
					</select> 
					&nbsp;&nbsp;选择类型：
					<select class="easyui-combobox" id="s_bannerType" name="s_bannerType" panelHeight="auto" editable="false" style="width:100px;"
						data-options="
						url:'bannerTypeItem',
			    		editable:false,
			    		valueField:'bannerType',
			    		textField:'bannerType' ">
					</select> 
					&nbsp;&nbsp;标题：
					<input id="s_title" name="s_title" type="text" size="22" />
					<a href="javascript:searchTopBanner()" class="easyui-linkbutton" iconCls="icon-search" plain="false">搜索</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
