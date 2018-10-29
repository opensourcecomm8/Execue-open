<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@page import="com.execue.core.common.type.PublishedFileType"%>

<%@page import="com.execue.core.common.type.CheckType"%>
<table width="98%" border="0" cellspacing="0" cellpadding="0"
	align="center">
	<tr>
		<td valign="top" class="descriptionText"><s:text
			name="execue.upload.main.description" /></td>
	</tr>
	<tr>
		<td valign="top" background="../images/admin/blueLine.jpg"><img
			src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
	</tr>
</table>
<div id="uploadCsvDynamicContent" style="float: left">

<div id="errorMessageOuter">
<div id="errorMessage" style="padding-top: 5px;"></div>
</div>

<div class="innerPane"
	style="width: 99%; white-space: nowrap; min-height: 200px; height: auto;"><s:form
	namespace="/publisher" action="uploadCSV" method="post"
	enctype="multipart/form-data">
	<table width="70%" border="0" cellspacing="0" cellpadding="0"
		style="margin-left: 20px;">
		<tr>
			<td colspan="2" align="right" height="20">&nbsp;</td>
		</tr>
		<tr>

			<td align="left" valign="top">

			<table width="1%" border="0" align="left" cellpadding="3"
				cellspacing="0">
				<tr>
					<td>

					<div
						style="width: 280px; border: 1px solid #CCC; padding: 5px; margin-top: 0px; height: 198px;">
					<table width="98%" border="0" cellpadding="0" cellspacing="0"
						align="center">
						<!--
                  <tr>
                    <td class="fieldNames"><s:text name="upload.main.application.name" /></td>
                    <td height="40"><s:property value="applicationContext.applicationName"/></td>
                  </tr>
                  -->
						<tr>
							<td width="26%" class="fieldNames"><s:text
								name="execue.upload.main.file.name"></s:text><s:text
								name="execue.global.mandatory"></s:text></td>
							<td width="74%" align="left"><s:textfield name="fileName"
								cssStyle="width:auto;min-width:200px;" id="fileName" />
								 <a href="#" id="helpLink" title="Help" alt="Help">?</a></td>
						</tr>
						<tr>
							<td class="fieldNames" valign="top" s><s:text
								name="execue.upload.main.file.description"></s:text><s:text
								name="execue.global.mandatory"></s:text></td>
							<td align="left" style="padding-top: 4px;"><s:textarea
								name="fileDescription" cols="23" rows="4" id="fileDescription" /></td>
						</tr>
						<tr>
							<td class="fieldNames" valign="top"><s:text
								name="execue.upload.main.file.tag"></s:text></td>
							<td align="left" style="padding-top: 4px;"><textarea
								cols="23" rows="2" name="tag"></textarea></td>
						</tr>

						<tr id="applicationSelectId">
							<td height="30"><s:text name="execue.upload.main.application"></s:text>
							</td>
							<td><select id="selectedAppId"
								name="selectedApp.applicationId">
								<option value="-1" selected="selected">None</option>
								<s:iterator value="applications">
									<option value="<s:property value="applicationId"/>"
										<s:if test="applicationId == applicationContext.appId">
									selected="selected"
								</s:if>
										modelId="<s:property value='modelId'/>"><s:property
										value="applicationName" /></option>
								</s:iterator>
							</select> <input type="hidden" name="selectedApp.modelId" id="modelId"
								value="" /> <input type="hidden"
								name="selectedApp.applicationName" id="selectedAppName" value="" />
							</td>
						</tr>

					</table>
					</div>



					</td>
				</tr>


				<tr>
					<td colspan="2" class="fieldNames">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr id="absorbDatasetTR">
							<td width="5%" align="left"><label> <input
								type="checkbox" id="absorbDatasetId" name="absorbDataset"
								value="YES" /> </label></td>
							<td width="93%" style="padding-left: 4px;"><s:text
								name="execue.upload.main.create.source.message"></s:text></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="left" valign="bottom"><span
						class="fieldNames"> <span id="uploadButtonSpan"> <input
						type="submit" class="buttonSize80" name="imageField"
						id="imageField" value="Upload" /> </span> <span
						id="uploadButtonSpanLoader" style="display: none;"> <input
						type="button" class="buttonLoaderSize80" disabled="disabled"
						name="imageField" value="Upload" /> </span> <span class="rightButton"><input
						type="button" class="buttonSize80" name="imageField2"
						id="imageField2" value="<s:text name='execue.global.cancel' />"
						onClick="cancelUpload();" /></span> </span></td>
				</tr>
			</table>
			</td>
			<td align="left" valign="top" style="padding-left: 0px;">
			<table width="1%" border="0" align="left" cellpadding="3"
				cellspacing="0">

				<tr>
					<td>
					<div
						style="width: 312px; border: 1px solid #CCC; padding: 5px; height: 65px;">
					<table border="0">
						<tr>
							<td width="24%" class="fieldNames"><s:text
								name="execue.upload.main.source-file" /></td>
							<td width="76%" align="left"><label> <s:file
								name="sourceData" label="File" onchange="showFileInfo(this);" />
							</label></td>
						</tr>
						<tr>
							<td width="24%" class="fieldNames"><s:text
								name="execue.upload.main.source.url"></s:text></td>
							<td width="76%" align="left"><s:textfield name="sourceURL"
								cssStyle="width:auto;min-width:216px;" /></td>
						</tr>
						<tr>
							<td></td>
							<td width="24%" align="center"><s:text
								name="execue.upload.main.file.select.source.message"></s:text><s:text
								name="execue.global.mandatory"></s:text></td>

						</tr>
						
					</table>
					</div>
					</td>
				</tr>
				<tr>
					<td height="35" colspan="2" class="fieldNames">
					<div
						style="width: 312px; border: 1px solid #CCC; padding: 5px; padding-bottom: 3px; padding-top: 3px; margin-top: 5px;"
						id="fileinfoDIV">
					<table width="100%" border="0" align="center" cellpadding="2"
						cellspacing="0">
						<tr>
							<td colspan="2">
							<table width="auto" border="0" cellspacing="0" cellpadding="4"
								align="center">
								<tr>
									<td><s:radio name="sourceType"
										list="csvTypes" value="%{csvTypes.CSV}"
										onchange="handleFileType(this);" /></td>
									<!-- <td><input name="" type="radio" value="" /><label>Others</label></td>-->
								</tr>
							</table>
							</td>
						</tr>

						<tr id="columnNamesPresentTR">
							<td width="58%"><s:text name="execue.upload.main.columns.specified" /></td>
							<td width="42%"><label> <s:checkbox
								name="columnNamesPresent" value="true" fieldValue="true" /> </label></td>
						</tr>
						<tr id="uploadAsCompressedFileTR">
							<td width="58%"><s:text name="execue.upload.main.file.zip" /></td>
							<td width="42%"><label> <input type="checkbox"
								id="uploadAsCompressedFileId" name="uploadAsCompressedFile"
								value="NO" onclick="handleUploadAsZip(this);" /> </label></td>
						</tr>
						<tr id="stringEnclosureTR">
							<td><s:text name="execue.upload.main.string.quoted.with" /></td>
							<td><s:select name="stringEnclosure" id="stringEnclosure"
								list="CSVStringEnclosedCharacterTypes" listValue="description" /></td>
						</tr>
						<tr id="nullIdentifierTR">
							<td><s:text name="execue.upload.main.null.specified.as" /></td>
							<td><s:select name="nullIdentifier" id="nullIdentifier"
								list="CSVEmptyFields" listValue="description" /></td>
						</tr>
						 <tr>
							<td width="58%"><s:text name="execue.upload.main.delimeter"/></td>
							<td width="42%"><label> <s:textfield cssStyle="width: 113px;" name="dataDelimeter"/> </label></td>
						</tr>
					</table>
					</div>
					</td>
				</tr>
			</table>
			</td>
		</tr>


	</table>

	<s:hidden name="wizardBased" />
	<s:hidden name="operationType" />
</s:form></div>
</div>

<script>
$(function(){ 
	$("#helpLink").helpMessage({messageKey:"execue.upload.help.datasetCollection.name"}); 
	$("#fileinfoDIV").hide(); 
	$("#absorbDatasetTR").hide();
	$("#applicationSelectId").hide();
	  
	
	hideShowAppSelectBox($("#absorbDatasetId"));	
	$("#selectedAppId").change(function(){
		setAppValues();
	});	
	$("#absorbDatasetId").click(function(){
		hideShowAppSelectBox($(this));	
	});
});

function handleUploadAsZip(obj){
   if($("#"+obj.id).is(':checked')){
     $("#uploadAsCompressedFileId").val('YES');
   }else{
      $("#uploadAsCompressedFileId").val('NO');
   }
}
function hideShowAppSelectBox(obj){
   if(obj.is(':checked')){
	  $("#applicationSelectId").show();
	  setAppValues();
	}else{
	  $("#applicationSelectId").hide();	  
	  resetAppValues();
	}
}
function setAppValues(){ 
	$("#modelId").val($("#selectedAppId option:selected").attr("modelid"));
	$("#selectedAppName").val($("#selectedAppId option:selected").text());	
	$("#selectedAppId").removeAttr("disabled");
	$("#modelId").removeAttr("disabled");
	$("#selectedAppName").removeAttr("disabled");
}
function resetAppValues(){ 
	$("#selectedAppId").attr("disabled","disabled");
	$("#modelId").attr("disabled","disabled");
	$("#selectedAppName").attr("disabled","disabled");
}
function handleFileType(obj){ 
var fileTypeOther='<%=PublishedFileType.OTHER%>';
  if(obj.value== fileTypeOther){
	  hideFileDetail();
	  resetAppValues();
	  $("#applicationSelectId").hide();	
	  $("#absorbDatasetId").removeAttr("checked"); 
  }else{
     showFileDetail();
  }
}
function showFileDetail(){ 
     $("#columnNamesPresentTR").show();
     $("#stringEnclosureTR").show();
     $("#nullIdentifierTR").show();
     $("#absorbDatasetTR").show();
     $("#uploadAsCompressedFileTR").show();	 
}
function hideFileDetail(){ 
	 $("#columnNamesPresentTR").hide();
	 $("#stringEnclosureTR").hide();
	 $("#nullIdentifierTR").hide(); 
	 $("#absorbDatasetTR").hide();
	 $("#uploadAsCompressedFileTR").hide();	 
}
function showFileInfo(obj){
 if(obj.value){
  $("#fileinfoDIV").show(); 
  $("#absorbDatasetTR").show();  
 }
 
 
}

function cancelUpload(){
window.location="../swi/showSearchAppsDashboard.action";	
showLoaderImageOnLoad();
}
</script>