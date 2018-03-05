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
<title>二手房推荐管理</title>
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
	
	//根据日期查询
	function searchOldHouse(){
		$('#dataGrid').datagrid('load',{
			houseType : $('#s_houseType').combobox("getValue"),
			area : $('#s_area').val()
		});
	}
	
	
	//删除资讯
	function deleteOldHouse() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示","请选择您要删除的资讯");
		}
		var strId = [];
		for(var i = 0; i < selectedRows.length; i++){
			strId.push(selectedRows[i].houseId);
		}
		var ids = strId.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？",function(r){
			if(r){
			$.ajax({
				type : 'post',
				url : 'deleteOldHouse',
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
		<div title="二手房推荐">
			<table id="dataGrid" title="二手房推荐" class="easyui-datagrid"
				data-options="
				iconCls:'icon-edit', 
				url:'oldHouseListData', 
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
						<th field="houseId" width="100" align="center">编号</th>
						<th field="houseType" width="100" align="center">销售类型</th>
						<th field="where" width="100" align="center">所属职责</th>
						<th field="image" width="100" align="center" formatter="showImg">图片</th>
						<th field="title" width="100" align="center">标题</th>
						<th field="mobile" width="100" align="center">电话</th>
						<th field="area" width="100" align="center">面积</th>
						<th field="price" width="100" align="center">价格</th>
						<th field="introduce" width="100" align="center">介绍</th>
					</tr>
				</thead>
			</table>
			<div id="tb">
				<div style="margin:4px;">
					<a href="javascript:deleteOldHouse()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
				</div>
				<div style="margin:4px">
					&nbsp;&nbsp;
					销售类型：
					<select class="easyui-combobox" id="s_houseType" name="s_houseType" 
						editable="false" panelHeight="auto" style="width:120px;">
						<option value="全部">全部</option>
						<option value="出售">出售</option>
						<option value="出租">出租</option>
					</select> 
					&nbsp;&nbsp;
					面积：
					<input type="text" name="s_area" id="s_area" size="10" />
					&nbsp;&nbsp;
					<a href="javascript:searchOldHouse()" class="easyui-linkbutton" iconCls="icon-search" plain="false">搜索</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
