<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" type="text/css" media="screen"
	href="../css/redmond/jquery-ui-1.7.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="../css/ui.jqgrid.css" />
<style type="text">
   html, body {
   margin: 0;			/* Remove body margin/padding */
   padding: 0;
   overflow: hidden;	/* Remove scroll bars on browser window */
   font-size: 75%;
   }
</style>
<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../js/i18n/grid.locale-en.js" type="text/javascript"></script>
<script src="../js/jquery.jqGrid.min.js" type="text/javascript"></script>
<!--  TODO: remove the loader.js after all the js files have been merged into one min file --> 
<script src="../js/jquery.execue.upload.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/jquery.execue.jobStatus.js"></script>
<script type="text/javascript">
var paginationType="availableAssets";
$(document).ready(function() { 
	var applIdFromSession = '<s:property value="applicationContext.appId"/>';
	var modIdFromSession = '<s:property value="applicationContext.modelId"/>';	
		getUploadDatasetInfo();
	if(applIdFromSession){
		$.each($("input[name='applicationId']"),function(k,v){
			if($(this).attr("value")==applIdFromSession){
				$(this).attr("checked",true);				
			}
		});
	} 
	/*if($("input[name='applicationId']")){
		$("input[name='applicationId']").bind("click", function(){
			// no need to reload the page any more.
			setAppInfo(true);
		});
	}*/
	
	$("a[name='appEdit']").click(function(){
						  
		$radioSelected=$(this).parent().prev().children();
		$radioSelected.attr("checked",true);
	});
});
function createAppFromFile(){
	$("#searchAppDashboardDiv").hide();
	$("#uploadFileInfoOuterDiv").show();
	
	$("#uploadFileInfo").slideDown("slow");

	$("#createAppFromFileLink").removeAttr("href").css("color","#000");

	$("#uploadFileInfo").fadeIn("fast");
    $("#uploadFileInfo").show();
	//$("#closeFileUpload").show();
}
function closeFileUpload(){
	$("#createAppFromFileLink").attr("href","#").css("color","#003399");
	$("#uploadFileInfo").slideUp("slow");
}
function getUploadDatasetInfo() {
 var publisherProcessType='<s:property value="publisherProcessType"/>';
 var url='';
     url="../publisher/showSimplifiedUpload.action?publisherProcessType=SIMPLIFIED_PUBLISHER_PROCESS";
  $.post(url,{}, function(data) { 
    
    $("#uploadFileInfo").empty();
    $("#uploadFileInfo").append(data);
    
    /* set the form to be submitted like ajax for uploading file(s) */
    ajaxUpload("uploadCSV", "execueBody", "../publisher/uploadCSV.action");
   // showDSConnection();
  });  
}


function setSelected(applId, appName, modId, appSrcType){
		if(applId > 0){
			$.post("../swi/setAppInfo.action",{applicationId:applId, modelId:modId, applicationName:appName, appSourceType:appSrcType});
			$("#appSelectedMessageDiv").text(appName+" App selected successfully");
			$("#appSelectedMessageDiv").slideDown('normal');
			$("#pleaseSelectApplication").empty();
		}
}
function deleteApp (deleteMessage, appId){
	confirmed = confirm(deleteMessage);
	if (confirmed) {
		$("#"+appId+"Div").empty();
		$("#showStatus"+appId).empty();
		$("#showStatus"+appId).jobStatus({
			requestJobURL : "../swi/deleteAppJob.action?application.id=" + appId,
			postDataForJobParam : function(){var data = {}; return data;},
			postCall : function(jobId, status) {updatePublishedFileInfo(appId, status)}
		});
	}
}

function updatePublishedFileInfo(appId, status) {
	$("#appSelectedMessageDiv").text("");
	$pubDiv = $("#showStatus"+appId);
	$pubDiv.show();
	if(status == "SUCCESS"){
		$pubDiv.empty().append("App Deleted Sucessfully");
		setTimeout(function(){$("#grid").delRowData( appId );  $("#grid").prev("div").css("height","0px"); 	},3000);
	}
	else if(status == "FAILURE")
		$pubDiv.empty().append("App Deletion Failed");
}
/*function setAppInfo(fromChangeEvent){
	var applIdFromSession = '<s:property value="applicationContext.appId"/>';
	var modIdFromSession = '<s:property value="applicationContext.modelId"/>';	
	var applId = "";
	var modId = "";
	
	if (fromChangeEvent){
		// a change event from the select box has been triggered.
		$radioSelected = $("input[name='applicationId']:checked");
		setSelected($radioSelected)
	}
}*/
function handleUploadRequest () {
  $("#errorMessage").empty(); 
 $("#uploadButtonSpan").hide();
 $("#uploadButtonSpanLoader").show();
} // End of handleUploadRequest()

/*
 * handling the response for upload file functionality 
 */
function handleUploadResponse (returnedData) {

  if (returnedData.status) {

    $("#uploadCsvDynamicContent").empty();
    $("#uploadCsvDynamicContent").hide();

	var htmlContent = buildResponse(returnedData);
	
	$("#uploadCsvDynamicContent").css("paddingLeft","10px").append(htmlContent);
    $("#uploadCsvDynamicContent").show();
	if (returnedData.absorbAsset) {
	checkAbsorptionStatus();
	}

  } else {

    $.each(returnedData.errorMessages, function () {
      $("#errorMessage").append("<li>"+this+"<\li>");
	     $("#uploadButtonSpan").show();
         $("#uploadButtonSpanLoader").hide();
    });

  }

} // end of handleUploadResponse()

function checkAbsorptionStatus () {
  var jobRequestId = $("#jobRequestId").val();
  var $checkAbsorbtionStatus=$('<div id="checkAbsorbtionStatusDiv" style="width:150px;float:left;display:none;margin-top:5px;margin-bottom:5px;"><a href="javascript:checkAbsorptionStatus();"  >Check Absobtion Status </a></div>');
  
  var $showAbsorbtionStatus=$('<div id="showAbsorbtionStatusDiv" style="background-color:#FFF;width:600px;float:left;margin-top:5px;margin-bottom:5px;display:none;"></div>');
  
 $("#errorMessageOuter").append($checkAbsorbtionStatus);
  $showAbsorbtionStatus.insertAfter($checkAbsorbtionStatus);
  
  $("#checkAbsorbtionStatus").hide();
  $("#checkAbsorbtionStatusDiv").hide();
  $("#uploadAnotherFile").hide();
  $("#showAbsorbtionStatusDiv").empty();
  // showUploadStatus
  //location.href="../publisher/showUploadStatus.action?jobRequest.id="+jobRequestId;
  $("#showAbsorbtionStatusDiv").jobStatus({
		//requestJobURL : "../swi/invokePublishAssetsMaintenaceJob.action",
		jobId:jobRequestId,
		/*postDataForJobParam : function () {
				var data = {};
				data.selectedAssetId = assetId;
				data.publishMode = publishMode;
				return data;
				},*/
		postCall : function(jobRequestId,status) {updateAbsorbtionInfo(jobRequestId,status)}
	});
  
} // end of checkAbsorptionStatus()
function updateAbsorbtionInfo(jobid,status) {
	// specifc code for showing specific
	//alert(status);
	if(status=="SUCCESS"){
	$pubDiv= $("#showAbsorbtionStatusDiv").css("textAlign","left");
	$pubDiv.show();
	//$pubDiv.empty()
		   //.append($("<a>").html("Metadata Screen").attr("href","javascript:showConfirmationScreen()"));
		   
	showConfirmationScreen();
	}else{
	$("#checkAbsorbtionStatusDiv").show();
	}
	//$("#uploadAnotherFile").show().css({marginLeft:"0px"});
}
function showConfirmationScreen() {
    var fileInfoId = $("#publishedFileInfoId").val();
    var jobRequestId = $("#jobRequestId").val();
    document.location = "../publisher/showPublishedFileTables.action?publishedFileId="+fileInfoId+"&jobRequestId="+jobRequestId;
}
function showAllUploadedFiles () {
  location.href="../swi/showAssetsDashboard.action";
} // end of showAllUploadedFiles()

function buildResponse(returnedData) {
	var htmlContent = staticMsga;
	$.each(returnedData.messages, function () {
	  htmlContent = htmlContent + "<li><span class='green'>"+this+"<\span><\li>";
	});
	htmlContent = htmlContent + staticMsgb;
	htmlContent = htmlContent + static1 + tableStart  + staticNamea;
	htmlContent = htmlContent + returnedData.fileName;
	htmlContent = htmlContent + staticNameb + tableEnd + static2 + static3 ;

	htmlContent = htmlContent + staticFilea;
  if (returnedData.sourceDataFileName) {
      htmlContent = htmlContent + returnedData.sourceDataFileName;
  } else {
  htmlContent = htmlContent + "Not Provided";
  }
  htmlContent = htmlContent + staticFileb;

  htmlContent = htmlContent + staticURLb + staticSourceTypea;
  if(returnedData.isCompressedFile=="YES"){
	  htmlContent = htmlContent + returnedData.sourceType + " (ZIP)";
	}else{
	  htmlContent = htmlContent + returnedData.sourceType;
  }	
  htmlContent = htmlContent + staticSourceTypeb;

	/* + staticDesca;
	if(returnedData.fielDescription){
	  htmlContent = htmlContent + returnedData.fielDescription;
	}else{
	  htmlContent = htmlContent + " ";
	}
	htmlContent = htmlContent + staticDescb;
  
	htmlContent = htmlContent + staticTagsa;
	if (returnedData.tag) {
		htmlContent = htmlContent + returnedData.tag;
	} else {
	  htmlContent = htmlContent + "Not Provided";
	}
	htmlContent = htmlContent + staticTagsb;

	if(!returnedData.applicationName){
	   returnedData.applicationName=" ";
	}
	if (returnedData.absorbAsset) {
	  htmlContent = htmlContent + staticAppa + returnedData.applicationName + staticAppb;
	}
	*/
	htmlContent = htmlContent + static8 + static9;

	

	/* + staticURLa;
	if (returnedData.fileURL) {
		htmlContent = htmlContent + returnedData.fileURL;
	} else {
			htmlContent = htmlContent + "Not Provided";
	}
	*/
	
	htmlContent = htmlContent + staticColumnNamesSpecifieda;
	htmlContent = htmlContent + returnedData.columnNamesPresent;
	htmlContent = htmlContent + staticColumnNamesSpecifiedb + staticStringsQuotea;
	htmlContent = htmlContent + returnedData.stringEnclosure;
	htmlContent = htmlContent + staticStringsQuoteb + staticNullValuesa;
	htmlContent = htmlContent + returnedData.nullIdentifier;
	htmlContent = htmlContent + staticNullValuesb;

	/* + staticAbsorbChecka;
	if (returnedData.absorbAsset) {
	  htmlContent = htmlContent + "Yes";
	} else {
	  htmlContent = htmlContent + "No";
	}
	htmlContent = htmlContent + staticAbsorbCheckb;
    */
    
	htmlContent = htmlContent + static20 + static21;
	
	htmlContent = htmlContent + staticButton1;
	if (returnedData.absorbAsset) {
	  htmlContent = htmlContent + staticCheckStatusButton;
	} else {
	  htmlContent = htmlContent + staticAllFilesButton;
	}
	htmlContent = htmlContent + staticUploadFileButton + staticButton2;
	
	htmlContent = htmlContent + static22;
	
	htmlContent = htmlContent + staticHidden1a;
	htmlContent = htmlContent + returnedData.jobRequestId;
	htmlContent = htmlContent + staticHidden1b;
	
	htmlContent = htmlContent + staticHidden2a;
	htmlContent = htmlContent + returnedData.publishedFileInfoId;
	htmlContent = htmlContent + staticHidden2b + staticEnd;
	return htmlContent;
}


/* Static content declaration Start */

var staticMsga = "<div id='errorMessageOuter'><div id='errorMessage' style='padding-top:5px;'>";
var staticMsgb = "</div></div>";
var static1 = "<div style='width: 100%; white-space: nowrap; min-height: 60px; height: auto;' class='innerPane'> <form name='uploadStatusDetails' style='float:left;'>";
var tableStart="<table width='120' cellspacing='0' cellpadding='0' border='0' style='float:left;'>";
var tableEnd="</table>";
var static2 = "<br/><table id='fileDetailsDiv' align='left' width='70%' cellspacing='0' cellpadding='0' border='0' style='display:none;'>";
var static3 = "<tr><td width='43%' valign='top' align='right'><table width='70%' cellspacing='0' cellpadding='3' border='0' align='left'><tbody>";
var staticNamea = "<tr><td width='66%' class='fieldNames'>Name :</td><td width='34%' style='padding-left:5px;'>";
var staticNameb = "</td><td style='padding-left:10px;'><a id='moreLinkDiv' href='javascript:showMoreDetails();'>More</a></td></tr>";
var staticDesca = "<tr><td class='fieldNames'>Description :</td><td>";
var staticDescb = "</td></tr>";
var staticTagsa = "<tr><td class='fieldNames'>Tags :</td><td>";
var staticTagsb = "</td></tr>";
var staticAppa = "<tr id='applicationSelectId'><td class='fieldNames'>Application :</td><td>";
var staticAppb = "</td></tr>";
var static8 = "</tbody></table></td><td width='57%' valign='top' align='left'><table width='80%' cellspacing='0' cellpadding='3' border='0' align='left' style='margin-left:20px;'>";
var static9 = "<tbody><tr><td ><table border='0'><tbody>";
var staticFilea = "<tr><td class='fieldNames'>Source File :</td><td>";
var staticFileb = "</td></tr>";
var staticURLa = "<tr><td class='fieldNames'>Source URL :</td><td>";
var staticURLb = "</td></tr>";
var staticSourceTypea = "<tr><td class='fieldNames'>Source Type :</td><td>";
var staticSourceTypeb = "</td></tr>";
var staticColumnNamesSpecifieda = "<tr><td width='71%' class='fieldNames'>Columns Specified :</td><td width='29%'>";
var staticColumnNamesSpecifiedb = "</td></tr>";
var staticStringsQuotea = "<tr><td class='fieldNames'>Strings Quoted With :</td><td>";
var staticStringsQuoteb = "</td></tr>";
var staticNullValuesa = "<tr><td class='fieldNames'>NULL Values Represented As :</td><td>";
var staticNullValuesb = "</td></tr>";
var staticAbsorbChecka = "<tr><td class='fieldNames'>Absorb Dataset Requested :</td><td>";
var staticAbsorbCheckb = "</td></tr>";
var static20 = "</tbody></table></td></tr>";
var static21 = "</tbody></table></td></tr>";

var staticButton1 = "<tr><td valign='bottom' colspan='2' align='left'>";
var staticCheckStatusButton = "<input type='button' value='Check Abosorption Status' id='checkAbsorbtionStatus' name='imageField' class='singleButton' onclick='javascript:checkAbsorptionStatus();'>&nbsp;";
var staticAllFilesButton = "<input type='button' value='View Uploaded Files' id='imageField' name='imageField' class='singleButton' onclick='javascript:showAllUploadedFiles();'>";
var staticUploadFileButton = "&nbsp;<input type='button' value='Upload Another File' id='uploadAnotherFile' name='imageField2' class='singleButton' style='margin-left:10px;' onclick='javascript:getUploadDatasetInfo();'>";
var staticButton2 = "</td></tr>";

var static21a = "</td><td align='left' valign='bottom'>";

var static22 = "</tbody></table>";

var staticHidden1a = "<input type='hidden' id='jobRequestId' name='jobRequestId' value='";
var staticHidden1b = "'>";

var staticHidden2a = "<input type='hidden' id='publishedFileInfoId' name='publishedFileInfoId' value='";
var staticHidden2b = "'>";

var staticEnd = "</form></div>";

/* Static content declaration End */

function showMoreDetails(){
	if($("#moreLinkDiv").text()=="More"){
	$("#fileDetailsDiv").slideDown("slow");	
	$("#moreLinkDiv").text("Less");
	}else{
		$("#fileDetailsDiv").slideUp("slow");	
	$("#moreLinkDiv").text("More");
	}
}
</script>