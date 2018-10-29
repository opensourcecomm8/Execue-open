<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="outerPane">
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.showDataSources.pageHeading" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">

			<tr>
				<td valign="top" >
                
               <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
			   <tr>
				<td valign="top" width="70%" class="descriptionText"><s:text name="execue.showDatasources.description" /></td>
                    
                    
                    <td align="right" height="30" width="30%"><div id="addNewUserLink"><a
									href="../swi/showDataSource.action" class="arrowBg"><s:text
									name="execue.showDataSources.createDataSource.linkName" /></a></div>
								<div id="loadingAddNewUserLink" style="display: none;"><img
									src="../images/admin/loadingBlue.gif" width="20" height="20"></div></td>
			</tr>
            </table> 
                    
                    
                    </td>
			</tr>
			
			<!-- tr>
              <td>
                <s:actionmessage/>
              </td>
            </tr-->
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->
					
					<tr>
						<td height="300" align="center" valign="top">
						<div id="dynamicPane">
						<div id="errorMessages" style="color: red"><s:actionerror /><s:fielderror /></div>
						<div id="actionMessages" style="color: green"><s:actionmessage /></div>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							
							<tr>
								<td>
								<div id="dynamicPaneDataSources"></div>
								</td>
							</tr>
						</table>
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
