<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/thickbox.css" type="text/css"
	media="screen" />

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/jquery.execue.js"></script>
<script language="JavaScript" src="../js/jquery.execue.jobStatus.js"></script>
<script language="JavaScript" src="../js/menun.js"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/jquery.layout.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/execue.commons.js"></script>
<script type="text/javascript" src="../js/jquery.execue.htmlResponse.js"></script>

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
	requestedPage=$("#requestedPageHiddenField").val();
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


function deleteDataset(assetId){
	confirmed = confirm("Are you sure you want to delete ?");
	if (confirmed) {
		$("#showDelete"+assetId+"Link").hide();
		$("#showStatus"+assetId).empty().show();
		$("#showStatus"+assetId).jobStatus({
				requestJobURL : "../swi/deleteAsset.action?asset.id=" + assetId,
				postDataForJobParam : function(){var data = {}; return data;},
				postCall : function(jobId,status) {updateDeleteInfo(jobId,status,assetId)}
		});
	}
}

function updateDeleteInfo(jobId,status,assetId) {
	$pubDiv = $("#showStatus"+assetId);
	$pubDiv.show();
	if(status == "SUCCESS"){
		$pubDiv.empty().append("<span style='color:green' >Dataset Deleted Sucessfully</span>");
	} else if(status == "FAILURE"){
		$pubDiv.empty().append("<span style='color:red' >Dataset Deletion Failed, Please try again later</span>");
		$("#showDelete"+assetId).show();
	}
}
</script>

