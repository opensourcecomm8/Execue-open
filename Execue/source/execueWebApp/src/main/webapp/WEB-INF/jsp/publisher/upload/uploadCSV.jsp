<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="com.execue.core.common.type.PublishedFileType"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<style>
select {
	width: 100px;
}
</style>
<script src="../js/jquery.execue.helpMessage.js" ></script>
<table width="98%" border="0" cellspacing="0" cellpadding="0"
	align="center">
	<tr>
		<td valign="top" class="descriptionText">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="40%" align="left"><s:text
					name="execue.upload.main.simplified.path.description" /></td>
					<td width="60%" align="right" style="padding-bottom: 3px;">&nbsp;
                    </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td valign="top" background="../images/blueLine.jpg"><img
			src="../images/blueLine.jpg" width="10" height="1" /></td>
	</tr>
</table>
<div id="uploadCsvDynamicContent" style="float: left">

<div id="errorMessageOuter" style="padding-left: 10px;">
<div id="errorMessage" style="padding-top: 5px;"></div>
</div>

<div class="innerPane"
	style="width: 900px; white-space: nowrap; min-height: 100px; height: auto;">
	<s:form namespace="/publisher" action="uploadCSV" method="post"  enctype="multipart/form-data">
	<table width="900" border="0" align="left" cellspacing="0"
		cellpadding="0" style="margin-left: 10px;">
		
		<tr>
			<td align="left" valign="top" style="padding-left: 0px;">
			
					
					<table border="0">
						<!-- <tr>
							<td width="24%" class="fieldNames"><s:text
								name="execue.upload.main.file.name"></s:text><s:text
								name="execue.global.mandatory">
							</s:text></td>
							<td width="76%" align="left"><s:textfield name="fileName"
								cssStyle="width:auto;min-width:143px;" id="fileName" /></td>
						</tr>-->
						<tr>
							<td width="24%" class="fieldNames"><s:text
								name="execue.upload.main.source-file" /></td>
							<td width="76%" align="left"><label> <s:file
								name="sourceData" label="File"  />
							</label>
							<label><i><s:text
								name="execue.upload.file.max.size" /> </i>
                                <a href="#" id="helpLink" title="Help" alt="Help">?</a>
							
                           
                            
                            </td>
						</tr>
						
						<tr>
							<td colspan="2" height="50">
                            
                            <div
						style="width: 350px; border: 1px solid #CCC; padding: 5px; min-height: 40px;height:auto;float:left;">
                        
							<table width="90%" border="0" cellspacing="0" cellpadding="4"
								align="center">
                                <tr><td colspan="2"><strong>Advanced</strong></td></tr>
								<tr>
									<td> <s:radio name="sourceType"
										list="csvTypes" value="%{csvTypes.CSV}"
										onchange="handleFileType(this);" /><input type="checkbox"
								id="uploadAsCompressedFileId" name="uploadAsCompressedFile"
								value="NO" onclick="handleUploadAsZip(this);" /> <s:text name="execue.upload.main.file.zip" /></td>
									<!-- <td><input name="" type="radio" value="" /><label>Others</label></td>-->
                                    <td align="right"><a href="#" id="moreLink" onclick="javascript:showAdvanceDetail();">More</a></td>
								</tr>
							</table>
                            
                            
                            <div
						style="width: 350px; border: 0px solid #CCC; padding: 5px;margin-left:5px; height: 100px;float:left;"
						id="fileinfoDIV">
					<table width="100%" border="0" align="center" cellpadding="2"
						cellspacing="0">


						<tr id="columnNamesPresentTR">
							<td width="58%"><s:text name="execue.upload.main.columns.specified" /></td>
							<td width="42%"><label> <s:checkbox
								name="columnNamesPresent" value="true" fieldValue="true" /> </label></td>
						</tr>
						<!-- <tr id="uploadAsCompressedFileTR">
							<td width="58%"><s:text name="upload.main.file.zip" /></td>
							<td width="42%"><label> <input type="checkbox"
								id="uploadAsCompressedFileId" name="uploadAsCompressedFile"
								value="NO" onclick="handleUploadAsZip(this);" /> </label></td>
						</tr>-->
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
							<td width="42%"><label> <s:textfield cssStyle="width: 100px;" name="dataDelimeter"/> </label></td>
						</tr>
					</table>
					</div>
                    
                            </div>
                            
                            
							</td>
						</tr>
                      
					</table>
					
					
					
					
			</td>
		</tr>

		<tr>
			<td colspan="2" align="left" valign="bottom"><span
				class="fieldNames"> <span id="uploadButtonSpan"> <input
				type="button" onclick="return checkFile();" class="buttonSize80" name="imageField" id="imageField"
				value="Upload" /> </span> <span id="uploadButtonSpanLoader"
				style="display: none;"> <input type="button"
				class="buttonLoaderSize80" disabled="disabled" name="imageField"
				value="Upload" /> </span> <span class="rightButton"><input
				type="button" class="buttonSize80" name="imageField2"
				id="imageField2" value="<s:text name='execue.global.cancel' />"
				onClick="cancelUpload();" /></span> </span></td>
		</tr>
		<tr>
		</tr>
		  <td colspan="2" align="left" valign="bottom">&nbsp; </td>
		<tr>
                <td colspan="2" align="left" valign="bottom"><span
					id="advancedConsoleLink" ><s:text name="execue.upload.tryAdvancedConsole" /> <a
					href="<c:out value="${basePath}"/>/swi/accountAccess.action?type=2"><s:text name="execue.upload.clickHere" /></a></span>
				</td>
		</tr>
	</table>

	<s:hidden name="wizardBased" />
	<s:hidden name="operationType" />
	<s:hidden name="publisherProcessType" />
</s:form></div>
</div>

<script>

var advance=false;
$(function(){  
	//showUploadHelp('upload.size.help');
	$("#helpLink").helpMessage({messageKey:"upload.size.help"}); 
	$("#fileinfoDIV").hide(); 	
});
function checkFile(){ 
$fval=$("#uploadCSV_sourceData").val();
if($fval==""){
$("#errorMessage").html("Click 'Browse' button to select source file").show();
setTimeout('$("#errorMessage").empty()',5000);
return false;
}else{
$("#uploadCSV").submit();
return true;
}
}
function handleUploadAsZip(obj){
   if($("#"+obj.id).is(':checked')){
     $("#uploadAsCompressedFileId").val('YES');
   }else{
      $("#uploadAsCompressedFileId").val('NO');
   }
}
function handleFileType(obj){ 
var fileTypeOther='<%=PublishedFileType.OTHER%>';
  if(obj.value== fileTypeOther){
	  hideFileDetail();
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
}
function hideFileDetail(){ 
	 $("#columnNamesPresentTR").hide();
	 $("#stringEnclosureTR").hide();
	 $("#nullIdentifierTR").hide(); 
}
/*function showFileInfo(obj){
 if(obj.value){
  $("#fileinfoDIV").show();  
 } 
}*/
function showAdvanceDetail(){
  if(!advance){
     $("#fileinfoDIV").slideDown();
	 $("#moreLink").text("Less");
	 //$("#uploadFileInfoOuterDiv").css("height","360px")
     advance=true;  
  }else{
     $("#fileinfoDIV").slideUp();
	 //$("#uploadFileInfoOuterDiv").css("height","250px").slideDown();
	 $("#moreLink").text("More");
     advance=false;  
  }
}

function cancelUpload(){
window.location="../swi/showSearchAppsDashboard.action";	
showLoaderImageOnLoad();
}

function showPopup(desc){
    var top= $("#helpLink").position().top;
    var left= $("#helpLink").position().left;
	$("#uploadHelpPopupText").text(desc); 
	$("#uploadHelpPopup").css("top",top-40+"px").css("left",left+20+"px").show();
}
function closeHelpPopup(){
$("#uploadHelpPopup").hide();
}
</script>