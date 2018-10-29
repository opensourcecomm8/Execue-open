<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<%
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>
<div class="tableList"
	style="height: 325px; width: 180px; margin-left: 0px; margin-bottom: 5px; white-space: nowrap;padding-left:0px;padding-top:3px;overflow-y:auto;">
	<form id="roleFormId">	
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
	id="searchList">
	  <s:iterator value="hierarchies" status="inst" id="role">
            <s:if test="#inst.even == true">
              <tr class="conceptRow1" id="tableRow3" >               
                <td width="100%" colspan="2" >
                <div id='showHierarchiesDiv<s:property value="id"/>Link'><a
				        href="#"
				        class="links" id="" onclick="javascript:showHierarchyDefinitions('<s:property value="id"/>','<s:property value="name"/>');"><s:property
				        value="name" /></a>
				</div>
			  <div id='loadingShowHierarchiesDiv<s:property value="id"/>Link'
				style="display: none;"><img src="../images/admin/loadingBlue.gif"
				width="25" height="25" />
			 </div> 
              </td>
              </tr>
              </s:if>
              <s:else>
                <tr id="conceptRow2">               
                <td width="100%" colspan="2" >
	                <div id='showHierarchiesDiv<s:property value="id"/>Link'><a
					    href="#"
					    class="links" id="" onclick="javascript:showHierarchyDefinitions('<s:property value="id"/>','<s:property value="name"/>');"><s:property
					    value="name" /></a></div>
				  <div id='loadingShowHierarchiesDiv<s:property value="id"/>Link'
					   style="display: none;"><img src="../images/admin/loadingBlue.gif"
					   width="25" height="25" />
				  </div> 
                </td>
              </tr>
            </s:else>
       </s:iterator>    
 <s:hidden name="maxHierarchyDefinitionSize" id="maxHierarchyDefinitionSizeId"></s:hidden>
</table>
</form>
</div>



<script>
highLightText(1);
</script>