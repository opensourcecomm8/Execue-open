<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />
<%@ taglib prefix="s" uri="/struts-tags"%>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.jobStatus.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/common/jquery.layout.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.htmlResponse.js"></script>

<script type="text/javascript">
var paginationType="availableAssets";
var path=window.location.href;
var requestedPage=1;
$(document).ready(function() { 
if(path.indexOf("showAssets.action")>-1){
//showAssetsDetails();
//showDetails("swi/viewAssetList.action","dynamicPaneAssets","get");   // new method added in execue.commons.js
$("#dynamicPaneAssets").getAjaxHtmlResponce("swi/viewAssetList.action","","","","get");
}
else if(path.indexOf("showDataSources.action")>-1){
//showDataSorcesDetails();	
paginationType="dataSourcesForPagination"; 
//$("#dynamicPaneDataSources").getAjaxHtmlResponce("swi/showDataSourcesDetails.action?paginationType="+paginationType,"","","","get");
showDetails("swi/showDataSourcesDetails.action?paginationType="+paginationType,"dynamicPaneDataSources","get");  
// new method added in execue.commons.js

}
else if(path.indexOf("showUsers.action")>-1){
//showUserDetails();
paginationType="usersPagination"; 
//$("#dynamicPane").getAjaxHtmlResponce("swi/showUsersList.action?paginationType="+paginationType,"","","","get");
showDetails("swi/showUsersList.action?paginationType="+paginationType,"dynamicPane","get");   // new method added in execue.commons.js
}
/*else if(path.indexOf("showManageUserRequests.action")>-1){
showDetails("swi/showUsersRequests.action","dynamicPane","get");   // new method added in execue.commons.js
}*/
});
//function showDataSorcesDetails(){
//paginationType="dataSourcesForPagination";
  //$.get("swi/showDataSourcesDetails.action?paginationType="+paginationType, function(data) {
  /*$.get("swi/showDataSourcesDetails.action", function(data) {
      $("#dynamicPaneDataSources").empty();
      $("#dynamicPaneDataSources").append(data);
    });*///End of populate BusinessTerms
//}
//function showAssetsDetails(){
	//paginationType="availableAssets"; 
  //$.get("swi/viewAssetList.action?paginationType="+paginationType, function(data) {
 /* $.get("swi/viewAssetList.action", function(data) {
      $("#dynamicPaneAssets").empty();
      $("#dynamicPaneAssets").append(data);
    });*///End of populate BusinessTerms
//}
/*function showUserDetails(){
	paginationType="usersPagination"; 
  $.get("swi/showUsersList.action?paginationType="+paginationType, function(data) {
      $("#dynamicPane").empty();
      $("#dynamicPane").append(data);
    });//End of populate BusinessTerms
}*/

/*function showDataSourceDeletionConfirmation(dname){
  obj=confirm("Are you sure to delete ?");  
  if(obj){
    url="swi/deleteDataSource.action?dataSourceName="+dname;  
    $.get(url , function(data) {      
      $("#outerPane").empty();
      $("#outerPane").fadeIn("fast");
      $("#outerPane").append(data);
      $("#outerPane").show();
    });
  }
}*/

//function showAssetDeletionConfirmation(assetId){
  //obj = confirm("Are you sure you want to delete ?");
  //if (obj) {
    //url="swi/deleteAsset.action?asset.id="+assetId; 
  //  url="swi/deleteAsset.action";
    //params = "asset.id="+assetId+"&applicationId="+$appId;	
   // params = "asset.id="+assetId;
    //$("#outerPane").createInput(url,"showDelete"+assetId+"Link","loadingShowDelete"+assetId+"Link",params); 
   //  document.location="swi/deleteAsset.action?asset.id="+assetId;
    /*$.get(url , function(data) {      
      $("#outerPane").empty();
      $("#outerPane").fadeIn("fast");
      $("#outerPane").append(data);
      $("#outerPane").show();
    });  */
 // }
//}

function showAssetStatus(assetId){
var reqField=$("#requestedPageHiddenField").val();
var requestedPage=1;
if(reqField!=undefined){
	requestedPage=reqField;
}
  var flag= confirm("Do you want change the status?");
  if (flag){
	//params = "asset.id="+assetId+"&applicationId="+$appId;
	params = "asset.id="+assetId+"&paginationType="+paginationType+"&requestedPage="+requestedPage;
    url="swi/updateAssetStatus.action?"+params;
	      
	$("#dynamicPaneAssets").createInput(url,"showStatus"+assetId+"Link","loadingShowStatus"+assetId+"Link",'');
	//$("#dynamicPaneAssets").getAjaxHtmlResponce(url,"showStatus"+assetId+"Link","loadingShowStatus"+assetId+"Link","","get");	
  }
}
//function showWords(){	
	//$params=$("#searchWord").val();
	/*$.post("swi/getWords.action?keyWord.word="+$params, function(data) {	   
    $("#innerPane").empty();    
    $("#innerPane").append(data);
    $("#innerPane").show();  */
	//showDetails("swi/getWords.action?keyWord.word="+$params,"innerPane","post");
   // });  
//}
function subList(index_id){
requestedPage=Number(index_id);
var actionName="";
	
if(path.indexOf("showAssets.action")>-1){
	paginationType="availableAssets";
		actionName="viewSubAssetList.action";
			/*$.post("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
		     $("#dynamicPaneAssets").empty();
             $("#dynamicPaneAssets").append(data);

		  });*/
		//$("#dynamicPaneAssets").getAjaxHtmlResponce("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType,"","","","post");	
			showDetails("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType,"dynamicPaneAssets","post");

}
 if(path.indexOf("showDataSources.action")>-1){
	
	paginationType="dataSourcesForPagination";        

		/*	$.post("swi/showSubDataSources.action?requestedPage="+index_id+"&paginationType="+paginationType+"&dataSource.name=dummy", function(data) {

		     $("#dynamicPaneDataSources").empty();
             $("#dynamicPaneDataSources").append(data);

		  });*/
			//$("#dynamicPaneDataSources").getAjaxHtmlResponce("swi/showSubDataSources.action?requestedPage="+index_id+"&paginationType="+paginationType+"&dataSource.name=dummy","","","","post");	
			showDetails("swi/showSubDataSources.action?requestedPage="+index_id+"&paginationType="+paginationType+"&dataSource.name=dummy","dynamicPaneDataSources","post");
}				
			
if(path.indexOf("showUsers.action")>-1){

	paginationType="usersPagination";        

			/*$.post("swi/showUsersSubList.action?requestedPage="+index_id+"&paginationType="+paginationType+"&dataSource.name=dummy", function(data) {

		     $("#dynamicPane").empty();
             $("#dynamicPane").append(data);

		  });*/
		//$("#dynamicPane").getAjaxHtmlResponce("swi/showUsersSubList.action?requestedPage="+index_id+"&paginationType="+paginationType+"&dataSource.name=dummy","","","","post");	
		$("#showAllUsersLink").attr("href","javascript:subList("+index_id+");");
			$("#closeInnerPaneLink").hide();
			$("#addNewUserLink").show();
			
			showDetails("swi/showUsersSubList.action?requestedPage="+index_id+"&paginationType="+paginationType+"&dataSource.name=dummy","dynamicPane","post");
			
			
}				
}

var screenWidth=screen.width;
var screenHeight=screen.height;
var boxLeft=screenWidth-(screenWidth/2)-200;
var boxTop=screenHeight-(screenHeight/2)-400;
function deleteDataset(assetId){
	confirmed = confirm("Are you sure you want to delete ?");
	<s:property value="assetUnderProcess"/>
	if (confirmed) {
	     $("#loadingShowDelete"+assetId+"Link").hide();
	    $("#showStatus"+assetId).empty().show();  		
		$("#showStatus"+assetId).jobStatus({
				requestJobURL : "../swi/deleteAsset.action?asset.id=" + assetId,
				postDataForJobParam : function(){var data = {}; return data;},
				postCall : function(jobId,status) {
				       updateDeleteInfo(jobId,status,assetId)
				 }
		});
	}
}

function updateDeleteInfo(jobId,status,assetId) {
	$pubDiv = $("#showStatus"+assetId);
	$pubDiv.show();
	if(status == "SUCCESS"){	
		$("#messageDiv").empty().append("<span style='color:green' >Dataset Deleted Sucessfully</span>");
		$("#dynamicPaneAssets").getAjaxHtmlResponce("swi/viewAssetList.action","","","","get");
	} else if(status == "FAILURE"){
		$pubDiv.empty().append("<span style='color:red' >Dataset Deletion Failed, Please try again later</span>");
		$("#showDelete"+assetId).show();
	}
}





function closeBox(){
	$("#hiddenPane").hide();	
}
function validateAssetUnderMaintenance(assetId) {
     $("#showDelete"+assetId+"Link").hide();
      $("#loadingShowDelete"+assetId+"Link").show();
     	
      $.get("swi/validateAssetUnderMaintenance.action?asset.id=" + assetId , function(data){ 
       if(data=="false"){            
             deleteDataset(assetId);   
         }else{
           $("#hiddenPane").fadeIn("fast").css("left",boxLeft+"px").css("top",boxTop+"px");      
           $("#assetGrainLink").show();  
	       $("#assetGrainLinkLoader").hide();
           $("#hiddenPaneContent").empty().append(data);
		   $("#boxTitleDiv").empty().append("<s:text name='execue.asset.operation.status.heading'/>");
           $("#hiddenPaneContent").width(400); 
           $("#showDelete"+assetId+"Link").show();  
            $("#loadingShowDelete"+assetId+"Link").hide();
         }
        });
}  
</script>

