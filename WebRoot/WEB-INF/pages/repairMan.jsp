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
<title>维修人员管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var url;
	$(function() {
	
	});
	
	//搜索条件查询
	function searchRepairMan() {
		$('#dataGrid').datagrid('load', {
			community : $('#r_community').combobox("getValue"),
		});
	}
	
	//删除维修人员
	function deleteRepairMan() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].repairmanId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteRepairManData',
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
	
	function openRepairManAddDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加维修人员");
		url = "saveRepairManData";
	}
	
	function closeRepairManDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}

	//保存缴费单
	function saveRepairMan() {
		$("#fm").form("submit", {
			url : url,
			/*默认支持，少天不能提交*/
			onSubmit : function() {
				if ($('#community').combobox("getValue") == "") {
					$.messager.alert("系统提示", "请选择小区");
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
		$("#community").combobox("setValue","");
		$("#name").val("");
		$("#mobile").val("");
	}

	function openRepairManModifyDialog() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "编辑维修人员");
		$("#fm").form("load", row);
		url = "saveRepairManData?repairmanId=" + row.repairmanId;
	}
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="维修人员" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'repairManListData', 
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
				<!-- <th field="repairmanId" width="100" align="center">编号</th> -->
				<th field="community" width="100" align="center">小区</th>
				<th field="name" width="100" align="center">姓名</th>
				<th field="mobile" width="100" align="center">联系方式</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openRepairManAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> 
			<a href="javascript:openRepairManModifyDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteRepairMan()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div style="margin:4px;">
		&nbsp;&nbsp;选择小区： 
		<select class="easyui-combobox" id="r_community" name="r_community" panelHeight="auto" editable="false" style="width:120px;"
			data-options="
			url:'communityItem',
    		editable:false,
    		valueField:'communityName',
    		textField:'communityName' ">
		</select>
		&nbsp;&nbsp;
		<a href="javascript:searchRepairMan()" class="easyui-linkbutton"
			iconCls="icon-search" plain="false">搜索</a>
	</div>
		
		<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
			style="width:auto; heigth:auto; padding:10px 20px;" closed="true">
			<form id="fm" method="post">
				<div style="margin:8px;">
			  		<span style="width:100px; display:inline-block;">小区：</span>
			  		<select class="easyui-combobox" id="community" name="community" panelHeight="auto" style="width:200px;"
						data-options="
						url:'communityItemTwo',
			    		editable:false,
			    		valueField:'communityName',
			    		textField:'communityName' ">
					</select>
		 		</div>
				<div style="margin:8px;">
			  		<span style="width:100px; display:inline-block;">姓名：</span>
			  		<input type="text" id="name" name="name" class="easyui-validatebox" style="width:200px;" data-options="prompt:'请输入姓名.',required:true,validType:'maxLength[50]'" />
			 	</div>
			 	<div style="margin:8px;">
			  		<span style="width:100px; display:inline-block;">联系方式：</span>
			  		<input type="text" id="mobile" name="mobile" class="easyui-validatebox" style="width:200px;" data-options="prompt:'请输入联系方式.',required:true,validType:'maxLength[50]'" />
			 	</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveRepairMan()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeRepairManDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
