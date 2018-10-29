<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form id="reportAggregationConfigurationsForm">
	<table width="98%" border="0" align="center" cellpadding="3" cellspacing="0">
		<!-- Response Message Block -->
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2" id="successMessageRepAgg" style="display:none;"></td></tr>
		<tr><td colspan="2" id="failureMessageRepAgg" style="display:none;"></td></tr>
		<tr><td colspan="2" id="filler">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		
		<!-- Content Block -->
		<tr>
			<td width="25%"><s:property value="aggConfig.enableDynamicRanges.label" /></td>
			<td class="columnHeading">
				<s:select name="aggConfig.enableDynamicRanges.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="aggConfig.enableDynamicRanges.identity"/>
		</tr>
		<tr>
			<td><s:property value="aggConfig.dynamicRangeBandCount.label" /></td>
			<td class="columnHeading">
				<s:textfield name="aggConfig.dynamicRangeBandCount.value" cssClass="textBoxRanges" maxlength="2" tooltip="Can only be between 3 and 8" required="true" size="5"/>	
			</td>
			<s:hidden name="aggConfig.dynamicRangeBandCount.identity"/>
		</tr>
		
		<tr>
			<td width="25%"><s:property value="aggConfig.enableDetailReports.label" /></td>
			<td class="columnHeading">
				<s:select name="aggConfig.enableDetailReports.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="aggConfig.enableDetailReports.identity"/>
		</tr>
		<tr>
			<td><s:property value="aggConfig.detailReportSelectionThreshold.label" /></td>
			<td class="columnHeading">
				<s:textfield name="aggConfig.detailReportSelectionThreshold.value" cssClass="textBoxRanges" maxlength="4" tooltip="Number of records to go for Detail Reporting" required="true" size="5"/>	
			</td>
			<s:hidden name="aggConfig.detailReportSelectionThreshold.identity"/>
		</tr>
		
		<tr>
			<td width="25%"><s:property value="aggConfig.skipUniVariants.label" /></td>
			<td class="columnHeading">
				<s:select name="aggConfig.skipUniVariants.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="aggConfig.skipUniVariants.identity"/>
		</tr>
		
		<tr>
			<td><s:property value="aggConfig.dataBrowserMinRecords.label" /></td>
			<td class="columnHeading">
				<s:textfield name="aggConfig.dataBrowserMinRecords.value" cssClass="textBoxRanges" maxlength="4" tooltip="Min number of records for Detail Browser" required="true" size="5"/>	
			</td>
			<s:hidden name="aggConfig.dataBrowserMinRecords.identity"/>
		</tr>
		<tr>
			<td><s:property value="aggConfig.dataBrowserMaxRecords.label" /></td>
			<td class="columnHeading">
				<s:textfield name="aggConfig.dataBrowserMaxRecords.value" cssClass="textBoxRanges" maxlength="4" tooltip="Max number of records for Detail Browser" required="true" size="5"/>	
			</td>
			<s:hidden name="aggConfig.dataBrowserMaxRecords.identity"/>
		</tr>
		<tr>
			<td><s:property value="aggConfig.reportTitleLength.label" /></td>
			<td class="columnHeading">
				<s:textfield name="aggConfig.reportTitleLength.value" cssClass="textBoxRanges" maxlength="4" tooltip="Report title maximum length in one single line" required="true" size="5"/>	
			</td>
			<s:hidden name="aggConfig.reportTitleLength.identity"/>
		</tr>
		
		<!-- Action Block -->
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td  colspan="2" align="left">
				<input type="button" class="buttonSize130" value="Save Configuration" alt="" align="left" 
				onclick="saveReportAggregationfigurations();" id="saveReportAggregationConfigurationsId" style="display: inline-block;">
			</td>
		</tr>
	</table>
</s:form>