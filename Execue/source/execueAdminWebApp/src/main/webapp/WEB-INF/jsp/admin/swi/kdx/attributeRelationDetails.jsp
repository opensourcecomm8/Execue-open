<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
<table width="100%" align="center" border="0" cellspacing="0"
	cellpadding="5" style="margin-left: 20px;">
	<tr>
		<td>
		<div id="validationMessage" style="display: none"></div>
		<div id="errorMessage"
			style="color: red; padding-top: 10px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
		<div id="actionMessage"
			style="color: green; padding-top: 10px; padding-left: 20px;"><s:actionmessage /></div>
		</td>
	</tr>

	<tr>
		<td colspan="4">
		<div id="behaviourDiv"
			style="width: 565px; height: auto; border: 1px solid #ccc; padding: 5px;">

		<table border="0" cellspacing="0" cellpadding="3">

			<tr>
				<td colspan="4"><s:text
							name="execue.relation.selectedConcept" /><strong> <s:property value="conceptName" /></strong></td>

			</tr>
			<tr>
				<td colspan="4">
				<table>
					<tr>
						<td><span id="relationHeading" style=""> <strong><s:text
							name="execue.addPathDefinition.Relation" /></strong>
						</span></td>
						<td><span id="realizationHeading" style=""><strong><s:text
							name="execue.addPathDefinition.concepts" /></strong></span></td>
						<td></td>
					</tr>
					<tr>

						<td valign="top"><input id="relationType"
							style="font-size: 11px; width: 160px;" type="text"
							value='<s:property value="%{relation.displayName}"/>' relationBedId='<s:property value="%{relation.bedId}"/>' disabled /></td>
						<td>
						<div id="realizedConceptsListDiv"><select
							id="realizationSelectedId" name="select3" style="width: 155px;"
							size="6" multiple="multiple">
							<s:iterator value="timeFrameTypeconcepts">
								<option attributeBedId="<s:property value='bedId'/>"
									value="<s:property value='id'/>" attributeName="<s:property value='name'/>"><s:property
									value="displayName" /></option>
							</s:iterator>
						</select></div>
						</td>
						<td valign="bottom"><SPAN id="addLink" style=""><a
							href="#" onClick="javascript:addRow();"><s:text
							name="execue.addPathDefinition.AddAssocationLink" /></a></SPAN></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="left">
				<div id="existingPathDefDiv"
					style="height: 140px; overflow: auto; overflow-x: hidden; width: 560px;">
				<s:iterator value="existingPathDefinitions">
					<s:if test="!(reverseAssociation)">
						<img height="2" width="500" src="../images/admin/space.jpg">
						<div style="padding-left: 2px; width: 100%;">
							<div style="float: left; padding: 3px;"><s:property
								value="sourceConceptName" /></div>
							<div style="float: left; padding: 3px;"><s:property
								value="relationName" /></div>
							<div style="float: left; padding: 3px;"><s:property
								value="destinationConceptName" /></div>
							<div id="deleteDiv1" style="float: left; padding: 3px;"><a
								href="#"><img title="Delete" alt="Delete"
								src="../images/admin/disabledIcon.gif" border="0" height="16"></a></div>
							<input type="hidden" name="hiddenAssociationData" 
							conceptname='<s:property value="sourceConceptName" />'
							conceptbedid='<s:property value="sourceConceptBedId" />' 
							relationtext='<s:property value="relationName" />' 
							relationbedid='<s:property value="relationBedId" />' 
							attributename='<s:property value="destinationConceptName" />' 
							attributebedid='<s:property value="destinationConceptBedId" />'/>
						</div>
					</s:if>
				</s:iterator></div>
				</td>
			</tr>
			<tr><td>
				<div id="forDeletePathDefDiv" style="display:none;">
				<s:iterator value="existingPathDefinitions">
					<input type="hidden" name="hiddenAssociationDataForDelete"
					pathDefId='<s:property value="pathDefinitionId" />'
					entityTripleDefId='<s:property value="entityTripleDefinitionId" />'
					conceptname='<s:property value="sourceConceptName" />'
					conceptbedid='<s:property value="sourceConceptBedId" />' 
					relationtext='<s:property value="relationName" />' 
					relationbedid='<s:property value="relationBedId" />' 
					attributename='<s:property value="destinationConceptName" />' 
					attributebedid='<s:property value="destinationConceptBedId" />'/>
				</s:iterator>
				</div>
			</td></tr>


		</table>


		</div>

		</td>
	</tr>
	<tr>
		<td align="left">
		<div style="padding-left: 2px;"><input id="submitButton"
			type="button" class="singleButton" value="Submit"
			onClick="javascript:saveAdvanceDefinition()" /> <input
			id="submitButtonLoader" type="button" style="display: none"
			class="singleButtonLoader" disabled="disabled" value="Submit" /></div>
		</td>
	</tr>

</table>
<input type="hidden" id="conceptBedId"
	value='<s:property value="conceptBedId"/>' />
<input type="hidden" id="relationBedId"
	value='<s:property value="relationBedId"/>' />
</body>
</html>
<script>
  $(document).ready(function() {
	$("#deleteDiv1 a").click(function(){
		$(this).parent('div').parent('div').next('img').remove();
		$(this).parent('div').parent('div').remove();		
	});
	$("#validationMessage").empty();
});
</script>