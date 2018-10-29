<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table width="100%" align="center" border="0" cellspacing="0"
	cellpadding="5" style="margin-left: 20px; ">
	<tr>
		<td>
		  <div id="validationMessageCRC" style="display: none"></div>
		  <div id="errorMessage" style="color: red; padding-top: 10px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
          <div id="actionMessage" style="color: green; padding-top: 10px; padding-left: 20px;"><s:actionmessage /></div>
		</td>
	</tr>
	
	<tr>
		<td colspan="4">
		<div id="behaviourDivCRC"
			style="width: 565px; height: auto; border: 1px solid #ccc; padding: 5px;">

		<table border="0" cellspacing="0" cellpadding="3">
			

			<tr>
				<td colspan="4">
                  <div id="selectedTypePathDefinitionsDivCRC">                
                	<s:iterator value="selectedCRCPathDefinitions" status="even_odd">
				       <input name="selectedTypePathDefinitionsCRC" 
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
				
				<td><span id="relationHeadingCRC" style=""> <strong><s:text name="execue.addPathDefinition.Relation" /></strong>
				 </span></td>
				<td><span id="realizationHeadingCRC" style=""><strong><s:text name="execue.addPathDefinition.concepts" /></strong></span></td>
				<td></td>
			</tr>
			<tr>

				<td valign="top"> <input id="relationTextCRC" style="font-size:11px;width:160px;"
					type="text" maxlength="255"/><a href="#" id="helpLinkRelation" title="Help" alt="Help">?</a></td>
				<td>
				<div id="realizedConceptsListDivCRC">
				<select id="realizationSelectedIdCRC" name="select3"  style="width:155px;" size="6" multiple="multiple">		
				   	<s:iterator value="nonAttributeConcepts">
					  <option attributeBedId="<s:property value='attributeBedId'/>" value="<s:property value='realizationBedId'/>"><s:property value="realizationName"/></option>
					</s:iterator>
				</select>
				</div>
				</td>
				<td valign="bottom"><SPAN id="addLinkCRC" style=""><a
					href="#" onClick="javascript:addRow('CRC');"><s:text name="execue.addPathDefinition.AddAssocationLink" /></a></SPAN></td>
			</tr>  </table>
            </td></tr>
			<tr>
				<td colspan="4" align="left">
				<div id="relationDisplayDivCRC" style="height:90px;overflow:auto;overflow-x:hidden;width:560px;"></div>

				</td>
			</tr>



		</table>


		</div>

		</td>
	</tr>
	<tr>
		<td align="left">

		<div style="padding-left: 2px;"><input id="submitButtonCRC" type="button" class="singleButton" value="Submit" onClick="javascript:saveCRCDefinition()" />
        
        <input id="submitButtonLoaderCRC" type="button" style="display:none" class="singleButtonLoader" disabled="disabled" value="Submit"  />
        
        
         <a href="#" id="resetButtonCRC" style="margin-left:20px;" onClick="javascript:resetCRCMeaning()"> Reset </a> 
        
           
         <img id="resetButtonLoaderCRC" style="display:none;margin-left:20px;" src="../images/admin/loadingBlue.gif" />
        </div>

		</td>
	</tr>

</table>
<ul style="display:none" id="relationsCRC">
<s:iterator value="baseRelations" >
<li><s:property /></li>
</s:iterator>
</ul>

<script>
  $(document).ready(function() {
	$("#helpLinkRelation").helpMessage({messageKey:"execue.relation.help.name"}); 					 
	showSelectedTypePathDefinitions("CRC");
	createAutoSuggest();
	$("#validationMessageCRC").empty();
});

function checkForCRCRequiredFields(){
	var requiredFieldsExists=true;
	if($("#selectAttributeIdCRC option[isrequired='YES']").length>0){
			$.each($("#selectAttributeIdCRC option[isrequired='YES']"),function(){
				$attId=$(this).val();
				if($("#relationDisplayDivCRC input[value='"+$attId+"']").length==0){
					requiredFieldsExists=false;
				}
				
			 });
	}else{
		requiredFieldsExists=false;
	}
	return requiredFieldsExists;
}
function saveCRCDefinition(){ 
       /**  Data to be saved**/
	  // alert("CRC:::Save");
	   var requiredFieldsExists=checkForCRCRequiredFields();
	   var conceptId=$("#selectedConceptId").val();	 
		$request=""		
		$request+="selectedConceptId="+conceptId;		
		$.each($("#relationDisplayDivCRC input[name='hiddenAssociationDataCRC']"),function(k,v){		
			$request+="&selectedCRCPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("conceptBedId");
			$request+="&selectedCRCPathDefinitions["+k+"].relationName="+$(this).attr("relationText");				
			$request+="&selectedCRCPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("realizationConceptBedId");
			$request+="&selectedCRCPathDefinitions["+k+"].attributeBedId="+$(this).val();
		});
		    if($("#selectAttributeIdCRC option[isrequired='YES']").length>0){
			if(!requiredFieldsExists){
				var message="Association with required attributes must exist.";
				showValidationMessage(message,'CRC');
				return false;
			   }
		    }
		    /**  Original Path definition**/
			$.each($("#selectedTypePathDefinitionsDivCRC input[name='selectedTypePathDefinitionsCRC']"),function(k,v){		
				$request+="&savedCRCPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("sourceConceptBedId");
				$request+="&savedCRCPathDefinitions["+k+"].relationName="+$(this).attr("relationName");
				$request+="&savedCRCPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("destinationConceptBedId");													
				$request+="&savedCRCPathDefinitions["+k+"].attributeBedId="+$(this).attr("attributeBedId");
				$request+="&savedCRCPathDefinitions["+k+"].pathDefinitionId="+$(this).attr("pathDefinitionId");		
				
			});	  
		
			$("#submitButtonCRC").hide();
			$("#submitButtonLoaderCRC").show();
			//alert("CRC:::"+$request);

	   $.ajax({url:"swi/saveCRCAssociation.action",
			    data:$request,			    
				success:function(data){showData(data);},
			    type:"post"
			   });
	   
		function showData(data){
		        $("#validationMessageCRC").empty(); CRCPathData=data;
		        $("#dynamicPaneConceptAssociations").empty().append(data);			  
			    $("#submitButtonCRC").show();
			    $("#submitButtonLoaderCRC").hide();
			}
	
}
function resetCRCMeaning(){ 
loadCRCAccociationTab()
}

 function createAutoSuggest(){ 
	  $("#relationTextCRC").autocomplete('<s:url action="suggestRelations" includeParams="none"/>', {
			selectionCallBack : function conceptSuggestCallBack(selectedObject) {}
		});
  } 
</script>