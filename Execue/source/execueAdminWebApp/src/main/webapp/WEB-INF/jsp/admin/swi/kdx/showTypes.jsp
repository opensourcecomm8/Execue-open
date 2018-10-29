<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>

<body>

<table width="100%" align="center" border="0" cellspacing="0"
	cellpadding="5" style="margin-left: 20px; margin-top: 30px;">
	<tr>
		<td>
		  <div id="statusMessage" style="display: none"></div>
		  <div id="validationMessage" style="display: none"></div>		  
		</td>
		
	</tr>
	<tr>
		<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="15%"><s:text name="execue.showTypes.SelectType" /></td>
				<td width="85%">
				<div id="selectTypeDiv"><select name="" id="selectTypeId">
					<s:iterator value="types">
						<option id="<s:property value='id'/>"
							value="<s:property value='id'/>"><s:property
							value='displayName' /></option>
					</s:iterator>
				</select></div>
                <input type="hidden" name="hiddenSelectedType"  id="hiddenSelectedType" />
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<s:hidden name="selectedConceptId"></s:hidden>
<script>
var savedDefaultType;
var savedConceptId;
  $(document).ready(function() {
		savedDefaultType=$("#selectTypeId option:selected").val();
	  getPathDefinition(savedDefaultType); 
   $("#selectTypeId").change(function(){
	var typeId=$("#selectTypeId option:selected").val();
	selectTypeIdDefaultsVal=typeId;
     getPathDefinition(typeId);
	});
});
function getPathDefinition(typeId){
   conceptId=$("#selectedConceptId").val(); 
   $.get("showPathDefinition.action?selectedTypeId="+typeId+"&selectedConceptId="+conceptId,function(data){
   $("#dynamicPanePathDefinition").empty().append(data);
   });
   checkToDisplayAdvancedMeaning(typeId,conceptId);
}

function checkToDisplayAdvancedMeaning(typId,conceptId){
if(savedDefaultType==typId){
    $.get("showAdvancedPathDefinition.action?selectedTypeId="+typId+"&selectedConceptId="+conceptId,function(data){
   $("#dynamicPaneAdvancedPathDefinition").empty().append(data);
   });
   }else{
	 $("#dynamicPaneAdvancedPathDefinition").empty().append("<div style='padding:30px;color:green;'>Please save 'Add Meaning' to access 'Advanced Meaning' options</div> ");  
   }	
}
</script>