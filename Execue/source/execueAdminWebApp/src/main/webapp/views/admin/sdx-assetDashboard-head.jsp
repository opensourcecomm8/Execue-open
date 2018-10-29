<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet type="text/css">
<link rel="stylesheet" type="text/css" media="screen"
	href="../css/admin/jquery-ui-admin-1.7.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="../css/common/ui.jqgrid.css" />
<style type="text">
   html, body {
   margin: 0;			/* Remove body margin/padding */
   padding: 0;
   overflow: hidden;	/* Remove scroll bars on browser window */
   font-size: 75%;
   }
</style>

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/common/jquery.js" language="JavaScript"></script>
<script src="../js/admin/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script src="../js/common/i18n/grid.locale-en.js" type="text/javascript"></script>
<script src="../js/common/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="../js/common/jquery.layout.js" type="text/javascript"></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript"></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>

<script type="text/javascript" src="../js/admin/jquery.execue.jobStatus.js"></script>

<script type="text/javascript">
var paginationType="availableAssets";
var fileIdPrcessing="";
$(document).ready(function() { 
	//showSearch('divGrain');
    // showAssets();
	
});


/*$(document).ajaxError(function(e,xhr,settings) {
		//$("#assetTableList").text("unable to process");
		//$("#loadingATLink").hide();
		alert(":"+settings.url);
		

       });
*/
/*
 * Populate the default population and deafult distribution select options 
 *   from the selected options of eligible grain
 * Maintain the previous selection from default population and deafult distribution
 *   if the previously selected item still exists in the newly populated options
 */  


function createSource(fileId){
	//$("#uploadedFilesForm_"+fileId).submit();
	fileIdPrcessing=fileId;
	$("#"+fileId+"Div").empty();
	$("#"+fileId+"Div").jobStatus({
		requestJobURL : "../publisher/invokeUploadedFileAbsorption.action",
		postDataForJobParam : function () {
				var data = {};
				data.publishedFileInfoId = fileId;
				return data;
				},
		postCall : function(jobId) {updatePublishedFileInfo(fileId,jobId)}
	});
	$("#showStatus"+fileId).hide();
}

function showStatus(fileId, jobRequestId){
  $("#"+fileId+"Div").empty();
  $("#"+fileId+"Div").jobStatus({
      jobId: jobRequestId,
      postCall : function(jobId) {updatePublishedFileInfo(fileId,jobId)}
  });
  $("#showStatus"+fileId).hide();
}

function updatePublishedFileInfo(fileId,jobId) {
	// specifc code for showing specific
	$pubDiv= $("#showStatus"+fileId);
	$pubDiv.show();
	$pubDiv.empty()
		   .append($("<a>").html("<s:text name='execue.assetDashboard.uploadFile.confirm' />").attr("href","../publisher/showPublishedFileTables.action?publishedFileId="+fileId+"&jobRequestId="+jobId));
}
function deleteDatasetCollection(fileId){
	var confir=confirm("Are you sure you want to delete dataset ?");
	if(confir){
	$("#showStatus"+fileId+" img").attr("src","../images/admin/loaderTrans.gif");
	$("#showStatus"+fileId+" img").css("width","16px");
	$("#showStatus"+fileId+" img").css("height","16px");
	$.post("../publisher/deleteUploadedDataset.action",{"publishedFileInfoId" : fileId},function(data){
	var obj = data; //eval("(" + data + ')');
	   if(obj.errorMessages){
	   if(obj.errorMessages.length > 1){
	      $.each(obj.errorMessages, function () {
		      $("#message").append("<li>"+$(this)+"<\li>");
		       setTimeout(function(){$("#message").empty();},2000);
               $("#message").css("color","red");
               $("#message").show();  
		    });
		   }else{
		      $("#message").append("<li>"+obj.errorMessages+"<\li>");
		       setTimeout(function(){$("#message").empty();},2000);
               $("#message").css("color","red");
               $("#message").show();  
		   }
	   }else if(obj.messages){
	   if(obj.messages.length > 1){
	      $.each(obj.messages, function () {
		      $("#message").append("<li>"+$(this)+"<\li>");
		       setTimeout(function(){$("#message").empty();},2000);
               $("#message").css("color","red");
               $("#message").show();  
		    });
	   }else{
	      $("#message").append("<li>"+obj.messages+"<\li>");
		       setTimeout(function(){$("#message").empty();},2000);
               $("#message").css("color","red");
               $("#message").show(); 
	      }	     
	   }
	   setTimeout(function(){generateGrid();},5000);
	    
	},"json");
	}
}


</script>
