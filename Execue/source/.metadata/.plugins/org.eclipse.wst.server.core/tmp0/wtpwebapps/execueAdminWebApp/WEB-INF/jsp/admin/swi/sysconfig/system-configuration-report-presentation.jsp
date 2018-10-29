<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form id="reportPresentationConfigurationsForm">
	<table width="98%" border="0" align="center" cellpadding="3" cellspacing="0">
		
		<!-- Response Message Block -->
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2" id="successMessageRepPre" style="display:none;"></td></tr>
		<tr><td colspan="2" id="failureMessageRepPre" style="display:none;"></td></tr>
		<tr><td colspan="2" id="filler">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		
		<!-- Content Block -->
		<tr>
			<td width="20%"><s:property value="repConfig.axisLabelMaxLength.label" /></td>
			<td class="columnHeading">
				<s:textfield name="repConfig.axisLabelMaxLength.value" cssClass="textBoxRanges" maxlength="2" tooltip="Can only be between 10 and 75" required="true"/>	
			</td>
			<s:hidden name="repConfig.axisLabelMaxLength.identity"/>
		</tr>
		
		<!-- Action Block -->
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td  colspan="2" align="left">
				<input type="button" class="buttonSize130" value="Save Configuration" alt="" align="left" 
				onclick="saveReportPresentationfigurations();" id="saveReportPresentationConfigurationsId" style="display: inline-block;">
			</td>
		</tr>
	</table>
</s:form>