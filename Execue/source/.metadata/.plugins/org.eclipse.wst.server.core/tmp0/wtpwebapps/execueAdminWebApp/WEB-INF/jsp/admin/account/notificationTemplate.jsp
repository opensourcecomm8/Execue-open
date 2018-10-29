<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>semantifi | meaning based search | business intelligence |
search databases | search data sets | bi tools</title>
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFF;
}

li {
	line-height: 22px;
	list-style-type: none;
}
-->
</style>
<script src="../js/jquery.js"></script>
<script>
$(function(){
	$("#templateType").change(function(){
	 var templateType=$("#templateType option:selected").val();
	  $("#templateTypeId").val(templateType);
	});	   
 });
function addParam(){
	var templateContent=$("#templateContent").val();
	var paramSelection=$("#paramSelection option:selected").val();
	$("#templateContent").val(templateContent+" &"+paramSelection);
}
function getNotificationTemplate(notificationType,disNotificationType){
 $("#message").empty();
  $("#errorMessage").empty();
var templateType=$("#templateType option:selected").val();
$("#displayNotificationType").empty().append("Notification Type: </strong>"+disNotificationType);
 $.post("../account/getNotificationTemplate.action?notificationTemplate.notificationType="+notificationType+"&notificationTemplate.templateType="+templateType , function(data) {
       data=eval('('+data+')');
       $("#templateContent").val(data.template);
       $("#notificationTemplateId").val(data.id);
       $("#notificationTypeId").val(data.notificationType);
       $("#templateTypeId").val(data.templateType);
	}); 
 
}

function saveTemplate(){
  $("#message").empty();
  $("#errorMessage").empty();
	var templateContent=$("#templateContent").val();	
	if($.trim(templateContent)==""){
		 $("#errorMessage").empty().html("please enter content in template text area");
		 $("#templateContent").focus();
		 return false;
	}
  var notificationTemplateForm=$("#notificationTemplateForm").serialize();  
   $.post("../account/saveUpdateNotificationTemplate.action",notificationTemplateForm, function(data) {
    data=eval('('+data+')');   
    if(data.message!=null){           
       $("#templateContent").val(data.template);
       $("#notificationTemplateId").val(data.id);
       $("#notificationTypeId").val(data.notificationType);
       $("#templateTypeId").val(data.templateType);
        $("#message").empty().append(data.message);
       }else{
         $("#errorMessage").empty().append(data.errorMessage);
       }       
	}); 
  
  
}
</script>
</head>

<body><table width="970" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="execue.notification.main.notification.template"/></td>
			</tr>
		</table></td>
  </tr>
  <tr>
    <td><div id="greyBorder">
<table width="98%" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-bottom:20px;">
	<tr>
		<td height="20" valign="bottom">
		
		</td>
	</tr>
	<tr>
		<td height="20"
			style="border-bottom: 1px dashed #CCC; ">&nbsp;</td>
	</tr>
	<tr>
		<td height="400" valign="top" style="border-bottom: 1px dashed #CCC;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="22%" height="400" valign="top"
					style="padding-top: 20px; border-right: 1px dashed #CCC;">
				<div class="tableBorder"
					style="padding-top: 5px; height: auto; width: 200px; margin-bottom: 5px; border: none;line-height:22px;"><strong><s:text name="execue.notification.main.notification.type"/> :</strong>
              <div style="height: 265px; width: 190px; margin-left: 0px; margin-bottom: 5px; white-space: nowrap; padding-left: 0px; padding-top: 3px; " class="tableList">  
				<ul style="margin:0px;padding:0px;padding-left:10px;">
					<s:iterator value="notificationTypes">
						<li><a
							href="javascript:getNotificationTemplate('<s:property/>','<s:property value="description"/>');"><s:property
							value="description" /> </a></li>
					</s:iterator>
				</ul>
                </div>
				</div>
				</td>
				<td width="78%" valign="top"
					style="padding-top: 20px; ">
                    
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-left:20px;">
  <tr>
    <td><div id="errorMessage" style="color:#F00;padding-bottom:5px;"><div></td>
  </tr>
  <tr>
    <td><div id="message" style="color:green;padding-bottom:5px;"><div></td>
  </tr>
  <tr>
    <td><table width="260" border="0" align="left" cellpadding="0"
							cellspacing="0">
					      <tr>
					        <td width="250"><s:text name="execue.notification.main.template.type"/></td>
					        <td width="150"><label><s:select id="templateType"
									list="templateTypes" listValue="description" /> </label></td>
					        </tr>
					      </table></td>
  </tr>
  <tr><td>
  <div id="displayNotificationType" style="padding-top:10px;"></div>
  </td></tr>
  <tr>
    <td><div style="width:550px;height:270px;border:1px solid #ccc;margin-top:20px;padding:10px;"><table width="80%" border="0" align="left" cellpadding="5"
					cellspacing="0" >
					
					
					<tr>
					  <td width="81%"><s:text name="execue.notification.main.select.parameter"/> <s:select
							id="paramSelection" list="notificationParamNames" listKey="value"
							listValue="value" /><label> <input type="submit"
							name="button2" id="button2" value="Add" onclick="addParam();" />
					    </label></td>
					  </tr>
					<s:form id="notificationTemplateForm">
						<tr>
							<td><label> <s:textarea
								name="notificationTemplate.template" id="templateContent"
								cols="60" rows="10"></s:textarea> </label></td>
						</tr>
						<tr>
							<td><s:hidden name="notificationTemplate.notificationType"
								id="notificationTypeId" /></td>
							<td><s:hidden name="notificationTemplate.templateType"
								id="templateTypeId" /></td>
							<td><s:hidden name="notificationTemplate.id"
								id="notificationTemplateId" /></td>
						</tr>
						<tr>
							<td><label> <input type="button" name="button"
								id="button" value="Update" onclick="saveTemplate();" /> </label></td>
						</tr>
					</s:form>

				</table>
                
                </div>
                </td>
  </tr>
</table>

				
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div></td>
  </tr>
</table>


</body>
</html>
