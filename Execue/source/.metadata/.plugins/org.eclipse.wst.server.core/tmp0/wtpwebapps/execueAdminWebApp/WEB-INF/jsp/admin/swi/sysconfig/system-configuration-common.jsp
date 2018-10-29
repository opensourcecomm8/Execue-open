<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form id="commonConfigurationsForm">
	<table width="98%" border="0" align="center" cellpadding="3" cellspacing="0">
		
		<!-- Response Message Block -->
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2" id="successMessageCommon" style="display:none;"></td></tr>
		<tr><td colspan="2" id="failureMessageCommon" style="display:none;"></td></tr>
		<tr><td colspan="2" id="filler">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		
		<!-- Content Block -->
		<tr>
			<td width="20%"><s:property value="commonConfig.displayQueryString.label" /></td>
			<td class="columnHeading">
				<s:select name="commonConfig.displayQueryString.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="commonConfig.displayQueryString.identity"/>
		</tr>
		<tr>
			<td><s:property value="commonConfig.systemLevelDefaultStat.label" /></td>
			<td class="columnHeading">
				<s:select name="commonConfig.systemLevelDefaultStat.value" list="#{'SUM':'Summation', 'AVG':'Average'}"/>
			</td>
			<s:hidden name="commonConfig.systemLevelDefaultStat.identity"/>
		</tr>
		
		<!-- Action Block -->
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td  colspan="2" align="left">
				<input type="button" class="buttonSize130" value="Save Configuration" alt="" align="left" 
				onclick="saveCommonConfigurations();" id="saveCommonConfigurationsId" style="display: inline-block;">
			</td>
		</tr>
	</table>
</s:form>