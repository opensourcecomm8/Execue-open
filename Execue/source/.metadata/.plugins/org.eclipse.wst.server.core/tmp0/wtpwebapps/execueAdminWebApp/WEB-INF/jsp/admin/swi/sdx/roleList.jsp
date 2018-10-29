<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>

<div class="tableList"
	style="height: 265px; width: 180px; margin-left: 0px; margin-bottom: 5px; white-space: nowrap;padding-left:0px;padding-top:3px;overflow-y:hidden;">
	<form id="roleFormId">	
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
	id="searchList">
	  <s:iterator value="roles" status="inst" id="role">
            <s:if test="#inst.even == true">
              <tr class="conceptRow1" id="tableRow3" >               
                <td width="100%" colspan="2" >
                <div id='showRoleDiv<s:property value="name"/>Link'><a
				        href="#"
				        class="links" id="" onclick="javascript:showObjectsForRole('<s:property value="name"/>');"><s:property
				        value="name" /></a>
				</div>
			  <div id='loadingShowRoleDiv<s:property value="name"/>Link'
				style="display: none;"><img src="../images/admin/loadingBlue.gif"
				width="25" height="25" />
			 </div> 
              </td>
              </tr>
              </s:if>
              <s:else>
                <tr id="conceptRow2">               
                <td width="100%" colspan="2" >
	                <div id='showRoleDiv<s:property value="name"/>Link'><a
					    href="#"
					    class="links" id="" onclick="javascript:showObjectsForRole('<s:property value="name"/>');"><s:property
					    value="name" /></a></div>
				  <div id='loadingShowRoleDiv<s:property value="name"/>Link'
					   style="display: none;"><img src="../images/admin/loadingBlue.gif"
					   width="25" height="25" />
				  </div> 
                </td>
              </tr>
            </s:else>
       </s:iterator>    
</table>
</form>
</div>
<div id="paginationDiv2" style="margin-top: 10px; margin-bottom: 5px;"><pg:page targetURL="showSecurityRoles.action" targetPane="roleDynamicPane"/></div>


<script>
function showObjectsForRole(roleName){
  var context=$("#contextId").val();   
	  if(context=='Asset'){
	    showAssets(roleName);
	  }else if(context=='Table'){
	    showTables(roleName);
	  }else if(context=='Column'){
	    showColumns(roleName);
	  }else if(context=='Member'){
	    showMembers(roleName);
	  }      
 }
highLightText(1);

</script>