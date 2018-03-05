
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

	//权限验证
	String username = (String) session.getAttribute("username");
	if (username == null) {
		//System.out.println("快去登录");
		response.sendRedirect("index.jsp");
		return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>安易点后台管理系统</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" 	href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>

<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/manager.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/public.css">
<script type="text/javascript" src="<%=path%>/resources/js/common.js"></script>

<script type="text/javascript">
	var url;	//跳转地址

	$(function() {
		//数据
		/* var treeHomeData = [
		{
			text : "报修信息管理",
			attributes : {
				url : "repairList"
			} 
		},
		{
			text : "缴费单管理",
			attributes : {
				url : "bill"
			} 
		},
		{
			text : "投诉建议管理",
			attributes : {
				url : "complaintProposal"
			} 
		},
		{
			text : "居委会通知管理",
			attributes : {
				url : "committeeNotice"
			} 
		},
		{
			text : "本地生活圈管理",
			attributes : {
				url : "lifeCircle"
			} 
		},
		{
			text : "生活助手管理",
			attributes : {
				url : "helper"
			} 
		},
		{
			text : "新房信息管理",
			attributes : {
				url : "newHouse"
			} 
		},
		{
			text : "二手房管理",
			attributes : {
				url : "oldHouse"
			} 
		},
		{
			text : "轮播图管理",
			attributes : {
				url : "topBanner"
			}
		}
		]
		
		var treeEstateData = [ 
		{
			text : "全民营销管理",
			attributes : {
				url : "nationalMarketing"
			} 
		},
		{
			text : "优惠政策管理",
			attributes : {
				url : "favouredPolicy"
			} 
		},
		{
			text : "最新资讯管理",
			attributes : {
				url : "newStatus"
			} 
		},
		];
		
		var treePropertyData = [ 
		{
			text : "行业动态管理",
			attributes : {
				url : "industryNews"
			} 
		},
		{
			text : "政策法规管理",
			attributes : {
				url : "policiesRegulations"
			} 
		},
		{
			text : "招标信息管理",
			attributes : {
				url : "biddingInfo"
			} 
		},
		{
			text : "通知公告管理",
			attributes : {
				url : "noticeBulletin"
			} 
		},
		{
			text : "小区公告管理",
			attributes : {
				url : "communityBulletin"
			} 
		},
		{
			text : "志愿者招募",
			attributes : {
				url : "volunteerRecruitment"
			} 
		},
		{
			text : "小区信息管理",
			attributes : {
				url : "community"
			} 
		},
		{
			text : "维修人员管理",
			attributes : {
				url : "repairMan"
			} 
		},
		{
			text : "维修条目管理",
			attributes : {
				url : "repairItem"
			} 
		},
		];
		
		var treeFriendCircleData = [ 
		{
			text : "邻里圈管理",
			attributes : {
				url : "friendCircle"
			} 
		},
		{
			text : "兴趣组管理",
			attributes : {
				url : "interestGroup"
			} 
		},
		];
		
		var treeUserData = [
		{
			text : "登录角色管理",
			attributes : {
				url : "manager"
			} 
		},

		];
		
		var treeUserData = [
		{
			text : "登录角色管理",
			attributes : {
				url : "manager"
			} 
		},
		{
			text : "户主信息管理",
			attributes : {
				url : "user"
			} 
		},
		{
			text : "积分兑换记录",
			attributes : {
				url : "convertibilityRecord"
			} 
		},
		{
			text : "缴费单统计",
			attributes : {
				url : "billStatistics"
			} 
		}
		];
		
		var treeSupermarketData = [ 
		{
			text : "商家管理",
			attributes : {
				url : "businessManage"
			} 
		},
		{
			text : "积分商城管理",
			attributes : {
				url : "integralMall"
			} 
		},
		{
			text : "商家商品管理",
			attributes : {
				url : "commodity"
			} 
		},
		{
			text : "订单管理",
			attributes : {
				url : "orderList"
			} 
		},
		]; */

		//实例化树菜单
		/* $("#homeTree").tree({
			data : treeHomeData,
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});
		
		$("#estateTree").tree({
			data : treeEstateData,
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});
		
		$("#propertyTree").tree({
			data : treePropertyData,
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});
		
		$("#friendCircleTree").tree({
			data : treeFriendCircleData,
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});
		
		$("#userTree").tree({
			data : treeUserData,
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});
		
		$("#supermarketTree").tree({
			data : treeSupermarketData,
			lines : true,
			onClick : function(node) {
				if (node.attributes) {
					openTab(node.text, node.attributes.url);
				}
			}
		});
 */
 		//ajax提交获取数据，加载左侧菜单栏
 		$.ajax({
 			url : 'menu',
 			type : 'post',
 			datatype : 'json',
 			success : function(data){
 				var d = $.parseJSON(data);
 				$.each(d, function(index,m){
 					var flag = false;
 					if (m.mid == 1){
 						flag = true;
 					}
 					$('#navMenu').accordion('add',{
 						title : m.text,
 						iconCls : m.miconCls,
 						content : '<div id='+m.text+' class="tabbtn" style="padding:10px;"></div>',
 						selected : flag
 					});
 				});
 			}
 		});
 		
 		//选择菜单时加载相应子菜单
 		$("#navMenu").accordion({
 			onSelect : function(title, index) {
 				$('#'+title).tree({
 					url : 'menu',
 					method: 'post',
 					queryParams :{
 						mid : index+1
 					},
 					lines : true,
 					onClick : function(node) {
						openTab(node.text, node.murl);
					}
 				});
 			}
 		});
 		
		//新增Tab
		function openTab(text, url) {
			//关闭当前窗口，只显示一个窗口
			var currTab = $('#tabs').tabs('getSelected');
		    if(currTab){
		        $('#tabs').tabs('close',currTab.panel('options').title);
		    }
			if ($("#tabs").tabs('exists', text)) {
				$("#tabs").tabs('select', text)
			} else {
				var content = "<iframe fameborder='0' scrolling='auto' style='width:100%; height:100%' src="
						+ url + "></iframe>"
				$("#tabs").tabs('add', {
					title : text,
					closable : true,
					content : content
				});
			}
		}
		
	});

	//更改密码弹窗
	function openUpdatePwdDialog() {
		$("#dlg_pwd").dialog("open");
		url = "updatePwd";
	}
	
	function closeUpdatePwdDialog() {
		$("#dlg_pwd").dialog("close");
		resetValues();
	}
	
	function resetValues() {
		$("#oldpwd").val("");
		$("#newpwd").val("");
		$("#confirmpwd").val("");
	}
	
	//更改密码
	function savePwd() {
		$("#frm_pwd").form("submit", {
			url:url,
			onSubmit:function() {
				return $(this).form("validate");
			},
			success:function(result) {
				var result = JSON.parse(result);
				if(result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				} else {
					$.messager.alert("系统提示", "修改成功");
					resetValues();
					$("#dlg_pwd").dialog("close");
				}
			}
		});
		
	} 
</script>

</head>

<body class="easyui-layout">
	<div region="north" style="height:90px;">
		<div style="background-color:#AED0EA; height:88px;">
			<div class="topleft"">
				<a> 
					<img src="<%=path%>/resources/images/logo.png" height="70px" title="系统首页" />
				</a>
			</div>
			<div class="topright">
				<ul>
					<li><a href="javascript:void(0);"
						onclick="openUpdatePwdDialog();">修改密码</a></li>
					<li><img src="<%=path%>/resources/images/line.png" height="13px" /></li>
					<li><a href="logout">注销</a></li>
				</ul>
				<div class="us123">
					<span>${username}&nbsp;&nbsp;</span>
				</div>
			</div>
		</div>
	</div>
	<div region="center">
		<div class="easyui-tabs" fit="true" border="false" id="tabs">
			<div title="首页">
				<div align="center" style="padding-top: 100px;">
					<font color="red" size="10">欢迎使用</font>
				</div>
			</div>
		</div>
	</div>
	<div region="west" style="width: 150px;" title="导航菜单" split="true">
		<div id="navMenu" class="easyui-accordion" data-options="fit:true, border:false">
			<!-- <div title="主页管理" class="tabbtn" data-options="iconCls:'icon-ok'" selected="true" style="padding:10px;">
				<ul id="homeTree"></ul>
			</div>
			
	        <div title="房产管理" class="tabbtn" data-options="iconCls:'icon-ok'" style="padding:10px;">
	        	<ul id="estateTree"></ul>
	        </div>
	        
	        <div title="物业管理" data-options="iconCls:'icon-ok'" style="padding:10px;">
	        	<ul id="propertyTree"></ul>
	        </div>
	        
	        <div title="邻里圈管理" class="tabbtn" data-options="iconCls:'icon-ok'" style="padding:10px;">
	        	<ul id="friendCircleTree"></ul>
	        </div>
	        
	        <div title="用户管理" class="tabbtn" data-options="iconCls:'icon-ok'" style="padding:10px;">
	        	<ul id="userTree"></ul>
	        </div>
	        
	        <div title="超市管理" data-options="iconCls:'icon-ok'" style="padding:10px;">
				<ul id="supermarketTree"></ul>
	        </div> -->
		</div> 
	</div>
	<div region="south" style="height: 25px;" align="center">
		版权所有<a href="https://www.baidu.com">https://www.baidu.com</a>
	</div>

	<!-- 修改密码弹窗 -->
	<div id="dlg_pwd" class="easyui-dialog" style="width:350px;height:200px;padding:10px;"
	        data-options="iconCls:'icon-save', buttons: '#dlgpwd-buttons', closed:true, modal:true, title:'修改密码'">
		<form id="frm_pwd" method="post">
	    	<div style="margin: 5px;">
		    	<span style="width:80px; display:inline-block;">原始密码:</span>
		    	<input type="password" id="oldpwd" name="oldpwd" class="easyui-validatebox textbox" style="width:180px;" data-options="prompt:'请输入原始密码.',required:true,validType:'length[5,20]'" />
	    	</div>
	    	<div style="margin: 5px;">
		    	<span style="width:80px; display:inline-block;">新密码:</span>
		    	<input type="password" id="newpwd" name="newpwd" class="easyui-validatebox textbox" style="width:180px;" data-options="prompt:'请输入新密码.',required:true,validType:'length[5,20]'" />
	    	</div>
	    	<div style="margin: 5px;">
		    	<span style="width:80px; display:inline-block;">确认密码:</span>
		    	<input type="password" id="confirmpwd" name="confirmpwd" class="easyui-validatebox textbox" style="width:180px;" data-options="prompt:'确认新密码.',required:true,validType:'equalTo[\'#newpwd\']'" />
	    	</div>
	    </form>
		<div id="dlgpwd-buttons">
			<a href="javascript:savePwd()" class="easyui-linkbutton" iconCls="icon-ok">确认</a> 
			<a href="javascript:closeUpdatePwdDialog()" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
		</div>
	</div>
</body>
</html>
