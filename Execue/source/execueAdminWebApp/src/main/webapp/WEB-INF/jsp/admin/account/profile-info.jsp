<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#dynamicPane select {
	width: 180px;
}
</style>
<div id="errorMessage"
				   style="color:red;font-size: 10px;min-height:5px;height:auto;text-align:left;"><s:actionerror /><s:fielderror /></div>
			 
			<div id="actionMessage" style="color: green;min-height:5px;height:auto;text-align:left"><s:actionmessage /></div>
<s:form id="updateProfileForm">

	<table width="40%" border="0" cellspacing="0" cellpadding="3"
		align="left" id="accountTable" style="margin-left:20px;">
	
		<tr>
			<td><s:text name="execue.account.profile.username" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="user.username" cssClass="textBox"
				id="user.username" readonly="true" maxlength="255"/>
				<a href="#" id="helpLink" title="Help" alt="Help">?</a></td>
		</tr>

		<tr>
			<td width="30%"><s:text name="execue.user.firstName" /></td>
			<td width="2%">&nbsp;</td>
			<td width="68%"><s:textfield name="user.firstName"
				cssClass="textBox" id="user.firstName" maxlength="255"/></td>
		</tr>
		<tr>
			<td><s:text name="execue.user.lastName" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="user.lastName" cssClass="textBox"
				id="user.lastName" maxlength="255" /></td>
		</tr>

		<tr>
			<td valign="top"><s:text name="execue.user.label.address1" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="user.address1" cssClass="textBox" maxlength="255"/></td>
		</tr>
		<tr>
			<td valign="top"><s:text name="execue.user.label.address2" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="user.address2" cssClass="textBox" maxlength="255"/></td>
		</tr>
		<tr>
			<td><s:text name="execue.user.label.city" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="user.city" cssClass="textBox" maxlength="255"/></td>
		</tr>
		<tr>
			<td><s:text name="execue.user.label.state" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="user.state" cssClass="textBox" maxlength="255" /></td>
		</tr>
		<tr>
			<td><s:text name="execue.user.label.zip" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="user.zip" cssClass="textBox" maxlength="255"/></td>
		</tr>
		<tr>
			<td><s:text name="execue.user.label.country" /></td>
			<td>&nbsp;</td>
			<td><s:select name="user.country" listKey="countryCode"
				listValue="description" list="countryCodes" headerKey=""
				headerValue="Select"></s:select></td>
		</tr>


		<tr>
			
			<td align="left" colspan="3"><input class="buttonSize108" type="button"
				id="button" value="<s:text name='execue.account.profile.updateButton'/>"
				onclick=
	updateProfile();;
></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>
</s:form>
<script>
$(function(){
  $("#helpLink").helpMessage({messageKey:"execue.user.profile.help.info"}); 
});
</script>