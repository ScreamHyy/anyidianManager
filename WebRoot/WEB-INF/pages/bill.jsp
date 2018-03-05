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
<title>缴费单管理</title>
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
		//$('#b_community').combobox({url:'communityData'});
		//$('#b_community').prepend("<option value=''>"+全部+"</option>");
		/* $.ajax({
			type:'post',
			url:'communityData',
			dataType: "json",
	        cache:false,
	        success: function (result) {
				if (result != null) {
					//var result = eval("("+result()+")");
					$('#b_community').prepend("<option value=''>全部</option>");
					$('#b_community').append("<option value='"+result.communityName+"'>"+result.communityName+"</option>");
				} else {
				
				}
	        }
	    });  */
		
    	dateSelectUtil('b_bdate');
    	dateSelectUtil('b_edate');
    	dateSelectUtil('date');
    	
    	function dateSelectUtil(id) {
		   var dateId = $('#'+id);
		   dateId.datebox({
		   	//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
	       	onShowPanel: function () {
	          //触发click事件弹出月份层
	          span.trigger('click'); 
	          if (!tds) {
	            //延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
	            setTimeout(function() {
	            	tds = p.find('div.calendar-menu-month-inner td');
	                tds.click(function(e) {
	                    //禁止冒泡执行easyui给月份绑定的事件
	                    e.stopPropagation(); 
	                    //得到年份
	                    var year = /\d{4}/.exec(span.html())[0] ,
	                    //月份
	                    //之前是这样的month = parseInt($(this).attr('abbr'), 10) + 1; 
	                    month = parseInt($(this).attr('abbr'), 10);  
		
				        //隐藏日期对象                     
				        dateId.datebox('hidePanel') 
		           	    //设置日期的值
		           	    .datebox('setValue', year + '-' + month); 
		          	});
		          }, 0);
		       }
            },
            //配置parser，返回选择的日期
            parser: function (s) {
                if (!s) return new Date();
                var arr = s.split('-');
                return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
            },
            //配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth()); 
            formatter: function (d) { 
                var currentMonth = (d.getMonth()+1);
                var currentMonthStr = currentMonth < 10 ? ('0' + currentMonth) : (currentMonth + '');
                return d.getFullYear() + '-' + currentMonthStr; 
           	}
	       });
	        //日期选择对象
	        var p = dateId.datebox('panel'), 
	        //日期选择对象中月份
	        tds = false, 
	        //显示月份层的触发控件
	        span = p.find('span.calendar-text'); 
	        var curr_time = new Date();
	        //设置前当月
	        //$("#b_edate").datebox("setValue", myformatter(curr_time));
		}
		
		//格式化日期
		function myformatter(date) {
		    //获取年份
		    var y = date.getFullYear();
		    //获取月份
		    var m = date.getMonth() + 1;
		    return y + '-' + m;
		}
	});
	
	//搜索条件查询
	function searchBill() {
		$('#dataGrid').datagrid('load', {
			mobile : $('#b_mobile').val(),
			room : $('#b_room').val(),
			community : $('#b_community').combobox("getValue"),
			billType : $('#b_billType').combobox("getValue"),
			state : $('#b_state').combobox("getValue"),
			bdate : $('#b_bdate').val(),
			edate : $('#b_edate').val(),
		});
	}
	
	function showStyle(value,row){
		if(value == "已缴费"){
			return '<span style="color:green">'+value+'</span>';
		} else {
			return '<span style="color:red">'+value+'</span>';
		}
	}

	//删除缴费单
	function deleteBill() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要删除的数据！");
			return;
		}
		var strIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			strIds.push(selectedRows[i].billId);
		}
		var ids = strIds.join(",");
		$.messager.confirm("系统提示", "您确认要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？", function(r) {
			if (r) {
				$.ajax({
					type:'post',
					url:'deleteBillData',
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
	
	function openBillAddDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加缴费单");
		url = "saveBillData";
	}
	
	function closeBillDialog() {
		$("#dlg").dialog("close");
		resetValues();
	}

	//保存缴费单
	function saveBill() {
		$.messager.confirm("系统提示", "您确定要添加该记录吗？提交后便不可更改", function(r) {
			if (r) {
				$("#fm").form("submit", {
					url : url,
					/*默认支持，少天不能提交*/
					onSubmit : function() {
						if ($('#community').combobox("getValue") == "") {
							$.messager.alert("系统提示", "请选择小区");
							return fasle;
						}
						if ($('#billType').combobox("getValue") == "") {
							$.messager.alert("系统提示", "请选择账单类型");
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
		});
	}

	function resetValues() {
		$("#community").combobox("setValue","");
		$("#billType").combobox("setValue","");
		$("#name").val("");
		$("#mobile").val("");
		$("#room").val("");
		$("#date").datebox("setValue","");
		$("#price").val("");
	}

	function openBillModifyDialog() {
		var selectedRows = $("#dataGrid").datagrid('getSelections');
		if(selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "编辑缴费单");
		$("#fm").form("load", row);
		url = "saveBillData?billId=" + row.billId;
	}
</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="缴费单" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'billListData', 
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
				<th field="community" width="100" align="center">小区</th>
				<th field="name" width="100" align="center">姓名</th>
				<th field="mobile" width="100" align="center">手机号</th>
				<th field="room" width="100" align="center">房间</th>
				<th field="billType" width="100" align="center">账单类型</th>
				<th field="date" width="100" align="center">月份</th>
				<th field="price" width="100" align="center">缴费额度</th>
				<th field="state" width="200" align="center" formatter="showStyle">缴费状态</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
		<div style="margin:4px;">
			<a href="javascript:openBillAddDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true">添加</a> 
			<a href="javascript:openBillModifyDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteBill()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">删除</a>
		</div>
		<div style="margin:4px;">
			&nbsp;&nbsp;手机号：
			<input type="text" name="b_mobile" id="b_mobile" size="10" />
			&nbsp;&nbsp;房间：
			<input type="text" name="b_room" id="b_room" size="10" />
			&nbsp;&nbsp;选择小区： 
			<select class="easyui-combobox" id="b_community" name="b_community" panelHeight="auto" editable="false" style="width:100px;"
				data-options="
				url:'communityItem',
	    		editable:false,
	    		valueField:'communityName',
	    		textField:'communityName' ">
	    		<%-- <option value="">全部</option>
		        <c:forEach items="${communityList}" var="item">
		        	<option value="${item.communityName}">${item.communityName}</option>
		        </c:forEach> --%>
			</select> 
			&nbsp;&nbsp;账单类型： 
			<select class="easyui-combobox" id="b_billType" name="b_billType" 
				editable="false" panelHeight="auto" style="width:100px;">
				<option value="">全部</option>
				<option value="物业服务费">物业服务费</option>
				<option value="车辆秩序费">车辆秩序费</option>
				<option value="公摊电费">公摊电费</option>
				<option value="代收电费">代收电费</option>
			</select> 
			&nbsp;&nbsp;缴费状态： 
			<select class="easyui-combobox" id="b_state" name="b_state" 
				editable="false" panelHeight="auto" style="width:100px;">
				<option value="">全部</option>
				<option value="已缴费">已缴费</option>
				<option value="未缴费">未缴费</option>
			</select>
			&nbsp;&nbsp;月份：
			<input id="b_bdate" name="b_bdate" class="easyui-datebox" 
				data-options="
				buttonText:'开始', 
				buttonAlign:'left', 
				width:120, 
				panelAlign:'right', 
				editable:true"/>
			--
			<input id="b_edate" name="b_edate" class="easyui-datebox" 
				data-options="
				buttonText:'结束', 
				buttonAlign:'left', 
				width:120, 
				panelAlign:'right', 
				editable:true"/>
			&nbsp;&nbsp;
			<a href="javascript:searchBill()" class="easyui-linkbutton"
				iconCls="icon-search" plain="false">搜索</a>
		</div>

		<div id="dlg" class="easyui-dialog" data-options="iconCls:'icon-save',buttons:'#dlg-buttons'" 
			style="width:auto; heigth:350px; padding:10px 20px;" closed="true">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td style="width:60px;">小区：</td>
						<td>
							<select class="easyui-combobox" id="community" name="community" panelHeight="auto" style="width:120px;"
								data-options="
								url:'communityItemTwo',
					    		editable:false,
					    		valueField:'communityName',
					    		textField:'communityName' ">
							</select> 
						</td>
						<td style="width:40px;"></td>
						<td style="width:100px;">账单类型：</td>
						<td>
							<select class="easyui-combobox" id="billType" name="billType" 
								editable="false" panelHeight="auto" style="width:120px;">
								<option value="物业服务费">物业服务费</option>
								<option value="车辆秩序费">车辆秩序费</option>
								<option value="公摊电费">公摊电费</option>
								<option value="代收电费">代收电费</option>
							</select> 
						</td>
					</tr>
					<tr>
						<td>姓名：</td>
						<td><input type="text" name="name" id="name" class="easyui-validatebox" required="true" style="width:120px;"/></td>
						<td></td>
						<td>手机号：</td>
						<td>
							<input type="text" name="mobile" id="mobile" class="easyui-validatebox" required="true" style="width:120px;"
							 	onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
						</td>
					</tr>
					<tr>
						<td>房间：</td>
						<td><input type="text" name="room" id="room" class="easyui-validatebox" required="true" style="width:120px;"/></td>
						<td></td>
						<td>缴费额度：</td>
						<td>
							<input name="price" id="price" class="easyui-validatebox" required="true" style="width:120px;" 
								onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" />
						</td>
					</tr>
					<tr>
						<td>月份：</td>
						<td>
							<input id="date" name="date" class="easyui-datebox" 
								data-options="
								width:120, 
								editable:true,
								required:'true'"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="dlg-buttons">
			<a href="javascript:saveBill()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeBillDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
