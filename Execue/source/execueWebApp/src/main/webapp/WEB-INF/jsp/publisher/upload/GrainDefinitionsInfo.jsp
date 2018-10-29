<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="innerPane" style="width: 99%"><s:form
	id="grainDefinitionForm" method="POST">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="51%">

			<table width="10%" border="0" align="center" cellpadding="3"
				cellspacing="0">
				<tr>
					<td width="50%" class="fieldNames">&nbsp;</td>
					<td width="50%" height="40">&nbsp;</td>
				</tr>
				<tr>
					<td class="fieldNames">Population</td>
					<td class="fieldNames">Distribution</td>
				</tr>
				<tr>
					<td><label> <s:select name="selectedPopulations"
						list="populations" listKey="id" listValue="evaluatedColumnName"
						id="uiPopulations" multiple="true"
						value="%{existingPopulations.{id}}" size="10"></s:select> </label></td>
					<td><s:select name="selectedDistributions"
						id="uiDistributions" list="distributions" listKey="id"
						listValue="evaluatedColumnName" multiple="true"
						value="%{existingDistributions.{id}}" size="10"></s:select></td>
				</tr>
				<tr>
					<td align="right"><img src="../images/absorbDatasetButton.jpg"
						alt="Update Grain Definitions"
						onclick="javascript:updateGrainDef();" /></td>
					<td height="40"><img src="../images/cancelRequestButton.jpg"
						width="118" height="26" /></td>
				</tr>
				<tr>
					<td height="35" colspan="2" class="fieldNames">&nbsp;</td>
				</tr>
			</table>

			</td>
		</tr>
	</table>
	
</s:form></div>