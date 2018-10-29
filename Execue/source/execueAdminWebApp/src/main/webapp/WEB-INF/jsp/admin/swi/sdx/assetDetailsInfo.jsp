<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div class="innerPane2" style="width: 430px; height: auto">
<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green"><s:actionmessage /></div>
<s:form id="assetDetailForm" namespace="/swi" action="updateAssetDetail"
	method="post">
	<table width="97%" border="0" align="center" cellpadding="0"
		cellspacing="3">

		

		<tr>
			<td height="40" class="fieldNames" style="white-space: nowrap;">
			<table width="100%" border="0" cellspacing="0" cellpadding="4">

				<tr>
					<td width="30%" valign="top"><s:text name="execue.asset.asset.name" />
					</td>
					<td width="70%" height="20" class="fieldNames"><b><s:property
						value="assetDisplayName" /></b></td>
				</tr>
				<tr>
					<td  valign="top"><s:text name="execue.global.description" />
					:</td>
					<td  height="20" class="fieldNames"><s:property
						value="assetDescription" /></td>
				</tr>

				<tr>
					<td  valign="top"><s:text name='execue.asset.asset.shortName' /></td>
					<td ><label> <s:textarea
						name="uiAssetDetail.shortNote" id="textarea" cols="30" rows="3"
						onkeydown="EnforceMaximumLength(this,100)"></s:textarea><br />
					<s:text name='execue.asset.asset.maxChars' /> </label></td>
				</tr>
				<tr>
					<td valign="top"><s:text name='execue.asset.asset.extendedNote' /></td>
					<td><s:textarea name="uiAssetDetail.extendedNote"
						id="textarea2" cols="30" rows="3"></s:textarea></td>
				</tr>
				<tr>
					<td valign="top"><s:text name='execue.asset.asset.disclaimer' /></td>
					<td><s:textarea name="uiAssetDetail.extendedDisclaimer"
						id="textarea3" cols="30" rows="3"></s:textarea></td>
				</tr>

				<tr>
					
					<td height="40" colspan="2" align="left"><span id="enableUpdateConcept">
					<input type="button"	class="buttonSize108" style="width:80px;" id="submitAssetDetail" value="<s:text name='execue.global.updatebutton.name' />"
						alt="Update Asset Detail" /> <span id="updateAssetDetailLoader"
						style="display: none;"><input type="button" disabled="disabled"	class="buttonLoaderSize80" style="width:80px;"
						value="<s:text name='execue.global.updatebutton.name' />" /></span> <span
						id="resetAssetDetailLoader" style="display: none;"><input type="button" disabled="disabled"	class="buttonLoaderSize51" style="width:90px;"
						value="<s:text name='execue.global.resetButton.name' />" /></span>
                        <span  id="resetAssetGrains"><input type="button"	class="buttonSize90" style="width:90px;"
						value="<s:text name='execue.global.resetButton.name' />" onclick="reset();" /></span></td>
				</tr>
			</table>

			</td>
		</tr>
		
		<s:hidden name="uiAssetDetail.shortNote" id="shortNoteId"/>
		<s:hidden name="uiAssetDetail.extendedNote" id="extendedNoteId"/>
		<s:hidden name="uiAssetDetail.extendedDisclaimer" id="extendedDisclaimerId" />				
		<s:hidden name="assetDisplayName" />
		<s:hidden name="assetDescription" />
		<s:hidden name="assetId" />
		<s:hidden name="uiAssetDetail.assetDetailId" />
		<s:hidden name="uiAssetDetail.extendedAssetDetailId" />
	</table>
</s:form></div>
<script type="text/javascript">
$("#submitAssetDetail").click(function(){
    $("#updateAssetDetailLoader").show();
    $("#submitAssetDetail").hide();   
    var updateAssetGrain="../swi/updateAssetDetail.action";      
    $.post(updateAssetGrain, $('#assetDetailForm').serialize(), function(data) {
		//$("#hiddenPane").empty();
		//$("#dynamicPane").fadeIn("fast");    
		// $("#updateAssetDetailLoader").hide(); 
		// $("#submitAssetDetail").show();  
		$("#hiddenPaneContent").empty().append(data);
		//$("#dynamicPane").show();		 
	}); 	
});
function EnforceMaximumLength(fld,len) {
if(fld.value.length > len) { 
  fld.value = fld.value.substr(0,len);
  return false;
 }
}
function reset() {    
   $("#resetAssetDetail").hide();
   $("#resetAssetDetailLoader").show();      
   $("#textarea").val($("#shortNoteId").val());
   $("#textarea2").val($("#extendedNoteId").val()); 
   $("#textarea3").val($("#extendedDisclaimerId").val());  
   $("#resetAssetDetail").show();
   $("#resetAssetDetailLoader").hide();    
}
</script>

