
<!-- This is page is body part of the tiles -->
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.role.pageHeading" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center">
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText" align="left">
                
                
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                   
                    <td width="100%" align="left" height="25">
                    
                    <div id="addNewRoleLink"><a href="javascript:;" class="arrowBg" id="addNewRole">Add
		New Role</a></div>
		<div id="loadingAddNewRoleLink" style="display: none;"><img
			src="../images/admin/loadingBlue.gif" width="25" height="25"></div>
            
            
          <div id="closeInnerPaneLink" style="display:none;"><a class="arrowBg" href="javascript:;"
			id="closeInnerPane"><s:text name="execue.role.showAllRoles.link" /></a></div>
		<div id="loadingCloseInnerPaneLink" style="display: none;"><img
			src="../images/admin/loading.gif" width="20" height="20"></div>
            
            
            </td>
                  </tr>
                  
                  <tr>
                    <td width="100%" height="25"><s:text name="execue.roles.description" /></td>
                  
                  </tr>
                  
                </table>
                
                    
                    </td>
			</tr>
			<tr>
				<td valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					
					<tr>
						<td height="300" align="center" valign="top">
						<div id="dynamicPane"><jsp:include
							page="/WEB-INF/jsp/admin/swi/security/rolesList.jsp" /></div>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>