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
<title>全民营销管理</title>
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
	$(function() {});
	
	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()-1;
		var d = date.getDate();
		return y+'-'+(m<0?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	
	//删除资讯
	function deleteNationalMarketing() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示","请选择您要删除的营销数据");
		}
		var strId = [];
		for(var i = 0; i < selectedRows.length; i++){
			strId.push(selectedRows[i].marketingId);
		}
		var ids = strId.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>" + selectedRows.length + "</font>条数据吗？",function(r){
			if(r){
			$.ajax({
				type : 'post',
				url : 'deleteNationalMarketing',
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
	function openNationalMarketingAddDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加全民营销");
		url = "saveNationalMarketingData";
	}
	
	function closeNationalMarketingDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}

	//保存缴费单
	function saveNationalMarketing() {
		$("#fm").form("submit", {
			url : url,
			/*默认支持，少天不能提交*/
			onSubmit : function() {
				if ($('#houseType').val() == "") {
					$.messager.alert("系统提示", "请填写楼盘");
					return fasle;
				}
				if ($('#houseNum').val() == "") {
					$.messager.alert("系统提示", "请填写房号");
					return fasle;
				}
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
		$("#houseType").val("");
		$("#houseNum").val("");
		$("#name").val("");
		$("#mobile").val("");
		$("#toName").val("");
		$("#toMobile").val("");
	}

	function openNationalMarketingModifyDialog() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "编辑全民营销");
		$("#fm").form("load", row);
		url = "saveNationalMarketingData?marketingId=" + row.marketingId;
	}
	
	
</script>
</head>

<body style="margin: 5px;">

	<table id="dataGrid" title="全民营销" class="easyui-datagrid"
				data-options="
				iconCls:'icon-edit', 
				url:'nationalMarketingListData',
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
				<!-- <th field="NationalMarketingId" width="100" align="center">编号</th> -->
				<th field="marketingId" width="100" align="center">编号</th>
				<th field="houseType" width="100" align="center">楼盘项目</th>
				<th field="houseNum" width="100" align="center">房号</th>
				<th field="name" width="100" align="center">介绍人</th>
				<th field="mobile" width="100" align="center">介绍人电话</th>
				<th field="toName" width="100" align="center">被介绍人</th>
				<th field="toMobile" width="100" align="center">被介绍人电话</th>
			</tr>
		</thead>
	</table>

	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openNationalMarketingAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a> 
			<a href="javascript:openNationalMarketingModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			<a href="javascript:deleteNationalMarketing()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>			
		</div>
		
		<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
			style="width:auto; heigth:350px; padding:10px 20px;" closed="true">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>楼盘：</td>
						<td>
							<input type="text" id="houseType" name="houseType" class="easyui-validatebox" required="true" style="width:120px;"/>
						</td>
						<td style="width:40px;"></td>
						<td>房号：</td>
						<td>
							<input type="text" id="houseNum" name="houseNum" class="easyui-validatebox" required="true" style="width:120px;"/>
						</td>
					</tr>
					<tr>
						<td>介绍人：</td>
						<td>
							<input type="text" name="name" id="name" class="easyui-validatebox" required="true" style="width:120px;"/>
						</td>
						<td></td>
						<td>介绍人手机号：</td>
						<td>
							<input type="text" name="mobile" id="mobile" class="easyui-validatebox" required="true" style="width:120px;"
							 	onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
						</td>
					</tr>
					<tr>
						<td>被介绍人：</td>
						<td>
							<input type="text" id="toName" name="toName" class="easyui-validatebox" style="width:120px;" required="true"/>
						</td>
						<td></td>
						<td>被介绍人手机号：</td>
						<td>
							<input name="toMobile" id="toMobile" class="easyui-validatebox" required="true" style="width:120px;" 
								onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveNationalMarketing()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeNationalMarketingDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
