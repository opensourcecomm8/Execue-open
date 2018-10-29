<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.execue.core.common.type.CheckType"%>
<%@page import="com.execue.core.common.type.BehaviorType"%>
<html>
<style>
#dynamicPane select {
	width: 155px;
}
</style>
<body>
<table width="100%" align="center" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td colspan="4">
		<div id="behaviourDiv"
			style="width: 565px; height: auto; border: 1px solid #ccc; padding: 5px; margin-top: 10px;">

		<table border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td><strong><s:text name="execue.addPathDefinition.Behavior" /></strong></td>
				<td colspan="3">&nbsp;</td>
			</tr>

			<tr>
				<td colspan="4">
				<div id="behaviorTypesDiv">
				<table border="0" cellspacing="0" cellpadding="3">
					<tr>
						<s:if test="typeBehaviors !=null && typeBehaviors.size()>0">
							<s:iterator value="typeBehaviors" status="even_odd">
								<td><input name="typeBehaviors" type="checkbox"
									desc="<s:property value='description' />" value=""
									disabled="disabled" /><!--  <input
								name="typeBehaviors" type="hidden" value='<s:property/>' />--></td>
								<td><s:property value="description" /></td>
							</s:iterator>
						</s:if>
						<s:else>
							<span style="white-space: nowrap;"> <s:text
								name="execue.addPathDefinition.NoBehaviorexists" /></span>
						</s:else>

					</tr>
				</table>

				</div>
				<div id="selectedTypeBehaviorsDiv"><s:iterator
					value="selectedTypeBehaviors" status="even_odd">
					<input name="selectedTypeBehaviors" type="hidden"
						value='<s:property/>' />
				</s:iterator></div>

				<div id="selectedTypePathDefinitionsDiv"><s:iterator
					value="selectedTypePathDefinitions" status="even_odd">
					<input name="selectedTypePathDefinitions"
						pathDefinitionId="<s:property value='pathDefinitionId'/>"
						sourceConceptBedId="<s:property value='sourceConceptBedId'/>"
						sourceConceptName="<s:property value='sourceConceptName'/>"
						destinationConceptBedId="<s:property value='destinationConceptBedId'/>"
						destinationConceptName="<s:property value='destinationConceptName'/>"
						relationName="<s:property value='relationName'/>"
						attributeBedId="<s:property value='attributeBedId'/>"
						attributeName="<s:property value='attributeName'/>"
						inherent="<s:property value='inherent'/>"
						relationBedId="<s:property value='relationBedId'/>" type="hidden"
						value='<s:property/>' />
				</s:iterator></div>
				</td>
			</tr>
			<tr>
				<td colspan="4">
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td><strong><s:text
							name="execue.addPathDefinition.Attributes" /></strong></td>
						<td><span id="realizationHeading" style="display: none"><strong><s:text
							name="execue.addPathDefinition.Realization" /></strong></span></td>
						<td></td>
					</tr>
					<tr>
						<td valign="top" style="padding-top: 3px;">
						<div id="selectAttributesDiv"><s:if
							test="typeAttributes.size()>0">
							<select name="" id="selectAttributeId">
								<option>Select</option>
								<s:iterator value="typeAttributes" id="typeAttr">
									<option value="<s:property value='attributeTypeId'/>"
										attributeBedId="<s:property value='attributeBedId'/>"
										relationName="<s:property value='relationName'/>"
										isRequired="<s:property value='isRequired'/>"
										attributeName="<s:property value='attributeName'/>"
										entityType="<s:property value='entityType'/>"
										relationBedId="<s:property value='relationBedId'/>">
									<s:if test="isRequired.value=='Y'">
										<s:property value="attributeName" />
										<s:text name="execue.addPathDefinition.Required" />
									</s:if> <s:else>
										<s:property value="attributeName" />
									</s:else></option>
								</s:iterator>
							</select>
						</s:if> <s:else>
							<span style="white-space: nowrap;"> <s:text
								name="execue.addPathDefinition.NoAttributesexists" /></span>
						</s:else></div>
						</td>
						<td style="padding-top: 3px; padding-left: 3px;">
						<div id="realizedConceptsListDiv" />
						</td>
						<td valign="bottom" style="padding-left: 3px;"><SPAN
							id="addLink" style="display: none"><a href="#"
							onClick="javascript:addRow('');"><s:text
							name="execue.addPathDefinition.AddAssocationLink" /></a></SPAN></td>
					</tr>

					<tr id="detailTypeRow">
						<td><strong>Detail Type</strong></td>
					</tr>
					<tr>
						<td valign="top" style="padding-top: 3px;">
						<div id="detailTypesDiv"><select name="selectedDetailTypeId" id="selectedDetailTypeId" > 
							<s:iterator value="detailTypes">
								<option id="<s:property value='id'/>"
									value="<s:property value='id'/>"><s:property
									value='displayName' /></option>
							</s:iterator>
						</select></div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="left">
				<div id="relationDisplayDiv"
					style="height: 90px; overflow: auto; overflow-x: hidden; width: 560px;"></div>

				</td>
			</tr>
		</table>
		</div>
		<s:hidden id="detailTypeId" name="selectedDetailTypeId"></s:hidden>
		</td>
	</tr>
</table>
</body>
</html>

<script>
  $(document).ready(function() {
    var abstrat=$("#selectTypeId option:selected").attr("abstrat"); 
    var detailTypeId=$("#detailTypeId").val();
    if(detailTypeId){
       $("#selectedDetailTypeId option[value='"+detailTypeId+"']").attr("selected",true); 
    }     
	showDetailTypes(abstrat);    
	  $("#selectAttributeId").change(function(){
		 getRelization("");
	 });	 
  

  //createAutoSuggest();
    showSelectedTypePathDefinitions("");
    showSelectedTypeBehaviors("");
    checkAttributes();   
	$("#validationMessage").empty();
	$("#statusMessage").empty();
	 populateUnitFormat();  
	
});


 function createAutoSuggest(){ 
	  $("#relationText").autocomplete('<s:url action="suggestConcepts" includeParams="none"/>', {
			selectionCallBack : function conceptSuggestCallBack(selectedObject) {},
			maxItemsToShow:10
		});
  } 
  function resetMeaning(){
 /*  getPathDefinition(savedDefaultType); 
   $.each($("#selectTypeId option"),function(){
	  if($(this).val()==savedDefaultType){
		$(this).attr("selected","selected")
	  }
   }); */
    cId=$("#hiddenConceptId").val();
	 getConcept(cId)
  }
  
 function getSelectedDetailTypeId(){
    var selectedDetailTypeId=$("#selectedDetailTypeId option:selected").attr("id");		
   return selectedDetailTypeId;
  }
  
 function populateUnitFormat(){ 
   if($("#relationDisplayDiv input[name='hiddenAssociationData']").length >0){      
      $.each($("#relationDisplayDiv input[name='hiddenAssociationData']"),function(k,v){		
			var $atributeText=$(this).attr("attributetext");  
			if($atributeText=='Value'){
			   changeFormat($(this).attr("realizationconceptname"));  
			}
	 });
   }    
 }

 
</script>