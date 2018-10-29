<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="innerPane2" style="width: 430px; height: auto">
<div id="errorMessage" style="color: red; padding-top: 10px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green; padding-top: 10px;"><s:actionmessage /></div>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0" style="margin-left: 20px; margin-top: 15px;">

	<tr>
		<td colspan="2"><s:form id="advanceConceptDetails">
			<table width="100%" border="0" align="center" cellpadding="2"
				cellspacing="0">
				<tr>
					<td colspan="2">
					<div id="message" STYLE="color: red" align="left"></div>
					</td>
				</tr>
	
				<tr>
					<td width="21%" class="fieldNames"><s:text
						name="execue.concept.defaultDataFormat" /></td>
					<td width="27%"><select Class="desc" id="dataFormats"
						 onchange="getFormatValue('Frmt',this.value);">
						<option value='NULL'></option>
					</select></td>
					<s:hidden name="concept.defaultDataFormat" cssClass="textBox"
						id="dDataFormat" />
                        <input type="hidden" value="<s:property value='concept.defaultDataFormat' />" id="dDataFormatDefault" />
				</tr>
				<tr>
					<td width="21%" class="fieldNames"><s:text
						name="execue.concept.defaultUnit" /></td>
					<td width="27%"><select Class="desc" id="units" 
						onchange="getFormatValue('unit',this.value);">
						<option value='NULL'></option>
					</select></td>
					<s:hidden name="concept.defaultUnit" cssClass="textBox" id="dUnit" />
                    <input type="hidden" value="<s:property value='concept.defaultUnit' />" id="dUnitDefault" />
					<s:hidden name="concept.id"/>
				</tr>
				<tr>
					<td align="left">
					<div style="padding-left: 2px;"><input
						id="submitButtonAdvanced" type="button" class="singleButton"
						value="Submit" onClick="javascript:saveAdvanceConceptDetails();" /> <input
						id="submitButtonLoaderAdvanced" type="button"
						style="display: none" class="singleButtonLoader"
						disabled="disabled" value="Submit" /> <a href="#"
						id="resetButtonAdvanced" style="margin-left: 20px;"
						onClick="javascript:resetAdvancedConceptDetails()"> Reset </a> <img
						id="resetButtonLoaderAdvanced"
						style="display: none; margin-left: 20px;"
						src="../images/loadingBlue.gif" /></div>
					</td>
				</tr>
			</table>
		</s:form></td>
	</tr>
</table>
</div>
<script>

  $(document).ready(function() {
	 populateUnitFormat(); 
});

</script>

