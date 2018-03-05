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
		var curr_time = new Date();
		$("#s_date").datebox("setValue",myformatter(curr_time));
		$("#s_date").datebox("getValue");
		
		$("#s_date").datebox({
			//配置parser，返回选择的日期
            parser: function (s) {
                if (!s) return new Date();
                var arr = s.split('-');
                return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, parseInt(arr[2],10));
            },
            //配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth()); 
            formatter: function (d) { 
                var currentMonth = (d.getMonth()+1);
                var currentMonthStr = currentMonth < 10 ? ('0' + currentMonth) : (currentMonth + '');
                var currentDay = (d.getDate());
                var currentDayStr = currentDay < 10 ? ('0' + currentDay) : (currentDay + '');
                return d.getFullYear() + '-' + currentMonthStr + '-' + currentDayStr; 
           	}
		});
	
	});
	
	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()-1;
		var d = date.getDate();
		return y+'-'+(m<0?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	
	function myparser(s){
		if(!s) return new Date();
		
		var arr = (s.split('-'));
		var y = parseInt(arr[0],10);
		var m = parseInt(arr[1],10);
		var d = parseInt(arr[2],10);
		
		if(!isNaN(y)&&!isNaN(m)&&!isNaN(d)){
			return new Date(y,m-1,d);
		}else{
			return new Date();
		}
	}
	
	//图片显示
	function showImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<a target="_blank" href="'+value+'"><img src='+value+' style="width:80px; height:50px;"/>'; 
	}
	
	//根据日期查询
	function searchNewStatus(){
		$('#dataGrid').datagrid('load',{
			date : $('#s_date').datebox("getValue"),
			statusType : '最新资讯',
			title : $('#s_title').val()
		});
	}
	
	
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
	function openNewStatusAddDialog(){
		closeTab("修改最新资讯");
		addTab("添加最新资讯","addNewStatusInfo");
	}
	//打开修改咨询的标签页
	function openNewStatusModifyDialog(){
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1){
			$.messager.alert("系统提示","请选择一条您要修改的资讯");
			return;
		}
		var row = selectedRows[0];
		closeTab("添加最新资讯");
		addTab("修改最新资讯","addNewStatusInfo?statusId="+row.statusId);
	}
	
	
	
</script>
</head>

<body style="margin: 5px;">
	<div id="div_tabs" class="easyui-tabs" fit="true" border="false">
		<div title="最新资讯">
			<table id="dataGrid" title="最新资讯" class="easyui-datagrid"
				data-options="
				iconCls:'icon-edit', 
				url:'newStatusListData',
				queryParams:{
					statusType:'最新资讯',
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
						<!-- <th field="billId" width="100" align="center">编号</th> -->
						<th field="statusId" width="100" align="center">编号</th>
						<th field="statusType" width="100" align="center">类型</th>
						<th field="image" width="100" align="center" formatter="showImg">图片</th>
						<th field="title" width="100" align="center">标题</th>
						<th field="date" width="100" align="center">日期</th>
						<th field="detail" width="100" align="center">详情</th>
					</tr>
				</thead>
			</table>
			<div id="tb">
				<div style="margin:4px;">
					<a href="javascript:openNewStatusAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a> 
					<a href="javascript:openNewStatusModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
					<a href="javascript:deleteNewStatus()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
				</div>
				<div style="margin:4px">
					&nbsp;&nbsp;日期：
					<input id="s_date" name="s_date" class="easyui-datebox"/>
					&nbsp;&nbsp;
					<input id="s_title" name="s_title" type="text" size="22" />
					&nbsp;&nbsp;
					<a href="javascript:searchNewStatus()" class="easyui-linkbutton" iconCls="icon-search" plain="false">搜索</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
