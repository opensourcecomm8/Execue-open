<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:form id="createCube" action="createCube" onsubmit="return validateData(true)">
	<table width="80%" border="0" align="center" cellpadding="2"
		cellspacing="0">
		<tr>
			<td colspan="3">
			<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
			<div id="actionMessage" style="color: red"><s:actionmessage /></div>
			</td>
		</tr>
		<tr>
			<td width="32%" class="fieldNames">Name<span class='fontRed'>*</span></td>
			<td width="68%"><s:textfield name="targetAsset.name" id="targetAssetNameId"
				cssClass="textBox" /></td>
		</tr>
		<tr>
			<td class="fieldNames">Display name<span class='fontRed'>*</span></td>
			<td><s:textfield name="targetAsset.displayName" id="targetAssetDispNameId"
				cssClass="textBox" /></td>
		</tr>
		<tr>
			<td class="fieldNames">Description<span class='fontRed'>*</span></td>
			<td><s:textfield name="targetAsset.description" id="targetAssetDesId"
				cssClass="textBox" /></td>
		</tr>		
		<tr>
			<td></td>
			<td><input type="image" src="../images/createCubeButton.gif" value="Create cube" /></td>
		</tr>
	</table>
</s:form>
<script type="text/javascript">
$("#createCube_targetAsset_name").focus();
</script>

