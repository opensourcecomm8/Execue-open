<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="errorMessage"
	style="color: red; padding-top: 10px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage"
	style="color: green; padding-top: 10px; padding-left: 20px;"><s:actionmessage /></div>
<div id="statusMessage" style="display: none; padding-left: 20px;"></div>
<div id="validationMessageHierarchy"
	style="display: none; padding-left: 20px;"></div>
<table cellspacing="0" cellpadding="0" border="0" align="left"
	width="100%" style="margin-left: 20px;">
	<tbody>
		<tr>

			<td height="25" width="60%" class="fieldNames"><strong>Update/View
			Hierarchy</strong></td>


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

						<table cellspacing="0" border="0" width="100%">
							<tbody>
								<tr>
									<td width="100" class="fieldNames">Selected Concept : </td>
									<td><s:property value="concept.displayName" /></td>
								</tr>
							</tbody>
						</table>
						<input type="hidden" id="conceptId"
							value="<s:property value='concept.id' />"> <input
							type="hidden" id="conceptName"
							value="<s:property value='concept.displayName' />"></td>
					</tr>
					<tr>
						<td>
						<table cellspacing="0" border="0">
							<tbody>
								<tr>
									<td width="100">Select Hierarchy</td>
									<td><select id="selectHierarchy" style="width: 205px;" name="">
										<option value="-1" selected="selected">Select</option>
										<s:iterator value="relationHiearchyTypes">
											<option value="<s:property/>"
												hierarchyBedId="<s:property value="value" />"><s:property
												value="description" /></option>
										</s:iterator>
									</select> <input type="hidden" id="hiddenSelectedHierarchy"
										name="hiddenSelectedHierarchy"></td>
									<td>&nbsp;</td>
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
									<div style="width: 565px; height: auto;display:none;"
										id="hierarchyDefinitionDiv" ></div>
                                        
                                        <div style="width: 576px; height: 288px;display:none;border:1px solid #ccc;background:url('../images/loadingBlue.gif') no-repeat center center;margin-top:10px;"
										id="hierarchyDefinitionDivLoader" ></div>

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
							onclick="javascript:return saveHierarchy()" value="Submit"
							class="singleButton" id="submitButton"> <input
							type="button" value="Submit" disabled="disabled"
							class="singleButtonLoader" style="display: none;"
							id="submitButtonLoader"> <a
							onclick="javascript:resetHierarchy()" id="resetButton"
							style="margin-left: 20px;" href="#"> Reset </a> <img
							src="../images/loadingBlue.gif"
							style="display: none; margin-left: 20px;" id="resetButtonLoader"></div>
						</td>
					</tr>
				</tbody>
			</table>
			<input type="hidden" id="businessEntityId"
				value='<s:property value="concept.bedId"/>' /></td>
		</tr>

	</tbody>
</table>

<script>
$(function(){		  
		$("#selectHierarchy").change(function(){  
				var saveExists=false;
				saveExists=checkAssociationsToSave();
				if(saveExists){
					confirmed = confirm("Un saved Associations Exist, Do you want to save the Changes ?");
					if(confirmed){
						$("#submitButton").click();
						return;
						}
					}
				var selectedHierarchyType=$(this).val();
				var selectedHierarchyId=$(this).attr("hierarchyBedId");
				var conceptBedId=$("#businessEntityId").val();
				if(selectedHierarchyType != -1){
					$("#hierarchyDefinitionDiv").hide();
					$("#hierarchyDefinitionDivLoader").show(); 
				    $.get("showHierarchyDetails.action?concept.bedId="+conceptBedId+"&selectedHierarchyType="+selectedHierarchyType,function(data){
			        $("#hierarchyDefinitionDiv").empty().append(data);
					$("#hierarchyDefinitionDivLoader").hide();
					 $("#hierarchyDefinitionDiv").fadeIn('fast');
				    });
				}else{
				 $("#hierarchyDefinitionDiv").empty();
				}
		 });

});
function checkAssociationsToSave(){
var saveExists=false;

$.each($("#assoiationDisplayDivHierarchy input[name='hiddenAssociationDataHierarchy']"),function(){
	if($(this).attr("entitytripledefinitionid")==undefined){
	saveExists=true;
	}
});
return saveExists;
}

function resetHierarchy(){
	$("#hierarchyDefinitionDiv").empty();
	//$("#selectHierarchy option[value='-1']").attr("selected",true);
	$("#selectHierarchy option").eq(0).attr("selected",true);

}
</script>