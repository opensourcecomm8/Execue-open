<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@page import="com.execue.web.core.util.ExecueWebConstants"%>

<c:set var="searchPath" value="<%=application.getAttribute(ExecueWebConstants.MAIN_CONTEXT)%>" />

<link rel="stylesheet" type="text/css"
	href="../css/admin/jquery.cleditor.css" />
<script src="../js/admin/jquery.execue.helpMessage.js"></script>
<script type="text/javascript" src="../js/admin/jquery.cleditor.min.js"></script>
<s:if test="application==null || application.id == null">
	<s:set name="mode" value="%{'add'}" />
</s:if>
<s:else>
	<s:set name="mode" value="%{'update'}" />
</s:else>
<style>
.fieldNames {
	white-space: nowrap;
}

.appTextFields {
	width: 217px;
}

input#example {
	height: 18px;
	padding-top: 2px;
	width: 180px;
}
</style>
<div id="uploadImageDynamicContent" />
<div id="errorMessage" style="padding-top: 5px; padding-bottom: 3px;"></div>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="50%" valign="top"><s:form id="appDefinitionForm"
			namespace="/swi" action="createApp" method="post"
			onsubmit="return checkName();" enctype="multipart/form-data">

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="70%">
					<table width="100%" border="0" align="left" cellpadding="0"
						cellspacing="0" style="margin-left: 20px;">

						<tr>
							<td width="25%" class="fieldNames"><s:text
								name="execue.application.name" /><s:text
								name="execue.global.mandatory"></s:text></td>
							<td width="75%"><s:textfield cssClass="appTextFields"
								name="application.name" id="appName" /> <a href="#"
								id="helpLink" title="Help" alt="Help" tabindex="-1">?</a>
							<div id="appNameMessage"
								style="display: none; white-space: nowrap; position: absolute; z-index: 100; padding: 4px; border: 1px solid #ccc; background-color: #FFF; color: #060;">Please
							enter only letters , numeric characters, <br />
							under score (_)</div>
							</td>
						</tr>
						<tr>
							<td class="fieldNames" valign="top"><s:text
								name="execue.global.description" /></td>
							<td style="padding-top: 3px;"><s:textarea
								name="application.description" cols="23" rows="4"
								cssClass="appTextFields" id="appDesc" /></td>
						</tr>
						<tr>
							<td class="fieldNames" valign="top"><s:text
								name="execue.application.appTag" /></td>
							<td style="padding-top: 3px;"><s:textfield
								name="application.applicationTag" cssClass="appTextFields"
								id="appTag" /></td>
						</tr>
						<s:if test="advancedPublisher">
							<tr>
								<td class="fieldNames" valign="top"><s:text
									name="Source type" />

								<div style="display: none;" class="unstructuredFields">
								Facet Nature</div>

								</td>
								<td style="padding-top: 3px;"><s:select
									id="appSourceTypeId" list="appSourceTypes"
									listValue="description" name="application.sourceType"
									cssClass="appTextFields" />

								<div
									style="display: none; float: left; margin-bottom: 3px; margin-top: 3px;"
									class="unstructuredFields">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="20" colspan="2"><s:select name="application.unstructuredApplicationDetail.facetNatureType"
											list="facetNatureTypes" 
											listValue="description" cssClass="appTextFields"></s:select></td>

									</tr>
									<tr>
										<td width="25"><input name="application.unstructuredApplicationDetail.locationFromContent" type="checkbox" id="locationFromContentId" value="YES" /></td>
										<td>Consider content from location</td>
									</tr>
								</table>


								</div>


								</td>
							</tr>
						</s:if>
						
						<!-- Showing the Application URL -->
						<s:if test="#mode != 'add'">
						<tr>
							<td class="fieldNames"><s:text
								name="execue.application.applicationURL" /></td>
							<td style="padding-top: 3px; padding-bottom: 3px;" id="applicationURLLink">
								<a href="<c:out value='${searchPath}'/><s:property value='application.applicationURL'/>">
								<s:property value="application.name"/>
								</a>
							</td>
						</tr>
						</s:if>
						<s:hidden id="appUrl" name="application.applicationURL"></s:hidden>
						<tr>
                            <td class="fieldNames" valign="top"><s:text
                                name="execue.application.publish" /></td>
                            <td style="padding-top: 3px;">
                            <s:if test="application == null || application.publishMode.value == 0 || application.publishMode.value == 1">
					            <input name="application.publishMode" value="LOCAL" id="appPublishModeId" type="checkbox" onchange="changePublishMode()"/>
					         </s:if>
					         <s:else>            
					             <input name="application.publishMode" value="COMMUNITY" id="appPublishModeId" type="checkbox"  checked="checked" onchange="changePublishMode()"/>             
					         </s:else>
					         <a href="#" id="publishHelpLink" title="Help" alt="Help" tabindex="-1">?</a>
                            </td>
                        </tr>
						<s:if test="#mode != 'update'">
						<tr id="applicationURLLinkRow" style="display:none;">
							<td class="fieldNames"><s:text
								name="execue.application.applicationURL" /></td>
							<td id="applicationURLLink" style="padding-top: 3px; padding-bottom: 3px;">
							</td>
						</tr>
						</s:if>
						<!-- End of Showing the Application URL -->
						
						<tr>
							<td class="fieldNames"><s:text
								name="execue.editApp.app.image"></s:text></td>
							<td align="left"><label> <s:file name="sourceData"
								label="File" /> </label></td>
						</tr>

					</table>
					</td>
					<td width="40%" valign="top">
					<div id="dynamicImageOuterDivBorder"
						style="border: 1px solid #D0D9D9; padding: 5px; margin: 5px; margin-top: 0px; width: 110px; display: none;">
					<table align="center">
						<tr>
							<td align="center"><s:text
								name="execue.editApp.existingAppImage"></s:text></td>
						</tr>
						<s:if test="application.imageId != -1">

							<tr>
								<td align="center">
								<div id="dynamicImageOuterDiv"><img id="dynamicImageDiv"
									src="getImage.action?applicationId=<s:property value='application.id'/>&appImageId=<s:property value='application.imageId'/>"
									alt="not found" border="0" width="90"></img></div>
								</td>
							</tr>
						</s:if>
						<s:else>

							<tr>
								<td align="center">
								<div id="dynamicImageOuterDiv"><img id="dynamicImageDiv"
									src="../images/admin/noImage-icon.gif" alt="not found"
									border="0" width="90"></img></div>
								</td>
							</tr>

						</s:else>
					</table>
					</div>


					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table align="left" cellpadding="0" cellspacing="0" border="0"
						style="margin-left: 20px;">
						<tr>
							<td class="fieldNames" width="22%">&nbsp;</td>
							<td align="left" width="78%"><s:text
								name="execue.editApp.imageUploadInstructions" /></td>
						</tr>



						<tr>
							<td height="15" colspan="4" align="left"
								style="padding-top: 10px;">
							<div id="enableUpdateApplication"
								style="float: left; width: 90px;"><input type="submit"
								id="updateButton" class="singleButton"
								onclick="return showHideUpdate();"
								value="<s:text name='execue.editApp.updateApplicationButton.name' />" /></div>
							<div id="updateProcess"
								style="display: none; float: left; width: 90px;"><input
								type="button" class="singleButtonLoader" disabled="disabled"
								value="<s:text name='execue.editApp.updateApplicationButton.name' />" /></div>
							<div id="deleteAppButton"
								style="float: left; width: 90px; margin-left: 10px;"><input
								type="button" class="singleButton" id="imageField2"
								value="<s:text name='execue.editApp.deleteApplicationButton.name' />"
								onclick="javascript:deleteApplication(<s:property value='application.id'/>);" />
							</div>

							<div id="showStatus"
								style="display: none; float: left; min-width: 190px; width: auto; margin-left: 10px; white-space: nowrap; margin-top: 10px;"></div>

							<div id="enableCreateApplication"
								style="float: left; width: 90px;"><input type="submit"
								class="buttonSize108" id="imageField"
								onclick="javascript:showHideCreate();"
								value="<s:text name='execue.editApp.createApplicationButton.name' />" /></div>
							<div id="createLoader"
								style="display: none; float: left; width: 90px;"><input
								type="button" class="buttonLoaderSize108" disabled="disabled"
								value="<s:text name='execue.editApp.createApplicationButton.name' />" /></div>

							<div id="resetButton"
								style="float: left; width: 90px; margin-left: 20px;"><input
								type="reset" class="buttonSize108" style="margin-left: 20px;"
								onclick="clearMessage();" id="imageField2"
								value="<s:text name='execue.editApp.clearAllFieldsButton.name' />" />
							</div>
							</td>
						</tr>
					</table>

					</td>
					<td></td>
				</tr>
			</table>

			<s:hidden id="applicationId" name="application.id" />
		</s:form> <s:if test="uiAssetDetail.isAssetDisclaimerApplicable.value=='Y'">
			<table style="margin-top: 25px;">
				<tr>
					<td valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="4%" valign="top" style="padding-top: 2px;"><a
								onfocus="this.blur();" class="disclaimerDivLink"
								href="javascript:showForm('disclaimerDiv');"><img
								id="disclaimerDivImg"
								src="../images/admin/bullet_toggle_plus.png" border="0" /></a></td>
							<td width="94%" style="padding-top: 3px;"><strong><a
								class="disclaimerDivLink" onfocus="this.blur();"
								href="javascript:showForm('disclaimerDiv');">Disclaimer</a></strong>

                         <div id="disclaimerMessageDiv" style="float: right; padding-right: 30px;"></div>
						 <div id="disclaimerMessageErrorDiv" style="float: right; color:red; padding-right: 30px;"></div>	
							

							<div id="disclaimerDiv" style="width: 100%; height: 260px;"><s:form
								action="" id="disclaimerForm">
								<table width="50%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td><s:textarea id="extendedDisclaimerTextId" name="uiAssetDetail.extendedDisclaimer"></s:textarea><input
											type="hidden" name="uiAssetDetail.applicationId"
											value='<s:property value="application.id"/>' /></td>
									</tr>
									<tr>
										<td>									
										<div id="disclaimerButtonDiv"><label> <input
											type="button" id="disclaimerButton" class="singleButton"
											value="Submit" onclick="javascript:saveDisclaimer();" /> </label> <img
											id="disclaimerButtonImg"
											style="display: none; margin-left: 20px; margin-top: 5px;"
											src='../images/admin/loaderTrans.gif' /></div>
										</td>
										
									</tr>
									
								
									<tr>
										<td>&nbsp;</td>
									</tr>
								</table>
							</s:form></div>

							</td>
							
						</tr>
					</table>
					</td>

				</tr>
				<tr>
					<td valign="top">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="4%" valign="top" style="padding-top: 2px;"><a
								onfocus="this.blur();" class="notesDivLink"
								href="javascript:showForm('notesDiv');"><img
								id="notesDivImg" src="../images/admin/bullet_toggle_plus.png"
								border="0" /></a></td>
							<td width="96%" style="padding-top: 3px;"><strong><a
								onfocus="this.blur();" class="notesDivLink"
								href="javascript:showForm('notesDiv');">Notes</a></strong>

							<div id="notesMessageDiv"
								style="float: right; padding-right: 30px;"></div>
							<div id="notesMessageErrorDiv"
								style="float: right;color:red; padding-right: 30px;"></div>
							<div id="notesDiv" style="width: 100%; height: 260px;"><s:form
								id="noteForm">
								<table width="50%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td><s:textarea id="extendedNoteTextId" name="uiAssetDetail.extendedNote"
											cols="52" rows="5">
										</s:textarea> <input type="hidden" name="uiAssetDetail.applicationId"
											value='<s:property value="application.id"/>' /></td>
									</tr>
									<tr>
										<td>
										<div id="noteButtonDiv"><label> <input
											type="button" id="noteButton" class="singleButton"
											value="Submit" onclick="javascript:saveNote();" /> </label> <img
											id="noteButtonImg"
											style="display: none; margin-left: 20px; margin-top: 5px;"
											src='../images/admin/loaderTrans.gif' /></div>
										</td>
									</tr>
									<tr>
										<td>&nbsp;</td>
									</tr>
								</table>
							</s:form></div>
							</td>
						</tr>
					</table>
					</td>

				</tr>
			</table>
		</s:if></td>
		<td valign="top" width="50%"
			style="border-left: 1px dashed #CCC; padding-left: 20px;">


		<table width="80%" border="0" cellspacing="0" cellpadding="0"
			align="center" id="applicationQueryExampleTable">
			<tr>
				<td width="36%" height="25"><strong><s:text
					name="execue.editApp.query.example"></s:text> </strong></td>
				<td width="64%">&nbsp;</td>
			</tr>

			<tr>
				<td><s:form id="appExampleForm">
					<table>
						<tr>
							<td height="30" style="padding-top: 5px;"><s:textfield
								name="applicationExample.queryName" id="example" /></td>
							<td><input type="button" value="Submit" class="singleButton"
								onclick="saveAppExmaple();" /></td>
							<td><input type="hidden"
								name="applicationExample.application.id"
								value="<s:property value='application.id'/>"
								id="applicationExampleAppId" /></td>
							<td><input type="hidden" name="applicationExample.id"
								id="applicationExampleId" /></td>
							<td><input type="hidden" name="applicationExample.type"
								id="userInterfaceTypeId" /></td>
						</tr>
					</table>
				</s:form></td>
			</tr>
			<tr>
				<td width="36%" colspan="2" style="padding-top: 10px;">
				<div id="alertMessaage" style="display: none;color:red;padding-left: 3px;"></div>
				</td>
			</tr>
			<tr>
				<td width="36%" colspan="2" style="padding-top: 10px;">
				<div id="snapShot">
				<ul>

					<s:iterator value="application.appQueryExamples">
						<li>
						<div style="float: left; width: 20px; padding-top: 3px;">&bull;</div>
						<div style="float: left; width: auto;"><a href="#"
							onclick="setAppExampleValue('<s:property value="queryName"/>',<s:property value="id"/>,'<s:property value="type"/>')"><s:property
							value="queryName" /></a></div>
						<div class='snapBit' title='Delete' alt='Delete'
							onclick='deleteAppExample(<s:property value="id"/>,<s:property value="application.id"/>);'>&nbsp;</div>
						</li>

					</s:iterator>
				</ul>
				</div>

				</td>
			</tr>
		</table>




		</td>
	</tr>

	<tr>
		<td valign="top">&nbsp;</td>
		<td valign="top"
			style="border-left: 1px dashed #CCC; padding-left: 20px;">&nbsp;</td>
	</tr>
</table>

<script type="text/javascript"
	src="../js/admin/jquery.execue.jobStatus.js"></script> <script>

$(document).ready(function (){
		var optDefault=	$("#appSourceTypeId").val();
		
		if(optDefault=="UNSTRUCTURED"){
				$(".unstructuredFields").show();
				var locationFromContent=	'<s:property value="application.unstructuredApplicationDetail.locationFromContent"/>';			
				if(locationFromContent=='YES'){	
				  $("#locationFromContentId").attr('checked',true); 
				  	//alert(locationFromContent);
				}
			}
		$("#appSourceTypeId").bind("change",function(){
			var opt=$(this).val();
			if(opt=="UNSTRUCTURED"){
				$(".unstructuredFields").show();
			}else{
				$(".unstructuredFields").hide();
			}
		});
	 $("#appDefinitionForm").submit(function(){	 //alert($("#enableUpdateApplication").css("display"));
		if($("#enableUpdateApplication").css("display")!="none"){									 
			$("#updateButton").hide();	
			$("#updateProcess").show();
		}
		});
	 

$("#disclaimerForm_uiAssetDetail_extendedDisclaimer").cleditor({height:220});
$("#noteForm_uiAssetDetail_extendedNote").cleditor({height:220});
 $("#disclaimerDiv").hide();     
 $("#notesDiv").hide();
 $("#helpLink").helpMessage({messageKey:"execue.app.help.name"});
 $("#publishHelpLink").helpMessage({messageKey:"execue.app.help.publish"});
$("#showStatus").empty();

// var staticUrl = "http://wiki.semantifi.com/index.php/";
/*$.getJSON("getWikiUrl.action", function(data){
	$("#appUrl").val(data.applicationURL);
	staticUrl = data.applicationURL;
});*/

	/*
	if($("#appUrl").val().length < 1){
		if($("#appName").val().length > 1){
			//in edit mode but the url is still empty, so compose it.
			$("#appUrl").val(staticUrl+$("#appName").val().replace(/ /g,"_")); 
		} else {
			$("#appUrl").val(staticUrl);
		}
	}
	$("#appName").bind("keyup", function(){
		$("#appUrl").val(staticUrl+$("#appName").val().replace(/ /g,"_"));
	});
	*/ // NOTE: -RG- Do not override any thing on appUrl, as it is pointing to search app's app home page rather than wiki Url
	
	$("#example").keydown(function(e){
								   
		   if(e.keyCode==13){
			saveAppExmaple();
			return false;
		   }
	});
	
	var splCharExist=false;
	$("#appName").bind("blur",function(e){
				   
	splCharExist=checkSpecialChar($(this));
			if(splCharExist){
				$("#createLoader").hide();
				var top=$(this).position().top;
				var left=$(this).position().left;
				$("#appNameMessage").css("top",top-8+"px").show();
				$("#appNameMessage").css("left",left+$(this).width()+10+"px");
				$(this).select().focus();
				
				tempField = $(this);
				setTimeout("tempField.select().focus();",1);
				return false;

			}else{
				$("#appNameMessage").hide();
			}
     });
	
});

mode='<s:property value="#mode"/>';
$(function(){
 checkMode();

});

// TODO: -SS- complete the wiki integration part for all the browsers.
function createWikiPage(){
	$.ajax({
		url: "http://wiki.semantifi.com/api.php?action=query&titles="+$("#appName").val()+"&format=jsonfm", 
		type: "GET",
		dataType: "jsonp",
		xhr: function(xhr){	xhr.setRequestHeader("User-Agent","Mozilla");},
		success: function(data,status,request){ $("#fillit").append(data); alert(status);},
		error: function(data){ $("#fillit").append(data); },
	});
}

function updateWikiPage(){
}

function checkMode(){
	$("#createLoader").hide();	
	$("#updateButton").show();	
if(mode=='update'){
    $("#enableUpdateApplication").show(); 
    $("#deleteAppButton").show();    
    $("#enableCreateApplication").hide();
    $("#resetButton").hide();
	$("#updateProcess").hide();
	$("#dynamicImageOuterDivBorder").show();	
	$("#appSourceTypeId").attr("disabled", true);
 }else{
    $("#enableUpdateApplication").hide(); 
    $("#deleteAppButton").hide();    
    $("#enableCreateApplication").show();
    $("#resetButton").show();
	$("#createProcess").hide();
	
 }	
}

function showApplicationURLLink () {
	var appLink = "<c:out value='${searchPath}'/>"+$("#appUrl").val();
	var linkTag = "<a href='"+appLink+"'>"+$("#appName").val()+"</a>";
	$("#applicationURLLinkRow").show();
	$("#applicationURLLink").empty().append(linkTag);
}

function showHideUpdate(){ 
	if(!splCharExist){
		$("#showStatus").empty();
		$("#enableUpdateApplication").hide();
		$("#updateButton").hide();	
		$("#updateProcess").show();
		$("#createProcess").hide();		
		return true;
	}
	else{
		$("#updateButton").show();			
		$("#updateProcess").hide();	
		return false;
	}
}
function showHideCreate(){
$("#createLoader").show();	
	
}
function checkName(){
appname=$("#appName");
	var splCharExist=checkSpecialChar(appname);
			if(!splCharExist){
				if(mode!='update'){
				$("#enableCreateApplication").hide();
				$("#createProcess").show();
				}
			}else{
			return false;	
			}
			return true;
}
function clearMessage(){
	$("#appNameMessage").hide();
}


function showForm(name){	
	$("#"+name).slideDown();
	$("#"+name+"Img").attr("src","../images/admin/bullet_toggle_minus.png");
	$("."+name+"Link").attr("href","javascript:closeForm('"+name+"');");	
}

function closeForm(name){	
	$("#"+name).slideUp();
	$("#"+name+"Img").attr("src","../images/admin/bullet_toggle_plus.png");
	$("."+name+"Link").attr("href","javascript:showForm('"+name+"');");
	
}

function saveDisclaimer(){
var disclaimer=$("#extendedDisclaimerTextId").val();
  if(isEmpty(disclaimer)){
		$("#disclaimerMessageErrorDiv").html("Please enter disclaimer");
		$("#disclaimerMessageErrorDiv").show();
		setTimeout(function(){$("#disclaimerMessageErrorDiv").hide();},3000);
		return false;
	}
  var disclaimer = $("#disclaimerForm").serialize();
   $("#disclaimerMessageDiv").empty().append("hello");
	setTimeout(function(){$("#disclaimerMessageDiv").hide();},3000);
   $("#disclaimerButton").hide();
   $("#disclaimerButtonImg").show();
  $.post("../qi/saveAjaxDisclaimer.action", disclaimer, function(data) {
       //alert(data.message);
	    $("#disclaimerMessageDiv").empty().append(data.message);
		setTimeout(function(){$("#disclaimerMessageDiv").hide();},3000);
	    $("#disclaimerButton").show();
        $("#disclaimerButtonImg").hide();
  });
}
function saveNote(){
 var note=$("#extendedNoteTextId").val();
  if(isEmpty(note)){
		$("#notesMessageErrorDiv").html("Please enter note");
		$("#notesMessageErrorDiv").show();
		setTimeout(function(){$("#notesMessageErrorDiv").hide();},3000);
		return false;
	}	
var notes=$('#noteForm').serialize(); 
   $("#noteButton").hide();
   $("#noteButtonImg").show();
  $.post("../qi/saveAjaxNotes.action", notes, function(data) {
      // alert(data.message);
	  $("#notesMessageDiv").empty().append(data.message);
	  setTimeout(function(){$("#notesMessageDiv").hide();},3000);
	    $("#noteButton").show();
   $("#noteButtonImg").hide();
  });
}

function changePublishMode() {
	   if ($("#appPublishModeId").is(":checked")) {
		   $("#appPublishModeId").val("COMMUNITY");
	   } else {
		   $("#appPublishModeId").val("LOCAL");
	   }
}
</script>