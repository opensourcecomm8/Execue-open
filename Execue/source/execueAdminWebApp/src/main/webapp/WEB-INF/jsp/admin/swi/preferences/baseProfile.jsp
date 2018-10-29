<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Base Profile</title>
</head>
<body>
<div class="innerPane2" style="width:99%;" >

<table width="90%" border="0" align="center" cellpadding="5" cellspacing="0">
  <tr>
    <td width="97%" height="40" class="innerPaneHeading"><strong>Define Profile </strong></td>
    <td width="3%" align="center" class="fieldNames"><!--<div id="closeInnerPaneLink" ><a href="javascript:;" id="closeInnerPane"><img src="images/admin/closeButton.jpg" alt="Close" width="14" height="15" border="0" /></a></div><div id="loadingCloseInnerPaneLink" style="display:none;"><img src="images/admin/loading.gif" width="25" height="25"></div>--></td>
  </tr>
  <tr>
    <td colspan="2" class="fieldNames"><strong>Base Profile :</strong> Base Profile 1</td>
  </tr>
  <tr>
    <td colspan="2">
    <s:form namespace="/swi" method="POST" action="profiles">
    <div  style="height:212px;width:600px;border:#D3D3D3 1px solid;padding:3px;">
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="27%"> <div class="tableList" style="height:210px;width:250px;">
      <table width="100%" border="0" cellspacing="0" cellpadding="2">
                     <s:iterator value="domainProfileConcepts" status="inst" id="profile">
                     
                      <tr id="tableRow1" >
                        <td width="15%" class="dotBullet"><label>
                          <input type="checkbox" name="checkbox" id="checkbox" />
                        </label></td>
                        <td width="85%" class="fieldNames"><s:property value="%{#profile.displayName}" />
                          </td>
                      </tr>
                      </s:iterator>
                     
                      </table></div></td>
    <td width="73%" valign="top"><table width="90%" border="0" cellspacing="0" cellpadding="5" align="center">
  <tr>
    <td class="fieldNames">Enter Profile Name</td>
  </tr>
  <tr>
    <td><input name="textfield" type="text" class="textBox" id="textfield" /></td>
  </tr>
  <tr>
    <td><input type="image" name="imageField" id="imageField" src="../images/admin/createProfileButton.gif" /></td>
  </tr>
</table>
</td>
  </tr>
</table>
</div>
    </s:form></td>
  </tr>
</table>
</div>

<script>
$(document).ready(function() { 

$("#closeInnerPane").click(function(){
									
$("#closeInnerPaneLink").hide(); 	
$("#loadingCloseInnerPaneLink").show(); 
$.get("groupList.htm", {}, function(data) { 
									
$("#loadingCloseInnerPaneLink").hide(); 									
$("#closeInnerPaneLink").show(); 	

$("#dynamicPane").empty(); 
$("#dynamicPane").append(data);
$("#dynamicPane").show();  
});
});

});
</script>

</body>
</html>