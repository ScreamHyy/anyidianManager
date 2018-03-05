<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/jquery-easyui-1.5.2/themes/ui-cupertino/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/js/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/public.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/resources/css/manager.css">
<script type="text/javascript" src="<%=path%>/resources/js/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/resources/js/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/resources/js/common.js"></script>