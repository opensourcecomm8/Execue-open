<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<jsp:include page="/WEB-INF/jsp/admin/swi/sdx/sdxPopupTemplate.jsp"></jsp:include>
<div id="outerPane">
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="50" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.showAssets.pageHeading" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0"><tr><td>
           
           <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
			<tr>
                    <td align="left" height="30" width="100%"><div id="addNewUserLink"><a href="../swi/newAsset.action"
									class="arrowBg"><s:text
									name="execue.showAssets.createAsset.linkName" /></a></div>
								<div id="loadingAddNewUserLink" style="display: none;"><img
									src="../images/admin/loadingBlue.gif" width="25" height="25"></div></td>
			</tr>
            <tr>
				<td valign="top" width="100%" class="descriptionText"><s:text
					name="execue.showAssets.description" /></td>
			</tr>
			 <tr>				
				<td valign="top" width="100%" ><div id="messageDiv"></div></td>                   
			</tr>
            </table>
            
            
            </td></tr>
			
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->

					<tr>
						<td height="300" align="center" valign="top">
						<div id="dynamicPane">

						<div id="dynamicPaneAssets"></div>
						</div>
						</td>
					</tr>
					
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
</div>