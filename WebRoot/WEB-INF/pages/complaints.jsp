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
<title>投诉建议信息管理</title>
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

<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/css/public.css">

<style type="text/css">
.datagrid-row {
	height: 50px;
}
</style>

<script type="text/javascript">
	var url;
	
	$(function() {
		
	});
	
	//搜索条件查询
	function searchComplaints() {
		$('#dataGrid').datagrid('load', {
			community : $('#c_community').combobox("getValue"),
			type : $('#c_type').combobox("getValue"),
			state : $('#c_state').combobox("getValue"),
		});
	}
	
	//图片显示
	function showImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<a target="_blank" href="'+value+'"><img src='+value+' style="width:80px; height:50px;"/>'; 
		
	}
	
	//操作显示
	function showOperate(value,row){
		if(row.state == "待处理"){
			return '<a href="javascript:void(0);" style="color:blue;" onclick="setOperate(\''+row.listId+'\')">'+"派遣处理"+'</a>';
		} else {
			return '<a href="javascript:void(0);" style="color:blue;" onclick="showResult('+row.listId+')">'+"查看结果"+'</a>';
			/* <pif:i permissionUrl="/">
				<pif:yes>
					return '<a href="javascript:void(0);" style="color:blue" onclick="setOperate('+row.state+',\''+row.listId+'\')">'+"操作"+'</a>';
				</pif:yes>
				<pif:no>
					return '<a href="javascript:void(0);" style="color:blue;" onclick="$.messager.alert(\'消息\',\'对不起,您没有操作权限!\');">操作</a>';
				</pif:no>
			</pif:i> */
		}
	}
	
	//处理状态颜色设置
	function showStyle(value,row){
		if(value == "待处理"){
			return '<span style="color:red">'+value+'</span>';
		} else {
			return '<span style="color:green">'+value+'</span>';
		}
	}
	
	//查看维修结果
	function showResult(listId){
		$('#dataGrid').datagrid('selectRecord',listId);
		var row = $('#dataGrid').datagrid('getSelected');
		$('#res_evaluate').text(row.evaluate ? row.evaluate : '无');
		$('#res_attitudeScore').text(row.attitudeScore ? row.attitudeScore+' 星' : '无');
		$('#res_timeScore').text(row.timeScore ? row.timeScore+' 星' : '无');
		$('#dlg_result').dialog('open');
	}
	
	//操作事件
	function setOperate(listId){
		$.messager.confirm("系统提示", "确定要派遣处理吗？", function(res) {
			if(res) {
				$.ajax({
					type:'post',
					url:'dealComplaints',
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
	
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="投诉建议信息" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'complaintsListData', 
		idField:'listId',
		pageSize:'20', 
		singleSelect:true,
		pagination:true, 
		fitColumns:true, 
		nowrap:false, 
		fit:true,
		toolbar:'#tb',
		rownumbers:true">
		<thead>
			<tr>
				<!-- <th field="listId" width="100" align="center">投诉编号</th> -->
				<th field="community" width="100" align="center">社区</th>
				<th field="name" width="100" align="center">姓名</th>
				<th field="mobile" width="100" align="center">手机号</th>
				<th field="address" width="150" align="center">住址</th>
				<th field="date" width="150" align="center">报修日期</th>
				<th field="type" width="150" align="center">报修类型</th>
				<th field="introduce" width="200" align="center">报修描述</th>
				<th field="image" width="150" align="center" formatter="showImg">报修图片</th>
				<th field="state" width="100" align="center" formatter="showStyle">处理状态</th>
				<th field="operate" width="100" align="center" formatter="showOperate">操作</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			&nbsp;&nbsp;选择小区： 
			<select class="easyui-combobox" id="c_community" name="c_community" panelHeight="auto" editable="false" style="width:120px;"
				data-options="
				url:'communityItem',
	    		editable:false,
	    		valueField:'communityName',
	    		textField:'communityName' ">
			</select>  
			&nbsp;&nbsp;投诉类型： 
			<select class="easyui-combobox" id="c_type" name="c_type" panelHeight="auto" size="10" editable="false" style="width:120px;">
				<option value="">全部</option>
				<option value="环境管理类">环境管理类</option>
				<option value="秩序维护类">秩序维护类</option>
				<option value="客户服务类">客户服务类</option>
				<option value="设备管理与维修">设备管理与维修</option>
			</select> 
			&nbsp;&nbsp;维修状态： 
			<select class="easyui-combobox" id="c_state" name="c_state" 
				editable="false" panelHeight="auto" style="width:120px;">
				<option value="">全部</option>
				<option value="待处理">待处理</option>
				<option value="处理中">处理中</option>
				<option value="待评价">待评价</option>
				<option value="已评价">已评价</option>
			</select> 
			&nbsp;&nbsp;
			<a href="javascript:searchComplaints()"
				class="easyui-linkbutton" iconCls="icon-search" plain="false">搜索</a>
		</div>
		
		<div id="dlg_result" class="easyui-dialog"
			style="width:350px;height:170px;padding:10px;"
			data-options="
            iconCls: 'icon-save',
            buttons: '#dlgres-buttons',
            closed:true,
            modal:true,
            title:'投诉建议结果' ">
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">用户反馈:</span> 
				<span id="res_evaluate"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">服务态度:</span> 
				<span id="res_attitudeScore"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">回馈及时性:</span> 
				<span id="res_timeScore"></span>
			</div>
		</div>
		<div id="dlgres-buttons">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				onclick="$('#dlg_result').dialog('close');">关闭</a>
		</div>
</body>
</html>
