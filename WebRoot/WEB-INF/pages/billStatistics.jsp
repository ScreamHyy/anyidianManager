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
<style type="text/css">
.datagrid-row {
	height: 50px;
}
</style>
<script type="text/javascript">
	var url;
	$(function() {
	
		$('#dataGrid').datagrid('load', {
			billType : "物业服务费",
			state : "已缴费",
		});
	
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

</script>
</head>

<body style="margin: 5px;">
	<table id="dataGrid" title="缴费单" class="easyui-datagrid"
		data-options="
		iconCls:'icon-edit', 
		url:'billListStatisticsData', 
		pageSize:'20', 
		fitColumns:true, 
		pagination:true, 
		rownumbers:true,
		singleSelect:true,
		nowrap:false,	//换行
		fit:true,
		toolbar:'#tb'">
		<thead>
			<tr>
				<th field="community" width="100" align="center">小区</th>
				<th field="name" width="100" align="center">姓名</th>
				<th field="mobile" width="100" align="center">手机号</th>
				<th field="room" width="100" align="center">房间</th>
				<th field="billType" width="100" align="center">账单类型</th>
				<th field="price" width="100" align="center">缴费总额</th>
				<th field="date" width="220" align="center">缴费月份</th>
				<th field="state" width="100" align="center" formatter="showStyle">缴费状态</th>
			</tr>
		</thead>
	</table>
	<div id="tb">
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
				<option value="物业服务费">物业服务费</option>
				<option value="车辆秩序费">车辆秩序费</option>
				<option value="公摊电费">公摊电费</option>
				<option value="代收电费">代收电费</option>
			</select> 
			&nbsp;&nbsp;缴费状态： 
			<select class="easyui-combobox" id="b_state" name="b_state" 
				editable="false" panelHeight="auto" style="width:100px;">
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
