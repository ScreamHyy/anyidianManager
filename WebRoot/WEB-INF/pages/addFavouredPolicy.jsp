<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.anyidian.util.StringUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>"></base>
<title>优惠政策详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>

	<script type="text/javascript">
	
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
			
			//显示原本的图片
			if(${status.image != null && status.image != ''}) {
				$("#imageShow").attr("height", "400px");
				$("#imageShow").attr("src", "${status.image}");
				$("#imagePage").attr("href", "${status.image}");
			}
			
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
			                $("#imageShow").attr("height", "400px");
			                $("#imageShow").attr("src", this.result);
			                $("#imagePage").attr("href", this.result);
			            }  
			            // 读取指定文件并形成URL
			            reader.readAsDataURL(file);
			        }
			    }
			});  
		});
		
		//删除图片按钮
		function deleteImage() {
			$("#image").val("");
			$("#imageUrl").textbox("setValue","");
			//$("#imageShow").attr("height", "0");
			$("#imageShow").attr("src", "");
			divPreview.style.display = 'none'; //隐藏
		}
		
		//判断是否为空 空格
		function JTrim(s) {  
		    return s.replace(/(^\s*)|(\s*$)/g, "");  
		}
		
		//保存信息
		function saveFavouredPolicyInfo() {
			var detail = $("#detail").val();
			if(JTrim(detail) == "") {
				$.messager.confirm("系统提示", "您确定不在消息详情里说点什么吗？", function(r) {
					if (r) {
						$("#frm_info").form("submit", {
						    url:'saveFavouredPolicy',
				            async: false,
				            cache: false,
				            contentType: false,
						    onSubmit: function() {
						        return $(this).form('validate'); 
						    },
						    success:function(result){
						    	var result = JSON.parse(result);
								if (result.errorMsg) {
									$.messager.alert("系统提示", result.errorMsg);
									return;
								} else {
									//$.messager.alert("系统提示", result.success)
									resetValues();
									window.location.href="favouredPolicy";
								}
						    }
						});
					}
				});
			} else {
				var detailStr = detail.replace("//r/n/gi", "/n/n");
				$("#detail").textbox("setValue",detailStr);
				$("#frm_info").form("submit", {
				    url:'saveFavouredPolicy',
		            async: false,
		            cache: false,
		            contentType: false,
				    onSubmit: function() {
				        return $(this).form('validate'); 
				    },
				    success:function(result){
				    	var result = JSON.parse(result);
						if (result.errorMsg) {
							$.messager.alert("系统提示", result.errorMsg);
							return;
						} else {
							//$.messager.alert("系统提示", result.success)
							resetValues();
							window.location.href="favouredPolicy";
						}
				    }
				});
			}
		}
		
		function resetValues() {
			$("#statusId").val("");
			$("#statusType").val("");
			$("#image").val("");
			$("#title").val("");
			$("#imageUrl").textbox("setValue","");
			$("#detail").val("");
		}
	</script>

	<form id="frm_info" method="post" enctype="multipart/form-data"
		style="margin-top:15px;">
		<input type="hidden" name="statusId" value="${status.statusId}" /> <input
			type="hidden" name="statusType" value="优惠政策" /> <input type="hidden"
			id="image" name="image" value="${status.image}" />
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">标题：</span> <input
				type="text" id="title" name="title" value="${status.title}"
				class="easyui-validatebox" style="width:200px;"
				data-options="prompt:'请输入消息标题.',required:true,validType:'maxLength[50]'" />
		</div>
		<div style="margin:8px;">
			<span style="width:100px; display:inline-block;">选择图片：</span> <input
				id="imageUrl" name="imageUrl" class="easyui-filebox"
				style="width:200px;" accept="image/png,image/jpeg,image/gif"
				data-options="prompt:'请选择图片',buttonText:'选择图片'" /> <a
				href="javascript:deleteImage()" class="easyui-linkbutton"
				iconCls="icon-no" plain="false" style="margin-left:4px;"></a>
		</div>
		<div id="divPreview" style="margin:8px;">
			<a target="_blank" id="imagePage" style="margin:8px;"> <img
				id="imageShow" />
			</a>
		</div>
		<div style="margin:8px;">
			<div style="width:100px;margin:10px 0;">内容：</div>
			<textarea id="detail" name="detail" class="easyui-textbox"
				style="width:800px;height:320px;padding:8px;"
				data-options="multiline:true,prompt:'请输入消息详情...'">${status.detail}</textarea>
		</div>
	</form>
	<div style="margin:8px 8px 16px 8px;">
		<a href="javascript:saveFavouredPolicyInfo();" class="easyui-linkbutton"
			iconCls="icon-save">发布通知</a>
	</div>
</body>
</html>
