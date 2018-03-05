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
<title>报修信息管理</title>
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
		$("#r_type").combobox({panelHeight:400});
	});
	
	//搜索条件查询
	function searchRepairList() {
		$('#dataGrid').datagrid('load', {
			mobile : $('#r_mobile').val(),
			community : $('#r_community').combobox("getValue"),
			type : $('#r_type').combobox("getValue"),
			state : $('#r_state').combobox("getValue"),
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
		if(row.state == "已评价" || row.state == "待评价"){
			return '<a href="javascript:void(0);" style="color:blue;" onclick="showResult('+row.listId+')">'+"查看结果"+'</a>';
		} else {
			return '<a href="javascript:void(0);" style="color:blue;" onclick="setOperate(\''+row.state+'\',\''+row.listId+'\',\''+row.community+'\')">'+"操作"+'</a>';
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
		$('#res_repairmanName').text(row.repairmanName);
		$('#res_cost').text(row.cost);
		$('#res_evaluate').text(row.evaluate ? row.evaluate : '无');
		$('#res_qualityScore').text(row.qualityScore ? row.qualityScore+' 星' : '无');
		$('#res_attitudeScore').text(row.attitudeScore ? row.attitudeScore+' 星' : '无');
		$('#res_timeScore').text(row.timeScore ? row.timeScore+' 星' : '无');
		$('#res_priceScore').text(row.priceScore ? row.priceScore+' 星' : '无');
		$('#dlg_result').dialog('open');
	}
	
	//操作事件
	function setOperate(state,listId,community){
		if(state == "待处理"){
			//派单人员加载
			$('#repairmanId').combobox({url:'loadRepairman?community='+community});
			$('#frm_info1').form('clear');
			$('#repairId').val(listId);
			$('#dlg1').dialog('open');
		} else {
			$('#frm_info2').form('clear');
			$('#repairListId').val(listId);
			$('#dlg2').dialog('open');
		} 
	}
	
	//派遣维修
	function saveRun(index){
	$('#frm_info'+index).form('submit', {
	    url:'addRepairWorkRun',
	    onSubmit: function(){
	        //进行表单验证
	        //如果返回false阻止提交
	        return $(this).form('validate'); 
	    },
	    success:function(res){
	    	var result = JSON.parse(res);
	    	if(result.errorMsg) {
				$.messager.alert("系统提示", result.errorMsg);
				return;
			} else {
				$.messager.alert("系统提示", result.success);
				$('#dataGrid').datagrid('reload');
				$('#dlg'+index).dialog('close');
			}
	    }
	});
}
	
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="报修信息" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'repairListData', 
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
				<!-- <th field="listId" width="100" align="center">报修单编号</th> -->
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
			&nbsp;&nbsp;手机号：
			<input type="text" name="r_mobile" id="r_mobile" size="12" />
			&nbsp;&nbsp;选择小区： 
			<select class="easyui-combobox" id="r_community" name="r_community" panelHeight="auto" editable="false" style="width:120px;"
				data-options="
				url:'communityItem',
	    		editable:false,
	    		valueField:'communityName',
	    		textField:'communityName' ">
			</select>  
			&nbsp;&nbsp;报修类型： 
			<select class="easyui-combobox" id="r_type" name="r_type" panelHeight="auto" size="10" editable="false" style="width:150px;"
				data-options="
				url:'repairItemTwoData',
	    		editable:true,
	    		valueField:'repairType',
	    		textField:'repairType' ">
			</select> 
			&nbsp;&nbsp;维修状态： 
			<select class="easyui-combobox" id="r_state" name="r_state" 
				editable="false" panelHeight="auto" style="width:120px;">
				<option value="">全部</option>
				<option value="待处理">待处理</option>
				<option value="维修派单">维修派单</option>
				<option value="已解决">已解决</option>
				<option value="待评价">待评价</option>
				<option value="已评价">已评价</option>
			</select> 
			&nbsp;&nbsp;
			<a href="javascript:searchRepairList()"
				class="easyui-linkbutton" iconCls="icon-search" plain="false">搜索</a>
		</div>
		
		<div id="dlg1" class="easyui-dialog" style="width:350px;height:200px;padding:10px;"
	        data-options="
            iconCls: 'icon-save',
            buttons: '#dlg1-buttons',
            closed:true,
            modal:true,
            title:'操作' ">
		    <form id="frm_info1" method="post">
		    	<input type="hidden" id="repairId" name="repairId" />	
		    	<div style="margin: 5px;">
			    	<span style="width:80px; display:inline-block;">指派维修人:</span>
			    	<select class="easyui-combobox" id="repairmanId" name="repairmanId" style="width:180px;" panelHeight="auto" 
			    		data-options="
			    		prompt:'请选择维修人员',
			    		required:true,
			    		editable:false,
			    		valueField:'repairmanId',
			    		textField:'name' ">
			    	</select>
		    	</div>
		    	<div style="margin: 5px;">
			    	<span style="width:80px; display:inline-block;">备注信息:</span>
			    	<input type="text" name="remarks" class="easyui-validatebox easyui-textbox" style="width:180px;height:60px;"  data-options="prompt:'请输入备注信息.',validType:'maxLength[250]',multiline:true" />
			    </div>
		    </form>
		</div>
		<div id="dlg1-buttons">
		    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveRun(1);">保存</a>
		    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#dlg1').dialog('close');">取消</a>
		</div>
		
		<div id="dlg2" class="easyui-dialog" style="width:350px;height:200px;padding:10px;"
	        data-options="
            iconCls: 'icon-save',
            buttons: '#dlg2-buttons',
            closed:true,
            modal:true,
            title:'操作' ">
		    <form id="frm_info2" method="post">
		    	<input type="hidden" id="repairListId" name="repairListId"/>
		    	<div style="margin: 5px;">
			    	<span style="width:80px; display:inline-block;">费用:</span>
			    	<input name="cost" class="easyui-numberbox" data-options="min:0,max:99999,precision:2,required:true"/>
		    	</div>
		    	<div style="margin: 5px;">
			    	<span style="width:80px; display:inline-block;">维修结果:</span>
			    	<input type="text" name="result" class="easyui-validatebox easyui-textbox" style="width:180px;height:60px;"  data-options="prompt:'请输入维修结果.',required:true,validType:'maxLength[250]',multiline:true" />
			    </div>
		    </form>
		</div>
		<div id="dlg2-buttons">
		    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveRun(2);">保存</a>
		    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="$('#dlg2').dialog('close');">取消</a>
		</div>

		<div id="dlg_result" class="easyui-dialog"
			style="width:350px;height:260px;padding:10px;"
			data-options="
            iconCls: 'icon-save',
            buttons: '#dlgres-buttons',
            closed:true,
            modal:true,
            title:'报修结果' ">
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">维修人:</span> 
				<span id="res_repairmanName"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">费用:</span> 
				<span id="res_cost"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">用户反馈:</span> 
				<span id="res_evaluate"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">维修质量:</span> 
				<span id="res_qualityScore"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">服务态度:</span> 
				<span id="res_attitudeScore"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">及时性:</span> 
				<span id="res_timeScore"></span>
			</div>
			<div style="margin: 5px;">
				<span style="width:80px; display:inline-block;">维修价格:</span> 
				<span id="res_priceScore"></span>
			</div>
		</div>
		<div id="dlgres-buttons">
			<a href="javascript:void(0);" class="easyui-linkbutton"
				onclick="$('#dlg_result').dialog('close');">关闭</a>
		</div>
</body>
</html>
