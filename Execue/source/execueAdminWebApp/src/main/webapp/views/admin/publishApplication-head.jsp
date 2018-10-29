<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>


<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/admin/jquery.layout2.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>
    
<script src="../js/common/jquery.layout.js" type="text/javascript"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.jobStatus.js"></script>    
<script type="text/javascript">
var myLayout;
$(document).ready(function() {

  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
	//,	west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
  });
    showHideSelectDataset();
    $mode='<s:property value="publishMode"/>';
    setAssetValues(); 
	 $("#selectedAssetId").change(function(){ 
	     showAssetAnalysisReportLink();
	 });
	

});

function showHideSelectDataset(){
var assetsSize ='<s:property value="assets.size"/>';
var appSourceType ='<s:property value="appSourceType.value"/>'; 
//if($("#selectedAssetId option").length>0){
	if(assetsSize==0 && appSourceType == 'S'){
		$("#datasetDetailsDiv").hide();
		$("#datasetDetailsMessageDiv").show();
	}else{
		$("#datasetDetailsDiv").show();
		$("#datasetDetailsMessageDiv").hide();
	}
}
function setAssetValues(){
	$assetId=$("#selectedAssetId option:selected").val();
	$publishMode=$("#selectedAssetId option:selected").attr("publishMode");
	if($mode){
	  $publishMode=$mode;
	}
	if($publishMode=='COMMUNITY'){
	 $("#uploadCSV_columnNamesPresent2").attr("checked",true);
	}else{
	 $("#uploadCSV_columnNamesPresent").attr("checked",true);
	}
}

function showAssetAnalysisReportLink(){
    $assetId=$("#selectedAssetId option:selected").val();
    //TODO:JTiwari Commeted this section for now
    /*$.getJSON("getAssetAnalysisReportByAssetId.action?selectedAssetId="+$assetId,function(data){
      $("#publisherReportDiv").empty();
	    if(data=="YES"){	      
	       $("#publisherReportDiv").append( "<a onclick='javascript:getAssetAnalysisReport("+$assetId+")' href='#'>View Dataset collection Analysis Report</a>");
	       $("#publisherReportDiv").show();
	    }
	});*/   
    $publishMode=$("#selectedAssetId option:selected").attr("publishMode");
	if($publishMode=='COMMUNITY'){
	 $("#uploadCSV_columnNamesPresent2").attr("checked",true);
	}else{
	 $("#uploadCSV_columnNamesPresent").attr("checked",true);
	}
}

function getAssetAnalysisReport(assetId){
	$checkedPublishMode=$("input[name='publishMode']:checked").val(); 
	window.location= "showExistingReport.action?selectedAssetId="+ $assetId+ "&publishMode="+$checkedPublishMode;
}


function publishDatasetCollection(){
    // NOTE: -RG- Terms of conditions is taken off
	// var check=checkTerms();
	// if(!check){return false;}
	 
  var appName=$("#appNameId").val();
  var assetId=$("#selectedAssetId").val();
  //var publishMode=$("input[type='radio']:checked").val();
  var publishMode=$("input[name='publishMode']:checked").val(); 
  var assetAnalysisReport=$("input#runAssetAnalysisReoprtId:checked").val();
 if(assetAnalysisReport){
   $.getJSON("generateAssetAnalysisReport.action?selectedAssetId="+assetId+"&publishMode="+publishMode,function(data){ 
	    if(data=="YES"){	      
	      getAssetAnalysisReport(assetId,publishMode);
	    }else{
	      publish(assetId,publishMode,appName);
	    }
	});	
 }else{ 
    publish(assetId,publishMode,appName);
 }
	
} 
function publish(assetId,publishMode,appName){   
    $("#publishAppButtonDiv").hide();
    $("#publisherStatusDiv").empty();
	$("#publisherStatusDiv").jobStatus({
		requestJobURL : "../swi/invokePublishAssetsMaintenanceJob.action",
		postDataForJobParam : function () {
				var data = {};
				data.selectedAssetId = assetId;
				data.publishMode = publishMode;
				data.appName =appName;
				return data;
				},
		postCall : function(jobid,status) {updatePublishedFileInfo(jobid,status)}
	});
	$("#absorbtionDiv").hide();
	
}
function updatePublishedFileInfo(jobid,status) {
	// specifc code for showing specific
	//alert(status);
	if(status=="SUCCESS"){
	$pubDiv= $("#absorbtionDiv");
	$pubDiv.show();
	$pubDiv.empty()
		   .html("Dataset collection published successfully");
	}
	$("#publishAppButtonDiv").show();
}
</script>
