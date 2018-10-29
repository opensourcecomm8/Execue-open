<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.execue.core.common.type.CheckType"%>
<%@page import="com.execue.core.common.type.BehaviorType"%>
<html>

<body>
<table width="100%" align="center" border="0" cellspacing="0"
	cellpadding="5" style="margin-left: 20px; ">
	<tr>
		<td>
		  <div id="validationMessageAdvanced" style="display: none"></div>
		  <div id="errorMessage" style="color: red; padding-top: 10px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
          <div id="actionMessage" style="color: green; padding-top: 10px; padding-left: 20px;"><s:actionmessage /></div>
		</td>
	</tr>
	<tr>
		<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="15%"><s:text name="execue.addAdvancedPathDefinition.SelectedType" /></td>
				<td width="85%">
				<div id="selectedTypeDiv"></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="4">
		<div id="behaviourDivAdvanced"
			style="width: 565px; height: auto; border: 1px solid #ccc; padding: 5px;">

		<table border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td><strong><s:text name="execue.addPathDefinition.Behavior" /></strong></td>
				<td colspan="3">&nbsp;</td>
			</tr>

			<tr>
				<td colspan="4">
				<div id="behaviorTypesDivAdvanced">
				<table  border="0" cellspacing="0" cellpadding="3">
					<tr>
					   <s:if test="extendedBehaviors.size()>0">
						<s:iterator value="extendedBehaviors" status="even_odd">
							<td><input name="extendedBehaviors" desc="<s:property value='description' />" type="checkbox" value="<s:property/>"
								 /> <!-- <input
								name="extendedBehaviors" type="hidden" value='<s:property/>' />--></td>
							<td><s:property value="description" /></td>
                            <s:if test="(#even_odd.index+1)%4==0">
                            <tr></tr>
                            </s:if>
						</s:iterator>
				    </s:if>
					<s:else>
					 <span style="white-space:nowrap;">  <s:text name="execue.addPathDefinition.NoBehaviorexists" /> </span>
					</s:else>
					</tr>
				</table>
                
                </div>
                
                 <div id="selectedTypeBehaviorsDivAdvanced">
                
               			 <s:iterator value="selectedExtendedBehaviors" status="even_odd">
							<input name="selectedTypeBehaviorsAdvanced" type="hidden" value='<s:property/>' />
						</s:iterator>
                </div>
                
                  <div id="selectedTypePathDefinitionsDivAdvanced">
                
                	<s:iterator value="selectedExtendedPathDefinitions" status="even_odd">
				<input name="selectedTypePathDefinitionsAdvanced" 
				            pathDefinitionId="<s:property value='pathDefinitionId'/>" 				
                            sourceConceptBedId="<s:property value='sourceConceptBedId'/>" 
                            sourceConceptName="<s:property value='sourceConceptName'/>"
                            destinationConceptBedId="<s:property value='destinationConceptBedId'/>"
                            destinationConceptName="<s:property value='destinationConceptName'/>"
                            relationName="<s:property value='relationName'/>"
                            attributeBedId="<s:property value='attributeBedId'/>" 
                            inherent="<s:property value='inherent'/>"                 
                             attributeName="<s:property value='attributeName'/>"                                 
                             type="hidden" value='<s:property/>' />
						</s:iterator>
                </div>
                
				</td>
				
			</tr>

			<tr>
				<td colspan="4"><strong>&nbsp;</strong></td>
				
			</tr>
            <tr>
				<td colspan="4">
            <table>
			<tr>
				<!--td><strong><s:text name="addPathDefinition.Attributes" /></strong></td-->
				<td><span id="relationHeadingAdvanced" style=""> <strong><s:text name="execue.addPathDefinition.Relation" /></strong>
				<!-- &nbsp;<a href="#" onClick="">Edit</a>--> </span></td>
				<td><span id="realizationHeadingAdvanced" style=""><strong><s:text name="execue.addPathDefinition.Attributes" /></strong></span></td>
				<td></td>
			</tr>
			<tr>

				<td valign="top"> <input id="relationTextAdvanced" style="font-size:11px;width:160px;"
					type="text" maxlength="255"/><a href="#" id="helpLinkRelation" title="Help" alt="Help">?</a></td>
				<td>
				<div id="realizedConceptsListDivAdvanced">
				<select id="realizationSelectedIdAdvanced" name="select3"  style="width:155px;" size="6" multiple="multiple">		
				   	<s:iterator value="extendedRealizations">
					  <option attributeBedId="<s:property value='attributeBedId'/>" value="<s:property value='realizationBedId'/>"><s:property value="realizationName"/></option>
					</s:iterator>
				</select>
				</div>
				</td>
				<td valign="bottom"><SPAN id="addLinkAdvanced" style=""><a
					href="#" onClick="javascript:addRow('Advanced');"><s:text name="execue.addPathDefinition.AddAssocationLink" /></a></SPAN></td>
			</tr>  </table>
            </td></tr>
			<tr>
				<td colspan="4" align="left">
				<div id="relationDisplayDivAdvanced" style="height:90px;overflow:auto;overflow-x:hidden;width:560px;"></div>

				</td>
			</tr>



		</table>


		</div>

		</td>
	</tr>
	<tr>
		<td align="left">

		<div style="padding-left: 2px;"><input id="submitButtonAdvanced" type="button" class="singleButton" value="Submit" onClick="javascript:saveAdvanceDefinition()" />
        
        <input id="submitButtonLoaderAdvanced" type="button" style="display:none" class="singleButtonLoader" disabled="disabled" value="Submit"  />
        
        
         <a href="#" id="resetButtonAdvanced" style="margin-left:20px;" onClick="javascript:resetAdvancedMeaning()"> Reset </a> 
        
           
         <img id="resetButtonLoaderAdvanced" style="display:none;margin-left:20px;" src="../images/admin/loadingBlue.gif" />
        </div>

		</td>
	</tr>

</table>
</body>
</html>
<script>
  $(document).ready(function() {
	$("#helpLinkRelation").helpMessage({messageKey:"execue.relation.help.name"}); 					 
	showSelectedTypePathDefinitions("Advanced");
	showSelectedTypeBehaviors("Advanced");
    setAdvancedTypeName(); 
	createAutoSuggest();
	$("#validationMessageAdvanced").empty();
});
 
function setAdvancedTypeName(){
 $("#selectedTypeDiv").text($("#selectTypeId option:selected").text());	
}
function checkForAdvanceRequiredFields(){
	var requiredFieldsExists=true;
	if($("#selectAttributeIdAdvanced option[isrequired='YES']").length>0){
			$.each($("#selectAttributeIdAdvanced option[isrequired='YES']"),function(){
				$attId=$(this).val();
				if($("#relationDisplayDivAdvanced input[value='"+$attId+"']").length==0){
					requiredFieldsExists=false;
				}
				
			 });
	}else{
		requiredFieldsExists=false;
	}
	return requiredFieldsExists;
}

function saveAdvanceDefinition(){ 
       /**  Data to be saved**/
	var requiredFieldsExists=checkForAdvanceRequiredFields();
	 var conceptId=$("#selectedConceptId").val();
	 var newType=$("#selectTypeId option:selected").attr("id");
		$request=""
		$request+="selectedTypeId="+$("#selectTypeId option:selected").attr("id");
		$request+="&selectedConceptId="+conceptId;		
		$.each($("#relationDisplayDivAdvanced input[name='hiddenAssociationDataAdvanced']"),function(k,v){		
			$request+="&selectedExtendedPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("conceptBedId");
			$request+="&selectedExtendedPathDefinitions["+k+"].relationName="+$(this).attr("relationText");				
			$request+="&selectedExtendedPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("realizationConceptBedId");
			$request+="&selectedExtendedPathDefinitions["+k+"].attributeBedId="+$(this).val();
		});
		    if($("#selectAttributeIdAdvanced option[isrequired='YES']").length>0){
			if(!requiredFieldsExists){
				var message="Association with required attributes must exist.";
				showValidationMessage(message,'Advanced');
				return false;
			   }
		    }
		    /**  Original Path definition**/
			$.each($("#selectedTypePathDefinitionsDivAdvanced input[name='selectedTypePathDefinitionsAdvanced']"),function(k,v){		
				$request+="&savedExtendedPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("sourceConceptBedId");
				$request+="&savedExtendedPathDefinitions["+k+"].relationName="+$(this).attr("relationName");
				$request+="&savedExtendedPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("destinationConceptBedId");													
				$request+="&savedExtendedPathDefinitions["+k+"].attributeBedId="+$(this).attr("attributeBedId");
				$request+="&savedExtendedPathDefinitions["+k+"].pathDefinitionId="+$(this).attr("pathDefinitionId");		
				
			});
		    /** Extended Behaviors to be saved **/  
			$.each($("input[name='extendedBehaviors']:checked"),function(k,v){
			  $request+="&selectedExtendedBehaviors["+k+"]="+$(this).val();
			});
			 /** Original Extended Behaviors **/
			$.each($("input[name='selectedTypeBehaviorsAdvanced']"),function(k,v){
			  $request+="&savedExtendedBehaviors["+k+"]="+$(this).val();
			});
		
			$("#submitButtonAdvanced").hide();
			$("#submitButtonLoaderAdvanced").show();
			//alert($request);

	   $.ajax({url:"swi/saveAdvanceConceptTypeAssociation.action",
			    data:$request,			    
				success:function(data){showData(data);},
			    type:"post"
			   });
		function showData(data){
		        $("#validationMessageAdvanced").empty(); advancedPathData=data;
		        $("#dynamicPaneAdvancedPathDefinition").empty().append(data);			  
			    $("#submitButtonAdvanced").show();
			    $("#submitButtonLoaderAdvanced").hide();
			}
	
}
function resetAdvancedMeaning(){ 
$("#dynamicPaneAdvancedPathDefinition").removeClass('dynamicPaneBgNoLoader').addClass('dynamicPaneBgLoader'); 
 $.get("showAdvancedPathDefinition.action?selectedTypeId="+selectTypeIdDefaultsVal+"&selectedConceptId="+conceptId,function(data){
			$("#dynamicPaneAdvancedPathDefinition").removeClass('dynamicPaneBgLoader').addClass('dynamicPaneBgNoLoader'); 
   $("#dynamicPaneAdvancedPathDefinition").empty().append(data);
   });
}
function resetAdvancedConceptDetails(){
  // $("#dynamicPaneAdvancedPathDefinition").empty().append(advancedPathData);
  var defaultDataFormat=$("#dDataFormatDefault").val();
  var defaultUnit=$("#dUnitDefault").val();
  
  $("#dDataFormat").val(defaultDataFormat);
  $("#dUnit").val(defaultUnit);
  $("#dataFormats option[value='"+defaultDataFormat+"']").attr("selected",true);
  $("#units option[value='"+defaultUnit+"']").attr("selected",true);
}

 function createAutoSuggest(){ 
	  $("#relationTextAdvanced").autocomplete('<s:url action="suggestRelations" includeParams="none"/>', {
			selectionCallBack : function conceptSuggestCallBack(selectedObject) {}
		});
  } 
</script>