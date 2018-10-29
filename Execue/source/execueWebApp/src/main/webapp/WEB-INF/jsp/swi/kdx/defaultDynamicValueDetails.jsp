<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" align="center" border="0" cellspacing="0" cellpadding="5" style="margin-left: 20px;">
	<tr>
		<td>
		<div id="validationMessage" style="display: none"></div>
		<div id="errorMessage" style="color: red; padding-top: 10px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
		<div id="actionMessage" style="color: green; padding-top: 10px; padding-left: 20px;"><s:actionmessage /></div>
		</td>
	</tr>
	<tr>
		<td colspan="4">
		<div id="behaviourDiv" style="width: 565px; height: auto; border: 1px solid #ccc; padding: 5px;">
		<table border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td colspan="4"><s:text name="execue.ddv.header" /></td>
			</tr>
			<tr>
				<td colspan="4">
				<form id="defaultValueForm">
				<table>
					<s:iterator value="uiDefaultDynamicValues" status="stat">
						<tr>
							<td><s:property value="qualifier" /> :</td>
							<td><input class="defaultDynamicValues" type="text" value="<s:property value='defaultValue'/>"
								name="uiDefaultDynamicValues[<s:property value='%{#stat.index}'/>].defaultValue" maxlength="255"/>
								<s:if test="#stat.index==0">
								<a href="#" id="helpLink" title="Help" alt="Help">?</a>
								</s:if> </td>
								
							<input type="hidden" name="uiDefaultDynamicValues[<s:property value='%{#stat.index}'/>].id"
								value="<s:property value='id'/>" />
							<input type="hidden" name="uiDefaultDynamicValues[<s:property value='%{#stat.index}'/>].assetId"
								value="<s:property value='assetId'/>" />
							<input type="hidden" name="uiDefaultDynamicValues[<s:property value='%{#stat.index}'/>].lhsDEDId"
								value="<s:property value='lhsDEDId'/>" />
							<input type="hidden" name="uiDefaultDynamicValues[<s:property value='%{#stat.index}'/>].qualifier"
								value="<s:property value='qualifier'/>" />
						</tr>
					</s:iterator>
				</table>
				</form>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td align="left">
		<div style="padding-left: 2px;"><input id="submitButton" type="button" class="singleButton" value="Update"
			onClick="javascript:saveDDV()" /> <input id="submitButtonLoader" type="button" style="display: none"
			class="singleButtonLoader" disabled="disabled" value="Submit" /></div>
		</td>
	</tr>
</table>
<script>
 $(document).ready(function() {
$("#helpLink").helpMessage({messageKey:"execue.ddv.help.name"}); 
	setTimeout('clearMsgs()', 3000);
});
</script>