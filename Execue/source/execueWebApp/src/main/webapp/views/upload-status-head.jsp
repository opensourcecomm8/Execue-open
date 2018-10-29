<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet>

<script src="../js/jquery.js" language="JavaScript" /></script>
<script src="../js/menun.js" language="JavaScript" /></script>
<script src="../js/mm_menu.js" language="JavaScript" /></script>
<script src="../js/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/searchList.js" type="text/javascript"
	language="javascript" /></script>

<script type="text/javascript"><!--


var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

var refreshCycle;

$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  
  showStatusDetails ();

});

function showStatusScreen(){
	$("#statusPaneDiv").show();
	$("#detailsDiv").hide();
}
function closeStatusScreen(){
	$("#statusPaneDiv").hide();
	$("#detailsDiv").show();
}
function showStatusDetails () {
    var jobRequestId = $("#jobRequestId").val();
    var jobRequestStatus = $("#jobRequestStatus").val();
    if (jobRequestStatus == 'SUCCESS' || jobRequestStatus == 'FAILURE') {
        clearTimeout(refreshCycle);
    } else {
        $.post("../publisher/showUploadStatusDetails.action?jobRequest.id="+jobRequestId, {}, function(data) {
          $("#statusDetails").fadeIn("fast");
          $("#statusDetails").empty();
          $("#statusDetails").append(data);
          $("#statusDetails").show();
        });
        refreshCycle = setTimeout("showStatusDetails()", 30000);
    }
}

function showAssetScreen () {
    var assetId = $("#assetId").val();
    var assetName = $("#assetName").val();
    if (assetId == '') {
        document.location = "../swi/showAssets.action";
    } else {
        document.location = "../swi/showAsset.action?asset.id="+assetId+"&asset.name="+assetName;
    }
}

function showConfirmationScreen () {
    var fileInfoId = $("#fileInfoId").val();
    var jobRequestId = $("#jobRequestId").val();
    document.location = "../publisher/showPublishedFileTables.action?publishedFileId="+fileInfoId+"&jobRequestId="+jobRequestId;
}

function showConsoleHome () {
    document.location = "../swi/showConsole.action";
}

--></script>
