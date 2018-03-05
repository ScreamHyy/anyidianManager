<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">

<title>系统登录</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="renderer" content="webkit" />
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/login.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/jquery-easyui-1.5.2/themes/icon.css">
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/resources/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/resources/js/common.js"></script>
<style type="text/css">
body {
	FILTER: progid:DXImageTransform.Microsoft.Gradient(gradientType=0,
		startColorStr=#dbeff6, endColorStr=#41accf); /*IE 6 7 8*/
	background: -ms-linear-gradient(top, #dbeff6, #41accf); /* IE 10 */
	background: -moz-linear-gradient(top, #dbeff6, #41accf); /*火狐*/
	background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#dbeff6),
		to(#41accf)); /*谷歌*/
	background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#dbeff6),
		to(#41accf)); /* Safari 4-5, Chrome 1-9*/
	background: -webkit-linear-gradient(top, #dbeff6, #41accf);
	/*Safari5.1 Chrome 10+*/
	background: -o-linear-gradient(top, #dbeff6, #41accf); /*Opera 11.10+*/
}

</style>

</head>

<body style="overflow: hidden;">
	<div class="login_bg_img"
		style="background: url(<%=path%>/resources/images/login.png) no-repeat center 26%;margin:0 auto;width:800px;height:1000px;display:block;">
		<form class="form" action="login" method="post" id="frm_login">
			<img src="<%=path%>/resources/images/logo.png" style="position:absolute; top:210px; left:100px;" /> 
			<label class="user_label">账号: 
				<input type="text" value="${username}" name="username" id="username" class="easyui-validatebox input_t1" data-options="prompt:'请输入登录账号.',required:true" />
			</label> 
			<label class="pwd_label">密码:
				<input type="text" onfocus="this.type='password'" autocomplete="off" value="${password}" name="password" id="password" class="easyui-validatebox input_t1" data-options="prompt:'请输入登录密码.',required:true" />
			</label> 
			<a class="login_text">登录系统</a>
			<input class="login_btn" type="submit">
			<label class="error_label"><font id="font_error" color="red">${error}</font></label> 
			<label class="bq_label">贵州安顺银驹物业有限公司版权所有</label>
		</form>
	</div>
</body>
</html>
