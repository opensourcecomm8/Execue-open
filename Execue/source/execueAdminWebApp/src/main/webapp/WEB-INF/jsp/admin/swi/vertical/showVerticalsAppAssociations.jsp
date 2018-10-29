<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
$(document).ready(function() {
	showVerticalsAppAssociations();
});

function showVerticalsAppAssociations() {
  showDetails("swi/getVerticalAppAssociation.action?seletedVerticalId="+<s:property value="seletedVerticalId"/>,"divVerticals","get");  
}
</script>
<table width="90%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td colspan="3" valign="top" class="descriptionText"><s:text
					name="Create Verticals App Associations" /></td>
			</tr>

			<tr>
				<td colspan="3" valign="top" background="../images/blueLine.jpg"><img
					src="../images/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="19%" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="27"><span class="tableSubHeading"><s:text
							name='Verticals List' /></span></td>
					</tr>
					<tr>
						<td height="25"><a
							href="javascript:createNewVerticalAppAssociations();this.blur()"
							class="arrowBg" id="addNewVertical"><s:text
							name='Add New App Association' /></a> <!--div id="loadingAddNewConceptLink" style="display:none;"><img src="../images/loading.gif" width="20" height="20" /></div--></td>
					</tr>

					<tr>
						<td align="left">
						<div class="tableBorder"
							style="padding-top: 5px; width: auto; margin-bottom: 5px; border: none;">
						<table width="99%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" align="left" valign="top">
								<div id="divVerticals"></DIV>


								</div>
								</td>
							</tr>

						</table>

						</div>
						</td>
					</tr>
				</table>
				</td>
				<td width="3%" height="350" align="center" class="blueLineVeritical">&nbsp;</td>
				<td width="78%" valign="top">
				<table border="0" cellpadding="0" cellspacing="0">
					<!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->

					<tr>
						<td height="350" align="left" valign="top">

						<div id="dynamicPane" style="width: 720px"></div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="3" valign="top" background="../images/blueLine.jpg"><img
					src="../images/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td colspan="3">&nbsp;</td>
			</tr>

		</table>
		</div>
		</td>
	</tr>

</table>

