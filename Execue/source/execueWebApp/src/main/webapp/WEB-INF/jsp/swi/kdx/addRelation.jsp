<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#actionMessage ul{
margin:0px;
padding:0px;

}
</style>
<div id="errorMessage"
	style="color: red; padding-top: 5px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage"
	style="color: green; padding-top: 5px; padding-left: 20px;"><s:actionmessage /></div>
<div id="statusMessage" style="display: none; padding-left: 20px;"></div>
<div id="validationMessageRelation"
	style="display: none; padding-left: 20px;"></div>
<table cellspacing="0" cellpadding="0" border="0" align="left"
	width="100%" style="margin-left: 20px; margin-top: 5px;">
	<tbody>
		<tr>
			<td height="25" width="60%" class="fieldNames"><strong>Update/View
			Relation</strong></td>
			<td align="center" width="40%" class="fieldNames">
			<div id="showInstancesLink"></div>
			<div style="display: none;" id="loadingShowInstanceLink"><img
				height="20" width="20" src="../images/loading.gif"></div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
			<table cellspacing="0" cellpadding="2" border="0" align="center"
				width="100%">
				<tbody>
					<tr>
						<td colspan="2">
						<div align="left" style="color: red;" id="message"></div>
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<form id="conceptForm">
						<table cellspacing="0" border="0" width="100%">
							<tbody>
								<tr>
									<td width="15%" class="fieldNames">Selected Concept : </td>
									<td width="85%"><s:property value="concept.displayName" /></td>
								</tr>
							</tbody>
						</table>
						</form>
						</td>
					</tr>
					<tr>
						<td>
						<table cellspacing="0" border="0">
							<tbody>
								<tr>
									<td>Add Relation</td>
									<td>
									<div id="selectTypeDiv"><label> <input
										name="relationText" type="text" id="relationText"
										value="has Asset" /> </label></div>
									<input type="hidden" id="hiddenSelectedType"
										name="hiddenSelectedType"></td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td valign="top" style="padding-top: 5px;padding-right:10px;">Select Concepts </td>
									<td style="padding-top: 5px;"><select multiple="multiple"
										size="6" style="width: 205px;" name="select3"
										id="conceptsForRelation">
										<s:iterator value="eligibleRelationConcepts">
											<option value='<s:property value="bedId"/>'><s:property
												value="displayName" /></option>
										</s:iterator>

									</select> 
									 <div id="selectedPathDefinitionsRelationDiv">
									<s:iterator value="selectedRelationPathDefinitions">
										<input type="hidden" name="selectedPathDefinitionsRelation"
											sourceConceptBedId='<s:property value="sourceConceptBedId"/>'
											sourceConceptName='<s:property value="sourceConceptName"/>'
											destinationConceptBedId='<s:property value="destinationConceptBedId"/>'
											destinationConceptName='<s:property value="destinationConceptName"/>'
											relationBedId='<s:property value="relationBedId"/>'
											relationName='<s:property value="relationName"/>'
                                            source='<s:property value="source"/>'
											entityTripleDefinitionId='<s:property value="entityTripleDefinitionId"/>' />
									</s:iterator>
									</div></td>
									<td valign="bottom" style="padding-left: 5px;"><a
										href="javascript:addRow('Relation');">Add</a></td>
								</tr>
							</tbody>
						</table>
						</td>
					</tr>

					<tr>
						<td colspan="2">
						<style>
#dynamicPane select {
	width: 155px;
}
</style>

						<table cellspacing="0" cellpadding="0" border="0" align="center"
							width="100%">
							<tbody>
								<tr>
									<td colspan="4">
									<div
										style="width: 565px; height: auto; border: 1px solid rgb(204, 204, 204); padding: 5px; margin-top: 10px;"
										id="behaviourDiv">

									<table cellspacing="0" cellpadding="3" border="0">
										<tbody>

											<tr>
												<td colspan="4">
												<table cellspacing="0" cellpadding="0" border="0">
													<tbody>
														<tr>
															<td><strong>Relations</strong></td>
															<td><span style="display: none;"
																id="realizationHeading"><strong>Realization</strong></span></td>
															<td></td>
														</tr>
													</tbody>
												</table>
												</td>
											</tr>
											<tr>
												<td align="left" colspan="4">
												<div
													style="height: 150px; overflow-y: auto; overflow-x: hidden; width: 560px;"
													id="assoiationDisplayDivRelation"></div>

												</td>
											</tr>



										</tbody>
									</table>


									</div>

									</td>
								</tr>
							</tbody>
						</table>
						</div>
						</td>
					</tr>
					<tr>
						<td align="left" colspan="2">
						<div style="padding-left: 2px;"><input type="button"
							onclick="javascript:return saveRelation()" value="Submit"
							class="singleButton" id="submitButton"> <input
							type="button" value="Submit" disabled="disabled"
							class="singleButtonLoader" style="display: none;"
							id="submitButtonLoader"> <a
							onclick="javascript:resetRelation()" id="resetButton"
							style="margin-left: 20px;" href="#"> Reset </a> <img
							src="../images/loadingBlue.gif"
							style="display: none; margin-left: 20px;" id="resetButtonLoader"></div>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>

	</tbody>
</table>
<script>
	$(document).ready(function(){
	   initRelation();
	   createAutoSuggest();
	}); 
function createAutoSuggest(){ 
	  $("#relationText").autocomplete('<s:url action="suggestRelations" includeParams="none"/>', {
			selectionCallBack : function conceptSuggestCallBack(selectedObject) {}
		});
  } 
  
  function resetRelation(){
  $("#assoiationDisplayDivRelation").empty();
  initRelation();
  }
  function initRelation(){
	 $("#relationText").val("");
     $.each($("#conceptsForRelation option"),function(){
									$(this).attr("selected",false)	;			  
		 });
	 $("#conceptsForRelation option").eq(0).attr("selected",true);
     showExistingPathDefinitions('Relation');  
  }
</script>