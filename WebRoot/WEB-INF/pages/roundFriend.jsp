<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
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
<title>邻里圈管理</title>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/resources/js/jquery-easyui-1/datagrid-detailview.js"></script>
<script type="text/javascript">
	var url;
	$(function() {
		$('#dataGrid').datagrid({
        view: detailview,
        detailFormatter:function(index,row){
            return '<div style="padding:2px"><table id="detail'+row.topicId+'" class="ddv"></table></div>';
        },
        onExpandRow: function(index,row){
            var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
            ddv.datagrid({
                url:'commentListData',
                queryParams:{statusId:row.statusId},
                fitColumns:true,
                singleSelect:true,
                rownumbers:true,
        	    pagination:true,
        	    pageSize:'20',
                loadMsg:'正在加载中,请稍候',
                height:'auto',
                columns:[[
                    {field:'name',title:'回帖人',width:80},
                    {field:'content',title:'回复内容',width:500}
                ]],
                onResize:function(){
                    $('#dataGrid').datagrid('fixDetailRowHeight',index);
                },
                onLoadSuccess:function(){
                    setTimeout(function(){
                        $('#dataGrid').datagrid('fixDetailRowHeight',index);
                    },0);
                }
            });
            $('#dataGrid').datagrid('fixDetailRowHeight',index);
        }
    });
	});
	
	
	//搜索条件查询
	function searchFriendCircle() {
		$('#dataGrid').datagrid('load', {
			community : $('#f_community').combobox("getValue"),
			statusType : $('#f_interestGroup').combobox("getValue"),
			name : $('#f_name').val(),
			mobile : $('#f_mobile').val(),
		});
	}
	
	//删除维修人员
	function deleteFriendCircle() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].statusId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteFriendCircleData',
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
	
	function resetValues() {
		$("#community").combobox("setValue","");
		$("#statusType").combobox("setValue","");
		$("#name").val("");
		$("#mobile").val("");
		$("#content").val("");
		
	}
	
	//图片显示
	function imgFormatter(value,row,index){  
     	if('' != value && null != value){  
    		var strs = new Array(); //定义一数组   
    		strs = value.split(","); //字符分割   
        
  			var rvalue ="";            
    		for (i=0;i<strs.length ;i++ ){   
        	rvalue += '<a target="_blank" href="'+strs[i]+'"><img src='+strs[i]+' style="width:50px; height:50px;"/>';  
        	}   
    	return  rvalue;        
     	}  
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
	function openRoundFriendCircle(){
		closeTab("人气榜");
		addTab("人气榜","addRoundFriend");
	}
	
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'roundFriendListData', 
		pageSize:'20', 
		fitColumns:true, 
		pagination:true, 
		rownumbers:false,
		singleSelect:true,
		fit:true,
		nowrap:false,
		toolbar:'#tb'">
		<thead>
			<tr>
				<th field="cb" checkbox="true"></th>
				<!-- <th field="nterestId" width="100" align="center">编号</th> -->
				<th field="community" width="100" align="center">小区</th>
				<th field="name" width="100" align="center">用户名</th>
				<th field="mobile" width="100" align="center">手机</th>
				<th field="date" width="100" align="center">发布时间</th>
				<th field="content" width="100" align="center">内容</th>
				<th field="statusType" width="100" align="center">兴趣组类型</th>
				<th field="likeNum" width="100" align="center">点赞数</th>
				<th field="commentNum" width="100" align="center">评论数</th>
				<th field="myImages" width="100" align="center" formatter="imgFormatter">图片</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:deleteFriendCircle()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div style="margin:4px;">
		&nbsp;&nbsp;选择小区： 
		<select class="easyui-combobox" id="f_community" name="f_community" panelHeight="auto" editable="false" style="width:120px;"
			data-options="
			url:'communityItem',
    		editable:false,
    		valueField:'communityName',
    		textField:'communityName' ">
		</select>
		&nbsp;&nbsp;选择兴趣组类型：
		<select class="easyui-combobox" id="f_interestGroup" name="f_interestGroup" panelHeight="auto" editable="false" style="width:120px;"
			data-options="
			url:'interestItem',
    		editable:false,
    		valueField:'interestType',
    		textField:'interestType' ">
		</select>
		&nbsp;&nbsp;姓名：
		<input id="f_name" name="f_name" type="text" size="22" />
		&nbsp;&nbsp;手机号：
		<input id="f_mobile" name="f_mobile" type="text" size="22" />
		<a href="javascript:searchFriendCircle()" class="easyui-linkbutton"
			iconCls="icon-search" plain="false">搜索</a>		
		</div>
	</div>
</body>
</html>
