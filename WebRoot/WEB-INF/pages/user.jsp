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
<title>户主信息管理</title>
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
		$.extend($.fn.filebox.methods, {  
		    getFile: function (myself) {  
		        var temp = $(myself).next().children("[type='file']");  
		        var file = document.getElementById(temp.attr("id"));  
		  
		        if (file.files.length > 0) {  
		            // 若选中一个文件，则返回该文件的File对象  
		            return file.files[0];  
		        }  
		        // 若未选中任何文件，则返回null  
		        return null;  
		    }  
		});
		
		//显示选中的图片
		$("#imageUrl").filebox({
		    onChange: function (event) { 
		        // 获取所选文件的File对象  
		        var file = $(this).filebox("getFile");  
		        var reader = new window.FileReader();
		        if (file != null) {
		            // 创建FileReader对象  
		            // 定义reader的onload事件  
		            // 当读完文件信息后触发onload事件  
		            reader.onload = function (e) {  
		                // reader.result保存着产生的虚拟URL
		                divPreview.style.display = 'block'; //显示 
		                //$("#imageShow").attr("heigth", "100px");
		                $("#imageShow").attr("src", this.result);
		                $("#imagePage").attr("href", this.result);
		            }  
		            // 读取指定文件并形成URL
		            reader.readAsDataURL(file);
		        }
		    }
		});
	});
	
	//搜索条件查询
	function searchUser() {
		$('#dataGrid').datagrid('load', {
			community : $('#u_community').combobox("getValue"),
			mobile : $('#u_mobile').val(),
			name : $('#u_name').val(),
		});
	}
	
	//图片显示
	function showImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<a target="_blank" href="'+value+'"><img src='+value+' style="width:80px; height:50px;" />'; 
	}
	
	//图片显示
	function showAvatarImg(value,row){
		if(value == "" || value == null) {
			value = '<%=path%>/resources/images/white.png';
		}
		return '<a target="_blank" href="'+value+'"><img src='+value+' style="width:50px; height:50px; border-radius:100%;" />'; 
	}
	
	function openModifyBgDialog() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择至少一条需要编辑的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].mobile);
		}
		var ids = strIds.join(",");
		$("#dlg").dialog("open").dialog("setTitle", "编辑户主背景图片");
		url = "updateBackground?mobile=" + ids;
	}
	
	function closeModifyBgDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}
	
	function saveBackground() {
		$("#fm").form("submit", {
			url : url,
			/*默认支持，少天不能提交*/
			onSubmit : function() {
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
		$("#imageUrl").textbox("setValue","");
		$("#imageUrl").textbox("setValue","");
		$("#imageShow").attr("src", "");
		divPreview.style.display = 'none'; //隐藏
	}
	
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="户主信息" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'userListData', 
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
				<th field="background" width="100" align="center" formatter="showImg">背景图</th>
				<th field="community" width="100" align="center">小区</th>
				<th field="name" width="100" align="center">姓名</th>
				<th field="mobile" width="100" align="center">联系方式</th>
				<th field="avatar" width="100" align="center" formatter="showAvatarImg">头像</th>
				<th field="idNumber" width="100" align="center">身份证</th>
				<th field="city" width="100" align="center">城市</th>
				<th field="floor" width="100" align="center">楼栋</th>
				<th field="unit" width="100" align="center">单元</th>
				<th field="houseNumber" width="100" align="center">门牌号</th>
				<th field="integral" width="100" align="center">积分</th>
				<th field="volunteer" width="100" align="center">是否志愿者</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openModifyBgDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改户主背景图片</a>
		</div>
		<div style="margin:4px;">
			&nbsp;&nbsp;选择小区： 
			<select class="easyui-combobox" id="u_community" name="u_community" panelHeight="auto" editable="false" style="width:120px;"
				data-options="
				url:'communityItem',
	    		editable:false,
	    		valueField:'communityName',
	    		textField:'communityName' ">
			</select>
			&nbsp;&nbsp;联系方式：
			<input type="text" name="u_mobile" id="u_mobile" size="12" />
			&nbsp;&nbsp;姓名：
			<input type="text" name="u_name" id="u_name" size="12" />
			&nbsp;&nbsp;
			<a href="javascript:searchUser()" class="easyui-linkbutton"
				iconCls="icon-search" plain="false">搜索</a>
		</div>
	</div>
	
	<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
			style="width:420px; heigth:auto; padding:10px 20px;" closed="true">
			<form id="fm" method="post" enctype="multipart/form-data">
				<div style="margin:8px;">
			  		<span display:inline-block;">选择图片：</span>
			  		<input id="imageUrl" name="imageUrl" class="easyui-filebox" style="width:200px;" accept="image/png,image/jpeg,image/gif"
			  			 data-options="prompt:'请选择图片',buttonText:'选择图片'" />
			 	</div>
			 	<div id="divPreview" style="margin:8px;">
			 		<a target="_blank" id="imagePage" style="margin:8px 0px;">
			 			<img id="imageShow" height="200px"/>
			 		</a>
			 	</div>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveBackground()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeModifyBgDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
</body>
</html>
