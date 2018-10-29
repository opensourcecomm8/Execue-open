<%@page import="com.execue.core.common.type.PublisherProcessType"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/common/jquery.js" language="JavaScript"></script>
<script src="../js/admin/menun.js" language="JavaScript"></script>
<script src="../js/common/mm_menu.js" language="JavaScript"></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/common/jquery.layout.js" type="text/javascript"></script>
<script src="../js/admin/jquery.execue.upload.js" type="text/javascript"></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.jobStatus.js"></script>
<script src="../js/admin/jquery.execue.helpMessage.js" ></script>

<style>
.nolink {
	text-decoration: none;
	color: gray;
	cursor: text;
	a: active :                         gray
}
</style>
<%
   String pageToDisplay = request.getParameter("show");
   if (pageToDisplay == null) {
      pageToDisplay = "";
   }
%>

<script type="text/javascript">

var TabbedPanels1;

var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  
  //Tabbed Panels should only be defined when the corresponding div is loaded
  TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1"); 
  getUploadDatasetInfo();
  <% if(pageToDisplay.equals("connect")){
	
%>
   TabbedPanels1.showPanel(1);
   <% }else{%>
    TabbedPanels1.showPanel(0);
   <% }%>
});
function showDSConnection(){  
	$.post("../swi/defineDataSource.action",{},function(data){
		 $("#connectionInfo").fadeIn("fast");
         $("#connectionInfo").empty();
         $("#connectionInfo").append(data);
         $("#connectionInfo").show();    
	});
}
function getUploadDatasetInfo(st) {
	if(st){
	showLoaderImageOnLoad();
	}
 var publisherProcessType='<s:property value="publisherProcessType"/>';
 var url='';
 if(publisherProcessType=='<%=PublisherProcessType.SIMPLIFIED_PUBLISHER_PROCESS%>'){
     url="showSimplifiedUpload.action?wizardBased=Y&publisherProcessType="+publisherProcessType;
 }else{
    url="uploadDataset.action?wizardBased=Y"
 } 
  $.post(url,{}, function(data) { 
    $("#uploadFileInfo").fadeIn("fast");
    $("#uploadFileInfo").empty();
    $("#uploadFileInfo").append(data);
    $("#uploadFileInfo").show();
	hideLoaderImageOnLoad();
    /* set the form to be submitted like ajax for uploading file(s) */
    ajaxUpload("uploadCSV", "execueBody", "../publisher/uploadCSV.action");
    showDSConnection();
  });  
}
function getDataSourceDetail() {
  $.post("../swi/showDataSourcesDetails.action",{}, function(data) {
    $("#connectionInfo").fadeIn("fast");
    $("#connectionInfo").empty();
    $("#connectionInfo").append(data);
    $("#connectionInfo").show();    
  });  
}
function showUploadCSVFileInfo(){	
	$.post("../publisher/uploadStatus.action",function(data){
		$("#contentInner").fadeIn("fast");
	    $("#contentInner").empty();
	    $("#contentInner").append(data);
	    $("#contentInner").show();
	});
}
function showDataSourceDeletionConfirmation(dname){
  obj=confirm("Are you sure to delete ?");  
  if(obj){
    url="../swi/deleteDataSource.action?dataSourceName="+dname;  
    $.get(url , function(data) {      
      $("#outerPane").empty();
      $("#outerPane").fadeIn("fast");
      $("#outerPane").append(data);
      $("#outerPane").show();
    });
  }
}
function createConnection(){	
$("#createConnectionButton").hide();
$("#createConnectionButtonLoader").show();
   var connectionForm=$("#createDataSourceForm").serialize();   
	$.post("../swi/createConnection.action",connectionForm,function(data){
		 $("#connectionInfo").fadeIn("fast");
         $("#connectionInfo").empty();
         $("#connectionInfo").append(data);
         $("#connectionInfo").show(); 
		 $("#createConnectionButtonLoader").hide();
		 $("#createConnectionButton").show();
        
	});
}

function showOwner(){
   var selectedProviderType= $("#providerType option:selected").val(); 	   
	  if(selectedProviderType=="Teradata" || selectedProviderType=="SAS_SHARENET" || selectedProviderType=="SAS_WORKSPACE"){
	    $("#dataSourceOwnerId").show();
	  }  
	  $("#providerType").bind("change",function(){
			var opt=$(this).val();			
			if(opt=="Teradata" || opt=="SAS_SHARENET" || opt=="SAS_WORKSPACE" || opt=="MSSql" || opt=="DB2"){
				$("#dataSourceOwnerId").show();
			}else{
				$("#dataSourceOwnerId").hide();
				$("#ownerId").val(null);
			}
		});
}


function onSelectChange(){ 
    var selected = $("#connectionType option:selected"); 
    var output = "";   
	$("#propertiesDiv").hide();
	$("#jndiDiv").hide();
       output = selected.text(); 
    if(output=="Properties"){
	  $("#propertiesDiv").show();	
	}
	if(output=="Jndi"){
		$("#jndiDiv").show();
	}
} 


function resetSelect(){
  var selected = document.getElementById("connectionType").value
  javascript:document.createDataSource.reset();
 		if(selected=="JNDI"){
		$("#propertiesDiv").show()
			$("#jndiDiv").hide();
		}    
 }

/*
 * handle the screen manipulations prior to submit
 */
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
	
	$("#uploadCsvDynamicContent").append(htmlContent);
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
	$pubDiv= $("#showAbsorbtionStatusDiv");
	$pubDiv.show();
	$pubDiv.empty()
		   .append($("<a>").html("Metadata Screen").attr("href","javascript:showConfirmationScreen()"));
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
	showLoaderImageOnLoad();
  location.href="../swi/showAssetsDashboard.action";
} // end of showAllUploadedFiles()

function buildResponse(returnedData) {
	var htmlContent = staticMsga;
	$.each(returnedData.messages, function () {
	  htmlContent = htmlContent + "<li><span class='green'>"+this+"<\span><\li>";
	});
	htmlContent = htmlContent + staticMsgb;
	htmlContent = htmlContent + static1 + static2 + static3 + staticNamea;
	htmlContent = htmlContent + returnedData.fileName;
	htmlContent = htmlContent + staticNameb + staticDesca;
	htmlContent = htmlContent + returnedData.fielDescription;
	htmlContent = htmlContent + staticDescb + staticTagsa;
	if (returnedData.tag) {
		htmlContent = htmlContent + returnedData.tag;
	} else {
	  htmlContent = htmlContent + "Not Provided";
	}
	htmlContent = htmlContent + staticTagsb;
	if (returnedData.absorbAsset) {
	  htmlContent = htmlContent + staticAppa + returnedData.applicationName + staticAppb;
	}
	htmlContent = htmlContent + static8 + static9 + staticFilea;
	
	if (returnedData.sourceDataFileName) {
		htmlContent = htmlContent + returnedData.sourceDataFileName;
	} else {
  	htmlContent = htmlContent + "Not Provided";
	}
	htmlContent = htmlContent + staticFileb + staticURLa;
	
	if (returnedData.fileURL) {
		htmlContent = htmlContent + returnedData.fileURL;
	} else {
			htmlContent = htmlContent + "Not Provided";
	}
	
	htmlContent = htmlContent + staticURLb + staticSourceTypea;
	htmlContent = htmlContent + returnedData.sourceType;
	htmlContent = htmlContent + staticSourceTypeb + staticColumnNamesSpecifieda;
	htmlContent = htmlContent + returnedData.columnNamesPresent;
	htmlContent = htmlContent + staticColumnNamesSpecifiedb + staticStringsQuotea;
	htmlContent = htmlContent + returnedData.stringEnclosure;
	htmlContent = htmlContent + staticStringsQuoteb + staticNullValuesa;
	htmlContent = htmlContent + returnedData.nullIdentifier;
	htmlContent = htmlContent + staticNullValuesb + staticAbsorbChecka;
	if (returnedData.absorbAsset) {
	  htmlContent = htmlContent + "Yes";
	} else {
	  htmlContent = htmlContent + "No";
	}
	htmlContent = htmlContent + staticAbsorbCheckb + static20 + static21;
	
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
var static1 = "<div style='width: 99%; white-space: nowrap; min-height: 200px; height: auto;' class='innerPane'> <form name='uploadStatusDetails' style='float:left;'>";
var static2 = "<table width='70%' cellspacing='0' cellpadding='0' border='0' style='margin-left:20px;'><tbody><tr><td align='right' colspan='2'>&nbsp;</td>";
var static3 = "</tr><tr><td width='43%' valign='top' align='right'><table width='70%' cellspacing='0' cellpadding='3' border='0' align='left'><tbody>";
var staticNamea = "<tr><td width='66%' class='fieldNames'>Name :</td><td width='34%'>";
var staticNameb = "</td></tr>";
var staticDesca = "<tr><td class='fieldNames'>Description :</td><td>";
var staticDescb = "</td></tr>";
var staticTagsa = "<tr><td class='fieldNames'>Tags :</td><td>";
var staticTagsb = "</td></tr>";
var staticAppa = "<tr id='applicationSelectId'><td class='fieldNames'>Application :</td><td>";
var staticAppb = "</td></tr>";
var static8 = "</tbody></table></td><td width='57%' valign='top' align='left'><table width='80%' cellspacing='0' cellpadding='3' border='0' align='left' style='margin-left:20px;'>";
var static9 = "<tbody><tr><td ><div style='width: 100%; border: 0px; padding: 0px; height: 175px;'><table border='0' width='300'><tbody>";
var staticFilea = "<tr><td class='fieldNames'>Source File :</td><td align='left'>";
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
var static20 = "</tbody></table></div></td></tr>";
var static21 = "</tbody></table></td></tr>";

var staticButton1 = "<tr><td valign='bottom' colspan='2' align='left'>";
var staticCheckStatusButton = "<input type='button' value='Check Abosorption Status' id='checkAbsorbtionStatus' name='imageField' class='singleButton' onclick='javascript:checkAbsorptionStatus();'>&nbsp;";
var staticAllFilesButton = "<input type='button' value='View Uploaded Files' id='imageField' name='imageField' class='singleButton' onclick='javascript:showAllUploadedFiles();'>";
var staticUploadFileButton = "&nbsp;<input type='button' value='Upload Another File' id='uploadAnotherFile' name='imageField2' class='singleButton' style='margin-left:10px;' onclick='javascript:getUploadDatasetInfo(true);'>";
var staticButton2 = "</td></tr>";

var static21a = "</td><td align='left' valign='bottom'>";

var static22 = "</tbody></table>";

var staticHidden1a = "<input type='hidden' id='jobRequestId' name='jobRequestId' value='";
var staticHidden1b = "'>";

var staticHidden2a = "<input type='hidden' id='publishedFileInfoId' name='publishedFileInfoId' value='";
var staticHidden2b = "'>";

var staticEnd = "</form></div>";

/* Static content declaration End */

</script>