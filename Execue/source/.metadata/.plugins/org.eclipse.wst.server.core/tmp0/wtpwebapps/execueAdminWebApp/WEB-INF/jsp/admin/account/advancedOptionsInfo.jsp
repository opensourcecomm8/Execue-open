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
<s:form id="updateAdvancedForm">

	<table>
		<tr>
			<td ><s:text name='execue.advOptions.requestMessage' /></td>
		</tr>
	</table>
	<br></br>
	<table width="40%" border="0" cellspacing="0" cellpadding="3"
		align="left" id="accountTable" style="margin-left:20px;">
	
		

		<tr>
			<td width="30%"><s:text name="subject"/></td>
			<td width="2%">&nbsp;</td>
			<td width="68%"><s:textfield name="userRequest.subject"
				cssClass="textBox" id="user.firstName" value="Enable Advanced Console"/></td>
		</tr>
		<tr>
			<td><s:text name="notes" /></td>
			<td>&nbsp;</td>
			<td><s:textarea name="userRequest.notes" cssClass="textBox" rows="5" cols="30"
				id="user.lastName" /></td>
		</tr>

		<tr>
			<td valign="top"><s:text name="email" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="userRequest.emailId" cssClass="textBox" /></td>
		</tr>
		<tr>
			<td valign="top"><s:text name="phone" /></td>
			<td>&nbsp;</td>
			<td><s:textfield name="userRequest.contactPhoneNum" cssClass="textBox" /></td>
		</tr>
		


		<tr>
			
			<td align="left" colspan="3">
			<input class="singleButton" type="button" value="<s:text name='Submit'/>" onclick='createUpdateAdvancedOptions();' />
			
</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>
</s:form>