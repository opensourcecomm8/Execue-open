<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<s:form id="answersCatalogConfigurationsForm">
	<table width="98%" border="0" align="center" cellpadding="3" cellspacing="0">
		<!-- Response Message Block -->
		<tr><td colspan="4">&nbsp;</td></tr>
		<tr><td colspan="4" id="successMessageAnsCat" style="display:none;"></td></tr>
		<tr><td colspan="4" id="failureMessageAnsCat" style="display:none;"></td></tr>
		<tr><td colspan="4" id="filler">&nbsp;</td></tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		
		<!-- Content Block -->
		<tr>
			<td width="33%"><s:property value="acConfig.samplingAlgoErroRate.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.samplingAlgoErroRate.value" cssClass="textBoxRanges" maxlength="5" tooltip="% with maximum of 2 decimal scale" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.samplingAlgoErroRate.identity"/>
			
			<td width="33%"><s:property value="acConfig.samplingAlgoConfidenceLevel.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.samplingAlgoConfidenceLevel.value" cssClass="textBoxRanges" maxlength="5" tooltip="% with maximum of 2 decimal scale" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.samplingAlgoConfidenceLevel.identity"/>
		</tr>
		<tr>
			<td width="33%"><s:property value="acConfig.samplingAlgoMinPopulation.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.samplingAlgoMinPopulation.value" cssClass="textBoxRanges" maxlength="5" tooltip="% with maximum of 2 decimal scale" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.samplingAlgoMinPopulation.identity"/>
			
			<td width="33%"><s:property value="acConfig.samplingAlgoMaxPopulation.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.samplingAlgoMaxPopulation.value" cssClass="textBoxRanges" maxlength="5" tooltip="% with maximum of 2 decimal scale" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.samplingAlgoMaxPopulation.identity"/>
		</tr>
		<tr>
			<td width="33%"><s:property value="acConfig.martUseBasicAlgo.label" /></td>
			<td class="columnHeading">
				<s:select name="acConfig.martUseBasicAlgo.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="acConfig.martUseBasicAlgo.identity"/>
			
			<td width="33%" colspan="2">&nbsp;</td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		<tr>
			<td width="33%"><s:property value="acConfig.martMaxDimensions.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.martMaxDimensions.value" cssClass="textBoxRanges" maxlength="2" tooltip="" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.martMaxDimensions.identity"/>
			
			<td width="33%"><s:property value="acConfig.martMaxMeasures.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.martMaxMeasures.value" cssClass="textBoxRanges" maxlength="5" tooltip="% with maximum of 2 decimal scale" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.martMaxMeasures.identity"/>
		</tr>
		<tr>
			<td width="33%"><s:property value="acConfig.martSourceSameAsTarget.label" /></td>
			<td class="columnHeading">
				<s:select name="acConfig.martSourceSameAsTarget.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="acConfig.martSourceSameAsTarget.identity"/>
			
			<td width="33%"><s:property value="acConfig.cubeSourceSameAsTarget.label" /></td>
			<td class="columnHeading">
				<s:select name="acConfig.cubeSourceSameAsTarget.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="acConfig.cubeSourceSameAsTarget.identity"/>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
		<tr>
			<td width="33%"><s:property value="acConfig.optCubeMinUsage.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.optCubeMinUsage.value" cssClass="textBoxRanges" maxlength="5" tooltip="% with maximum of 2 decimal scale" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.optCubeMinUsage.identity"/>
			
			<td width="33%"><s:property value="acConfig.optCubeMaxSpace.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.optCubeMaxSpace.value" cssClass="textBoxRanges" maxlength="5" tooltip="% with maximum of 2 decimal scale" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.optCubeMaxSpace.identity"/>
		</tr>
		<tr>
			<td width="33%"><s:property value="acConfig.optApplyConstraints.label" /></td>
			<td class="columnHeading">
				<s:select name="acConfig.optApplyConstraints.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="acConfig.optApplyConstraints.identity"/>
			
			<td width="33%"><s:property value="acConfig.optSpaceAtRuntime.label" /></td>
			<td class="columnHeading">
				<s:select name="acConfig.optSpaceAtRuntime.value" list="#{'false':'No', 'true':'Yes'}"/>	
			</td>
			<s:hidden name="acConfig.optSpaceAtRuntime.identity"/>
		</tr>
		<tr>
			<td width="33%"><s:property value="acConfig.optParentAssetSpace.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.optParentAssetSpace.value" cssClass="textBoxRanges" maxlength="10" tooltip="Space in MB" required="true" size="8"/>	
			</td>
			<s:hidden name="acConfig.optParentAssetSpace.identity"/>
			
			<td width="33%"><s:property value="acConfig.optLookupValueColumnLengthAtParentAsset.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.optLookupValueColumnLengthAtParentAsset.value" cssClass="textBoxRanges" maxlength="5" tooltip="Length in Characters" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.optLookupValueColumnLengthAtParentAsset.identity"/>
		</tr>
		<tr>
			<td width="33%"><s:property value="acConfig.optNumberOfParentAssetMeasures.label" /></td>
			<td class="columnHeading">
				<s:textfield name="acConfig.optNumberOfParentAssetMeasures.value" cssClass="textBoxRanges" maxlength="5" tooltip="Number of measures in parent asset definition" required="true" size="5"/>	
			</td>
			<s:hidden name="acConfig.optNumberOfParentAssetMeasures.identity"/>
			
			<td width="33%" colspan="2">&nbsp;</td>
		</tr>
		<!-- Action Block -->
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<tr>
			<td  colspan="4" align="left">
				<input type="button" class="buttonSize130" value="Save Configuration" alt="" align="left" 
				onclick="saveAnswersCatalogfigurations();" id="saveAnswersCatalogConfigurationsId" style="display: inline-block;">
			</td>
		</tr>
	</table>
</s:form>