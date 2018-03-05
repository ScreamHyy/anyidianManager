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
<title>商家商品管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/css/public.css">
	
<style type="text/css">
	/* .datagrid-row {
		height: 50px;
	} */
</style>
	
<script type="text/javascript">
	var url;
	$(function() {

	});
	
	//处理状态颜色设置
	function showStyle(value,row){
		if(value == "下架"){
			return '<span style="color:red">'+value+'</span>';
		} else {
			return '<span style="color:green">'+value+'</span>';
		}
	}
	
	//操作显示
	function showOperate(value,row){
		var value;
		if(row.state == "下架"){
			value = "启用";
		} else {
			value = "下架";
		}
		return '<a href="javascript:void(0);" style="color:blue;" onclick="setOperate(\''+row.state+'\',\''+row.listId+'\',\''+row.community+'\')">'+value+'</a>';
	}
	
	//操作事件
	function setOperate(state,communityId){
		if(state == "下架"){
			//派单人员加载
		} else {
		
		} 
	}
	
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
	function searchCommodity() {
		$('#dataGrid').datagrid('load', {
			shop : $('#c_shop').combobox("getValue"),
			state : $('#c_state').combobox("getValue"),
			name : $('#c_name').val(),
		});
	}

	//删除
	function deleteCommodity() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].commodityId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteCommodity',
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
	
	function openCommodityAdd() {
		closeTab("修改商品");
		addTab("新增商品	","addCommodity");
	}
	
	function openCommodityModify() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
	    closeTab("新增商品");
		addTab("修改商品","addCommodity?commodityId="+row.commodityId);
	}
	
</script>
</head>

<body style="margin: 5px;">
<div id="div_tabs" class="easyui-tabs" fit="true" border="false">
	
	<div data-options="title:'商家商品'">
		<table id="dataGrid" class="easyui-datagrid"
			data-options="
			iconCls:'icon-edit', 
			url:'commodityData', 
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
					<!-- <th field="commodityId" width="100" align="center">编号</th> -->
					<th field="images" width="200" align="left" formatter="showImgages">图片</th>
					<th field="name" width="100" align="center">商品名称</th>
					<th field="supplier" width="100" align="center">供应商  </th>
					<th field="shop" width="100" align="center">销售商 </th>
					<th field="state" width="100" align="center" formatter="showStyle">商品状态 </th>
					<th field="oldPrice" width="100" align="center">市场价（元） </th>
					<th field="nowPrice" width="100" align="center">现售价（元）</th>
					<th field="supplyPrice" width="100" align="center">供货价（元）</th>
					<th field="fee" width="100" align="center">手续费（元）</th>
					<th field="stockNum" width="100" align="center">库存</th>
					<th field="commentNum" width="100" align="center">评论数 </th>
					<th field="introduce" width="200" align="center">详情介绍</th>
					<th field="operate" width="100" align="center" formatter="showOperate">操作</th>
				</tr>
			</thead>
		</table>
		<div id="tb">
			<div style="margin:4px;">
				<a href="javascript:openCommodityAdd()" class="easyui-linkbutton"
					iconCls="icon-add" plain="true">添加</a> 
				<a href="javascript:openCommodityModify()"
					class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
				<a href="javascript:deleteCommodity()" class="easyui-linkbutton"
					iconCls="icon-remove" plain="true">删除</a>
			</div>
			<div style="margin:4px;">
				&nbsp;&nbsp;所属商家： 
				<select class="easyui-combobox" id="c_shop" name="c_shop" panelHeight="auto" editable="false" style="width:120px;"
					data-options="
					url:'commodityItem',
		    		editable:false,
		    		valueField:'shopName',
		    		textField:'shopName' ">
				</select>
				<%-- <select class="easyui-combobox" id="c_shop" name="c_shop" panelHeight="auto" editable="false" style="width:200px;">
			        <c:forEach items="${commodityItem}" var="item">
			        	<option value="${item}">${item}</option>
			        </c:forEach>
			    </select> --%>
				&nbsp;&nbsp;商品状态： 
				<select class="easyui-combobox" id="c_state" name="c_state" 
					editable="false" panelHeight="auto" style="width:100px;">
					<option value="">全部</option>
					<option value="正常">正常</option>
					<option value="下架">下架</option>
				</select> 
				&nbsp;&nbsp;商品名称： 
				<input type="text" name="c_name" id="c_name" style="width:120px;" />
				&nbsp;&nbsp; 
				<a href="javascript:searchCommodity()" class="easyui-linkbutton"
					iconCls="icon-search" plain="false">搜索</a>
			</div>
		</div>
	</div>
	
</div>
</body>
</html>
