<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<style>
#actionMessage ul {
	margin: 0px;
	padding: 0px;
	margin-bottom: 5px;
}
</style>

<div id="errorMessage"
	style="color: red; padding-top: 5px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage"
	style="color: green; padding-top: 0px; padding-left: 20px;"><s:actionmessage /></div>
<div id="statusMessage" style="display: none; padding-left: 20px;"></div>
<div id="validationMessage" style="display: none; padding-left: 20px;"></div>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0" style="margin-left: 20px;">


	<tr>
		<td>
		<table width="70%" border="0" cellspacing="5" cellpadding="0">
			<tr>
				<td height="20" colspan="3"><strong><s:text name="execue.vertical.createAppAssociation"/></strong></td>
			</tr>
			<tr>
				<td width="40%">
				<s:select list="filteredVerticals" name="filteredVerticalSelect" id="filteredVerticalSelect" listKey="id" listValue="name" value="filteredVerticalId"/></td>
				<td width="7%">&nbsp;</td>
				<td width="68%">&nbsp;</td>
			</tr>
			<tr>
				<td valign="top">
				<div id="divFilteredApps"/>
				</td>
				<td><a href="javascript:moveApp();"><img height="22" border="0"
					title="Move to right" alt="Move to right"
					src="../images/moveRightButton.gif"></a>
					<a href="javascript:copyApp();"><img height="22" style="margin-top:10px;" border="0"
					title="Copy to right" alt="Copy to right"
					src="../images/copyRightButton.gif"></a></td>
				<td valign="top">
				<div id="divExistingApps"/>
				</td>
                <td valign="top" style="padding-top:40px;">
				<label><input type="button" style="margin-left: 10px;" onclick="javascript:deleteApp();"
					class="singleButton" name="Delete" id="Delete" value="Delete" /> </label>
				</td>
			</tr>

		</table>

		</td>
	</tr>

	<tr>
		<td height="35" colspan="2" align="left">
		<div style="padding-left: 2px;"><input id="submitButton"
			type="button" class="singleButton" value="Update"
			onClick="javascript:return updateAssociation();" /> <input
			id="submitButtonLoader" type="button" style="display: none"
			class="singleButtonLoader" disabled="disabled" value="Submit" />
			<a href="#" style="margin-left: 20px;" id="resetButton"
				onClick='javascript:getVerticalAssociation(<s:property value="selectedVerticalId"/>);'> Reset</a>
			<img id="resetButtonLoader" style="display: none; margin-left: 20px;"
				src="../images/loadingBlue.gif" />&nbsp;&nbsp;
			</div>
		</td>
	</tr>
</table>
</td>
</tr>

</table>
<input type="hidden" id="selectedVerticalId" value="<s:property value="selectedVerticalId"/>" />